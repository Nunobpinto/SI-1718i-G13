const express = require('express')
const router = express.Router()
const authService = require('../data/service/authService')
const global = require('../data/global')
const query = require('querystring')
const request = require('request')

router.get('/', function(req, res) {
	res.render('home')
})

router.get('/google', function(req, res) {
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

module.exports = router