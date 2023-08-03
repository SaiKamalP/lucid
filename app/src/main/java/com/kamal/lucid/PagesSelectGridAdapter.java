package com.kamal.lucid;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.LabeledIntent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class PagesSelectGridAdapter extends BaseAdapter {
    public ArrayList<Integer> Pages;
    Context contxt;
    public ArrayList<View> views;
    public ArrayList<Integer>selectedpages;

    @Override
    public int getCount() {
        return Pages.size();
    }
    PagesSelectGridAdapter(Context context,int Pages_count,ArrayList<Integer> selpages){
        contxt=context;
        Pages=new ArrayList<>();
        for (int i = 1; i < Pages_count+1; i++) {
            Pages.add(i);
        }
        selectedpages=selpages;
    }
    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }



    @SuppressLint("SetTextI18n")
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        @SuppressLint("WrongConstant") View inflate=((LayoutInflater)contxt.getSystemService("layout_inflater")).inflate(R.layout.single_page_number_view,null);
        TextView txtview=inflate.findViewById(R.id.NumberDisplay);
        txtview.setText(" "+Pages.get(i)+" ");
        if(selectedpages.contains(i)){
            inflate.findViewById(R.id.layoutpagenumber).setBackgroundResource(R.drawable.custombuttontheme2);
        }
        return inflate;
    }

}
