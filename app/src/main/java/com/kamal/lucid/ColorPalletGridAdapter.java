package com.kamal.lucid;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;

public class ColorPalletGridAdapter extends BaseAdapter {
    ArrayList<Integer> colorsToDisplay;
    Context context;
    @Override
    public int getCount() {
        return colorsToDisplay.size();
    }

    public ColorPalletGridAdapter(Context context2, ArrayList<Integer> colorsToDisplay2) {
        colorsToDisplay = colorsToDisplay2;
        context=context2;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        @SuppressLint("ViewHolder") View inflated=((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.single_color_pallet_color_layout,null);

        inflated.setBackgroundResource(R.drawable.custom_pallet_color_background);
        ((GradientDrawable) inflated.getBackground()).setColor(colorsToDisplay.get(i));
        Log.d("colorreseved","color is: "+colorsToDisplay.get(i));
        return inflated;
    }
}
