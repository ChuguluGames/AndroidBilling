package com.phonegap.androidbilling;

import com.phonegap.store.Store;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import com.phonegap.*;

public class AndroidBilling extends DroidGap {

    private static final String TAG = "StoreGap";
    public Store store;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate");

        store = new Store(this);

        super.loadUrl("file:///android_asset/www/index.html");
    }

    /**
     * Called when this activity becomes visible.
     */
    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart");

        store.registerPurchaseObserver();
    }

    /**
     * Called when this activity is no longer visible.
     */
    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "onStop");

        store.unregisterPurchaseObserver();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy");

        store.unbindBillingService();
    }

    /**
     * Save the context of the log so simple things like rotation will not
     * result in the log being cleared.
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        Log.d(TAG, "onSaveInstanceState");
    }

    /**
     * Restore the contents of the log if it has previously been saved.
     */
    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        Log.d(TAG, "onRestoreInstanceState");
    }
}