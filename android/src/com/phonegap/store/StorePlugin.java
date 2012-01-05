package com.phonegap.store;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.phonegap.api.Plugin;
import com.phonegap.api.PluginResult;
import com.phonegap.api.PluginResult.Status;

public class StorePlugin extends Plugin {

  public static final String CMD_REQUEST_PAYMENT = "requestPayment";
  public static final String CMD_GET_PRODUCTS = "getProducts";

  /*
  * Executes the request and returns PluginResult.
  *
  * @param action action to perform.
  * @param data input data.
  * @param callbackId The callback id used when calling back into JavaScript.
  * @return A PluginResult object with a status and message.
  *
  * @see com.phonegap.api.Plugin#execute(java.lang.String,
  * org.json.JSONArray, java.lang.String)
  */
  @Override
  public PluginResult execute(String action, JSONArray arguments, String callbackId) {
    Log.d("HelloWorldPlugin", "Plugin Called");

    PluginResult result = null;

    if (action.equals(CMD_REQUEST_PAYMENT)) {
      Log.d("StorePlugin", "execute requestPayment");
      result = new PluginResult( Status.OK );

    } else if (action.equals(CMD_GET_PRODUCTS)) {
      Log.d("StorePlugin", "execute getProducts");
      result = new PluginResult( Status.OK );

    } else {
      Log.d( "StorePlugin", "Invalid action : " + action + " passed");
      result = new PluginResult(Status.INVALID_ACTION);
    }

    return result;
  };

}