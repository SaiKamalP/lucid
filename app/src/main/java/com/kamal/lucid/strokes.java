package com.kamal.lucid;

import java.util.concurrent.CopyOnWriteArrayList;

/* loaded from: classes.dex */
public class strokes implements Cloneable {
    public CopyOnWriteArrayList<Pnts> pointsl;

    public strokes(CopyOnWriteArrayList<Pnts> copyOnWriteArrayList) {
        this.pointsl = copyOnWriteArrayList;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // java.lang.Object
    public Object clone() throws CloneNotSupportedException {
        CopyOnWriteArrayList<Pnts> clonedPointsl=new CopyOnWriteArrayList<>();
        for(Pnts x:pointsl){
            clonedPointsl.add(new Pnts(x.x,x.y));
        }
        return new strokes(clonedPointsl);
    }
}
