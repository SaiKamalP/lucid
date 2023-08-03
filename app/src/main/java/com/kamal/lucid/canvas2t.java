package com.kamal.lucid;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

/* loaded from: classes.dex */
public class canvas2t extends View {
    public Path path = new Path();
    public Paint paint = new Paint();
    public Canvas C4 = new Canvas();

    public canvas2t(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.paint.setAntiAlias(true);
        this.paint.setStrokeJoin(Paint.Join.ROUND);
        this.paint.setStyle(Paint.Style.STROKE);
        this.paint.setStrokeCap(Paint.Cap.ROUND);
    }

    @Override // android.view.View
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        this.C4 = canvas;
        canvas.drawPath(this.path, this.paint);
    }
}
