'use strict'

const debug = require('debug')('webapp:authRoute')
const express = require('express')
const router = express.Router()
const query = require('querystring')
const global = require('../data/global')
const authService = require('../data/service/authService')
const idContainer = require('../data/idContainer')

router.get('/', function(req, res) {
	if( res.app.locals.user )
		res.redirect('/home')
	res.render('login')
})

router.get('/github', function(req, res) {
	debug('Received authentication request to github')
	const token = req.csrfToken()
	let queryString = query.stringify({
		scope: 'repo user',
		redirect_uri: global.Github_Redirect_URI,
		state: token,
		client_id: global.Github_ClientID
	})
	res.set({'set-cookie': 'state=' + token})
	res.redirect(302, 'https://github.com/login/oauth/authorize?' + queryString)
})

router.get(
	'/github/callback',
	checkState,
	function(req, res, next) {
		const code = req.query['code']
		debug('Received response from github with auth code ' + code)
		authService.postForGithubToken(code, (err, data) => {
			if( err ) {
				debug('Failed to exchanged code for github token')
				return next(err)
			}
			debug('Successfully exchanged code for github token')
			req.app.locals.github_token = data.access_token
			res.redirect('/github/repos/private')
		})
	}
)

router.get('/google/', function(req, res) {
	debug('Received authentication request to google')
	const token = req.csrfToken()
	let queryString = query.stringify({
		scope: 'openid https://www.googleapis.com/auth/calendar email profile',
		redirect_uri: global.Google_Redirect_URI,
		response_type: 'code',
		state: token,
		client_id: global.Google_ClientID,
		prompt: 'consent'
	})
	res.set({'set-cookie': 'state=' + token})
	res.redirect(302, 'https://accounts.google.com/o/oauth2/v2/auth?' + queryString)
})

router.get(
	'/google/callback',
	checkState,
	function(req, res, next) {
		const code = req.query['code']
		debug('Received response from google with auth code ' + code)
		authService.postForGoogleToken(code, (err, user,token) => {
			if( err ) {
				debug('Failed to exchanged code for google token')
				return next(err)
			}
			debug('Successfully exchanged code for google token')
			req.app.locals.user = user
			res.cookie('google_id', idContainer.addToken(token))
			res.redirect('/home')
		})
	}
)

function checkState(req, res, next) {
	if( req.query.state !== req.cookies.state ) {
		debug('Failed to validate csrfToken!!')
		delete req.cookies.state
		let err = new Error('state mismatch!')
		err.statusCode = 401
		return next(err)
	}
	debug('csrfToken validated')
	delete req.cookies.state
	next()
}

module.exports = router