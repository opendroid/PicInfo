/*
 *
 * Copyright (c) 2015. Pacific Street Ventures, LLC
 * No permission required for "Fair use"
 * @author Ajay Thakur, ajay@pacificstreetventures.com
 * Created on ${DATE}.
 * Version: 0.1
 */

package com.pacificstreetventures.apps.picinfo;

import android.app.Activity;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.database.StaleDataException;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.provider.MediaStore;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ImageDataLoadFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ImageDataLoadFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ImageDataLoadFragment extends Fragment
        implements LoaderManager.LoaderCallbacks<Cursor> {

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        public void onDatabaseUpdated(final List<PicInfo> dataList, final int loaderId);
    }


    private final String TAG = getClass().getSimpleName();
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private List<PicInfo> mPicsDataAll = null;
    private List<PicInfo> mPicsDataInternalDb = null;
    private List<PicInfo> mPicsDataExternalDb = null;
    public static final int IMAGE_LOADER_EXTERNAL = 1;
    public static final int IMAGE_LOADER_INTERNAL = 2;
    Loader<Cursor> mInternalLoader, mExternalLoader;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ImageDataLoadFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ImageDataLoadFragment newInstance(String param1, String param2) {
        ImageDataLoadFragment fragment = new ImageDataLoadFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public ImageDataLoadFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        setRetainInstance(true); // A retained fragment.

        // Update Pictures database
        LoaderManager mgr = getActivity().getSupportLoaderManager();
        if (mgr != null) {
            Log.d(TAG, "onCreate int mgr");
            mExternalLoader = mgr.initLoader(IMAGE_LOADER_EXTERNAL, null, this);
            mInternalLoader = mgr.initLoader(IMAGE_LOADER_INTERNAL, null, this);
            if (mPicsDataAll == null) {
                mPicsDataAll = new ArrayList<>();
            }

        } else {
            Log.d(TAG, "onCreate mgr is null");
        }
        Log.d(TAG, "onCreate");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Log.d(TAG, "onCreateView");
        return null;
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
        Log.d(TAG, "onAttach");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
        Log.d(TAG, "onDetach");
    }


    //---------------------------------------------------------------------------------------------
    // Interface methods:
    //    LoaderManager.LoaderCallbacks<Cursor> interaction.
    //---------------------------------------------------------------------------------------------
    @Override
    public Loader<Cursor> onCreateLoader(int loaderID, Bundle bundle) {
        Log.d(TAG, "onCreateLoader:" + loaderID);
        /*
         * Takes action based on the ID of the Loader that's being created
         */

        switch (loaderID) {
            case IMAGE_LOADER_EXTERNAL:
                // Returns a new CursorLoader
                return new CursorLoader(
                        getActivity(),   // Parent activity context
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,        // Table to query
                        null,     // Projection to return
                        null,            // No selection clause
                        null,            // No selection arguments
                        MediaStore.MediaColumns.DATE_ADDED + " ASC" // Default sort order
                );
            case IMAGE_LOADER_INTERNAL:
                // Returns a new CursorLoader
                return new CursorLoader(
                        getActivity(),   // Parent activity context
                        MediaStore.Images.Media.INTERNAL_CONTENT_URI,        // Table to query
                        null,     // Projection to return
                        null,            // No selection clause
                        null,            // No selection arguments
                        MediaStore.MediaColumns.DATE_ADDED + " ASC" // Default sort order
                );
            default:
                // An invalid id was passed in
                return null;
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cr) {
        final List<PicInfo> pix = new ArrayList<>();
        final List<PicInfo> whichList;
        int loaderId = loader.getId();
        String dbName;

        if (loaderId == IMAGE_LOADER_INTERNAL) {
            if (mPicsDataInternalDb == null)
                mPicsDataInternalDb = new ArrayList<>();
            whichList = mPicsDataInternalDb;
            dbName = "IMAGE_LOADER_INTERNAL";
            Log.d(TAG, "onLoadFinished -- IMAGE_LOADER_INTERNAL");
        } else if (loaderId == IMAGE_LOADER_EXTERNAL) {
            if (mPicsDataExternalDb == null)
                mPicsDataExternalDb = new ArrayList<>();
            whichList = mPicsDataExternalDb;
            dbName = "IMAGE_LOADER_EXTERNAL";
            Log.d(TAG, "onLoadFinished -- IMAGE_LOADER_EXTERNAL");
        } else {
            Log.d(TAG, "onLoadFinished -- what?");
            return;
        }

        try {
            int nPixData = cr.getCount();
            int COLUMNS = cr.getColumnCount();
            Log.d(TAG, "onLoadFinished: COLUMNS - " + COLUMNS + ", rows - " + nPixData);
            // if (nPixData > 0) nPixData = 2; // debug
            cr.moveToFirst();
            for (int i = 0; i < nPixData; i++) {
                // Check if thread is interrupted
                // Read each row of the DB and save columns data safely.
                // Assumption - column #s dont change across manufacturer
                try {
                    PicInfo p = new PicInfo();
                    // Get as much as as much available.
                    if (COLUMNS > 0) p.setID(cr.getLong(0));    // 'id' idx=0
                    if (COLUMNS > 2) p.setSize(cr.getLong(2));  // '_size' idx=2
                    if (COLUMNS > 3) p.setDisplayName(cr.getString(3)); // '_display_name' idx=3
                    if (COLUMNS > 4) p.setMimeType(cr.getString(4)); // 'mime_type' idx=4
                    if (COLUMNS > 5) p.setTitle(cr.getString(5)); // 'title' idx=5
                    if (COLUMNS > 6)
                        p.setDateAdded(cr.getLong(6) * 1000); // 'date_added' idx=6 in seconds
                    if (COLUMNS > 7)
                        p.setDateModified(cr.getLong(7) * 1000); // 'date_modified' idx=7
                    if (COLUMNS > 8) p.setDescription(cr.getString(8)); // 'description' idx=8
                    if (COLUMNS > 9) p.setPicasaId(cr.getString(9)); // 'picasa_id' idx=9
                    if (COLUMNS > 10) p.setPrivate(cr.getInt(10));// 'isprivate' idx=10
                    // 'latitude' idx=11, and 'longitude' idx=12
                    if (COLUMNS > 12) p.setLocation(cr.getDouble(11), cr.getDouble(12));
                    if (COLUMNS > 13) p.setDateTaken(cr.getLong(13)); // 'datetaken' idx=13
                    if (COLUMNS > 14) p.setOrientation(cr.getInt(14)); // 'orientation' idx=14
                    if (COLUMNS > 15)
                        p.setThumbnailMagic(cr.getInt(15)); // 'mini_thumb_magic' idx=15 -- use with map
                    // 'bucket_id' idx=16  and 'bucket_display_name' idx=17
                    if (COLUMNS > 17) p.setBucket(cr.getString(16), cr.getString(17));
                    if (COLUMNS > 19) p.setWidthHeight(cr.getInt(18), cr.getInt(19));
                    if (COLUMNS > 1) p.processImageMetaData(cr.getString(1)); // DATA filename
                    p.setColumns(COLUMNS); // Store how much data is available -- handle different versions
                    pix.add(p);
                    // Log.d(TAG, "Image:" + i + ", Data:\n" + p);
                } catch (CursorIndexOutOfBoundsException ex) {
                    Log.e(TAG, "onLoadFinished: Bad cursor index=" + i + "\n", ex);
                } catch (StaleDataException staleEx) {
                    Log.e(TAG, "onLoadFinished: Stale data:", staleEx);
                }
                cr.moveToNext(); // Next item
            }
            // Log.d(TAG, "Uri:" + uri.toString());
            // Log.d(TAG, "Count:" + nPixData + ", Fields: " + Arrays.toString(cr.getColumnNames()));
        } catch (OutOfMemoryError ex) {
            cr = null;
            Log.e(TAG, "onLoadFinished: OutOfMemoryError:", ex);
        } finally {
            if (cr != null) cr.close();
        }
        // All finished save it to public list
        if (pix.size() > 0) { // something to add.
            whichList.addAll(pix);
            mPicsDataAll.addAll(whichList);
            Log.d(TAG, "onLoadFinished: added from - " + dbName + ", read:" + pix.size() + " pictures");
            if (mListener != null) {
                mListener.onDatabaseUpdated(mPicsDataAll, loaderId);
            }
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        switch (loader.getId()) {
            case IMAGE_LOADER_EXTERNAL:
                mPicsDataAll.removeAll(mPicsDataExternalDb);
                mPicsDataExternalDb = null;
                Log.d(TAG, "onLoaderReset: - IMAGE_LOADER_EXTERNAL");
                break;
            case IMAGE_LOADER_INTERNAL:
                mPicsDataAll.removeAll(mPicsDataInternalDb);
                mPicsDataInternalDb = null;
                Log.d(TAG, "onLoaderReset: - IMAGE_LOADER_INTERNAL");
                break;
        }
        if (mListener != null) {
            mListener.onDatabaseUpdated(mPicsDataAll, loader.getId());
        }
    } // End reset loader

}
