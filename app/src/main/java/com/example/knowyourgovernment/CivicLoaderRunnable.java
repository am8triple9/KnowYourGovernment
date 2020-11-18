package com.example.knowyourgovernment;

import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class CivicLoaderRunnable implements Runnable {

    private static final String TAG = "CivicLoaderRunnable";
    private MainActivity mainActivity;
    private static final String key = "AIzaSyD9OKsQ2p6ZRYcVrlien3W3WWCjDLodYNI";
    private static final String DATA_URL = "https://www.googleapis.com/civicinfo/v2/representatives?key=" + key + "&address=";

    CivicLoaderRunnable(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    @Override
    public void run() {
        Uri dataUri = Uri.parse(DATA_URL + mainActivity.address);
        String urlToUse = dataUri.toString();
        Log.d(TAG, "run: " + urlToUse);

        StringBuilder sb = new StringBuilder();
        try {
            URL url = new URL(urlToUse);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.connect();

            if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
                Log.d(TAG, "run: HTTP ResponseCode NOT OK: " + conn.getResponseCode());
                handleResults(null);
                return;
            }

            InputStream is = conn.getInputStream();
            BufferedReader reader = new BufferedReader((new InputStreamReader(is)));

            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append('\n');
            }

            Log.d(TAG, "run: " + sb.toString());

        } catch (Exception e) {
            Log.e(TAG, "run: ", e);
            handleResults(null);
            return;
        }

        handleResults(sb.toString());

    }

    private void handleResults(String o) {
        if (o == null) {
            Log.d(TAG, "handleResults: Failure in data download");
            mainActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mainActivity.downloadFailed();
                }
            });
            return;
        }

        final ArrayList<Official> officialList = parseJSON(o);
        mainActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (officialList != null) {
                    Toast.makeText(mainActivity, "Loaded " + officialList.size() + " officials.", Toast.LENGTH_LONG).show();
                    mainActivity.updateData(officialList);
                }
            }
        });
    }

    private ArrayList<Official> parseJSON(String o) {
        ArrayList<Official> officialList = new ArrayList<>();
        try {
            JSONObject jObjMain = new JSONObject(o);
            JSONObject jNormalizedInput = jObjMain.getJSONObject("normalizedInput");
            JSONArray jOffices = jObjMain.getJSONArray("offices");
            JSONArray jOfficials = jObjMain.getJSONArray("officials");

            for (int i = 0; i < jOffices.length(); i++) {
                JSONObject jOffice = (JSONObject) jOffices.get(i);

                JSONArray temp = jOffice.getJSONArray("officialIndices");
                int numberOfOfficials = temp.length();
                if (numberOfOfficials > 0) {
                    for (int j = 0; j < numberOfOfficials; j++) {
                        String officialIndex = temp.getString(j);
                        addOfficial(officialIndex, jNormalizedInput, jOffice, jOfficials, officialList);
                    }
                }
            }
            return officialList;
        } catch (Exception e) {
            Log.d(TAG, "parseJSON: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    private void addOfficial(String officialIndex, JSONObject jNormalizedInput, JSONObject jOffice, JSONArray jOfficials, ArrayList<Official> officialList) throws JSONException {
        JSONObject jOfficial = (JSONObject) jOfficials.get(Integer.parseInt(officialIndex));

        String city = "";
        if (jNormalizedInput.has("city")) city = jNormalizedInput.getString("city");
        String state = "";
        if (jNormalizedInput.has("state")) state = jNormalizedInput.getString("state");
        String zip = "";
        if (jNormalizedInput.has("zip")) zip = jNormalizedInput.getString("zip");
        String officeName = "";
        if (jOffice.has("name")) officeName = jOffice.getString("name");
        String officialName = "";
        if (jOfficial.has("name")) officialName = jOfficial.getString("name");

        JSONArray jAddress = jOfficial.getJSONArray("address");
        JSONObject jOAddress = jAddress.getJSONObject(0);
        String address = getAddress(jOAddress);


        String party = "";
        if(jOfficial.has("party")) party = jOfficial.getString("party");
        String phones = "";
        if (jOfficial.has("phones")) phones = jOfficial.getJSONArray("phones").getString(0);
        String urls = "";
        if (jOfficial.has("urls")) urls = jOfficial.getJSONArray("urls").getString(0);
        String emails = "";
        if (jOfficial.has("emails")) {
            emails = jOfficial.getJSONArray("emails").getString(0);
        }
        String photoUrl = "";
        if (jOfficial.has("photoUrl")) photoUrl = jOfficial.getString("photoUrl");

        JSONArray jChannels = null;
        String[][] channels = null;
        if (jOfficial.has("jChannels")) {
            jChannels = jOfficial.getJSONArray("channels");
            int numChannels = jChannels.length();
            channels = new String[numChannels][2];
            for (int i = 0; i < numChannels; i++) {
                JSONObject channel = jChannels.getJSONObject(i);
                channels[i][0] = channel.getString("type");
                channels[i][1] = channel.getString("id");
            }
        }


        officialList.add(new Official(city, state, zip, officeName, officialName, address, party, phones, urls, emails, photoUrl, channels));
    }

    private String getAddress(JSONObject jOAddress) throws JSONException {
        String line1 = "";
        if(jOAddress.has("line1")) {
            line1 = jOAddress.getString("line1");
        }

        String line2 = "";
        if(jOAddress.has("line2")) {
            line2 = jOAddress.getString("line2");
        }

        String city = "";
        if(jOAddress.has("city")) {
            city = jOAddress.getString("city");
        }

        String state = "";
        if(jOAddress.has("state")) {
            state = jOAddress.getString("state");
        }

        String zip = "";
        if(jOAddress.has("zip")) {
            zip = jOAddress.getString("zip");
        }

        String address = line1 + " " +
                line2 + " " +
                city + ", " +
                state + " " +
                zip;

        return address;
    }


}
