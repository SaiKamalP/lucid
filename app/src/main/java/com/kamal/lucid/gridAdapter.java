package com.kamal.lucid;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes.dex */
public class gridAdapter extends BaseAdapter {
    private List<Bitmap> bitmapnew1;
    Context context;
    private int[] images;
    private String[] name;
    public int numoFolders;
    public AFolder foldersent;
    ArrayList<View> lodedViewssent;
    ArrayList<Integer> lodedIndex;
    int numcolms;

    @Override // android.widget.Adapter
    public Object getItem(int i) {
        return null;
    }

    @Override // android.widget.Adapter
    public long getItemId(int i) {
        return 0;
    }

    public gridAdapter(String[] strArr, int[] iArr, List<Bitmap> list, Context context,int numOfFolders,AFolder folder,ArrayList<View> lodedviews,ArrayList<Integer> intsent,int numcol) {
        this.name = strArr;
        this.images = iArr;
        this.context = context;
        this.bitmapnew1 = new ArrayList();
        this.bitmapnew1 = list;
        numoFolders=numOfFolders;
        foldersent=folder;
        lodedViewssent=lodedviews;
        lodedIndex=intsent;
        numcolms=numcol;
    }

    @Override // android.widget.Adapter
    public int getCount() {
        return this.name.length;
    }

    @SuppressLint({"WrongConstant", "SetTextI18n"})
    @Override // android.widget.Adapter
    public View getView(int i, View view, ViewGroup viewGroup) {
        if(lodedIndex.contains(i)){
            return lodedViewssent.get(lodedIndex.indexOf(i));
        }
        ImageView imageView;
        TextView textView;
        TextView textview8;
        View inflate;

        Log.d("LucidLog","loading "+i +",numcols "+numcolms);
        if(!(i+1>(numoFolders-numoFolders%numcolms))) {

           inflate = ((LayoutInflater) this.context.getSystemService("layout_inflater")).inflate(R.layout.folderdisplaylayout02, (ViewGroup) null);
            imageView = (ImageView) inflate.findViewById(R.id.imageView);
            textView = (TextView) inflate.findViewById(R.id.textView7);
            textview8=(TextView) inflate.findViewById(R.id.textView8);
            textview8.setText(""+foldersent.Folders.get(i).Folders.size()+" Folders/ "+foldersent.Folders.get(i).NoteLinks.size()+" Notes.");
        }
        else if(i<numoFolders){
            inflate = ((LayoutInflater) this.context.getSystemService("layout_inflater")).inflate(R.layout.folderframeforfixing, (ViewGroup) null);
            imageView = (ImageView) inflate.findViewById(R.id.imageView);
            textView = (TextView) inflate.findViewById(R.id.textView7);
            textview8=(TextView) inflate.findViewById(R.id.textView8);
            textview8.setText(""+foldersent.Folders.get(i).Folders.size()+" Folders/ "+foldersent.Folders.get(i).NoteLinks.size()+" Notes.");
        }
        else {
            inflate = ((LayoutInflater) this.context.getSystemService("layout_inflater")).inflate(R.layout.folderframe, (ViewGroup) null);
            imageView = (ImageView) inflate.findViewById(R.id.iconimage);
            textView = (TextView) inflate.findViewById(R.id.textdata);


        }
        int[] iArr = this.images;
        if (i < iArr.length) {
            imageView.setImageResource(iArr[i]);
        } else {
            imageView.setImageBitmap(this.bitmapnew1.get(i - iArr.length));
        }
        textView.setText(this.name[i]);
        //Log.d("new333", "yes");
        if(numcolms>0) {
            lodedIndex.add(i);
            lodedViewssent.add(inflate);
        }

        return inflate;
    }


}
