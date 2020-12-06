package com.example.geaux;

import android.os.AsyncTask;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class GetFlightsTask extends AsyncTask <Void, Void, JSONObject>{
    @Override
    protected JSONObject doInBackground(Void... voids) {
        JSONObject jsonObject = null;
        try {
            String json = "";
            String line;
            URL url = new URL("https://test.api.amadeus.com/v2/shopping/flight-offers?originLocationCode=SYD&destinationLocationCode=BKK&departureDate=2021-02-01&returnDate=2021-02-05&adults=1&max=3");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
            while((line = in.readLine()) != null){
                json+=line;
            }
            in.close();
            jsonObject = new JSONObject(json);
            System.out.println(jsonObject);
            /*HttpURLConnection
            HttpClient client = new DefaultHttpClient();
            String getURL = "http://helloworld.com/getmethod.aspx?id=1&method=getData";
            HttpGet httpGet = new HttpGet(getURL);
            **httpGet .setHeader("Content-Type", "application/x-zip");**
            HttpResponse response = client.execute(httpGet);
            HttpEntity resEntity = response.getEntity();
            if (resEntity != null) {
                //parse response.
                Log.e("Response",EntityUtils.toString(resEntity));
            }*/
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
