'use strict'

const express = require('express')
const router = express.Router()

module.exports = router

router.get('/',function (req, res, next) {
	if(req.app.locals.github_token)
		delete req.app.locals.github_token
	if(req.app.locals.user)
		delete req.app.locals.user
	res.redirect('/')
})