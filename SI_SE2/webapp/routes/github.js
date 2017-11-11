const debug = require('debug')('webapp:auth')
const express = require('express')
const router = express.Router()
const query = require('querystring')
const global = require('../data/global')
const githubService = require('../data/service/githubService')

module.exports = router

router.get('/repos/private', function (req, res, next) {
    githubService
        .getPrivateRepositories(req.app.locals.github_token,
            (err, data) => {
                if (err) {
                    return next(err)
                }
                res.render('repositories', {
                    message: `Private Repositories of ${data.user}:`,
                    list: data.repos
                })
            })
})

router.get('/repos/public', function (req, res, next) {
    let keyword = req.query['keyword']
    githubService
        .getPublicRepositories(keyword, (err, data) => {
            if (err) {
                return next(err)
            }
            res.render('repositories', {
                message: `Results for ${keyword}:`,
                list: data
            })
        })
})

router.get('/repos/:owner/:repo/milestones', function (req, res, next) {
    let owner = req.params.owner
    let repo = req.params.repo
    repo = owner + '/' + repo
    githubService
        .getMilestones(
            req.app.locals.github_token,
            repo,
            (err, data) => {
                if (err)
                    return next(err)
                res.render('milestones', {
                    message: `Milestones for ${repo}`,
                    list: data
                })
            }
        )
})