'use strict'

const express = require('express')
const router = express.Router()
const debug = require('debug')('webapp:githubRoute')
const githubService = require('../data/service/githubService')
const validator = require('../routes/validation')

router.get(
	'/repos/private',
	validator.checkGoogleAuth,
	function(req, res, next) {
		debug('Fetching private repositories')
		githubService.getPrivateRepositories(
			req.app.locals.github_token,
			(err, data) => {
				if( err )
					return next(err)
				res.render('repositories', {
					message: `${data.user}'s repositories:`,
					list: data.repos
				})
			}
		)
	}
)

router.get(
	'/repos/public',
	validator.checkGoogleAuth,
	function(req, res, next) {
		const keyword = req.query['keyword']
		debug('Fetching public repositories by ' + keyword)
		githubService.getPublicRepositories(
			keyword,
			(err, data) => {
				if( err )
					return next(err)
				res.render('repositories', {
					message: `Results for ${keyword}:`,
					list: data
				})
			}
		)
	}
)

router.get(
	'/repos/:owner/:repo/milestones',
	validator.checkGoogleAuth,
	function(req, res, next) {
		const owner = req.params.owner
		const repo = req.params.repo
		debug(`Fetching milestones from ${owner}/${repo}`)
		githubService.getMilestones(
			req.app.locals.github_token,
			`${owner}/${repo}`,
			(err, data) => {
				if( err )
					return next(err)
				res.render('milestones', {
					csrfToken: req.csrfToken(),
					message: `Milestones for ${repo}`,
					list: data
				})
			}
		)
	}
)

module.exports = router