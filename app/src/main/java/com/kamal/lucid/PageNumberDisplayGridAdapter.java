package com.kamal.lucid;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class PageNumberDisplayGridAdapter extends BaseAdapter {
    ArrayList<Integer> pageNymbersArray;
    Context context;
    int Current_page;
    ArrayList<Integer> impPages;
    int lastmovepagenum;
    @Override
    public int getCount() {
        return pageNymbersArray.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }
    public PageNumberDisplayGridAdapter(Context context2,int TotalNumberOfNumbers,int Currentpage,ArrayList<Integer> imppagesarray,int lastmovedpage){
        pageNymbersArray=new ArrayList<>();
        impPages=new ArrayList<>();
        impPages=imppagesarray;
        context=context2;
        Current_page=Currentpage;
        for (int i = 1; i <= TotalNumberOfNumbers; i++) {
            pageNymbersArray.add(i);
        }
        lastmovepagenum=lastmovedpage;
    }
    @Override
    public long getItemId(int i) {
        return 0;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        @SuppressLint({"WrongConstant"}) View inflate=((LayoutInflater) this.context.getSystemService("layout_inflater")).inflate(R.layout.single_page_number_view,null);
        TextView textView=inflate.findViewById(R.id.NumberDisplay);
        textView.setText(" "+pageNymbersArray.get(i)+" ");
        if(impPages.contains(i)){
            View lt=inflate.findViewById(R.id.layoutpagenumber);
            lt.setBackgroundResource(R.drawable.custombuttontheme3);

        }
        if(i==lastmovepagenum){
            View lt=inflate.findViewById(R.id.layoutpagenumber);
            lt.setBackgroundResource(R.drawable.custombuttontheme4);

        }
        if(i==Current_page){
            View lt=inflate.findViewById(R.id.layoutpagenumber);
            lt.setBackgroundResource(R.drawable.custombuttontheme2);
        }
        return inflate;
    }
}
