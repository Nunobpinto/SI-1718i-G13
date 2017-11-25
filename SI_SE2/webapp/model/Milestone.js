'use strict'

function Milestone(title,description,created,closed,due) {
	this.title = title
	this.description = description === '' ? 'No description' : description
	this.created = getSimpleDate(created)
	this.closed = getSimpleDate(closed)
	this.due = getSimpleDate(due)
	this.toString = function() {
		return `${this.title}\n${this.description}\n${this.created}\n${this.closed}\n${this.due}`
	}
}

function getSimpleDate(date) {
	return date === null ? null : date.split('T', 1).pop()
}

module.exports = Milestone