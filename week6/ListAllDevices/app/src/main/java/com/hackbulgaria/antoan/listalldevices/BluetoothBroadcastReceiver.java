package com.hackbulgaria.antoan.listalldevices;

import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by Antoan on 02-Dec-14.
 */
public class BluetoothBroadcastReceiver extends BroadcastReceiver {

    private OnBluetoothDeviceFoundListener mOnBluetoothDeviceFoundListener;

    @Override
    public void onReceive(Context context, Intent intent) {
        if(mOnBluetoothDeviceFoundListener != null) {
            BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
            mOnBluetoothDeviceFoundListener.onBluetoothDeviceFound(device.getName());
        }
    }

    public void setOnBluetoothDeviceFoundListener(OnBluetoothDeviceFoundListener listener) {
        mOnBluetoothDeviceFoundListener = listener;
    }
}