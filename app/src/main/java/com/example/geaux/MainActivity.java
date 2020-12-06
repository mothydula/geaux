package com.example.geaux;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
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
        TextView appTitle = (TextView)findViewById(R.id.app_title);
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
        Animation animSlideDown = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.slide_down);
        Animation animSlideUp = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.slide_up);

        itinerariesButton.startAnimation(animSlideDown);
        newButton.startAnimation(animSlideDown);
        appTitle.startAnimation(animSlideDown);

        //findViewById(R.id.itineraries).setX(findViewById(R.id.itineraries).getX() - 100f);
        ObjectAnimator animation = ObjectAnimator.ofFloat(findViewById(R.id.itineraries), "translationX", 100f);
        animation.setDuration(2000);
        //animation.start();

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
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {

                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(MainActivity.this, "Permission denied to read your External storage", Toast.LENGTH_SHORT).show();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    @Override
    public void onBackPressed(){
        Intent a = new Intent(Intent.ACTION_MAIN);
        a.addCategory(Intent.CATEGORY_HOME);
        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(a);
    }

    public void openHelp(View view) {
        TextView helpButton = (TextView)findViewById(R.id.help_button);
        if(getSupportFragmentManager().findFragmentById(R.id.outer_help_container) != null) {
            helpButton.setText("help");
            getSupportFragmentManager()
                    .beginTransaction().
                    remove(getSupportFragmentManager().findFragmentById(R.id.outer_help_container)).commit();
        }
        else {
            HelpFragment helpFrag = new HelpFragment();
            helpButton.setText("close");
            helpFrag.setContainerActivity(this);

            //Create transaction for list fragment
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.outer_help_container, helpFrag);
            transaction.commit();
        }
    }
}