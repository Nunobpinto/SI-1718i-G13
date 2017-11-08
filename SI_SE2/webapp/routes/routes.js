const express = require('express')
const router = express.Router()
const authService = require('../data/service/authService')
const global = require('../data/global')
const query = require('querystring')


router.get('/', function(req, res, next) {
    res.render('home')
})

router.get('/login',function (req,res,next) {
    res.statusCode = 302
    let queryString = query.stringify({
        scope :'openid%20email',
        redirect_uri : global.Google_Redirect_URI ,
        response_type : 'code',
        client_id : global.Google_ClientID,
        prompt : 'consent'
    })
    res.set({
        Location : 'https://accounts.google.com/o/oauth2/v2/auth?' + queryString
    })
})

router.get('/login/callback',function (req,res,next) {
    res.render('home')
})

module.exports = router