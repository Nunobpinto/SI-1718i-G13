const express = require('express')
const router = express.Router()
const global = require('../data/global')
const query = require('querystring')
const request = require('request')

router.get('/', function(req, res) {
	res.render('home')
})

router.get('/login/google', function(req, res) {
	res.statusCode = 302
	let queryString = query.stringify({
		scope: 'openid email',
		redirect_uri: global.Google_Redirect_URI,
		response_type: 'code',
		client_id: global.Google_ClientID,
		prompt: 'consent'
	})
	res.set({Location: 'https://accounts.google.com/o/oauth2/v2/auth?' + queryString})
	res.end()
})

router.get('/google/callback', function(req, res) {
	const code = req.query['code']
	console.log('Authorization code is = ' + code)

	const params = {
		code: code,
		client_id: global.Google_ClientID,
		client_secret: global.Google_ClientSecret,
		redirect_uri: global.Google_Redirect_URI,
		state: req.csrfToken(),
		grant_type: 'authorization_code'
	}

	request.post(
		'https://www.googleapis.com/oauth2/v4/token',
		{json: true, form: params},
		function(err, resp, data) {
			//TODO: do stuff
		}
	)
})

router.get('/login/github',function (req, res) {
	res.statusCode = 302
	let queryString = query.stringify({
		scope: 'repo',
		redirect_uri: global.Github_Redirect_URI,
		state: req.csrfToken(),
		client_id: global.Github_ClientID
	})
	res.set({Location: 'https://github.com/login/oauth/authorize?' + queryString})
	res.end()
})

router.get('/github/callback', function(req, res) {
	const code = req.query['code']
	const state = req.query['state']
	console.log('Authorization code is = ' + code)

	const params = {
		code: code,
		client_id: global.Github_ClientID,
		client_secret: global.Github_ClientSecret,
		redirect_uri: global.Github_Redirect_URI,
		state: state
	}

	request.post(
		'https://github.com/login/oauth/access_token',
		{json: true, form: params},
		function(err, resp, data) {
			//TODO: do stuff
		}
	)
})

module.exports = router