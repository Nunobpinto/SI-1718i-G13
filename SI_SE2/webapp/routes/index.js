'use strict'

const express = require('express')
const router = express.Router()
const validator = require('../routes/validation')

router.get('/', function(req, res) {
	res.redirect('/home')
})

router.get(
	'/home',
	validator.checkGoogleAuth,
	function(req, res) {
		res.render('home')
	}
)

module.exports = router