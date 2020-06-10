package com.jb.waypoint.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.jb.waypoint.R;
import com.jb.waypoint.model.Fields;
import com.jb.waypoint.selectors.BoundarySelect;
import com.jb.waypoint.selectors.FarmSelect;

import java.util.ArrayList;

public class FieldsAdapter extends RecyclerView.Adapter<FieldsAdapter.FieldsViewHolder> {
    private ArrayList<Fields> fieldsArrayList;

    private Context contextFA;
    String orgId;
    String farmId;

    public FieldsAdapter(ArrayList<Fields> fieldsArrayList, String o, String f, Context c) {
        contextFA = c;
        orgId = o;
        farmId = f;
        this.fieldsArrayList = fieldsArrayList;
    }

    @NonNull
    @Override
    public  FieldsAdapter.FieldsViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
                                                              int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.single_row_field_selector, parent, false);
        return new FieldsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(FieldsViewHolder holder, int position) {
        holder.txtFieldTitle.setText(fieldsArrayList.get(position).getName());
        holder.txtFieldDescription.setText(fieldsArrayList.get(position).getId());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentBoundarySelect = new Intent(contextFA, BoundarySelect.class);
                intentBoundarySelect.putExtra("org_id", orgId);
                intentBoundarySelect.putExtra("field_id", fieldsArrayList.get(position).getId());

                v.getContext().startActivity(intentBoundarySelect);
            }
        });
    }

    @Override
    public int getItemCount() {
        return fieldsArrayList.size();
    }

    class FieldsViewHolder extends RecyclerView.ViewHolder {
        TextView txtFieldTitle, txtFieldDescription;

        public FieldsViewHolder(View itemView) {
            super(itemView);
            txtFieldTitle = itemView.findViewById(R.id.text_single_row_fs_title);
            txtFieldDescription = itemView.findViewById(R.id.text_single_row_description);
        }
    }
}
