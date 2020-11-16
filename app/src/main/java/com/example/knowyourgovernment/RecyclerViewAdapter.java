package com.example.knowyourgovernment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewViewHolder>{

    private static final String TAG = "RecyclerView Adapter";
    private List<Official> officialsList;
    private MainActivity mainAct;

    RecyclerViewAdapter(List<Official> officialsList, MainActivity ma) {
        this.officialsList = officialsList;
        mainAct = ma;
    }

    @NonNull
    @Override
    public RecyclerViewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d(TAG, "onCreateViewHolder: MAKING NEW RecyclerViewViewHolder");

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.political_official_list_row, parent, false);

        itemView.setOnClickListener(mainAct);
        itemView.setOnLongClickListener(mainAct);

        return new RecyclerViewViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder: FILLING VIEW HOLDER Official " + position);

        Official official = officialsList.get(position);

        holder.office.setText(official.getOfficeName());
        holder.name.setText(official.getName());
        holder.party.setText(official.getParty());
    }

    @Override
    public int getItemCount() {
        return officialsList.size();
    }
}
