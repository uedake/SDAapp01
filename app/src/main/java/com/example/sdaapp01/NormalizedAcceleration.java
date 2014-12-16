package com.example.sdaapp01;

/**
 * Created by ken on 2014/12/14.
 */
public class NormalizedAcceleration {
    double vertical;
    double horizontal;
    public NormalizedAcceleration(double vertical,double horizontal){
        this.vertical=vertical;
        this.horizontal=horizontal;
    }
    static public NormalizedAcceleration covert(Acceleration a,Acceleration normal){
        double vertical = normal.getNormalizedVector().calcInner(a);
        double horizontal=normal.getNormalizedVector().scale(-vertical).add(a).calcNorm();
        return new NormalizedAcceleration(vertical,horizontal);
    }
    public double calcNorm(){
        return Math.sqrt(vertical*vertical+horizontal*horizontal);
    }
}
