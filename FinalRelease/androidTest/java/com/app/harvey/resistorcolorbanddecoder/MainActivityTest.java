package com.app.harvey.resistorcolorbanddecoder;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.ImageView;
import android.util.Log;

import org.opencv.android.Utils;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import static org.opencv.imgproc.Imgproc.resize;


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

public class MainActivityTest extends ActivityInstrumentationTestCase2<MainActivity> {
    public MainActivityTest() {
        super(MainActivity.class);
    }



    //  test 1: red can be detect and located
    public void test1() {
        boolean[] colorTF;
        int index = 0;
        int index1 = 1 ;
        int[] location=new int[3];


        MainActivity activity = getActivity();
        ImageView image = (ImageView) activity.findViewById(R.id.imagePreview1);
        Bitmap icon = BitmapFactory.decodeResource(image.getResources(),
                R.drawable.origin0);

        Log.v("Width and Height", "" + icon.getWidth() + " " + icon.getHeight());
        Mat matSrc = new Mat(icon.getHeight(), icon.getWidth(), CvType.CV_8UC3);
        Utils.bitmapToMat(icon, matSrc);

        Mat I = new Mat(152, 250, CvType.CV_8UC3);
        Imgproc.resize(matSrc, I, new Size(152, 250)); //double the scale


        Resistor res = new Resistor(I);
        int ohm = res.resistance_cal();
        colorTF = res.Color_detected;
//        assertNotSame(false, colorTF[5]);
//        assertNotSame(false, colorTF[2]);
//        assertNotSame(false, colorTF[6]);
//        assertEquals(5600, ohm);
        for (int i = 1; i < 5; i++)
        {
            if (ohm%10 == 0 && ohm/10 !=0)
            {index++;
                ohm = ohm/10;}
            else
            {location[index1] = ohm%10;
                index1--;
            ohm = ohm/10;}

        }
        location[2] = index;
        assertEquals(2,location[2]);

    }

    //  test 2: green can be detect and located
    public void test2() {
        int index = 0;
        int index1 = 1 ;
        int[] location=new int[3];


        MainActivity activity = getActivity();
        ImageView image = (ImageView) activity.findViewById(R.id.imagePreview1);
        Bitmap icon = BitmapFactory.decodeResource(image.getResources(),
                R.drawable.origin0);

        Log.v("Width and Height", "" + icon.getWidth() + " " + icon.getHeight());
        Mat matSrc = new Mat(icon.getHeight(), icon.getWidth(), CvType.CV_8UC3);
        Utils.bitmapToMat(icon, matSrc);

        Mat I = new Mat(152, 250, CvType.CV_8UC3);
        Imgproc.resize(matSrc, I, new Size(152, 250)); //double the scale


        Resistor res = new Resistor(I);
        int ohm = res.resistance_cal();

        for (int i = 1; i < 5; i++)
        {
            if (ohm%10 == 0 && ohm/10 !=0)
            {index++;
                ohm = ohm/10;}
            else
            {location[index1] = ohm%10;
                index1--;
                ohm = ohm/10;}

        }
        location[2] = index;
        assertEquals(5,location[0]);

    }

    //  test 3: blue can be detect and located
    public void test3() {
        int index = 0;
        int index1 = 1 ;
        int[] location=new int[3];


        MainActivity activity = getActivity();
        ImageView image = (ImageView) activity.findViewById(R.id.imagePreview1);
        Bitmap icon = BitmapFactory.decodeResource(image.getResources(),
                R.drawable.origin0);

        Log.v("Width and Height", "" + icon.getWidth() + " " + icon.getHeight());
        Mat matSrc = new Mat(icon.getHeight(), icon.getWidth(), CvType.CV_8UC3);
        Utils.bitmapToMat(icon, matSrc);

        Mat I = new Mat(152, 250, CvType.CV_8UC3);
        Imgproc.resize(matSrc, I, new Size(152, 250)); //double the scale


        Resistor res = new Resistor(I);
        int ohm = res.resistance_cal();

        for (int i = 1; i < 5; i++)
        {
            if (ohm%10 == 0 && ohm/10 !=0)
            {index++;
                ohm = ohm/10;}
            else
            {location[index1] = ohm%10;
                index1--;
                ohm = ohm/10;}

        }
        location[2] = index;
        assertEquals(6,location[1]);

    }

