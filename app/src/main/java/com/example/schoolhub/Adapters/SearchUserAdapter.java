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

import com.example.schoolhub.R;
import com.example.schoolhub.UserProfile;
import com.example.schoolhub.data.LoginResult;

import java.util.List;

public class SearchUserAdapter extends RecyclerView.Adapter<SearchUserAdapter.ViewHolder>{
    List<LoginResult> loginResults;
    Context context;
    @NonNull
    @Override
    public SearchUserAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.frag_user_search_result,parent,false);
        SearchUserAdapter.ViewHolder vH = new SearchUserAdapter.ViewHolder(v);
        return vH;
    }

    @Override
    public void onBindViewHolder(@NonNull SearchUserAdapter.ViewHolder holder, int position) {
        holder.userName.setText(loginResults.get(position).getUsername());
        holder.userType.setText(loginResults.get(position).getType());
        holder.search_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View vee) {
                Intent it = new Intent( context, UserProfile.class);
                it.putExtra("EXTRA_USER_ID",loginResults.get(position).getUserID() );
                it.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(it);
            }
        });
    }

    @Override
    public int getItemCount() {
        return loginResults.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView userType,userName;
        public LinearLayout search_item;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.userName=itemView.findViewById(R.id.userName);
            this.userType=itemView.findViewById(R.id.userType);
            this.search_item=itemView.findViewById(R.id.search_item);
        }
    }
}
