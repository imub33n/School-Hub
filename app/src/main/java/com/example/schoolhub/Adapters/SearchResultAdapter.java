package com.example.schoolhub.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.schoolhub.R;
import com.example.schoolhub.data.SchoolData;

import java.util.List;

public class SearchResultAdapter extends RecyclerView.Adapter<SearchResultAdapter.ViewHolder>  {
    List<SchoolData> schoolData;
    Context context;
    public SearchResultAdapter(List<SchoolData> schoolData, Context applicationContext) {
        this.schoolData=schoolData;
        this.context=applicationContext;
    }

    @NonNull
    @Override
    public SearchResultAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_search_result,parent,false);
        SearchResultAdapter.ViewHolder viewHolder = new SearchResultAdapter.ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull SearchResultAdapter.ViewHolder holder, int position) {
        holder.nameSkol.setText(schoolData.get(position).getSchoolName());
        holder.addressSkol.setText(schoolData.get(position).getSchoolAddress());
    }

    @Override
    public int getItemCount() {
        return schoolData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView nameSkol,addressSkol;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.nameSkol=itemView.findViewById(R.id.nameSkol);
            this.addressSkol=itemView.findViewById(R.id.addressSkol);
        }
    }
}
