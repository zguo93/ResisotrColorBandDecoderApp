package com.app.harvey.resistorcolorbanddecoder;

import android.util.Log;

import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Rect;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;

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

public class Resistor
{
    Mat image=new Mat();
    private Mat cropped;
    public boolean[] Color_detected=new boolean[8];//1 to 7 represent color brown to violet;
    public data[] Color_data;

    public Resistor(Mat input)
    {
        //initialize the variables and crop the image,
        //then resize the image to 500x300 image
        int w=input.width(), h=input.height();
        Rect block=new Rect(0,50,152,150); // use to crop out only the resistor
        cropped=new Mat(input,block);
        if(cropped==null)
            Log.v("cropped is null","");
        Imgproc.resize(cropped, image, new Size(block.width * 2, block.height * 2)); //double the scale
        //Imgproc.resize(cropped, image, new Size(500, 300)); //test
        Imgproc.cvtColor(image,cropped,Imgproc.COLOR_RGB2HSV);
    }

    public class data{
        float mean;
        float variance;
        public data(float m,float v)
        {
            mean=m;
            variance=v;
        }
    }

    public data band(int blocksize,int color)
    {
        //find out certain color with specific blocksize and color.
        //byte[] color_requirement=color_distinguish(color);
        int[] color_requirement=color_distinguish(color);
        Log.d("color"," "+color_requirement[0]+" "+color_requirement[1]+" "+color_requirement[2]+" "+color_requirement[3]);
        Mat binary=Mat.zeros(cropped.height(),cropped.width(), CvType.CV_8UC1);
        int sum;//for block operation
        int sum_j=0;//sum of y coorditate of detected point
        int num_j=0;//the number of detected points
        ArrayList<Integer> point_X=new ArrayList<Integer>();
        for(int i =0;i<cropped.height()-blocksize-1;i+=2)
        {
            for(int j=0;j<cropped.width()-blocksize-1;j+=2)
            {
                sum=0;
                for(int x=0;x<blocksize;x++)
                {
                    for(int y=0;y<blocksize;y++)
                    {
                        byte[] data=new byte[3];
                        int[] dataToUnsignedInt=new int[3];
                        cropped.get(i + x, j + y, data);
                        dataToUnsignedInt = ByteToUnsignedInt(data);
                        if(dataToUnsignedInt[0]>color_requirement[0] && dataToUnsignedInt[0]<=color_requirement[1] && dataToUnsignedInt[1]>=color_requirement[2] && dataToUnsignedInt[1]<color_requirement[3])
                            sum++;
                    }
                }
                if(sum>=blocksize*blocksize-1)
                {
                    num_j++;//1 more point is detected
                    sum_j=sum_j+j;//calculate the summation
                    point_X.add(j);

                    binary.put(i, j, 255);
                    byte[] data=new byte[3];
                    cropped.get(i, j, data);

                    //Log.d("1 bit is comfired ", " " + i + " " + j+" " + data[0] + " " + data[1] + " " + data[2]);
                }
            }
        }
//        Mat refined_orange=Mat.zeros(cropped.height(),cropped.width(), CvType.CV_8UC1);
//        for(int i =0;i<orange.height()-5;i++) {
//            for (int j = 0; j < orange.width()-5; j++) {
//                {
//                    sum=0;
//                    for(int x=0;x<4;x++)
//                    {
//                        for(int y=0;y<4;y++)
//                        {
//                            byte[] data=new byte[1];
//                            orange.get(i + x, j + y, data);
//                            if(data[0]==-1)
//                                sum++;
//                        }
//                    }
//                    if(sum>2) {
//                        refined_orange.put(i, j, 255);
//                        num_j++;//1 more point is detected
//                        sum_j=sum_j+j;//calculate the summation
//                    }
//                }
//            }
//        }
        float temp=0;
        float mean=(float)sum_j/num_j;
        for(int i=0;i<point_X.size();i++)
        {
            temp+=(mean-point_X.get(i))*(mean-point_X.get(i));
        }
        float variance=temp/num_j;

        data output=new data(mean,variance);
//
        if(num_j<10)
            return null; //error
        else {
            Color_detected[color]=true;
            return output; //detected color
        }

        //return binary; //test
    }

