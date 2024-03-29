'use strict'

const express = require('express')
const router = express.Router()
const debug = require('debug')('webapp:googleRoutes')
const googleService = require('../data/service/googleService')
const validator = require('../routes/validation')

router.post(
	'/calendar',
	validator.checkGoogleAuth,
	function(req, res, next) {
		const milestone = req.body.milestone.split('\n')
		debug('Posting all day event to google calendar')
		if( milestone[4].includes('null') ) {
			const currDate = new Date()
			milestone[4] = `${currDate.getFullYear()}-${currDate.getMonth() + 1}-${currDate.getDate()}`
		}
		googleService.postCalendarAllDayEvent(
			res.locals.access_token,
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
				res.redirect(data.htmlLink)
			}
		)
	}
)

module.exports = router