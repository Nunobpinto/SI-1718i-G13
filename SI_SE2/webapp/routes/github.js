'use strict'

const express = require('express')
const router = express.Router()
const githubService = require('../data/service/githubService')
const validator = require('../routes/validation')

module.exports = router

router.get(
	'/repos/private',
	validator.checkGoogleAuth,
	function(req, res, next) {
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
		githubService.getMilestones(
			req.app.locals.github_token,
			owner + '/' + repo,
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