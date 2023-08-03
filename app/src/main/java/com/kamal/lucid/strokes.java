package com.kamal.lucid;

import java.util.concurrent.CopyOnWriteArrayList;

/* loaded from: classes.dex */
public class strokes implements Cloneable {
    public CopyOnWriteArrayList<Pnts> pointsl;

    public strokes(CopyOnWriteArrayList<Pnts> copyOnWriteArrayList) {
        this.pointsl = new CopyOnWriteArrayList<>();
        this.pointsl = copyOnWriteArrayList;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // java.lang.Object
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
