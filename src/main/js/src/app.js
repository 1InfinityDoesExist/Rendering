const Vue = require('vue');

module.exports = function createApp (template, data) {
	return new Vue({
		"data": data, 
		"template": template
	});
};
