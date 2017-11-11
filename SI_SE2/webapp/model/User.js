'use strict'

module.exports = User

function User(name, picture, email, access_token){
	this.name = name
	this.picture = picture
	this.email = email
	this.access_token = access_token
}