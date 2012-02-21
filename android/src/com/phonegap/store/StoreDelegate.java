package com.phonegap.store;

import android.util.Log;
import com.phonegap.api.PluginResult.Status;
import com.phonegap.api.PluginResult;

import com.phonegap.store.StoreConstants.Errors;

public class StoreDelegate {
    private static final String TAG = "StoreDelegate";
    
    private StorePlugin mPlugin;
    private String mCallbackID;
    
    public StoreDelegate(StorePlugin plugin, String callbackID) {
        mPlugin = plugin;
        mCallbackID = callbackID;
    }
    
    public void onPurchaseSuccess() {
        Log.d(TAG, "onPurchaseSuccess");
        PluginResult result = new PluginResult(Status.OK);
        mPlugin.success(result, mCallbackID);
    }
    
    public void onPurchaseCanceled() {
        Log.d(TAG, "onPurchaseCanceled");
        PluginResult result = new PluginResult(Status.ERROR);
        mPlugin.success(result, mCallbackID);
    }
    
    public void onPurchaseError() {
        Log.d(TAG, "onPurchaseError");
        PluginResult result = new PluginResult(Status.ERROR);
        mPlugin.success(result, mCallbackID);
    }    
}
