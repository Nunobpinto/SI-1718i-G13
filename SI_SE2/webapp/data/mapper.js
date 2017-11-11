'use strict'

const Milestone = require('../model/Milestone')
const Repo = require('../model/Repo')
const User = require('../model/user')

module.exports = {
	mapToUser,
	mapToRepo,
	mapToMilestone
}

function mapToUser(user, access_token) {
	return new User(user.name, user.picture, user.email, access_token)
}

function mapToRepo(repo) {
	return new Repo(repo.fullName)
}

function mapToMilestone(milestone) {
	return new Milestone(milestone.title, milestone.description, milestone.created, milestone.closed)
}