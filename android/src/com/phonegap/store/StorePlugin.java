package com.phonegap.store;

import com.phonegap.store.Store;
import com.phonegap.androidbilling.AndroidBilling;
import com.phonegap.api.Plugin;
import com.phonegap.api.PluginResult;
import com.phonegap.api.PluginResult.Status;

import java.util.List;
import java.util.HashSet;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.util.Log;
import android.os.Handler;
import android.database.Cursor;
import android.content.Context;
import android.content.SharedPreferences;
 
import android.app.AlertDialog;

import android.widget.Toast;

import com.billing.BillingService;
import com.billing.BillingService.RequestPurchase;
import com.billing.BillingService.RestoreTransactions;
import com.billing.ResponseHandler;

import com.billing.Consts;
import com.billing.Consts.ResponseCode;
import com.billing.PurchaseObserver;
import com.billing.PurchaseDatabase;
import com.billing.Consts.PurchaseState;

public class StorePlugin extends Plugin {
	private static final String TAG = "StorePlugin";

	public static final String CMD_REQUEST_PURCHASE = "requestPurchase";
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
	public PluginResult execute(String action, JSONArray arguments, String callbackId) {
		Log.d(TAG, "Plugin Called");

		PluginResult result = null;

		if (action.equals(CMD_REQUEST_PURCHASE)) {
			Log.d(TAG, "Execute requestPurchase:" + arguments);
			
			if (arguments.length() == 1) {
				try {        
					JSONObject jElement = arguments.getJSONObject(0);
		            String response = jElement.getString("productID");
					getStore().requestPurchase(response);
					//this.requestPayment(response);
				}
				catch (JSONException e) {
		            Log.e(TAG, "JSON exception: ", e);
					return new PluginResult( Status.JSON_EXCEPTION );
				}

				result = new PluginResult( Status.OK );		
			}
			else {
				result = new PluginResult( Status.ERROR );
			}

		} else if (action.equals(CMD_GET_PRODUCTS)) {
			Log.d(TAG, "Execute getProducts");
			result = new PluginResult( Status.OK );

		} else {
			Log.e(TAG, "Invalid action : " + action + " passed");
			result = new PluginResult(Status.INVALID_ACTION);
		}

		return result;
	};

	public Activity getActivity() {
		return getStore().mActivity;
	} 

	public Store getStore() {
		return (Store) ((AndroidBilling) this.ctx).store;
	} 
	
    public StorePlugin() {
		Log.d(TAG, "Constructor ");
	}
	
	public void finalize() {
		Log.d(TAG, "Finalize");
	}
}