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
	request(githubPrivateRepositoriesURI,
		{
			headers: {
				'User-Agent': 'SecurityWebApp',
				'Authorization': 'token ' + access_token
			}
		},
		function (err, resp, data) {
			if(err){
				return cb(err)
			}
			data = JSON.parse(data)
			data = data.map(item=>mapper.mapToRepo(item))
			cb(null, data)
		})
}

function getMilestones(access_token,fullName, cb) {
	const githubMilestonesURI = `https://api.github.com/repos/${fullName}/milestones?state=alll`
	request(githubMilestonesURI,
		{
			headers: {
				'User-Agent': 'SecurityWebApp',
				'Authorization': 'token ' + access_token
			}
		},
		function (err, resp, data) {
			if(err || resp.statusCode !== 200){
				return cb(err)
			}
			data = JSON.parse(data)
			data = data.map(item=>mapper.mapToMilestone(item))
			cb(null, data)
		})
}
