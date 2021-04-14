package com.example.facialrecognition;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import com.google.mlkit.vision.face.Face;

import java.util.List;

public class FaceView extends View {
    private Bitmap mBitmap;
    private List<Face> mFaces;

    public FaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    void setContent(Bitmap bitmap, List<Face> faces){
        Log.v("***DRAW***", "setContent");

        mBitmap = bitmap;

        mFaces = faces;
        invalidate();
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if ((mBitmap != null) && (mFaces != null)) {
            double scale = drawBitmap(canvas);
            Log.v("***DRAW***", " " + scale);
            drawFaceAnnotations(canvas, scale);
        }
    }

    private double drawBitmap(Canvas canvas) {
        double viewWidth = canvas.getWidth();
        double viewHeight = canvas.getHeight();
        double imageWidth = mBitmap.getWidth();
        double imageHeight = mBitmap.getHeight();
        double scale = Math.min(viewWidth/imageWidth, viewHeight/imageHeight);

        Rect destBounds = new Rect(0, 0, (int)(imageWidth * scale), (int)(imageHeight * scale));
        canvas.drawBitmap(mBitmap, null, destBounds, null);
        return scale;
    }

    private void drawFaceRectangle(Canvas canvas, double scale){
        Paint paint = new Paint();
        paint.setColor(Color.GREEN);
        paint.setStyle(Paint.Style.Stroke);
        paint.setStrokeWidth(5);

        for(int i = 0; i < mFaces.size(); i++){
            Face face = mFaces.valueAt(i);
            canvas.drawRect((float)(face.getPosition().x * scale),
                    (float)(face.getPosition().y * scale),
                    (float)((face.getPosition().x + face.getWidth()) * scale),
                    (float)((face.getPosition().y + face.getHeight()) * scale),
                    paint);
        }
    }

    private void drawFaceAnnotations(Canvas canvas, double scale) {
        Paint paint = new Paint();
        paint.setColor(Color.GREEN);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(5);

        for(Face face : mFaces) {
            Rect bounds = face.getBoundingBox();
            int left = (int)(bounds.left*scale);
            int right = (int)(bounds.right*scale);
            int top = (int)(bounds.top*scale);
            int bottom = (int)(bounds.bottom*scale);
            bounds.set(left, top, right, bottom);

            canvas.drawRect(bounds, paint);
        }
    }
}