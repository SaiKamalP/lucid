package com.kamal.lucid;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.pdf.PdfDocument;
import android.graphics.pdf.PdfRenderer;
import android.os.Bundle;
import android.os.Handler;
import android.os.ParcelFileDescriptor;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.FileProvider;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
//import com.itextpdf.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.ref.WeakReference;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Timer;
import java.util.TimerTask;

import yuku.ambilwarna.AmbilWarnaDialog;

/* loaded from: classes.dex */
public class drawingpart extends AppCompatActivity {
    public static WeakReference<drawingpart> weakActivity;
    public int tempp1i;
    public float tempp1f;
    public View viewtodisplay;

    //----------
    public ANote ImportedNotes;
    public String CurrentNoteId;
    public ANote CurrentNotesDrawingPart;
    public static float ParalaxX,ParalaxY;
    public static boolean imagemode;
    public static Bitmap bitmapforonDraw;
    public static int totalpdfpagesinopenpdf;
    public static File filetoopenPdfas;
    public static boolean should_save_now=true;
    public static String nameOfNote;

    int bitmapsizew = 235;
    int bitmapsizeh = ItemTouchHelper.Callback.DEFAULT_SWIPE_ANIMATION_DURATION;
    //--------------

    ANote COpenNotes;
    Button Cp2;
    Button Eb;
    Button Eb1;
    Button Eb2;
    Button brb;
    Button bstroke;
    Button bub;
    float f1;
    float f2;
    Button m1;
    Button m1prime;
    Button m2;
    Button m3;
    Button m4;
    Mycanvas1 mycanv;
    TextView pcb;
    Button pdb;
    Button pub;
    SeekBar skb,Smoothbar;
    SeekBar XshiftBar,YshiftBar;
    View v2;
    float expzf = 1.0f;
    int delnow = 0;
    AlertDialog.Builder dialogBuilder;
    AlertDialog dialog;
    ProgressBar progressBar;
    TextView displayer;
    Timer tmer;
    int movedFromPageIndex=-10;

    public ViewGroup layoutP;
    public View option_menu_export;
    public boolean started_long_select;
    public int startingFromSelection;
    public int endingFromSelection;
    public PagesSelectGridAdapter pagesSelectGridAdapter;
    public DrawingPartViewModel drawingPartViewModel;

    //--------------
    public ArrayList<String> colorPalletSections;
    public ArrayList<ArrayList<Integer>> coloursOfAllSections;
    public int lastChoosenColorSection;
    public View viewcp;
    //------------

    private Handler handler=new Handler();
    public static drawingpart getmInstanceActivity() {
        return weakActivity.get();
    }

    @Override // androidx.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.COpenNotes = new ANote();
        drawingPartViewModel= new ViewModelProvider(this).get(DrawingPartViewModel.class);
        CurrentNoteId=getIntent().getStringExtra("NoteId");
        UserPreferencesloaddata();
        ParalaxX=UserPreferencesXShift;
        ParalaxY=UserPreferencesYShift;
        imagemode=getIntent().getBooleanExtra("imagemode",false);
        if(imagemode){
            bitmapforonDraw= BitmapFactory.decodeFile(getExternalFilesDir("/") + "/tempnoteforimage.png");
        }//getIntent().getParcelableExtra("bitmapforondraw");
        totalpdfpagesinopenpdf=getIntent().getIntExtra("totalpagesinopenpdf",0);
        nameOfNote=getIntent().getStringExtra("nameofnote");
        if(getIntent().getBooleanExtra("ispdf",false)) {
            filetoopenPdfas = new File(drawingpart.this.getExternalFilesDir("/"), CurrentNoteId + ".pdf");
        }

        weakActivity = new WeakReference<>(this);
        setContentView(R.layout.activity_drawingpart);
        layoutP=findViewById(R.id.parentDrawing);




        //---------------
        colorPalletSections=new ArrayList<>();
        coloursOfAllSections=new ArrayList<>();
        loadColors();
        //----------------

