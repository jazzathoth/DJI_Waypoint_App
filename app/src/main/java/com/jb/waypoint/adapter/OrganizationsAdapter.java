package com.jb.waypoint.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.jb.waypoint.R;
import com.jb.waypoint.model.Organizations;

import java.util.ArrayList;

public class OrganizationsAdapter extends RecyclerView.Adapter<OrganizationsAdapter.OrganizationsViewHolder> {
    private ArrayList<Organizations> organizationsArrayList;

    public OrganizationsAdapter(ArrayList<Organizations> organizationsArrayList){
        this.organizationsArrayList = organizationsArrayList;
    }

    @NonNull
    @Override
    public OrganizationsAdapter.OrganizationsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.single_row_field_selector, parent, false);
        return new OrganizationsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(OrganizationsViewHolder holder, int position) {
        holder.txtOrgTitle.setText(organizationsArrayList.get(position).getName());
        holder.txtOrgDescription.setText(organizationsArrayList.get(position).getId());
    }

    @Override
    public int getItemCount() {
        return organizationsArrayList.size();
    }


    class OrganizationsViewHolder extends RecyclerView.ViewHolder {
        TextView txtOrgTitle, txtOrgDescription;

        OrganizationsViewHolder(View itemView) {
            super(itemView);
            txtOrgTitle = itemView.findViewById(R.id.text_single_row_fs_title);
            txtOrgDescription = itemView.findViewById(R.id.text_single_row_description);
        }
    }
}
