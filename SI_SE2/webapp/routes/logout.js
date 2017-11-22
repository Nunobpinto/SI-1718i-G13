'use strict'

const express = require('express')
const router = express.Router()
const container = require('../data/idContainer')

module.exports = router

router.get('/',function (req, res, next) {
	if(req.app.locals.github_token)
		delete req.app.locals.github_token
	if(req.app.locals.user){
		container.deleteIDPair(req.cookies.google_id)
		delete req.app.locals.user
	}
	res.redirect('/')
})