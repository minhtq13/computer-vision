package com.example.multiplechoiceexam.Teacher.ExamsOffline.savedbscoring;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.multiplechoiceexam.R;

public class RecyclerViewItemDecoration extends RecyclerView.ItemDecoration {
    private final Paint paint;
    private final int dividerHeight;

    public RecyclerViewItemDecoration(Context context, int dividerHeight) {
        this.dividerHeight = dividerHeight;
        paint = new Paint();
        paint.setColor(ContextCompat.getColor(context, R.color.circleBorder));
        paint.setStrokeWidth(dividerHeight);
    }

    @Override
    public void onDrawOver(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = parent.getChildAt(i);

            int top = child.getBottom();
            int bottom = top + dividerHeight;

            c.drawLine(child.getLeft(), top, child.getRight(), bottom, paint);
        }
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        outRect.bottom = dividerHeight;
    }
}

