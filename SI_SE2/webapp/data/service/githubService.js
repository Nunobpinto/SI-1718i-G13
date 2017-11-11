'use strict'

const request = require('request')
const mapper = require('../mapper')
const global = require('../global')

module.exports = {
	getPublicRepositories,
	getPrivateRepositories,
	getMilestones
}

function getPublicRepositories(keyword, cb) {
	const githubPublicRepositoriesURI = `https://api.github.com/search/repositories?q=${keyword}`
	request(githubPublicRepositoriesURI,
		{
			headers: {
				'User-Agent': 'SecurityWebApp'
			}
		},
		function (err, resp, data) {
			if(err){
				return cb(err)
			}
			data = JSON.parse(data)
			data = data.items.map(item=>mapper.mapToRepo(item))
			cb(null, data)
		})
}

function getPrivateRepositories(access_token,cb) {
	const githubPrivateRepositoriesURI = 'https://api.github.com/user/repos?visibility=private'
	const headers = {
		headers: {
			'User-Agent': 'SecurityWebApp',
			'Authorization': 'token ' + access_token
		}
	}
	request(githubPrivateRepositoriesURI,
		headers,
		function (err, resp, data) {
			if(err){
				return cb(err)
			}
			data = JSON.parse(data)
			data = data.map(item=>mapper.mapToRepo(item))
			request('https://api.github.com/user',headers,function (err, resp, body) {
				if(err){
					return cb(err)
				}
				body = JSON.parse(body)
				cb(null, {user : body.login, repos: data})
			})
		})
}

function getMilestones(access_token,fullName, cb) {
	const githubMilestonesURI = `https://api.github.com/repos/${fullName}/milestones?state=all`
	request(githubMilestonesURI,
		{
			headers: {
				'User-Agent': 'SecurityWebApp',
				'Authorization': 'token ' + access_token
			}
		},
		function (err, resp, data) {
			if(err || resp.statusCode !== 200){
				return cb({ message: 'Something broke!', statusCode: (resp ? resp.statusCode : 500) } )
			}
			data = JSON.parse(data)
			data = data.map(item=>mapper.mapToMilestone(item))
			cb(null, data)
		})
}
