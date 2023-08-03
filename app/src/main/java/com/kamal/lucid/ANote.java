package com.kamal.lucid;

import android.graphics.Color;
import android.os.Parcel;
//import com.itextpdf.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;

/* loaded from: classes.dex */
public class ANote implements Cloneable {
    public boolean APdfNote;
    public String[] IDMAKER;
    public String Id;
    public int Maximum_Allowed_Page_Width;
    public int Maximum_Allowed_Page_Height;
    public int Fixed_Page_Width;//-10 means not fixed
    public int Fixed_Page_Height;//-10means not fixed
    public float lastZ_Zoomfactor;
    public int lastseenpage;
    public String name;
    public CopyOnWriteArrayList<PageLucid> pages;
    public Pen pen1;
    public boolean usepen;
    public int xvs;
    public int yvs;
    public int exportNumber;
    public String DateOfCreation;
    public ArrayList<Integer> Importantpages;
    public boolean pdfFixedBoundaries=true;
    public int PageBackgroundColorForPdfView=-1;
    public boolean ShowGuideLines=false;
    public float GudieLineSpacing=80f;
    public float GuideLinesthickness=2f;
    public int GuideLinesColor= Color.rgb(200,200,200);

    public ANote() {
        this.name = "Untitled";
        Date c = Calendar.getInstance().getTime();
        System.out.println("Current time => " + c);
        SimpleDateFormat df = new SimpleDateFormat(" HH:mm dd-MMM-yyyy", Locale.getDefault());
        String formattedDate = df.format(c);
        this.name=formattedDate;

        c = Calendar.getInstance().getTime();
        System.out.println("Current time => " + c);
        df = new SimpleDateFormat(" yyyyMMddHHmmss", Locale.getDefault());
        formattedDate = df.format(c);
        this.DateOfCreation=formattedDate;
        Importantpages=new ArrayList<>();
        this.APdfNote = false;
        this.lastZ_Zoomfactor = 1.0f;
        CopyOnWriteArrayList<PageLucid> copyOnWriteArrayList = new CopyOnWriteArrayList<>();
        this.pages = copyOnWriteArrayList;
        copyOnWriteArrayList.add(new PageLucid(1));
        this.pen1 = new Pen();
        this.usepen = false;
        this.xvs = 0;
        this.yvs = 0;
        this.Maximum_Allowed_Page_Width = 100000000;
        this.Maximum_Allowed_Page_Height = 100000000;
        this.Fixed_Page_Width=-10;
        this.Fixed_Page_Height=-10;
        this.IDMAKER = new String[]{"a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z", "1", "2", "3", "4", "5", "6", "7", "8", "9", "0"};
        this.Id = "";
        for (int i = 0; i < 64; i++) {
            this.Id += this.IDMAKER[new Random().nextInt(62)];
        }
    }

    protected ANote(Parcel parcel) {
        this.name = "Untitled";
        boolean z = false;
        this.APdfNote = false;
        this.lastZ_Zoomfactor = 1.0f;
        this.Id = parcel.readString();
        this.IDMAKER = parcel.createStringArray();
        this.name = parcel.readString();
        this.xvs = parcel.readInt();
        this.yvs = parcel.readInt();
        this.usepen = parcel.readByte() != 0;
        this.Maximum_Allowed_Page_Width = parcel.readInt();
        this.Maximum_Allowed_Page_Height = parcel.readInt();
        this.APdfNote = parcel.readByte() != 0 ? true : z;
        this.lastseenpage = parcel.readInt();
        this.lastZ_Zoomfactor = parcel.readFloat();
    }

    public void RegenerateId() {
        this.IDMAKER = new String[]{"a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z", "1", "2", "3", "4", "5", "6", "7", "8", "9", "0"};
        this.Id = "";
        for (int i = 0; i < 64; i++) {
            this.Id += this.IDMAKER[new Random().nextInt(62)];
        }
    }

    @Override // java.lang.Object
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
