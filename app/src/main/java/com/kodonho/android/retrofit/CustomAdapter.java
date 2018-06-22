package com.kodonho.android.retrofit;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.Holder>{
    List<GitUser> data = new ArrayList<>();
    MainActivity mainActivity;

    public CustomAdapter(MainActivity mainActivity){
        this.mainActivity = mainActivity;
    }

    public void setDataAndRefresh(List<GitUser> data){
        this.data = data;
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
            .inflate(R.layout.list_item, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        GitUser gitUser = data.get(position);
        holder.textLogin.setText(gitUser.getLogin());
        holder.textNode.setText(gitUser.getNode_id());
        holder.gitUser = gitUser;
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class Holder extends RecyclerView.ViewHolder{
        TextView textLogin, textNode;
        GitUser gitUser;
        public Holder(View itemView) {
            super(itemView);
            textLogin = itemView.findViewById(R.id.textLogin);
            textNode = itemView.findViewById(R.id.textNode);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mainActivity.setUser(gitUser.getLogin());
                }
            });
        }
    }
}
