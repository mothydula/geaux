package com.example.geaux;

import android.os.AsyncTask;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

public class GetPlaylistTask extends AsyncTask<Void, Void, JSONObject> {
    private String weather = "";
    public GetPlaylistTask(String weather){
        if((weather != "") && (weather != null))
            this.weather = weather.replaceAll(" ", "+") + "+";
    }

    @Override
    protected JSONObject doInBackground(Void... voids) {
        JSONObject jsonObject = null;
        try {
            String json = "";
            String line;
            URL url = new URL("https://api.spotify.com/v1/search?q="+this.weather+"weather&type=playlist&limit=5&access_token=BQAgTOFACj2A49z2mxbaLDNEuGVOR-jom2f25hoM1fdcL1nnHqFXOM7ALpPHX2Q4f98k3eorcqpyHCr7adydHOvF_985ApzHv8vPLpL4CJzUEn9m6J54qV7hAZjhlgMJhqt2ZOrtXPIUyXjoFN44lno4dNyP4iZ592E");
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