    //  test 4: yellow can be detect and located
    public void test4() {
        int index = 0;
        int index1 = 1 ;
        int[] location=new int[3];


        MainActivity activity = getActivity();
        ImageView image = (ImageView) activity.findViewById(R.id.imagePreview1);
        Bitmap icon = BitmapFactory.decodeResource(image.getResources(),
                R.drawable.yellowpurple);

        Log.v("Width and Height", "" + icon.getWidth() + " " + icon.getHeight());
        Mat matSrc = new Mat(icon.getHeight(), icon.getWidth(), CvType.CV_8UC3);
        Utils.bitmapToMat(icon, matSrc);

        Mat I = new Mat(152, 250, CvType.CV_8UC3);
        Imgproc.resize(matSrc, I, new Size(152, 250)); //double the scale


        Resistor res = new Resistor(I);
        int ohm = res.resistance_cal();

        for (int i = 1; i < 5; i++)
        {
            if (ohm%10 == 0 && ohm/10 !=0)
            {index++;
                ohm = ohm/10;}
            else
            {location[index1] = ohm%10;
                index1--;
                ohm = ohm/10;}

        }
        location[2] = index;
        assertEquals(4,location[0]);

    }

    //  test 5: purple can be detect and located
    public void test5() {
        int index = 0;
        int index1 = 1 ;
        int[] location=new int[3];


        MainActivity activity = getActivity();
        ImageView image = (ImageView) activity.findViewById(R.id.imagePreview1);
        Bitmap icon = BitmapFactory.decodeResource(image.getResources(),
                R.drawable.yellowpurple);

        Log.v("Width and Height", "" + icon.getWidth() + " " + icon.getHeight());
        Mat matSrc = new Mat(icon.getHeight(), icon.getWidth(), CvType.CV_8UC3);
        Utils.bitmapToMat(icon, matSrc);

        Mat I = new Mat(152, 250, CvType.CV_8UC3);
        Imgproc.resize(matSrc, I, new Size(152, 250)); //double the scale


        Resistor res = new Resistor(I);
        int ohm = res.resistance_cal();

        for (int i = 1; i < 5; i++)
        {
            if (ohm%10 == 0 && ohm/10 !=0)
            {index++;
                ohm = ohm/10;}
            else
            {location[index1] = ohm%10;
                index1--;
                ohm = ohm/10;}

        }
        location[2] = index;
        assertEquals(7,location[1]);

    }


    //  test 6: orange can be detect and located
    public void test6() {
        boolean[] colorTF;
        int index = 0;
        int index1 = 1 ;
        int[] location=new int[3];


        MainActivity activity = getActivity();
        ImageView image = (ImageView) activity.findViewById(R.id.imagePreview1);
        Bitmap icon = BitmapFactory.decodeResource(image.getResources(),
                R.drawable.orange);

        Log.v("Width and Height", "" + icon.getWidth() + " " + icon.getHeight());
        Mat matSrc = new Mat(icon.getHeight(), icon.getWidth(), CvType.CV_8UC3);
        Utils.bitmapToMat(icon, matSrc);

        Mat I = new Mat(152, 250, CvType.CV_8UC3);
        Imgproc.resize(matSrc, I, new Size(152, 250)); //double the scale


        Resistor res = new Resistor(I);
        int ohm = res.resistance_cal();
        colorTF = res.Color_detected;
//        assertNotSame(false, colorTF[5]);
//        assertNotSame(false, colorTF[2]);
//        assertNotSame(false, colorTF[6]);
        assertEquals(75000, ohm);
        for (int i = 1; i < 6; i++)
        {
            if (ohm%10 == 0 && ohm/10 !=0)
            {index++;
                ohm = ohm/10;}
            else
            {location[index1] = ohm%10;
                index1--;
                ohm = ohm/10;}

        }
        location[2] = index;
        assertEquals(3,location[2]);

    }

    //  test 7: calculation function works can be detect and located
    public void test7() {

        MainActivity activity = getActivity();
        ImageView image = (ImageView) activity.findViewById(R.id.imagePreview1);
        Bitmap icon = BitmapFactory.decodeResource(image.getResources(),
                R.drawable.orange);

        Log.v("Width and Height", "" + icon.getWidth() + " " + icon.getHeight());
        Mat matSrc = new Mat(icon.getHeight(), icon.getWidth(), CvType.CV_8UC3);
        Utils.bitmapToMat(icon, matSrc);

        Mat I = new Mat(152, 250, CvType.CV_8UC3);
        Imgproc.resize(matSrc, I, new Size(152, 250)); //double the scale


        Resistor res = new Resistor(I);
        int ohm = res.resistance_cal();
        assertEquals(75000, ohm);


    }



}