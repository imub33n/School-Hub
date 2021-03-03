package com.example.schoolhub.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.schoolhub.LandingScreen;
import com.example.schoolhub.R;
import com.example.schoolhub.SchoolDetails;
import com.example.schoolhub.data.SchoolData;

import java.util.List;

public class SearchResultAdapter extends RecyclerView.Adapter<SearchResultAdapter.ViewHolder>  {
    List<SchoolData> schoolData;
    Context context;
    public static String userIDsearch="";
    public static String skolNameSearch="";
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
        holder.search_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userIDsearch=schoolData.get(position).getAdminID();
                skolNameSearch=schoolData.get(position).getSchoolName();
                Intent it = new Intent( context, SchoolDetails.class);
                it.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(it);
            }
        });
    }

    @Override
    public int getItemCount() {
        return schoolData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView nameSkol,addressSkol;
        public LinearLayout search_item;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.nameSkol=itemView.findViewById(R.id.nameSkol);
            this.addressSkol=itemView.findViewById(R.id.addressSkol);
            this.search_item=itemView.findViewById(R.id.search_item);
        }
    }
}
