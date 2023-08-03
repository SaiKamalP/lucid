package com.kamal.lucid;

import java.util.concurrent.CopyOnWriteArrayList;

/* loaded from: classes.dex */
public class AFolder implements Cloneable {
    public String FName;
    public CopyOnWriteArrayList<String> NoteLinks = new CopyOnWriteArrayList<>();// with Id.txt
    public CopyOnWriteArrayList<String> NoteNames = new CopyOnWriteArrayList<>();
    public CopyOnWriteArrayList<AFolder> Folders = new CopyOnWriteArrayList<>();
    public int Fs = 0;
    //public String EXtradataSaved;

    public AFolder(String str) {
        this.FName = str;
    }

    @Override // java.lang.Object
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
