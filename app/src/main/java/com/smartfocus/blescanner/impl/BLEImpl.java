package com.smartfocus.blescanner.impl;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 *
 * @author Adrian Antoci
 * @since 30/06/15
 */
public class BLEImpl {
    public interface Callback {
        void onResult(BleScan scan);
    }

    public enum FilterType{
        NONE(),
        MAC(),
        SERVICE_UUID();

        List<String> macAddressList;
        UUID serviceUUID;

        FilterType() {
        }

        public List<String> getMacAddressList() {
            return macAddressList;
        }

        public void setMacAddressList(List<String> macAddressList) {
            this.macAddressList = macAddressList;
        }

        public UUID getServiceUUID() {
            return serviceUUID;
        }

        public void setServiceUUID(UUID serviceUUID) {
            this.serviceUUID = serviceUUID;
        }
    }

    private Context mContext;

    private BluetoothAdapter mBluetoothAdapter;
    private Callback mCallback;

    private boolean isScanning;

    private FilterType mScanType;
    private ExecutorService threadExecutor;

    /**
     * Default constructor
     * @param context
     */
    public BLEImpl(Context context){
        mContext = context;
        threadExecutor = Executors.newSingleThreadExecutor();
    }

    /**
     *
     * @param filterType
     * @param callback
     * @return true if successful
     */
    public boolean startScan(FilterType filterType, Callback callback) {

        mCallback = callback;
        mScanType = filterType;

        if (filterType != FilterType.NONE){

            if (filterType == FilterType.SERVICE_UUID){

                UUID[] uuidArray = new UUID[]{filterType.getServiceUUID()};
                isScanning = getAdapter().startLeScan(uuidArray ,scanCallback);

            }else{ //filter by mac
                isScanning = getAdapter().startLeScan(scanCallback);
            }
        }else{
            isScanning = getAdapter().startLeScan(scanCallback);
        }
        return isScanning;
    }

    /**
     *
     */
    public void stopScanning() {
        if (!hasBLEnabled()) return;

        getAdapter().stopLeScan(scanCallback);

        isScanning = false;
    }

    /**
     *
     * @return true if enabled
     */
    public boolean hasBLEnabled() {
        return getAdapter() != null && getAdapter().isEnabled();
    }

    /**
     *
     * @return true if scanning
     */
    public boolean isScanning() {
        return isScanning;
    }

    private BluetoothAdapter.LeScanCallback scanCallback = new BluetoothAdapter.LeScanCallback() {
        @Override
        public void onLeScan(final BluetoothDevice device, final int rssi, final byte[] scanRecord) {
            if (isScanning) {
                switch (mScanType){
                    case NONE:
                        threadExecutor.execute(new Runnable() {
                            @Override
                            public void run() {
                                mCallback.onResult(buildScanResult(device, rssi, scanRecord));
                            }
                        });
                        break;
                    case MAC:
                        threadExecutor.execute(new Runnable() {
                            @Override
                            public void run() {
                                if (filterByMac(device.getAddress())){
                                    mCallback.onResult(buildScanResult(device, rssi, scanRecord));
                                }
                            }
                        });
                        break;
                    case SERVICE_UUID:
                            threadExecutor.execute(new Runnable() {
                                @Override
                                public void run() {
                                    mCallback.onResult(buildScanResult(device, rssi, scanRecord));
                                }
                            });
                        break;
                    default:
                        throw new RuntimeException("unhandled exception");
                }

            } else {
                //is not a beacon
            }
        }
    };

    /**
     *
     * @param device
     * @param rssi
     * @param scanRecord
     * @return
     */
    private BleScan buildScanResult(BluetoothDevice device, int rssi, byte[] scanRecord) {
        BleScan bleScan = new BleScan();
        bleScan.setTimestamp(System.nanoTime());//in order to match the sensor timestamp in nano
        bleScan.setMac(device.getAddress());
        bleScan.setName(device.getName());
        bleScan.setRssi(rssi);
        return  bleScan;
    }

    /**
     *
     * @param macAddress
     * @return
     */
    private boolean filterByMac(String macAddress) {
        if (macAddress == null) return false;

        for (String filterValue : mScanType.getMacAddressList()) {
            if (filterValue.toLowerCase().equals(macAddress.toLowerCase())) return true;
        }

        return false;
    }

    private BluetoothAdapter getAdapter() {
        if (android.os.Build.VERSION.SDK_INT < 18) {
            return null;
        }
        if (mBluetoothAdapter == null) {
            final BluetoothManager bluetoothManager = (BluetoothManager) mContext.getSystemService(Context.BLUETOOTH_SERVICE);
            mBluetoothAdapter = bluetoothManager.getAdapter();

        }
        return mBluetoothAdapter;
    }



}
