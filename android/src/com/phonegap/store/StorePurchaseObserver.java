package com.phonegap.store;

import com.phonegap.androidbilling.AndroidBilling;

import com.billing.BillingService.RequestPurchase;
import com.billing.BillingService.RestoreTransactions;
import com.billing.PurchaseObserver;
import com.billing.Consts.PurchaseState;
import com.billing.Consts.ResponseCode;

import android.app.Activity;
import android.os.Handler;

import android.util.Log;

public class StorePurchaseObserver extends PurchaseObserver {

    private static final String TAG = "StorePurchaseObserver";
    
    private Activity mContext;
    
    public StorePurchaseObserver(Activity context, Handler handler) {
        super(context, handler);
        Log.d(TAG, "StorePurchaseObserver");
        mContext = context;
    }

    @Override
    public void onBillingSupported(boolean supported) {
        Log.i(TAG, "supported: " + supported);
        
        Store store = getStore();
        store.setBillingSupport(supported);
    }

    @Override
    public void onPurchaseStateChange(PurchaseState purchaseState, String itemId, int quantity, long purchaseTime, String developerPayload) {
        Log.i(TAG, "onPurchaseStateChange() itemId: " + itemId + " " + purchaseState);
        
        if (purchaseState == PurchaseState.PURCHASED) {
        }
    }

    @Override
    public void onRequestPurchaseResponse(RequestPurchase request, ResponseCode responseCode) {
        Store store = getStore();
        
        if (responseCode == ResponseCode.RESULT_OK) {
            Log.i(TAG, "purchase was successfully sent to server");
            store.getDelegate().onPurchaseSuccess(request);
        } else if (responseCode == ResponseCode.RESULT_USER_CANCELED) {
            Log.i(TAG, "user canceled purchase");
            store.getDelegate().onPurchaseCanceled(request);
        } else {
            Log.i(TAG, "purchase failed");
            store.getDelegate().onPurchaseError(request);
        }
    }

    @Override
    public void onRestoreTransactionsResponse(RestoreTransactions request, ResponseCode responseCode) {
        if (responseCode == ResponseCode.RESULT_OK) {
            Log.d(TAG, "completed RestoreTransactions request");
        } else {
            Log.d(TAG, "RestoreTransactions error: " + responseCode);
        }
    }
    
    private Store getStore() {
        return (Store) ((AndroidBilling) mContext).store;
    }    
}