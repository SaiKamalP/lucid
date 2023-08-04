package com.kamal.lucid;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArrayList;

/* loaded from: classes.dex */
public class Mycanvas1 extends View {
    public ArrayList<Bitmap> Bitmapspdf;
    Canvas C;
    int CPgNumber;
    Bitmap CurrentBitmap;
    File CurrentHandellingPdf;
    public ANote CurrentNotes;
    Bitmap OnDrawCurrentBitmap;
    public float Pix;
    public float Piy;
    public float Pjx;
    public float Pjy;
    float beforeMotionZF;
    public CopyOnWriteArrayList<PathswithC> ShiftPositionOfThesePath;
    public CopyOnWriteArrayList<strokes> ShiftPositionOfThesePathStrokes;
    float Xps;
    public float Xshift;
    public float YShift;
    float Ymaxinitailal;
    float Yps;
    float Z_zoomfactor;
    float Zfi;
    float Zx1;
    float Zx2;
    float Zx3;
    float Zx4;
    float Zx5;
    float Zy1;
    float Zy2;
    float Zy3;
    float Zy4;
    float Zy5;
    int SmoothingIndex;
    Path Eraserpath;
    float EXi, EYi;
    CopyOnWriteArrayList<AFolder> f;
    boolean imagemode;
    Paint paint;
    Paint paint2;
    Paint paint5;
    Paint paint6;
    Path path;
    Path path2;
    Path path3;
    Path path4;
    Path path5;
    Pen pen1;
    CopyOnWriteArrayList<Pnts> pnts;
    boolean thisisAPdfNote;
    public long time23;
    int total_pages_pdf;
    public boolean usepen;
    float xp2;
    public float xs1;
    public float xs2;
    public CopyOnWriteArrayList<Float> yinitials;
    float yp2;
    public float ys1;
    public float ys2;
    public boolean ispenup = true;
    public boolean canslide = false;
    public int letssave = 0;
    float xp = -5.0f;
    float yp = -5.0f;
    int yesno = 1;
    float Vx = 0.0f;
    float Vy = 0.0f;
    public boolean isSavingNow = false;
    boolean firstTouch = false;
    float Z_Cx = 300.0f;
    float Z_Cy = 400.0f;
    float Z_Fm = 0.4f;
    float Z_FM = 3.0f;
    boolean noMoveMode = false;
    float ParallaxX = 0f, ParallaxY = 0f;
    float PdfFcx, PdfFcy;
    float XshiftEase=500f,YshiftEase=150f,YshiftEaseDefault=150f,XshiftEaseDefault=500f;
    Handler handler = new Handler();

    public Mycanvas1(Context context, AttributeSet attributeSet) throws CloneNotSupportedException {
        super(context, attributeSet);
        this.Xshift = 0.0f;
        this.YShift = 0.0f;
       ParallaxX=drawingpart.ParalaxX;
       ParallaxY=drawingpart.ParalaxY;
        this.CPgNumber = 0;
        this.thisisAPdfNote = false;
        this.imagemode = false;
        this.total_pages_pdf = 0;
        this.Z_zoomfactor = 1.0f;
        this.CurrentNotes = new ANote();
        this.CurrentNotes = drawingpart.getmInstanceActivity().getNoteWithStrokes();
        boolean z = drawingpart.imagemode;
        this.imagemode = z;
        if (z) {
            int i = this.CurrentNotes.pages.get(0).PColor;
            this.CurrentNotes.pages = new CopyOnWriteArrayList<>();
            this.CurrentNotes.pages.add(new PageLucid(0));
            this.CurrentNotes.pages.get(0).PColor = i;
            this.OnDrawCurrentBitmap = drawingpart.bitmapforonDraw;
            this.CurrentBitmap=this.OnDrawCurrentBitmap;
            this.usepen = true;
            this.CurrentNotes.pages.get(0).Minimim_Page_Width_To_Fit_drawing = (float) this.OnDrawCurrentBitmap.getWidth();
            this.CurrentNotes.pages.get(0).Minimum_Page_Height_To_Fit_drawing = (float) this.OnDrawCurrentBitmap.getHeight();
        }
        else {
            this.CPgNumber = this.CurrentNotes.lastseenpage;
            this.Xshift = (float) this.CurrentNotes.xvs;
            this.YShift = (float) this.CurrentNotes.yvs;
            this.Z_zoomfactor = this.CurrentNotes.lastZ_Zoomfactor;
            this.usepen = true;
            if (this.CurrentNotes.APdfNote && drawingpart.totalpdfpagesinopenpdf > 0) {
                this.thisisAPdfNote = true;
                this.CurrentHandellingPdf = drawingpart.filetoopenPdfas;
                this.total_pages_pdf = drawingpart.totalpdfpagesinopenpdf;
            }
            this.CurrentBitmap = Bitmap.createBitmap(Resources.getSystem().getDisplayMetrics().widthPixels, Resources.getSystem().getDisplayMetrics().heightPixels, Bitmap.Config.ARGB_8888);
            SetCurrentBitmap();
            PdfFcx = CurrentBitmap.getWidth();
            PdfFcy = CurrentBitmap.getHeight();
            this.CurrentNotes.lastseenpage = this.CPgNumber;
        }
        this.pen1 = new Pen();
        this.pen1 = (Pen) this.CurrentNotes.pen1.clone();
        this.SmoothingIndex = pen1.SmoothingIndex;
        this.paint = new Paint();
        this.paint2 = new Paint();
        this.paint5 = new Paint();
        this.path = new Path();

        this.path2 = new Path();
        this.path4 = new Path();
        this.path5 = new Path();
        this.pnts = new CopyOnWriteArrayList<>();
        this.yinitials = new CopyOnWriteArrayList<>();
        this.ShiftPositionOfThesePath = new CopyOnWriteArrayList<>();
        this.ShiftPositionOfThesePathStrokes = new CopyOnWriteArrayList<>();
        this.paint.setAntiAlias(true);
        this.paint.setStrokeJoin(Paint.Join.ROUND);
        this.paint.setStyle(Paint.Style.STROKE);
        this.paint.setStrokeCap(Paint.Cap.ROUND);
        this.paint5.setAntiAlias(true);
        this.paint5.setStrokeJoin(Paint.Join.ROUND);
        this.paint5.setStyle(Paint.Style.FILL);
        this.paint5.setStrokeCap(Paint.Cap.ROUND);
        this.paint2.setColor(Color.rgb(255, 255, 255));
        this.paint2.setAntiAlias(true);

        this.path3 = new Path();
        Eraserpath = new Path();

        this.paint6=new Paint();
        paint6.setStrokeWidth(2f);
        paint6.setStyle(Paint.Style.STROKE);
        paint6.setAntiAlias(true);
        paint6.setColor(Color.rgb(0,0,0));

    }

    @Override // android.view.View
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        this.C = canvas;
        canvas.translate(this.Xshift, this.YShift);
        Canvas canvas2 = this.C;
        canvas2.scale(this.Z_zoomfactor, this.Z_zoomfactor);

