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

	public static final String CMD_REQUEST_PAYMENT = "requestPayment";
	public static final String CMD_GET_PRODUCTS = "getProducts";
	
	private boolean firstLaunch;
		
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

		init();
		PluginResult result = null;

		if (action.equals(CMD_REQUEST_PAYMENT)) {
			Log.d(TAG, "Execute requestPayment:" + arguments);
			
			if (arguments.length() == 1) {
				try {        
					JSONObject jElement = arguments.getJSONObject(0);
		            String response = jElement.getString("productID");
					this.requestPayment(response);
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

	private void getProducts() {
		Store store = getStore();
	}	
	
	// do a payment with the specified producID (sku identifier)
	private void requestPayment(String productID) {
		Log.d(TAG, "requestPayment with productID: " + productID);
		
		String mPayloadContents = ""; // for debug only
		//BillingService mBillingService = new BillingService();
		//Context context = ((AndroidBilling) this.ctx).getApplicationContext();		
        //mBillingService.setContext(context);

		//mBillingService.setContext(this);
		if (!mBillingService.requestPurchase(productID, mPayloadContents)) {
			Log.d(TAG, "requestPayment error");	
		}
	}
	
	/**
     * The SharedPreferences key for recording whether we initialized the
     * database.  If false, then we perform a RestoreTransactions request
     * to get all the purchases for this user.
     */
    private static final String DB_INITIALIZED = "db_initialized";

    private StorePurchaseObserver mStorePurchaseObserver;
    private Handler mHandler;

    private BillingService mBillingService;

    private PurchaseDatabase mPurchaseDatabase;
    private Cursor mOwnedItemsCursor;
    private Set<String> mOwnedItems = new HashSet<String>();

    /**
     * The developer payload that is sent with subsequent
     * purchase requests.
     */
    private String mPayloadContents = null;

    /**
     * A {@link PurchaseObserver} is used to get callbacks when Android Market sends
     * messages to this application so that we can update the UI.
     */
    private class StorePurchaseObserver extends PurchaseObserver {
		private boolean mBillingSupported; // is billing supported
	
        public StorePurchaseObserver(Handler handler) {
            //super(AndroidBilling, handler);
			super(getActivity(),handler);
        }

        @Override
        public void onBillingSupported(boolean supported) {
            if (Consts.DEBUG) {
                Log.i(TAG, "supported: " + supported);
            }
			mBillingSupported = supported;
            if (supported) {
                restoreDatabase();
            } else {
                // @showDialog(DIALOG_BILLING_NOT_SUPPORTED_ID);
            }
        }

        @Override
        public void onPurchaseStateChange(PurchaseState purchaseState, String itemId, int quantity, long purchaseTime, String developerPayload) {
            if (Consts.DEBUG) {
                Log.i(TAG, "onPurchaseStateChange() itemId: " + itemId + " " + purchaseState);
            }

            if (purchaseState == PurchaseState.PURCHASED) {
                mOwnedItems.add(itemId);
            }
            //mCatalogAdapter.setOwnedItems(mOwnedItems); // normalement ne sert a rien
            mOwnedItemsCursor.requery();
        }

        @Override
        public void onRequestPurchaseResponse(RequestPurchase request, ResponseCode responseCode) {
            if (Consts.DEBUG) {
                Log.d(TAG, request.mProductId + ": " + responseCode);
            }
            if (responseCode == ResponseCode.RESULT_OK) {
                if (Consts.DEBUG) {
                    Log.i(TAG, "purchase was successfully sent to server");
                }
            } else if (responseCode == ResponseCode.RESULT_USER_CANCELED) {
                if (Consts.DEBUG) {
                    Log.i(TAG, "user canceled purchase");
                }
            } else {
                if (Consts.DEBUG) {
                    Log.i(TAG, "purchase failed");
                }
            }
        }

        @Override
        public void onRestoreTransactionsResponse(RestoreTransactions request, ResponseCode responseCode) {
            if (responseCode == ResponseCode.RESULT_OK) {
                if (Consts.DEBUG) {
                    Log.d(TAG, "completed RestoreTransactions request");
                }
                // Update the shared preferences so that we don't perform
                // a RestoreTransactions again.
                SharedPreferences prefs = getActivity().getPreferences(Context.MODE_PRIVATE);
                SharedPreferences.Editor edit = prefs.edit();
                edit.putBoolean(DB_INITIALIZED, true);
                edit.commit();
            } else {
                if (Consts.DEBUG) {
                    Log.d(TAG, "RestoreTransactions error: " + responseCode);
                }
            }
        }
    }

    /** Called first */
    public StorePlugin() {
		firstLaunch = true;
		Log.d(TAG, "Constructor ");
	}
	
	public void init() {
		if (!firstLaunch) return;
		firstLaunch = false;
		Log.d(TAG, "Init with activity: " + getActivity());
		
		
        mHandler = new Handler();
        mStorePurchaseObserver = new StorePurchaseObserver(mHandler);
        mBillingService = new BillingService();
        //mBillingService.setContext(getActivity());

		Context context = ((AndroidBilling) this.ctx).getApplicationContext();		
        mBillingService.setContext(context);

        mPurchaseDatabase = new PurchaseDatabase(getActivity());

        // Check if billing is supported.
        ResponseHandler.register(mStorePurchaseObserver);
        if (!mBillingService.checkBillingSupported()) {
            // @showDialog(DIALOG_CANNOT_CONNECT_ID);
        }
		initializeOwnedItems(); // Called when this activity becomes visible. (@ onStart)
    }

	public void finalize() {
		ResponseHandler.unregister(mStorePurchaseObserver); // @ onStop
		mPurchaseDatabase.close(); // @ onDestroy
        mBillingService.unbind();
	}

    /**
     * If the database has not been initialized, we send a
     * RESTORE_TRANSACTIONS request to Android Market to get the list of purchased items
     * for this user. This happens if the application has just been installed
     * or the user wiped data. We do not want to do this on every startup, rather, we want to do
     * only when the database needs to be initialized.
     */
    private void restoreDatabase() {
        SharedPreferences prefs = getActivity().getPreferences(Context.MODE_PRIVATE);
        boolean initialized = prefs.getBoolean(DB_INITIALIZED, false);
        if (!initialized) {
            mBillingService.restoreTransactions();
            Toast.makeText(getActivity(), "Restoring transactions", Toast.LENGTH_LONG).show();// @ "R.string.restoring_transactions
        }
    }

    /**
     * Creates a background thread that reads the database and initializes the
     * set of owned items.
     */
    private void initializeOwnedItems() {
        new Thread(new Runnable() {
            public void run() {
                doInitializeOwnedItems();
            }
        }).start();
    }

    /**
     * Reads the set of purchased items from the database in a background thread
     * and then adds those items to the set of owned items in the main UI
     * thread.
     */
    private void doInitializeOwnedItems() {
        Cursor cursor = mPurchaseDatabase.queryAllPurchasedItems();
        if (cursor == null) {
            return;
        }

        final Set<String> ownedItems = new HashSet<String>();
        try {
            int productIdCol = cursor.getColumnIndexOrThrow(PurchaseDatabase.PURCHASED_PRODUCT_ID_COL);
            while (cursor.moveToNext()) {
                String productId = cursor.getString(productIdCol);
                ownedItems.add(productId);
            }
        } finally {
            cursor.close();
        }

        // We will add the set of owned items in a new Runnable that runs on
        // the UI thread so that we don't need to synchronize access to
        // mOwnedItems.
        mHandler.post(new Runnable() {
            public void run() {
                mOwnedItems.addAll(ownedItems);
                // @ mCatalogAdapter.setOwnedItems(mOwnedItems);
            }
        });
    }
	
	// visual classic message box, but not modal and need to use back button to close it
	public void MessageBox(String title,String message){
		AlertDialog alertDialog;
		alertDialog = new AlertDialog.Builder(getActivity()).create();
		alertDialog.setTitle(title);
		alertDialog.setMessage(message);
		alertDialog.show();
	}
	
	// toast message : look like tool-tips for 2 seconds
	public void MessageBox2(String message){
	    Toast.makeText(getActivity(),message,Toast.LENGTH_SHORT).show();
	}	
}