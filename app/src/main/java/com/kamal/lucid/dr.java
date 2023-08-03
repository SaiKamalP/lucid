package com.kamal.lucid;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import androidx.appcompat.app.AppCompatActivity;
import java.io.IOException;

/* loaded from: classes.dex */
public class dr extends AppCompatActivity {
    Button brb;
    Button bstroke;
    Button bub;
    Button m1;
    Button m2;
    Button m3;
    Button m4;
    Mycanvas1 mycanv;
    SeekBar skb;
    View v2;

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // androidx.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.dr);
        this.mycanv = (Mycanvas1) findViewById(R.id.view3);
        SeekBar seekBar = (SeekBar) findViewById(R.id.strokebar);
        this.skb = seekBar;
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() { // from class: com.kamal.lucid.dr.1
            @Override // android.widget.SeekBar.OnSeekBarChangeListener
            public void onStartTrackingTouch(SeekBar seekBar2) {
            }

            @Override // android.widget.SeekBar.OnSeekBarChangeListener
            public void onStopTrackingTouch(SeekBar seekBar2) {
            }

            @Override // android.widget.SeekBar.OnSeekBarChangeListener
            public void onProgressChanged(SeekBar seekBar2, int i, boolean z) {
                dr.this.mycanv.pen1.size = ((float) ((i * 20) / 100)) + 1.0f;
            }
        });
        this.m1 = (Button) findViewById(R.id.m1);
        this.m2 = (Button) findViewById(R.id.m2);
        this.m3 = (Button) findViewById(R.id.m3);
        this.m4 = (Button) findViewById(R.id.m4);
        this.bub = (Button) findViewById(R.id.bub);
        this.brb = (Button) findViewById(R.id.brb);
        this.bstroke = (Button) findViewById(R.id.bstroke);
        this.m1.setBackgroundColor(Color.rgb(this.mycanv.pen1.color[0], this.mycanv.pen1.color[1], this.mycanv.pen1.color[2]));
        this.m2.setBackgroundColor(Color.rgb(this.mycanv.pen1.color[3], this.mycanv.pen1.color[4], this.mycanv.pen1.color[5]));
        this.m3.setBackgroundColor(Color.rgb(this.mycanv.pen1.color[6], this.mycanv.pen1.color[7], this.mycanv.pen1.color[8]));
        m1clicked(this.mycanv);
    }

    public void m1clicked(View view) {
        if (this.m2.getVisibility() == View.VISIBLE) {
            this.m2.setVisibility(View.INVISIBLE);;
            this.m3.setVisibility(View.INVISIBLE);;
            this.m4.setVisibility(View.INVISIBLE);;
            this.bub.setVisibility(View.INVISIBLE);;
            this.brb.setVisibility(View.INVISIBLE);;
            this.bstroke.setVisibility(View.INVISIBLE);;
            this.skb.setVisibility(View.INVISIBLE);;
            this.m2.setEnabled(false);
            this.m3.setEnabled(false);
            this.m4.setEnabled(false);
            this.bub.setEnabled(false);
            this.brb.setEnabled(false);
            this.bstroke.setEnabled(false);
            this.skb.setEnabled(false);
            return;
        }
        this.m2.setVisibility(View.VISIBLE);;
        this.m3.setVisibility(View.VISIBLE);;
        this.m4.setVisibility(View.VISIBLE);;
        this.bub.setVisibility(View.VISIBLE);;
        this.brb.setVisibility(View.VISIBLE);;
        this.bstroke.setVisibility(View.VISIBLE);;
        this.m2.setEnabled(true);
        this.m3.setEnabled(true);
        this.m4.setEnabled(true);
        this.bub.setEnabled(true);
        this.brb.setEnabled(true);
        this.bstroke.setEnabled(true);
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
            return;
        }
        this.skb.setVisibility(View.VISIBLE);;
        this.skb.setEnabled(true);
    }

    public void pagedown(View view) throws CloneNotSupportedException {
        this.mycanv.pagedown2();
    }

    public void pageup(View view) throws IOException, CloneNotSupportedException {
        this.mycanv.pageup2();
    }

    public void ActivateEraser(View view) {
        this.mycanv.pen1.mode = 1 - this.mycanv.pen1.mode;
    }
}
