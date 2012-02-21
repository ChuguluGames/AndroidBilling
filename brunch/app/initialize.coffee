class exports.Application
	constructor: ->
		@waitForDocumentReady()

	waitForDocumentReady: ->
		console.log "waitForDocumentReady"
		document.addEventListener "deviceready", =>
			@onDeviceReady()
		, false

	onDeviceReady: ->
		console.log "onDeviceReady"
		@initialize()

	initialize: ->
		# android.test.purchased
		# android.test.canceled
		# android.test.refunded
		# android.test.item_unavailable
		window.plugins.Store.initialize ->
			window.plugins.Store.requestPurchase "android.test.purchased", (status) ->
				console.log "purchaseddddddddddddd " + status

window.app = new exports.Application