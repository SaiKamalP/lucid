package com.kamal.lucid;

/* loaded from: classes.dex */
public class Pen implements Cloneable {
    public int[] color;
    public int[] pshift;
    public int mode = 0;
    public int CurrentTask = 0;
    public float MinDistPoint = 45.0f;
    public float EraserSize = 20.0f;
    public float size = 3.5f;
    public int SmoothingIndex;

    public Pen() {
        this.color  = new int[]{0, 0, 0, 255, 0, 0, 0, 0, 255};
        this.pshift = new int[]{0, 0};
        this.SmoothingIndex=8;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // java.lang.Object
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
