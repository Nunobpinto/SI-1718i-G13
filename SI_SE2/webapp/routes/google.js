'use strict'

const express = require('express')
const router = express.Router()
const googleService = require('../data/service/googleService')
const validator = require('../routes/validation')

module.exports = router

router.get(
	'/calendar',
	validator.checkGoogleAuth,
	function(req, res, next) {
		const milestone = req.query.milestone.split('\n')
		googleService.postCalendarAllDayEvent(
			req.app.locals.user.access_token,
			{
				summary: milestone[0],
				description: milestone[1],
				start: {
					date: milestone[4]
				},
				end: {
					date: milestone[4]
				}
			},
			function(err, data) {
				if( err )
					next(err)
				//TODO: faz-se mais o que???
				res.redirect(data.htmlLink)
			}
		)
	}
)