package com.phonegap.store;

import com.phonegap.store.StorePurchaseObserver;
import com.billing.BillingService;
import com.billing.ResponseHandler;

import android.content.Context;
import android.content.Intent;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

public class Store {
	private static final String TAG = "Store";

	private StorePurchaseObserver mStorePurchaseObserver;
	private Handler mStoreHandler;
	private BillingService mStoreBillingService;

	public Boolean billingSupported;

	public Store(Activity context) {
		mStoreHandler = new Handler();
		mStorePurchaseObserver = new StorePurchaseObserver(context, mStoreHandler);

		mStoreBillingService = new BillingService();
		mStoreBillingService.setContext(context);

		// Check if billing is supported.
		if (!mStoreBillingService.checkBillingSupported()) {
			Log.d(TAG, "Billing not supported");
			billingSupported = false;
		} else {
			billingSupported = true;
		}
	}

	public void registerPurchaseObserver() {
		ResponseHandler.register(mStorePurchaseObserver);
	}

	public void unregisterPurchaseObserver() {
		ResponseHandler.unregister(mStorePurchaseObserver);
	}

	public void unbindBillingService() {
		mStoreBillingService.unbind();
	}

	public void requestPurchase() {
    // if (!mBillingService.requestPurchase(mSku, mPayloadContents)) {

    // }
	}

	public void getCatalog() {

	}
}