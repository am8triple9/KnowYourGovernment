package com.example.knowyourgovernment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static android.content.pm.PackageManager.PERMISSION_GRANTED;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, View.OnLongClickListener {

    private final List<Official> officialsList = new ArrayList<>();  // Main content is here
    private RecyclerView recyclerView; // Layout's recyclerview
    private RecyclerViewAdapter mAdapter; // Data to recyclerview adapter
    private static int MY_LOCATION_REQUEST_CODE_ID = 111;
    private LocationManager locationManager;
    private Criteria criteria;
    public String address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recycler);
        mAdapter = new RecyclerViewAdapter(officialsList, this);
        recyclerView.setAdapter(mAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        getDefaultLocation();

        //createDummyData();

    }

    private void createDummyData() {
        for (int i = 0; i < 20; i++) {
            Official dummy = new Official();
            dummy.setOfficeName("Office " + (i + 1));
            if ((i % 2 == 0)) {
                dummy.setParty("Republican");
            } else {
                dummy.setParty("Democratic");
            }
            dummy.setOfficialName("Name " + (i + 1));
            dummy.setAddress("1600 Pennsylvania Avenue NW, Washington, DC 20500 with Official Index " + (i + 1));
            dummy.setPhones("Phone" + (i + 1));
            dummy.setEmails("Email" + (i + 1));
            dummy.setUrls("Website" + (i + 1));
            officialsList.add(dummy);
        }
    }

    private void getDefaultLocation() {

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        criteria = new Criteria();

        // use gps for location
        // criteria.setPowerRequirement(Criteria.POWER_HIGH);
        // criteria.setAccuracy(Criteria.ACCURACY_FINE);

        // use network for location
        criteria.setPowerRequirement(Criteria.POWER_LOW);
        criteria.setAccuracy(Criteria.ACCURACY_COARSE);

        criteria.setAltitudeRequired(false);
        criteria.setBearingRequired(false);
        criteria.setSpeedRequired(false);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    this,
                    new String[]{
                            Manifest.permission.ACCESS_FINE_LOCATION
                    },
                    MY_LOCATION_REQUEST_CODE_ID);
        } else {
            setLocation();
        }

    }

    @Override
    public void onRequestPermissionsResult(
            int requestCode, @NonNull
            String[] permissions, @NonNull
                    int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == MY_LOCATION_REQUEST_CODE_ID) {
            if (permissions[0].equals(Manifest.permission.ACCESS_FINE_LOCATION) &&
                    grantResults[0] == PERMISSION_GRANTED) {
                setLocation();
                return;
            }
        }
        ((TextView) findViewById(R.id.locationData)).setText("No Permission");

    }

    @SuppressLint("MissingPermission")
    private void setLocation() {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());

        String bestProvider = locationManager.getBestProvider(criteria, true);
        //((TextView) findViewById(R.id.bestText)).setText(bestProvider);

        Location defaultLocation = null;
        if (bestProvider != null) {
            defaultLocation = locationManager.getLastKnownLocation(bestProvider);
        }
        if (defaultLocation != null) {
            double lat = defaultLocation.getLatitude();
            double lon = defaultLocation.getLongitude();
            List<Address> addresses;
            Address defaultAddress = null;
            try {
                addresses = geocoder.getFromLocation(lat, lon, 10);
                defaultAddress = addresses.get(0);
                if (defaultAddress != null) {
                    String addrLine = defaultAddress.getAddressLine(0);
                    String[] allAddr = addrLine.split(",");
                    ((TextView) findViewById(R.id.locationData)).setText(allAddr[1] + ", " + allAddr[2]);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            ((TextView) findViewById(R.id.locationData)).setText("No Data For Location");
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.action_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.search:
                showSearchDialog();
                Toast.makeText(MainActivity.this, "You want to do a search!", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.info:
                openAboutActivity();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void showSearchDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        final EditText et = new EditText(this);
        et.setInputType(InputType.TYPE_CLASS_TEXT);
        et.setGravity(Gravity.CENTER_HORIZONTAL);
        builder.setView(et);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                address = et.getText().toString();
                searchOfficialByLocation();
            }
        });
        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                Toast.makeText(MainActivity.this, "You changed your mind!", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setMessage("Enter a city, State or a Zip Code:");
        builder.setTitle("Search Officials by Location");

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void searchOfficialByLocation() {

        CivicLoaderRunnable civicLoaderRunnable = new CivicLoaderRunnable(this);
        new Thread(civicLoaderRunnable).start();

    }

    public void openAboutActivity() {
        Intent intent = new Intent(this, AboutActivity.class);
        startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        int pos = recyclerView.getChildLayoutPosition(v);
        Official o = officialsList.get(pos);
        Intent intent = new Intent(MainActivity.this, OfficialActivity.class);
        intent.putExtra("Official", o);
        startActivity(intent);
    }

    @Override
    public boolean onLongClick(View v) {
        int pos = recyclerView.getChildLayoutPosition(v);
        Official o = officialsList.get(pos);
        Toast.makeText(v.getContext(), "LONG " + o.getOfficeName(), Toast.LENGTH_SHORT).show();
        return false;
    }

    public void downloadFailed() {
        officialsList.clear();
        mAdapter.notifyDataSetChanged();
    }

    public void updateData(ArrayList<Official> list) {
        officialsList.addAll(list);
        mAdapter.notifyDataSetChanged();
    }
}