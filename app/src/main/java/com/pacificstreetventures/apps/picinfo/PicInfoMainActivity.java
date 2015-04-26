/*
 *
 * Copyright (c) 2015. Pacific Street Ventures, LLC
 * No permission required for "Fair use"
 * @author Ajay Thakur, ajay@pacificstreetventures.com
 * Created on ${DATE}.
 * Version: 0.1
 */
package com.pacificstreetventures.apps.picinfo;

import android.content.ContentResolver;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.database.StaleDataException;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class PicInfoMainActivity extends ActionBarActivity {
    private List<PicInfo> mPicsData = null;
    private final String TAG = getClass().getSimpleName();
    private Thread mReadPicDbThread = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pic_info_main);

        // Upload the data
        mPicsData = new ArrayList<>();
        uploadImageDataThread(MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        uploadImageDataThread(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
    }

    private void    uploadImageDataThread(final Uri uri) {
        // Read images from Media.
        String[] projection = new String[]{
                MediaStore.MediaColumns.DISPLAY_NAME
                , MediaStore.MediaColumns.DATE_ADDED
                , MediaStore.MediaColumns.MIME_TYPE
        };

        // Upload the data
        mReadPicDbThread = new Thread(new Runnable() {
            @Override
            public void run() {
                final List<PicInfo> pix = new ArrayList<PicInfo>();
                Cursor cr = null;
                ContentResolver contentResolver = null;
                try {
                    contentResolver = getContentResolver();
                    cr = contentResolver.query(uri, // uri
                            null, // projection - Which columns to return.
                            null, // WHERE clause.
                            null, // WHERE clause value substitution
                            MediaStore.MediaColumns.DATE_ADDED + " DESC" // Sort order.
                            // "datetaken DESC"
                    );

                    int nPixData = cr.getCount();
                    int COLUMNS = cr.getColumnCount();
                    // if (nPixData > 0) nPixData = 10; // debug

                    cr.moveToFirst();
                    for (int i = 0; i < nPixData; i++) {
                        // Check if thread is interrupted
                        if (Thread.interrupted()) {
                            throw new InterruptedException();
                        }
                        // Read each row of the DB and save columns data safely.
                        // Assumption - column #s dont change across manufacturer
                        try {
                            PicInfo p = new PicInfo();
                            // Get as much as as much available.
                            p.setColumns(COLUMNS); // Store how much data is available -- handle different versions
                            if (COLUMNS > 0) p.setID(cr.getLong(0));    // 'id' idx=0
                            if (COLUMNS > 2) p.setSize(cr.getLong(2));  // '_size' idx=2
                            if (COLUMNS > 3) p.setDisplayName(cr.getString(3)); // '_display_name' idx=3
                            if (COLUMNS > 4) p.setMimeType(cr.getString(4)); // 'mime_type' idx=4
                            if (COLUMNS > 5) p.setTitle(cr.getString(5)); // 'title' idx=5
                            if (COLUMNS > 6) p.setDateAdded(cr.getLong(6) * 1000); // 'date_added' idx=6 in seconds
                            if (COLUMNS > 7) p.setDateModified(cr.getLong(7) * 1000); // 'date_modified' idx=7
                            if (COLUMNS > 8) p.setDescription(cr.getString(8)); // 'description' idx=8
                            if (COLUMNS > 9) p.setPicasaId(cr.getString(9)); // 'picasa_id' idx=9
                            if (COLUMNS > 10) p.setPrivate(cr.getInt(10));// 'isprivate' idx=10
                            // 'latitude' idx=11, and 'longitude' idx=12
                            if (COLUMNS > 12) p.setLocation(cr.getDouble(11), cr.getDouble(12));
                            if (COLUMNS > 13) p.setDateTaken(cr.getLong(13)); // 'datetaken' idx=13
                            if (COLUMNS > 14) p.setOrientation(cr.getInt(14)); // 'orientation' idx=14
                            if (COLUMNS > 15) p.setThumbnailMagic(cr.getInt(15)); // 'mini_thumb_magic' idx=15 -- use with map
                            // 'bucket_id' idx=16  and 'bucket_display_name' idx=17
                            if (COLUMNS > 17) p.setBucket(cr.getString(16), cr.getString(17));
                            if (COLUMNS > 19) p.setWidthHeight(cr.getInt(18), cr.getInt(19));
                            pix.add(p);
                            Log.d(TAG, "Image:" + i + ", Data:\n" + p);
                        } catch (CursorIndexOutOfBoundsException ex) {
                            Log.e(TAG,"Bad cursor index=" + i + "\n" , ex);
                        } catch (StaleDataException staleEx) {
                            Log.e(TAG,"Stale date:", staleEx);
                        }
                        cr.moveToNext(); // Next item
                    }
                    Log.d(TAG, "Uri:" + uri.toString());
                    Log.d(TAG, "Count:" + nPixData + ", Fields: " + Arrays.toString(cr.getColumnNames()));
                } catch (OutOfMemoryError ex) {
                    Log.e(TAG,"OutOfMemoryError:", ex);
                } catch (InterruptedException ex) {
                    Log.e(TAG,"InterruptedException:", ex);
                }
                finally {
                    if (cr != null) cr.close();
                }

                // All finished save it to public list
                if (pix.size() > 0 ) { // something to add.
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mPicsData.addAll(pix);
                        }
                    });
                }
            }
        });
        mReadPicDbThread.start(); // Start thread

        // Internal Media
    }

    @Override
    protected void onDestroy() {
        // Shut down the interrupts.
        if ((mReadPicDbThread != null ) && (mReadPicDbThread.isAlive()) ) {
            mReadPicDbThread.interrupt();
        }
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
}
