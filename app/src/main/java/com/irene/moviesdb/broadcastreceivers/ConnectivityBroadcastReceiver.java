package com.irene.moviesdb.broadcastreceivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.irene.moviesdb.utils.NetworkConnection;


/**
 * Created by irene.
 */

public class ConnectivityBroadcastReceiver extends BroadcastReceiver {

    private ConnectivityReceiverListener mConnectivityReceiverListener;

    public ConnectivityBroadcastReceiver(ConnectivityReceiverListener mConnectivityReceiverListener) {
        this.mConnectivityReceiverListener = mConnectivityReceiverListener;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (mConnectivityReceiverListener != null && NetworkConnection.isConnected(context))
            mConnectivityReceiverListener.onNetworkConnectionConnected();
    }


    public interface ConnectivityReceiverListener {
        void onNetworkConnectionConnected();
    }

}
