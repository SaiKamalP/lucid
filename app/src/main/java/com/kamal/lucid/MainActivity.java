package com.kamal.lucid;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.pdf.PdfRenderer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.ParcelFileDescriptor;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
//import com.itextpdf.io.font.constants.FontWeights;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.ref.WeakReference;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/* loaded from: classes.dex */
public class MainActivity extends AppCompatActivity {
    public static AFolder CurrentFolder = null;
    public static ANote CurrentNote = null;
    public static AFolder MainFolder = null;
    public static Bitmap bitmapforonDraw = null;
    public static ArrayList<Bitmap> bitmapsofcurrentpdf = null;
    public static File filetoopenPdfas = null;
    public static AFolder foldertosave = null;
    public static boolean imagemode = false;
    public static final String nameforsavingmainfolder = "saved57mainfolder657insahredwqe3preferences";
    public static final String sharedpreferencesname = "shared678preferences675lucid2886";
    public static WeakReference<MainActivity> weakActivity;
    public Button Deletebtn1;
    public ANote ImportedNotes;
    List<Bitmap> bitmapnew;
    public Button btn1;
    public Button btn2;
    public Button btn3;
    public Button btn4;
    public Button cancelbtn;
    public Button createbtn;
    public Button MoveToBtn;
    public AlertDialog dialog;
    public AlertDialog.Builder dialogbuilder;
    public CopyOnWriteArrayList<AFolder> dirforbackbutton;
    GridView grid;
    int[] icons;
    public EditText inputfoldername;
    Intent intent734;
    public TextView locationview;
    String[] names;
    int bitmapsizew = 235;
    int bitmapsizeh = ItemTouchHelper.Callback.DEFAULT_SWIPE_ANIMATION_DURATION;
    public boolean show = false;
    public int ST_CODE = 1;
    public int totalpdfpagesinopenpdf = 0;
    private Handler handler=new Handler();
    public boolean showfoldersnow=true;
    public int enableClickingMode=0;
    public static float ParalaxX,ParalaxY;
    public volatile boolean Should_Save_Now=true;
    public AFolder tempreferenceFolder;
    public AlertDialog moveNoteDialog;
    TextView moveToLocationTextView;
    public ArrayList<View> loadedViews;
    public ArrayList<Integer> lodedViewsIndex;
    public int savednumcol=-1;



    public static MainActivity getmInstanceActivity() {
        return weakActivity.get();
    }




    @Override // androidx.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_main);


