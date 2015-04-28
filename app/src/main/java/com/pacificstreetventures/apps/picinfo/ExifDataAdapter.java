/*
 *
 * Copyright (c) 2015. Pacific Street Ventures, LLC
 * No permission required for "Fair use"
 * @author Ajay Thakur, ajay@pacificstreetventures.com
 * Created on ${DATE}.
 * Version: 0.1
 */

package com.pacificstreetventures.apps.picinfo;

import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by ajaythakur on 4/27/15.
 */
public class ExifDataAdapter extends RecyclerView.Adapter<ExifDataAdapter.PictureViewHolder> {
    private final String TAG = getClass().getSimpleName();
    private List<PicInfo> mPicInfoList;

    public ExifDataAdapter(List<PicInfo> picInfoList) {
        mPicInfoList = picInfoList;
    }

    @Override
    public PictureViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return null;
    }

    @Override
    public void onBindViewHolder(PictureViewHolder pictureViewHolder, int i) {

    }

    @Override
    public int getItemCount() {
        return mPicInfoList.size();
    }

    public class PictureViewHolder extends RecyclerView.ViewHolder {

        // Viewholder -- Display a 'PictureObject' depending upon the state on screen when
        // requested by 'RecyclerView.ViewHolder'
        public PictureViewHolder(View itemView, int viewType) {
            super(itemView);
        }

    }

}
