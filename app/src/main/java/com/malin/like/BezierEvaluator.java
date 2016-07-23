package com.malin.like;

import android.animation.TypeEvaluator;
import android.graphics.PointF;

public class BezierEvaluator implements TypeEvaluator<PointF> {

    //途径的两个点
    private PointF pointF1;
    private PointF pointF2;

    public BezierEvaluator(PointF pointF1, PointF pointF2) {
        this.pointF1 = pointF1;
        this.pointF2 = pointF2;
    }

    @Override
    public PointF evaluate(float t, PointF startPointF, PointF endPointF) {

        PointF pointF0 = startPointF;//起点
        PointF pointF3 = endPointF;//终点

        float t1 = 1.0f-t;

        PointF pointF = new PointF();//结果

        //三次方贝塞尔曲线公式
        pointF.x =
                pointF0.x*t1*t1*t1+
                3*pointF1.x*t*t1*t1+
                3*pointF2.x*t*t*t1+
                pointF3.x*t*t*t;

        pointF.y =
                        pointF0.y*t1*t1*t1+
                        3*pointF1.y*t*t1*t1+
                        3*pointF2.y*t*t*t1+
                        pointF3.y*t*t*t;
        return pointF;
    }
}
