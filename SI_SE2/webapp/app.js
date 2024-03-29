const express = require('express')
const path = require('path')
const favicon = require('serve-favicon')
const logger = require('morgan')
const cookieParser = require('cookie-parser')
const bodyParser = require('body-parser')
const hbs = require('hbs')
const csrf = require('csurf')
const index = require('./routes/index')
const authRoutes = require('./routes/auth')
const githubRoutes = require('./routes/github')
const googleRoutes = require('./routes/google')
const logoutRoute = require('./routes/logout')

const app = express()

// view engine setup
app.set('views', path.join(__dirname, 'views'))
app.set('view engine', 'hbs')
hbs.registerPartials(__dirname + '/views/partials')

app.use(favicon(path.join(__dirname, 'public', 'favicon.ico')))
app.use(logger('dev'))
app.use(bodyParser.json())
app.use(bodyParser.urlencoded({extended: false}))
app.use(cookieParser())
app.use(csrf({cookie: true}))
app.use(express.static(path.join(__dirname, 'public')))

app.use(index)
app.use('/login', authRoutes)
app.use('/github', githubRoutes)
app.use('/google', googleRoutes)
app.use('/logout',logoutRoute)

// catch 404 and forward to error handler
app.use(function(req, res, next) {
	let err = new Error('Not Found')
	err.status = 404
	next(err)
})

// error handler
app.use(function(err, req, res, next) {
	// set locals, only providing error in development
	res.locals.message = err.message
	res.locals.error = req.app.get('env') === 'development' ? err : {}

	// render the error page
	res.status(err.status || 500)
	res.render('error')
})

module.exports = app