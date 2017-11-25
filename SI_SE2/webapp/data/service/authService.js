'use strict'

const request = require('request')
const mapper = require('../mapper')
const global = require('../global')

function postForGoogleToken(code, cb) {
	const googleAPIPostPath = 'https://www.googleapis.com/oauth2/v4/token'
	const params = {
		code: code,
		client_id: global.Google_ClientID,
		client_secret: global.Google_ClientSecret,
		redirect_uri: global.Google_Redirect_URI,
		grant_type: 'authorization_code'
	}

	request.post(
		googleAPIPostPath,
		{json: true, form: params},
		function(err, resp, data) {
			if( err )
				return cb(err)
			let id_tokenDecoded = JSON.parse(Buffer.from(data.id_token.split('.')[1], 'base64').toString())
			cb(null, mapper.mapToUser(id_tokenDecoded), data.access_token)
		}
	)
}

function postForGithubToken(code, cb) {
	const githubAPIPostPath = 'https://github.com/login/oauth/access_token'
	const params = {
		code: code,
		client_id: global.Github_ClientID,
		client_secret: global.Github_ClientSecret,
		redirect_uri: global.Github_Redirect_URI,
	}

	request.post(
		githubAPIPostPath,
		{json: true, form: params},
		function(err, resp, data) {
			if( err )
				return cb(err)
			cb(null, data)
		}
	)
}

module.exports = {
	postForGoogleToken,
	postForGithubToken
}