package com.example.sbh.comvision_team9;

import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.Button;

import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.JavaCameraView;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

/*
 본 프로젝트는 안드로이드 카메라를 이용하여 특정 색을 (빨간색, 초록색, 파란색) 검출하는 프로젝트이다.
 시작일 : 2017 06 01
 */
public class MainActivity extends AppCompatActivity implements CameraBridgeViewBase.CvCameraViewListener2{
    static{
        // opencv 로드
        if(!OpenCVLoader.initDebug()){
            Log.d("TAG","OpenCV NOT LOADED");
        }   else{
            Log.d("TAG","OpenCV LOADED");
        }

    }
    // 색 초기값
    int imageLowH = 0;
    int imageHighH = 0;
    int imageLowS = 20;
    int imageHighS = 255;
    int imageLowV = 10;
    int imageHighV = 255;
    Mat imgHSV, imgThresholded;
    Scalar sc1, sc2;
    JavaCameraView cameraView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        sc1 = new Scalar(imageLowH,imageLowS,imageLowV);
        sc2 = new Scalar(imageHighH,imageHighS,imageHighV);
        cameraView = (JavaCameraView)findViewById(R.id.cameraview);
        cameraView.setCameraIndex(0);
        cameraView.setCvCameraViewListener(this);
        cameraView.enableView();
        //버튼을 이용하여 원하는 색 으로 바꿈
        findViewById(R.id.Rbutton).setOnClickListener(rClickListener);
        findViewById(R.id.Gbutton).setOnClickListener(gClickListener);
        findViewById(R.id.Bbutton).setOnClickListener(bClickListener);
    }

    Button.OnClickListener rClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            imageLowH = 100;
            imageHighH = 120;
            sc1 = new Scalar(imageLowH,imageLowS,imageLowV);
            sc2 = new Scalar(imageHighH,imageHighS,imageHighV);
        }
    };

    Button.OnClickListener gClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            imageLowH = 40;
            imageHighH = 80;
            sc1 = new Scalar(imageLowH,imageLowS,imageLowV);
            sc2 = new Scalar(imageHighH,imageHighS,imageHighV);
        }
    };

    Button.OnClickListener bClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            imageLowH = 0;
            imageHighH = 20;
            sc1 = new Scalar(imageLowH,imageLowS,imageLowV);
            sc2 = new Scalar(imageHighH,imageHighS,imageHighV);
        }
    };

    @Override
    protected void onPause() {
        super.onPause();
        cameraView.disableView();
    }

    @Override
    public void onCameraViewStarted(int width, int height) {
        imgHSV = new Mat(width,height, CvType.CV_16UC4);
        imgThresholded = new Mat(width,height, CvType.CV_16UC4);

    }

    @Override
    public void onCameraViewStopped() {

    }

    @Override
    public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame) {
        Imgproc.cvtColor(inputFrame.rgba(),imgHSV,Imgproc.COLOR_BGR2HSV);
        Core.inRange(imgHSV, sc1, sc2,imgThresholded);
        //SC1 과 SC2의 차이의 색을 검출한다.
        return imgThresholded;
    }
}
