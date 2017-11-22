'use strict'

const container = new Map()
const shortid = require('shortid')

function addToken(id) {
	let value = shortid.generate()
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