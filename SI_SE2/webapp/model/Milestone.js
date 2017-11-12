'use strict'

module.exports = Milestone

function Milestone(title,description,created,closed,due) {
	this.title = title
	this.description = description
	this.created = created
	this.closed = closed
	this.due = due
	this.toString = function() {
		return `${title}\n${description}\n${getSimpleDate(created)}\n${getSimpleDate(closed)}\n${getSimpleDate(due)}`
	}
}

function getSimpleDate(date) {
	if(date === null) return getCurrentDateTime()
	return date.getFullYear() + '-' + date.getMonth() + '-' + date.getDate()
}

function getCurrentDateTime() {
	const date = new Date()
	let year = date.getFullYear()
	let month = date.getMonth() + 1
	month = (month < 10 ? '0' : '') + month
	let day  = date.getDate()
	day = (day < 10 ? '0' : '') + day
	return year + '-' + month + '-' + day
}