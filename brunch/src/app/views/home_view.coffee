homeTemplate = require('templates/home')

class exports.HomeView extends Backbone.View
	id: 'home-view'

	render: ->
		window.plugins.StorePlugin.getProducts "productid"
		, ->
			alert "success"
		, ->
			alert "error"
		, ->
			alert "cancel"

		$(@el).html homeTemplate()
		@
