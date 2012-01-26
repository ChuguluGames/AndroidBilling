homeTemplate = require('templates/home')

class exports.HomeView extends Backbone.View
	id: 'home-view'

	render: ->
  	window.plugins.StorePlugin.requestPurchase "test_001"
    	, ->
  			alert "requestPurchase: success"
  		, ->
  			alert "requestPurchase: error"
  		, ->
  			alert "requestPurchase: cancel"

		$(@el).html homeTemplate()
		@
