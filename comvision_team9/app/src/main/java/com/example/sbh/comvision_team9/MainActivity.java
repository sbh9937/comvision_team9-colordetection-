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
 蹂??꾨줈?앺듃???덈뱶濡쒖씠??移대찓?쇰? ?댁슜?섏뿬 ?뱀젙 ?됱쓣 (鍮④컙?? 珥덈줉?? ?뚮??? 寃異쒗븯???꾨줈?앺듃?대떎.
 ?쒖옉??: 2017 06 01
 */
public class MainActivity extends AppCompatActivity implements CameraBridgeViewBase.CvCameraViewListener2{
    static{
        // ?ㅽ뵂 CV 濡쒕뱶
        if(!OpenCVLoader.initDebug()){
            Log.d("TAG","OpenCV NOT LOADED");
        }   else{
            Log.d("TAG","OpenCV LOADED");
        }

    }
    // 珥덇린 媛?
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
        //踰꾪듉???댁슜?섏뿬 ?먰븯?????쇰줈 諛붽퓞
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
        //SC1 怨?SC2??李⑥씠???됱쓣 寃異쒗븳??
        return imgThresholded;
    }
}
