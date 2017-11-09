const express = require('express')
const router = express.Router()
const global = require('../data/global')
const query = require('querystring')
const request = require('request')

router.get('/', function(req, res) {
	//TODO: check if logged in, go home if true
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
	function(req, res) {
		const code = req.query['code']
		console.log('Authorization code is = ' + code)

		const params = {
			code: code,
			client_id: global.Github_ClientID,
			client_secret: global.Github_ClientSecret,
			redirect_uri: global.Github_Redirect_URI,
			state: req.query['state']
		}

		request.post(
			'https://github.com/login/oauth/access_token',
			{json: true, form: params},
			function(err, resp, data) {
				//TODO: do stuff
			}
		)
	}
)

router.get('/google/', function(req, res) {
	res.statusCode = 302
	let queryString = query.stringify({
		scope: 'openid email',
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

		console.log('Authorization code is = ' + code)

		const params = {
			code: code,
			client_id: global.Google_ClientID,
			client_secret: global.Google_ClientSecret,
			redirect_uri: global.Google_Redirect_URI,
			grant_type: 'authorization_code'
		}

		request.post(
			'https://www.googleapis.com/oauth2/v4/token',
			{json: true, form: params},
			function(err, resp, data) {
				//TODO: create cookie, other authentication stuff
				res.redirect('home')
			}
		)
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