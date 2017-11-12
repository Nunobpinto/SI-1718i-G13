'use strict'

const request = require('request')
const mapper = require('../mapper')

module.exports = {
	postCalendarAllDayEvent
}

function postCalendarAllDayEvent(access_token, body, cb) {
	const googlePostAllDayEventURI = 'https://www.googleapis.com/calendar/v3/calendars/primary/events'
	const options = {
		url: googlePostAllDayEventURI,
		body: body,
		json: true,
		headers: {
			'User-Agent': 'SecurityWebApp',
			'Authorization': 'Bearer ' + access_token,
		}
	}
	request.post(options, function(err, resp, data) {
		if( err )
			return cb(err)
		return cb(null, data)
	})
}