package com.example.sdaapp01;

import com.example.sdaapp01.util.L;

/**
 * Created by ken on 2014/12/14.
 */
public class Acceleration {
    double x;
    double y;
    double z;

    public Acceleration(double x,double y,double z){
        this.x = x;
        this.y = y;
        this.z = z;
    }
    public double calcNorm(){
        return Math.sqrt(x * x + y * y + z * z);
    }
    public double calcInner(Acceleration a){
        return x *a.x + y *a.y + z *a.z;
    }
    public Acceleration add(Acceleration a){
        return new Acceleration(x +a.x, y +a.y, z +a.z);
    }
    public Acceleration scale(double c){
        return new Acceleration(x *c, y *c, z *c);
    }

    public Acceleration getNormalizedVector(){
        double norm=calcNorm();
        return new Acceleration(x /norm, y /norm, z /norm);
    }

    public static Acceleration fromBytes(byte[] byteData) {
        short ax = (short)((byteData[1]<<8) | byteData[0]&0xFF);
        short ay = (short)((byteData[3]<<8) | byteData[2]&0xFF);
        short az =  (short)((byteData[5]<<8) | byteData[4]&0xFF);
        Acceleration acceleration = new Acceleration(ax,ay,az);
        L.i("acceleration:" + acceleration.getIntString());
        return acceleration;
    }
    public String getIntString(){
        return (int)x + "," + (int)y + "," + (int)z;
    }
}

