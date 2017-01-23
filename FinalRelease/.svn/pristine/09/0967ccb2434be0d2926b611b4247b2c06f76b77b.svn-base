/*
Created By: Harvey Chen
Created Date: 02/26/2016
Project: Resistor Color Band Decoder for ECE 573 at University of Arizona
Last Edited:
*/
package com.app.harvey.resistorcolorbanddecoder;

// Copyright (c) <2016>, <Harvey Chen, Zichang Guo, Litong Shen>
//   All rights reserved.
//
//   Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:
//
//   1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.
//
//   2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.
//
//   3. Neither the name of the copyright holder nor the names of its contributors may be used to endorse or promote products derived from this software without specific prior written permission.
//
//   THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
//   FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
//   DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
//   WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.CvType;
import org.opencv.core.Mat;

public class MainActivity extends AppCompatActivity
{
    //static{System.loadLibrary("opencv_java3");}
    private static final int CAPTURE_IMAGE_REQUEST_CODE = 100; //  value use to match with requestCode

    ImageView mImageView;
    ImageView mImageView1; // For test case
    TextView mTextView, mTextInstructions, mTextColors;

    Bitmap srcBitmap, dstBitmap;
    public int ohm=0;

    Button capButton;   // capture image, button
    Button exitButton;  // exit the APP, button2


    /*
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (!OpenCVLoader.initDebug()) {
            Log.e(this.getClass().getSimpleName(), "  OpenCVLoader.initDebug(), not working.");
        } else {
            Log.d(this.getClass().getSimpleName(), "  OpenCVLoader.initDebug(), working.");
        }

        mImageView = (ImageView) findViewById(R.id.imagePreview);
        mImageView1 = (ImageView) findViewById(R.id.imagePreview1); // for test case
        mTextView = (TextView)findViewById(R.id.textView); // Shows user calculated resistance value
        mTextInstructions = (TextView)findViewById(R.id.instructions); // Shows user how to use this application
        mTextInstructions.setVisibility(View.VISIBLE);
        mTextColors = (TextView)findViewById(R.id.colorStatus);
        capButton = (Button) findViewById(R.id.captureButton);
        exitButton = (Button) findViewById(R.id.quitButton);


        // Capture button = access the camera
        capButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                takePicture();
            }
        });
        // Exit button = end of application
        exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                finish();
            }
        });

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    private void takePicture()
    {
        // create Intent to take a picture
        Intent intentTakePic = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        // start the image capture Intent
        if (intentTakePic.resolveActivity(getPackageManager()) != null)
        {
            startActivityForResult(intentTakePic, CAPTURE_IMAGE_REQUEST_CODE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        int bmpHeight, bmpWidth;
        if (requestCode == CAPTURE_IMAGE_REQUEST_CODE && resultCode == RESULT_OK)
        {
            Bundle extras = data.getExtras();
            srcBitmap = (Bitmap) extras.get("data"); //convert to bitmap format
            //dstBitmap = toGrayscale(srcBitmap);

            // convert bitmap to mat
            Mat matSrc = new Mat(srcBitmap.getHeight(),srcBitmap.getWidth(), CvType.CV_8UC3);
            Utils.bitmapToMat(srcBitmap,matSrc);
            Resistor res = new Resistor(matSrc);
            //Mat temp=res.band(2,7);//test, (block size, color code)
            //Mat temp_resize = new Mat.zeros(temp.height(),temp.width(),CvType.CV_8UC1); //test
            ohm = res.resistance_cal();
            String colors="";
            boolean[] colorTF = res.Color_detected;
            for(int i=0;i<colorTF.length;i++)
            {
                if(colorTF[i]==true)
                {
                    colors=colors.concat("T ");
                }
                else
                {
                    colors=colors.concat("F ");
                }
            }
            //Bitmap testBitmap = Bitmap.createBitmap(temp.cols(), temp.rows(), Bitmap.Config.ARGB_8888);// test
            try {
                Utils.matToBitmap(matSrc, srcBitmap);
                //Utils.matToBitmap(temp, testBitmap); //test
            }
            catch (Exception e)
            {
                System.out.println(e.getMessage());
            }
            mTextInstructions.setVisibility(View.INVISIBLE);
            mImageView.setImageBitmap(srcBitmap);
            //mImageView.setImageBitmap(testBitmap);//test
            mTextColors.setVisibility(View.INVISIBLE);
            mTextColors.setText(colors);
            mTextView.setText("Resistance = " + ohm + " ohm");

        }
    }

    // Convert color bitmap to grayscale bitmap
    public Bitmap toGrayscale(Bitmap bmpOriginal)
    {
        int width, height;
        height = bmpOriginal.getHeight();
        width = bmpOriginal.getWidth();

        Bitmap bmpGrayscale = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
        Canvas c = new Canvas(bmpGrayscale);
        Paint paint = new Paint();
        ColorMatrix cm = new ColorMatrix();
        cm.setSaturation(0);
        ColorMatrixColorFilter filter = new ColorMatrixColorFilter(cm);
        paint.setColorFilter(filter);
        c.drawBitmap(bmpOriginal, 0, 0, paint);
        return bmpGrayscale;
    }

    @Override
    public void onStart()
    {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.app.harvey.resistorcolorbanddecoder/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop()
    {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.app.harvey.resistorcolorbanddecoder/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }
}