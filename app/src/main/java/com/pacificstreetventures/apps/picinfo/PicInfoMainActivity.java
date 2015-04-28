/*
 *
 * Copyright (c) 2015. Pacific Street Ventures, LLC
 * No permission required for "Fair use"
 * @author Ajay Thakur, ajay@pacificstreetventures.com
 * Created on ${DATE}.
 * Version: 0.1
 */
package com.pacificstreetventures.apps.picinfo;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;

public class PicInfoMainActivity extends ActionBarActivity
        implements ImageDataLoadFragment.OnFragmentInteractionListener {
    private final String TAG = getClass().getSimpleName();
    private List<PicInfo> mPicsData = null;
    private final String FRAGMENT_DOWNLOAD_NAME = "IMAGE_DATA_DOWNLOAD_FRAGMENT";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pic_info_main);
        Log.d(TAG, "onCreate");

        // Setup list.
        if (mPicsData == null)
            mPicsData = new ArrayList<>();

        // Create download fragment it does not exist
        FragmentManager fMgr = getSupportFragmentManager();
        ImageDataLoadFragment fragment = (ImageDataLoadFragment) fMgr.findFragmentByTag(FRAGMENT_DOWNLOAD_NAME);
        // Create one if one does not exist
        if (fragment == null) {
            FragmentTransaction fragmentTransaction = fMgr.beginTransaction();
            ImageDataLoadFragment dataLoadFragement = ImageDataLoadFragment.newInstance("1", "2");
            fragmentTransaction.add(dataLoadFragement, FRAGMENT_DOWNLOAD_NAME)
                    .commit();
            Log.d(TAG, "Created RetainedFragment - ImageDataLoadFragment");
        }
    }




    @Override
    protected void onPause() {
        Log.d(TAG, "onPause");
        super.onPause();
    }

    @Override
    protected void onResume() {
        Log.d(TAG, "onResume");
        super.onResume();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        Log.d(TAG, "onSaveInstanceState");
        super.onSaveInstanceState(outState);
    }

    @Override
    public Object onRetainCustomNonConfigurationInstance() {
        Log.d(TAG, "onRetainCustomNonConfigurationInstance");
        return super.onRetainCustomNonConfigurationInstance();
    }

    @Override
    public Object getLastCustomNonConfigurationInstance() {
        Log.d(TAG, "getLastCustomNonConfigurationInstance");
        return super.getLastCustomNonConfigurationInstance();
    }

    @Override
    protected void onDestroy() {
        Log.d(TAG, "onDestroy");
        // Destroy.
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_pic_info_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //---------------------------------------------------------------------------------------------
    // Interface methods:
    //    onDatabaseUpdated: to communicate with 'ImageDataLoadFragment'
    //---------------------------------------------------------------------------------------------

    @Override
    public void onDatabaseUpdated(List<PicInfo> dataList) {
        mPicsData = dataList; // update data list
        // Inform adapter of changes
    }
}
