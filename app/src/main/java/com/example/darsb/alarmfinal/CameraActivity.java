package com.example.darsb.alarmfinal;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Canvas;
import android.os.Handler;
import android.os.Looper;
import android.support.constraint.solver.widgets.Rectangle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceView;

import org.opencv.core.Point;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.Utils;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase.CvCameraViewFrame;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.CameraBridgeViewBase.CvCameraViewListener2;
import org.opencv.objdetect.CascadeClassifier;
import org.opencv.imgproc.Imgproc;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.Toast;

public class CameraActivity extends AppCompatActivity implements CvCameraViewListener2{

    CameraBridgeViewBase   cameraBridgeViewBase;
     Mat  mRgba;
//     Mat  mGray;

    CascadeClassifier      cascadeClassifier;

    private static String TAG = "CameraActivity";

     Imgproc imgproc;
    int absoluteLogoSize;

    BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            switch (status) {
                case BaseLoaderCallback.SUCCESS: {
                    cameraBridgeViewBase.enableView();
                    Log.e(TAG, "OpenCV loaded successfully");
                    initializeOpenCVDependencies();
                    break;
                }

                default: {
                    super.onManagerConnected(status);
                    break;
                }
            }
        }

    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.e(TAG, "called onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        cameraBridgeViewBase = findViewById(R.id.CameraAs);
        cameraBridgeViewBase.setVisibility(SurfaceView.VISIBLE);
        cameraBridgeViewBase.setCvCameraViewListener(this);
        cameraBridgeViewBase.setCameraIndex(1);
        getSupportActionBar().hide();


//        final Handler handler = new Handler();
//        handler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                if (burger_kingArray.length>=5){
//
//                    Intent intent= new Intent(CameraActivity.this,OffActivity.class);
//                    startActivity(intent);
//                }
//            }
//        }, 4000);

 //       getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (cameraBridgeViewBase != null) {
            cameraBridgeViewBase.disableView();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (cameraBridgeViewBase != null) {
            cameraBridgeViewBase.disableView();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (OpenCVLoader.initDebug()) {
            Log.e("Cv", "Loaded");
            mLoaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS);
        } else
            Log.e("Cv", "Not");
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_0_0,this,mLoaderCallback);
    }


    @Override
    public void onCameraViewStarted(int width, int height) {
        mRgba = new Mat(height,width,CvType.CV_8UC4);
        absoluteLogoSize = (int) (height* 0.2);
    }

    @Override
    public void onCameraViewStopped() {
        mRgba.release();
    }

    @Override
    public Mat onCameraFrame(final CvCameraViewFrame inputFrame) {
         mRgba = inputFrame.rgba();
        Core.flip(mRgba,mRgba,1);
        Log.e(TAG,"CAmera framw chala");

        // Placeholder for the logos found
    //      Imgproc.cvtColor( mRgba,mRgba,Imgproc.COLOR_BayerBG2BGR);
   //         Imgproc.cvtColor( mRgba, mRgba, Imgproc.COLOR_RGBA2RGB);

        final MatOfRect burger_king=  new MatOfRect();
        // Use the classifier to detect the logos
        if (cascadeClassifier != null) {
            cascadeClassifier.detectMultiScale(mRgba, burger_king, 1.1,3,3,
                    new Size(absoluteLogoSize , absoluteLogoSize), new Size());
        }
        final Rect[] burger_kingArray = burger_king.toArray();


        // Get asset manager to get the blue sky or forest from the assets folder
//        AssetManager assetManager = getAssets();
//        InputStream istr;
        Bundle extras = getIntent().getExtras();

        // Loop through all logos found and get their bounding rectangle coordinates
        for (int i = 0; i <burger_kingArray.length; i++){
            double xd1 = burger_kingArray[i].tl().x;
            double yd1 = burger_kingArray[i].tl().y;
            double xd2 = burger_kingArray[i].br().x;
            double yd2 = burger_kingArray[i].br().y;
            int ixd1 = (int) xd1;
            int iyd1 = (int) yd1;
            int ixd2 = (int) xd2;
            int iyd2 = (int) yd2;

            // Create a rectangle around it
//            imgproc.rectangle( mRgba, burger_kingArray[i].tl(), burger_kingArray[i].br(), new Scalar(0, 255, 200));
              Rect roi = new Rect(ixd1, iyd1, ixd2 - ixd1, iyd2 - iyd1);
            Imgproc.rectangle(mRgba,new Point(ixd1,iyd1), new Point(ixd2 ,iyd2),new Scalar(0, 255, 0,255),4);
            imgproc.rectangle(mRgba, burger_kingArray[i].tl(), burger_kingArray[i].br(), new Scalar(0,255,0,255), 3);
            Log.e(TAG,"Rectsfd");
        }

        final Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                    Log.e("Size","Size"+burger_kingArray.length);

                if (burger_kingArray.length>=2){
                    Intent intent= new Intent(CameraActivity.this,AlarmReceiver.class);
                    intent.putExtra("No","1");
                    sendBroadcast(intent);

//                    Intent i = new Intent(CameraActivity.this, AlarmReceiver.class);
//                    Bundle b = new Bundle();
//                    b.putString("No", "1");
//                    i.putExtras(b);
//                    startActivity(i);

              //      startActivity(intent);
                }
                else{
                    Intent intent= new Intent(CameraActivity.this,AlarmReceiver.class);
                    intent.putExtra("No","0");
                    sendBroadcast(intent);
              //      startActivity(intent);
                }
            }
        }, 4000);

        return mRgba;

    }


    private void initializeOpenCVDependencies() {

        try {
            // Copy the resource into a temp file so OpenCV can load it
            InputStream is = getResources().openRawResource(R.raw.cascade);
            File cascadeDir = getDir("cascade", Context.MODE_PRIVATE);
            File mCascadeFile = new File(cascadeDir, "cascade.xml");
            FileOutputStream os = new FileOutputStream(mCascadeFile);

            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = is.read(buffer)) != -1) {
                os.write(buffer, 0, bytesRead);
            }
            is.close();
            os.close();

            // Load the cascade classifier
            cascadeClassifier = new CascadeClassifier(mCascadeFile.getAbsolutePath());
            Log.e(TAG,"cascade Loaded");

        } catch (Exception e) {
            Log.e(TAG, "Error loading cascade", e);
        }

        // Initialise the camera view
        cameraBridgeViewBase.enableView();

    }
}