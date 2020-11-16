package com.example.knowyourgovernment;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, View.OnLongClickListener {

    private final List<Official> officialsList = new ArrayList<>();  // Main content is here

    private RecyclerView recyclerView; // Layout's recyclerview

    private RecyclerViewAdapter mAdapter; // Data to recyclerview adapter

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recycler);
        mAdapter = new RecyclerViewAdapter(officialsList, this);
        recyclerView.setAdapter(mAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        //Make some data - not always needed - just used to fill list
        for (int i = 0; i < 20; i++) {
            Official dummy = new Official();
            dummy.setOfficeName("Office " + (i + 1));
            dummy.setParty("Party " + (i + 1));
            dummy.setName("Name " + (i + 1));
            officialsList.add(dummy);
        }
    }

    @Override
    public void onClick(View v) {
        int pos = recyclerView.getChildLayoutPosition(v);
        Official o = officialsList.get(pos);
        Toast.makeText(v.getContext(), "SHORT " + o.getName(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onLongClick(View v) {
        int pos = recyclerView.getChildLayoutPosition(v);
        Official o = officialsList.get(pos);
        Toast.makeText(v.getContext(), "LONG " + o.getName(), Toast.LENGTH_SHORT).show();
        return false;
    }
}