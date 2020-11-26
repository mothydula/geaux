package com.example.geaux;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

public class Itineraries extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_itineraries);

        //Start itinerary list fragment
        ItineraryListFragment itineraryListFrag = new ItineraryListFragment();
        itineraryListFrag.setContainerActivity(this);

        //Create transaction for list fragment
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.itinerary_list_container, itineraryListFrag);
        transaction.commit();
    }

    @Override
    protected void onStop() {
        super.onStop();
        new RetrieveViewModelTask(this, "WRITE").execute();
    }

    @Override
    public void onBackPressed()
    {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}