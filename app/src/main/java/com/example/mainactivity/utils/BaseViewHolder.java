package com.example.mainactivity.utils;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public abstract class BaseViewHolder extends RecyclerView.ViewHolder {

    private int currentPosition;

    public BaseViewHolder(View itemView) {
        super(itemView);
    }

    protected abstract void clear();

    public void onBind(int position)
    {
        currentPosition = position;
        clear();
    }

    public int getCurrentPosition()
    {
        return currentPosition;
    }
}
