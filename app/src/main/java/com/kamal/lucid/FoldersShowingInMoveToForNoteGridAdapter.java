package com.kamal.lucid;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class FoldersShowingInMoveToForNoteGridAdapter extends BaseAdapter {
    ArrayList<String> namesOfFolders;
    Context contxt;
    @Override
    public int getCount() {
        return namesOfFolders.size();
    }
    FoldersShowingInMoveToForNoteGridAdapter(Context context2,AFolder FolderToDisplay){
        contxt=context2;
        namesOfFolders=new ArrayList<>();
        namesOfFolders.add("...");
        for (int i = 0; i < FolderToDisplay.Folders.size(); i++) {
            namesOfFolders.add(FolderToDisplay.Folders.get(i).FName);
        }
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
        @SuppressLint({"WrongConstant", "ViewHolder"}) View inflate=((LayoutInflater)contxt.getSystemService("layout_inflater")).inflate(R.layout.single_text_display,null);
        TextView txtview=inflate.findViewById(R.id.SingleTextText);
        txtview.setText(" "+namesOfFolders.get(i)+" ");
        return inflate;
    }
}
