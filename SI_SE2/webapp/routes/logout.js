'use strict'

const express = require('express')
const router = express.Router()
const debug = require('debug')('webapp:logout')
const container = require('../data/idContainer')

router.get('/',function (req, res) {
	debug('Logging out user ' + req.app.locals.user.name)
	if(req.app.locals.github_token)
		delete req.app.locals.github_token
	if(req.app.locals.user){
		container.deleteIDPair(req.cookies.google_id)
		delete req.app.locals.user
	}
	res.redirect('/')
})

module.exports = router