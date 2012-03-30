package com.phonegap.store;

import android.util.Log;
import com.billing.BillingService.RequestPurchase;
import com.phonegap.api.PluginResult.Status;
import com.phonegap.api.PluginResult;

import com.phonegap.store.StoreConstants.Errors;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONException;
import org.json.JSONObject;

public class StoreDelegate {
    private static final String TAG = "StoreDelegate";
    
    private StorePlugin mPlugin;
    private String mCallbackID;
    
    public StoreDelegate(StorePlugin plugin, String callbackID) {
        mPlugin = plugin;
        mCallbackID = callbackID;
    }
    
    public void onPurchaseSuccess(RequestPurchase request) {
        Log.d(TAG, "onPurchaseSuccess");
        
        PluginResult result = new PluginResult(Status.OK);
        mPlugin.success(result, mCallbackID);
    }
    
    public void onPurchaseCanceled(RequestPurchase request) {
        Log.d(TAG, "onPurchaseCanceled");
        PluginResult result;
        JSONObject error = new JSONObject();
        try {
            error.put("code", Errors.ON_UNKNOWN_ERROR);
            error.put("request", Errors.ON_UNKNOWN_ERROR);
            result = new PluginResult(Status.ERROR, error);
        } catch (JSONException ex) {
            result = new PluginResult(Status.JSON_EXCEPTION);
        }
        
        mPlugin.error(result, mCallbackID);
    }
    
    public void onPurchaseError(RequestPurchase request) {
        Log.d(TAG, "onPurchaseError");
        PluginResult result;
        JSONObject error = new JSONObject();
        try {
            error.put("code", Errors.ON_UNKNOWN_ERROR);
            error.put("request", Errors.ON_UNKNOWN_ERROR);
            result = new PluginResult(Status.ERROR, error);
        } catch (JSONException ex) {
            result = new PluginResult(Status.JSON_EXCEPTION);
        }
        
        mPlugin.error(result, mCallbackID);
    }    
}
