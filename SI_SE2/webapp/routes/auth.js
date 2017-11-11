const debug = require('debug')('webapp:auth')
const express = require('express')
const router = express.Router()
const query = require('querystring')
const global = require('../data/global')
const authService = require('../data/service/authService')

router.get('/', function(req, res) {
	if( res.locals.user )
		res.redirect('/home')
	res.render('login')
})

router.get('/github', function(req, res) {
	res.statusCode = 302
	let queryString = query.stringify({
		scope: 'repo',
		redirect_uri: global.Github_Redirect_URI,
		state: req.csrfToken(),
		client_id: global.Github_ClientID
	})
	res.set({
		Location: 'https://github.com/login/oauth/authorize?' + queryString,
		'set-cookie': 'state=' + req.csrfToken()
	})
	res.end()
})

router.get(
	'/github/callback',
	checkState,
	function(req, res, next) {
		const code = req.query['code']
		console.log('Authorization code is = ' + code)
		authService.postForGithubToken(code, (err, data) => {
			if( err )
				return next(err)
			req.app.locals.github_token = data.access_token
			res.redirect('/github/repos/private')
		})
	}
)

router.get('/google/', function(req, res) {
	res.statusCode = 302
	let queryString = query.stringify({
		scope: 'openid https://www.googleapis.com/auth/calendar email profile',
		redirect_uri: global.Google_Redirect_URI,
		response_type: 'code',
		state: req.csrfToken(),
		client_id: global.Google_ClientID,
		prompt: 'consent'
	})
	res.set({
		Location: 'https://accounts.google.com/o/oauth2/v2/auth?' + queryString,
		'set-cookie': 'state=' + req.csrfToken()
	})
	res.end()
})

router.get(
	'/google/callback',
	checkState,
	function(req, res, next) {
		const code = req.query['code']
		debug('Authorization code is ' + code)

		authService.postForGoogleToken(code, (err, user) => {
			if( err )
				return next(err)
			req.app.locals.user = user
			res.cookie('google_id', user.access_token)
			res.redirect('/home')
		})
	}
)

function checkState(req, res, next) {
	if( req.query.state !== req.cookies.state ) {
		let err = new Error('state mismatch!')
		err.statusCode = 401
		return next(err)
	}
	next()
}

module.exports = router