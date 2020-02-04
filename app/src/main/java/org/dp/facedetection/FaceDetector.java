package org.dp.facedetection;

import android.graphics.Bitmap;
import android.os.Build;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.opencv.android.Utils;
import org.opencv.core.MatOfRect;

public class FaceDetector {

    private static FaceDetector INSTANCE;

    public static FaceDetector getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new FaceDetector();
        }
        return INSTANCE;
    }

    private boolean mIsInit = false;

    private void init() {
        try {
            System.loadLibrary("facedetection");
            mIsInit = true;
        } catch (UnsatisfiedLinkError e) {
            String abi = Build.SUPPORTED_ABIS[0];
            Log.e("libfacedetection", "Couldn't load OpenCV, arch: " + abi);
        }
    }

    private native Face[] facedetect(long matOfRectAddr);

    public List<Face> detect(Bitmap bitmap) {
        if (!mIsInit) {
            init();
        }
        if (!mIsInit) {
            Log.e("libfacedetection", "Can't detect faces, lib cannot init");
            return new ArrayList<>();
        }
        MatOfRect matOfRect = new MatOfRect();
        //val bmp2 = bmp.copy(bmp.config, true)
        Utils.bitmapToMat(bitmap, matOfRect);
        Face []facesArray = facedetect(matOfRect.getNativeObjAddr());
        if (facesArray != null) {
            return Arrays.asList(facesArray);
        }
        return new ArrayList<>();
    }
}
