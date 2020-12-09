package com.example.geaux;

import android.app.Activity;
import android.os.AsyncTask;
import android.widget.TextView;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

import static com.example.geaux.Itinerary.itineraryWeather;

public class GetWeatherTask extends AsyncTask<Void, Void, JSONObject> {
    private String zipCode;
    private String weather;
    private String temperature;
    Activity containerActivity;
    public GetWeatherTask(String zipCode, Activity conatinerActivity){
        this.zipCode = zipCode;
        this.containerActivity = conatinerActivity;
    }

    @Override
    protected JSONObject doInBackground(Void... voids) {
        JSONObject jsonObject = null;
        try {
            String json = "";
            String line;
            URL url = new URL("https://api.interzoid.com/getweatherzipcode?license=e08ec0bbe796b9e7c0ff0d05cea9269a&zip=" + this.zipCode);
            BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
            while((line = in.readLine()) != null){
                json+=line;
            }
            in.close();
            jsonObject = new JSONObject(json);
            this.weather = jsonObject.getString("Weather");
            this.temperature = jsonObject.getString("TempF");
            itineraryWeather = this.weather;
            System.out.println(jsonObject);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(JSONObject jsonObject) {
        super.onPostExecute(jsonObject);
        //Set weather text when task has completed
        TextView weatherText = (TextView)this.containerActivity.findViewById(R.id.weather_outlook);
        weatherText.setText("weather: "+this.weather+", "+this.temperature);
    }
}
