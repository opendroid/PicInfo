/*
 *
 * Copyright (c) 2015. Pacific Street Ventures, LLC
 * No permission required for "Fair use"
 * @author Ajay Thakur, ajay@pacificstreetventures.com
 * Created on ${DATE}.
 * Version: 0.1
 */

package com.pacificstreetventures.apps.picinfo;

import android.graphics.Bitmap;
import android.graphics.ImageFormat;
import android.media.ExifInterface;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.util.Rational;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

/**
 * Created by ajaythakur on 4/24/15.
 */
public class PicInfo {
    private final String TAG = getClass().getSimpleName();

    private UUID  mImageId; // Unqiue image ID traversing the list
    private Uri mImageFileName; // As provided by Content Provider
    private byte[] mImageThumbNail160x160;
    private ImageFormat mImageFormat;
    private ExifInterface mExifInterfaceData; // Exit Iterface data
    private ImageColumnsInDb mColumnsInDb;

    public PicInfo() {
        mImageId = UUID.randomUUID();
        mImageFileName = null;
        mExifInterfaceData = null;
        mImageThumbNail160x160 = null;

        mColumnsInDb = new ImageColumnsInDb();
        return;
    }

    // Store pic info
    public void ProcessExifPicInfo(Uri filename) {
        // Image UUID
        // mImageId = UUID.randomUUID(); // Generate a random UUID
        mImageFileName = Uri.parse(filename.toString());
        mExifInterfaceData = null;
        mImageThumbNail160x160 = null;
        //
        // Get EXIF data ImageFormat. ImageFormat.JPEG
        // Get mimetype 'mimeType.equalsIgnoreCase("image/jpeg"'
        // MediaStore.Images.Media.CONTENT_TYPE has value 'image/jpeg'
        //
        //
        if (filename != null) {
            File file = new File(filename.toString());
            if (file.exists()) {
                try {
                    // Get Image Format
                    // mImageFormat = new ImageFormat(filename);
                    //
                    // Save EXIF data
                    mExifInterfaceData = new ExifInterface(filename.toString());
                    mImageThumbNail160x160 = mExifInterfaceData.getThumbnail(); // blocking call
                } catch (IOException ex) {
                    Log.e(TAG, "Cant read EXIF for " + filename);
                }
            }
        } // end if
    }

    // EXIF info
    private class ExifJpegData {
        // Picture -- taken data.
        private String      mAperture;
        private String      mDatetime;
        private String      mExposureTime;
        private Integer     mFlash;
        private Rational    mFocus;
        private Integer     mCameraOrientation;
        private Integer     mWhiteBalance;

        // GOS Data -- times.
        private Integer     mGpsAltitude;
        private String      mGpsDateStamp;
        private String      mGpsTimeStamp;
        private String      mGpsLongitude;
        private String      mGpsLatitude;
        private String      mGpsLatitudeRef;
        private String      mGpsLongitudeRef;
        private Integer     mGpsAltitudeRef;
        private String      mGpsProcessingMethod;

        // Camera data -- times.
        private String      mCameraIso;
        private String      mCameraMake;
        private String      mCameraModel;

        public ExifJpegData() {
            // CLean object data state
            mAperture = mDatetime = mExposureTime = null;
            mGpsDateStamp = mGpsTimeStamp = mGpsLongitude = mGpsLatitude = null;
            mGpsLatitudeRef = mGpsLongitudeRef = null;
            mCameraIso = mCameraMake = mCameraModel = null;
            mFlash = mCameraOrientation = mWhiteBalance = -1;
            mGpsAltitude = mGpsAltitudeRef = 0;
            return;
        } // Constructor
    }

    // Columns read in file. -- same name as in database.
    // @link https://android.googlesource.com/platform/packages/apps/Gallery2/+/jb-dev/src/com/android/gallery3d/data/LocalAlbumSet.java
    // for explanation
    class ImageColumnsInDb {
        private long mColumns; // How many columns data is available
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
         * Note found these extra columns on Samsung note 3
         *
         * group_id, spherical_mosaic, addr, langagecode, is_secretbox,
         * weather_ID, sef_file_type, reusable]
         *
         */

