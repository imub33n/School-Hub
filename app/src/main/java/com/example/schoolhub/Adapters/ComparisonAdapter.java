package com.example.schoolhub.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.schoolhub.R;
import com.example.schoolhub.data.SchoolData;

import java.util.List;

public class ComparisonAdapter extends RecyclerView.Adapter<ComparisonAdapter.ViewHolder>{
    List<SchoolData> schoolData;

    public ComparisonAdapter(List<SchoolData> comparisonSchools) {
        this.schoolData=comparisonSchools;
    }

    @NonNull
    @Override
    public ComparisonAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.list_compare_schools,parent,false);
        ComparisonAdapter.ViewHolder viewHolder = new ComparisonAdapter.ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ComparisonAdapter.ViewHolder holder, int position) {
        holder.noSkol.setText(String.valueOf(position+1));
        holder.nameSkol.setText(schoolData.get(position).getSchoolName());
    }

    @Override
    public int getItemCount() {
        return schoolData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView noSkol,nameSkol;
        ImageView cancelAttachment;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.nameSkol=itemView.findViewById(R.id.nameSkol);
            this.noSkol=itemView.findViewById(R.id.noSkol);
            this.cancelAttachment=itemView.findViewById(R.id.cancelAttachment);
            cancelAttachment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    schoolData.remove(pos);
                    notifyDataSetChanged();
                }
            });
        }
    }
}
