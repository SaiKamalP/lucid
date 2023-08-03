package com.kamal.lucid;

import android.graphics.Color;
import java.util.concurrent.CopyOnWriteArrayList;

/* loaded from: classes.dex */
public class PageLucid implements Cloneable {
    public int PGnumber;
    public int UndoNumber;
    public float XshiftMax = 0.0f;
    public float YshiftMax = 0.0f;
    public float Minimim_Page_Width_To_Fit_drawing = 100.0f;
    public float Minimum_Page_Height_To_Fit_drawing = 100.0f;
    public float Minimum_Page_Width = 100.0f;
    public float Minimum_Page_Height = 100.0f;
    public int PColor = Color.rgb(255, 255, 255);
    public float lastxs = 0.0f;
    public float lastys = 0.0f;
    public float last_Z_Zoomfactor = 1.0f;

    public CopyOnWriteArrayList<PathswithC> pathslist = new CopyOnWriteArrayList<>();
    public CopyOnWriteArrayList<strokes> pathsCstrokes = new CopyOnWriteArrayList<>();

    public PageLucid(int i) {
        this.PGnumber = i;
    }

    @Override // java.lang.Object
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