    // HUE = matlab*360/2; S = matlab*255, if matlab*255 > 128 then minus 256
    public int[] color_distinguish(int n)
    {
        //Temp[0] and Temp[1] marks lower boundary and higher boundary for Hue of Image.
        //Temp[2] and Temp[3] marks lower boundary and higher boundary for Saturation of Image.
        byte[] temp=new byte[4];
        int[] tempToUnsignedInt=new int[4];
        //all zeros mean that that color is not implemented yet
        switch(n) {
            case 1:
                //for brown; Hue = 0.06 to 0.1, S = 0.7 to 1
                //temp[0]=(byte)(0.035 * 360/2);  //hue min
                temp[0]=(byte)(0*360/2);  //hue min
                //temp[1]=(byte)(0.1*360/2);  //hue max
                temp[1]=(byte)(0*360/2);  //hue max
                //temp[2]=(byte)(0.78*255);  //S min
                temp[2]=(byte)(0*255);  //S min
                //temp[3]=(byte)(0.83*255);  //S max
                temp[3]=(byte)(0*255); //S max
                tempToUnsignedInt=ByteToUnsignedInt(temp);
                break;
            case 2:
                //for red
                temp[0]=(byte)(0*360/2);
                temp[1]=(byte)(0.031*360/2);
                temp[2]=(byte)(0.6*255); //red is -77
                temp[3]=(byte)(1*255); //red is -1
                tempToUnsignedInt=ByteToUnsignedInt(temp);
                break;
            case 3:
                //for orange
                temp[0]=(byte)(0.035*360/2);
                temp[1]=(byte)(0.06*360/2);
                temp[2]=(byte)(0.07*255);
                temp[3]=(byte)(1*255);
                tempToUnsignedInt=ByteToUnsignedInt(temp);
                break;
            case 4:
                //for yellow; Hue=0.12 to 0.16, S=0.7 to 1
                temp[0]=(byte)(0.12*360/2);
                temp[1]=(byte)(0.16*360/2);
                temp[2]=(byte)(0.7*255);
                temp[3]=(byte)(1*255);
                tempToUnsignedInt=ByteToUnsignedInt(temp);
                break;
            case 5:
                // for green
                //39,58,-115,-1
                //Convert to matlab value: first two values *360/2, last two values /256
                temp[0]=(byte)(0.22*360/2);
                temp[1]=(byte)(0.4*360/2);
                temp[2]=(byte)(0.45*255);
                temp[3]=(byte)(1*255);
                tempToUnsignedInt=ByteToUnsignedInt(temp);
                break;
            case 6:
                // for blue
                temp[0]=(byte)(0.5*360/2);
                temp[1]=(byte)(0.65*360/2);
                temp[2]=(byte)(0.55*255);
                temp[3]=(byte)(1*255);
                tempToUnsignedInt=ByteToUnsignedInt(temp);
                break;
            case 7:
                //for violet; Hue = 0.02 to 0.06, S = 0.4 to 0.62
                temp[0]=(byte)(0.7*360/2); // Hue min
                temp[1]=(byte)(1*360/2); // Hue max
                temp[2]=(byte)(0.3*255); // Sat min
                temp[3]=(byte)(0.7*255); // Sat max
                tempToUnsignedInt=ByteToUnsignedInt(temp);
                break;
            case 8:
                //for grey
                temp[0]=0;
                temp[1]=0;
                temp[2]=0;
                temp[3]=0;
                tempToUnsignedInt=ByteToUnsignedInt(temp);
                break;
            case 9:
                //for white
                temp[0]=0;
                temp[1]=0;
                temp[2]=0;
                temp[3]=0;
                tempToUnsignedInt=ByteToUnsignedInt(temp);
                break;
        }
        return tempToUnsignedInt;
    }
    public int resistance_cal()
    {
        //calculate resistance based on detected color.
        //sort the color with band function return value.
        //Mapping: index=color-1
        data[] color=new data[9];


        //iterate through each color
        for(int i =0;i<9;i++)
        {
            color[i]=band(2,i+1);

        }
        Color_data=color.clone();
        int band_pattern=1;
        //Band patterns:
        // 1: A B C
        // 2: A B A
        float threshold=0;
        int DoubleBandIndex=0;
        for(int i =0;i<9;i++)
        {
            if(color[i]!=null&&color[i].variance>99999) {
                band_pattern = 2;
                DoubleBandIndex=i;
                break;
            }
        }
        if(band_pattern==1)
            return ABC_cal(color);
        else
            return ABA_cal(color,DoubleBandIndex);
    }

    //
    public int ABA_cal(data[] color,int index)
    {
        // index range from 0 to 8
        int index_doubleBand=index;
        int index_singleBand=0;
        for(int i =0;i<9;i++)
        {
            if(color[i]!=null&&color[i].mean!=0&& i!=index_doubleBand)
                index_singleBand=i;
        }
        index_doubleBand++;
        index_singleBand++;
        return (10*index_doubleBand+index_singleBand)*(int)Math.pow(10,index_doubleBand);
    }

    public int ABC_cal(data[] color)
    {
        float min=500;
        int min_index=0;
        float max=0;
        int max_index=0;

        for(int i =0;i<9;i++)
        {
            if(color[i]!=null)
            {
                if(color[i].mean>max) {
                    max = color[i].mean;
                    max_index=i;
                }
                if(color[i].mean<min) {
                    min = color[i].mean;
                    min_index=i;
                }
            }
        }

        // middle band value
        int index=0, check_zero=0;
        for(int i =0;i<9;i++)
        {
            if(color[i]!=null)
            {
                if (color[i].mean == 0) {
                    check_zero++;
                }
                if (color[i].mean != 0 && i != min_index && i != max_index) {
                    index = i;
                }
                if (check_zero == 9) {
                    return 0;
                }
            }
        }
        min_index++;
        index++;
        max_index++;
        return (10*min_index+index)*(int)Math.pow(10,max_index);
    }


    Mat getImage()
    {
        //for testing, image display.
        return cropped;
    }

    public int[] ByteToUnsignedInt(byte[] byteIn)
    {
        int[]toInteger = new int[byteIn.length];
        for(int i=0;i<byteIn.length; i++)
        {
            toInteger[i]=byteIn[i] & 0xFF;
        }
        return toInteger;
    }
}
