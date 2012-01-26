var Store = function() {};
Store.prototype.errors = [
	{CANT_GET_PRODUCTS_LIST_ON_SIMULATOR:  "Impossible de récupérer les produits dans le simulateur"},
	{CANT_GET_PRODUCTS_LIST:               "Impossible de récupérer les produits"},
	{NOT_CONNECTED:                        "Vous devez etre connecté à internet"},
	{MISSING_PRODUCT_ID:                   "L'ID du produit est manquant"},
	{ON_UNAUTHORIZE_PAYMENT:               "Le paiement n'est pas autorisé"},
	{ON_INVALID_PAYMENT:                   "Le paiement est invalide"},
	{ON_UNKNOWN_ERROR:                     "Une erreur inconnue est survenue"}
];

Store.prototype.generateErrorFromCode = function(code) {
	var self = this,
			error = self.errors[code];

	if (typeof error == undefined || !error)
		return false;

	for (name in error) break;

	return error[name];
};

Store.prototype.requestPurchase = function(productID, success, fail, cancel) {
	var self = this;

	return PhoneGap.exec(success, function(error) {
		if (error.code == -1)
			cancel()
		else
			fail(self.generateErrorFromCode(error.code), error.code)
	}, "StorePlugin", "requestPurchase", [{productID: productID}]);
};

Store.prototype.getProducts = function(params, success, fail) {
	var self = this;

	return PhoneGap.exec(success, function(error) {
		fail(self.generateErrorFromCode(error.code), error.code)
	}, 'StorePlugin', 'getProducts', []);
};

PhoneGap.addConstructor(function() {
	PhoneGap.addPlugin('StorePlugin', new Store());
});