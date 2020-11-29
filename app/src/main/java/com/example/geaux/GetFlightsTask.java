package com.example.geaux;

import android.os.AsyncTask;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

public class GetFlightsTask extends AsyncTask <Void, Void, JSONObject>{
    @Override
    protected JSONObject doInBackground(Void... voids) {
        JSONObject jsonObject = null;
        try {
            String json = "";
            String line;
            URL url = new URL("http://partners.api.skyscanner.net/apiservices/browseroutes/v1.0/FR/eur/en-US/us/anywhere/anytime/anytime?apikey=prtl6749387986743898559646983194");
            BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
            while((line = in.readLine()) != null){
                json+=line;
            }
            in.close();
            jsonObject = new JSONObject(json);
            System.out.println(jsonObject);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
