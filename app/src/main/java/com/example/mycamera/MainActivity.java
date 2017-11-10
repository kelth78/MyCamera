package com.example.mycamera;

import android.hardware.Camera;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.widget.FrameLayout;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "CamTestActivity"; // Kel

    Preview mPreview; // Kel
    Camera mCamera; // Kel

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d(TAG, "onCreate");
        mPreview = new Preview(this, (SurfaceView)findViewById(R.id.surfaceView));
        mPreview.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
        ((FrameLayout) findViewById(R.id.layout)).addView(mPreview);

        mPreview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCamera.takePicture(/*shutterCallback*/null, rawCallback, /*SavePic*/null);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        int numCams = Camera.getNumberOfCameras();
        Log.v(TAG, "Number of cameras found: " + numCams);

        if (numCams > 0) {
            try {
                // id 0 default rear cam, 1 front cam
                mCamera = Camera.open(1/*id*/);
                mCamera.startPreview();
                mPreview.setCamera(mCamera);
            } catch (RuntimeException e) {
                Log.e(TAG, "Fail to open camera!");
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mCamera != null) {
            Log.d(TAG, "onPause");
            mCamera.stopPreview();
            mPreview.setCamera(null);
            mCamera.release();
            mCamera = null;
        }
    }

    Camera.PictureCallback rawCallback = new Camera.PictureCallback() {
        @Override
        public void onPictureTaken(byte[] bytes, Camera camera) {
            Log.d(TAG, "Picture taken!");
        }
    };
}