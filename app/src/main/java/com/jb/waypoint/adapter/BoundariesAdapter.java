package com.jb.waypoint.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.jb.waypoint.R;
import com.jb.waypoint.model.Boundaries;
import com.jb.waypoint.selectors.SelectorState;

import java.nio.channels.SelectableChannel;
import java.util.ArrayList;

public class BoundariesAdapter extends RecyclerView.Adapter<BoundariesAdapter.BoundariesViewHolder> {

    private ArrayList<Boundaries> boundariesArrayList;

    SelectorState selectorState = SelectorState.getInstance();

    private Context contextBA;
    String orgId, fieldId;

    public BoundariesAdapter(ArrayList<Boundaries> boundariesArrayList, String o, String f, Context c) {
        this.boundariesArrayList = boundariesArrayList;
        orgId = o;
        fieldId = f;
        contextBA = c;
    }

    @NonNull
    @Override
    public BoundariesAdapter.BoundariesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.single_row_field_selector, parent, false);
        return new BoundariesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BoundariesAdapter.BoundariesViewHolder holder, int position) {
        holder.txtBoundaryTitle.setText(boundariesArrayList.get(position).getName());
        holder.txtBoundaryDescription.setText(boundariesArrayList.get(position).getCreated());

        holder.itemView.setOnClickListener(v -> {
            selectorState.setBorderId(boundariesArrayList.get(position).getId());
            selectorState.setBorderName(boundariesArrayList.get(position).getName());
        });
    }

    @Override
    public int getItemCount() {
        return boundariesArrayList.size();
    }

    class BoundariesViewHolder extends RecyclerView.ViewHolder {
        TextView txtBoundaryTitle, txtBoundaryDescription;

        BoundariesViewHolder(View itemView) {
            super(itemView);
            txtBoundaryTitle = itemView.findViewById(R.id.text_single_row_fs_title);
            txtBoundaryDescription = itemView.findViewById(R.id.text_single_row_fs_description);
        }
    }
}
