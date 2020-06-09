package com.jb.waypoint.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.jb.waypoint.FieldSelect;
import com.jb.waypoint.R;
import com.jb.waypoint.model.Clients;

import java.util.ArrayList;

public class ClientsAdapter extends RecyclerView.Adapter<ClientsAdapter.ClientsViewHolder> {
    private ArrayList<Clients> clientsArrayList;

    private Context context;
    String orgId;

    public ClientsAdapter(ArrayList<Clients> clientsArrayList, String o, Context c){
        context = c;
        orgId = o;
        this.clientsArrayList = clientsArrayList;
    }

    @NonNull
    @Override
    public ClientsAdapter.ClientsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.single_row_field_selector, parent, false);
        return new ClientsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ClientsViewHolder holder, int position) {
        holder.txtClientTitle.setText(clientsArrayList.get(position).getName());
        holder.txtClientDescription.setText(clientsArrayList.get(position).getId());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentFarmSelect = new Intent(context, FieldSelect.class);
                intentFarmSelect.putExtra("org_id", orgId);
                intentFarmSelect.putExtra("client_id",
                        clientsArrayList.get(position).getId());
                v.getContext().startActivity(intentFarmSelect);
            }
        });
    }

    @Override
    public int getItemCount() {
        return clientsArrayList.size();
    }


    class ClientsViewHolder extends RecyclerView.ViewHolder {
        TextView txtClientTitle, txtClientDescription;

        ClientsViewHolder(View itemView) {
            super(itemView);
            txtClientTitle = itemView.findViewById(R.id.text_single_row_fs_title);
            txtClientDescription = itemView.findViewById(R.id.text_single_row_description);
        }
    }
}