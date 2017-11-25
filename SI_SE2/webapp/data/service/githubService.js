'use strict'

const request = require('request')
const mapper = require('../mapper')

function getPublicRepositories(keyword, cb) {
	const githubPublicRepositoriesURI = `https://api.github.com/search/repositories?q=${keyword}`
	request(githubPublicRepositoriesURI,
		{ headers: { 'User-Agent': 'SecurityWebApp' } },
		function(err, resp, data) {
			if( err )
				return cb(err)
			const jsonRepos = JSON.parse(data)
			const repos = jsonRepos.items.map(item => mapper.mapToRepo(item))
			cb(null, repos)
		}
	)
}

function getPrivateRepositories(access_token, cb) {
	const githubPrivateRepositoriesURI = 'https://api.github.com/user/repos?visibility=private'
	const githubUserInfoURI = 'https://api.github.com/user'
	const headers = {
		headers: {
			'User-Agent': 'SecurityWebApp',
			'Authorization': `token ${access_token}`
		}
	}
	request(githubPrivateRepositoriesURI,
		headers,
		function(err, resp, data) {
			if( err )
				return cb(err)
			const jsonRepos = JSON.parse(data)
			const repos = jsonRepos.map(item => mapper.mapToRepo(item))
			request(githubUserInfoURI,
				headers,
				function(err, resp, data) {
					if( err )
						return cb(err)
					const jsonUser = JSON.parse(data)
					cb(null, {user: jsonUser.login, repos: repos})
				}
			)
		}
	)
}

function getMilestones(access_token, fullName, cb) {
	let headers = {'User-Agent': 'SecurityWebApp'}
	if(access_token)
		headers['Authorization'] = `token ${access_token}`
	const githubMilestonesURI = `https://api.github.com/repos/${fullName}/milestones?state=all`
	request(githubMilestonesURI,
		{headers},
		function(err, resp, data) {
			if( err )
				return cb(err)
			const jsonMilestones = JSON.parse(data)
			const milestones = jsonMilestones.map(item => mapper.mapToMilestone(item)).filter( milestone => milestone.closed === null )
			cb(null, milestones)
		})
}

module.exports = {
	getPublicRepositories,
	getPrivateRepositories,
	getMilestones
}