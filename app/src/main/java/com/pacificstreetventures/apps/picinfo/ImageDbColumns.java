/*
 *
 * Copyright (c) 2015. Pacific Street Ventures, LLC
 * No permission required for "Fair use"
 * @author Ajay Thakur, ajay@pacificstreetventures.com
 * Created on ${DATE}.
 * Version: 0.1
 */

package com.pacificstreetventures.apps.picinfo;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

// Columns read in file. -- same name as in database.
// @link https://android.googlesource.com/platform/packages/apps/Gallery2/+/jb-dev/src/com/android/gallery3d/data/LocalAlbumSet.java
// for explanation


/**
 * Created by ajaythakur on 4/27/15.
 */
public class ImageDbColumns {
    private int mColumns; // How many columns data is available
    private long _id;
    // private byte[]  _data;
    private long _size;
    private String _display_name;
    private String mime_type;
    private String title;
    private Date date_added;
    private Date date_modified;
    private String description;
    private String picasa_id;
    private int isprivate;
    private double latitude;
    private double longitude;
    private Date datetaken;
    private int orientation;
    private int mini_thumb_magic;
    private String bucket_id;
    private String bucket_display_name;
    private int width;
    private int height;

    /**
     * Note found these extra columns on Samsung note 3. Not implemented yet.
     * <p/>
     * group_id, spherical_mosaic, addr, langagecode, is_secretbox,
     * weather_ID, sef_file_type, reusable]
     */

    // Setters/getters
    public void setColumns(int columns) {
        mColumns = columns;
    }

    public int getColumns() {
        return mColumns;
    }

    public void setID(long id) {
        _id = id;
    }

    public long getID() {
        return _id;
    }

    public void setSize(long sz) {
        _size = sz;
    }

    public long getSize() {
        return _size;
    }

    public void setDisplayName(String displayName) {
        _display_name = displayName;
    }

    public String getDisplayName() {
        return _display_name;
    }

    public void setMimeType(String mimeType) {
        mime_type = mimeType;
    }

    public String getMimeType() {
        return mime_type;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setDateAdded(long dateAddedMilliSec) {
        date_added = new Date(dateAddedMilliSec);
    }

    public Date getDateAdded() {
        return date_added;
    }

    public void setDateModified(long dateModifiedMilliSec) {
        date_modified = new Date(dateModifiedMilliSec);
    }

    public Date getDateModified() {
        return date_modified;
    }

    public void setDateTakenMilliSec(long dateTakenMilliSec) {
        datetaken = new Date(dateTakenMilliSec);
    }

    public Date getDateTakenMilliSec() {
        return datetaken;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setPicasaId(String id) {
        this.picasa_id = id;
    }

    public String getPicasaId() {
        return picasa_id;
    }

    public void setPrivate(int isprivate) {
        this.isprivate = isprivate;
    }

    public int getIsPrivate() {
        return isprivate;
    }

    public void setLocation(double _latitide, double _longitude) {
        latitude = _latitide;
        longitude = _longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setWidthHeight(int _width, int _height) {
        width = _width;
        height = _height;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public void setOrientation(int orientation) {
        this.orientation = orientation;
    }

    public int getOrientation() {
        return orientation;
    }

    public void setBucket(String bucketId, String bucketDisplayName) {
        bucket_id = bucketId;
        bucket_display_name = bucketDisplayName;
    }

    public String getBucketId() {
        return bucket_id;
    }

    public String getBucketDisplayName() {
        return bucket_display_name;
    }

    public void setThumbnailMagic(int thumbnail) {
        mini_thumb_magic = thumbnail;
    }

    public int getThumbnailMagic() {
        return mini_thumb_magic;
    }

    @Override
    public String toString() {
        DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        String s = "";
        s += "\n[IMAGEDB] Database Columns - " + mColumns;
        s += "\n[IMAGEDB] Display Name - " + _display_name;
        s += "\n[IMAGEDB] Description - " + description;
        s += "\n[IMAGEDB] Title - " + title;
        s += "\n[IMAGEDB] ID - " + _id + ", Size - " + _size;
        s += ", MIME - " + mime_type;
        if (mColumns > 13) s += "\n[IMAGEDB] Taken - " + df.format(datetaken);
        else s += "\n[IMAGEDB] Taken: NA";
        s += ", Added - " + df.format(date_added);
        s += ", Modified - " + df.format(date_modified);
        s += "\n[IMAGEDB] PicasaId - " + picasa_id + ",Private - " + isprivate;
        if (mColumns > 19)
            s += "\n[IMAGEDB] {WxH:(" + width + "," + height + ")}";
        else s += ",{HW - (NA,NA)}";
        if (mColumns > 15) s += ",ThumbNail:" + mini_thumb_magic;
        else s += ", ThumbNail - NA";
        if (mColumns > 14) s += ",Orientation:" + orientation;
        else s += ", Orientation - NA";
        if (mColumns > 12)
            s += "\n[IMAGEDB] Location - (" + latitude + "," + longitude + ")";
        else
            s += "\n[IMAGEDB] Location - (NA,NA)";
        if (mColumns > 16) s += "\n[IMAGEDB] BktId - " + bucket_id;
        else s += "\n[IMAGEDB] BktId - NA";
        if (mColumns > 17) s += ", BktDisName - " + bucket_display_name + "\n";
        else s += ", BktDisName - NA\n";
        return s;
    }

    @Override
    protected void finalize() throws Throwable {
        try {
            date_added = null;
            date_modified = null;
            datetaken = null;
        } finally {
            super.finalize();
        }
    }
}
