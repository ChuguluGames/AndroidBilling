homeTemplate = require('templates/home')

class exports.HomeView extends Backbone.View
	id: 'home-view'

	render: ->
	  alert "requestPurchase: test_001"
  	window.plugins.StorePlugin.requestPurchase "test_001"
    	, ->
  			alert "requestPurchase1\nlaunch: success"
  		, ->
  			alert "requestPurchase1\nlaunch: error"
  		, ->
  			alert "requestPurchase1\nlaunch: cancel"

    alert "requestPurchase: test_002"
    window.plugins.StorePlugin.requestPurchase "test_002"
   	  , ->
  			alert "requestPurchase2\nlaunch: success"
  		, ->
  			alert "requestPurchase2\nlaunch: error"
  		, ->
  			alert "requestPurchase2\nlaunch: cancel"
    	
#    alert "requestPurchase: test_003"		
#    window.plugins.StorePlugin.requestPurchase "test_003"
#      , ->
#        alert "requestPurchase3\nlaunch: success"
#      , ->
#        alert "requestPurchase3\nlaunch: error"
#      , ->
#        alert "requestPurchase3\nlaunch: cancel"
      			  
		$(@el).html homeTemplate()
		@
