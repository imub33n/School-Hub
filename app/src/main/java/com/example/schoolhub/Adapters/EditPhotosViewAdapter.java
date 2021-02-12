package com.example.schoolhub.Adapters;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class EditPhotosViewAdapter extends RecyclerView.Adapter<EditPhotosViewAdapter.EditPhotosViewHolder> {
//    public static
    @NonNull
    @Override
    public EditPhotosViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull EditPhotosViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class EditPhotosViewHolder extends RecyclerView.ViewHolder {
        public EditPhotosViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
