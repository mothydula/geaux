package com.example.geaux;

import android.os.AsyncTask;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

public class GetLocationTask extends AsyncTask<Void, Void, JSONObject> {
    @Override
    protected JSONObject doInBackground(Void... voids) {
        JSONObject jsonObject = null;
        try {
            String json = "";
            String line;
            URL url = new URL("https://api.openweathermap.org/data/2.5/weather?q=phoeix&appid=d712bccb4092d117fcecf171eb7c62dc");
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
