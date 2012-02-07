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

import com.billing.PurchaseDatabase;

public class Store {
	private static final String TAG = "Store";

	private StorePurchaseObserver mStorePurchaseObserver;
	private Handler mStoreHandler;
	private BillingService mStoreBillingService;
	public Activity mActivity;
	//private PurchaseDatabase mPurchaseDatabase;

	public Boolean billingSupported;

	public Store(Activity context) {
		Log.d(TAG, "begin with activity:" + context);
		mActivity = context;
		mStoreHandler = new Handler();
		mStorePurchaseObserver = new StorePurchaseObserver(context, mStoreHandler);

		mStoreBillingService = new BillingService();
		mStoreBillingService.setContext(context);

        //mPurchaseDatabase = new PurchaseDatabase(context);

		registerPurchaseObserver();
		
		// Check if billing is supported.
		billingSupported = mStoreBillingService.checkBillingSupported();
		if (!billingSupported) {
			Log.d(TAG, "Billing not supported");
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

	// do a payment with the specified producID (sku identifier)
	public boolean requestPurchase(String mSku) {
		Log.d(TAG, "requestPayment with sku: " + mSku);
		
		String mPayloadContents = null; // for debug only

		if (!mStoreBillingService.requestPurchase(mSku, mPayloadContents)) {
			Log.d(TAG, "requestPayment error");	
			return false;
		}
		return true;
	}

	public void getCatalog() {

	}
}