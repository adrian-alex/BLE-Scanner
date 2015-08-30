package com.smartfocus.blescanner.impl;

/**
 * Copyright (c) 2015. SmartFocus UK Limited. All rights reserved.
 *
 * @author Adrian Antoci
 * @since 01/05/15
 */
public class BleScan {
    private String name;
    private String mac;
    private int rssi;
    private long timestamp;

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public int getRssi() {
        return rssi;
    }

    public void setRssi(int rssi) {
        this.rssi = rssi;
    }

    public boolean equalsTo(BleScan bleScan){
        return bleScan.getMac().toLowerCase().equals(mac.toLowerCase());
    }

    @Override
    public String toString() {
        if (name!=null){
            return name;
        }
        return name;
    }


}
