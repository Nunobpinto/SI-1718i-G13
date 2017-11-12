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
	return new Repo(repo.full_name)
}

function mapToMilestone(milestone) {
	if(milestone.created_at === null || milestone.closed_at === null || milestone.due_on === null ){
		return new Milestone(milestone.title, milestone.description, null, null, null)
	}
	return new Milestone(milestone.title, milestone.description, new Date(milestone.created_at), new Date(milestone.closed_at), new Date(milestone.due_on))
}