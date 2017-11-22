'use strict'


module.exports = {
	checkGoogleAuth: function(req, res, next) {
		const user = req.app.locals.user
		if( !user )
			return res.redirect('/login')
		/*
		if( res.locals.access_token !== req.cookies.google_id)
			return res.redirect('/login')
		*/
		res.locals.user = user
		next()
	}
}