        //----------
        View findViewByIdThisView = findViewById(R.id.view2);
        option_menu_export = LayoutInflater.from(getApplicationContext()).inflate(R.layout.menu_export_pdf_options, layoutP, false);
        //------------
        findViewByIdThisView.setSystemUiVisibility(4102);
        findViewByIdThisView.setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() { // from class: com.kamal.lucid.drawingpart.1
            @Override // android.view.View.OnSystemUiVisibilityChangeListener
            public void onSystemUiVisibilityChange(int i) {
                if ((i & 4) == 0) {
                    drawingpart.this.mycanv.saveit();
                    drawingpart.this.mycanv.isSavingNow = false;
                    if (drawingpart.this.delnow == 1) {
                       // new File(drawingpart.getmInstanceActivity().getExternalFilesDir("/"), drawingpart.this.mycanv.CurrentNotes.name + ".pdf").delete();
                        drawingpart.this.delnow = 0;
                    }
                    if (drawingpart.this.delnow == 0) {
                        drawingpart.this.delnow++;
                    }
                }
            }
        });
        progressBar=findViewById(R.id.progressBar);
        progressBar.setVisibility(View.INVISIBLE);
        this.f1 = 72.0f;
        float f = (float) getResources().getDisplayMetrics().densityDpi;
        this.f2 = f;
        this.expzf = this.f1 / f;

        this.mycanv = (Mycanvas1) findViewByIdThisView;

        layoutP.removeView(mycanv);
        ViewGroup linearLayout2=new CustomLayout(this);
        linearLayout2.addView(mycanv);
        layoutP.addView(linearLayout2,0);
        //--------------
        SeekBar seekBar = (SeekBar) findViewById(R.id.strokebar);
        SeekBar Smoothbar=(SeekBar)findViewById(R.id.smoothIndxBar);
        SeekBar XshiftBar=(SeekBar)findViewById(R.id.XShiftMeasure);
        SeekBar YshiftBar=(SeekBar)findViewById(R.id.YShiftMeasure);
        displayer=(TextView)findViewById(R.id.ValueDiplayer);

        displayer.setText("  ");
        this.XshiftBar=XshiftBar;
        this.YshiftBar=YshiftBar;
        this.Smoothbar=Smoothbar;
        this.skb = seekBar;
        this.skb.setMax(1000);
        this.skb.setMin(0);
        this.skb.setProgress((int) (Math.sqrt((drawingpart.this.mycanv.pen1.size-1f)/20f)*1000f) );
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() { // from class: com.kamal.lucid.drawingpart.2
            @Override // android.widget.SeekBar.OnSeekBarChangeListener
            public void onStartTrackingTouch(SeekBar seekBar2) {
            }

            @Override // android.widget.SeekBar.OnSeekBarChangeListener
            public void onStopTrackingTouch(SeekBar seekBar2) {
            }

            @Override // android.widget.SeekBar.OnSeekBarChangeListener
            public void onProgressChanged(SeekBar seekBar2, int i, boolean z) {
                float ieq=i/1000f;
                drawingpart.this.mycanv.pen1.size = ((ieq)*(ieq)*20f ) + 1.0f;
                drawingpart.this.mycanv.CurrentNotes.pen1.size = drawingpart.this.mycanv.pen1.size;
                displayer.setText(String.valueOf(seekBar.getProgress()));
            }
        });

        //----
        this.XshiftBar.setMin(-1000);
        this.XshiftBar.setMax(1000);
        this.XshiftBar.setProgress((int) (ParalaxX*100f));
        this.XshiftBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                displayer.setText("Parallax-X : "+String.valueOf(seekBar.getProgress()));

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if(seekBar.getProgress()==1000){
                    seekBar.setProgress(0);
                }
                mycanv.ParallaxX=seekBar.getProgress()/100f;
                drawingpart.UserPreferencesXShift=seekBar.getProgress()/100f;
                drawingpart.getmInstanceActivity().UserPreferencesSaveData();
                //displayer.setText(" ");
            }
        });
        //----

        this.YshiftBar.setMin(-1000);
        this.YshiftBar.setMax(1000);
        this.YshiftBar.setProgress((int) (-ParalaxY*100f));
        this.YshiftBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                displayer.setText("Parallax-Y : "+String.valueOf(seekBar.getProgress()));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if(seekBar.getProgress()==1000){
                    seekBar.setProgress(0);
                }
                mycanv.ParallaxY=-seekBar.getProgress()/100f;
                drawingpart.UserPreferencesYShift=-seekBar.getProgress()/100f;
                drawingpart.getmInstanceActivity().UserPreferencesSaveData();
                //displayer.setText(" ");

            }
        });
        //------------
        //------------------
        //------------------
        this.Smoothbar.setMin(0);
        this.Smoothbar.setMax(15);
        this.Smoothbar.setProgress(mycanv.SmoothingIndex);
        this.Smoothbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                mycanv.CurrentNotes.pen1.SmoothingIndex=i;
                mycanv.pen1.SmoothingIndex=i;
                displayer.setText("Smoothing: "+String.valueOf(seekBar.getProgress()));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                //displayer.setText(" ");

            }
        });
        //--------------------
        this.m1 = (Button) findViewById(R.id.m1);
        this.m2 = (Button) findViewById(R.id.m2);
        this.m3 = (Button) findViewById(R.id.m3);
        this.m4 = (Button) findViewById(R.id.m4);
        this.m1prime = (Button) findViewById(R.id.m);
        this.bub = (Button) findViewById(R.id.bub);
        this.brb = (Button) findViewById(R.id.brb);
        this.pub = (Button) findViewById(R.id.pub);
        this.pdb = (Button) findViewById(R.id.pdb);
        this.Eb = (Button) findViewById(R.id.Eb);
        this.Eb1 = (Button) findViewById(R.id.Eb1);
        this.Eb2 = (Button) findViewById(R.id.Eb2);
        this.pcb = (TextView) findViewById(R.id.pcb);
        this.bstroke = (Button) findViewById(R.id.bstroke);
        this.Cp2 = (Button) findViewById(R.id.Cp2);
        this.m1.setBackgroundColor(Color.rgb(this.mycanv.pen1.color[0], this.mycanv.pen1.color[1], this.mycanv.pen1.color[2]));
        this.m2.setBackgroundColor(Color.rgb(this.mycanv.pen1.color[3], this.mycanv.pen1.color[4], this.mycanv.pen1.color[5]));
        this.m3.setBackgroundColor(Color.rgb(this.mycanv.pen1.color[6], this.mycanv.pen1.color[7], this.mycanv.pen1.color[8]));
        m1clicked(this.mycanv);
        this.m4.setBackgroundColor(Color.rgb(220, 220, 220));
        displaypagecount(this.mycanv);
        this.m4.setOnLongClickListener(new View.OnLongClickListener() { // from class: com.kamal.lucid.-$$Lambda$drawingpart$yfgkeUH2Uh6VQ4VpqQyf5lw0n8w
            @Override // android.view.View.OnLongClickListener
            public final boolean onLongClick(View view) {
                return drawingpart.this.lambda$onCreate$0$drawingpart(view);
            }
        });
        this.m4.setOnClickListener(new View.OnClickListener() { // from class: com.kamal.lucid.-$$Lambda$drawingpart$gAb9VEaZEtLZ1Z3hbBKGNvYVtys
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                drawingpart.this.lambda$onCreate$1$drawingpart(view);
            }
        });
        this.Eb1.setOnLongClickListener(new View.OnLongClickListener() { // from class: com.kamal.lucid.drawingpart.3
            @Override // android.view.View.OnLongClickListener
            public boolean onLongClick(View view) {
                drawingpart.this.exportlucidnote();
                return true;
            }
        });
        this.Eb1.setOnClickListener(new View.OnClickListener() { // from class: com.kamal.lucid.drawingpart.4
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                drawingpart drawingpartVar = drawingpart.this;
                drawingpartVar.OpenExportPdfMenu(drawingpartVar.mycanv);
            }
        });
        this.m1.setOnLongClickListener(new View.OnLongClickListener() { // from class: com.kamal.lucid.drawingpart.5
            @Override // android.view.View.OnLongClickListener
            public boolean onLongClick(View view) {
                if (drawingpart.this.mycanv.pen1.CurrentTask == 2) {
                    Iterator<PathswithC> it = drawingpart.this.mycanv.ShiftPositionOfThesePath.iterator();
                    while (it.hasNext()) {
                        PathswithC next = it.next();
                        next.c[0] = drawingpart.this.mycanv.pen1.color[0];
                        next.c[1] = drawingpart.this.mycanv.pen1.color[1];
                        next.c[2] = drawingpart.this.mycanv.pen1.color[2];
                    }
                    drawingpart.this.mycanv.invalidate();
                }
                return true;
            }
        });
        this.m1prime.setOnLongClickListener(new View.OnLongClickListener() { // from class: com.kamal.lucid.drawingpart.6
            @Override // android.view.View.OnLongClickListener
            public boolean onLongClick(View view) {
                if (drawingpart.this.mycanv.pen1.CurrentTask == 2) {
                    Iterator<PathswithC> it = drawingpart.this.mycanv.ShiftPositionOfThesePath.iterator();
                    while (it.hasNext()) {
                        PathswithC next = it.next();
                        next.c[0] = drawingpart.this.mycanv.pen1.color[0];
                        next.c[1] = drawingpart.this.mycanv.pen1.color[1];
                        next.c[2] = drawingpart.this.mycanv.pen1.color[2];
                    }
                    drawingpart.this.mycanv.invalidate();
                }
                return true;
            }
        });
        this.Eb2.setOnLongClickListener(new View.OnLongClickListener() { // from class: com.kamal.lucid.drawingpart.7
            @Override // android.view.View.OnLongClickListener
            public boolean onLongClick(View view) {
                if (drawingpart.this.mycanv.CurrentNotes.APdfNote) {
                    //Toast.makeText(drawingpart.this, "You can't change mode for Pdf documents", Toast.LENGTH_SHORT).show();
                    if(mycanv.CurrentNotes.pdfFixedBoundaries){
                        mycanv.CurrentNotes.pdfFixedBoundaries=false;
                        mycanv.XshiftEase=mycanv.XshiftEaseDefault;
                        mycanv.setCanvas();
                        mycanv.invalidate();
                    }
                    else{
                        mycanv.CurrentNotes.pdfFixedBoundaries=true;
                        mycanv.setShiftEasesOfAPdfNote();
                        mycanv.setCanvas();
                        mycanv.invalidate();
                    }
                    return true;
                }
                else if (drawingpart.this.mycanv.CurrentNotes.Maximum_Allowed_Page_Width == mmToPix(210)) {
                    drawingpart.this.mycanv.CurrentNotes.Maximum_Allowed_Page_Width = 1000000000;
                    drawingpart.this.mycanv.CurrentNotes.Maximum_Allowed_Page_Height = 1000000000;

                    drawingpart.this.mycanv.CurrentNotes.Fixed_Page_Width=-10;
                    drawingpart.this.mycanv.CurrentNotes.Fixed_Page_Height=-10;
                    mycanv.setCanvas();
                    mycanv.invalidate();
                    Toast.makeText(drawingpart.this, "Paper Infinite", Toast.LENGTH_SHORT).show();
                    return true;
                }
                else if(drawingpart.this.mycanv.CurrentNotes.Maximum_Allowed_Page_Width == 1000000000){
                    drawingpart.this.mycanv.CurrentNotes.Maximum_Allowed_Page_Width = mmToPix(297);
                    drawingpart.this.mycanv.CurrentNotes.Maximum_Allowed_Page_Height = mmToPix(210);

                    drawingpart.this.mycanv.CurrentNotes.Fixed_Page_Width= mmToPix(297);
                    drawingpart.this.mycanv.CurrentNotes.Fixed_Page_Height=mmToPix(210);

                    mycanv.setCanvas();
                    drawingpart.this.mycanv.invalidate();
                    Toast.makeText(drawingpart.this, "Paper A4(Horizontal)", Toast.LENGTH_SHORT).show();
                    return true;
                }
                else{
                    drawingpart.this.mycanv.CurrentNotes.Maximum_Allowed_Page_Width = mmToPix(210);
                    drawingpart.this.mycanv.CurrentNotes.Maximum_Allowed_Page_Height = mmToPix(297);

                    drawingpart.this.mycanv.CurrentNotes.Fixed_Page_Width=mmToPix(210);
                    drawingpart.this.mycanv.CurrentNotes.Fixed_Page_Height=mmToPix(297);
                    mycanv.setCanvas();
                    drawingpart.this.mycanv.invalidate();
                    Toast.makeText(drawingpart.this, "Paper A4(Vertical)", Toast.LENGTH_SHORT).show();
                    return true;
                }
            }
        });

        this.pcb.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                displaypagecount(mycanv);
                OpenMoveToDialog();
            }
        });
        this.pcb.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                new AmbilWarnaDialog(drawingpart.this, mycanv.CurrentNotes.PageBackgroundColorForPdfView, new AmbilWarnaDialog.OnAmbilWarnaListener() { // from class: com.kamal.lucid.drawingpart.8
                    @Override // yuku.ambilwarna.AmbilWarnaDialog.OnAmbilWarnaListener
                    public void onCancel(AmbilWarnaDialog ambilWarnaDialog) {
                    }

                    @Override // yuku.ambilwarna.AmbilWarnaDialog.OnAmbilWarnaListener
                    public void onOk(AmbilWarnaDialog ambilWarnaDialog, int i) {
                        drawingpart.this.mycanv.CurrentNotes.PageBackgroundColorForPdfView= i;
                        drawingpart.this.mycanv.SetCurrentBitmap();
                        drawingpart.this.mycanv.invalidate();
                    }
                }).show();

                return true;
            }
        });

        this.pdb.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                mycanv.Z_zoomfactor=1f;
                mycanv.setCanvas();
                mycanv.CurrentNotes.lastZ_Zoomfactor=mycanv.Z_zoomfactor;
                mycanv.invalidate();
                return true;
            }
        });
        this.pub.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                AlertDialog.Builder dialog2=new AlertDialog.Builder(drawingpart.this,R.style.WrapContentDialog);
               viewtodisplay=getLayoutInflater().inflate(R.layout.guide_lines_settings,null);
                dialog2.setView(viewtodisplay);
                Dialog dialog11=dialog2.create();
                dialog11.show();
                viewtodisplay.findViewById(R.id.ShowColor).setBackgroundColor(mycanv.CurrentNotes.GuideLinesColor);
                tempp1i=mycanv.CurrentNotes.GuideLinesColor;
                viewtodisplay.findViewById(R.id.ChangeColBtn).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        new AmbilWarnaDialog(drawingpart.this, mycanv.CurrentNotes.GuideLinesColor, new AmbilWarnaDialog.OnAmbilWarnaListener() { // from class: com.kamal.lucid.drawingpart.8
                            @Override // yuku.ambilwarna.AmbilWarnaDialog.OnAmbilWarnaListener
                            public void onCancel(AmbilWarnaDialog ambilWarnaDialog) {
                            }

                            @Override // yuku.ambilwarna.AmbilWarnaDialog.OnAmbilWarnaListener
                            public void onOk(AmbilWarnaDialog ambilWarnaDialog, int i) {
                               tempp1i=i;
                               viewtodisplay.findViewById(R.id.ShowColor).setBackgroundColor(i);
                            }
                        }).show();

                    }
                });
                SeekBar sk22=(SeekBar) viewtodisplay.findViewById(R.id.pixelStrokeWith);
                sk22.setProgress((int) (mycanv.CurrentNotes.GuideLinesthickness*20));
                sk22.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                        mycanv.CurrentNotes.GuideLinesthickness= i/20f+0.1f;
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {

                    }
                });
                tempp1f=mycanv.CurrentNotes.GudieLineSpacing;
                ((EditText)viewtodisplay.findViewById(R.id.SpacingInputted)).setHint(""+tempp1f);
                viewtodisplay.findViewById(R.id.SetLengthBtn).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        EditText txt=viewtodisplay.findViewById(R.id.SpacingInputted);
                        try {
                            float tempp22 = Float.parseFloat(txt.getText().toString());
                            if(tempp22>=5f && tempp22<=200f){
                                tempp1f=tempp22;
                                mycanv.CurrentNotes.ShowGuideLines=true;
                                mycanv.CurrentNotes.GudieLineSpacing=tempp1f;
                                mycanv.invalidate();
                            }
                            else{
                                Toast.makeText(drawingpart.this,"invallied input,Range (5-200)",Toast.LENGTH_SHORT).show();
                            }

                        } catch (NumberFormatException nm){

                        }

                    }
                });
                viewtodisplay.findViewById(R.id.ApplyBtn).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mycanv.CurrentNotes.ShowGuideLines=true;
                        mycanv.CurrentNotes.GuideLinesColor=tempp1i;
                        mycanv.CurrentNotes.GudieLineSpacing=tempp1f;
                        mycanv.invalidate();
                        dialog11.dismiss();
                    }
                });
                viewtodisplay.findViewById(R.id.CancleBtn).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mycanv.CurrentNotes.ShowGuideLines=false;
                        mycanv.invalidate();
                        dialog11.dismiss();
                    }
                });
                return true;
            }
        });

        //-----
       tmer=new Timer();
       TimerTask tmsk=new TimerTask() {
           @Override
           public void run() {
               handler.post(new Runnable() {
                   @Override
                   public void run() {
                       if(should_save_now) {
                           should_save_now=false;
                           mycanv.saveitNoNotification();
                       }
                   }
               });
           }
       };
       tmer.schedule(tmsk,5000,10000);
        //-------
    }




    public /* synthetic */ boolean lambda$onCreate$0$drawingpart(View view) {
        this.mycanv.pen1.CurrentTask = 1;
        this.m4.setBackgroundColor(Color.rgb(200, 200, 0));
        return true;
    }

    public /* synthetic */ void lambda$onCreate$1$drawingpart(View view) {
        if (this.mycanv.pen1.CurrentTask != 0) {
            this.mycanv.pen1.CurrentTask = 0;
            this.m4.setBackgroundColor(Color.rgb(220, 220, 220));

            return;
        }
        OpenColorPallet(this.mycanv);
    }

    public void exportlucidnote() {
        Toast.makeText(getApplicationContext(),"Please Wait while Exporting",Toast.LENGTH_SHORT).show();
        ExportThreadLucidNote exportThread=new ExportThreadLucidNote(mycanv);
        new Thread(exportThread).start();


    }

    class ExportThreadLucidNote implements Runnable{
        Mycanvas1 mycan222;
        ExportThreadLucidNote(Mycanvas1 mycan22){
            mycan222=mycan22;
        }
        @Override
        public void run() {
            if (!this.mycan222.imagemode) {
                String str = "LucidExportsNote" + new Gson().toJson(this.mycan222.CurrentNotes);
                try {
                    FileOutputStream fileOutputStream = new FileOutputStream(new File(getExternalFilesDir("/"), this.mycan222.CurrentNotes.name + ".lucidnotes"));
                    fileOutputStream.write(str.getBytes());
                    fileOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Intent intent = new Intent("android.intent.action.SEND");
                intent.setType("application/pdf");
                intent.putExtra("android.intent.extra.STREAM", FileProvider.getUriForFile(getApplicationContext(), "com.kamal.lucid.provider", new File(getExternalFilesDir("/"), this.mycan222.CurrentNotes.name + ".lucidnotes")));
                startActivity(Intent.createChooser(intent, "Export"));
            }
        }
    }

    public void m1clicked(View view) {
        if (this.m2.getVisibility() == View.VISIBLE) {
            this.m2.setVisibility(View.INVISIBLE);;
            this.m3.setVisibility(View.INVISIBLE);;
            this.Cp2.setVisibility(View.INVISIBLE);;
            this.m4.setVisibility(View.VISIBLE);;
            this.bub.setVisibility(View.INVISIBLE);;
            this.brb.setVisibility(View.INVISIBLE);;
            this.bstroke.setVisibility(View.INVISIBLE);;
            this.skb.setVisibility(View.INVISIBLE);;
            this.Smoothbar.setVisibility(View.INVISIBLE);;
            this.XshiftBar.setVisibility(View.INVISIBLE);;
            this.YshiftBar.setVisibility(View.INVISIBLE);;
            this.displayer.setVisibility(View.INVISIBLE);
            this.pdb.setVisibility(View.INVISIBLE);;
            this.pcb.setVisibility(View.INVISIBLE);;
            this.pub.setVisibility(View.INVISIBLE);;
            this.Eb.setVisibility(View.INVISIBLE);;
            this.Eb1.setVisibility(View.INVISIBLE);;
            RemoveAllAdditionalViews();
            this.Eb2.setVisibility(View.INVISIBLE);;
            this.m2.setEnabled(false);
            this.m3.setEnabled(false);
            this.Cp2.setEnabled(false);
            this.m4.setEnabled(true);
            this.bub.setEnabled(false);
            this.brb.setEnabled(false);
            this.bstroke.setEnabled(false);
            this.skb.setEnabled(false);
            this.Smoothbar.setEnabled(false);
            this.XshiftBar.setEnabled(false);
            this.YshiftBar.setEnabled(false);
            this.pdb.setEnabled(false);
            this.pcb.setEnabled(false);
            this.pub.setEnabled(false);
            this.Eb.setEnabled(false);
            this.Eb1.setEnabled(false);
            this.Eb2.setEnabled(false);
        }
        else {
            this.m2.setVisibility(View.VISIBLE);;
            this.m3.setVisibility(View.VISIBLE);;
            this.Cp2.setVisibility(View.VISIBLE);;
            this.m4.setVisibility(View.INVISIBLE);;
            this.bub.setVisibility(View.VISIBLE);;
            this.brb.setVisibility(View.VISIBLE);;
            this.bstroke.setVisibility(View.VISIBLE);;
            this.pdb.setVisibility(View.VISIBLE);;
            this.pcb.setVisibility(View.VISIBLE);;
            this.pub.setVisibility(View.VISIBLE);;
            this.Eb.setVisibility(View.VISIBLE);;
            this.m2.setEnabled(true);
            this.m3.setEnabled(true);
            this.Cp2.setEnabled(true);
            this.m4.setEnabled(false);
            this.bub.setEnabled(true);
            this.brb.setEnabled(true);
            this.bstroke.setEnabled(true);
            this.pdb.setEnabled(true);
            this.pcb.setEnabled(true);
            this.pub.setEnabled(true);
            this.Eb.setEnabled(true);
        }
        penmodeactive();
    }

    public void m2(View view) {
        int[] iArr = new int[9];
        System.arraycopy(this.mycanv.pen1.color, 0, iArr, 0, 9);
        for (int i = 0; i < 9; i++) {
            this.mycanv.pen1.color[i] = iArr[(i + 3) % 9];
        }
        this.m1.setBackgroundColor(Color.rgb(this.mycanv.pen1.color[0], this.mycanv.pen1.color[1], this.mycanv.pen1.color[2]));
        this.m2.setBackgroundColor(Color.rgb(this.mycanv.pen1.color[3], this.mycanv.pen1.color[4], this.mycanv.pen1.color[5]));
        this.m3.setBackgroundColor(Color.rgb(this.mycanv.pen1.color[6], this.mycanv.pen1.color[7], this.mycanv.pen1.color[8]));
        m1clicked(this.mycanv);
        penmodeactive();
    }

    public void m3(View view) {
        int[] iArr = new int[9];
        System.arraycopy(this.mycanv.pen1.color, 0, iArr, 0, 9);
        for (int i = 0; i < 9; i++) {
            this.mycanv.pen1.color[i] = iArr[(i + 6) % 9];
        }
        this.m1.setBackgroundColor(Color.rgb(this.mycanv.pen1.color[0], this.mycanv.pen1.color[1], this.mycanv.pen1.color[2]));
        this.m2.setBackgroundColor(Color.rgb(this.mycanv.pen1.color[3], this.mycanv.pen1.color[4], this.mycanv.pen1.color[5]));
        this.m3.setBackgroundColor(Color.rgb(this.mycanv.pen1.color[6], this.mycanv.pen1.color[7], this.mycanv.pen1.color[8]));
        m1clicked(this.mycanv);
        penmodeactive();
    }

    public void bub(View view) {
        if (this.mycanv.CurrentNotes.pages.get(this.mycanv.CPgNumber).UndoNumber < this.mycanv.CurrentNotes.pages.get(this.mycanv.CPgNumber).pathslist.size()) {
            this.mycanv.CurrentNotes.pages.get(this.mycanv.CPgNumber).UndoNumber++;
            this.mycanv.invalidate();
        }
    }

    public void brb(View view) {
        if (this.mycanv.CurrentNotes.pages.get(this.mycanv.CPgNumber).UndoNumber > 0) {
            PageLucid pageVar = this.mycanv.CurrentNotes.pages.get(this.mycanv.CPgNumber);
            pageVar.UndoNumber--;
            this.mycanv.invalidate();
        }
    }

    public void bstroke(View view) {
        if (this.skb.getVisibility() == View.VISIBLE) {
            this.skb.setVisibility(View.INVISIBLE);;
            this.skb.setEnabled(false);
            this.Smoothbar.setVisibility(View.INVISIBLE);;
            this.Smoothbar.setEnabled(false);
            this.XshiftBar.setVisibility(View.INVISIBLE);;
            this.XshiftBar.setEnabled(false);
            this.YshiftBar.setVisibility(View.INVISIBLE);;
            this.YshiftBar.setEnabled(false);
            this.displayer.setVisibility(View.INVISIBLE);
        } else {
            this.skb.setVisibility(View.VISIBLE);;
            this.skb.setEnabled(true);
            this.Smoothbar.setVisibility(View.VISIBLE);;
            this.Smoothbar.setEnabled(true);
            this.XshiftBar.setVisibility(View.VISIBLE);;
            this.XshiftBar.setEnabled(true);
            this.YshiftBar.setVisibility(View.VISIBLE);;
            this.YshiftBar.setEnabled(true);
            this.displayer.setVisibility(View.VISIBLE);
        }
        penmodeactive();
    }

    public void pagedown(View view) {
        this.mycanv.pagedown2();
        penmodeactive();
        displaypagecount(this.mycanv);
    }

    public void pageup(View view) {
        this.mycanv.pageup2();
        penmodeactive();
        displaypagecount(this.mycanv);
    }

    public void OpenColorPallet(View view) {
        if (this.mycanv.usepen) {
            if(layoutP.indexOfChild(viewcp)==-1) {
                viewcp = getLayoutInflater().inflate(R.layout.pen_colors_chooser, layoutP, false);
                LinearLayout linearLayout = viewcp.findViewById(R.id.linearLayout);

                GridView gridViewodColors = viewcp.findViewById(R.id.gridOfColors);
                gridViewodColors.setAdapter(new ColorPalletGridAdapter(drawingpart.this, coloursOfAllSections.get(lastChoosenColorSection)));
                ArrayList<View> colorSectionTileViews = new ArrayList<>();
                for (int i = 0; i < colorPalletSections.size(); i++) {
                    View vtemp = getLayoutInflater().inflate(R.layout.single_title_display, null);
                    ((TextView) vtemp.findViewById(R.id.textView6)).setText(colorPalletSections.get(i));
                    colorSectionTileViews.add(vtemp);
                    linearLayout.addView(vtemp);
                    if (i == lastChoosenColorSection) {
                        vtemp.setBackgroundColor(Color.parseColor("#ff6600"));
                    }
                    if (i != 8) {
                        vtemp.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                for (View vt : colorSectionTileViews) {
                                    vt.setBackgroundColor(Color.parseColor("#0077AE"));
                                }
                                lastChoosenColorSection = colorSectionTileViews.indexOf(view);
                                colorSectionTileViews.get(lastChoosenColorSection).setBackgroundColor(Color.parseColor("#ff6600"));
                                gridViewodColors.setAdapter(new ColorPalletGridAdapter(drawingpart.this, coloursOfAllSections.get(lastChoosenColorSection)));
                                SaveColors();
                            }
                        });
                    } else {
                        vtemp.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                layoutP.removeView(viewcp);
                            }
                        });
                    }
                    if (i == 0) {
                        vtemp.setOnLongClickListener(new View.OnLongClickListener() {
                            @Override
                            public boolean onLongClick(View view) {
                                resetPalletone();
                                SaveColors();
                                return false;
                            }
                        });
                    }
                }

                gridViewodColors.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        layoutP.removeView(viewcp);
//                        int[] tempset = new int[9];
//                        System.arraycopy(mycanv.pen1.color, 0, tempset, 0, 9);
//                        for (int j = 0; j < 9; j++) {
//                            mycanv.pen1.color[(j + 3) % 9] = tempset[j];
//                        }
                        mycanv.pen1.color[0] = Color.red(coloursOfAllSections.get(lastChoosenColorSection).get(i));
                        mycanv.pen1.color[1] = Color.green(coloursOfAllSections.get(lastChoosenColorSection).get(i));
                        mycanv.pen1.color[2] = Color.blue(coloursOfAllSections.get(lastChoosenColorSection).get(i));
                        m1.setBackgroundColor(coloursOfAllSections.get(lastChoosenColorSection).get(i));
                        m2.setBackgroundColor(Color.rgb(mycanv.pen1.color[3], mycanv.pen1.color[4], mycanv.pen1.color[5]));
                        m3.setBackgroundColor(Color.rgb(mycanv.pen1.color[6], mycanv.pen1.color[7], mycanv.pen1.color[8]));
                    }
                });
                gridViewodColors.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                    @Override
                    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                        new AmbilWarnaDialog(drawingpart.this, coloursOfAllSections.get(lastChoosenColorSection).get(i), new AmbilWarnaDialog.OnAmbilWarnaListener() { // from class: com.kamal.lucid.drawingpart.8
                            @Override // yuku.ambilwarna.AmbilWarnaDialog.OnAmbilWarnaListener
                            public void onCancel(AmbilWarnaDialog ambilWarnaDialog) {
                            }

                            @Override // yuku.ambilwarna.AmbilWarnaDialog.OnAmbilWarnaListener
                            public void onOk(AmbilWarnaDialog ambilWarnaDialog, int j) {
                                coloursOfAllSections.get(lastChoosenColorSection).set(i, j);
                                SaveColors();
                                gridViewodColors.setAdapter(new ColorPalletGridAdapter(drawingpart.this, coloursOfAllSections.get(lastChoosenColorSection)));
                                mycanv.pen1.color[0] = Color.red(coloursOfAllSections.get(lastChoosenColorSection).get(i));
                                mycanv.pen1.color[1] = Color.green(coloursOfAllSections.get(lastChoosenColorSection).get(i));
                                mycanv.pen1.color[2] = Color.blue(coloursOfAllSections.get(lastChoosenColorSection).get(i));
                                m1.setBackgroundColor(coloursOfAllSections.get(lastChoosenColorSection).get(i));
                                m2.setBackgroundColor(Color.rgb(mycanv.pen1.color[3], mycanv.pen1.color[4], mycanv.pen1.color[5]));
                                m3.setBackgroundColor(Color.rgb(mycanv.pen1.color[6], mycanv.pen1.color[7], mycanv.pen1.color[8]));
                            }
                        }).show();

                        return true;
                    }
                });
                viewcp.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    }
                });
                viewcp.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {
                        return false;
                    }
                });

                layoutP.addView(viewcp);
            }
            else{
                layoutP.removeView(viewcp);
            }