        // Setters.
        public void setID(long id) {
            _id = id;
        }
        public void setSize(long sz) {
            _size = sz;
        }
        public void setDisplayName (String displayName) {
            _display_name = displayName;
        }
        public void setMimeType (String mimeType) {
            mime_type  = mimeType;
        }
        public void setTitle (String title) {
            this.title = title;
        }
        public void setDateAdded (long dateAddedMilliSec) {
            date_added = new Date(dateAddedMilliSec);
        }
        public void setDateModified (long dateModifiedMilliSec) {
            date_modified = new Date(dateModifiedMilliSec);
        }
        public void setDateTake (long dateTakenMilliSec) {
            datetaken = new Date(dateTakenMilliSec);
        }
        public void setDescription (String description) {
            this.description = description;
        }
        public void setPicasaId (String id) {
            this.picasa_id = id;
        }
        public void setPrivate (int isprivate) {
            this.isprivate = isprivate;
        }
        public void setLocation(double _latitide, double _longitude) {
            latitude = _latitide;
            longitude = _longitude;
        }
        public void setWidthHeight(int _width, int _height) {
            width = _width;
            height = _height;
        }
        public void setOrientation (int orientation) {
            this.orientation = orientation;
        }
        public void setBucket (String bucketId, String bucketDisplayName) {
            bucket_id = bucketId;
            bucket_display_name = bucketDisplayName;
        }
        public void setThumbnail(int thumbnail) {
            mini_thumb_magic = thumbnail;
        }
    } // End inner class

    // Accessors to set Inner class:
    public void setColumns (long nColumns) {
        mColumnsInDb.mColumns = nColumns;
    }
    public void setID(long id) {
        mColumnsInDb._id = id;
    }
    public void setSize(long sz) {
        mColumnsInDb._size = sz;
    }
    public void setDisplayName (String displayName) {
        mColumnsInDb._display_name = displayName;
    }
    public void setMimeType (String mimeType) {
        mColumnsInDb.mime_type  = mimeType;
    }
    public void setTitle (String title) {
        mColumnsInDb.title = title;
    }
    public void setDateAdded (long dateAddedMilliSec) {
        mColumnsInDb.date_added = new Date(dateAddedMilliSec);
    }
    public void setDateModified (long dateModifiedMilliSec) {
        mColumnsInDb.date_modified = new Date(dateModifiedMilliSec);
    }
    public void setDateTaken (long dateTakenMilliSec) {
        mColumnsInDb.datetaken = new Date(dateTakenMilliSec);
    }
    public void setDescription (String description) {
        mColumnsInDb.description = description;
    }
    public void setPicasaId (String id) {
        mColumnsInDb.picasa_id = id;
    }
    public void setPrivate (int isprivate) {
        mColumnsInDb.isprivate = isprivate;
    }
    public void setLocation(double _latitide, double _longitude) {
        mColumnsInDb.latitude = _latitide;
        mColumnsInDb.longitude = _longitude;
    }
    public void setWidthHeight(int _width, int _height) {
        mColumnsInDb.width = _width;
        mColumnsInDb.height = _height;
    }
    public void setBucket (String bucketId, String bucketDisplayName) {
        mColumnsInDb.bucket_id = bucketId;
        mColumnsInDb.bucket_display_name = bucketDisplayName;
    }
    public void setThumbnailMagic(int thumbnail) {
        mColumnsInDb.mini_thumb_magic = thumbnail;
    }
    public void setOrientation (int orientation) {
        mColumnsInDb.orientation = orientation;
    }

    @Override
    public String toString() {

        DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");

        String s ="";
        s += "Columns:" + mColumnsInDb.mColumns;
        s += ",ID:" + mColumnsInDb._id + ", Sz:" + mColumnsInDb._size;
        s += ",Name:" + mColumnsInDb._display_name;
        s += "\nMIME:" + mColumnsInDb.mime_type + ",Title:" +  mColumnsInDb.title;
        if (mColumnsInDb.mColumns > 13) s += "\nTaken: " + df.format(mColumnsInDb.datetaken);
        else s += "\nTaken: NA";
        s += ",Added: " + df.format(mColumnsInDb.date_added);
        s += ",Modified: " + df.format(mColumnsInDb.date_modified);
        s += "\nDescription:" + mColumnsInDb.description;
        s += "\nPicasaId:" + mColumnsInDb.picasa_id + ",Private:" + mColumnsInDb.isprivate;
        if (mColumnsInDb.mColumns > 19)
            s += ",{WH:(" + mColumnsInDb.width + "," + mColumnsInDb.height + ")}";
        else s += ",{WH:(NA,NA)}";
        if (mColumnsInDb.mColumns > 15) s += ",ThumbNail:" + mColumnsInDb.mini_thumb_magic;
        else s += ",ThumbNail:NA";
        if (mColumnsInDb.mColumns > 14) s += ",Orientation:" + mColumnsInDb.orientation;
        else s += ",Orientation:NA";
        if (mColumnsInDb.mColumns > 12)
            s += "\nLocation:(" + mColumnsInDb.latitude +"," + mColumnsInDb.longitude + ")";
        else
            s += "\nLocation:(NA,NA)";
        if (mColumnsInDb.mColumns > 16) s += "\nBktId:" + mColumnsInDb.bucket_id;
        else s += "\nBktId:NA";
        if (mColumnsInDb.mColumns > 17) s += ",BktDisName:" + mColumnsInDb.bucket_display_name;
        else s += ",BktDisName:NA";
        return s;
    }
}
