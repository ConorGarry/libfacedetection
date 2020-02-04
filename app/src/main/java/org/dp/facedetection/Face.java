package org.dp.facedetection;

import org.opencv.core.Rect;

/**
 * Stay Hungry Stay Foolish
 * Author: dp on 2019/3/25 12:51
 */
public class Face {
    public Rect faceRect;
    public int faceConfidence;
    public int faceAngle;

    public android.graphics.Rect getAndroidRect() {
        return new android.graphics.Rect(
                faceRect.x,
                faceRect.y,
                faceRect.x + faceRect.width,
                faceRect.y + faceRect.height
        );
    }
}
