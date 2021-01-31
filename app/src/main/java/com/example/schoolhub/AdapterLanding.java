package com.example.schoolhub;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.schoolhub.data.SchoolsLandingModel;

import java.util.ArrayList;
import java.util.List;

public class AdapterLanding extends RecyclerView.Adapter<AdapterLanding.ViewHolder>{
    private ArrayList<SchoolsLandingModel> models= new ArrayList<>();
    //private LayoutInflater layoutInflater;
    private Context context;
    public AdapterLanding(ArrayList<SchoolsLandingModel> models, Context context) {
        this.models = models;
        this.context = context;

    }
    @NonNull
    @Override
    public AdapterLanding.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_layout,parent,false);
        AdapterLanding.ViewHolder viewHolder = new AdapterLanding.ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterLanding.ViewHolder holder, int position) {
        holder.imageView.setImageResource(models.get(position).getImage());
        holder.title.setText(models.get(position).getTitle());
        holder.desc.setText(models.get(position).getDesc());

    }

    @Override
    public int getItemCount() {
        return models.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageView;
        public TextView title, desc;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.imageView = itemView.findViewById(R.id.image);
            this.title = itemView.findViewById(R.id.title);
            this.desc = itemView.findViewById(R.id.desc);
        }
    }
}
//<androidx.viewpager2.widget.ViewPager2
//        android:id="@+id/schoolsSliderLandingPage"
//        android:layout_width="match_parent"
//        android:layout_height="wrap_content"
//        android:padding="10dp"
//        android:paddingLeft="15dp"
//        android:paddingRight="80dp"
//        android:orientation="horizontal"
//        />