//            m2(this.mycanv);
//            m1clicked(this.mycanv);
            return;
        }
    }

    public void penmodeactive() {
        if (this.mycanv.pen1.mode == 1) {
            OpenColorPallet(this.mycanv);
        }
    }

    public void displaypagecount(View view) {
        this.pcb.setText("" + (this.mycanv.CPgNumber + 1));
    }

    public void Eb(View view) {
        if (this.Eb1.getVisibility() == View.VISIBLE) {
            this.Eb1.setVisibility(View.INVISIBLE);;
            RemoveAllAdditionalViews();
            this.Eb1.setEnabled(false);
            this.Eb2.setVisibility(View.INVISIBLE);;
            this.Eb2.setEnabled(false);
            return;
        }
        this.Eb1.setVisibility(View.VISIBLE);;
        this.Eb1.setEnabled(true);
        this.Eb2.setVisibility(View.VISIBLE);;
        this.Eb2.setEnabled(true);
    }

    public void exporttopdf(View view) {


        Toast.makeText(getApplicationContext(),"Export will soon begin,Processing",Toast.LENGTH_SHORT).show();
        ExportThreadLucidPdf exportThreadLucidPdf=new ExportThreadLucidPdf(mycanv,expzf);
        new Thread(exportThreadLucidPdf).start();

    }

    public void exporttopdfOnlyDrawnings(View view) {

            Toast.makeText(getApplicationContext(), "Export will soon begin,Processing", Toast.LENGTH_SHORT).show();
            ExportThreadLucidPdfOnlyDrawings exportThreadLucidPdfOnlyDrawings = new ExportThreadLucidPdfOnlyDrawings(mycanv, expzf);
            new Thread(exportThreadLucidPdfOnlyDrawings).start();


    }

    public void exporttopdfOnlySelectedPages(View view,ArrayList<Integer> selpages) {

        Toast.makeText(getApplicationContext(), "Export will soon begin,Processing", Toast.LENGTH_SHORT).show();
        ExportThreadLucidPdfOnlySelectedPages exportThreadLucidPdfOnlySelectedPages = new ExportThreadLucidPdfOnlySelectedPages(mycanv, expzf,selpages);
        new Thread(exportThreadLucidPdfOnlySelectedPages).start();


    }

    public void OpenExportPdfMenu(View view){
        ToggleAdditionalView(option_menu_export);

            TextView AllPagesOption = option_menu_export.findViewById(R.id.menu_export_pdf_options_option1);
            TextView PNGoption = option_menu_export.findViewById(R.id.menu_export_pdf_options_option2);
            TextView SelectPagesOption = option_menu_export.findViewById(R.id.menu_export_pdf_options_option3);
            TextView PagesWithDrawingsOption = option_menu_export.findViewById(R.id.menu_export_pdf_options_option4);

        AllPagesOption.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    RemoveAllAdditionalViews();
                    if(!mycanv.imagemode) {
                        exporttopdf(view);
                    }
                }
            });
        PNGoption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RemoveAllAdditionalViews();
                //if(!mycanv.imagemode) {
                    Toast.makeText(getApplicationContext(), "Exporting as image", Toast.LENGTH_SHORT).show();
                    Bitmap bitmap = getBitmap();
                    saveBitmap("Lucid_OnDraw", bitmap,true);
                    Intent intent = new Intent("android.intent.action.SEND");
                    intent.setType("image/png");
                    intent.putExtra("android.intent.extra.STREAM", FileProvider.getUriForFile(getApplicationContext(), "com.kamal.lucid.provider", new File(getExternalFilesDir("/"), "Lucid_OnDraw.jpeg")));
                    startActivity(Intent.createChooser(intent, "Export"));
               // }
               // else{
                //    exporttopdf(view);
                //}

            }
        });
        SelectPagesOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                layoutP.removeView(option_menu_export);
                if(!mycanv.imagemode) {
                    //Toast.makeText(getApplicationContext(),"This feature is under Development",Toast.LENGTH_SHORT).show();
                    AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(drawingpart.this);
                    View dialogView=getLayoutInflater().inflate(R.layout.pages_select_number_display_layout,null);

                    dialogBuilder.setView(dialogView);
                    Dialog dialog2=dialogBuilder.create();
                    dialog2.show();

                    GridView gridview2=dialogView.findViewById(R.id.Page_Number_Selector_Grid);

                    ArrayList<Integer> selectedpages=new ArrayList<>();
                    gridview2.setAdapter(new PagesSelectGridAdapter(drawingpart.this,Math.max(mycanv.total_pages_pdf,mycanv.CurrentNotes.pages.size()),selectedpages));

                    gridview2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                if (selectedpages.contains(i)) {
                                    view.findViewById(R.id.layoutpagenumber).setBackgroundResource(R.drawable.custombuttontheme1);
                                    selectedpages.remove(selectedpages.indexOf(i));
                                } else {
                                    selectedpages.add(i);
                                    view.findViewById(R.id.layoutpagenumber).setBackgroundResource(R.drawable.custombuttontheme2);
                                }

                        }
                    });
                    Button expSelbtn=dialogView.findViewById(R.id.Export_selection_btn);
                    expSelbtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if(selectedpages.size()>0){
                                exporttopdfOnlySelectedPages(mycanv,selectedpages);
                                dialog2.dismiss();
                            }
                            else{
                                Toast.makeText(getApplicationContext(),"Please Select Pages To Export",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                    //exporttopdf(view);
                }

            }
        });
        PagesWithDrawingsOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                layoutP.removeView(option_menu_export);
                if(!mycanv.imagemode) {
                    exporttopdfOnlyDrawnings(view);
                }
            }
        });
        View v222=layoutP.findViewById(R.id.view2);
        v222.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(),"yes1",Toast.LENGTH_SHORT).show();
            }
        });


    }

    public Bitmap getBitmap(){
        Bitmap tempbitmap = null;
        mycanv.ResetMinimumPageSizesForDrawingExport();
        int cpg=mycanv.CPgNumber;
         ANote Cnote=mycanv.CurrentNotes;
         PageLucid page=mycanv.CurrentNotes.pages.get(cpg);
         Paint p1=new Paint();
         int width,height;
         if(mycanv.imagemode){
             width = (int) Math.max(Cnote.pages.get(cpg).Minimim_Page_Width_To_Fit_drawing, Math.max(Cnote.pages.get(cpg).Minimum_Page_Width, mycanv.CurrentBitmap.getWidth()+30));
             height = (int) Math.max(Cnote.pages.get(cpg).Minimum_Page_Height_To_Fit_drawing, Math.max(Cnote.pages.get(cpg).Minimum_Page_Height, mycanv.CurrentBitmap.getHeight()+50));
         }
         else {
            width = (int) Math.max(Cnote.pages.get(cpg).Minimim_Page_Width_To_Fit_drawing, Math.max(Cnote.pages.get(cpg).Minimum_Page_Width, mycanv.CurrentBitmap.getWidth()));
             height = (int) Math.max(Cnote.pages.get(cpg).Minimum_Page_Height_To_Fit_drawing, Math.max(Cnote.pages.get(cpg).Minimum_Page_Height, mycanv.CurrentBitmap.getHeight()));
         }
        if(Cnote.Fixed_Page_Height>0) {
            width=Cnote.Fixed_Page_Width;
            height=Cnote.Fixed_Page_Height;
        }

        tempbitmap=Bitmap.createBitmap(width, height,Bitmap.Config.ARGB_8888);

        Canvas c=new Canvas(tempbitmap);
        p1.setAntiAlias(true);p1.setColor(page.PColor);p1.setStyle(Paint.Style.FILL);
        c.drawRect(0,0,width,height,p1);
        if(mycanv.imagemode){
            c.drawBitmap(mycanv.CurrentBitmap, 30, 50, mycanv.paint);
        }
        else {
            c.drawBitmap(mycanv.CurrentBitmap, 0, 0, mycanv.paint);
        }
        p1.setStrokeCap(Paint.Cap.ROUND);
        p1.setStrokeJoin(Paint.Join.ROUND);
        p1.setStyle(Paint.Style.STROKE);
        for (PathswithC p:page.pathslist) {
            p1.setColor(Color.rgb(p.c[0],p.c[1],p.c[2]));
            p1.setStrokeWidth(p.sw);
            p.P.offset(p.pathXShift,p.pathYShift);
            c.drawPath(p.P,p1);
            p.P.offset(-p.pathXShift,-p.pathYShift);
        }

        return tempbitmap;
    }

    class ExportThreadLucidPdf implements Runnable{
        Mycanvas1 mycan222p;
        float expzf=1f;
        int i4=0,i92=0,i12=0;
        ExportThreadLucidPdf(Mycanvas1 mycanvas1,float expzf1){
            mycan222p=mycanvas1;
            this.expzf=expzf1;
        }
        @Override
        public void run() {
            int i = 1;
            if (this.mycan222p.imagemode) {
                Bitmap bitmap = null;
                int i2 = 0;
                while (i2 < this.mycan222p.CurrentNotes.pages.size()) {
                    PageLucid pageVar = this.mycan222p.CurrentNotes.pages.get(i2);
                    int max = ((int) Math.max(pageVar.Minimum_Page_Width, pageVar.Minimim_Page_Width_To_Fit_drawing)) + 80;
                    int max2 = ((int) Math.max(pageVar.Minimum_Page_Height, pageVar.Minimum_Page_Height_To_Fit_drawing)) + 80;
                    Bitmap createBitmap = Bitmap.createBitmap(max, max2, Bitmap.Config.ARGB_8888);
                    Canvas canvas = new Canvas();
                    canvas.setBitmap(createBitmap);
                    this.mycan222p.paint2.setColor(this.mycan222p.CurrentNotes.pages.get(i2).PColor);
                    this.mycan222p.paint.setStrokeCap(Paint.Cap.ROUND);
                    canvas.drawRect(0.0f, 0.0f, (float) max, (float) max2, this.mycan222p.paint2);
                    canvas.drawBitmap(this.mycan222p.OnDrawCurrentBitmap, 30.0f, 50.0f, this.mycan222p.paint);
                    for (int i3 = 0; i3 < this.mycan222p.CurrentNotes.pages.get(i2).pathslist.size() - this.mycan222p.CurrentNotes.pages.get(i2).UndoNumber; i3++) {
                        if (this.mycan222p.CurrentNotes.pages.get(i2).pathslist.get(i3).IsVissable == 1) {
                            this.mycan222p.paint.setStrokeWidth(this.mycan222p.CurrentNotes.pages.get(i2).pathslist.get(i3).sw);
                            this.mycan222p.paint.setColor(Color.rgb(this.mycan222p.CurrentNotes.pages.get(i2).pathslist.get(i3).c[0], this.mycan222p.CurrentNotes.pages.get(i2).pathslist.get(i3).c[1], this.mycan222p.CurrentNotes.pages.get(i2).pathslist.get(i3).c[2]));
                            this.mycan222p.CurrentNotes.pages.get(i2).pathslist.get(i3).P.offset(this.mycan222p.CurrentNotes.pages.get(i2).pathslist.get(i3).pathXShift, this.mycan222p.CurrentNotes.pages.get(i2).pathslist.get(i3).pathYShift);
                            canvas.drawPath(this.mycan222p.CurrentNotes.pages.get(i2).pathslist.get(i3).P, this.mycan222p.paint);
                            this.mycan222p.CurrentNotes.pages.get(i2).pathslist.get(i3).P.offset(-this.mycan222p.CurrentNotes.pages.get(i2).pathslist.get(i3).pathXShift, -this.mycan222p.CurrentNotes.pages.get(i2).pathslist.get(i3).pathYShift);
                        }
                    }
                    i2++;
                    bitmap = createBitmap;
                }
                saveBitmap("Lucid_OnDraw", bitmap,true);
                Intent intent = new Intent("android.intent.action.SEND");
                intent.setType("image/png");
                intent.putExtra("android.intent.extra.STREAM", FileProvider.getUriForFile(getApplicationContext(), "com.kamal.lucid.provider", new File(getExternalFilesDir("/"), "Lucid_OnDraw.jpeg")));
                startActivity(Intent.createChooser(intent, "Export"));
                return;
            }
            else{
                mycan222p.CurrentNotes.exportNumber+=1;
            }
            //this.Eb1.setText("Exporting");
            PdfDocument pdfDocument = new PdfDocument();
            handler.post(new Runnable() {
                @Override
                public void run() {
                    progressBar.setVisibility(View.VISIBLE);
                    progressBar.setMax(mycan222p.CurrentNotes.pages.size());
                }
            });
            if (this.mycan222p.CurrentNotes.Maximum_Allowed_Page_Height <= 100000) {
                while (i4 < this.mycan222p.CurrentNotes.pages.size()) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            progressBar.setProgress(i4);
                        }
                    });
                    int i5 = (int) (((float) this.mycan222p.CurrentNotes.Maximum_Allowed_Page_Width) * this.expzf);
                    int i6 = ((int) (((float) this.mycan222p.CurrentNotes.Maximum_Allowed_Page_Height) * this.expzf)) + 30;
                    int i7 = i4 + 1;
                    PdfDocument.Page startPage = pdfDocument.startPage(new PdfDocument.PageInfo.Builder(i5, i6, i7).create());
                    Canvas canvas2 = startPage.getCanvas();
                    float f = this.expzf;
                    canvas2.scale(f, f);
                    this.mycan222p.paint2.setColor(this.mycan222p.CurrentNotes.pages.get(i4).PColor);
                    this.mycan222p.paint.setStrokeCap(Paint.Cap.ROUND);
                    float f2 = this.expzf;
                    canvas2.drawRect(0.0f, 0.0f, ((float) i5) / f2, ((float) i6) / f2, this.mycan222p.paint2);
                    int i8 = 0;
                    while (i8 < this.mycan222p.CurrentNotes.pages.get(i4).pathslist.size() - this.mycan222p.CurrentNotes.pages.get(i4).UndoNumber) {
                        if (this.mycan222p.CurrentNotes.pages.get(i4).pathslist.get(i8).IsVissable == i) {
                            this.mycan222p.paint.setStrokeWidth(this.mycan222p.CurrentNotes.pages.get(i4).pathslist.get(i8).sw);
                            this.mycan222p.paint.setColor(Color.rgb(this.mycan222p.CurrentNotes.pages.get(i4).pathslist.get(i8).c[0], this.mycan222p.CurrentNotes.pages.get(i4).pathslist.get(i8).c[1], this.mycan222p.CurrentNotes.pages.get(i4).pathslist.get(i8).c[2]));
                            this.mycan222p.CurrentNotes.pages.get(i4).pathslist.get(i8).P.offset(this.mycan222p.CurrentNotes.pages.get(i4).pathslist.get(i8).pathXShift, this.mycan222p.CurrentNotes.pages.get(i4).pathslist.get(i8).pathYShift);
                            canvas2.drawPath(this.mycan222p.CurrentNotes.pages.get(i4).pathslist.get(i8).P, this.mycan222p.paint);
                            this.mycan222p.CurrentNotes.pages.get(i4).pathslist.get(i8).P.offset(-this.mycan222p.CurrentNotes.pages.get(i4).pathslist.get(i8).pathXShift, -this.mycan222p.CurrentNotes.pages.get(i4).pathslist.get(i8).pathYShift);
                        }
                        i8++;
                        i = 1;
                    }
                    pdfDocument.finishPage(startPage);
                    i4 = i7;
                    i = 1;
                }
            }
            else if (this.mycan222p.CurrentNotes.APdfNote) {
                Bitmap PdfToBitmap = null;
                PdfRenderer pdfRenderer = null;
                try {
                    pdfRenderer = new PdfRenderer(ParcelFileDescriptor.open(this.mycan222p.CurrentHandellingPdf, 268435456));

                } catch (IOException e) {
                    e.printStackTrace();
                }

                for (int i9 = 0; i9 < this.mycan222p.CurrentNotes.pages.size(); i9++) {
                    i92=i9;
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            progressBar.setProgress(i92);
                        }
                    });
                    if (i9 < this.mycan222p.total_pages_pdf) {

                        //-----------
                        PdfRenderer.Page openPage = pdfRenderer.openPage(i9);
                        Bitmap createBitmap2 = Bitmap.createBitmap((getResources().getDisplayMetrics().densityDpi / 72) * openPage.getWidth(), (getResources().getDisplayMetrics().densityDpi / 72) * openPage.getHeight(), Bitmap.Config.ARGB_8888);
                        openPage.render(createBitmap2, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY);
                        try {
                            openPage.close();
                            PdfToBitmap = createBitmap2;
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        //-------------

                        new PageLucid(1);
                        PageLucid pageVar2 = this.mycan222p.CurrentNotes.pages.get(i9);
                        int max3 = (int) (Math.max(Math.max(pageVar2.Minimum_Page_Width,mycan222p.PdfFcx), pageVar2.Minimim_Page_Width_To_Fit_drawing) * this.expzf);
                        int max4 = (int) (Math.max(Math.max(pageVar2.Minimum_Page_Height,mycan222p.PdfFcy), pageVar2.Minimum_Page_Height_To_Fit_drawing) * this.expzf);
                        if(mycan222p.CurrentNotes.pdfFixedBoundaries){
                            max3= (int) (createBitmap2.getWidth()*this.expzf);
                            max4= (int) (createBitmap2.getHeight()*this.expzf);
                        }
                        PdfDocument.Page startPage2 = pdfDocument.startPage(new PdfDocument.PageInfo.Builder(max3, max4, i9 + 1).create());
                        Canvas canvas3 = startPage2.getCanvas();
                        float f3 = this.expzf;
                        canvas3.scale(f3, f3);
                        this.mycan222p.paint2.setColor(this.mycan222p.CurrentNotes.pages.get(i9).PColor);
                        this.mycan222p.paint.setStrokeCap(Paint.Cap.ROUND);
                        float f4 = this.expzf;
                        canvas3.drawRect(0.0f, 0.0f, ((float) max3) / f4, ((float) max4) / f4, this.mycan222p.paint2);
                        canvas3.drawBitmap(PdfToBitmap, 0.0f, 0.0f, this.mycan222p.paint);
                        for (int i10 = 0; i10 < this.mycan222p.CurrentNotes.pages.get(i9).pathslist.size() - this.mycan222p.CurrentNotes.pages.get(i9).UndoNumber; i10++) {
                            if (this.mycan222p.CurrentNotes.pages.get(i9).pathslist.get(i10).IsVissable == 1) {
                                this.mycan222p.paint.setStrokeWidth(this.mycan222p.CurrentNotes.pages.get(i9).pathslist.get(i10).sw);
                                this.mycan222p.paint.setColor(Color.rgb(this.mycan222p.CurrentNotes.pages.get(i9).pathslist.get(i10).c[0], this.mycan222p.CurrentNotes.pages.get(i9).pathslist.get(i10).c[1], this.mycan222p.CurrentNotes.pages.get(i9).pathslist.get(i10).c[2]));
                                this.mycan222p.CurrentNotes.pages.get(i9).pathslist.get(i10).P.offset(this.mycan222p.CurrentNotes.pages.get(i9).pathslist.get(i10).pathXShift, this.mycan222p.CurrentNotes.pages.get(i9).pathslist.get(i10).pathYShift);
                                canvas3.drawPath(this.mycan222p.CurrentNotes.pages.get(i9).pathslist.get(i10).P, this.mycan222p.paint);
                                this.mycan222p.CurrentNotes.pages.get(i9).pathslist.get(i10).P.offset(-this.mycan222p.CurrentNotes.pages.get(i9).pathslist.get(i10).pathXShift, -this.mycan222p.CurrentNotes.pages.get(i9).pathslist.get(i10).pathYShift);
                            }
                        }
                        pdfDocument.finishPage(startPage2);
                    }
                    else {
                        PageLucid pageVar3 = this.mycan222p.CurrentNotes.pages.get(i9);
                        int max5 = (int) Math.max(pageVar3.Minimum_Page_Width + 80.0f, pageVar3.Minimim_Page_Width_To_Fit_drawing);
                        int max6 = (int) Math.max(pageVar3.Minimum_Page_Height + 80.0f, pageVar3.Minimum_Page_Height_To_Fit_drawing);
                        PdfDocument.Page startPage3 = pdfDocument.startPage(new PdfDocument.PageInfo.Builder(max5, max6, i9 + 1).create());
                        Canvas canvas4 = startPage3.getCanvas();
                        this.mycan222p.paint2.setColor(this.mycan222p.CurrentNotes.pages.get(i9).PColor);
                        this.mycan222p.paint.setStrokeCap(Paint.Cap.ROUND);
                        Canvas canvas5 = canvas4;
                        canvas4.drawRect(0.0f, 0.0f, (float) max5, (float) max6, this.mycan222p.paint2);
                        for (int i11 = 0; i11 < this.mycan222p.CurrentNotes.pages.get(i9).pathslist.size() - this.mycan222p.CurrentNotes.pages.get(i9).UndoNumber; i11++) {
                            if (this.mycan222p.CurrentNotes.pages.get(i9).pathslist.get(i11).IsVissable == 1) {
                                this.mycan222p.paint.setStrokeWidth(this.mycan222p.CurrentNotes.pages.get(i9).pathslist.get(i11).sw);
                                this.mycan222p.paint.setColor(Color.rgb(this.mycan222p.CurrentNotes.pages.get(i9).pathslist.get(i11).c[0], this.mycan222p.CurrentNotes.pages.get(i9).pathslist.get(i11).c[1], this.mycan222p.CurrentNotes.pages.get(i9).pathslist.get(i11).c[2]));
                                this.mycan222p.CurrentNotes.pages.get(i9).pathslist.get(i11).P.offset(this.mycan222p.CurrentNotes.pages.get(i9).pathslist.get(i11).pathXShift, this.mycan222p.CurrentNotes.pages.get(i9).pathslist.get(i11).pathYShift);
                                canvas5 = canvas5;
                                canvas5.drawPath(this.mycan222p.CurrentNotes.pages.get(i9).pathslist.get(i11).P, this.mycan222p.paint);
                                this.mycan222p.CurrentNotes.pages.get(i9).pathslist.get(i11).P.offset(-this.mycan222p.CurrentNotes.pages.get(i9).pathslist.get(i11).pathXShift, -this.mycan222p.CurrentNotes.pages.get(i9).pathslist.get(i11).pathYShift);
                            } else {
                                canvas5 = canvas5;
                            }
                        }
                        pdfDocument.finishPage(startPage3);
                    }
                }
                pdfRenderer.close();
            }
            else {
                while (i12 < this.mycan222p.CurrentNotes.pages.size()) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            progressBar.setProgress(i12);
                        }
                    });
                    PageLucid pageVar4 = this.mycan222p.CurrentNotes.pages.get(i12);
                    int max7 = (int) (Math.max(pageVar4.Minimum_Page_Width, pageVar4.Minimim_Page_Width_To_Fit_drawing) * this.expzf);
                    int max8 = (int) (Math.max(pageVar4.Minimum_Page_Height, pageVar4.Minimum_Page_Height_To_Fit_drawing) * this.expzf);
                    int i13 = i12 + 1;
                    PdfDocument.Page startPage4 = pdfDocument.startPage(new PdfDocument.PageInfo.Builder(max7, max8, i13).create());
                    Canvas canvas6 = startPage4.getCanvas();
                    float f5 = this.expzf;
                    canvas6.scale(f5, f5);
                    this.mycan222p.paint2.setColor(this.mycan222p.CurrentNotes.pages.get(i12).PColor);
                    this.mycan222p.paint.setStrokeCap(Paint.Cap.ROUND);
                    float f6 = this.expzf;
                    Canvas canvas7 = canvas6;
                    canvas6.drawRect(0.0f, 0.0f, ((float) max7) / f6, ((float) max8) / f6, this.mycan222p.paint2);
                    for (int i14 = 0; i14 < this.mycan222p.CurrentNotes.pages.get(i12).pathslist.size() - this.mycan222p.CurrentNotes.pages.get(i12).UndoNumber; i14++) {
                        if (this.mycan222p.CurrentNotes.pages.get(i12).pathslist.get(i14).IsVissable == 1) {
                            this.mycan222p.paint.setStrokeWidth(this.mycan222p.CurrentNotes.pages.get(i12).pathslist.get(i14).sw);
                            this.mycan222p.paint.setColor(Color.rgb(this.mycan222p.CurrentNotes.pages.get(i12).pathslist.get(i14).c[0], this.mycan222p.CurrentNotes.pages.get(i12).pathslist.get(i14).c[1], this.mycan222p.CurrentNotes.pages.get(i12).pathslist.get(i14).c[2]));
                            this.mycan222p.CurrentNotes.pages.get(i12).pathslist.get(i14).P.offset(this.mycan222p.CurrentNotes.pages.get(i12).pathslist.get(i14).pathXShift, this.mycan222p.CurrentNotes.pages.get(i12).pathslist.get(i14).pathYShift);
                            canvas7 = canvas7;
                            canvas7.drawPath(this.mycan222p.CurrentNotes.pages.get(i12).pathslist.get(i14).P, this.mycan222p.paint);
                            this.mycan222p.CurrentNotes.pages.get(i12).pathslist.get(i14).P.offset(-this.mycan222p.CurrentNotes.pages.get(i12).pathslist.get(i14).pathXShift, -this.mycan222p.CurrentNotes.pages.get(i12).pathslist.get(i14).pathYShift);
                        } else {
                            canvas7 = canvas7;
                        }
                    }
                    pdfDocument.finishPage(startPage4);
                    i12 = i13;
                }
            }
            handler.post(new Runnable() {
                @Override
                public void run() {
                    progressBar.setVisibility(View.INVISIBLE);
                }
            });
            try {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),"Saving Locally first,processing",Toast.LENGTH_SHORT).show();
                    }
                });
                FileOutputStream fileOutputStream2=new FileOutputStream(new File(getExternalFilesDir("/"), this.mycan222p.CurrentNotes.name + ".pdf"));
                //Log.d("testkk",pdfDocument.);
                pdfDocument.writeTo(fileOutputStream2);
            } catch (IOException e) {
                e.printStackTrace();
            }
            pdfDocument.close();
            Intent intent2 = new Intent("android.intent.action.SEND");
            intent2.setType("application/pdf");
            intent2.putExtra("android.intent.extra.STREAM", FileProvider.getUriForFile(getApplicationContext(), "com.kamal.lucid.provider", new File(getExternalFilesDir("/"), this.mycan222p.CurrentNotes.name + ".pdf")));
            //this.Eb1.setText("Export");
            startActivity(Intent.createChooser(intent2, "Export"));
            //this.delnow = 0;
        }
    }

    class ExportThreadLucidPdfOnlyDrawings implements Runnable{
        Mycanvas1 mycan222p;
        float expzf=1f;
        int i4=0,i92=0,i12=0;
        ExportThreadLucidPdfOnlyDrawings(Mycanvas1 mycanvas1,float expzf1){
            mycan222p=mycanvas1;
            this.expzf=expzf1;
        }
        @Override
        public void run() {
            int i = 1;
            if (this.mycan222p.imagemode) {
                Bitmap bitmap = null;
                int i2 = 0;
                while (i2 < this.mycan222p.CurrentNotes.pages.size()) {
                    PageLucid pageVar = this.mycan222p.CurrentNotes.pages.get(i2);
                    int max = ((int) Math.max(pageVar.Minimum_Page_Width, pageVar.Minimim_Page_Width_To_Fit_drawing)) + 80;
                    int max2 = ((int) Math.max(pageVar.Minimum_Page_Height, pageVar.Minimum_Page_Height_To_Fit_drawing)) + 80;
                    Bitmap createBitmap = Bitmap.createBitmap(max, max2, Bitmap.Config.ARGB_8888);
                    Canvas canvas = new Canvas();
                    canvas.setBitmap(createBitmap);
                    this.mycan222p.paint2.setColor(this.mycan222p.CurrentNotes.pages.get(i2).PColor);
                    this.mycan222p.paint.setStrokeCap(Paint.Cap.ROUND);
                    canvas.drawRect(0.0f, 0.0f, (float) max, (float) max2, this.mycan222p.paint2);
                    canvas.drawBitmap(this.mycan222p.OnDrawCurrentBitmap, 30.0f, 50.0f, this.mycan222p.paint);
                    for (int i3 = 0; i3 < this.mycan222p.CurrentNotes.pages.get(i2).pathslist.size() - this.mycan222p.CurrentNotes.pages.get(i2).UndoNumber; i3++) {
                        if (this.mycan222p.CurrentNotes.pages.get(i2).pathslist.get(i3).IsVissable == 1) {
                            this.mycan222p.paint.setStrokeWidth(this.mycan222p.CurrentNotes.pages.get(i2).pathslist.get(i3).sw);
                            this.mycan222p.paint.setColor(Color.rgb(this.mycan222p.CurrentNotes.pages.get(i2).pathslist.get(i3).c[0], this.mycan222p.CurrentNotes.pages.get(i2).pathslist.get(i3).c[1], this.mycan222p.CurrentNotes.pages.get(i2).pathslist.get(i3).c[2]));
                            this.mycan222p.CurrentNotes.pages.get(i2).pathslist.get(i3).P.offset(this.mycan222p.CurrentNotes.pages.get(i2).pathslist.get(i3).pathXShift, this.mycan222p.CurrentNotes.pages.get(i2).pathslist.get(i3).pathYShift);
                            canvas.drawPath(this.mycan222p.CurrentNotes.pages.get(i2).pathslist.get(i3).P, this.mycan222p.paint);
                            this.mycan222p.CurrentNotes.pages.get(i2).pathslist.get(i3).P.offset(-this.mycan222p.CurrentNotes.pages.get(i2).pathslist.get(i3).pathXShift, -this.mycan222p.CurrentNotes.pages.get(i2).pathslist.get(i3).pathYShift);
                        }
                    }
                    i2++;
                    bitmap = createBitmap;
                }
                saveBitmap("Lucid_OnDraw", bitmap,true);
                Intent intent = new Intent("android.intent.action.SEND");
                intent.setType("image/png");
                intent.putExtra("android.intent.extra.STREAM", FileProvider.getUriForFile(getApplicationContext(), "com.kamal.lucid.provider", new File(getExternalFilesDir("/"), "Lucid_OnDraw.jpeg")));
                startActivity(Intent.createChooser(intent, "Export"));
                return;
            }
            else{
                mycan222p.CurrentNotes.exportNumber+=1;
            }
            //this.Eb1.setText("Exporting");
            PdfDocument pdfDocument = new PdfDocument();
            handler.post(new Runnable() {
                @Override
                public void run() {
                    progressBar.setVisibility(View.VISIBLE);
                    progressBar.setMax(mycan222p.CurrentNotes.pages.size());
                }
            });
            if (this.mycan222p.CurrentNotes.Maximum_Allowed_Page_Height <= 100000) {
                while (i4 < this.mycan222p.CurrentNotes.pages.size()) {
                    if (this.mycan222p.CurrentNotes.pages.get(i4).pathslist.size() > 0 || i4==mycan222p.CPgNumber) {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                progressBar.setProgress(i4);
                            }
                        });
                        int i5 = (int) (((float) this.mycan222p.CurrentNotes.Maximum_Allowed_Page_Width) * this.expzf);
                        int i6 = ((int) (((float) this.mycan222p.CurrentNotes.Maximum_Allowed_Page_Height) * this.expzf)) + 30;
                        int i7 = i4 + 1;
                        PdfDocument.Page startPage = pdfDocument.startPage(new PdfDocument.PageInfo.Builder(i5, i6, i7).create());
                        Canvas canvas2 = startPage.getCanvas();
                        float f = this.expzf;
                        canvas2.scale(f, f);
                        this.mycan222p.paint2.setColor(this.mycan222p.CurrentNotes.pages.get(i4).PColor);
                        this.mycan222p.paint.setStrokeCap(Paint.Cap.ROUND);
                        float f2 = this.expzf;
                        canvas2.drawRect(0.0f, 0.0f, ((float) i5) / f2, ((float) i6) / f2, this.mycan222p.paint2);
                        int i8 = 0;
                        while (i8 < this.mycan222p.CurrentNotes.pages.get(i4).pathslist.size() - this.mycan222p.CurrentNotes.pages.get(i4).UndoNumber) {
                            if (this.mycan222p.CurrentNotes.pages.get(i4).pathslist.get(i8).IsVissable == i) {
                                this.mycan222p.paint.setStrokeWidth(this.mycan222p.CurrentNotes.pages.get(i4).pathslist.get(i8).sw);
                                this.mycan222p.paint.setColor(Color.rgb(this.mycan222p.CurrentNotes.pages.get(i4).pathslist.get(i8).c[0], this.mycan222p.CurrentNotes.pages.get(i4).pathslist.get(i8).c[1], this.mycan222p.CurrentNotes.pages.get(i4).pathslist.get(i8).c[2]));
                                this.mycan222p.CurrentNotes.pages.get(i4).pathslist.get(i8).P.offset(this.mycan222p.CurrentNotes.pages.get(i4).pathslist.get(i8).pathXShift, this.mycan222p.CurrentNotes.pages.get(i4).pathslist.get(i8).pathYShift);
                                canvas2.drawPath(this.mycan222p.CurrentNotes.pages.get(i4).pathslist.get(i8).P, this.mycan222p.paint);
                                this.mycan222p.CurrentNotes.pages.get(i4).pathslist.get(i8).P.offset(-this.mycan222p.CurrentNotes.pages.get(i4).pathslist.get(i8).pathXShift, -this.mycan222p.CurrentNotes.pages.get(i4).pathslist.get(i8).pathYShift);
                            }
                            i8++;
                            i = 1;
                        }
                        pdfDocument.finishPage(startPage);
                        i = 1;
                    }
                    i4+=1;
                }
            }
            else if (this.mycan222p.CurrentNotes.APdfNote) {
                Bitmap PdfToBitmap = null;
                PdfRenderer pdfRenderer = null;
                try {
                    pdfRenderer = new PdfRenderer(ParcelFileDescriptor.open(this.mycan222p.CurrentHandellingPdf, 268435456));

                } catch (IOException e) {
                    e.printStackTrace();
                }

                for (int i9 = 0; i9 < this.mycan222p.CurrentNotes.pages.size(); i9++) {
                    i92=i9;
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            progressBar.setProgress(i92);
                        }
                    });
                    if (this.mycan222p.CurrentNotes.pages.get(i9).pathslist.size() > 0 || i9==mycan222p.CPgNumber) {
                        if (i9 < this.mycan222p.total_pages_pdf) {

                            //-----------
                            PdfRenderer.Page openPage = pdfRenderer.openPage(i9);
                            Bitmap createBitmap2 = Bitmap.createBitmap((getResources().getDisplayMetrics().densityDpi / 72) * openPage.getWidth(), (getResources().getDisplayMetrics().densityDpi / 72) * openPage.getHeight(), Bitmap.Config.ARGB_8888);
                            openPage.render(createBitmap2, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY);
                            try {
                                openPage.close();
                                PdfToBitmap = createBitmap2;
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            //-------------

                            new PageLucid(1);
                            PageLucid pageVar2 = this.mycan222p.CurrentNotes.pages.get(i9);
                            int max3 = (int) (Math.max(Math.max(pageVar2.Minimum_Page_Width, mycan222p.PdfFcx), pageVar2.Minimim_Page_Width_To_Fit_drawing) * this.expzf);
                            int max4 = (int) (Math.max(Math.max(pageVar2.Minimum_Page_Height, mycan222p.PdfFcy), pageVar2.Minimum_Page_Height_To_Fit_drawing) * this.expzf);
                            if(mycan222p.CurrentNotes.pdfFixedBoundaries){
                                max3= (int) (createBitmap2.getWidth()*this.expzf);
                                max4= (int) (createBitmap2.getHeight()*this.expzf);
                            }
                            PdfDocument.Page startPage2 = pdfDocument.startPage(new PdfDocument.PageInfo.Builder(max3, max4, i9 + 1).create());
                            Canvas canvas3 = startPage2.getCanvas();
                            float f3 = this.expzf;
                            canvas3.scale(f3, f3);
                            this.mycan222p.paint2.setColor(this.mycan222p.CurrentNotes.pages.get(i9).PColor);
                            this.mycan222p.paint.setStrokeCap(Paint.Cap.ROUND);
                            float f4 = this.expzf;
                            canvas3.drawRect(0.0f, 0.0f, ((float) max3) / f4, ((float) max4) / f4, this.mycan222p.paint2);
                            canvas3.drawBitmap(PdfToBitmap, 0.0f, 0.0f, this.mycan222p.paint);
                            for (int i10 = 0; i10 < this.mycan222p.CurrentNotes.pages.get(i9).pathslist.size() - this.mycan222p.CurrentNotes.pages.get(i9).UndoNumber; i10++) {
                                if (this.mycan222p.CurrentNotes.pages.get(i9).pathslist.get(i10).IsVissable == 1) {
                                    this.mycan222p.paint.setStrokeWidth(this.mycan222p.CurrentNotes.pages.get(i9).pathslist.get(i10).sw);
                                    this.mycan222p.paint.setColor(Color.rgb(this.mycan222p.CurrentNotes.pages.get(i9).pathslist.get(i10).c[0], this.mycan222p.CurrentNotes.pages.get(i9).pathslist.get(i10).c[1], this.mycan222p.CurrentNotes.pages.get(i9).pathslist.get(i10).c[2]));
                                    this.mycan222p.CurrentNotes.pages.get(i9).pathslist.get(i10).P.offset(this.mycan222p.CurrentNotes.pages.get(i9).pathslist.get(i10).pathXShift, this.mycan222p.CurrentNotes.pages.get(i9).pathslist.get(i10).pathYShift);
                                    canvas3.drawPath(this.mycan222p.CurrentNotes.pages.get(i9).pathslist.get(i10).P, this.mycan222p.paint);
                                    this.mycan222p.CurrentNotes.pages.get(i9).pathslist.get(i10).P.offset(-this.mycan222p.CurrentNotes.pages.get(i9).pathslist.get(i10).pathXShift, -this.mycan222p.CurrentNotes.pages.get(i9).pathslist.get(i10).pathYShift);
                                }
                            }
                            pdfDocument.finishPage(startPage2);
                        }
                        else {
                            PageLucid pageVar3 = this.mycan222p.CurrentNotes.pages.get(i9);
                            int max5 = (int) Math.max(pageVar3.Minimum_Page_Width + 80.0f, pageVar3.Minimim_Page_Width_To_Fit_drawing);
                            int max6 = (int) Math.max(pageVar3.Minimum_Page_Height + 80.0f, pageVar3.Minimum_Page_Height_To_Fit_drawing);
                            PdfDocument.Page startPage3 = pdfDocument.startPage(new PdfDocument.PageInfo.Builder(max5, max6, i9 + 1).create());
                            Canvas canvas4 = startPage3.getCanvas();
                            this.mycan222p.paint2.setColor(this.mycan222p.CurrentNotes.pages.get(i9).PColor);
                            this.mycan222p.paint.setStrokeCap(Paint.Cap.ROUND);
                            Canvas canvas5 = canvas4;
                            canvas4.drawRect(0.0f, 0.0f, (float) max5, (float) max6, this.mycan222p.paint2);
                            for (int i11 = 0; i11 < this.mycan222p.CurrentNotes.pages.get(i9).pathslist.size() - this.mycan222p.CurrentNotes.pages.get(i9).UndoNumber; i11++) {
                                if (this.mycan222p.CurrentNotes.pages.get(i9).pathslist.get(i11).IsVissable == 1) {
                                    this.mycan222p.paint.setStrokeWidth(this.mycan222p.CurrentNotes.pages.get(i9).pathslist.get(i11).sw);
                                    this.mycan222p.paint.setColor(Color.rgb(this.mycan222p.CurrentNotes.pages.get(i9).pathslist.get(i11).c[0], this.mycan222p.CurrentNotes.pages.get(i9).pathslist.get(i11).c[1], this.mycan222p.CurrentNotes.pages.get(i9).pathslist.get(i11).c[2]));
                                    this.mycan222p.CurrentNotes.pages.get(i9).pathslist.get(i11).P.offset(this.mycan222p.CurrentNotes.pages.get(i9).pathslist.get(i11).pathXShift, this.mycan222p.CurrentNotes.pages.get(i9).pathslist.get(i11).pathYShift);
                                    canvas5 = canvas5;
                                    canvas5.drawPath(this.mycan222p.CurrentNotes.pages.get(i9).pathslist.get(i11).P, this.mycan222p.paint);
                                    this.mycan222p.CurrentNotes.pages.get(i9).pathslist.get(i11).P.offset(-this.mycan222p.CurrentNotes.pages.get(i9).pathslist.get(i11).pathXShift, -this.mycan222p.CurrentNotes.pages.get(i9).pathslist.get(i11).pathYShift);
                                } else {
                                    canvas5 = canvas5;
                                }
                            }
                            pdfDocument.finishPage(startPage3);
                        }
                    }
                }
                pdfRenderer.close();
            }
            else {
                while (i12 < this.mycan222p.CurrentNotes.pages.size()) {
                    if (this.mycan222p.CurrentNotes.pages.get(i12).pathslist.size() > 0 || i12==mycan222p.CPgNumber) {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                progressBar.setProgress(i12);
                            }
                        });
                        PageLucid pageVar4 = this.mycan222p.CurrentNotes.pages.get(i12);
                        int max7 = (int) (Math.max(pageVar4.Minimum_Page_Width, pageVar4.Minimim_Page_Width_To_Fit_drawing) * this.expzf);
                        int max8 = (int) (Math.max(pageVar4.Minimum_Page_Height, pageVar4.Minimum_Page_Height_To_Fit_drawing) * this.expzf);
                        int i13 = i12 + 1;
                        PdfDocument.Page startPage4 = pdfDocument.startPage(new PdfDocument.PageInfo.Builder(max7, max8, i13).create());
                        Canvas canvas6 = startPage4.getCanvas();
                        float f5 = this.expzf;
                        canvas6.scale(f5, f5);
                        this.mycan222p.paint2.setColor(this.mycan222p.CurrentNotes.pages.get(i12).PColor);
                        this.mycan222p.paint.setStrokeCap(Paint.Cap.ROUND);
                        float f6 = this.expzf;
                        Canvas canvas7 = canvas6;
                        canvas6.drawRect(0.0f, 0.0f, ((float) max7) / f6, ((float) max8) / f6, this.mycan222p.paint2);
                        for (int i14 = 0; i14 < this.mycan222p.CurrentNotes.pages.get(i12).pathslist.size() - this.mycan222p.CurrentNotes.pages.get(i12).UndoNumber; i14++) {
                            if (this.mycan222p.CurrentNotes.pages.get(i12).pathslist.get(i14).IsVissable == 1) {
                                this.mycan222p.paint.setStrokeWidth(this.mycan222p.CurrentNotes.pages.get(i12).pathslist.get(i14).sw);
                                this.mycan222p.paint.setColor(Color.rgb(this.mycan222p.CurrentNotes.pages.get(i12).pathslist.get(i14).c[0], this.mycan222p.CurrentNotes.pages.get(i12).pathslist.get(i14).c[1], this.mycan222p.CurrentNotes.pages.get(i12).pathslist.get(i14).c[2]));
                                this.mycan222p.CurrentNotes.pages.get(i12).pathslist.get(i14).P.offset(this.mycan222p.CurrentNotes.pages.get(i12).pathslist.get(i14).pathXShift, this.mycan222p.CurrentNotes.pages.get(i12).pathslist.get(i14).pathYShift);
                                canvas7 = canvas7;
                                canvas7.drawPath(this.mycan222p.CurrentNotes.pages.get(i12).pathslist.get(i14).P, this.mycan222p.paint);
                                this.mycan222p.CurrentNotes.pages.get(i12).pathslist.get(i14).P.offset(-this.mycan222p.CurrentNotes.pages.get(i12).pathslist.get(i14).pathXShift, -this.mycan222p.CurrentNotes.pages.get(i12).pathslist.get(i14).pathYShift);
                            } else {
                                canvas7 = canvas7;
                            }
                        }
                        pdfDocument.finishPage(startPage4);
                    }
                    i12 += 1;
                }
            }
            handler.post(new Runnable() {
                @Override
                public void run() {
                    progressBar.setVisibility(View.INVISIBLE);
                }
            });
            try {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),"Saving Locally first,processing",Toast.LENGTH_SHORT).show();
                    }
                });
                FileOutputStream fileOutputStream2=new FileOutputStream(new File(getExternalFilesDir("/"), this.mycan222p.CurrentNotes.name + ".pdf"));
                //Log.d("testkk",pdfDocument.);

                 
                pdfDocument.writeTo(fileOutputStream2);
                Log.d("iii", String.valueOf(getExternalFilesDir("/")));
            } catch (IOException e) {
                e.printStackTrace();
            }
            pdfDocument.close();
            Intent intent2 = new Intent("android.intent.action.SEND");
            intent2.setType("application/pdf");
            intent2.putExtra("android.intent.extra.STREAM", FileProvider.getUriForFile(getApplicationContext(), "com.kamal.lucid.provider", new File(getExternalFilesDir("/"), this.mycan222p.CurrentNotes.name + ".pdf")));
            //this.Eb1.setText("Export");
            startActivity(Intent.createChooser(intent2, "Export"));
            //this.delnow = 0;
        }
    }

    class ExportThreadLucidPdfOnlySelectedPages implements Runnable{
        Mycanvas1 mycan222p;
        float expzf=1f;
        int i4=0,i92=0,i12=0;
        ArrayList<Integer> selectedPagesToexport;
        ExportThreadLucidPdfOnlySelectedPages(Mycanvas1 mycanvas1,float expzf1,ArrayList<Integer> selectedPagesToexport2){
            mycan222p=mycanvas1;
            this.expzf=expzf1;
            selectedPagesToexport=selectedPagesToexport2;
        }
        @Override
        public void run() {
            int i = 1;
            if (this.mycan222p.imagemode) {
                Bitmap bitmap = null;
                int i2 = 0;
                while (i2 < this.mycan222p.CurrentNotes.pages.size()) {
                    PageLucid pageVar = this.mycan222p.CurrentNotes.pages.get(i2);
                    int max = ((int) Math.max(pageVar.Minimum_Page_Width, pageVar.Minimim_Page_Width_To_Fit_drawing)) + 80;
                    int max2 = ((int) Math.max(pageVar.Minimum_Page_Height, pageVar.Minimum_Page_Height_To_Fit_drawing)) + 80;
                    Bitmap createBitmap = Bitmap.createBitmap(max, max2, Bitmap.Config.ARGB_8888);
                    Canvas canvas = new Canvas();
                    canvas.setBitmap(createBitmap);
                    this.mycan222p.paint2.setColor(this.mycan222p.CurrentNotes.pages.get(i2).PColor);
                    this.mycan222p.paint.setStrokeCap(Paint.Cap.ROUND);
                    canvas.drawRect(0.0f, 0.0f, (float) max, (float) max2, this.mycan222p.paint2);
                    canvas.drawBitmap(this.mycan222p.OnDrawCurrentBitmap, 30.0f, 50.0f, this.mycan222p.paint);
                    for (int i3 = 0; i3 < this.mycan222p.CurrentNotes.pages.get(i2).pathslist.size() - this.mycan222p.CurrentNotes.pages.get(i2).UndoNumber; i3++) {
                        if (this.mycan222p.CurrentNotes.pages.get(i2).pathslist.get(i3).IsVissable == 1) {
                            this.mycan222p.paint.setStrokeWidth(this.mycan222p.CurrentNotes.pages.get(i2).pathslist.get(i3).sw);
                            this.mycan222p.paint.setColor(Color.rgb(this.mycan222p.CurrentNotes.pages.get(i2).pathslist.get(i3).c[0], this.mycan222p.CurrentNotes.pages.get(i2).pathslist.get(i3).c[1], this.mycan222p.CurrentNotes.pages.get(i2).pathslist.get(i3).c[2]));
                            this.mycan222p.CurrentNotes.pages.get(i2).pathslist.get(i3).P.offset(this.mycan222p.CurrentNotes.pages.get(i2).pathslist.get(i3).pathXShift, this.mycan222p.CurrentNotes.pages.get(i2).pathslist.get(i3).pathYShift);
                            canvas.drawPath(this.mycan222p.CurrentNotes.pages.get(i2).pathslist.get(i3).P, this.mycan222p.paint);
                            this.mycan222p.CurrentNotes.pages.get(i2).pathslist.get(i3).P.offset(-this.mycan222p.CurrentNotes.pages.get(i2).pathslist.get(i3).pathXShift, -this.mycan222p.CurrentNotes.pages.get(i2).pathslist.get(i3).pathYShift);
                        }
                    }
                    i2++;
                    bitmap = createBitmap;
                }
                saveBitmap("Lucid_OnDraw", bitmap,true);
                Intent intent = new Intent("android.intent.action.SEND");
                intent.setType("image/png");
                intent.putExtra("android.intent.extra.STREAM", FileProvider.getUriForFile(getApplicationContext(), "com.kamal.lucid.provider", new File(getExternalFilesDir("/"), "Lucid_OnDraw.jpeg")));
                startActivity(Intent.createChooser(intent, "Export"));
                return;
            }
            else{
                mycan222p.CurrentNotes.exportNumber+=1;
            }
            //this.Eb1.setText("Exporting");
            PdfDocument pdfDocument = new PdfDocument();
            handler.post(new Runnable() {
                @Override
                public void run() {
                    progressBar.setVisibility(View.VISIBLE);
                    progressBar.setMax(mycan222p.CurrentNotes.pages.size());
                }
            });

            if (this.mycan222p.CurrentNotes.Maximum_Allowed_Page_Height <= 100000) {
                while (i4 < this.mycan222p.CurrentNotes.pages.size()) {
                    if (selectedPagesToexport.contains(i4)) {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                progressBar.setProgress(i4);
                            }
                        });
                        int i5 = (int) (((float) this.mycan222p.CurrentNotes.Maximum_Allowed_Page_Width) * this.expzf);
                        int i6 = ((int) (((float) this.mycan222p.CurrentNotes.Maximum_Allowed_Page_Height) * this.expzf)) + 30;
                        int i7 = i4 + 1;
                        PdfDocument.Page startPage = pdfDocument.startPage(new PdfDocument.PageInfo.Builder(i5, i6, i7).create());
                        Canvas canvas2 = startPage.getCanvas();
                        float f = this.expzf;
                        canvas2.scale(f, f);
                        this.mycan222p.paint2.setColor(this.mycan222p.CurrentNotes.pages.get(i4).PColor);
                        this.mycan222p.paint.setStrokeCap(Paint.Cap.ROUND);
                        float f2 = this.expzf;
                        canvas2.drawRect(0.0f, 0.0f, ((float) i5) / f2, ((float) i6) / f2, this.mycan222p.paint2);
                        int i8 = 0;
                        while (i8 < this.mycan222p.CurrentNotes.pages.get(i4).pathslist.size() - this.mycan222p.CurrentNotes.pages.get(i4).UndoNumber) {
                            if (this.mycan222p.CurrentNotes.pages.get(i4).pathslist.get(i8).IsVissable == i) {
                                this.mycan222p.paint.setStrokeWidth(this.mycan222p.CurrentNotes.pages.get(i4).pathslist.get(i8).sw);
                                this.mycan222p.paint.setColor(Color.rgb(this.mycan222p.CurrentNotes.pages.get(i4).pathslist.get(i8).c[0], this.mycan222p.CurrentNotes.pages.get(i4).pathslist.get(i8).c[1], this.mycan222p.CurrentNotes.pages.get(i4).pathslist.get(i8).c[2]));
                                this.mycan222p.CurrentNotes.pages.get(i4).pathslist.get(i8).P.offset(this.mycan222p.CurrentNotes.pages.get(i4).pathslist.get(i8).pathXShift, this.mycan222p.CurrentNotes.pages.get(i4).pathslist.get(i8).pathYShift);
                                canvas2.drawPath(this.mycan222p.CurrentNotes.pages.get(i4).pathslist.get(i8).P, this.mycan222p.paint);
                                this.mycan222p.CurrentNotes.pages.get(i4).pathslist.get(i8).P.offset(-this.mycan222p.CurrentNotes.pages.get(i4).pathslist.get(i8).pathXShift, -this.mycan222p.CurrentNotes.pages.get(i4).pathslist.get(i8).pathYShift);
                            }
                            i8++;
                            i = 1;
                        }
                        pdfDocument.finishPage(startPage);
                        i = 1;
                    }
                    i4+=1;
                }
            }
            else if (this.mycan222p.CurrentNotes.APdfNote) {
                Bitmap PdfToBitmap = null;
                PdfRenderer pdfRenderer = null;
                try {
                    pdfRenderer = new PdfRenderer(ParcelFileDescriptor.open(this.mycan222p.CurrentHandellingPdf, 268435456));

                } catch (IOException e) {
                    e.printStackTrace();
                }

                for (int i9 = 0; i9 < this.mycan222p.CurrentNotes.pages.size(); i9++) {
                    i92=i9;
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            progressBar.setProgress(i92);
                        }
                    });
                    if (selectedPagesToexport.contains(i9)) {
                        if (i9 < this.mycan222p.total_pages_pdf) {

                            //-----------
                            PdfRenderer.Page openPage = pdfRenderer.openPage(i9);
                            Bitmap createBitmap2 = Bitmap.createBitmap((getResources().getDisplayMetrics().densityDpi / 72) * openPage.getWidth(), (getResources().getDisplayMetrics().densityDpi / 72) * openPage.getHeight(), Bitmap.Config.ARGB_8888);
                            openPage.render(createBitmap2, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY);
                            try {
                                openPage.close();
                                PdfToBitmap = createBitmap2;
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            //-------------

                            new PageLucid(1);
                            PageLucid pageVar2 = this.mycan222p.CurrentNotes.pages.get(i9);
                            int max3 = (int) (Math.max(Math.max(pageVar2.Minimum_Page_Width, mycan222p.PdfFcx), pageVar2.Minimim_Page_Width_To_Fit_drawing) * this.expzf);
                            int max4 = (int) (Math.max(Math.max(pageVar2.Minimum_Page_Height, mycan222p.PdfFcy), pageVar2.Minimum_Page_Height_To_Fit_drawing) * this.expzf);
                            if(mycan222p.CurrentNotes.pdfFixedBoundaries){
                                max3= (int) (createBitmap2.getWidth()*this.expzf);
                                max4= (int) (createBitmap2.getHeight()*this.expzf);
                            }
                            PdfDocument.Page startPage2 = pdfDocument.startPage(new PdfDocument.PageInfo.Builder(max3, max4, i9 + 1).create());
                            Canvas canvas3 = startPage2.getCanvas();
                            float f3 = this.expzf;
                            canvas3.scale(f3, f3);
                            this.mycan222p.paint2.setColor(this.mycan222p.CurrentNotes.pages.get(i9).PColor);
                            this.mycan222p.paint.setStrokeCap(Paint.Cap.ROUND);
                            float f4 = this.expzf;
                            canvas3.drawRect(0.0f, 0.0f, ((float) max3) / f4, ((float) max4) / f4, this.mycan222p.paint2);
                            canvas3.drawBitmap(PdfToBitmap, 0.0f, 0.0f, this.mycan222p.paint);
                            for (int i10 = 0; i10 < this.mycan222p.CurrentNotes.pages.get(i9).pathslist.size() - this.mycan222p.CurrentNotes.pages.get(i9).UndoNumber; i10++) {
                                if (this.mycan222p.CurrentNotes.pages.get(i9).pathslist.get(i10).IsVissable == 1) {
                                    this.mycan222p.paint.setStrokeWidth(this.mycan222p.CurrentNotes.pages.get(i9).pathslist.get(i10).sw);
                                    this.mycan222p.paint.setColor(Color.rgb(this.mycan222p.CurrentNotes.pages.get(i9).pathslist.get(i10).c[0], this.mycan222p.CurrentNotes.pages.get(i9).pathslist.get(i10).c[1], this.mycan222p.CurrentNotes.pages.get(i9).pathslist.get(i10).c[2]));
                                    this.mycan222p.CurrentNotes.pages.get(i9).pathslist.get(i10).P.offset(this.mycan222p.CurrentNotes.pages.get(i9).pathslist.get(i10).pathXShift, this.mycan222p.CurrentNotes.pages.get(i9).pathslist.get(i10).pathYShift);
                                    canvas3.drawPath(this.mycan222p.CurrentNotes.pages.get(i9).pathslist.get(i10).P, this.mycan222p.paint);
                                    this.mycan222p.CurrentNotes.pages.get(i9).pathslist.get(i10).P.offset(-this.mycan222p.CurrentNotes.pages.get(i9).pathslist.get(i10).pathXShift, -this.mycan222p.CurrentNotes.pages.get(i9).pathslist.get(i10).pathYShift);
                                }
                            }
                            pdfDocument.finishPage(startPage2);
                        }
                        else {
                            PageLucid pageVar3 = this.mycan222p.CurrentNotes.pages.get(i9);
                            int max5 = (int) Math.max(pageVar3.Minimum_Page_Width + 80.0f, pageVar3.Minimim_Page_Width_To_Fit_drawing);
                            int max6 = (int) Math.max(pageVar3.Minimum_Page_Height + 80.0f, pageVar3.Minimum_Page_Height_To_Fit_drawing);
                            PdfDocument.Page startPage3 = pdfDocument.startPage(new PdfDocument.PageInfo.Builder(max5, max6, i9 + 1).create());
                            Canvas canvas4 = startPage3.getCanvas();
                            this.mycan222p.paint2.setColor(this.mycan222p.CurrentNotes.pages.get(i9).PColor);
                            this.mycan222p.paint.setStrokeCap(Paint.Cap.ROUND);
                            Canvas canvas5 = canvas4;
                            canvas4.drawRect(0.0f, 0.0f, (float) max5, (float) max6, this.mycan222p.paint2);
                            for (int i11 = 0; i11 < this.mycan222p.CurrentNotes.pages.get(i9).pathslist.size() - this.mycan222p.CurrentNotes.pages.get(i9).UndoNumber; i11++) {
                                if (this.mycan222p.CurrentNotes.pages.get(i9).pathslist.get(i11).IsVissable == 1) {
                                    this.mycan222p.paint.setStrokeWidth(this.mycan222p.CurrentNotes.pages.get(i9).pathslist.get(i11).sw);
                                    this.mycan222p.paint.setColor(Color.rgb(this.mycan222p.CurrentNotes.pages.get(i9).pathslist.get(i11).c[0], this.mycan222p.CurrentNotes.pages.get(i9).pathslist.get(i11).c[1], this.mycan222p.CurrentNotes.pages.get(i9).pathslist.get(i11).c[2]));
                                    this.mycan222p.CurrentNotes.pages.get(i9).pathslist.get(i11).P.offset(this.mycan222p.CurrentNotes.pages.get(i9).pathslist.get(i11).pathXShift, this.mycan222p.CurrentNotes.pages.get(i9).pathslist.get(i11).pathYShift);
                                    canvas5 = canvas5;
                                    canvas5.drawPath(this.mycan222p.CurrentNotes.pages.get(i9).pathslist.get(i11).P, this.mycan222p.paint);
                                    this.mycan222p.CurrentNotes.pages.get(i9).pathslist.get(i11).P.offset(-this.mycan222p.CurrentNotes.pages.get(i9).pathslist.get(i11).pathXShift, -this.mycan222p.CurrentNotes.pages.get(i9).pathslist.get(i11).pathYShift);
                                } else {
                                    canvas5 = canvas5;
                                }
                            }
                            pdfDocument.finishPage(startPage3);
                        }
                    }
                }
                pdfRenderer.close();
            }
            else {
                while (i12 < this.mycan222p.CurrentNotes.pages.size()) {
                    if (selectedPagesToexport.contains(i12)) {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                progressBar.setProgress(i12);
                            }
                        });
                        PageLucid pageVar4 = this.mycan222p.CurrentNotes.pages.get(i12);
                        int max7 = (int) (Math.max(pageVar4.Minimum_Page_Width, pageVar4.Minimim_Page_Width_To_Fit_drawing) * this.expzf);
                        int max8 = (int) (Math.max(pageVar4.Minimum_Page_Height, pageVar4.Minimum_Page_Height_To_Fit_drawing) * this.expzf);
                        int i13 = i12 + 1;
                        PdfDocument.Page startPage4 = pdfDocument.startPage(new PdfDocument.PageInfo.Builder(max7, max8, i13).create());
                        Canvas canvas6 = startPage4.getCanvas();
                        float f5 = this.expzf;
                        canvas6.scale(f5, f5);
                        this.mycan222p.paint2.setColor(this.mycan222p.CurrentNotes.pages.get(i12).PColor);
                        this.mycan222p.paint.setStrokeCap(Paint.Cap.ROUND);
                        float f6 = this.expzf;
                        Canvas canvas7 = canvas6;
                        canvas6.drawRect(0.0f, 0.0f, ((float) max7) / f6, ((float) max8) / f6, this.mycan222p.paint2);
                        for (int i14 = 0; i14 < this.mycan222p.CurrentNotes.pages.get(i12).pathslist.size() - this.mycan222p.CurrentNotes.pages.get(i12).UndoNumber; i14++) {
                            if (this.mycan222p.CurrentNotes.pages.get(i12).pathslist.get(i14).IsVissable == 1) {
                                this.mycan222p.paint.setStrokeWidth(this.mycan222p.CurrentNotes.pages.get(i12).pathslist.get(i14).sw);
                                this.mycan222p.paint.setColor(Color.rgb(this.mycan222p.CurrentNotes.pages.get(i12).pathslist.get(i14).c[0], this.mycan222p.CurrentNotes.pages.get(i12).pathslist.get(i14).c[1], this.mycan222p.CurrentNotes.pages.get(i12).pathslist.get(i14).c[2]));
                                this.mycan222p.CurrentNotes.pages.get(i12).pathslist.get(i14).P.offset(this.mycan222p.CurrentNotes.pages.get(i12).pathslist.get(i14).pathXShift, this.mycan222p.CurrentNotes.pages.get(i12).pathslist.get(i14).pathYShift);
                                canvas7 = canvas7;
                                canvas7.drawPath(this.mycan222p.CurrentNotes.pages.get(i12).pathslist.get(i14).P, this.mycan222p.paint);
                                this.mycan222p.CurrentNotes.pages.get(i12).pathslist.get(i14).P.offset(-this.mycan222p.CurrentNotes.pages.get(i12).pathslist.get(i14).pathXShift, -this.mycan222p.CurrentNotes.pages.get(i12).pathslist.get(i14).pathYShift);
                            } else {
                                canvas7 = canvas7;
                            }
                        }
                        pdfDocument.finishPage(startPage4);
                    }
                    i12 += 1;
                }
            }
            handler.post(new Runnable() {
                @Override
                public void run() {
                    progressBar.setVisibility(View.INVISIBLE);
                }
            });
            try {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),"Saving Locally first,processing",Toast.LENGTH_SHORT).show();
                    }
                });
                FileOutputStream fileOutputStream2=new FileOutputStream(new File(getExternalFilesDir("/"), this.mycan222p.CurrentNotes.name + ".pdf"));
                //Log.d("testkk",pdfDocument.);

                 
                pdfDocument.writeTo(fileOutputStream2);
                Log.d("iii", String.valueOf(getExternalFilesDir("/")));
            } catch (IOException e) {
                e.printStackTrace();
            }
            pdfDocument.close();
            Intent intent2 = new Intent("android.intent.action.SEND");
            intent2.setType("application/pdf");
            intent2.putExtra("android.intent.extra.STREAM", FileProvider.getUriForFile(getApplicationContext(), "com.kamal.lucid.provider", new File(getExternalFilesDir("/"), this.mycan222p.CurrentNotes.name + ".pdf")));
            //this.Eb1.setText("Export");
            startActivity(Intent.createChooser(intent2, "Export"));
            //this.delnow = 0;
        }
    }

    public void setBackground(View view) {
        new AmbilWarnaDialog(this, this.mycanv.CurrentNotes.pages.get(this.mycanv.CPgNumber).PColor, new AmbilWarnaDialog.OnAmbilWarnaListener() { // from class: com.kamal.lucid.drawingpart.8
            @Override // yuku.ambilwarna.AmbilWarnaDialog.OnAmbilWarnaListener
            public void onCancel(AmbilWarnaDialog ambilWarnaDialog) {
            }

            @Override // yuku.ambilwarna.AmbilWarnaDialog.OnAmbilWarnaListener
            public void onOk(AmbilWarnaDialog ambilWarnaDialog, int i) {
                drawingpart.this.mycanv.CurrentNotes.pages.get(drawingpart.this.mycanv.CPgNumber).PColor = i;
                if (Color.red(i) == Color.blue(i) && Color.green(i) == Color.blue(i) && Color.red(i) <= 122) {
                    drawingpart.this.mycanv.pen1.color[0] = 255;
                    drawingpart.this.mycanv.pen1.color[1] = 255;
                    drawingpart.this.mycanv.pen1.color[2] = 255;
                    drawingpart.this.mycanv.pen1.color[3] = 255;
                    drawingpart.this.mycanv.pen1.color[4] = 239;
                    drawingpart.this.mycanv.pen1.color[5] = 0;
                    drawingpart.this.mycanv.pen1.color[6] = 0;
                    drawingpart.this.mycanv.pen1.color[7] = 247;
                    drawingpart.this.mycanv.pen1.color[8] = 255;
                    drawingpart drawingpartVar = drawingpart.this;
                    drawingpartVar.m2(drawingpartVar.mycanv);
                    drawingpart drawingpartVar2 = drawingpart.this;
                    drawingpartVar2.m2(drawingpartVar2.mycanv);
                    drawingpart drawingpartVar3 = drawingpart.this;
                    drawingpartVar3.m2(drawingpartVar3.mycanv);
                }
                if (Color.red(i) == Color.blue(i) && Color.green(i) == Color.blue(i) && Color.red(i) > 122) {
                    drawingpart.this.mycanv.pen1.color[0] = 0;
                    drawingpart.this.mycanv.pen1.color[1] = 0;
                    drawingpart.this.mycanv.pen1.color[2] = 0;
                    drawingpart.this.mycanv.pen1.color[3] = 255;
                    drawingpart.this.mycanv.pen1.color[4] = 0;
                    drawingpart.this.mycanv.pen1.color[5] = 0;
                    drawingpart.this.mycanv.pen1.color[6] = 0;
                    drawingpart.this.mycanv.pen1.color[7] = 0;
                    drawingpart.this.mycanv.pen1.color[8] = 255;
                    drawingpart drawingpartVar4 = drawingpart.this;
                    drawingpartVar4.m2(drawingpartVar4.mycanv);
                    drawingpart drawingpartVar5 = drawingpart.this;
                    drawingpartVar5.m2(drawingpartVar5.mycanv);
                    drawingpart drawingpartVar6 = drawingpart.this;
                    drawingpartVar6.m2(drawingpartVar6.mycanv);
                }
                drawingpart.this.mycanv.invalidate();
            }
        }).show();
    }

    public void newpencolor(View view) {
        new AmbilWarnaDialog(this, Color.rgb(this.mycanv.pen1.color[0], this.mycanv.pen1.color[1], this.mycanv.pen1.color[2]), new AmbilWarnaDialog.OnAmbilWarnaListener() { // from class: com.kamal.lucid.drawingpart.9
            @Override // yuku.ambilwarna.AmbilWarnaDialog.OnAmbilWarnaListener
            public void onCancel(AmbilWarnaDialog ambilWarnaDialog) {
            }

            @Override // yuku.ambilwarna.AmbilWarnaDialog.OnAmbilWarnaListener
            public void onOk(AmbilWarnaDialog ambilWarnaDialog, int i) {
                drawingpart.this.mycanv.pen1.color[0] = Color.red(i);
                drawingpart.this.mycanv.pen1.color[1] = Color.green(i);
                drawingpart.this.mycanv.pen1.color[2] = Color.blue(i);
                drawingpart drawingpartVar = drawingpart.this;
                drawingpartVar.m2(drawingpartVar.mycanv);
                drawingpart drawingpartVar2 = drawingpart.this;
                drawingpartVar2.m2(drawingpartVar2.mycanv);
                drawingpart drawingpartVar3 = drawingpart.this;
                drawingpartVar3.m2(drawingpartVar3.mycanv);
            }
        }).show();
    }

    @Override // androidx.activity.ComponentActivity, android.app.Activity
    public void onBackPressed() {
        tmer.cancel();
        tmer.purge();
        drawingpart.this.mycanv.saveit();
        drawingpart.this.mycanv.isSavingNow = false;
        finish();
    }

    public void reactivatePen() {
        this.m4.setBackgroundColor(Color.rgb(220, 220, 220));
    }

    public void saveBitmap(String str, Bitmap bitmap) {
        File file = new File(getExternalFilesDir("/"), str + ".png");
        try {
            file.createNewFile();
        } catch (IOException unused) {
        }
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);
        try {
            fileOutputStream.flush();
        } catch (IOException e2) {
            e2.printStackTrace();
        }
        try {
            fileOutputStream.close();
        } catch (IOException e3) {
            e3.printStackTrace();
        }
    }

    public void saveBitmap(String str, Bitmap bitmap,boolean is_jpeg) {
        if(is_jpeg) {
            File file = new File(getExternalFilesDir("/"), str + ".jpeg");
            try {
                file.createNewFile();
            } catch (IOException unused) {
            }
            FileOutputStream fileOutputStream = null;
            try {
                fileOutputStream = new FileOutputStream(file);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);
            try {
                fileOutputStream.flush();
            } catch (IOException e2) {
                e2.printStackTrace();
            }
            try {
                fileOutputStream.close();
            } catch (IOException e3) {
                e3.printStackTrace();
            }
        }
    }

    public void OpenMoveToDialog(){
        if(!mycanv.imagemode) {
            dialogBuilder = new AlertDialog.Builder(this);
            View inflate = getLayoutInflater().inflate(R.layout.move_to_dialog, null);

            GridView gridview=inflate.findViewById(R.id.Pages_list);
            gridview.setAdapter(new PageNumberDisplayGridAdapter(this,Math.max(mycanv.CurrentNotes.pages.size(),mycanv.total_pages_pdf),mycanv.CPgNumber,mycanv.CurrentNotes.Importantpages,movedFromPageIndex));
            gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                    movedFromPageIndex=mycanv.CPgNumber;
                    mycanv.MoveToPage(i);
                    displaypagecount(mycanv);
                    dialog.dismiss();
                }
            });
            gridview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                    if(!mycanv.CurrentNotes.Importantpages.contains(i)){
                        mycanv.CurrentNotes.Importantpages.add(i);
                        view.findViewById(R.id.layoutpagenumber).setBackgroundResource(R.drawable.custombuttontheme3);
                    }
                    else{
                        mycanv.CurrentNotes.Importantpages.remove(mycanv.CurrentNotes.Importantpages.indexOf(i));
                        view.findViewById(R.id.layoutpagenumber).setBackgroundResource(R.drawable.custombuttontheme1);
                    }
                    return true;
                }
            });

            Button moveToBtn = (Button) inflate.findViewById(R.id.moveToPageBtn);
            EditText edttxt = (EditText) inflate.findViewById(R.id.editTextNumber);

                ((EditText) inflate.findViewById(R.id.editTextNumber)).setHint("( 1,...," + Math.max(mycanv.total_pages_pdf,mycanv.CurrentNotes.pages.size()) + " )");

            dialogBuilder.setView(inflate);
            dialog = dialogBuilder.create();
            dialog.show();
            moveToBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int number = -16727367;
                    try {
                        number = Integer.parseInt((edttxt).getText().toString());
                    } catch (NumberFormatException e) {
                        Toast.makeText(getApplicationContext(), "Please input a vallied number", Toast.LENGTH_SHORT).show();
                    }
                    if (number != -16727367) {
                        if (number >= 1 && number <= Math.max(mycanv.total_pages_pdf,mycanv.CurrentNotes.pages.size())) {
                            movedFromPageIndex=mycanv.CPgNumber;
                            mycanv.MoveToPage(number - 1);
                        }
                        else{
                            Toast.makeText(getApplicationContext(),"Out of range",Toast.LENGTH_SHORT).show();
                        }
                        displaypagecount(v2);
                    }
                    dialog.dismiss();
                }
            });
        }
    }

    public void RemoveAllAdditionalViews(){
        layoutP.removeView(option_menu_export);
    }
    public void ToggleAdditionalView(View vtemp){
        if(layoutP.indexOfChild(vtemp)==-1) {
            RemoveAllAdditionalViews();
            layoutP.removeView(vtemp);
            layoutP.addView(vtemp);
            vtemp.bringToFront();
        }
        else{
            layoutP.removeView(vtemp);
        }
    }

    public void showMoveToNumberGrid(){

    }

    public int mmToPix(int i){
        return (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_MM, i, getResources().getDisplayMetrics());
    }

    //-----------------------------
    public ANote getNoteWithStrokes(){
        if(drawingPartViewModel.CurrentNotes==null) {
            CurrentNotesDrawingPart = NoteLinkTONoteNoCheck(CurrentNoteId + ".txt");
            createpathsfromstrokes(CurrentNotesDrawingPart);
            CurrentNotesDrawingPart.name=nameOfNote;
        }
        else{
            CurrentNotesDrawingPart=drawingPartViewModel.CurrentNotes;
        }
        return CurrentNotesDrawingPart;
    }


    public ANote NoteLinkTONoteNoCheck(String str) {
        ImportedNotes=new ANote();
        String readFromFile = readFromFile(str, getApplicationContext());
        String sb2 = readFromFile.substring(16);//sb.toString();
        Gson gson = new Gson();
        Type type = new TypeToken<ANote>() {
        }.getType();
        if (sb2 == null) {
            Toast.makeText(getApplicationContext(), "It look like the note is corrupted", Toast.LENGTH_SHORT).show();
        }
        else {
            this.ImportedNotes = (ANote) gson.fromJson(sb2, type);
        }

        return this.ImportedNotes;
    }
    private String readFromFile(String str, Context context) {
        try {
            FileInputStream openFileInput = context.openFileInput(str);
            if (openFileInput == null) {
                return "";
            }
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(openFileInput));
            StringBuilder sb = new StringBuilder();
            while (true) {
                String readLine = bufferedReader.readLine();
                if (readLine != null) {
                    sb.append(readLine).append("\n");
                }
                else {
                    openFileInput.close();
                    return sb.toString();
                }
            }
        } catch (FileNotFoundException e) {
            Log.e("login activity", "File not found: " + e.toString());
            return "";
        } catch (IOException e2) {
            Log.e("login activity", "Can not read file: " + e2.toString());
            return "";
        }
    }

    public void createpathsfromstrokes(ANote aNote) {
        for (PageLucid pg: aNote.pages) {
            for (strokes stk:pg.pathsCstrokes) {
                Path path = new Path();
                path.moveTo(stk.pointsl.get(0).x, stk.pointsl.get(0).y);
                for (Pnts pnts:stk.pointsl) {
                    path.lineTo(pnts.x, pnts.y);
                }
               pg.pathslist.get(pg.pathsCstrokes.indexOf(stk)).P = path;
            }
        }
    }

    public void savethisnote(ANote aNote,int opennednote) {
        drawingpart.SaveNoteFromNotefile saveNoteFromNotefile=new drawingpart.SaveNoteFromNotefile(aNote);
        new Thread(saveNoteFromNotefile).start();
        SaveThumbOfNote(aNote,opennednote);
    }

    public void savethisnote(ANote aNote,int opennednote,Bitmap btmap) {
        drawingpart.SaveNoteFromNotefile saveNoteFromNotefile=new drawingpart.SaveNoteFromNotefile(aNote);
        new Thread(saveNoteFromNotefile).start();
        //writeToFile("LucidExportsNote" + new Gson().toJson(aNote), aNote.Id + ".txt", getApplicationContext());
        SaveThumbOfNote(aNote,opennednote,btmap);
    }

    public void savethisnote(ANote aNote,int opennednote,Bitmap btmap,int i) {
        drawingpart.SaveNoteFromNotefileNoNotification saveNoteFromNotefileNoNotification=new drawingpart.SaveNoteFromNotefileNoNotification(aNote);
        new Thread(saveNoteFromNotefileNoNotification).start();
        //writeToFile("LucidExportsNote" + new Gson().toJson(aNote), aNote.Id + ".txt", getApplicationContext());
        SaveThumbOfNote(aNote,opennednote,btmap);
    }

    class SaveNoteFromNotefile implements Runnable{
        ANote notett;
        SaveNoteFromNotefile(ANote notet){
            notett=notet;
        }
        @Override
        public void run() {
            try {
                writeToFile("LucidExportsNote" + new Gson().toJson(notett), notett.Id + ".txt", getApplicationContext());
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                            Toast.makeText(getApplicationContext(), "All Note Saved", Toast.LENGTH_SHORT).show();
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }

            try{
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        MainActivity.getmInstanceActivity().showfolders();
                    }
                });
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    class SaveNoteFromNotefileNoNotification implements Runnable{
        ANote notett;
        SaveNoteFromNotefileNoNotification(ANote notet){
            notett=notet;
        }
        @Override
        public void run() {
            try {
                writeToFile("LucidExportsNote" + new Gson().toJson(notett), notett.Id + ".txt", getApplicationContext());
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                            should_save_now = true;
                            //Toast.makeText(getApplicationContext(),"kkkkkkkkk",Toast.LENGTH_SHORT).show();


                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void writeToFile(String str, String str2, Context context) {
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput(str2, 0));
            outputStreamWriter.write(str);
            outputStreamWriter.close();
        } catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }

    public void SaveThumbOfNote(ANote aNote,int opennednote) {
        //0 means note is new
        // 1 means note is already openned
        //2 means dont care
        if(opennednote==0) {
            Display defaultDisplay = getWindowManager().getDefaultDisplay();
            Point point = new Point();
            defaultDisplay.getSize(point);
            Bitmap createBitmap = Bitmap.createBitmap((Math.max(point.x, point.y) * this.bitmapsizew) / 500, (Math.max(point.x, point.y) * this.bitmapsizeh) / 500, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas();
            canvas.setBitmap(createBitmap);
            canvas.scale(0.7f, 0.7f);
            Paint paint = new Paint();
            Paint paint2 = new Paint();
            paint.setAntiAlias(true);
            paint.setStrokeJoin(Paint.Join.ROUND);
            paint.setStyle(Paint.Style.STROKE);
            paint2.setColor(aNote.pages.get(aNote.lastseenpage).PColor);
//            if (aNote.pages.get(aNote.lastseenpage).PColor == Color.rgb(255, 255, 255)) {
//                paint2.setColor(Color.rgb(225, 225, 220));
//            }
            createpathsfromstrokes(aNote); ///Currentwork01
            canvas.translate((float) aNote.xvs, (float) aNote.yvs);
            canvas.scale(aNote.lastZ_Zoomfactor, aNote.lastZ_Zoomfactor);
            canvas.drawRect(((float) (-aNote.xvs)) / aNote.lastZ_Zoomfactor, ((float) (-aNote.yvs)) / aNote.lastZ_Zoomfactor, (((float) ((Math.max(point.x, point.y) * this.bitmapsizew) / 300)) / aNote.lastZ_Zoomfactor) + (((float) (-aNote.xvs)) / aNote.lastZ_Zoomfactor), (((float) (-aNote.yvs)) / aNote.lastZ_Zoomfactor) + (((float) ((Math.max(point.x, point.y) * this.bitmapsizeh) / 300)) / aNote.lastZ_Zoomfactor), paint2);
            if (aNote.APdfNote) {
                File file = new File(getExternalFilesDir("/"), aNote.Id + ".pdf");
                if (TotalPagesPdf(file) > aNote.lastseenpage) {
                    canvas.drawBitmap(PdfToBitmap(file, aNote.lastseenpage), 0.0f, 0.0f, paint);
                }
            }
            for (int i = 0; i < aNote.pages.get(aNote.lastseenpage).pathslist.size() - aNote.pages.get(aNote.lastseenpage).UndoNumber; i++) {
                if (aNote.pages.get(aNote.lastseenpage).pathslist.get(i).IsVissable == 1) {
                    paint.setStrokeWidth(aNote.pages.get(aNote.lastseenpage).pathslist.get(i).sw);
                    paint.setColor(Color.rgb(aNote.pages.get(aNote.lastseenpage).pathslist.get(i).c[0], aNote.pages.get(aNote.lastseenpage).pathslist.get(i).c[1], aNote.pages.get(aNote.lastseenpage).pathslist.get(i).c[2]));
                    canvas.drawPath(aNote.pages.get(aNote.lastseenpage).pathslist.get(i).P, paint);
                }
            }

            //saveBitmap(aNote.Id, Bitmap.createScaledBitmap(createBitmap, this.bitmapsizew, this.bitmapsizeh, true));
            saveBitmap(aNote.Id, createBitmap);
        }
        else if(opennednote==1){
            Display defaultDisplay = getWindowManager().getDefaultDisplay();
            Point point = new Point();
            defaultDisplay.getSize(point);
            Bitmap createBitmap = Bitmap.createBitmap((Math.max(point.x, point.y) * this.bitmapsizew) / 500, (Math.max(point.x, point.y) * this.bitmapsizeh) / 500, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas();
            canvas.setBitmap(createBitmap);
            canvas.scale(0.7f, 0.7f);
            Paint paint = new Paint();
            Paint paint2 = new Paint();
            paint.setAntiAlias(true);
            paint.setStrokeJoin(Paint.Join.ROUND);
            paint.setStyle(Paint.Style.STROKE);
            paint2.setColor(aNote.pages.get(aNote.lastseenpage).PColor);
//            if (aNote.pages.get(aNote.lastseenpage).PColor == Color.rgb(255, 255, 255)) {
//                paint2.setColor(Color.rgb(225, 225, 220));
//            }
            // createpathsfromstrokes(aNote); ///Currentwork01
            canvas.translate((float) aNote.xvs, (float) aNote.yvs);
            canvas.scale(aNote.lastZ_Zoomfactor, aNote.lastZ_Zoomfactor);
            canvas.drawRect(((float) (-aNote.xvs)) / aNote.lastZ_Zoomfactor, ((float) (-aNote.yvs)) / aNote.lastZ_Zoomfactor, (((float) ((Math.max(point.x, point.y) * this.bitmapsizew) / 300)) / aNote.lastZ_Zoomfactor) + (((float) (-aNote.xvs)) / aNote.lastZ_Zoomfactor), (((float) (-aNote.yvs)) / aNote.lastZ_Zoomfactor) + (((float) ((Math.max(point.x, point.y) * this.bitmapsizeh) / 300)) / aNote.lastZ_Zoomfactor), paint2);
            if (aNote.APdfNote) {
                File file = new File(getExternalFilesDir("/"), aNote.Id + ".pdf");
                if (TotalPagesPdf(file) > aNote.lastseenpage) {
                    canvas.drawBitmap(PdfToBitmap(file, aNote.lastseenpage), 0.0f, 0.0f, paint);
                }
            }
            for (int i = 0; i < aNote.pages.get(aNote.lastseenpage).pathslist.size() - aNote.pages.get(aNote.lastseenpage).UndoNumber; i++) {
                if (aNote.pages.get(aNote.lastseenpage).pathslist.get(i).IsVissable == 1) {
                    paint.setStrokeWidth(aNote.pages.get(aNote.lastseenpage).pathslist.get(i).sw);
                    paint.setColor(Color.rgb(aNote.pages.get(aNote.lastseenpage).pathslist.get(i).c[0], aNote.pages.get(aNote.lastseenpage).pathslist.get(i).c[1], aNote.pages.get(aNote.lastseenpage).pathslist.get(i).c[2]));
                    PathswithC pctemp=aNote.pages.get(aNote.lastseenpage).pathslist.get(i);
                    pctemp.P.offset(pctemp.pathXShift,pctemp.pathYShift);
                    canvas.drawPath(pctemp.P, paint);
                    pctemp.P.offset(-pctemp.pathXShift,-pctemp.pathYShift);
                }
            }
            //saveBitmap(aNote.Id, Bitmap.createScaledBitmap(createBitmap, this.bitmapsizew, this.bitmapsizeh, true));
            saveBitmap(aNote.Id, createBitmap);
        }
    }

    public void SaveThumbOfNote(ANote aNote,int opennednote,Bitmap btmap) {
        //0 means note is new
        // 1 means note is already openned
        //3 means dont care
        if(opennednote==0) {
            Display defaultDisplay = getWindowManager().getDefaultDisplay();
            Point point = new Point();
            defaultDisplay.getSize(point);
            Bitmap createBitmap = Bitmap.createBitmap((Math.max(point.x, point.y) * this.bitmapsizew) / 500, (Math.max(point.x, point.y) * this.bitmapsizeh) / 500, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas();
            canvas.setBitmap(createBitmap);
            canvas.scale(0.7f, 0.7f);
            Paint paint = new Paint();
            Paint paint2 = new Paint();
            paint.setAntiAlias(true);
            paint.setStrokeJoin(Paint.Join.ROUND);
            paint.setStyle(Paint.Style.STROKE);
            paint2.setColor(aNote.pages.get(aNote.lastseenpage).PColor);
//            if (aNote.pages.get(aNote.lastseenpage).PColor == Color.rgb(255, 255, 255)) {
//                paint2.setColor(Color.rgb(225, 225, 220));
//            }
            createpathsfromstrokes(aNote); ///Currentwork01
            canvas.translate((float) aNote.xvs, (float) aNote.yvs);
            canvas.scale(aNote.lastZ_Zoomfactor, aNote.lastZ_Zoomfactor);
            canvas.drawRect(((float) (-aNote.xvs)) / aNote.lastZ_Zoomfactor, ((float) (-aNote.yvs)) / aNote.lastZ_Zoomfactor, (((float) ((Math.max(point.x, point.y) * this.bitmapsizew) / 300)) / aNote.lastZ_Zoomfactor) + (((float) (-aNote.xvs)) / aNote.lastZ_Zoomfactor), (((float) (-aNote.yvs)) / aNote.lastZ_Zoomfactor) + (((float) ((Math.max(point.x, point.y) * this.bitmapsizeh) / 300)) / aNote.lastZ_Zoomfactor), paint2);
            if (aNote.APdfNote) {
                File file = new File(getExternalFilesDir("/"), aNote.Id + ".pdf");
                if (TotalPagesPdf(file) > aNote.lastseenpage) {
                    canvas.drawBitmap(PdfToBitmap(file, aNote.lastseenpage), 0.0f, 0.0f, paint);
                }
            }
            for (int i = 0; i < aNote.pages.get(aNote.lastseenpage).pathslist.size() - aNote.pages.get(aNote.lastseenpage).UndoNumber; i++) {
                if (aNote.pages.get(aNote.lastseenpage).pathslist.get(i).IsVissable == 1) {
                    paint.setStrokeWidth(aNote.pages.get(aNote.lastseenpage).pathslist.get(i).sw);
                    paint.setColor(Color.rgb(aNote.pages.get(aNote.lastseenpage).pathslist.get(i).c[0], aNote.pages.get(aNote.lastseenpage).pathslist.get(i).c[1], aNote.pages.get(aNote.lastseenpage).pathslist.get(i).c[2]));
                    canvas.drawPath(aNote.pages.get(aNote.lastseenpage).pathslist.get(i).P, paint);
                }
            }

            //saveBitmap(aNote.Id, Bitmap.createScaledBitmap(createBitmap, this.bitmapsizew, this.bitmapsizeh, true));
            saveBitmap(aNote.Id, createBitmap);
        }
        else if(opennednote==1){
            Display defaultDisplay = getWindowManager().getDefaultDisplay();
            Point point = new Point();
            defaultDisplay.getSize(point);
            Bitmap createBitmap = Bitmap.createBitmap((Math.max(point.x, point.y) * this.bitmapsizew) / 500, (Math.max(point.x, point.y) * this.bitmapsizeh) / 500, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas();
            canvas.setBitmap(createBitmap);
            canvas.scale(0.7f, 0.7f);
            Paint paint = new Paint();
            Paint paint2 = new Paint();
            paint.setAntiAlias(true);
            paint.setStrokeJoin(Paint.Join.ROUND);
            paint.setStyle(Paint.Style.STROKE);
            paint2.setColor(aNote.pages.get(aNote.lastseenpage).PColor);
//            if (aNote.pages.get(aNote.lastseenpage).PColor == Color.rgb(255, 255, 255)) {
//                paint2.setColor(Color.rgb(225, 225, 220));
//            }
            // createpathsfromstrokes(aNote); ///Currentwork01
            canvas.translate((float) aNote.xvs, (float) aNote.yvs);
            canvas.scale(aNote.lastZ_Zoomfactor, aNote.lastZ_Zoomfactor);
            canvas.drawRect(((float) (-aNote.xvs)) / aNote.lastZ_Zoomfactor, ((float) (-aNote.yvs)) / aNote.lastZ_Zoomfactor, (((float) ((Math.max(point.x, point.y) * this.bitmapsizew) / 300)) / aNote.lastZ_Zoomfactor) + (((float) (-aNote.xvs)) / aNote.lastZ_Zoomfactor), (((float) (-aNote.yvs)) / aNote.lastZ_Zoomfactor) + (((float) ((Math.max(point.x, point.y) * this.bitmapsizeh) / 300)) / aNote.lastZ_Zoomfactor), paint2);

            canvas.drawBitmap(btmap, 0.0f, 0.0f, paint);
            for (int i = 0; i < aNote.pages.get(aNote.lastseenpage).pathslist.size() - aNote.pages.get(aNote.lastseenpage).UndoNumber; i++) {
                if (aNote.pages.get(aNote.lastseenpage).pathslist.get(i).IsVissable == 1) {
                    paint.setStrokeWidth(aNote.pages.get(aNote.lastseenpage).pathslist.get(i).sw);
                    paint.setColor(Color.rgb(aNote.pages.get(aNote.lastseenpage).pathslist.get(i).c[0], aNote.pages.get(aNote.lastseenpage).pathslist.get(i).c[1], aNote.pages.get(aNote.lastseenpage).pathslist.get(i).c[2]));
                    PathswithC pctemp=aNote.pages.get(aNote.lastseenpage).pathslist.get(i);
                    pctemp.P.offset(pctemp.pathXShift,pctemp.pathYShift);
                    canvas.drawPath(pctemp.P, paint);
                    pctemp.P.offset(-pctemp.pathXShift,-pctemp.pathYShift);
                }
            }

            //saveBitmap(aNote.Id, Bitmap.createScaledBitmap(createBitmap, this.bitmapsizew, this.bitmapsizeh, true));
            saveBitmap(aNote.Id, createBitmap);
        }
    }

    public int TotalPagesPdf(File file) {
        PdfRenderer pdfRenderer;
        try {
            pdfRenderer = new PdfRenderer(ParcelFileDescriptor.open(file, 268435456));
        } catch (IOException e) {
            e.printStackTrace();
            pdfRenderer = null;
        }
        return pdfRenderer.getPageCount();
    }

    public Bitmap PdfToBitmap(File file, int i) {
        Exception e;
        Bitmap createBitmap = Bitmap.createBitmap( 2*getResources().getDisplayMetrics().densityDpi* Resources.getSystem().getDisplayMetrics().widthPixels/72,  2*getResources().getDisplayMetrics().densityDpi*Resources.getSystem().getDisplayMetrics().heightPixels/72, Bitmap.Config.ARGB_8888);
        try {
            PdfRenderer pdfRenderer = new PdfRenderer(ParcelFileDescriptor.open(file, 268435456));
            int pageCount = pdfRenderer.getPageCount();
            //Log.d("kamal2", String.valueOf(pageCount));
            if (i < pageCount) {
                PdfRenderer.Page openPage = pdfRenderer.openPage(i);
                Bitmap createBitmap2 = Bitmap.createBitmap((getResources().getDisplayMetrics().densityDpi / 72) * openPage.getWidth(), (getResources().getDisplayMetrics().densityDpi/ 72) * openPage.getHeight(), Bitmap.Config.ARGB_8888);
                Canvas canvv=new Canvas(createBitmap2);
                canvv.drawColor(Color.WHITE);
                canvv.drawBitmap(createBitmap2,0,0,null);
                openPage.render(createBitmap2, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY);

                try {
                    openPage.close();
                    createBitmap = createBitmap2;

                } catch (Exception e2) {
                    e = e2;
                    createBitmap = createBitmap2;
                    e.printStackTrace();
                    return createBitmap;
                }
            }
            pdfRenderer.close();
        } catch (Exception e3) {
            e = e3;
        }
        return createBitmap;
    }

    public Bitmap PdfToBitmap(File file, int i,int clor) {
        Exception e;
        Bitmap createBitmap = Bitmap.createBitmap( 2*getResources().getDisplayMetrics().densityDpi*Resources.getSystem().getDisplayMetrics().widthPixels/72,  2*getResources().getDisplayMetrics().densityDpi*Resources.getSystem().getDisplayMetrics().heightPixels/72, Bitmap.Config.ARGB_8888);
        try {

            PdfRenderer pdfRenderer = new PdfRenderer(ParcelFileDescriptor.open(file, 268435456));
            int pageCount = pdfRenderer.getPageCount();
            //Log.d("kamal2", String.valueOf(pageCount));
            if (i < pageCount) {
                PdfRenderer.Page openPage = pdfRenderer.openPage(i);
                Bitmap createBitmap2 = Bitmap.createBitmap((getResources().getDisplayMetrics().densityDpi / 72) * openPage.getWidth(), (getResources().getDisplayMetrics().densityDpi/ 72) * openPage.getHeight(), Bitmap.Config.ARGB_8888);
                Canvas canvv=new Canvas(createBitmap2);
                canvv.drawColor(clor);
                canvv.drawBitmap(createBitmap2,0,0,null);
                openPage.render(createBitmap2, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY);

                try {
                    openPage.close();
                    createBitmap = createBitmap2;

                } catch (Exception e2) {
                    e = e2;
                    createBitmap = createBitmap2;
                    e.printStackTrace();
                    return createBitmap;
                }
            }
            pdfRenderer.close();
        } catch (Exception e3) {
            e = e3;
        }
        return createBitmap;
    }

    public static final String UserPreferencesSharedPreferencesName="UserPreferncesSharedPreferencesLucidNotesdraw";
    public static final String UserPreferencesXShiftOfPen="XShiftfloat";
    public static final String UserPreferencesYShiftOfPen="YShiftfloat";
    public static final String UserPreferencesColorSections="ColorsectionArrayList";
    public static final String UserPreferencesColorofAll="ColorOfAll";
    public static final String UserPreferencesLastColorSection="lastcolorsection";
    public static float UserPreferencesXShift=0f;
    public static float UserPreferencesYShift=0f;
    public void UserPreferencesSaveData(){
        SharedPreferences.Editor edit = drawingpart.this.getSharedPreferences(UserPreferencesSharedPreferencesName, 0).edit();
        edit.putFloat(UserPreferences.XShiftOfPen,UserPreferencesXShift);
        edit.putFloat(UserPreferences.YShiftOfPen,UserPreferencesYShift);

        edit.apply();
    }
    public void UserPreferencesloaddata() {
        UserPreferencesXShift = drawingpart.this.getSharedPreferences(UserPreferencesSharedPreferencesName, 0).getFloat(UserPreferencesXShiftOfPen, 0f);
        UserPreferencesYShift = drawingpart.this.getSharedPreferences(UserPreferencesSharedPreferencesName, 0).getFloat(UserPreferencesYShiftOfPen, 0f);
    }



    //----------------------------

    public void loadColors(){
        Gson gson=new Gson();
        String temp1=drawingpart.this.getSharedPreferences(UserPreferencesSharedPreferencesName,0).getString(UserPreferencesColorSections,null);
        String temp2=drawingpart.this.getSharedPreferences(UserPreferencesSharedPreferencesName,0).getString(UserPreferencesColorofAll,null);
        if(temp1!=null && temp2!=null) {
            colorPalletSections = gson.fromJson(temp1, new TypeToken<ArrayList<String>>() {}.getType());
            coloursOfAllSections = gson.fromJson(temp2, new TypeToken<ArrayList<ArrayList<Integer>>>() {}.getType());
            lastChoosenColorSection = drawingpart.this.getSharedPreferences(UserPreferencesSharedPreferencesName, 0).getInt(UserPreferencesLastColorSection, 0);
        }else{
            regenerateColurPallet();
        }
    }
    public void SaveColors(){

        Gson gson=new Gson();
        String temp1=gson.toJson(colorPalletSections);
        String temp2=gson.toJson(coloursOfAllSections);

        SharedPreferences.Editor edit = drawingpart.this.getSharedPreferences(UserPreferencesSharedPreferencesName, 0).edit();
        edit.putString(UserPreferencesColorSections,temp1);
        edit.putString(UserPreferencesColorofAll,temp2);
        edit.putInt(UserPreferencesLastColorSection,lastChoosenColorSection);
        edit.apply();

    }

    public void regenerateColurPallet(){
        colorPalletSections=new ArrayList<>();
        for(int i=1;i<=8;i++) {
            colorPalletSections.add(" P"+i+" ");
        }
        colorPalletSections.add(" x");
        coloursOfAllSections=new ArrayList<>();
        ArrayList<Integer> colorarr=new ArrayList<>();
        colorarr.add(Color.parseColor("#000000"));//a
        colorarr.add(Color.parseColor("#990000"));//b
        colorarr.add(Color.parseColor("#000080"));//c
        colorarr.add(Color.parseColor("#004d00"));//d
        colorarr.add(Color.parseColor("#330033"));//e
        colorarr.add(Color.parseColor("#333333"));//a
        colorarr.add(Color.parseColor("#e60000"));//b
        colorarr.add(Color.parseColor("#0000cc"));//c
        colorarr.add(Color.parseColor("#008000"));//d
        colorarr.add(Color.parseColor("#660066"));//e
        colorarr.add(Color.parseColor("#595959"));//a
        colorarr.add(Color.parseColor("#ff0000"));//b
        colorarr.add(Color.parseColor("#0000ff"));//c
        colorarr.add(Color.parseColor("#00ff00"));//d
        colorarr.add(Color.parseColor("#990099"));//e
        colorarr.add(Color.parseColor("#999999"));//a
        colorarr.add(Color.parseColor("#ff3333"));//b
        colorarr.add(Color.parseColor("#3333ff"));//c
        colorarr.add(Color.parseColor("#4dff4d"));//d
        colorarr.add(Color.parseColor("#ff00ff"));//e
        colorarr.add(Color.parseColor("#ffffff"));//a
        colorarr.add(Color.parseColor("#ff8080"));//b
        colorarr.add(Color.parseColor("#6666ff"));//c
        colorarr.add(Color.parseColor("#99ff99"));//d
        colorarr.add(Color.parseColor("#ff99ff"));//e
        coloursOfAllSections.add(colorarr);

        for(int j=0;j<7;j++) {
            colorarr = new ArrayList<>();
            for (int i = 0; i < 25; i++) {
                colorarr.add(Color.rgb(200, 200, 200));
            }
            coloursOfAllSections.add(colorarr);
        }



        lastChoosenColorSection=0;
    }

    public void resetPalletone(){
        ArrayList<Integer> colorarr=new ArrayList<>();
        colorarr.add(Color.parseColor("#000000"));//a
        colorarr.add(Color.parseColor("#990000"));//b
        colorarr.add(Color.parseColor("#000080"));//c
        colorarr.add(Color.parseColor("#004d00"));//d
        colorarr.add(Color.parseColor("#330033"));//e
        colorarr.add(Color.parseColor("#333333"));//a
        colorarr.add(Color.parseColor("#e60000"));//b
        colorarr.add(Color.parseColor("#0000cc"));//c
        colorarr.add(Color.parseColor("#008000"));//d
        colorarr.add(Color.parseColor("#660066"));//e
        colorarr.add(Color.parseColor("#595959"));//a
        colorarr.add(Color.parseColor("#ff0000"));//b
        colorarr.add(Color.parseColor("#0000ff"));//c
        colorarr.add(Color.parseColor("#00ff00"));//d
        colorarr.add(Color.parseColor("#990099"));//e
        colorarr.add(Color.parseColor("#999999"));//a
        colorarr.add(Color.parseColor("#ff3333"));//b
        colorarr.add(Color.parseColor("#3333ff"));//c
        colorarr.add(Color.parseColor("#4dff4d"));//d
        colorarr.add(Color.parseColor("#ff00ff"));//e
        colorarr.add(Color.parseColor("#ffffff"));//a
        colorarr.add(Color.parseColor("#ff8080"));//b
        colorarr.add(Color.parseColor("#6666ff"));//c
        colorarr.add(Color.parseColor("#99ff99"));//d
        colorarr.add(Color.parseColor("#ff99ff"));//e
        coloursOfAllSections.set(0,colorarr);
    }

    
}
