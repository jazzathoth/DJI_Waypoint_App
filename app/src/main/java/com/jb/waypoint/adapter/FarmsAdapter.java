package com.jb.waypoint.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.jb.waypoint.selectors.FieldSelect;
import com.jb.waypoint.R;
import com.jb.waypoint.model.Farms;

import java.util.ArrayList;

public class FarmsAdapter extends RecyclerView.Adapter<FarmsAdapter.FarmsViewHolder> {

    private ArrayList<Farms> farmsArrayList;

    private Context contextFA;
    String orgId;

    public FarmsAdapter(ArrayList<Farms> farmsArrayList, String o, Context c) {
        this.farmsArrayList = farmsArrayList;
        orgId = o;
        contextFA = c;
    }

    @NonNull
    @Override
    public FarmsAdapter.FarmsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.single_row_field_selector, parent, false);
        return new FarmsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FarmsAdapter.FarmsViewHolder holder, int position) {
        holder.txtFarmTitle.setText(farmsArrayList.get(position).getName());
        holder.txtFarmDescription.setText(farmsArrayList.get(position).getId());

        holder.itemView.setOnClickListener(v -> {
            Intent intentFieldSelect = new Intent(contextFA, FieldSelect.class);
            intentFieldSelect.putExtra("org_id", orgId);
            intentFieldSelect.putExtra("farm_id", farmsArrayList.get(position).getId());

            v.getContext().startActivity(intentFieldSelect);
        });

    }

    @Override
    public int getItemCount() {
        return farmsArrayList.size();
    }

    class  FarmsViewHolder extends RecyclerView.ViewHolder {
        TextView txtFarmTitle, txtFarmDescription;

        FarmsViewHolder(View itemView) {
            super(itemView);
            txtFarmTitle = itemView.findViewById(R.id.text_single_row_fs_title);
            txtFarmDescription = itemView.findViewById(R.id.text_single_row_fs_description);
            itemView.findViewById(R.id.text_fs_content_org).setVisibility(View.GONE);
            itemView.findViewById(R.id.text_fs_content_client).setVisibility(View.GONE);
            itemView.findViewById(R.id.text_fs_content_farm).setVisibility(View.GONE);
            itemView.findViewById(R.id.text_fs_content_field).setVisibility(View.GONE);
        }
    }
}
