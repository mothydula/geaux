package com.example.geaux;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import java.io.File;

import static com.example.geaux.EditItinerary.newItinerary;

public class MainActivity extends AppCompatActivity {
    private SharedPreferences sharedPrefs;
    public static MyViewModel model;
    public static ItineraryObject currentItinerary;
    public static String NEW_ITINERARY= "NEW_ITINERARY";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button newButton = (Button)findViewById(R.id.new_itinerary);
        Button itinerariesButton = (Button)findViewById(R.id.itineraries);

        newButton.setVisibility(View.INVISIBLE);
        itinerariesButton.setVisibility(View.INVISIBLE);
        if(this.model != null) {
            new RetrieveViewModelTask(this, "READ").execute();
        }
        else {
            if(isFilePresent(this,"storage.json")){
                new RetrieveViewModelTask(this, "READ").execute();
            }
            else{
                System.out.println("BRIIIICK SQUAD");
                this.model = ViewModelProviders.of(this).get(MyViewModel.class);
                findViewById(R.id.itineraries).setVisibility(View.VISIBLE);
                findViewById(R.id.new_itinerary).setVisibility(View.VISIBLE);
            }

        }
    }

    public void goToItineraries(View view) {
        Intent itineraryIntent = new Intent(MainActivity.this, Itineraries.class);
        startActivity(itineraryIntent);
    }

    public boolean isFilePresent(Context context, String fileName) {
        System.out.println(context.getFilesDir().getAbsolutePath());
        String path = context.getFilesDir().getAbsolutePath() + "/" + fileName;
        File file = new File(path);
        return file.exists();
    }

    @Override
    public void onStop() {

        super.onStop();
        new RetrieveViewModelTask(MainActivity.this, "WRITE").execute();

    }

    public void goToEditNew(View view) {
        newItinerary = true;
        Intent intent = new Intent(MainActivity.this, EditItinerary.class);
        intent.putExtra(NEW_ITINERARY, true);
        startActivity(intent);

    }

    @Override
    public void onBackPressed(){
        Intent a = new Intent(Intent.ACTION_MAIN);
        a.addCategory(Intent.CATEGORY_HOME);
        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(a);
    }
}