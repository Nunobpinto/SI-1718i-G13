'use strict'

const Milestone = require('../model/Milestone')
const Repo = require('../model/Repo')
const User = require('../model/user')

function mapToUser(user) {
	return new User(user.name, user.picture, user.email)
}

function mapToRepo(repo) {
	return new Repo(repo.full_name)
}

function mapToMilestone(milestone) {
	return new Milestone(milestone.title, milestone.description, milestone.created_at, milestone.closed_at, milestone.due_on)
}

module.exports = {
	mapToUser,
	mapToRepo,
	mapToMilestone
}