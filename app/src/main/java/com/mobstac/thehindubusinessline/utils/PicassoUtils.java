package com.mobstac.thehindubusinessline.utils;

import android.content.Context;
import android.os.Environment;
import android.util.Log;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by arvind on 13/1/17.
 */

public class PicassoUtils {
    private static final String TAG = "PicassoUtils";

    public static void downloadImage(Context mContext, final String imageUrl, ImageView imageView, int placeHolder) {
        Log.i(TAG, "downloadImage: " + imageUrl);
        Picasso picasso = Picasso.with(mContext);
        picasso.load(imageUrl)
                .placeholder(placeHolder)
                .error(placeHolder)
                .into(imageView);

    }

    public static void printLogsInSdCard(String errorMassage) {

        try {
            File sd = Environment.getExternalStorageDirectory();
            if (sd.canWrite()) {
                File logFile = new File("sdcard/log.txt");
                if (!logFile.exists()) {
                    try {
                        logFile.createNewFile();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                try {
                    BufferedWriter buf = new BufferedWriter(new FileWriter(logFile, true));
                    buf.append("Hi Arvind");
                    buf.newLine();
                    buf.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
        }
    }
}
