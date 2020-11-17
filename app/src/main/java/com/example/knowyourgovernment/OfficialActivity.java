package com.example.knowyourgovernment;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

public class OfficialActivity extends AppCompatActivity {

    private Official official;
    private TextView officialLocation;
    private TextView officialOffice;
    private TextView officialName;
    private TextView officialParty;
    private TextView officialAddress;
    private TextView officialPhone;
    private TextView officialEmail;
    private TextView officialWebsite;
    private View officialLayout;
    private View partyImage;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_official);

        Intent intent = getIntent();
        if (intent.hasExtra("Official")) {
            official = (Official) intent.getSerializableExtra("Official");
            updateTextViews(official);
            updatePartyBackGroundAndIcon(official);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void updatePartyBackGroundAndIcon(Official official) {
        officialLayout = findViewById(R.id.officialConstraintLayout);
        partyImage = findViewById(R.id.partyImage);
        if(official.getParty().contentEquals("Republican")) {
            partyImage.setBackgroundResource(R.drawable.rep_logo);
            officialLayout.setBackgroundColor(getColor(android.R.color.holo_red_light));
        } else if (official.getParty().contentEquals("Democratic")) {
            partyImage.setBackgroundResource(R.drawable.dem_logo);
            officialLayout.setBackgroundColor(getColor(android.R.color.holo_blue_light));
        } else {
            officialLayout.setBackgroundColor(getColor(android.R.color.holo_purple));
        }
    }

    private void updateTextViews(Official official) {

        officialLocation = findViewById(R.id.officialLocationData);
        officialOffice = findViewById(R.id.officialOffice);
        officialName = findViewById(R.id.officialName);
        officialParty = findViewById(R.id.officialParty);
        officialAddress = findViewById(R.id.address);
        officialPhone = findViewById(R.id.phone);
        officialEmail = findViewById(R.id.email);
        officialWebsite = findViewById(R.id.website);

        if (official != null) {
            if (official.getAddress() != null) {
                String addrLine = official.getAddress();
                String[] allAddr = addrLine.split(",");
                officialLocation.setText(allAddr[1] + ", " + allAddr[2]);
            }
            if (official.getOfficeName() != null) officialOffice.setText(official.getOfficeName());
            if (official.getName() != null) officialName.setText(official.getName());
            if (official.getParty() != null) officialParty.setText(official.getParty());
            if (official.getAddress() != null) officialAddress.setText(official.getAddress());
            if (official.getPhones() != null) officialPhone.setText(official.getPhones());
            if (official.getEmails() != null) officialEmail.setText(official.getEmails());
            if (official.getUrls() != null) officialWebsite.setText(official.getUrls());
        }

    }

}
