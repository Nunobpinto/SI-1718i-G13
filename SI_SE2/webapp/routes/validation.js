'use strict'

const container = require('../data/idContainer')
const debug = require('debug')('webapp:validationMiddleware')

module.exports = {
	checkGoogleAuth: function(req, res, next) {
		debug('Validating token ' + req.cookies.google_id)
		const token = container.getID(req.cookies.google_id)
		const user = req.app.locals.user
		if( !token || !user ) {
			debug('Invalid token!!')
			return res.redirect('/login')
		}
		debug('Token successfully validated!')
		res.locals.access_token = token
		res.locals.user = user
		next()
	}
}