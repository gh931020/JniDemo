package com.example.jnidemo.bean;

import android.graphics.Color;

public class Circle {
    private int id;

    private float fixWidth;
    private float startYPoint;
    /**
     * X坐标
     */
    private float xPoint = 0;
    /**
     * Y坐标
     */
    private float yPoint = 0;
    /**
     * 圆圈颜色
     */
    private int color = Color.RED;
    /**
     * 标记是否显示
     */
    private boolean isShowing = false;
    /**
     * 是否可以显示
     */
    private boolean canShow = false;
    /**
     * 长度
     */
    private float width;
    private float height;

    public boolean isShowing() {
        return isShowing;
    }

    public void setShowing(boolean showing) {
        isShowing = showing;
    }

    public Circle() {
    }

    public Circle(int id, float xPoint, float yPoint, float startYPoint, float width, float fixWidth, float height, boolean canShow) {
        this.id = id;
        this.xPoint = xPoint;
        this.yPoint = yPoint;
        this.startYPoint = startYPoint;
        this.width = width;
        this.fixWidth = fixWidth;
        this.height = height;
        this.canShow = canShow;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public float getxPoint() {
        return xPoint;
    }

    public void setxPoint(float xPoint) {
        this.xPoint = xPoint;
    }

    public float getyPoint() {
        return yPoint;
    }

    public void setyPoint(float yPoint) {
        this.yPoint = yPoint;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public boolean isCanShow() {
        return canShow;
    }

    public void setCanShow(boolean canShow) {
        this.canShow = canShow;
    }

    public float getWidth() {
        return width;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public float getFixWidth() {
        return fixWidth;
    }

    public float getStartYPoint() {
        return startYPoint;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    @Override
    public String toString() {
        return "Circle{" +
                ", xPoint=" + xPoint +
                ", yPoint=" + yPoint +
                ", isShowing=" + isShowing +
                ", canShow=" + canShow +
                ", width=" + width +
                ", height=" + height +
                '}';
    }
}
