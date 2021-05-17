package com.example.schoolhub.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.schoolhub.R;
import com.example.schoolhub.data.FacultyRequest;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class RequestFacultyAdapter extends RecyclerView.Adapter<RequestFacultyAdapter.ViewHolder>{
    List<FacultyRequest> facultyRequests;
    Context context;

    public RequestFacultyAdapter(List<FacultyRequest> body, Context context) {
        this.facultyRequests=body;
        this.context=context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_faculty_requests,parent,false);
        RequestFacultyAdapter.ViewHolder viewHolder = new RequestFacultyAdapter.ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.nameFaculty.setText(facultyRequests.get(position).getTeacherName());
        holder.depFaculity.setText(facultyRequests.get(position).getStatusRequest());

    }

    @Override
    public int getItemCount() {
        return facultyRequests.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CircleImageView imageFaculty;
        TextView depFaculity,nameFaculty;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nameFaculty=itemView.findViewById(R.id.nameFaculty);
            depFaculity=itemView.findViewById(R.id.depFaculity);
            imageFaculty=itemView.findViewById(R.id.imageFaculty);
        }
    }
}
