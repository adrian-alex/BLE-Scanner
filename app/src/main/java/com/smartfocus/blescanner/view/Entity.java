package com.smartfocus.blescanner.view;

import com.smartfocus.blescanner.impl.BleScan;

/**
 * @author Adrian Antoci
 * @since 27/08/15
 */
public class Entity {
    private BleScan bleScan;
    private String hz;

    public BleScan getBleScan() {
        return bleScan;
    }

    public void setBleScan(BleScan bleScan) {
        this.bleScan = bleScan;
    }

    public String getHz() {
        return hz;
    }

    public void setHz(String hz) {
        this.hz = hz;
    }
}
