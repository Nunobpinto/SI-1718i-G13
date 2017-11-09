const express = require('express')
const router = express.Router()

router.get('/', function(req, res) {
	//TODO: to home if logged in, to login if not
})

router.get('/home', function(req, res) {
	//TODO: check if logged in, go to login if false
	res.render('home')
})

module.exports = router