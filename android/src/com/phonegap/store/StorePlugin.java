package com.phonegap.store;

import com.phonegap.store.Store;
import com.phonegap.store.StoreDelegate;
import com.phonegap.androidbilling.AndroidBilling;

import java.util.List;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.util.Log;

import com.phonegap.api.Plugin;
import com.phonegap.api.PluginResult;
import com.phonegap.api.PluginResult.Status;

public class StorePlugin extends Plugin {
    private static final String TAG = "StorePlugin";
    
    public static final String CMD_INITIALIZE = "initialize";
    public static final String CMD_REQUEST_PAYMENT = "requestPurchase";
    public static final String CMD_GET_PRODUCTS = "getProducts";

    /*
     * Executes the request and returns PluginResult.
     *
     * @param action action to perform.
     * @param data input data.
     * @param callbackId The callback id used when calling back into JavaScript.
     * @return A PluginResult object with a status and message.
     *
     * org.json.JSONArray, java.lang.String)
     */
    @Override
    public PluginResult execute(String action, JSONArray argumentsArray, String callbackId) {
        Log.d(TAG, "Plugin Called");

        Store store = getStore();
        
        // check if billing is supported    
        if (!store.isBillingSupported()) {
             return new PluginResult(Status.ERROR);
        } else {
            
            if (action.equals(CMD_INITIALIZE)) {
                store.setDelegate(new StoreDelegate(this, callbackId));    
                return new PluginResult(Status.OK);
                
            } else if (action.equals(CMD_REQUEST_PAYMENT)) {
                Log.d(TAG, "Execute " + CMD_REQUEST_PAYMENT);

                JSONObject argumentsObject;
                try {
                    argumentsObject = argumentsArray.getJSONObject(0);
                    String productID = argumentsObject.getString("productID");
                    store.requestPurchase(productID);

                } catch (JSONException ex) {
                    Logger.getLogger(StorePlugin.class.getName()).log(Level.SEVERE, null, ex);
                    return new PluginResult(Status.JSON_EXCEPTION);
                }

            } else if (action.equals(CMD_GET_PRODUCTS)) {
                Log.d(TAG, "Execute " + CMD_GET_PRODUCTS);
                this.getProducts();
                return new PluginResult(Status.OK);

            } else {
                Log.e(TAG, "Invalid action : " + action + " passed");
                return new PluginResult(Status.INVALID_ACTION);
            }
        }
        
        return null;
    }

	private void getProducts() {
    }
        
    private Store getStore() {
        return (Store) ((AndroidBilling) this.ctx).store;
    }
}