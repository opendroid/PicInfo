/*
 *
 * Copyright (c) 2015. Pacific Street Ventures, LLC
 * No permission required for "Fair use"
 * @author Ajay Thakur, ajay@pacificstreetventures.com
 * Created on ${DATE}.
 * Version: 0.1
 */

package com.pacificstreetventures.apps.picinfo;

import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by ajaythakur on 4/28/15.
 */
public class OverallPicsSummary {
    private final String TAG = getClass().getSimpleName();
    List<ImageTypeSummaryData> mSummarizedDataList;

    public OverallPicsSummary(List<PicInfo> listSummarizeThis) {

        if (listSummarizeThis != null) {
            // Create and add objects
            ImageTypeSummaryData mBmp = new ImageTypeSummaryData(ImageType.BMP);
            ImageTypeSummaryData mGif = new ImageTypeSummaryData(ImageType.GIF);
            ImageTypeSummaryData mJpg = new ImageTypeSummaryData(ImageType.JPEG);
            ImageTypeSummaryData mPdf = new ImageTypeSummaryData(ImageType.PDF);
            ImageTypeSummaryData mPng = new ImageTypeSummaryData(ImageType.PNG);
            ImageTypeSummaryData mTiff = new ImageTypeSummaryData(ImageType.TIFF);
            ImageTypeSummaryData mUnknown = new ImageTypeSummaryData(ImageType.UNKNOWN);
            mSummarizedDataList = new ArrayList<>();
            mSummarizedDataList.add(mBmp);
            mSummarizedDataList.add(mGif);
            mSummarizedDataList.add(mJpg);
            mSummarizedDataList.add(mPdf);
            mSummarizedDataList.add(mPng);
            mSummarizedDataList.add(mTiff);
            mSummarizedDataList.add(mUnknown);

            for (PicInfo p : listSummarizeThis) {
                ImageType whichTypeImage = p.getImageType();
                if (whichTypeImage != null) {
                    switch (whichTypeImage) {
                        case BMP:
                            mBmp.incrementCount();
                            mBmp.addSize(p.getImageSizeBytes());
                            if (p.hasLocation()) mBmp.incrementHasLocation();
                            if (p.hasMakeModelInfo()) mBmp.incrementHasMakeModel();
                            break;
                        case GIF:
                            mGif.incrementCount();
                            mGif.addSize(p.getImageSizeBytes());
                            if (p.hasLocation()) mGif.incrementHasLocation();
                            if (p.hasMakeModelInfo()) mGif.incrementHasMakeModel();
                            break;
                        case JPEG:
                            mJpg.incrementCount();
                            mJpg.addSize(p.getImageSizeBytes());
                            if (p.hasLocation()) mJpg.incrementHasLocation();
                            if (p.hasMakeModelInfo()) mJpg.incrementHasMakeModel();
                            break;
                        case PDF:
                            mPdf.incrementCount();
                            mPdf.addSize(p.getImageSizeBytes());
                            if (p.hasLocation()) mPdf.incrementHasLocation();
                            if (p.hasMakeModelInfo()) mPdf.incrementHasMakeModel();
                            break;
                        case PNG:
                            mPng.incrementCount();
                            mPng.addSize(p.getImageSizeBytes());
                            if (p.hasLocation()) mPng.incrementHasLocation();
                            if (p.hasMakeModelInfo()) mPng.incrementHasMakeModel();
                            break;
                        case TIFF:
                            mTiff.incrementCount();
                            mTiff.addSize(p.getImageSizeBytes());
                            if (p.hasLocation()) mTiff.incrementHasLocation();
                            if (p.hasMakeModelInfo()) mTiff.incrementHasMakeModel();
                            break;
                        case UNKNOWN:
                        default:
                            mUnknown.incrementCount();
                            mUnknown.addSize(p.getImageSizeBytes());
                            if (p.hasLocation()) mUnknown.incrementHasLocation();
                            if (p.hasMakeModelInfo()) mUnknown.incrementHasMakeModel();
                            break;
                    } // end switch
                } else {
                    Log.d(TAG, "No data type set:" + p);
                }
            } // end for
        } // End if -- null check
    } // end constructor

    public void sortByCountAsc() {
        Collections.sort(mSummarizedDataList);
    }

    public void sortByCountDesc() {
        Collections.sort(mSummarizedDataList, ImageTypeSummaryData.compareDesc);
    }


    public void sortBySizeAsc() {
        Collections.sort(mSummarizedDataList, ImageTypeSummaryData.compareBySizeAsc);
    }

    public void sortBySizeDesc() {
        Collections.sort(mSummarizedDataList, ImageTypeSummaryData.compareBySizeDesc);
    }

    public List<ImageTypeSummaryData> getSummarizedDataList() {
        return mSummarizedDataList;
    }

    @Override
    public String toString() {
        String s = "";

        for (ImageTypeSummaryData summaryData : mSummarizedDataList)
            s += summaryData;

        return s;
    }

}
