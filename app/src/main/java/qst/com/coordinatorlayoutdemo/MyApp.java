package qst.com.coordinatorlayoutdemo;

import android.app.Application;
import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.OnLifecycleEvent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;

import com.facebook.stetho.Stetho;
import com.google.firebase.analytics.FirebaseAnalytics;



public class MyApp extends Application implements LifecycleObserver {

    public static final String DATABASE_FILE_NAME = "tron_wallet.db";

    public static final String FOREGROUND_CHANGED = "com.eletac.tronwallet.app.foreground_changed";

    public static final long BLOCKCHAIN_UPDATE_INTERVAL = Long.MAX_VALUE; // not used - using singleShots only
    public static final long NETWORK_UPDATE_INTERVAL = Long.MAX_VALUE; // not used - using singleShots only
    public static final long TOKENS_UPDATE_INTERVAL = Long.MAX_VALUE; // not used - using singleShots only
    public static final long ACCOUNTS_UPDATE_INTERVAL = Long.MAX_VALUE; // not used - using singleShots only

    public static final long ACCOUNT_UPDATE_FOREGROUND_INTERVAL = 2000;
    public static final long ACCOUNT_UPDATE_BACKGROUND_INTERVAL = 15000;
    public static final long PRICE_UPDATE_INTERVAL = 15000;

    private FirebaseAnalytics mFirebaseAnalytics;
    private static Context mContext;
    private static boolean mIsInForeground;

    public void onCreate() {
        super.onCreate();
        this.mContext=this;

        Stetho.initializeWithDefaults(this);

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.APP_OPEN, new Bundle());

    }

    public static Context getAppContext() {
        return mContext;
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    private void onAppBackgrounded() {
        mIsInForeground = false;
        LocalBroadcastManager.getInstance(mContext).sendBroadcast(new Intent(FOREGROUND_CHANGED));
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    private void onAppForegrounded() {
        mIsInForeground = true;
        LocalBroadcastManager.getInstance(mContext).sendBroadcast(new Intent(FOREGROUND_CHANGED));
    }

    public static boolean isIsInForeground() {
        return mIsInForeground;
    }

}
