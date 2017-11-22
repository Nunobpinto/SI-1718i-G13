'use strict'

const container = require('../data/idContainer')

module.exports = {
	checkGoogleAuth: function(req, res, next) {
		const token = container.getID(req.cookies.google_id)
		const user = req.app.locals.user
		if( !token || !user )
			return res.redirect('/login')
		res.locals.access_token = token
		res.locals.user = user
		next()
	}
}