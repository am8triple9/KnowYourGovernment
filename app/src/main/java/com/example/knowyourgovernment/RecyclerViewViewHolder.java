package com.example.knowyourgovernment;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class RecyclerViewViewHolder extends RecyclerView.ViewHolder {

    public TextView office;
    TextView name;
    TextView party;


    public RecyclerViewViewHolder(@NonNull View itemView) {
        super(itemView);
        office = itemView.findViewById(R.id.office);
        name = itemView.findViewById(R.id.name);
        party = itemView.findViewById(R.id.party);
    }
}
