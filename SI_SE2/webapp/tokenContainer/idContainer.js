'use strict'

const container = new Map()
const crypto = require('crypto')
const key = 'crypt'
const cipher = crypto.createCipher('aes-256-cbc', key)

function addToken(id) {
	cipher.update(id, 'utf8', 'base64')
	let value = cipher.final('base64')
	container.set(value,id)
	return value
}

function getID(key) {
	return container.get(key)
}

function deleteIDPair(key) {
	container.delete(key)
}

module.exports = {
	addToken,
	getID,
	deleteIDPair
}