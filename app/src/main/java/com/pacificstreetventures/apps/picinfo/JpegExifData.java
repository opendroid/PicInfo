/*
 *
 * Copyright (c) 2015. Pacific Street Ventures, LLC
 * No permission required for "Fair use"
 * @author Ajay Thakur, ajay@pacificstreetventures.com
 * Created on ${DATE}.
 * Version: 0.1
 */

package com.pacificstreetventures.apps.picinfo;

import android.media.ExifInterface;
import android.util.Log;


import java.io.IOException;

/**
 * Created by ajaythakur on 4/26/15.
 */
public class JpegExifData {
    private final String TAG = getClass().getSimpleName();
    private String mFilename;           // Filename with this object
    private ExifInterface mExifData;    // Exif Interface Object
    private String mAperture;           // 'TAG_APERTURE'
    private String mDateTime;           // 'TAG_DATETIME'
    private String mExposureTime;       // 'TAG_EXPOSURE_TIME'
    private int mFlash;              // 'TAG_FLASH'
    private String mFocalLength;      // 'TAG_FOCAL_LENGTH'
    private Double mGpsAltitude;        // 'TAG_GPS_ALTITUDE'
    private int mGpsAltitudeRef;     // 'TAG_GPS_ALTITUDE_REF'
    private float mGpsLatitude;        // 'TAG_GPS_LATITUDE'
    private String mGpsLatitudeRef;     // 'TAG_GPS_LATITUDE_REF'
    private float mGpsLongitude;       // 'TAG_GPS_LONGITUDE'
    private String mGpsLongitudeRef;    // 'TAG_GPS_LONGITUDE_REF'
    private String mGpsProcessingMethod;// 'TAG_GPS_PROCESSING_METHOD'
    private int mImageLength;        // 'TAG_IMAGE_LENGTH'
    private int mImageWidth;         // 'TAG_IMAGE_WIDTH'
    private String mIso;                // 'TAG_ISO'
    private String mMake;               // 'TAG_MAKE'
    private String mModel;              // 'TAG_MODEL'
    private int mOrientation;        // 'TAG_ORIENTATION'
    private int mWhiteBalance;       // 'TAG_WHITE_BALANCE'
    private String mGpsDateStamp;       // 'TAG_GPS_DATESTAMP'
    private String mGpsTimestamp;       // 'TAG_GPS_TIMESTAMP'

    private String mError;              // Error string

    // Constructor
    public JpegExifData(final String fileName) throws IOException, OutOfMemoryError {
        mFilename = fileName;
        mExifData = null;
        try {
            mExifData = new ExifInterface(fileName);
        } catch (IOException | OutOfMemoryError ex) {
            mFilename = null;
            mExifData = null;
            Log.e(TAG, "IOException | OutOfMemoryError:", ex);
            mError = "IOException or OutOfMemoryError on filename:" + fileName;
            throw ex; // rethrow
        }

        // Extract the data.
        String val;
        mAperture = getExifTag(mExifData, ExifInterface.TAG_APERTURE);
        mDateTime = getExifTag(mExifData, ExifInterface.TAG_DATETIME);
        mExposureTime = getExifTag(mExifData, ExifInterface.TAG_EXPOSURE_TIME);
        val = getExifTag(mExifData, ExifInterface.TAG_FLASH);
        if (!val.isEmpty()) mFlash = Integer.valueOf(val);
        mFocalLength = getExifTag(mExifData, ExifInterface.TAG_FOCAL_LENGTH);
        mGpsAltitude = mExifData.getAltitude(0);
        val = getExifTag(mExifData, ExifInterface.TAG_GPS_ALTITUDE_REF);
        if (!val.isEmpty()) mGpsAltitudeRef = Integer.valueOf(val);
        // Get Lat/Long
        float[] latlon = new float[2];
        if (mExifData.getLatLong(latlon)) {
            mGpsLatitude = latlon[0];
            mGpsLongitude = latlon[1];
        }
        mGpsLongitudeRef = getExifTag(mExifData, ExifInterface.TAG_GPS_LONGITUDE_REF);
        mGpsLatitudeRef = getExifTag(mExifData, ExifInterface.TAG_GPS_LATITUDE_REF);
        mGpsProcessingMethod = getExifTag(mExifData, ExifInterface.TAG_GPS_PROCESSING_METHOD);
        val = getExifTag(mExifData, ExifInterface.TAG_IMAGE_LENGTH);
        if (!val.isEmpty()) mImageLength = Integer.valueOf(val);
        val = getExifTag(mExifData, ExifInterface.TAG_IMAGE_WIDTH);
        if (!val.isEmpty()) mImageWidth = Integer.valueOf(val);
        mIso = getExifTag(mExifData, ExifInterface.TAG_ISO);
        mMake = getExifTag(mExifData, ExifInterface.TAG_MAKE);
        mModel = getExifTag(mExifData, ExifInterface.TAG_MODEL);
        val = getExifTag(mExifData, ExifInterface.TAG_ORIENTATION);
        if (!val.isEmpty()) mOrientation = Integer.valueOf(val);
        val = getExifTag(mExifData, ExifInterface.TAG_WHITE_BALANCE);
        if (!val.isEmpty()) mWhiteBalance = Integer.valueOf(val);
        mGpsDateStamp = getExifTag(mExifData, ExifInterface.TAG_GPS_DATESTAMP);
        mGpsTimestamp = getExifTag(mExifData, ExifInterface.TAG_GPS_TIMESTAMP);
    }

    private String getExifTag(ExifInterface exifInterface, String tag) {
        String value = exifInterface.getAttribute(tag);
        return (null != value ? value : "");
    }

    @Override
    public String toString() {
        String s = "";
        if (mFilename != null) {
            s += "[EXIF] Model - " + mModel + ", Make - " + mMake;
            s += ", Datetime - " + mDateTime;
            s += "\n[EXIF] Aperture - " + mAperture;
            s += ", Exposure time - " + mExposureTime;
            s += ", Flash - " + mFlash;
            s += "\n[EXIF] Focal Length - " + mFocalLength + ", ISO" + mIso;
            s += ", Orientation - " + mOrientation + ", White balance - " + mWhiteBalance;
            s += "\n[EXIF] WxL - (" + mImageWidth + "," + mImageLength + ")";
            s += "\n[EXIF] GPS Data - Processing method - " + mGpsProcessingMethod;
            s += "\n[EXIF] GPS Date - " + mGpsDateStamp + ", Time - " + mGpsTimestamp;
            s += "\n[EXIF] GPS Location - (" + mGpsLatitude + "," + mGpsLongitude + "," + mGpsAltitude + ")";
            s += "\n[EXIF] GPS Ref - (" + mGpsLatitudeRef + "," + mGpsLongitudeRef + ",";
            s += mGpsAltitudeRef + ")\n";
        }
        if (mError != null) {
            s += "\n[EXIF] Error - " + mError;
        }
        return s;
    }
}
