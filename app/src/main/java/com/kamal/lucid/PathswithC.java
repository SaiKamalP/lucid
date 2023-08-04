package com.kamal.lucid;

import android.graphics.Path;

/* loaded from: classes.dex */
public class PathswithC implements Cloneable {
    public Path P;
    public int[] c;
    public float sw;
    public int IsVissable = 1;
    public float pathXShift = 0.0f;
    public float pathYShift = 0.0f;

    public PathswithC(Path path, int i, int i2, int i3, float f) {
        this.P = path;
        this.c  = new int[]{i, i2, i3};
        this.sw = f;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // java.lang.Object
    public Object clone() throws CloneNotSupportedException {
        PathswithC clonedPathWithC=new PathswithC(new Path(this.P),this.c[0],this.c[1],this.c[2],this.sw);
        clonedPathWithC.IsVissable=IsVissable;
        clonedPathWithC.pathXShift=pathXShift;
        clonedPathWithC.pathYShift=pathYShift;
        return clonedPathWithC;
    }

}
