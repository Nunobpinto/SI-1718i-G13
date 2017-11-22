'use strict'
const container = require('./idContainer')

module.exports =
	function(req, res, next) {
		if(req.cookies.google_id){
			res.locals.access_token = container.getID(req.cookies.google_id)
		}
		next()
	}
