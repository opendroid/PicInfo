/*
 *
 * Copyright (c) 2015. Pacific Street Ventures, LLC
 * No permission required for "Fair use"
 * @author Ajay Thakur, ajay@pacificstreetventures.com
 * Created on ${DATE}.
 * Version: 0.1
 */

package com.pacificstreetventures.apps.picinfo;
import android.net.Uri;
import android.util.Log;

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.Tag;

import java.io.File;
import java.io.IOException;
import java.util.Locale;
import java.util.UUID;

/**
 * Created by ajaythakur on 4/24/15.
 */
public class PicInfo {
    private final String TAG = getClass().getSimpleName();
    private ImageType mType;
    private UUID mImageId; // Unique image ID traversing the list
    private Uri mImageFileName; // As provided by Content Provider
    private JpegExifData mJpegExifData;
    private Metadata mImageMetaData; // Exit Interface data
    private ImageDbColumns mColumnsInDb;
    private String mError;

    public PicInfo() {
        mImageId = UUID.randomUUID();
        mImageFileName = null;
        mImageMetaData = null;
        mJpegExifData = null;
        mColumnsInDb = new ImageDbColumns();
        mError = null;
    }

    // Call this after other elenets have been set.
    public void processImageMetaData(final String filename) {
        // Image UUID
        // mImageId = UUID.randomUUID(); // Generate a random UUID

        mImageFileName = Uri.parse(filename);
        mImageMetaData = null;

        // Get EXIF data ImageFormat. ImageFormat.JPEG
        // Get mimetype 'mimeType.equalsIgnoreCase("image/jpeg"'
        // MediaStore.Images.Media.CONTENT_TYPE has value 'image/jpeg'
        if (filename != null) {
            File imageFile = new File(filename);
            if (imageFile.exists()) {
                // Use Android Library to get EXIF info.
                // Use mime type from database not image itself ('easy')
                if ((mColumnsInDb != null) &&
                        (mType == ImageType.JPEG)) {
                    fetchExifDataForJpeg(filename);
                }

                // Use library metadata-extractor-2.8.1.jar and 'xmpcore-5.1.2.jar'
                try {
                    mImageMetaData = ImageMetadataReader.readMetadata(imageFile);
                    mError = null;
                } catch (ImageProcessingException ex) {
                    mImageMetaData = null;
                    mError = "\n[IMAGEDB] Error Image processing exception on: " + filename;
                    Log.e(TAG, "ProcessingException: filename:" + filename + "\n", ex);
                    Log.d(TAG, "MetaData:" + this.toString());
                } catch (IOException ex) {
                    Log.e(TAG, "IOException: filename:" + filename + "\n", ex);
                    mImageMetaData = null;
                    mError = "\n[IMAGEDB] Error Can not read image: " + filename;
                    // Log.d(TAG, "MetaData:" + this.toString());
                }

            } // end if 'imageFile.exists()'
        } // end if 'filename != null'
    }

    // Helper function.
    private void fetchExifDataForJpeg(final String filename) {
        try {
            mJpegExifData = new JpegExifData(filename);
            // Log.d(TAG,"fetchExifDataForJpeg, TAG infor for file: " + filename);
            // Log.e(TAG,"Data:" + mJpegExifData);
        } catch (IOException | OutOfMemoryError ex) {
            mJpegExifData = null;
        }
    }


    // Accessors to set Inner class:
    public void setColumns(int nColumns) {
        mColumnsInDb.setColumns(nColumns);
    }
    public void setID(long id) {
        mColumnsInDb.setID(id);
    }

    public ImageType getImageType() {
        return mType;
    }
    public void setSize(long sz) {
        mColumnsInDb.setSize(sz);
    }
    public void setDisplayName (String displayName) {
        mColumnsInDb.setDisplayName(displayName);
    }
    public void setMimeType (String mimeType) {
        mColumnsInDb.setMimeType(mimeType);
        if (mimeType.toLowerCase(Locale.US).contains("jpe") ||
                mimeType.toLowerCase(Locale.US).contains("jpeg") ||
                mimeType.toLowerCase(Locale.US).contains("jpg")) {
            mType = ImageType.JPEG;
        } else if (mimeType.toLowerCase(Locale.US).contains("png")) {
            mType = ImageType.PNG;
        } else if (mimeType.toLowerCase(Locale.US).contains("bmp")) {
            mType = ImageType.BMP;
        } else if (mimeType.toLowerCase(Locale.US).contains("gif")) {
            mType = ImageType.GIF;
        } else if (mimeType.toLowerCase(Locale.US).contains("pdf")) {
            mType = ImageType.PDF;
        } else if (mimeType.toLowerCase(Locale.US).contains("tiff")) {
            mType = ImageType.TIFF;
        } else {
            mType = ImageType.UNKNOWN;
        }
    }
    public void setTitle (String title) {
        mColumnsInDb.setTitle(title);
    }
    public void setDateAdded (long dateAddedMilliSec) {
        mColumnsInDb.setDateAdded(dateAddedMilliSec);
    }
    public void setDateModified (long dateModifiedMilliSec) {
        mColumnsInDb.setDateModified(dateModifiedMilliSec);
    }
    public void setDateTaken (long dateTakenMilliSec) {
        mColumnsInDb.setDateTakenMilliSec(dateTakenMilliSec);
    }
    public void setDescription (String description) {
        mColumnsInDb.setDescription(description);
    }
    public void setPicasaId (String id) {
        mColumnsInDb.setPicasaId(id);
    }
    public void setPrivate (int isprivate) {
        mColumnsInDb.setPrivate(isprivate);
    }
    public void setLocation(double _latitide, double _longitude) {
        mColumnsInDb.setLocation(_latitide, _longitude);
    }
    public void setWidthHeight(int _width, int _height) {
        mColumnsInDb.setWidthHeight(_width, _height);
    }
    public void setBucket (String bucketId, String bucketDisplayName) {
        mColumnsInDb.setBucket(bucketId, bucketDisplayName);
    }
    public void setThumbnailMagic(int thumbnail) {
        mColumnsInDb.setThumbnailMagic(thumbnail);

    }
    public void setOrientation (int orientation) {
        mColumnsInDb.setOrientation(orientation);
    }

    @Override
    public String toString() {

        String s = "";

        if (mImageFileName != null) s += "\n[IMAGEDB] Filename - " + mImageFileName.toString()
                + ", MIME:" + mType;
        if (mColumnsInDb != null) s += mColumnsInDb; // Print DB columns
        if (mError != null) s += mError; //Error in this module
        if (mJpegExifData != null) s += mJpegExifData; // Read EXIF data

        // Read MetaData from Library
        if (mImageMetaData != null) {
            for (Directory directory : mImageMetaData.getDirectories()) {
                // Each Directory stores values in Tag objects
                for (Tag tag : directory.getTags()) {
                    s += tag.toString() + "\n";
                }
                // Each Directory may also contain error messages
                if (directory.hasErrors()) {
                    for (String error : directory.getErrors()) {
                        s += "ERROR: " + error;
                    }
                } // end if
            } // end for 'all directories'
        }

        return s;
    }

    @Override
    protected void finalize() throws Throwable {
        try {
            mImageId = null;
            mImageFileName = null;
            mImageMetaData = null;
            mJpegExifData = null;
            mColumnsInDb = null;
        } finally {
            super.finalize();
        }
    }
}
