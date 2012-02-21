package com.phonegap.store;

import com.phonegap.store.StorePurchaseObserver;
import com.phonegap.store.StoreDelegate;
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
    public boolean mbillingSupported;
    private StoreDelegate mDelegate;
    
    public Store(Activity context) {
        mStoreHandler = new Handler();
        mStorePurchaseObserver = new StorePurchaseObserver(context, mStoreHandler);

        mStoreBillingService = new BillingService();
        mStoreBillingService.setContext(context);
        
        runAsynchroniousBillingSupportedRequest();
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
    public Boolean requestPurchase(String mSku) {
        Log.d(TAG, "requestPayment with sku: " + mSku);
                
        String mPayloadContents = null; // for debug only

        if (!mStoreBillingService.requestPurchase(mSku, mPayloadContents)) {
            Log.d(TAG, "requestPayment error");
            return false;
        }
        return true;
    }
    
    /* delegate */
    public void setDelegate(StoreDelegate delegate) {
        mDelegate = delegate;
    }
    
    public StoreDelegate getDelegate() {
        return mDelegate;
    }
    
    /* Billing support */
    private void runAsynchroniousBillingSupportedRequest() {
        // Check if billing is supported.
        mStoreBillingService.checkBillingSupported();
    }

    public void setBillingSupport(boolean supported) {
        Log.d(TAG, "setBillingSupport " + supported);
        mbillingSupported = supported;
    }
    
    public boolean isBillingSupported() {
        return mbillingSupported;
    }
    
    public void getCatalog() {
    }    
}