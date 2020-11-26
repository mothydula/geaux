package com.example.geaux;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

import static com.example.geaux.MainActivity.currentItinerary;

public class ItineraryArrayAdapter extends ArrayAdapter<ItineraryObject> {
    private Context mContext;
    private ArrayList<ItineraryObject> itineraryList = new ArrayList<>();

    public ItineraryArrayAdapter(@NonNull Context context, ArrayList<ItineraryObject> list) {
        super(context, 0 , list);
        mContext = context;
        itineraryList = list;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItem = convertView;
        if(listItem == null)
            listItem = LayoutInflater.from(mContext).inflate(R.layout.list_row,parent,false);

        ItineraryObject itinerary = itineraryList.get(position);

        TextView name = (TextView) listItem.findViewById(R.id.itinerary_name);
        name.setText(itinerary.getName());

        listItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, Itinerary.class);
                currentItinerary = itinerary;
                System.out.println(currentItinerary.getName());
                mContext.startActivity(intent);
            }
        });

        return listItem;
    }
}
