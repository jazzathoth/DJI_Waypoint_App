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
import com.jb.waypoint.model.ClientsValues;
import com.jb.waypoint.selectors.FarmSelect;

import java.util.ArrayList;

public class ClientsAdapter extends RecyclerView.Adapter<ClientsAdapter.ClientsViewHolder> {
    private ArrayList<ClientsValues> clientsValuesArrayList;

    private Context contextCA;
    String orgId, orgName;

    public ClientsAdapter(ArrayList<ClientsValues> clientsValuesArrayList,
                          String oid,
                          String on,
                          Context c){
        contextCA = c;
        orgId = oid;
        orgName = on;
        this.clientsValuesArrayList = clientsValuesArrayList;
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
        holder.txtClientTitle.setText(clientsValuesArrayList.get(position).getName());
        holder.txtClientDescription.setText(clientsValuesArrayList.get(position).getId());
        holder.txtOrgName.setText(orgName);

        holder.itemView.setOnClickListener(v -> {
            Intent intentFarmSelect = new Intent(contextCA, FarmSelect.class);
            intentFarmSelect.putExtra("org_id", orgId);
            intentFarmSelect.putExtra("org_name", orgName);
            intentFarmSelect.putExtra("client_id",
                    clientsValuesArrayList.get(position).getId());
            intentFarmSelect.putExtra("client_name",
                    clientsValuesArrayList.get(position).getName());
            v.getContext().startActivity(intentFarmSelect);
        });
    }

    @Override
    public int getItemCount() {
        return clientsValuesArrayList.size();
    }


    class ClientsViewHolder extends RecyclerView.ViewHolder {
        TextView txtClientTitle, txtClientDescription, txtOrgName;

        ClientsViewHolder(View itemView) {
            super(itemView);
            txtClientTitle = itemView.findViewById(R.id.text_single_row_fs_title);
            txtClientDescription = itemView.findViewById(R.id.text_single_row_fs_description);
            txtOrgName = itemView.findViewById(R.id.text_fs_content_org);
            itemView.findViewById(R.id.text_fs_content_org).setVisibility(View.VISIBLE);
            itemView.findViewById(R.id.text_fs_content_client).setVisibility(View.GONE);
            itemView.findViewById(R.id.text_fs_content_farm).setVisibility(View.GONE);
            itemView.findViewById(R.id.text_fs_content_field).setVisibility(View.GONE);
        }
    }
}