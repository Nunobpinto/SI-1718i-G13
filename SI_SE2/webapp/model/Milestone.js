'use strict'

module.exports = Milestone

function Milestone(title,description,created,closed) {
	this.title = title
	this.description = description
	this.created = created
	this.closed = closed
}