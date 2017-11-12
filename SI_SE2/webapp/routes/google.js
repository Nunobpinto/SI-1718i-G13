'use strict'

const express = require('express')
const router = express.Router()
const googleService = require('../data/service/googleService')
const validator = require('../routes/validation')

module.exports = router

router.post(
	'/calendar',
	validator.checkGoogleAuth,
	function(req, res, next) {
		const milestone = JSON.parse(req.body.milestone)
		googleService.postCalendarAllDayEvent(
			req.app.locals.user.access_token,
			milestone,
			function(err, cb) {
				//TODO: send post to google, show event
			}
		)
	}
)