package com.example.multiplechoiceexam.Teacher.ExamsOffline.student;

import android.graphics.Canvas;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

public class SwipeToStudentCallback extends ItemTouchHelper.SimpleCallback {
    private final StudentAdapter adapter;
    private final RecyclerView recyclerView;


    public SwipeToStudentCallback(StudentAdapter adapter, RecyclerView recyclerView) {
        super(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT);
        this.adapter = adapter;
        this.recyclerView = recyclerView;
    }
    @Override
    public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
        int swipeFlags = ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;
        return makeMovementFlags(0, swipeFlags);
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
        return false;
    }
    @Override
    public float getSwipeThreshold(@NonNull RecyclerView.ViewHolder viewHolder) {
        return 0.5f;
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
        int position = viewHolder.getAdapterPosition();
        if (direction == ItemTouchHelper.LEFT) {
//            showMessageIcon(position);
            adapter.deleteItem(position);

        } else if (direction == ItemTouchHelper.RIGHT) {
//            showCallIcon(position);
            adapter.updateItem(position);

        }
    }


    @Override
    public void onChildDraw(
            @NonNull Canvas c,
            @NonNull RecyclerView recyclerView,
            @NonNull RecyclerView.ViewHolder viewHolder,
            float dX,
            float dY,
            int actionState,
            boolean isCurrentlyActive) {

        if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
            // Hiển thị và ẩn icon tùy thuộc vào hướng vuốt
            int direction = dX > 0 ? ItemTouchHelper.RIGHT : ItemTouchHelper.LEFT;
            StudentAdapter.StudentViewHolder holder = (StudentAdapter.StudentViewHolder) viewHolder;

            if (direction == ItemTouchHelper.LEFT) {
                holder.imageDelete.setVisibility(View.VISIBLE);
                holder.imageEdit.setVisibility(View.INVISIBLE);
            } else if (direction == ItemTouchHelper.RIGHT) {
                holder.imageDelete.setVisibility(View.INVISIBLE);
                holder.imageEdit.setVisibility(View.VISIBLE);
            }

            // Nếu không được đẩy, ẩn cả hai icon
            if (dX == 0) {
                holder.imageDelete.setVisibility(View.INVISIBLE);
                holder.imageEdit.setVisibility(View.INVISIBLE);
            }
        }

        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
    }





}
