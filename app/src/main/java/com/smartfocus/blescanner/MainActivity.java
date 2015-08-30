package com.smartfocus.blescanner;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.smartfocus.blescanner.impl.BLEImpl;
import com.smartfocus.blescanner.impl.BleScan;
import com.smartfocus.blescanner.view.BasicListAdapter;
import com.smartfocus.blescanner.view.Entity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends ActionBarActivity {

    private BasicListAdapter mAdapter;
    private BLEImpl mBLEImplementation;

    private List<Entity> mScanList = new ArrayList<Entity>();
    private List<BleScan> timerScanList = new ArrayList<>();

    private final static String SM_SERVICE_UUID = "50414A03-D6C0-4C18-9EAD-DDB3CCD64CF8";

    private ExecutorService mExecutorService;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        mExecutorService = Executors.newSingleThreadExecutor();

        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);

        RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.MAIN_recyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new BasicListAdapter(this);
        mRecyclerView.setAdapter(mAdapter);

        mBLEImplementation = new BLEImpl(this);

        BLEImpl.FilterType filterType = BLEImpl.FilterType.SERVICE_UUID;
        filterType.setServiceUUID(UUID.fromString(SM_SERVICE_UUID));

        mBLEImplementation.startScan(filterType, new BLEImpl.Callback() {
            @Override
            public void onResult(final BleScan bleScan) {
                mExecutorService.execute(new Runnable() {
                    @Override
                    public void run() {

                        timerScanList.add(bleScan);

                        //pre-process
                        if (bleScan.getName() == null) {
                            bleScan.setName("unknown");
                        }

                        int index = -1;
                        for (int i = 0; i < mScanList.size(); i++) {
                            if (mScanList.get(i).getBleScan().getMac().equals(bleScan.getMac())) {
                                index = i;
                                break;
                            }
                        }

                        Entity entity = new Entity();
                        entity.setHz("");
                        entity.setBleScan(bleScan);

                        if (index == -1) {

                            mScanList.add(entity);
                        } else {
                            mScanList.set(index, entity);
                        }

                        Collections.sort(mScanList, new Comparator<Entity>() {

                            public int compare(Entity o1, Entity o2) {
                                return Integer.valueOf(o2.getBleScan().getRssi()).compareTo(Integer.valueOf(o1.getBleScan().getRssi()));
                            }
                        });
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mAdapter.setData(mScanList);
                            }
                        });
                    }
                });
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }
}
