package com.example.geaux;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class EventArrayAdapter extends ArrayAdapter<ItineraryItem> {
    private Context mContext;
    private ArrayList<ItineraryItem> eventList = new ArrayList<>();

    public EventArrayAdapter(@NonNull Context context, ArrayList<ItineraryItem> list) {
        super(context, 0 , list);
        mContext = context;
        eventList = list;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItem = convertView;
        if(listItem == null)
            listItem = LayoutInflater.from(mContext).inflate(R.layout.itinerary_row,parent,false);

        ItineraryItem event = eventList.get(position);

        TextView dateAndTime = (TextView) listItem.findViewById(R.id.date_and_time);
        TextView description = (TextView) listItem.findViewById(R.id.itinerary_description);
        dateAndTime.setText(event.getDate() + "\n" + event.getTime());
        description.setText(event.getDescription());
        return listItem;
    }
}