        if (this.imagemode) {
            this.paint2.setColor(this.CurrentNotes.pages.get(this.CPgNumber).PColor);
            canvas.drawRect((-this.Xshift) / this.Z_zoomfactor, (-this.YShift) / this.Z_zoomfactor, ((-this.Xshift) / this.Z_zoomfactor) + (((float) getWidth()) / this.Z_zoomfactor), ((-this.YShift) / this.Z_zoomfactor) + (((float) getHeight()) / this.Z_zoomfactor), this.paint2);
            canvas.drawBitmap(this.OnDrawCurrentBitmap, 30.0f, 50.0f, paint2);
            int size = this.CurrentNotes.pages.get(this.CPgNumber).pathslist.size();
            int i = this.CurrentNotes.pages.get(this.CPgNumber).UndoNumber;
            Iterator<PathswithC> it = this.CurrentNotes.pages.get(this.CPgNumber).pathslist.iterator();
            while (it.hasNext()) {
                PathswithC next = it.next();
                if (next.IsVissable == 1 && size > i) {
                    this.paint.setStrokeWidth(next.sw);
                    this.paint.setColor(Color.rgb(next.c[0], next.c[1], next.c[2]));
                    next.P.offset(next.pathXShift, next.pathYShift);
                    canvas.drawPath(next.P, this.paint);
                    next.P.offset(-next.pathXShift, -next.pathYShift);
                }
                size--;
            }
            this.paint.setColor(Color.rgb(this.pen1.color[0], this.pen1.color[1], this.pen1.color[2]));
            this.paint.setStrokeWidth((float) (((double) this.pen1.size) / Math.sqrt((double) this.Z_zoomfactor)));
            canvas.drawPath(this.path, this.paint);

            this.path3.moveTo(0.0f, 20.0f);
            this.path3.lineTo(20.0f, 20.0f);
            canvas.drawPath(this.path3, this.paint);

            if (this.CurrentNotes.Maximum_Allowed_Page_Width > 100000) {
                this.path4.moveTo(0.0f, this.CurrentNotes.pages.get(this.CPgNumber).Minimum_Page_Height_To_Fit_drawing);
                this.path4.lineTo(10.0f, this.CurrentNotes.pages.get(this.CPgNumber).Minimum_Page_Height_To_Fit_drawing);
                canvas.drawPath(this.path4, this.paint);
                this.path4.reset();

            }
        }
        else {

            this.paint2.setColor(this.CurrentNotes.pages.get(this.CPgNumber).PColor);
            canvas.drawRect((-this.Xshift) / this.Z_zoomfactor, (-this.YShift) / this.Z_zoomfactor, ((-this.Xshift) / this.Z_zoomfactor) + (((float) getWidth()) / this.Z_zoomfactor), ((-this.YShift) / this.Z_zoomfactor) + (((float) getHeight()) / this.Z_zoomfactor), this.paint2);
            if(CurrentNotes.ShowGuideLines) {
                canvas.scale(1 / Z_zoomfactor, 1 / Z_zoomfactor);
                paint6.setStrokeWidth(CurrentNotes.GuideLinesthickness * Z_zoomfactor);
                paint6.setColor(CurrentNotes.GuideLinesColor);
                canvas.drawPath(GenerateGuideLines(), paint6);
                canvas.scale(Z_zoomfactor, Z_zoomfactor);
            }

            if (this.thisisAPdfNote && this.CPgNumber < this.total_pages_pdf) {
                canvas.drawBitmap(this.CurrentBitmap, 0.0f, 0.0f, paint);
            }
            int size2 = this.CurrentNotes.pages.get(this.CPgNumber).pathslist.size();
            int i2 = this.CurrentNotes.pages.get(this.CPgNumber).UndoNumber;
            Iterator<PathswithC> it2 = this.CurrentNotes.pages.get(this.CPgNumber).pathslist.iterator();
            while (it2.hasNext()) {
                PathswithC next2 = it2.next();
                if (next2.IsVissable == 1 && size2 > i2) {
                    this.paint.setStrokeWidth(next2.sw);
                    this.paint.setColor(Color.rgb(next2.c[0], next2.c[1], next2.c[2]));
                    next2.P.offset(next2.pathXShift, next2.pathYShift);
                    canvas.drawPath(next2.P, this.paint);
                    next2.P.offset(-next2.pathXShift, -next2.pathYShift);
                }
                size2--;
            }
            this.paint.setColor(Color.rgb(this.pen1.color[0], this.pen1.color[1], this.pen1.color[2]));
            this.paint.setStrokeWidth((float) (((double) this.pen1.size) / Math.sqrt((double) this.Z_zoomfactor)));
            canvas.drawPath(this.path, this.paint);
            this.path3.moveTo(0.0f, 20.0f);
            this.path3.lineTo(20.0f, 20.0f);
            canvas.drawPath(this.path3, this.paint);
        }
        //--------
        Path borderspath=new Path();
        C.scale(1/Z_zoomfactor,1/Z_zoomfactor);
        borderspath.addRect(Math.min(0,Xshift)-Xshift,-YShift,Math.max(0,Xshift)-Xshift,getHeight()-YShift, Path.Direction.CCW);
        borderspath.addRect(-Xshift,Math.min(0,YShift)-YShift,getWidth()-Xshift,Math.max(0,YShift)-YShift, Path.Direction.CCW);
        if(CurrentNotes.Fixed_Page_Width>0){
            float pgw=CurrentNotes.Fixed_Page_Width;
            float pgh=CurrentNotes.Fixed_Page_Height;
            borderspath.addRect(pgw*Z_zoomfactor,-YShift,getWidth()-Xshift,getHeight()-YShift, Path.Direction.CCW);
            borderspath.addRect(-Xshift,pgh*Z_zoomfactor,getWidth()-Xshift,getHeight()-YShift, Path.Direction.CCW);
        }
        if(CurrentNotes.APdfNote && CurrentNotes.pdfFixedBoundaries && total_pages_pdf>CPgNumber){
            float pgw=CurrentBitmap.getWidth();
            float pgh=CurrentBitmap.getHeight();
            borderspath.addRect(pgw*Z_zoomfactor,-YShift,getWidth()-Xshift,getHeight()-YShift, Path.Direction.CCW);
            borderspath.addRect(-Xshift,pgh*Z_zoomfactor,getWidth()-Xshift,getHeight()-YShift, Path.Direction.CCW);
        }
        paint5.setColor(Color.rgb(200,200,200));
        C.drawPath(borderspath,paint5);
        C.scale(Z_zoomfactor,Z_zoomfactor);
        //---------

//        if (this.CurrentNotes.Maximum_Allowed_Page_Width <= 100000) {
//                this.paint5.setColor(Color.rgb(230, 230, 230));
//                Path path = this.path5;
//                float f8 = this.Z_zoomfactor;
//                float f9 = (-this.Xshift) / f8;
//                float f10 = this.YShift;
//                path.addRect(f9, (-f10) / f8, 0.0f, ((-f10) / f8) + (((float) getHeight()) / this.Z_zoomfactor), Path.Direction.CW);
//                Path path2 = this.path5;
//                float width = ((float) getWidth()) - (this.Xshift * 2.0f);
//                float f11 = this.Z_zoomfactor;
//                float f12 = width / f11;
//                float f13 = (-this.YShift) / f11;
//                float width2 = ((float) getWidth()) - this.Xshift;
//                float f14 = this.Z_zoomfactor;
//                path2.addRect(f12, f13, width2 / f14, ((-this.YShift) / f14) + (((float) getHeight()) / this.Z_zoomfactor), Path.Direction.CW);
//                canvas.drawPath(this.path5, this.paint5);
//                this.path5.reset();
//
//                this.paint5.setColor(Color.rgb(230, 230, 230));
//                Path path3 = this.path5;
//                float f15 = this.Xshift;
//                float f16 = this.Z_zoomfactor;
//                path3.addRect((-f15) / f16, (-this.YShift) / f16, ((-f15) / f16) + (((float) getWidth()) / this.Z_zoomfactor), 0.0f, Path.Direction.CW);
//                float f17 = this.Z_zoomfactor;
//                this.path5.addRect((-this.Xshift) / this.Z_zoomfactor, (((float) getHeight()) - (this.YShift * 2.0f)) / f17, ((-this.Xshift) / f17) + (((float) getWidth()) / this.Z_zoomfactor), (((float) getHeight()) - this.YShift) / this.Z_zoomfactor, Path.Direction.CW);
//                canvas.drawPath(this.path5, this.paint5);
//                this.path5.reset();
//
//        }

    }


    @Override // android.view.View
    public boolean onTouchEvent(MotionEvent motionEvent) {
        float f;
        int i = 0;
        if (this.pen1.CurrentTask == 0) {
            if (!this.isSavingNow) {
                if (motionEvent.getSource() == 20482) {
                    float x = ((motionEvent.getX() + ParallaxX) - this.Xshift) / this.Z_zoomfactor;
                    float y = ((motionEvent.getY() + ParallaxY) - this.YShift) / this.Z_zoomfactor;
                    int action = motionEvent.getAction();
                    if (action == 0) {
                        this.ispenup = false;
                        this.Pix = x;
                        this.Pjx = x;
                        this.Piy = y;
                        this.Pjy = y;
                        if (this.CurrentNotes.pages.get(this.CPgNumber).UndoNumber > 0) {
                            for (int i2 = 0; i2 < this.CurrentNotes.pages.get(this.CPgNumber).UndoNumber; i2++) {
                                this.CurrentNotes.pages.get(this.CPgNumber).pathslist.remove(this.CurrentNotes.pages.get(this.CPgNumber).pathslist.size() - 1);
                                this.CurrentNotes.pages.get(this.CPgNumber).pathsCstrokes.remove(this.CurrentNotes.pages.get(this.CPgNumber).pathsCstrokes.size() - 1);
                            }
                        }
                        this.CurrentNotes.pages.get(this.CPgNumber).UndoNumber = 0;
                        Path path = new Path();
                        this.path = path;
                        path.moveTo(x, y);
                        this.pnts.add(new Pnts(x, y));
                        this.xp = x;
                        this.yp = y;
                        this.xp2 = x;
                        this.yp2 = y;
                    }
                    else if (action == 1) {
                        this.path.lineTo(x, y);
                        this.pnts.add(new Pnts((float) ((int) x), (float) ((int) y)));
                        normalizePnts(this.pen1.SmoothingIndex);
                        compressPnts();
                        this.path = new Path();
                        pathfrompnts();
                        this.CurrentNotes.pages.get(this.CPgNumber).pathslist.add(new PathswithC(this.path, this.pen1.color[0], this.pen1.color[1], this.pen1.color[2], (float) (((double) this.pen1.size) / Math.sqrt((double) this.Z_zoomfactor))));
                        this.path = new Path();
                        float f3 = this.Pix;
                        float f4 = this.Z_zoomfactor;
                        float f5 = this.Xshift;
                        float f6 = this.YShift;
                        invalidate((int) (((f3 * f4) - 15.0f) + f5), (int) (((this.Piy * f4) - 15.0f) + f6), (int) ((this.Pjx * f4) + 15.0f + f5), (int) ((this.Pjy * f4) + 15.0f + f6));
                        this.CurrentNotes.pages.get(this.CPgNumber).pathsCstrokes.add(new strokes(this.pnts));
                        this.pnts = new CopyOnWriteArrayList<>();
                        this.ispenup = true;
                        if (this.CurrentNotes.pages.get(this.CPgNumber).XshiftMax > this.Xshift / this.Z_zoomfactor) {
                            this.CurrentNotes.pages.get(this.CPgNumber).XshiftMax = this.Xshift / this.Z_zoomfactor;
                        }
                        if (this.CurrentNotes.pages.get(this.CPgNumber).YshiftMax > this.YShift / this.Z_zoomfactor) {
                            this.CurrentNotes.pages.get(this.CPgNumber).YshiftMax = this.YShift / this.Z_zoomfactor;
                        }
                        if (this.Pjx > this.CurrentNotes.pages.get(this.CPgNumber).Minimim_Page_Width_To_Fit_drawing) {
                            this.CurrentNotes.pages.get(this.CPgNumber).Minimim_Page_Width_To_Fit_drawing = this.Pjx + 50.0f;
                        }
                        if (this.Pjy > this.CurrentNotes.pages.get(this.CPgNumber).Minimum_Page_Height_To_Fit_drawing) {
                            this.CurrentNotes.pages.get(this.CPgNumber).Minimum_Page_Height_To_Fit_drawing = this.Pjy + 50.0f;
                        }
                    }
                    else if (action == 2) {
                        int historySize = motionEvent.getHistorySize();
                        for (int i3 = 0; i3 < historySize; i3++) {
                            this.path.lineTo((motionEvent.getHistoricalX(0, i3) + ParallaxX - this.Xshift) / this.Z_zoomfactor, (motionEvent.getHistoricalY(0, i3) + ParallaxY - this.YShift) / this.Z_zoomfactor);
                            this.pnts.add(new Pnts((motionEvent.getHistoricalX(0, i3) + ParallaxX - this.Xshift) / this.Z_zoomfactor, (motionEvent.getHistoricalY(0, i3) + ParallaxY - this.YShift) / this.Z_zoomfactor));
                        }
                        this.path.lineTo(x, y);
                        this.pnts.add(new Pnts(x, y));
                        invalidate((int) (((Math.min(this.xp, x) * this.Z_zoomfactor) - 10.0f) + this.Xshift), (int) (((Math.min(this.yp, y) * this.Z_zoomfactor) - 10.0f) + this.YShift), (int) ((Math.max(this.xp, x) * this.Z_zoomfactor) + 10.0f + this.Xshift), (int) ((Math.max(this.yp, y) * this.Z_zoomfactor) + 10.0f + this.YShift));
                        this.Pix = Math.min(this.Pix, x);
                        this.Pjx = Math.max(this.Pjx, x);
                        this.Piy = Math.min(this.Piy, y);
                        this.Pjy = Math.max(this.Pjy, y);
                        this.xp2 = this.xp;
                        this.yp2 = this.yp;
                        this.xp = x;
                        this.yp = y;
                    }
                    int action2 = motionEvent.getAction();

                    if (action2 == 211) {

                        for (int i4 = 0; i4 < this.CurrentNotes.pages.get(this.CPgNumber).pathslist.size() - this.CurrentNotes.pages.get(this.CPgNumber).UndoNumber; i4++) {
                            if (this.CurrentNotes.pages.get(this.CPgNumber).pathslist.get(i4).IsVissable == 0) {
                                this.CurrentNotes.pages.get(this.CPgNumber).pathslist.remove(i4);
                                this.CurrentNotes.pages.get(this.CPgNumber).pathsCstrokes.remove(i4);
                                i4--;
                            }
                        }
                        if (this.CurrentNotes.pages.get(this.CPgNumber).UndoNumber > 0) {
                            for (int i5 = 0; i5 < this.CurrentNotes.pages.get(this.CPgNumber).UndoNumber; i5++) {
                                this.CurrentNotes.pages.get(this.CPgNumber).pathslist.remove(this.CurrentNotes.pages.get(this.CPgNumber).pathslist.size() - 1);
                                this.CurrentNotes.pages.get(this.CPgNumber).pathsCstrokes.remove(this.CurrentNotes.pages.get(this.CPgNumber).pathsCstrokes.size() - 1);
                            }
                        }
                        this.CurrentNotes.pages.get(this.CPgNumber).UndoNumber = 0;
                    } else if (action2 == 213) {

                        for (int i6 = 0; i6 < this.CurrentNotes.pages.get(this.CPgNumber).pathsCstrokes.size(); i6++) {
                            for (Pnts next : this.CurrentNotes.pages.get(this.CPgNumber).pathsCstrokes.get(i6).pointsl) {
                                if ((Math.abs((x - next.x) - this.CurrentNotes.pages.get(this.CPgNumber).pathslist.get(i6).pathXShift) + Math.abs((y - next.y) - this.CurrentNotes.pages.get(this.CPgNumber).pathslist.get(i6).pathYShift)) * this.Z_zoomfactor < this.pen1.EraserSize) {
                                    this.CurrentNotes.pages.get(this.CPgNumber).pathslist.remove(i6);
                                    this.CurrentNotes.pages.get(this.CPgNumber).pathsCstrokes.remove(i6);
                                    i6--;
                                    break;
                                }

                            }
                        }

                        this.ispenup = true;
                        invalidate();
                    }


                }
                else {
                    float x2 = ((motionEvent.getX() + ParallaxX) - this.Xshift) / this.Z_zoomfactor;
                    float y2 = ((motionEvent.getY() + ParallaxY) - this.YShift) / this.Z_zoomfactor;

//                    if (!this.usepen) {
//                        if (this.pen1.mode == 0) {
//                            int action3 = motionEvent.getAction();
//                            if (action3 == 0) {
//                                this.ispenup = false;
//                                if (this.CurrentNotes.pages.get(this.CPgNumber).UndoNumber > 0) {
//                                    for (int i7 = 0; i7 < this.CurrentNotes.pages.get(this.CPgNumber).UndoNumber; i7++) {
//                                        Log.d("hii", "" + this.CurrentNotes.pages.get(this.CPgNumber).pathslist.size() + "," + this.CurrentNotes.pages.get(this.CPgNumber).pathsCstrokes.size());
//                                        this.CurrentNotes.pages.get(this.CPgNumber).pathslist.remove(this.CurrentNotes.pages.get(this.CPgNumber).pathslist.size() - 1);
//                                        this.CurrentNotes.pages.get(this.CPgNumber).pathsCstrokes.remove(this.CurrentNotes.pages.get(this.CPgNumber).pathsCstrokes.size() - 1);
//                                    }
//                                }
//                                this.CurrentNotes.pages.get(this.CPgNumber).UndoNumber = 0;
//                                Path path2 = new Path();
//                                this.path = path2;
//                                path2.moveTo(x2, y2);
//                                this.pnts.add(new Pnts((float) ((int) x2), (float) ((int) y2)));
//                                this.xp = x2;
//                                this.yp = y2;
//                                return true;
//                            } else if (action3 == 1) {
//                                this.pnts.add(new Pnts((float) ((int) x2), (float) ((int) y2)));
//                                this.CurrentNotes.pages.get(this.CPgNumber).pathslist.add(new PathswithC(this.path, this.pen1.color[0], this.pen1.color[1], this.pen1.color[2], this.pen1.size));
//                                this.path = new Path();
//                                this.CurrentNotes.pages.get(this.CPgNumber).pathsCstrokes.add(new strokes(this.pnts));
//                                this.pnts = new CopyOnWriteArrayList<>();
//                                this.ispenup = true;
//                                invalidate();
//                            } else if (action3 != 2) {
//                                return false;
//                            } else {
//                                int historySize2 = motionEvent.getHistorySize();
//                                int pointerCount = motionEvent.getPointerCount();
//                                for (int i8 = 0; i8 < historySize2; i8++) {
//                                    for (int i9 = 0; i9 < pointerCount; i9++) {
//                                        if (Math.abs(x2 - motionEvent.getHistoricalX(i9, i8) - ParallaxX) + Math.abs(y2 - motionEvent.getHistoricalY(i9, i8) - ParallaxY) < 80.0f) {
//                                            this.path.lineTo(motionEvent.getHistoricalX(i9, i8) + ParallaxX, motionEvent.getHistoricalY(i9, i8) + ParallaxY);
//                                            this.pnts.add(new Pnts((float) ((int) (motionEvent.getHistoricalX(i9, i8) + ParallaxX)), (float) ((int) (motionEvent.getHistoricalY(i9, i8) + ParallaxY))));
//                                        }
//                                    }
//                                }
//                                this.path.lineTo(x2, y2);
//                                this.pnts.add(new Pnts((float) ((int) x2), (float) ((int) y2)));
//                                invalidate((int) ((Math.min(this.xp, x2) - 20.0f) + this.Xshift), (int) ((Math.min(this.yp, y2) - 20.0f) + this.YShift), (int) (Math.max(this.xp, x2) + 20.0f + this.Xshift), (int) (Math.max(this.yp, y2) + 20.0f + this.YShift));
//                                this.xp = x2;
//                                this.yp = y2;
//                            }
//                        }
//                        if (this.pen1.mode == 1 && motionEvent.getAction() == 2) {
//                            for (int i10 = 0; i10 < this.CurrentNotes.pages.get(this.CPgNumber).pathsCstrokes.size(); i10++) {
//                                if (this.CurrentNotes.pages.get(this.CPgNumber).pathslist.get(i10).IsVissable == 1) {
//                                    int i11 = 0;
//                                    while (true) {
//                                        if (i11 >= this.CurrentNotes.pages.get(this.CPgNumber).pathsCstrokes.get(i10).pointsl.size()) {
//                                            break;
//                                        } else if (Math.abs(x2 - this.CurrentNotes.pages.get(this.CPgNumber).pathsCstrokes.get(i10).pointsl.get(i11).x) + Math.abs(y2 - this.CurrentNotes.pages.get(this.CPgNumber).pathsCstrokes.get(i10).pointsl.get(i11).y) < this.pen1.EraserSize) {
//                                            this.CurrentNotes.pages.get(this.CPgNumber).pathslist.get(i10).IsVissable = 0;
//                                            break;
//                                        } else {
//                                            i11++;
//                                        }
//                                    }
//                                }
//                            }
//                            this.ispenup = true;
//                            invalidate();
//                        }
//                    }
//                    else
                        if (!this.noMoveMode) {
                        int action4 = motionEvent.getAction();

                        if (action4 != 0) {
                            if (action4 == 1) {
                                this.canslide = false;
                            }
                            else if (action4 == 2 && this.canslide) {
                                float x3 = (motionEvent.getX() + ParallaxX);
                                float y3 = (motionEvent.getY() + ParallaxY);
                                this.Xshift = this.Xshift - ((this.Xps - x3) * 1.0f);
                                YShift = this.YShift - ((this.Yps - y3) * 1.0f);
                                this.Xps = x3;
                                this.Yps = y3;
                               setCanvas();
                                invalidate();
                                this.CurrentNotes.xvs = (int) this.Xshift;
                                this.CurrentNotes.yvs = (int) this.YShift;
                                this.CurrentNotes.lastZ_Zoomfactor = this.Z_zoomfactor;
                            }
                        }
                        else if ((motionEvent.getY() + ParallaxY) < ((float) (getHeight() - 100))) {
                            this.canslide = true;
                            float x4 = (motionEvent.getX() + ParallaxX);
                            float y4 = (motionEvent.getY() + ParallaxY);
                            this.Xps = x4;
                            this.Yps = y4;
                            this.Vx = 0.0f;
                            this.Vy = 0.0f;
                        }
                    }
                }
            }
        }
        else if (this.pen1.CurrentTask == 1) {
            float x5 = ((motionEvent.getX() + ParallaxX) - this.Xshift) / this.Z_zoomfactor;
            float y5 = ((motionEvent.getY() + ParallaxY) - this.YShift) / this.Z_zoomfactor;
            if (motionEvent.getSource() == 20482) {
                this.CurrentNotes.usepen = true;
                this.usepen = true;
                int action5 = motionEvent.getAction();
                if (action5 == 0) {
                    this.ispenup = false;
                    if (this.CurrentNotes.pages.get(this.CPgNumber).UndoNumber > 0) {
                        for (int i12 = 0; i12 < this.CurrentNotes.pages.get(this.CPgNumber).UndoNumber; i12++) {
                            this.CurrentNotes.pages.get(this.CPgNumber).pathslist.remove(this.CurrentNotes.pages.get(this.CPgNumber).pathslist.size() - 1);
                            this.CurrentNotes.pages.get(this.CPgNumber).pathsCstrokes.remove(this.CurrentNotes.pages.get(this.CPgNumber).pathsCstrokes.size() - 1);
                        }
                    }
                    this.CurrentNotes.pages.get(this.CPgNumber).UndoNumber = 0;
                    Path path3 = new Path();
                    this.path = path3;
                    path3.moveTo(x5, y5);
                    this.xp = x5;
                    this.yp = y5;
                    this.xs1 = x5;
                    this.ys1 = y5;
                    this.xs2 = x5;
                    this.ys2 = y5;
                    this.ShiftPositionOfThesePath = new CopyOnWriteArrayList<>();
                } else if (action5 == 1) {
                    this.path.lineTo(x5, y5);
                    this.path = new Path();
                    this.ispenup = true;
                    if (this.CurrentNotes.pages.get(this.CPgNumber).XshiftMax * this.Z_zoomfactor > this.Xshift) {
                        this.CurrentNotes.pages.get(this.CPgNumber).XshiftMax = this.Xshift / this.Z_zoomfactor;
                    }
                    if (this.CurrentNotes.pages.get(this.CPgNumber).YshiftMax * this.Z_zoomfactor > this.YShift) {
                        this.CurrentNotes.pages.get(this.CPgNumber).YshiftMax = this.YShift / this.Z_zoomfactor;
                    }
                    for (int i13 = 0; i13 < this.CurrentNotes.pages.get(this.CPgNumber).pathsCstrokes.size(); i13++) {
                        if (this.CurrentNotes.pages.get(this.CPgNumber).pathslist.get(i13).IsVissable == 1) {
                            int i14 = 0;
                            while (true) {
                                if (i14 >= this.CurrentNotes.pages.get(this.CPgNumber).pathsCstrokes.get(i13).pointsl.size()) {
                                    break;
                                }
                                if ((this.CurrentNotes.pages.get(this.CPgNumber).pathsCstrokes.get(i13).pointsl.get(i14).x + this.CurrentNotes.pages.get(this.CPgNumber).pathslist.get(i13).pathXShift) - this.xs1 > 0.0f && (this.CurrentNotes.pages.get(this.CPgNumber).pathsCstrokes.get(i13).pointsl.get(i14).x + this.CurrentNotes.pages.get(this.CPgNumber).pathslist.get(i13).pathXShift) - this.xs2 < 0.0f && (this.CurrentNotes.pages.get(this.CPgNumber).pathsCstrokes.get(i13).pointsl.get(i14).y + this.CurrentNotes.pages.get(this.CPgNumber).pathslist.get(i13).pathYShift) - this.ys1 > 0.0f && (this.CurrentNotes.pages.get(this.CPgNumber).pathsCstrokes.get(i13).pointsl.get(i14).y + this.CurrentNotes.pages.get(this.CPgNumber).pathslist.get(i13).pathYShift) - this.ys2 < 0.0f) {
                                    this.ShiftPositionOfThesePath.add(this.CurrentNotes.pages.get(this.CPgNumber).pathslist.get(i13));
                                    this.ShiftPositionOfThesePathStrokes.add(this.CurrentNotes.pages.get(this.CPgNumber).pathsCstrokes.get(i13));
                                    break;
                                }
                                i14++;
                            }
                        }
                    }
                    if (this.ShiftPositionOfThesePath.size() > 0) {
                        this.pen1.CurrentTask = 2;
                        this.path.addRect(this.xs1, this.ys1, this.xs2, this.ys2, Path.Direction.CCW);
                    }
                    invalidate();
                } else if (action5 == 2) {
                    int historySize3 = motionEvent.getHistorySize();
                    int pointerCount2 = motionEvent.getPointerCount();
                    int i15 = 0;
                    while (i15 < historySize3) {
                        while (i < pointerCount2) {
                            this.path.lineTo((motionEvent.getHistoricalX(i, i15) + ParallaxX - this.Xshift) / this.Z_zoomfactor, (motionEvent.getHistoricalY(i, i15) + ParallaxY - this.YShift) / this.Z_zoomfactor);
                            i++;
                        }
                        i15++;
                        i = 0;
                    }
                    this.path.lineTo(x5, y5);
                    invalidate((int) (((Math.min(this.xp, x5) * this.Z_zoomfactor) - 10.0f) + this.Xshift), (int) (((Math.min(this.yp, y5) * this.Z_zoomfactor) - 10.0f) + this.YShift), (int) ((Math.max(this.xp, x5) * this.Z_zoomfactor) + 10.0f + this.Xshift), (int) ((Math.max(this.yp, y5) * this.Z_zoomfactor) + 10.0f + this.YShift));
                    if (this.xs1 > x5) {
                        this.xs1 = x5;
                    }
                    if (this.ys1 > y5) {
                        this.ys1 = y5;
                    }
                    if (this.xs2 < x5) {
                        this.xs2 = x5;
                    }
                    if (this.ys2 < y5) {
                        this.ys2 = y5;
                    }
                    this.xp = x5;
                    this.yp = y5;
                }

                switch (motionEvent.getAction()) {
                    case 211:
                        this.Ymaxinitailal = this.CurrentNotes.pages.get(this.CPgNumber).YshiftMax;
                        this.ShiftPositionOfThesePath = new CopyOnWriteArrayList<>();
                        this.yinitials = new CopyOnWriteArrayList<>();
                        this.ys1 = y5;

                        for (int i16 = 0; i16 < this.CurrentNotes.pages.get(this.CPgNumber).pathsCstrokes.size(); i16++) {
                            if (this.CurrentNotes.pages.get(this.CPgNumber).pathslist.get(i16).IsVissable == 1) {
                                int i17 = 0;
                                while (true) {
                                    if (i17 >= this.CurrentNotes.pages.get(this.CPgNumber).pathsCstrokes.get(i16).pointsl.size()) {
                                        break;
                                    } else if ((this.CurrentNotes.pages.get(this.CPgNumber).pathsCstrokes.get(i16).pointsl.get(i17).y + this.CurrentNotes.pages.get(this.CPgNumber).pathslist.get(i16).pathYShift) - this.ys1 > 0.0f) {
                                        this.ShiftPositionOfThesePath.add(this.CurrentNotes.pages.get(this.CPgNumber).pathslist.get(i16));
                                        this.yinitials.add(this.CurrentNotes.pages.get(this.CPgNumber).pathslist.get(i16).pathYShift);
                                        break;
                                    } else {
                                        i17++;
                                    }
                                }
                            }
                        }
                        break;
                    case 212:
                        this.ShiftPositionOfThesePath = new CopyOnWriteArrayList<>();
                        this.yinitials = new CopyOnWriteArrayList<>();
                        this.path = new Path();
                        this.pen1.CurrentTask = 0;
                        drawingpart.getmInstanceActivity().reactivatePen();
                        this.CurrentNotes.pages.get(this.CPgNumber).Minimum_Page_Height_To_Fit_drawing += (y5 - this.ys1) / this.Z_zoomfactor;
                        this.CurrentNotes.pages.get(this.CPgNumber).YshiftMax = this.Ymaxinitailal + ((y5 - this.ys1) / this.Z_zoomfactor);
                        break;
                    case 213:
                        Path path4 = new Path();
                        this.path = path4;
                        path4.moveTo((0.0f - this.Xshift) / this.Z_zoomfactor, this.ys1);
                        this.path.lineTo((((float) getWidth()) - this.Xshift) / this.Z_zoomfactor, this.ys1);
                        this.path.moveTo((-this.Xshift) / this.Z_zoomfactor, y5);
                        this.path.lineTo((((float) getWidth()) - this.Xshift) / this.Z_zoomfactor, y5);
                        for (int i18 = 0; i18 < this.ShiftPositionOfThesePath.size(); i18++) {
                            this.ShiftPositionOfThesePath.get(i18).pathYShift = (this.yinitials.get(i18).floatValue() + y5) - this.ys1;
                        }
                        invalidate();
                        break;
                }
            }
        }
        else if (this.pen1.CurrentTask == 2) {
            if (motionEvent.getSource() == 20482) {
                int action6 = motionEvent.getAction();
                if (action6 != 0) {
                    if (action6 != 1) {
                        if (action6 != 2) {
                            switch (action6) {
                                case 211:
                                    float x6 = (motionEvent.getX() + ParallaxX);
                                    float y6 = (motionEvent.getY() + ParallaxY);
                                    this.Xps = x6;
                                    this.Yps = y6;
                                    this.Vx = 0.0f;
                                    this.Vy = 0.0f;
                                    float f11 = this.Xshift;
                                    float f12 = this.Z_zoomfactor;
                                    if (((x6 - f11) / f12) - this.xs1 > 0.0f && ((x6 - f11) / f12) - this.xs2 < 0.0f) {
                                        float f13 = this.YShift;
                                        if (((y6 - f13) / f12) - this.ys1 > 0.0f && ((y6 - f13) / f12) - this.ys2 < 0.0f) {
                                            if (this.CurrentNotes.pages.get(this.CPgNumber).UndoNumber > 0) {
                                                for (int i19 = 0; i19 < this.CurrentNotes.pages.get(this.CPgNumber).UndoNumber; i19++) {
                                                    this.CurrentNotes.pages.get(this.CPgNumber).pathslist.remove(this.CurrentNotes.pages.get(this.CPgNumber).pathslist.size() - 1);
                                                    this.CurrentNotes.pages.get(this.CPgNumber).pathsCstrokes.remove(this.CurrentNotes.pages.get(this.CPgNumber).pathsCstrokes.size() - 1);
                                                }
                                            }
                                            this.CurrentNotes.pages.get(this.CPgNumber).UndoNumber = 0;
                                            for (int i20 = 0; i20 < this.ShiftPositionOfThesePathStrokes.size(); i20++) {
                                                try {
                                                    this.CurrentNotes.pages.get(this.CPgNumber).pathslist.add((PathswithC) this.ShiftPositionOfThesePath.get(i20).clone());
                                                } catch (CloneNotSupportedException e) {
                                                    e.printStackTrace();
                                                }
                                                try {
                                                    this.CurrentNotes.pages.get(this.CPgNumber).pathsCstrokes.add((strokes) this.ShiftPositionOfThesePathStrokes.get(i20).clone());
                                                } catch (CloneNotSupportedException e2) {
                                                    e2.printStackTrace();
                                                }
                                            }
                                            int size = this.ShiftPositionOfThesePath.size();
                                            this.ShiftPositionOfThesePath = new CopyOnWriteArrayList<>();
                                            this.ShiftPositionOfThesePathStrokes = new CopyOnWriteArrayList<>();
                                            for (int i21 = 0; i21 < size; i21++) {
                                                this.ShiftPositionOfThesePath.add(this.CurrentNotes.pages.get(this.CPgNumber).pathslist.get((this.CurrentNotes.pages.get(this.CPgNumber).pathslist.size() - i21) - 1));
                                                this.ShiftPositionOfThesePathStrokes.add(this.CurrentNotes.pages.get(this.CPgNumber).pathsCstrokes.get((this.CurrentNotes.pages.get(this.CPgNumber).pathsCstrokes.size() - i21) - 1));
                                            }
                                            break;
                                        }
                                    }
                                    this.ShiftPositionOfThesePath = new CopyOnWriteArrayList<>();
                                    this.ShiftPositionOfThesePathStrokes = new CopyOnWriteArrayList<>();
                                    this.path = new Path();
                                    invalidate();
                                    break;
                                case 213:
                                    float x7 = (motionEvent.getX() + ParallaxX);
                                    float y7 = (motionEvent.getY() + ParallaxY);
                                    Path path5 = new Path();
                                    this.path = path5;
                                    path5.addRect(this.xs1, this.ys1, this.xs2, this.ys2, Path.Direction.CCW);
                                    float f14 = this.xs1;
                                    float f15 = this.Xps;
                                    float f16 = this.Z_zoomfactor;
                                    this.xs1 = f14 - ((f15 - x7) / f16);
                                    float f17 = this.ys1;
                                    float f18 = this.Yps;
                                    this.ys1 = f17 - ((f18 - y7) / f16);
                                    this.xs2 -= (f15 - x7) / f16;
                                    this.ys2 -= (f18 - y7) / f16;
                                    Iterator<PathswithC> it2 = this.ShiftPositionOfThesePath.iterator();
                                    while (it2.hasNext()) {
                                        PathswithC next2 = it2.next();
                                        next2.pathXShift -= (this.Xps - x7) / this.Z_zoomfactor;
                                        next2.pathYShift -= (this.Yps - y7) / this.Z_zoomfactor;
                                    }
                                    this.Xps = x7;
                                    this.Yps = y7;
                                    invalidate();
                                    break;
                            }
                        }
                        float x8 = (motionEvent.getX() + ParallaxX);
                        float y8 = (motionEvent.getY() + ParallaxY);
                        Path path6 = new Path();
                        this.path = path6;
                        path6.addRect(this.xs1, this.ys1, this.xs2, this.ys2, Path.Direction.CCW);
                        float f19 = this.xs1;
                        float f20 = this.Xps;
                        float f21 = this.Z_zoomfactor;
                        this.xs1 = f19 - ((f20 - x8) / f21);
                        float f22 = this.ys1;
                        float f23 = this.Yps;
                        this.ys1 = f22 - ((f23 - y8) / f21);
                        this.xs2 -= (f20 - x8) / f21;
                        this.ys2 -= (f23 - y8) / f21;
                        Iterator<PathswithC> it3 = this.ShiftPositionOfThesePath.iterator();
                        while (it3.hasNext()) {
                            PathswithC next3 = it3.next();
                            next3.pathXShift -= (this.Xps - x8) / this.Z_zoomfactor;
                            next3.pathYShift -= (this.Yps - y8) / this.Z_zoomfactor;
                        }
                        this.Xps = x8;
                        this.Yps = y8;
                        invalidate();
                    } else {
                        if (this.ShiftPositionOfThesePath.size() == 0) {
                            this.pen1.CurrentTask = 0;
                            this.path = new Path();
                            drawingpart.getmInstanceActivity().reactivatePen();
                        }
                        invalidate();
                    }
                    if (this.ShiftPositionOfThesePath.size() == 0) {
                        this.pen1.CurrentTask = 0;
                        this.path = new Path();
                        drawingpart.getmInstanceActivity().reactivatePen();
                    }
                    invalidate();
                } else {
                    float x9 = (motionEvent.getX() + ParallaxX);
                    float y9 = (motionEvent.getY() + ParallaxY);
                    this.Xps = x9;
                    this.Yps = y9;
                    this.Vx = 0.0f;
                    this.Vy = 0.0f;
                    float f24 = this.Xshift;
                    float f25 = this.Z_zoomfactor;
                    if (((x9 - f24) / f25) - this.xs1 > 0.0f && ((x9 - f24) / f25) - this.xs2 < 0.0f) {
                        float f26 = this.YShift;
                        if (((y9 - f26) / f25) - this.ys1 > 0.0f && ((y9 - f26) / f25) - this.ys2 < 0.0f) {
                            return true;
                        }
                    }
                    this.ShiftPositionOfThesePath = new CopyOnWriteArrayList<>();
                    this.ShiftPositionOfThesePathStrokes = new CopyOnWriteArrayList<>();
                    this.path = new Path();
                    invalidate();
                }
            }
            else {
                int action7 = motionEvent.getAction();
                if (action7 == 0) {
                    float x10 = (motionEvent.getX() + ParallaxX);
                    float y10 = (motionEvent.getY() + ParallaxY);
                    this.Xps = x10;
                    this.Yps = y10;
                    this.Vx = 0.0f;
                    this.Vy = 0.0f;
                    return true;
                }
                else if (action7 == 2) {
                    float x11 = (motionEvent.getX() + ParallaxX);
                    float y11 = (motionEvent.getY() + ParallaxY);
                    this.Xshift = this.Xshift - ((this.Xps - x11));
                    this.YShift = this.YShift - ((this.Yps - y11));
                    this.Xps = x11;
                    this.Yps = y11;

                    setCanvas();
                    invalidate();
                }
            }
        }

        if (motionEvent.getAction() == 0 && motionEvent.getSource() != 20482 && motionEvent.getActionIndex() == 0 && !this.noMoveMode) {
            if (!this.firstTouch || System.currentTimeMillis() - this.time23 > 250) {
                this.firstTouch = true;
                this.time23 = System.currentTimeMillis();
                Log.e("** SINGLE  TAP**", " First Tap time  " + this.time23);
            }
            else {
                if ((motionEvent.getX() + ParallaxX) < ((float) ((getWidth() * 1) / 5))) {
                    drawingpart.getmInstanceActivity().pagedown(this);
                }
                else if ((motionEvent.getX() + ParallaxX) > ((float) ((getWidth() * 4) / 5))) {
                    drawingpart.getmInstanceActivity().pageup(this);
                }
                else {
                    Log.e("** DOUBLE TAP**", " second tap ");
                    drawingpart.getmInstanceActivity().m2(this);
                    drawingpart.getmInstanceActivity().m1clicked(this);
                }
                this.firstTouch = false;
            }
        }

        if (motionEvent.getAction() == 0) {
            this.Zx1 = (motionEvent.getX() + ParallaxX);
            this.Zy1 = (motionEvent.getY() + ParallaxY);
            this.Zy2 = 0.0f;
            beforeMotionZF = this.Z_zoomfactor;
        }

        if (motionEvent.getPointerCount() == 2) {
            if (this.Zy2 == 0.0f) {
                this.Zx1 = (motionEvent.getX() + ParallaxX);
                this.Zy1 = (motionEvent.getY() + ParallaxY);
                this.Zy2 = motionEvent.getY(1);
                float x12 = motionEvent.getX(1);
                this.Zx2 = x12;
                this.Zx4 = (this.Zx1 + x12) / 2.0f;
                this.Zy4 = (this.Zy1 + this.Zy2) / 2.0f;
                this.Zx5 = this.Xshift;
                this.Zy5 = this.YShift;
                this.Zfi = this.Z_zoomfactor;
                this.noMoveMode = true;
            }
            else {
                this.Zy3 = motionEvent.getY(1);
                float x13 = motionEvent.getX(1);
                this.Zx3 = x13;
                float dist = (float) ((((double) beforeMotionZF) * dist(motionEvent.getX(0), motionEvent.getY(0), x13, this.Zy3)) / dist(this.Zx1, this.Zy1, this.Zx2, this.Zy2));
                this.Z_zoomfactor = dist;
                //this.Zx2 = this.Zx3;
                //this.Zy2 = this.Zy3;
                float f29 = this.Z_Fm;
                if (dist < f29) {
                    this.Z_zoomfactor = f29;
                }
                float f30 = this.Z_zoomfactor;
                float f31 = this.Z_FM;
                if (f30 > f31) {
                    this.Z_zoomfactor = f31;
                }
                //-----
                float shiftInMidpointX = (motionEvent.getX(0) + motionEvent.getX(1) - this.Zx1 - this.Zx2) / 2;
                float shiftInMidpointY = (motionEvent.getY(0) + motionEvent.getY(1) - this.Zy1 - this.Zy2) / 2;
                //--------

                float f32 = this.Zx5;
                float f33 = this.Zfi;
                float f34 = this.Z_zoomfactor;
                float f35 = f32 + (((f33 - f34) * (this.Zx4 - f32)) / f33) + shiftInMidpointX;
                this.Xshift = f35;
                float f36 = this.Zy5;
                float f37 = f36 + (((f33 - f34) * (this.Zy4 - f36)) / f33) + shiftInMidpointY;
                this.YShift = f37;
                setCanvas();
                this.CurrentNotes.xvs = (int) this.Xshift;
                this.CurrentNotes.yvs = (int) this.YShift;
                this.CurrentNotes.lastZ_Zoomfactor = this.Z_zoomfactor;
                invalidate();
                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    this.noMoveMode = false;
                }
            }
        }
        else if (motionEvent.getAction() == 1) {
            this.noMoveMode = false;
        }
        return true;
    }



    public double dist(float f, float f2, float f3, float f4) {
        float f5 = f - f3;
        float f6 = f2 - f4;
        return Math.max(Math.sqrt((double) ((f5 * f5) + (f6 * f6))), 1.0d);
    }

    public void pageup2() {
        if (!this.imagemode) {
            this.CurrentNotes.pages.get(this.CPgNumber).lastxs = this.Xshift;
            this.CurrentNotes.pages.get(this.CPgNumber).lastys = this.YShift;
            this.CurrentNotes.pages.get(this.CPgNumber).last_Z_Zoomfactor = this.Z_zoomfactor;
            int i = this.CPgNumber + 1;
            this.CPgNumber = i;
            if (i == this.CurrentNotes.pages.size()) {
                this.CurrentNotes.pages.add(new PageLucid(this.CPgNumber));
                this.CurrentNotes.pages.get(this.CPgNumber).PColor = this.CurrentNotes.pages.get(this.CPgNumber - 1).PColor;
                this.CurrentNotes.pages.get(this.CPgNumber).last_Z_Zoomfactor = this.CurrentNotes.pages.get(this.CPgNumber - 1).last_Z_Zoomfactor;
                // Log.d("newtest","newpage");
            }
            this.CurrentNotes.lastseenpage = this.CPgNumber;
            this.Xshift = this.CurrentNotes.pages.get(this.CPgNumber).lastxs;
            this.YShift = this.CurrentNotes.pages.get(this.CPgNumber).lastys;
            if(!this.CurrentNotes.APdfNote) {
                this.Z_zoomfactor = this.CurrentNotes.pages.get(this.CPgNumber).last_Z_Zoomfactor;
            }
            SetCurrentBitmap();
            setShiftEasesOfAPdfNote();
            setCanvas();
            this.CurrentNotes.xvs = (int) this.Xshift;
            this.CurrentNotes.yvs = (int) this.YShift;
            this.CurrentNotes.lastZ_Zoomfactor = this.Z_zoomfactor;
            //Log.d("newtest", String.valueOf(this.Z_zoomfactor));
            invalidate();
            this.ispenup = true;
            //Toast.makeText(drawingpart.getmInstanceActivity().getApplicationContext(),String.valueOf(CPgNumber+1),Toast.LENGTH_SHORT).show();
        }
    }

    public void setCanvas() {
        setShiftEasesOfAPdfNote();
        if(!CurrentNotes.APdfNote) {
            if (this.Xshift > XshiftEase) {
                this.Xshift = XshiftEase;
            } else if (this.Xshift < getWidth() - CurrentNotes.Fixed_Page_Width * Z_zoomfactor - XshiftEase && CurrentNotes.Fixed_Page_Width > 0) {
                this.Xshift = getWidth() - CurrentNotes.Fixed_Page_Width * Z_zoomfactor - XshiftEase;
            }
            if (this.Xshift > XshiftEase) {
                this.Xshift = XshiftEase;
            }
            if (this.YShift > YshiftEase) {
                this.YShift = YshiftEase;
            } else if (this.YShift < getHeight() - CurrentNotes.Fixed_Page_Height * Z_zoomfactor - YshiftEase && CurrentNotes.Fixed_Page_Width > 0) {
                this.YShift = getHeight() - CurrentNotes.Fixed_Page_Height * Z_zoomfactor - YshiftEase;
            }
            if (this.YShift > YshiftEase) {
                this.YShift = YshiftEase;
            }
        }
        else {
            if (this.Xshift > XshiftEase) {
                this.Xshift = XshiftEase;
            }
            else if (this.Xshift < getWidth() - CurrentBitmap.getWidth() * Z_zoomfactor - XshiftEase && CurrentNotes.pdfFixedBoundaries && total_pages_pdf>CPgNumber) {
                this.Xshift = getWidth() - CurrentBitmap.getWidth() * Z_zoomfactor - XshiftEase;
            }
            if (this.Xshift > XshiftEase) {
                this.Xshift = XshiftEase;
            }
            if (this.YShift > YshiftEase) {
                this.YShift = YshiftEase;
            }
            else if (this.YShift < getHeight() - CurrentBitmap.getHeight() * Z_zoomfactor - YshiftEase && CurrentNotes.pdfFixedBoundaries && total_pages_pdf>CPgNumber) {
                this.YShift = getHeight() - CurrentBitmap.getHeight() * Z_zoomfactor - YshiftEase;
            }
            if (this.YShift > YshiftEase) {
                this.YShift = YshiftEase;
            }

        }


    }

    public void pageup2withotinvalidation() {
        if (!this.imagemode) {
            this.CurrentNotes.pages.get(this.CPgNumber).lastxs = this.Xshift;
            this.CurrentNotes.pages.get(this.CPgNumber).lastys = this.YShift;
            this.CurrentNotes.pages.get(this.CPgNumber).last_Z_Zoomfactor = this.Z_zoomfactor;
            int i = this.CPgNumber + 1;
            this.CPgNumber = i;
            if (i == this.CurrentNotes.pages.size()) {
                this.CurrentNotes.pages.add(new PageLucid(this.CPgNumber));
                this.CurrentNotes.pages.get(this.CPgNumber).PColor = this.CurrentNotes.pages.get(this.CPgNumber - 1).PColor;
                this.CurrentNotes.pages.get(this.CPgNumber).last_Z_Zoomfactor = this.CurrentNotes.pages.get(this.CPgNumber - 1).last_Z_Zoomfactor;
            }
            this.CurrentNotes.lastseenpage = this.CPgNumber;
            this.Xshift = this.CurrentNotes.pages.get(this.CPgNumber).lastxs;
            this.YShift = this.CurrentNotes.pages.get(this.CPgNumber).lastys;
            this.Z_zoomfactor = this.CurrentNotes.pages.get(this.CPgNumber).last_Z_Zoomfactor;
            setCanvas();
            this.CurrentNotes.xvs = (int) this.Xshift;
            this.CurrentNotes.yvs = (int) this.YShift;
            this.CurrentNotes.lastZ_Zoomfactor = this.Z_zoomfactor;
            //SetCurrentBitmap();
            // invalidate();
            this.ispenup = true;
            //Toast.makeText(drawingpart.getmInstanceActivity().getApplicationContext(),String.valueOf(CPgNumber+1),Toast.LENGTH_SHORT).show();
        }
    }

    public void MoveToPage(int j) {
        if (!this.imagemode) {
            if (0 <= j) {
                int tempstorageofc = CPgNumber;
                if (j > CPgNumber) {
                    for (int i = 0; i < j - tempstorageofc; i++) {
                        pageup2withotinvalidation();
                    }
                } else if (j < CPgNumber) {
                    for (int i = 0; i < tempstorageofc - j; i++) {
                        pagedown2withoutInvalidation();
                    }
                }
            }
            SetCurrentBitmap();
            setShiftEasesOfAPdfNote();
            setCanvas();
            invalidate();
        }
    }

    public void pagedown2() {
        if (!this.imagemode) {
            this.CurrentNotes.pages.get(this.CPgNumber).lastxs = this.Xshift;
            this.CurrentNotes.pages.get(this.CPgNumber).lastys = this.YShift;
            this.CurrentNotes.pages.get(this.CPgNumber).last_Z_Zoomfactor = this.Z_zoomfactor;
            int i = this.CPgNumber;
            if (i > 0) {
                this.CPgNumber = i - 1;
                if (this.thisisAPdfNote) {
                    if (this.CurrentNotes.pages.size() > 1 && this.CurrentNotes.pages.get(this.CurrentNotes.pages.size() - 1).pathslist.size() == 0 && this.total_pages_pdf < this.CPgNumber + 2) {
                        this.CurrentNotes.pages.remove(this.CurrentNotes.pages.size() - 1);
                    }
                }
                else if (this.CurrentNotes.pages.size() > 1 && this.CurrentNotes.pages.get(this.CurrentNotes.pages.size() - 1).pathslist.size() == 0) {
                    this.CurrentNotes.pages.remove(this.CurrentNotes.pages.size() - 1);
                }
                this.Xshift = this.CurrentNotes.pages.get(this.CPgNumber).lastxs;
                this.YShift = this.CurrentNotes.pages.get(this.CPgNumber).lastys;
                if(!this.CurrentNotes.APdfNote) {
                    this.Z_zoomfactor = this.CurrentNotes.pages.get(this.CPgNumber).last_Z_Zoomfactor;
                }
                this.CurrentNotes.xvs = (int) this.Xshift;
                this.CurrentNotes.yvs = (int) this.YShift;
                this.CurrentNotes.lastZ_Zoomfactor = this.Z_zoomfactor;

                SetCurrentBitmap();
                setShiftEasesOfAPdfNote();
                setCanvas();
                invalidate();
            }
            this.CurrentNotes.lastseenpage = this.CPgNumber;
            this.ispenup = true;
            //Toast.makeText(drawingpart.getmInstanceActivity().getApplicationContext(),String.valueOf(CPgNumber+1),Toast.LENGTH_SHORT).show();
        }
    }

    public void pagedown2withoutInvalidation() {
        if (!this.imagemode) {
            this.CurrentNotes.pages.get(this.CPgNumber).lastxs = this.Xshift;
            this.CurrentNotes.pages.get(this.CPgNumber).lastys = this.YShift;
            this.CurrentNotes.pages.get(this.CPgNumber).last_Z_Zoomfactor = this.Z_zoomfactor;
            int i = this.CPgNumber;
            if (i > 0) {
                this.CPgNumber = i - 1;
                if (this.thisisAPdfNote) {
                    if (this.CurrentNotes.pages.size() > 1 && this.CurrentNotes.pages.get(this.CurrentNotes.pages.size() - 1).pathslist.size() == 0 && this.total_pages_pdf < this.CPgNumber + 2) {
                        this.CurrentNotes.pages.remove(this.CurrentNotes.pages.size() - 1);
                    }
                } else if (this.CurrentNotes.pages.size() > 1 && this.CurrentNotes.pages.get(this.CurrentNotes.pages.size() - 1).pathslist.size() == 0) {
                    this.CurrentNotes.pages.remove(this.CurrentNotes.pages.size() - 1);
                }
                this.Xshift = this.CurrentNotes.pages.get(this.CPgNumber).lastxs;
                this.YShift = this.CurrentNotes.pages.get(this.CPgNumber).lastys;
                this.Z_zoomfactor = this.CurrentNotes.pages.get(this.CPgNumber).last_Z_Zoomfactor;
                this.CurrentNotes.xvs = (int) this.Xshift;
                this.CurrentNotes.yvs = (int) this.YShift;
                this.CurrentNotes.lastZ_Zoomfactor = this.Z_zoomfactor;
                //invalidate();
            }
            //SetCurrentBitmap();
            this.CurrentNotes.lastseenpage = this.CPgNumber;
            this.ispenup = true;
            //Toast.makeText(drawingpart.getmInstanceActivity().getApplicationContext(),String.valueOf(CPgNumber+1),Toast.LENGTH_SHORT).show();
        }
    }


    public void saveit() {
        this.isSavingNow = true;
        if (!this.imagemode) {
            drawingpart.getmInstanceActivity().savethisnote(this.CurrentNotes, 1, this.CurrentBitmap);
        } else {
            drawingpart.getmInstanceActivity().savethisnote(this.CurrentNotes, 2);
        }
        this.yesno = 1;
        this.isSavingNow = false;
    }

    public void saveitNoNotification(){
        this.isSavingNow = true;
        if (!this.imagemode) {
            drawingpart.getmInstanceActivity().savethisnote(this.CurrentNotes, 1, this.CurrentBitmap,3);
        }
        this.yesno = 1;
        this.isSavingNow = false;

    }

    public void normalizePnts() {
        int i = 0;
        while (i < this.pnts.size() - 3) {
            int i2 = i + 1;
            int i3 = i + 2;
            this.pnts.get(i2).x = ((this.pnts.get(i).x + this.pnts.get(i2).x) + this.pnts.get(i3).x) / 3.0f;
            this.pnts.get(i2).y = ((this.pnts.get(i).y + this.pnts.get(i2).y) + this.pnts.get(i3).y) / 3.0f;
            i = i2;
        }
    }

    public void normalizePnts(int i) {
        for (int i2 = 0; i2 < i; i2++) {
            int i3 = 0;
            while (i3 < this.pnts.size() - 3) {
                int i4 = i3 + 1;
                int i5 = i3 + 2;
                this.pnts.get(i4).x = ((this.pnts.get(i3).x + this.pnts.get(i4).x) + this.pnts.get(i5).x) / 3.0f;
                this.pnts.get(i4).y = ((this.pnts.get(i3).y + this.pnts.get(i4).y) + this.pnts.get(i5).y) / 3.0f;
                i3 = i4;
            }
        }
    }

    public void compressPnts(){

            if(this.pnts.size()>3){
                boolean nothingLeft=false;
                while(!(this.pnts.size()<=3 || nothingLeft)){
                    boolean gogo=false;
                    for (int i = 0; i < this.pnts.size()-3; i++) {
                        while(Math.abs(this.pnts.get(i).x-this.pnts.get(i+1).x)<0.1f && Math.abs(this.pnts.get(i).y-this.pnts.get(i+1).y)<0.1f && i+1<pnts.size()){
                            this.pnts.remove(i+1);
                            gogo=true;
                        }
                    }
                    if(!gogo){
                        nothingLeft=true;
                    }
                }
            }

    }

    public void pathfrompnts() {
        this.path.moveTo(this.pnts.get(0).x, this.pnts.get(0).y);
        for (int i = 1; i < this.pnts.size(); i++) {
            this.path.lineTo(this.pnts.get(i).x, this.pnts.get(i).y);
        }
    }

    public void SetCurrentBitmap() {

        if (this.CPgNumber < this.total_pages_pdf && this.thisisAPdfNote) {

                    CurrentBitmap = drawingpart.getmInstanceActivity().PdfToBitmap(CurrentHandellingPdf, CPgNumber,CurrentNotes.PageBackgroundColorForPdfView);

                    CurrentNotes.pages.get(CPgNumber).Minimum_Page_Width = (float) CurrentBitmap.getWidth();
                    CurrentNotes.pages.get(CPgNumber).Minimum_Page_Height = (float) CurrentBitmap.getHeight();


        }
    }

    public void setShiftEasesOfAPdfNote(){
        if(CurrentNotes.APdfNote){
            if(CurrentNotes.pdfFixedBoundaries){
                XshiftEase=Math.max(XshiftEaseDefault,(getWidth()-CurrentBitmap.getWidth()*Z_zoomfactor)/2f);
            }
            YshiftEase=YshiftEaseDefault;

        }
    }

    public void ResetMinimumPageSizesForDrawingExport(){
        float x=0;
        float y=0;
        PageLucid pg=CurrentNotes.pages.get(CPgNumber);
        for (int i = 0; i < pg.pathsCstrokes.size()-pg.UndoNumber; i++) {
            for (int j = 0; j < pg.pathsCstrokes.get(i).pointsl.size(); j++) {
                if(x<pg.pathsCstrokes.get(i).pointsl.get(j).x+pg.pathslist.get(i).pathXShift){
                    x=pg.pathsCstrokes.get(i).pointsl.get(j).x+pg.pathslist.get(i).pathXShift;
                }
                if(y<pg.pathsCstrokes.get(i).pointsl.get(j).y+pg.pathslist.get(i).pathYShift){
                    y=pg.pathsCstrokes.get(i).pointsl.get(j).y+pg.pathslist.get(i).pathYShift;
                }
            }
        }
        x+=30;
        y+=30;
        CurrentNotes.pages.get(CPgNumber).Minimim_Page_Width_To_Fit_drawing=x;
        CurrentNotes.pages.get(CPgNumber).Minimum_Page_Height_To_Fit_drawing=y;
    }
    public void ResetMinimumPageSizesForDrawingExport(int pageIndex){
        if(pageIndex<CurrentNotes.pages.size()) {
            float x = 0;
            float y = 0;
            PageLucid pg = CurrentNotes.pages.get(pageIndex);
            for (int i = 0; i < pg.pathsCstrokes.size() - pg.UndoNumber; i++) {
                for (int j = 0; j < pg.pathsCstrokes.get(i).pointsl.size(); j++) {
                    if (x < pg.pathsCstrokes.get(i).pointsl.get(j).x + pg.pathslist.get(i).pathXShift) {
                        x = pg.pathsCstrokes.get(i).pointsl.get(j).x + pg.pathslist.get(i).pathXShift;
                    }
                    if (y < pg.pathsCstrokes.get(i).pointsl.get(j).y + pg.pathslist.get(i).pathYShift) {
                        y = pg.pathsCstrokes.get(i).pointsl.get(j).y + pg.pathslist.get(i).pathYShift;
                    }
                }
            }
            x += 30;
            y += 30;
            CurrentNotes.pages.get(CPgNumber).Minimim_Page_Width_To_Fit_drawing = x;
            CurrentNotes.pages.get(CPgNumber).Minimum_Page_Height_To_Fit_drawing = y;
        }
    }

    public Path GenerateGuideLines(){
        Path pat=new Path();
        int number_of_lines_needed= (int) (getHeight()/(CurrentNotes.GudieLineSpacing*this.Z_zoomfactor))+2;
        for (int i = 0; i < number_of_lines_needed; i++) {
            pat.moveTo(-Xshift,-YShift+i*CurrentNotes.GudieLineSpacing*Z_zoomfactor+(YShift%(CurrentNotes.GudieLineSpacing*this.Z_zoomfactor)));
            pat.lineTo(-Xshift+getWidth(),-YShift+i*CurrentNotes.GudieLineSpacing*Z_zoomfactor+(YShift%(CurrentNotes.GudieLineSpacing*this.Z_zoomfactor)));
            pat.close();
        }
        return pat;
    }

}