        bitmapsofcurrentpdf = new ArrayList<>();
        if (ContextCompat.checkSelfPermission(this, "android.permission.READ_EXTERNAL_STORAGE") != 0) {
           // requestpermission();
        }
        foldertosave = new AFolder("MAIN");
        this.btn1 = (Button) findViewById(R.id.newfolderbtn);
        this.btn2 = (Button) findViewById(R.id.newnotebtn);
        this.btn3 = (Button) findViewById(R.id.ImportNotetbtn);
        this.btn4 = (Button) findViewById(R.id.importPdfbtn);
        this.dirforbackbutton = new CopyOnWriteArrayList<>();
        this.bitmapnew = new ArrayList();
        weakActivity = new WeakReference<>(this);
        CurrentNote = new ANote();
        this.ImportedNotes = new ANote();
        Intent intent = getIntent();
        String action = intent.getAction();
        this.locationview = (TextView) findViewById(R.id.locationD);
        MainFolder = new AFolder("Main");
        loaddata();
        this.dirforbackbutton.add(MainFolder);
        CurrentFolder = new AFolder("temp");
        CurrentFolder = MainFolder;
        if ("android.intent.action.VIEW".equals(action)) {
            Uri data = intent.getData();
            try {
                InputStream openInputStream = getContentResolver().openInputStream(data);
                if (openInputStream != null) {
                   if ("application/pdf".endsWith(intent.getType())) {
                        showfoldersnow=false;
                        ANote aNote2 = new ANote();
                        aNote2.APdfNote = true;
                        aNote2.name = "PDF NOTE";
                       Cursor query = getContentResolver().query(data, null, null, null, null);
                       query.moveToFirst();
                       @SuppressLint("Range") String string = query.getString(query.getColumnIndex("_display_name"));
                       aNote2.name = string.substring(0,string.length()-4);
                       savethisnote(aNote2,2);
                       savedata();
                       showfolders();

                        importPdfExternal importPdfExternal=new importPdfExternal(data,aNote2);
                        new Thread(importPdfExternal).start();



                        Toast.makeText(this, "Please Wait while image loads", Toast.LENGTH_SHORT).show();
                    }
                   else{
                       BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(openInputStream));
                       StringBuilder sb = new StringBuilder();
                       while (true) {
                           String readLine = bufferedReader.readLine();
                           if (readLine == null) {
                               break;
                           }
                           sb.append(readLine);
                       }
                       openInputStream.close();
                       char[] charArray = sb.toString().toCharArray();
                       if (charArray[0] == 'L' && charArray[1] == 'u' && charArray[2] == 'c' && charArray[3] == 'i' && charArray[4] == 'd' && charArray[5] == 'E' && charArray[6] == 'x' && charArray[7] == 'p' && charArray[8] == 'o' && charArray[9] == 'r' && charArray[10] == 't' && charArray[11] == 's' && charArray[12] == 'N') {
                           Log.d("valimage", "its ");
                           StringBuilder sb2 = new StringBuilder();
                           for (int i = 16; i < charArray.length; i++) {
                               sb2.append(charArray[i]);
                           }
                           String sb3 = sb2.toString();
                           Gson gson = new Gson();
                           Type type = new TypeToken<ANote>() { // from class: com.kamal.lucid.MainActivity.1
                           }.getType();
                           if (sb3 == null) {
                               Toast.makeText(getApplicationContext(), "It look like the note is corrupted", Toast.LENGTH_SHORT).show();
                           } else {
                               ANote aNote = (ANote) gson.fromJson(sb3, type);
                               this.ImportedNotes = aNote;
                               aNote.RegenerateId();
                               this.ImportedNotes.usepen = true;
                               Boolean bool = false;
                               Iterator<AFolder> it = MainFolder.Folders.iterator();
                               int i2 = -1;
                               while (true) {
                                   if (!it.hasNext()) {
                                       break;
                                   }
                                   i2++;
                                   if (it.next().Fs == 1) {
                                       bool = true;
                                       break;
                                   }
                               }
                               if (!bool.booleanValue()) {
                                   MainFolder.Folders.add(new AFolder("Imported Notes"));
                                   MainFolder.Folders.get(MainFolder.Folders.size() - 1).Fs = 1;
                                   i2 = MainFolder.Folders.size() - 1;
                               }
                               MainFolder.Folders.get(i2).NoteLinks.add(0, this.ImportedNotes.Id + ".txt");
                               MainFolder.Folders.get(i2).NoteNames.add(0, this.ImportedNotes.name);

                               this.ImportedNotes.APdfNote = false;
                               savethisnote(this.ImportedNotes,0);
                               savedata();
                               Toast.makeText(getApplicationContext(), "Imported", Toast.LENGTH_SHORT).show();
                               // ANote NoteLinkTONote = NoteLinkTONote(MainFolder.Folders.get(i2).NoteLinks.get(0));
                               //CurrentNote = NoteLinkTONote;
                               //createpathsfromstrokes(NoteLinkTONote);
                               CurrentNote=this.ImportedNotes;
                               imagemode = false;
                               openNote();
                           }
                       }

                   }
                }
            } catch (FileNotFoundException e) {
                Log.e("login activity", "File not found: " + e.toString());
                Toast.makeText(getApplicationContext(), "file not found", Toast.LENGTH_SHORT).show();
            } catch (IOException e2) {
                Log.e("login activity", "Can not read file: " + e2.toString());
                Toast.makeText(getApplicationContext(), "How an error Again.", Toast.LENGTH_SHORT).show();
            }
        }
        if ("android.intent.action.SEND".equals(action) && intent.getType().startsWith("image/")) {
            InputStream inputStream = null;
            try {
                inputStream = getContentResolver().openInputStream((Uri) getIntent().getExtras().get("android.intent.extra.STREAM"));
            } catch (FileNotFoundException e3) {
                e3.printStackTrace();
            }
            try {
                BufferedOutputStream bufferedOutputStream2 = new BufferedOutputStream(new FileOutputStream(new File(getExternalFilesDir("/"), "tempnoteforimage.png")));
                byte[] bArr2 = new byte[1000];
                while (true) {
                    int read2 = inputStream.read(bArr2);
                    if (read2 == -1) {
                        break;
                    }
                    bufferedOutputStream2.write(bArr2, 0, read2);
                }
                bufferedOutputStream2.close();
            } catch (IOException e4) {
                e4.printStackTrace();
            }
            bitmapforonDraw = BitmapFactory.decodeFile(getExternalFilesDir("/") + "/tempnoteforimage.png");
            ANote NoteLinkTONote2 = NoteLinkTONote("tempnoteforimage.txt");
            Log.d("fileCq", NoteLinkTONote2.Id);
            savethisnote(NoteLinkTONote2,3);
            CurrentNote = NoteLinkTONote2;
            imagemode = true;
            openNote();
        }



        this.btn1.setEnabled(false);
        this.btn1.setVisibility(View.INVISIBLE);
        this.btn2.setEnabled(false);
        this.btn2.setVisibility(View.INVISIBLE);
        this.btn3.setEnabled(false);
        this.btn3.setVisibility(View.INVISIBLE);
        this.btn4.setEnabled(false);
        this.btn4.setVisibility(View.INVISIBLE);
        if(showfoldersnow){
            showfolders();
        }
        this.grid.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() { // from class: com.kamal.lucid.MainActivity.2
            @Override // android.widget.AdapterView.OnItemLongClickListener
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i3, long j) {
                if(enableClickingMode==0) {
                    if (i3 < MainActivity.CurrentFolder.Folders.size()) {
                        MainActivity.this.RenameFolder(i3);
                        return true;
                    }
                    MainActivity.this.RenameAnote(i3 - MainActivity.CurrentFolder.Folders.size());
                }
                else{
                    Toast.makeText(getApplicationContext(),"Please Wait, Note is being save",Toast.LENGTH_SHORT).show();
                }
                return true;
            }
        });
        this.grid.setOnItemClickListener(new AdapterView.OnItemClickListener() { // from class: com.kamal.lucid.MainActivity.3
            @Override // android.widget.AdapterView.OnItemClickListener
            public void onItemClick(AdapterView<?> adapterView, View view, int i3, long j) {

                if (enableClickingMode==0) {
                    if (i3 < MainActivity.CurrentFolder.Folders.size()) {
                        MainActivity.CurrentFolder = MainActivity.CurrentFolder.Folders.get(i3);
                        MainActivity.this.dirforbackbutton.add(MainActivity.CurrentFolder);
                        MainActivity.this.showfolders();
                        return;
                    }
                    int size = i3 - MainActivity.CurrentFolder.Folders.size();
                    String linktobechanged = MainActivity.CurrentFolder.NoteLinks.get(size);
                    String nametobechanged = MainActivity.CurrentFolder.NoteNames.get(size);

                    MainActivity.CurrentFolder.NoteLinks.remove(size);
                    MainActivity.CurrentFolder.NoteLinks.add(0, linktobechanged);

                    MainActivity.CurrentFolder.NoteNames.remove(size);
                    MainActivity.CurrentFolder.NoteNames.add(0, nametobechanged);
                    MainActivity.CurrentNote = MainActivity.this.NoteLinkTONoteNoCheck(MainActivity.CurrentFolder.NoteLinks.get(0));
                    MainActivity.CurrentNote.name = MainActivity.CurrentFolder.NoteNames.get(0);
                    MainActivity.this.createpathsfromstrokes(MainActivity.CurrentNote);
                    if (MainActivity.CurrentNote.APdfNote) {
                        Toast.makeText(MainActivity.this, "Please wait.", Toast.LENGTH_SHORT).show();
                        MainActivity.filetoopenPdfas = new File(MainActivity.this.getExternalFilesDir("/"), MainActivity.CurrentNote.Id + ".pdf");
                        MainActivity mainActivity = MainActivity.this;
                        mainActivity.totalpdfpagesinopenpdf = mainActivity.TotalPagesPdf(MainActivity.filetoopenPdfas);
                    }
                    new File(MainActivity.this.getExternalFilesDir("/"), MainActivity.CurrentNote.name + ".pdf").delete();
                    MainActivity.imagemode = false;
                    MainActivity.this.savedata();
                    MainActivity.this.openNote();
                }
                else {
                    Toast.makeText(getApplicationContext(), "Please Wait, Note is being save", Toast.LENGTH_SHORT).show();
                }
            }
        });
        Log.d("lucidlog4",this.grid.getChildCount()+"  kk");
    }

    class importPdfExternal implements Runnable {
        Uri data1;
        ANote notett;

        importPdfExternal(Uri data2, ANote notet) {
            data1 = data2;
            notett = notet;
        }

        @Override
        public void run() {
            InputStream openInputStream2 = null;
            try {
                openInputStream2 = getContentResolver().openInputStream(data1);

                BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(new File(getExternalFilesDir("/"), notett.Id + ".pdf")));
                byte[] bArr = new byte[1000];
                while (true) {
                    int read = openInputStream2.read(bArr);
                    if (read == -1) {
                        break;
                    }
                    bufferedOutputStream.write(bArr, 0, read);
                }
                bufferedOutputStream.close();
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        savethisnote(notett,0);
                        MainFolder.NoteLinks.add(0, notett.Id + ".txt");
                        MainFolder.NoteNames.add(0, notett.name);
                        MainFolder.NoteNames.set(0, notett.name);
                        savedata();
                        showfolders();
                    }
                });
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    public void openNote() {
        Intent intent2=new Intent(this, drawingpart.class);
        intent2.putExtra("NoteId",CurrentNote.Id);
        intent2.putExtra("paralaxx",ParalaxX);
        intent2.putExtra("paralaxy",ParalaxY);
        intent2.putExtra("imagemode",imagemode);
        intent2.putExtra("totalpagesinopenpdf",totalpdfpagesinopenpdf);
        intent2.putExtra("filetoopenpdfas",filetoopenPdfas);
        intent2.putExtra("ispdf",CurrentNote.APdfNote);
        intent2.putExtra("nameofnote",CurrentNote.name);
        startActivity(intent2);
        Should_Save_Now=true;
    }

    public void loaddata() {
        Gson gson = new Gson();
        String string = getSharedPreferences(sharedpreferencesname, 0).getString(nameforsavingmainfolder, null);
        Type type = new TypeToken<AFolder>() { // from class: com.kamal.lucid.MainActivity.4
        }.getType();
        if (string == null) {
            MainFolder = new AFolder("MAIN");
            Log.d("new33", "nope: " + string);
            MainFolder = new AFolder("MAIN");
            ANote aNote = new ANote();
            aNote.Id = "tempnoteforimage";
            savethisnote(aNote,0);
            if (savedata()) {
                Log.d("new33", "created");
                return;
            }
            return;
        }
        MainFolder = (AFolder) gson.fromJson(string, type);
    }

    public boolean savedata() {
        new Thread(new Runnable() { // from class: com.kamal.lucid.MainActivity.5
            @Override // java.lang.Runnable
            public void run() {
                //Log.d("new33", json);

                SharedPreferences.Editor edit = MainActivity.this.getSharedPreferences(MainActivity.sharedpreferencesname, 0).edit();
                edit.putString(MainActivity.nameforsavingmainfolder, new Gson().toJson(MainActivity.MainFolder));
                edit.apply();
            }
        }).start();
        return true;
    }

    public void showfolders() {
        loadedViews=new ArrayList<>();
        lodedViewsIndex=new ArrayList<>();
        this.bitmapnew = new ArrayList();
        this.grid = (GridView) findViewById(R.id.datagrid);
        this.icons = new int[CurrentFolder.Folders.size()];
        this.names = new String[CurrentFolder.Folders.size() + CurrentFolder.NoteLinks.size()];
        for (int i = 0; i < CurrentFolder.Folders.size(); i++) {
            this.icons[i] = R.drawable.foldericon;
            this.names[i] = CurrentFolder.Folders.get(i).FName;
        }
        getWindowManager().getDefaultDisplay().getSize(new Point());
        for (int i2 = 0; i2 < CurrentFolder.NoteLinks.size(); i2++) {
            this.names[CurrentFolder.Folders.size() + i2] = CurrentFolder.NoteNames.get(i2);
            this.bitmapnew.add(BitmapFactory.decodeFile(getExternalFilesDir("/") + "/" + CurrentFolder.NoteLinks.get(i2).substring(0, CurrentFolder.NoteLinks.get(i2).length() - 4) + ".png"));
        }
        int numcols=2;
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
        numcols=Math.round((dpWidth-dpWidth%250)/250);
            this.grid.setAdapter((ListAdapter) new gridAdapter(this.names, this.icons, this.bitmapnew, this, CurrentFolder.Folders.size(), CurrentFolder, loadedViews, lodedViewsIndex, numcols));
            savednumcol=grid.getNumColumns();

        showLocationOnTop();
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

    public void showLocationOnTop() {
        Iterator<AFolder> it = this.dirforbackbutton.iterator();
        String str = "";
        while (it.hasNext()) {
            str = str + "/ " + it.next().FName;
        }
        this.locationview.setText(str);
    }

    public void createpathsfromstrokes(ANote aNote) {
        for (int i = 0; i < aNote.pages.size(); i++) {
            for (int i2 = 0; i2 < aNote.pages.get(i).pathsCstrokes.size(); i2++) {
                Path path = new Path();
                path.moveTo(aNote.pages.get(i).pathsCstrokes.get(i2).pointsl.get(0).x, aNote.pages.get(i).pathsCstrokes.get(i2).pointsl.get(0).y);
                for (int i3 = 1; i3 < aNote.pages.get(i).pathsCstrokes.get(i2).pointsl.size(); i3++) {
                    path.lineTo(aNote.pages.get(i).pathsCstrokes.get(i2).pointsl.get(i3).x, aNote.pages.get(i).pathsCstrokes.get(i2).pointsl.get(i3).y);
                }
                aNote.pages.get(i).pathslist.get(i2).P = path;
            }
        }
    }

    @Override // androidx.activity.ComponentActivity, android.app.Activity
    public void onBackPressed() {
        if (this.dirforbackbutton.size() > 1) {
            CopyOnWriteArrayList<AFolder> copyOnWriteArrayList = this.dirforbackbutton;
            CurrentFolder = copyOnWriteArrayList.get(copyOnWriteArrayList.size() - 2);
            CopyOnWriteArrayList<AFolder> copyOnWriteArrayList2 = this.dirforbackbutton;
            copyOnWriteArrayList2.remove(copyOnWriteArrayList2.size() - 1);
            savedata();
            Log.d("new323", "yes");
            showfolders();
            return;
        }
        finish();
        System.exit(0);
    }

    public void plusbutton(View view) {
        if (this.btn1.getVisibility() == View.VISIBLE) {
            this.btn1.setEnabled(false);
            this.btn1.setVisibility(View.INVISIBLE);
            this.btn2.setEnabled(false);
            this.btn2.setVisibility(View.INVISIBLE);
            this.btn3.setEnabled(false);
            this.btn3.setVisibility(View.INVISIBLE);
            this.btn4.setEnabled(false);
            this.btn4.setVisibility(View.INVISIBLE);
            return;
        }
        this.btn1.setEnabled(true);
        this.btn1.setVisibility(View.VISIBLE);
        this.btn2.setEnabled(true);
        this.btn2.setVisibility(View.VISIBLE);
        this.btn3.setEnabled(true);
        this.btn3.setVisibility(View.VISIBLE);
        this.btn4.setEnabled(true);
        this.btn4.setVisibility(View.VISIBLE);
    }

    public void makenewfolder(String str) {
        CurrentFolder.Folders.add(new AFolder(str));
        Bundle bundle = new Bundle();
        bundle.putInt("kamal", 1);
        savedata();
        showfolders();
    }

    public void clickednewfolder(View view) {
        Askfoldername();
        this.btn1.setEnabled(false);
        this.btn1.setVisibility(View.INVISIBLE);
        this.btn2.setEnabled(false);
        this.btn2.setVisibility(View.INVISIBLE);
        this.btn3.setEnabled(false);
        this.btn3.setVisibility(View.INVISIBLE);
        this.btn4.setEnabled(false);
        this.btn4.setVisibility(View.INVISIBLE);
    }

    public void newNote(View view) {
        ANote aNote = new ANote();
        CurrentFolder.NoteLinks.add(0, aNote.Id + ".txt");
        CurrentFolder.NoteNames.add(0, aNote.name);
        savethisnote(aNote,0);
        savedata();
        showfolders();
        this.btn1.setEnabled(false);
        this.btn1.setVisibility(View.INVISIBLE);
        this.btn2.setEnabled(false);
        this.btn2.setVisibility(View.INVISIBLE);
        this.btn3.setEnabled(false);
        this.btn3.setVisibility(View.INVISIBLE);
        this.btn4.setEnabled(false);
        this.btn4.setVisibility(View.INVISIBLE);
    }

    public void Askfoldername() {
        this.dialogbuilder = new AlertDialog.Builder(this,R.style.WrapContentDialog);

        View inflate = getLayoutInflater().inflate(R.layout.singleinput, (ViewGroup) null);


        this.createbtn = (Button) inflate.findViewById(R.id.CreateButton);
        this.cancelbtn = (Button) inflate.findViewById(R.id.cancelbutton);
        this.inputfoldername = (EditText) inflate.findViewById(R.id.inputfoldername);

        inputfoldername.dispatchTouchEvent(MotionEvent.obtain(System.currentTimeMillis(),System.currentTimeMillis(),MotionEvent.ACTION_DOWN,10,10,0));
        inputfoldername.dispatchTouchEvent(MotionEvent.obtain(System.currentTimeMillis(),System.currentTimeMillis(),MotionEvent.ACTION_UP,10,10,0));

        this.dialogbuilder.setView(inflate);
        AlertDialog create = this.dialogbuilder.create();
        this.dialog = create;
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.getWindow().setGravity(Gravity.BOTTOM);



        create.show();
        this.createbtn.setOnClickListener(new View.OnClickListener() { // from class: com.kamal.lucid.MainActivity.6
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                MainActivity.this.makenewfolder(MainActivity.this.inputfoldername.getText().toString());
                MainActivity.this.dialog.dismiss();
            }
        });
        this.cancelbtn.setOnClickListener(new View.OnClickListener() { // from class: com.kamal.lucid.MainActivity.7
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                MainActivity.this.dialog.dismiss();
            }
        });
    }

    public void RenameFolder(final int i) {
        this.dialogbuilder = new AlertDialog.Builder(this,R.style.WrapContentDialog);
        View inflate = getLayoutInflater().inflate(R.layout.rename_folder_dialog, (ViewGroup) null);
        this.createbtn = (Button) inflate.findViewById(R.id.CreateButtonFolder);
        this.cancelbtn = (Button) inflate.findViewById(R.id.cancelbuttonFolder);
        this.Deletebtn1 = (Button) inflate.findViewById(R.id.deletebtn1Folder);
        this.inputfoldername = (EditText) inflate.findViewById(R.id.inputfoldername);
        this.MoveToBtn=(Button) inflate.findViewById(R.id.MoveFolderBtn);
        this.dialogbuilder.setView(inflate);
        AlertDialog create = this.dialogbuilder.create();
        this.dialog = create;
        create.show();
        inputfoldername.setText(""+CurrentFolder.Folders.get(i).FName);
        this.createbtn.setOnClickListener(new View.OnClickListener() { // from class: com.kamal.lucid.MainActivity.8
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                MainActivity.CurrentFolder.Folders.get(i).FName = MainActivity.this.inputfoldername.getText().toString();
                MainActivity.this.savedata();
                MainActivity.this.showfolders();
                MainActivity.this.dialog.dismiss();
            }
        });
        this.cancelbtn.setOnClickListener(new View.OnClickListener() { // from class: com.kamal.lucid.MainActivity.9
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                MainActivity.this.dialog.dismiss();
            }
        });
        this.Deletebtn1.setOnLongClickListener(new View.OnLongClickListener() { // from class: com.kamal.lucid.MainActivity.10
            @Override // android.view.View.OnClickListener
            public boolean onLongClick(View view) {
                MainActivity.this.dialog.dismiss();
                    deleteFolderContents(MainActivity.CurrentFolder.Folders.get(i));
                    MainActivity.CurrentFolder.Folders.remove(i);

                MainActivity.this.savedata();
                MainActivity.this.showfolders();
                return true;
            }
        });
        this.MoveToBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                AlertDialog.Builder alertDialogBuilder=new AlertDialog.Builder(MainActivity.this,R.style.WrapContentDialog);
                View inflate2=getLayoutInflater().inflate(R.layout.display_all_folders_to_move_note,null);
                GridView mainSelectingGrid=inflate2.findViewById(R.id.Show_Folders_grid);
                Button ConfirmMove=inflate2.findViewById(R.id.MoveNowBtn);
                Button cancle=inflate2.findViewById(R.id.CancleMove);
                moveToLocationTextView=inflate2.findViewById(R.id.Location_displayer_in_Move);
                moveToLocationTextView.setText("/MAIN(Root)/");
                alertDialogBuilder.setView(inflate2);
                moveNoteDialog=alertDialogBuilder.create();
                moveNoteDialog.show();
                ArrayList<Integer> MomentTractker=new ArrayList<>();
                tempreferenceFolder=MainFolder;
                mainSelectingGrid.setAdapter(new FoldersShowingInMoveToForNoteGridAdapter(MainActivity.this,MainFolder));

                mainSelectingGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i2, long l) {

                        if(i2==0){
                            if(MomentTractker.size()>0){
                                MomentTractker.remove(MomentTractker.size()-1);
                                tempreferenceFolder=MainFolder;
                                for(int k :MomentTractker){
                                    tempreferenceFolder=tempreferenceFolder.Folders.get(k);
                                }
                                mainSelectingGrid.setAdapter(new FoldersShowingInMoveToForNoteGridAdapter(MainActivity.this,tempreferenceFolder));

                            }
                            else{
                                Toast.makeText(getApplicationContext(),"Can't move there.",Toast.LENGTH_SHORT).show();
                            }
                        }
                        else if(CurrentFolder.Folders.get(i)!=tempreferenceFolder.Folders.get(i2-1)) {
                                i2 -= 1;
                                tempreferenceFolder = tempreferenceFolder.Folders.get(i2);
                                mainSelectingGrid.setAdapter(new FoldersShowingInMoveToForNoteGridAdapter(MainActivity.this, tempreferenceFolder));
                                MomentTractker.add(i2);
                            }
                        else{
                            Toast.makeText(getApplicationContext(),"Can't move there.",Toast.LENGTH_SHORT).show();
                        }

                        String str="/MAIN(Root)/";
                        AFolder tempreferenceFolder2=MainFolder;
                        for (int k :MomentTractker){
                            tempreferenceFolder2=tempreferenceFolder2.Folders.get(k);
                            str=str+tempreferenceFolder2.FName+"/";
                        }
                        moveToLocationTextView.setText(str);
                    }
                });
                ConfirmMove.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        moveNoteDialog.dismiss();
                        MoveFolderLocation(CurrentFolder,i,tempreferenceFolder);
                        showfolders();
                    }
                });
                cancle.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        moveNoteDialog.dismiss();
                    }
                });


                showfolders();

            }
        });
    }

    public void RenameAnote(final int i) {
        this.dialogbuilder = new AlertDialog.Builder(this,R.style.WrapContentDialog);
        View inflate = getLayoutInflater().inflate(R.layout.rename_notes_dialog, (ViewGroup) null);
        this.createbtn = (Button) inflate.findViewById(R.id.CreateButtonFolder);
        this.cancelbtn = (Button) inflate.findViewById(R.id.cancelbuttonFolder);
        this.Deletebtn1 = (Button) inflate.findViewById(R.id.deletebtn1Folder);
        this.inputfoldername = (EditText) inflate.findViewById(R.id.inputfoldername);
        this.MoveToBtn=(Button) inflate.findViewById(R.id.MoveFolderBtn);
        this.dialogbuilder.setView(inflate);
        AlertDialog create = this.dialogbuilder.create();
        this.dialog = create;
        create.show();
        this.createbtn.setOnClickListener(new View.OnClickListener() { // from class: com.kamal.lucid.MainActivity.11
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                String obj = MainActivity.this.inputfoldername.getText().toString();
                new ANote();
                //ANote NoteLinkTONote = MainActivity.this.NoteLinkTONote(MainActivity.CurrentFolder.NoteLinks.get(i));
                new File(MainActivity.this.getExternalFilesDir("/"), MainActivity.CurrentFolder.NoteNames.get(i)+".pdf").delete();
                MainActivity.CurrentFolder.NoteNames.set(i, obj);
                // MainActivity.this.savethisnote(NoteLinkTONote,3);
                MainActivity.this.savedata();
                MainActivity.this.showfolders();
                MainActivity.this.dialog.dismiss();
            }
        });
        this.cancelbtn.setOnClickListener(new View.OnClickListener() { // from class: com.kamal.lucid.MainActivity.12
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                MainActivity.this.dialog.dismiss();
            }
        });
        this.Deletebtn1.setOnLongClickListener(new View.OnLongClickListener() { // from class: com.kamal.lucid.MainActivity.13
            @Override // android.view.View.OnClickListener
            public boolean onLongClick(View view) {
                MainActivity.this.dialog.dismiss();
                    new File(MainActivity.this.getExternalFilesDir("/"), MainActivity.CurrentFolder.NoteLinks.get(i)).delete();
                new File(MainActivity.this.getExternalFilesDir("/"), MainActivity.CurrentFolder.NoteLinks.get(i).substring(0,MainActivity.CurrentFolder.NoteLinks.get(i).length()-1-3)+".pdf").delete();
                new File(MainActivity.this.getExternalFilesDir("/"), MainActivity.CurrentFolder.NoteNames.get(i)+".pdf").delete();

                    MainActivity.CurrentFolder.NoteLinks.remove(i);
                    MainActivity.CurrentFolder.NoteNames.remove(i);

                MainActivity.this.savedata();
                MainActivity.this.showfolders();
                MainActivity.this.dialog.dismiss();
                return true;
            }
        });

        this.MoveToBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                AlertDialog.Builder alertDialogBuilder=new AlertDialog.Builder(MainActivity.this,R.style.WrapContentDialog);
                View inflate2=getLayoutInflater().inflate(R.layout.display_all_folders_to_move_note,null);
                GridView mainSelectingGrid=inflate2.findViewById(R.id.Show_Folders_grid);
                Button ConfirmMove=inflate2.findViewById(R.id.MoveNowBtn);
                Button cancle=inflate2.findViewById(R.id.CancleMove);
                moveToLocationTextView=inflate2.findViewById(R.id.Location_displayer_in_Move);
                moveToLocationTextView.setText("/MAIN(Root)/");
                alertDialogBuilder.setView(inflate2);
                moveNoteDialog=alertDialogBuilder.create();
                moveNoteDialog.show();
                ArrayList<Integer> MomentTractker=new ArrayList<>();
                tempreferenceFolder=MainFolder;
                mainSelectingGrid.setAdapter(new FoldersShowingInMoveToForNoteGridAdapter(MainActivity.this,MainFolder));

                mainSelectingGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i2, long l) {
                        if(i2==0){
                            if(MomentTractker.size()>0){
                                MomentTractker.remove(MomentTractker.size()-1);
                                tempreferenceFolder=MainFolder;
                               for(int k :MomentTractker){
                                   tempreferenceFolder=tempreferenceFolder.Folders.get(k);
                               }
                                mainSelectingGrid.setAdapter(new FoldersShowingInMoveToForNoteGridAdapter(MainActivity.this,tempreferenceFolder));

                            }
                            else{
                                Toast.makeText(getApplicationContext(),"Can't move there.",Toast.LENGTH_SHORT).show();
                            }
                        }
                        else{
                            i2-=1;
                            tempreferenceFolder=tempreferenceFolder.Folders.get(i2);
                            mainSelectingGrid.setAdapter(new FoldersShowingInMoveToForNoteGridAdapter(MainActivity.this,tempreferenceFolder));
                            MomentTractker.add(i2);

                        }
                        String str="/MAIN(Root)/";
                        AFolder tempreferenceFolder2=MainFolder;
                        for (int k :MomentTractker){
                            tempreferenceFolder2=tempreferenceFolder2.Folders.get(k);
                            str=str+tempreferenceFolder2.FName+"/";
                        }
                        moveToLocationTextView.setText(str);
                    }
                });
                ConfirmMove.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        moveNoteDialog.dismiss();
                        MoveNoteLocation(CurrentFolder,i,tempreferenceFolder);
                        showfolders();
                    }
                });
                cancle.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        moveNoteDialog.dismiss();
                    }
                });


                showfolders();

            }
        });
    }



    public void deleteFolderContents(AFolder foldertod){
        int tempc1=foldertod.NoteLinks.size();
        for (int i = 0; i < tempc1; i++) {
            Log.d("delpp",String.valueOf(i));
            Log.d("delpp",String.valueOf(foldertod.NoteNames));
            new File(MainActivity.this.getExternalFilesDir("/"), foldertod.NoteLinks.get(0)).delete();
            new File(MainActivity.this.getExternalFilesDir("/"), foldertod.NoteLinks.get(0).substring(0,foldertod.NoteLinks.get(0).length()-1-3)+".pdf").delete();
            new File(MainActivity.this.getExternalFilesDir("/"), foldertod.NoteNames.get(0)+".pdf").delete();
            foldertod.NoteLinks.remove(0);
            foldertod.NoteNames.remove(0);
        }
        for (int i = 0; i < foldertod.Folders.size(); i++) {
            deleteFolderContents(foldertod.Folders.get(i));
        }
    }

    @Override // android.app.Activity
    public boolean onTouchEvent(MotionEvent motionEvent) {
        int action = motionEvent.getAction();
        if (action == 0) {
            this.show = true;
        } else if (action == 1) {
            this.show = false;
        }
        return super.onTouchEvent(motionEvent);
    }

    public void importnoteNow(View view) {
        Intent intent = new Intent("android.intent.action.GET_CONTENT");
        this.intent734 = intent;
        intent.setType("*/*");
        startActivityForResult(this.intent734, 734);
    }

    public void importpdfNow(View view) {
        Intent intent = new Intent("android.intent.action.GET_CONTENT");
        this.intent734 = intent;
        intent.setType("application/pdf");
        //pdfImportActivityResultLauncher.launch(intent);
        startActivityForResult(this.intent734, 735);
    }


    @Override // androidx.fragment.app.FragmentActivity, android.app.Activity
    protected void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);

        if (i != 734 && intent!=null) {
            if (i == 735 && i2 == -1) {
                /*
                Toast.makeText(this, "Please Wait", Toast.LENGTH_SHORT).show();
                Uri data = intent.getData();
                Log.d("pdf24", String.valueOf(getPath(this, data)));
                File file = new File(Uri.fromFile(new File(Objects.requireNonNull(getPath(this, data)))).getPath());
                ANote aNote = new ANote();
                aNote.APdfNote = true;
                try {
                    copyDirectoryOneLocationToAnotherLocation(file, new File(getExternalFilesDir("/"), aNote.Id + ".pdf"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                CurrentFolder.NoteLinks.add(0, aNote.Id + ".txt");
                CurrentFolder.NoteNames.add(0, aNote.name);

                aNote.name = "PDF NOTE";
                Cursor query = getContentResolver().query(data, null, null, null, null);
                query.moveToFirst();
                @SuppressLint("Range") String string = query.getString(query.getColumnIndex("_display_name"));
                aNote.name = string.substring(0, Math.min(string.length() - 4, 15));
                CurrentFolder.NoteNames.set(0, aNote.name);
                savethisnote(aNote,0);
                savedata();
                showfolders();
                Toast.makeText(this, "No of pages: " + String.valueOf(TotalPagesPdf(file)), Toast.LENGTH_SHORT).show();
                */
                Log.d("pdftest",intent.getDataString());

                Uri data = intent.getData();


                try {
                    InputStream openInputStream = getContentResolver().openInputStream(data);
                    if (openInputStream != null) {
                        if (intent.getDataString().endsWith("pdf")) {
                            Toast.makeText(getApplicationContext(),"Import In Progress",Toast.LENGTH_SHORT).show();
                            showfolders();
                            ANote aNote2 = new ANote();
                            aNote2.APdfNote = true;
                            aNote2.name = "PDF NOTE";
                            AFolder foldertemp=MainActivity.CurrentFolder;

                            Cursor query = getContentResolver().query(data, null, null, null, null);
                            query.moveToFirst();
                            @SuppressLint("Range") String string = query.getString(query.getColumnIndex("_display_name"));
                            //aNote2.name = string.substring(0, Math.min(string.length() - 4, 16));
                            aNote2.name = string.substring(0,string.length()-4);
                            //MainActivity.CurrentFolder.NoteNames.set(0, aNote2.name);
                            savethisnote(aNote2,2);

                            importPdfFromInsideApp importPdfFromInsideApp=new importPdfFromInsideApp(data,aNote2,foldertemp);
                            new Thread(importPdfFromInsideApp).start();


                        }
                    }
                } catch (FileNotFoundException e) {
                    Log.e("login activity", "File not found: " + e.toString());
                    Toast.makeText(getApplicationContext(), "file not found", Toast.LENGTH_SHORT).show();
                } catch (IOException e2) {
                    Log.e("login activity", "Can not read file: " + e2.toString());
                    Toast.makeText(getApplicationContext(), "How an error Again.", Toast.LENGTH_SHORT).show();
                }



            }
        }
        else if (i2 == -1) {
            try {
                InputStream openInputStream = getContentResolver().openInputStream(intent.getData());
                if (openInputStream != null) {
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(openInputStream));
                    StringBuilder sb = new StringBuilder();
                    while (true) {
                        String readLine = bufferedReader.readLine();
                        if (readLine == null) {
                            break;
                        }
                        sb.append(readLine);
                    }
                    openInputStream.close();
                    char[] charArray = sb.toString().toCharArray();
                    if (charArray[0] == 'L' && charArray[1] == 'u' && charArray[2] == 'c' && charArray[3] == 'i' && charArray[4] == 'd' && charArray[5] == 'E' && charArray[6] == 'x' && charArray[7] == 'p' && charArray[8] == 'o' && charArray[9] == 'r' && charArray[10] == 't' && charArray[11] == 's' && charArray[12] == 'N') {
                        StringBuilder sb2 = new StringBuilder();
                        for (int i3 = 16; i3 < charArray.length; i3++) {
                            sb2.append(charArray[i3]);
                        }
                        String sb3 = sb2.toString();
                        Gson gson = new Gson();
                        Type type = new TypeToken<ANote>() { // from class: com.kamal.lucid.MainActivity.18
                        }.getType();
                        if (sb3 == null) {
                            Toast.makeText(getApplicationContext(), "It look like the note is corrupted", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        ANote aNote2 = (ANote) gson.fromJson(sb3, type);
                        this.ImportedNotes = aNote2;
                        aNote2.RegenerateId();
                        this.ImportedNotes.usepen = false;
                        CurrentFolder.NoteLinks.add(0, this.ImportedNotes.Id + ".txt");
                        CurrentFolder.NoteNames.add(0, this.ImportedNotes.name);
                        this.ImportedNotes.APdfNote = false;
                        savethisnote(this.ImportedNotes,0);
                        savedata();
                        Toast.makeText(getApplicationContext(), "Imported", Toast.LENGTH_SHORT).show();
                        //ANote NoteLinkTONote = NoteLinkTONote(CurrentFolder.NoteLinks.get(0));
                       // CurrentNote = NoteLinkTONote;
                        //createpathsfromstrokes(NoteLinkTONote);
                        CurrentNote=ImportedNotes;
                        imagemode = false;
                        openNote();
                        return;
                    }
                    Toast.makeText(getApplicationContext(), "Lucid Can't handle this file type yet", Toast.LENGTH_SHORT).show();
                }
            } catch (FileNotFoundException e2) {
                Log.e("login activity", "File not found: " + e2.toString());
                Toast.makeText(getApplicationContext(), "file not found", Toast.LENGTH_SHORT).show();
            } catch (IOException e3) {
                Log.e("login activity", "Can not read file: " + e3.toString());
                Toast.makeText(getApplicationContext(), "How an error Again.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    class importPdfFromInsideApp implements Runnable{
        Uri data22;
        ANote notett;
        AFolder foltemp2;
        importPdfFromInsideApp(Uri data23, ANote notet,AFolder foltemp){
            data22=data23;
            notett=notet;
            foltemp2=foltemp;
        }
        @Override
        public void run() {
            try {
                InputStream openInputStream2 = getContentResolver().openInputStream(data22);
                BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(new File(getExternalFilesDir("/"), notett.Id + ".pdf")));
                byte[] bArr = new byte[1000];
                while (true) {
                    int read = openInputStream2.read(bArr);
                    if (read == -1) {
                        break;
                    }
                    bufferedOutputStream.write(bArr, 0, read);
                }
                bufferedOutputStream.close();
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        foltemp2.NoteLinks.add(0, notett.Id + ".txt");
                        foltemp2.NoteNames.add(0, notett.name);
                        savedata();
                        savethisnote(notett,0);
                        showfolders();
                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public ANote NoteLinkTONote(String str) {
        String readFromFile = readFromFile(str, getApplicationContext());
        //Log.d("idcheck3", str);
        char[] charArray = readFromFile.toCharArray();
        if (charArray[0] == 'L' && charArray[1] == 'u' && charArray[2] == 'c' && charArray[3] == 'i' && charArray[4] == 'd' && charArray[5] == 'E' && charArray[6] == 'x' && charArray[7] == 'p' && charArray[8] == 'o' && charArray[9] == 'r' && charArray[10] == 't' && charArray[11] == 's' && charArray[12] == 'N') {
           // StringBuilder sb = new StringBuilder();
           // for (int i = 16; i < charArray.length; i++) {
           //     sb.append(charArray[i]);
           // }
            String sb2 = readFromFile.substring(16);//sb.toString();
            Gson gson = new Gson();
            Type type = new TypeToken<ANote>() { // from class: com.kamal.lucid.MainActivity.19
            }.getType();
            if (sb2 == null) {
                Toast.makeText(getApplicationContext(), "It look like the note is corrupted", Toast.LENGTH_SHORT).show();
            } else {
                this.ImportedNotes = (ANote) gson.fromJson(sb2, type);
            }
        }
        return this.ImportedNotes;
    }

    public ANote NoteLinkTONoteNoCheck(String str) {
        String readFromFile = readFromFile(str, getApplicationContext());
            String sb2 = readFromFile.substring(16);//sb.toString();
            Gson gson = new Gson();
            Type type = new TypeToken<ANote>() { // from class: com.kamal.lucid.MainActivity.19
            }.getType();
            if (sb2 == null) {
                Toast.makeText(getApplicationContext(), "It look like the note is corrupted", Toast.LENGTH_SHORT).show();
            } else {
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

    public void writeToFile(String str, String str2, Context context) {
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput(str2, 0));
            outputStreamWriter.write(str);
            outputStreamWriter.close();
        } catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }

    public void savethisnote(ANote aNote,int opennednote) {
        enableClickingMode+=1;
        SaveNoteFromNotefile saveNoteFromNotefile=new SaveNoteFromNotefile(aNote);
        new Thread(saveNoteFromNotefile).start();
        SaveThumbOfNote(aNote,opennednote);
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
                        enableClickingMode += -1;
                        if (enableClickingMode == 0) {

                            Toast.makeText(getApplicationContext(), "All Note Saved", Toast.LENGTH_SHORT).show();

                            savedata();
                            showfolders();
                        }

                    }
                });
            } catch (Exception e) {
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
                        enableClickingMode += -1;
                        if (enableClickingMode == 0) {
                            Should_Save_Now = true;
                            //Toast.makeText(getApplicationContext(),"kkkkkkkkk",Toast.LENGTH_SHORT).show();
                            savedata();
                        }

                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private ArrayList<Bitmap> pdfToBitmap(File file) {
        ArrayList<Bitmap> arrayList = new ArrayList<>();
        try {
            PdfRenderer pdfRenderer = new PdfRenderer(ParcelFileDescriptor.open(file, 268435456));
            int pageCount = pdfRenderer.getPageCount();
            Log.d("kamal2", String.valueOf(pageCount));
            for (int i = 0; i < pageCount; i++) {
                PdfRenderer.Page openPage = pdfRenderer.openPage(i);
                Bitmap createBitmap = Bitmap.createBitmap((getResources().getDisplayMetrics().densityDpi / 72) * openPage.getWidth(), (getResources().getDisplayMetrics().densityDpi / 72) * openPage.getHeight(), Bitmap.Config.ARGB_8888);
                openPage.render(createBitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY);
                arrayList.add(createBitmap);
                openPage.close();
            }
            pdfRenderer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return arrayList;
    }

    public Bitmap PdfToBitmap(File file, int i) {
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



    @Override // androidx.fragment.app.FragmentActivity, android.app.Activity, androidx.core.app.ActivityCompat.OnRequestPermissionsResultCallback
    public void onRequestPermissionsResult(int i, String[] strArr, int[] iArr) {
        super.onRequestPermissionsResult(i, strArr, iArr);
        if (i == this.ST_CODE && iArr.length > 0 && iArr[0] == 0) {
            Toast.makeText(this, "PERMISSION GRANTED", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        showfolders();
    }

        /*  public static String getPath(Context context, Uri uri) {
        if (!(Build.VERSION.SDK_INT >= 19) || !DocumentsContract.isDocumentUri(context, uri)) {
            return null;
        }
        System.out.println("getPath() uri: " + uri.toString());
        System.out.println("getPath() uri authority: " + uri.getAuthority());
        System.out.println("getPath() uri path: " + uri.getPath());
        if (!"com.android.externalstorage.documents".equals(uri.getAuthority())) {
            return null;
        }
        String documentId = DocumentsContract.getDocumentId(uri);
        String[] split = documentId.split(":");
        String str = split[0];
        System.out.println("getPath() docId: " + documentId + ", split: " + split.length + ", type: " + str);
        if (!"primary".equalsIgnoreCase(str)) {
            return "storage/" + documentId.replace(":", "/");
        }
        if (split.length > 1) {
            return Environment.getExternalStorageDirectory() + "/" + split[1] + "/";
        }
        return Environment.getExternalStorageDirectory() + "/";
    }
   */
    /*public static void copyDirectoryOneLocationToAnotherLocation(File file, File file2) throws IOException {
        if (file.isDirectory()) {
            if (!file2.exists()) {
                file2.mkdir();
            }
            String[] list = file.list();
            for (int i = 0; i < file.listFiles().length; i++) {
                copyDirectoryOneLocationToAnotherLocation(new File(file, list[i]), new File(file2, list[i]));
            }
            return;
        }
        FileInputStream fileInputStream = new FileInputStream(file);
        FileOutputStream fileOutputStream = new FileOutputStream(file2);
        byte[] bArr = new byte[1024];
        while (true) {
            int read = fileInputStream.read(bArr);
            if (read > 0) {
                fileOutputStream.write(bArr, 0, read);
            } else {
                fileInputStream.close();
                fileOutputStream.close();
                return;
            }
        }
    }

     */

/*    public String getRealPathFromURI(Uri uri) {
        Cursor loadInBackground = new CursorLoader(this, uri, new String[]{"_data", "_data"}, null, null, null).loadInBackground();
        loadInBackground.moveToFirst();
        return loadInBackground.getString(0);
    }
    */


    /*public static void copyStream(InputStream inputStream, OutputStream outputStream) throws IOException {
        byte[] bArr = new byte[1024];
        while (true) {
            int read = inputStream.read(bArr);
            if (read != -1) {
                outputStream.write(bArr, 0, read);
            } else {
                return;
            }
        }
    }*/
    public void MoveNoteLocation(AFolder CurrrentParent,int Curent_Note_index,AFolder Parent_Destination){
        if(Parent_Destination!=CurrrentParent){
            Parent_Destination.NoteNames.add(0,CurrrentParent.NoteNames.get(Curent_Note_index));
            Parent_Destination.NoteLinks.add(0,CurrrentParent.NoteLinks.get(Curent_Note_index));
            CurrrentParent.NoteLinks.remove(Curent_Note_index);
            CurrrentParent.NoteNames.remove(Curent_Note_index);
            savedata();
        }
    }

    public void MoveFolderLocation(AFolder CurrentParent,int IndexOfFolder,AFolder Parent_Destination) {
        try {
            Parent_Destination.Folders.add(0, (AFolder) CurrentParent.Folders.get(IndexOfFolder).clone());
            CurrentParent.Folders.remove(IndexOfFolder);
            savedata();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        showfolders();
    }
}
