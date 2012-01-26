homeTemplate = require('templates/home')

class exports.HomeView extends Backbone.View
	id: 'home-view'

	render: ->
		window.plugins.StorePlugin.getProducts "productid"
		  , ->
  			alert "getProducts: success"
  		, ->
  			alert "getProducts: error"
  		, ->
  			alert "getProducts: cancel"
			
  	window.plugins.StorePlugin.requestPayment "test_001"
    	, ->
  			alert "requestPayment: success"
  		, ->
  			alert "requestPayment: error"
  		, ->
  			alert "requestPayment: cancel"

		$(@el).html homeTemplate()
		@
