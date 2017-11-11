const debug = require('debug')('webapp:auth')
const express = require('express')
const router = express.Router()
const query = require('querystring')
const global = require('../data/global')
const githubService = require('../data/service/githubService')

module.exports = router

router.get('/repos/private',function (req, res, next) {
	githubService
		.getPrivateRepositories(req.app.locals.github_token,
			(err,data)=>{
				if(err){
					return next(err)
				}
				res.render('repositories',{
					message : `Private Repositories of ${req.app.locals.user.name}`,
					list : data
				})
			})
})

router.get('/repos/public',function (req, res, next) {
	let keyword = req.query['keyword']
	githubService
		.getPublicRepositories(keyword,(err,data)=>{
			if(err){
				return next(err)
			}
			res.render('repositories',{
				message : `Results for ${keyword} `,
				list : data
			})
		})
})

router.get('/repos/:repo/milestones',function (req, res, next) {
	let repo = req.query['repo']
	githubService
		.getMilestones(repo,(err,data)=>{
			if(err){
				return next(err)
			}
			res.render('milestones',{
				message : `Milestones for ${repo}`,
				list : data
			})
		})
})