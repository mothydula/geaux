package com.example.geaux;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.fragment.app.FragmentTransaction;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static com.example.geaux.AddEventFragment.textCountNonZero;
import static com.example.geaux.ContactsActivity.uri;
import static com.example.geaux.MainActivity.NEW_ITINERARY;
import static com.example.geaux.MainActivity.currentItinerary;

public class Itinerary extends AppCompatActivity
        implements DatePickerDialog.OnDateSetListener , TimePickerDialog.OnTimeSetListener{
    private ItineraryObject itinerary;
    private String newDateTime = "";
    private String newTimeFormatted = "";
    private String newDate = "";
    private String newDateFormatted = "";
    private String newTime = "";
    private String timeOfDay = "";
    private String newDescription;
    private boolean addingToggle = false;
    public static boolean dateSet = false;
    public static boolean timeSet = false;
    public static String itineraryWeather = "";
    private Button addEventButton;
    public static ItineraryItem currentEvent;
    private String currentPhotoPath = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_itinerary);


        TextView itinTitle = (TextView)findViewById(R.id.itinerary_title);
        itinTitle.setText(currentItinerary.getName());

        this.addEventButton = (Button)findViewById(R.id.add_event_button);

        //Start itinerary events list fragment
        ItineraryItems itinItemsFrag = new ItineraryItems();
        itinItemsFrag.setContainerActivity(this);

        //Create transaction for list fragment
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.itinerary_items_outer_container, itinItemsFrag);
        transaction.commit();
    }

    public void editItinerary(View view) {
        Intent intent = new Intent(this, EditItinerary.class);
        intent.putExtra(NEW_ITINERARY, false);
        intent.setFlags(intent.getFlags() | Intent.FLAG_ACTIVITY_NO_HISTORY);
        startActivity(intent);
    }

    @Override
    public void onBackPressed()
    {
        Intent intent = new Intent(this, Itineraries.class);
        intent.setFlags(intent.getFlags() | Intent.FLAG_ACTIVITY_NO_HISTORY);
        startActivity(intent);
    }

    public void addEvent(View view) {
        if(!addingToggle){
            if(getSupportFragmentManager().findFragmentById(R.id.itinerary_items_container) != null) {
                getSupportFragmentManager()
                        .beginTransaction().
                        remove(getSupportFragmentManager().findFragmentById(R.id.itinerary_items_container)).commit();
            }

            //Start itinerary events list fragment
            AddEventFragment addEventFrag = new AddEventFragment();
            addEventFrag.setContainerActivity(this);

            //Create transaction for list fragment
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.itinerary_items_outer_container, addEventFrag);
            transaction.commit();
            this.addEventButton.setVisibility(View.INVISIBLE);
            addingToggle = true;
        }
        else{
            this.newDateTime = this.newDateFormatted + "\n" + this.newTimeFormatted;
            EditText editText = (EditText)findViewById(R.id.description_input);
            this.newDescription = editText.getText().toString();
            ItineraryItem itineraryItem = new ItineraryItem(this.newDate, this.newTime, this.newDateFormatted, this.newTimeFormatted, this.newDescription, new FlightObject(), this.newDateTime);
            currentItinerary.addEvent(itineraryItem);


            if(getSupportFragmentManager().findFragmentById(R.id.add_event_container) != null) {
                getSupportFragmentManager()
                        .beginTransaction().
                        remove(getSupportFragmentManager().findFragmentById(R.id.add_event_container)).commit();
            }
            System.out.println("ONE");
            //Start itinerary events list fragment
            ItineraryItems itinItemsFrag = new ItineraryItems();
            itinItemsFrag.setContainerActivity(this);
            System.out.println("TWO");
            //Create transaction for list fragment
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.itinerary_items_outer_container, itinItemsFrag);
            transaction.commit();
            System.out.println("THREE");
            dateSet = false;
            timeSet = false;
            addingToggle = false;
        }
    }

    public void openContacts(View view) {

        uri = null;
        ListView listView = (ListView) findViewById(R.id.itinerary_events);
        //Get bitmap of the Drawing
        Bitmap bitmap = getScreenBitmap(view);
        Canvas canvas = new Canvas(bitmap);
        listView.draw(canvas);
        File file = null;
        try {
            file = createImageFile();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (file != null) {
            FileOutputStream outputStream;
            try {
                outputStream = new FileOutputStream(file);
                bitmap.compress(Bitmap.CompressFormat.PNG, 70, outputStream);
                outputStream.flush();
            } catch (Exception e) {
                e.printStackTrace();
            }
            uri = FileProvider.getUriForFile(this, "com.example.geaux.fileprovider", file);
        }

        //Start contacts activity
        Intent intent = new Intent(this, ContactsActivity.class);
        startActivity(intent);
    }

    private Bitmap getBitmapFromView(View view, int height, int width) {
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        Drawable bgDrawable = view.getBackground();
        if (bgDrawable != null)
            bgDrawable.draw(canvas);
        else
            canvas.drawColor(Color.WHITE);
        view.draw(canvas);
        return bitmap;
    }

    public Bitmap getScreenBitmap(View v) {
        v.setDrawingCacheEnabled(true);
        v.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        v.layout(0, 0, v.getMeasuredWidth(), v.getMeasuredHeight());

        v.buildDrawingCache(true);
        Bitmap b = Bitmap.createBitmap(v.getDrawingCache());
        v.setDrawingCacheEnabled(false); // clear drawing cache
        return b;
    }

    public static Bitmap getWholeListViewItemsToBitmap(ListView listView) {
        ListAdapter adapter  = listView.getAdapter();
        int itemscount       = adapter.getCount();
        int allitemsheight   = 0;
        List<Bitmap> bmps    = new ArrayList<Bitmap>();

        for (int i = 0; i < itemscount; i++) {

            View childView      = adapter.getView(i, null, listView);
            childView.measure(View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.EXACTLY),
                    View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));

            childView.layout(0, 0, childView.getMeasuredWidth(), childView.getMeasuredHeight());
            childView.setDrawingCacheEnabled(true);
            childView.buildDrawingCache();
            bmps.add(childView.getDrawingCache());
            allitemsheight+=childView.getMeasuredHeight();
        }

        Bitmap bigbitmap    = Bitmap.createBitmap(listView.getMeasuredWidth(), allitemsheight, Bitmap.Config.ARGB_8888);
        Canvas bigcanvas    = new Canvas(bigbitmap);

        Paint paint = new Paint();
        int iHeight = 0;

        for (int i = 0; i < bmps.size(); i++) {
            Bitmap bmp = bmps.get(i);
            bigcanvas.drawBitmap(bmp, 0, iHeight, paint);
            iHeight+=bmp.getHeight();

            bmp.recycle();
            bmp=null;
        }
        return bigbitmap;
    }

    private File createImageFile() throws Exception {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imageFileName, ".jpg", storageDir);
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    public void showDatePickerDialog(View view){
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                this,
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
                );
        datePickerDialog.show();
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth){
        int[] months = new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12};
        month = months[month];
        System.out.println("MONTH: " + month);
        String date = "month/day/month: " + month + "/" + dayOfMonth + "/" + year;
        this.newDateFormatted = month + "/" + dayOfMonth + "/" + year;
        this.newDate = getSingleDigitValue(year-2000) + "" + getSingleDigitValue(month) + "" + getSingleDigitValue(dayOfMonth);
        this.dateSet = true;
        if(this.dateSet && this.timeSet && textCountNonZero){
            this.addEventButton.setVisibility(View.VISIBLE);
        }
        TextView textView = (TextView)findViewById(R.id.test_text);
    }

    public void showTimePickerDialog(View view){
        TimePickerDialog timePickerDialog = new TimePickerDialog(
                this,
                this,
                Calendar.getInstance().get(Calendar.HOUR_OF_DAY),
                Calendar.getInstance().get(Calendar.MINUTE),
                false);
        timePickerDialog.show();
    }

    @Override
    public void onTimeSet(TimePicker timePicker, int hour, int minute) {
        this.newTime = getSingleDigitValue(hour) + "" + getSingleDigitValue(minute);
        System.out.println("TIME: " + this.newTime);
        this.newTimeFormatted = getTimeOfDayValuedHour(hour).get(0) + ":" + getSingleDigitValue(minute) + " " + getTimeOfDayValuedHour(hour).get(1);
        //this.timeOfDay = getSingleDigitValue(hour) + "" + getSingleDigitValue(minute);
        this.timeSet = true;
        if(this.dateSet && this.timeSet && textCountNonZero){
            this.addEventButton.setVisibility(View.VISIBLE);
        }
        TextView textView = (TextView) findViewById(R.id.test_text_2);
        textView.setText(this.timeOfDay);
    }

    private List<String> getTimeOfDayValuedHour(int hour){
        if(hour == 12){
            List<String> returnArray = Arrays.asList("12", "PM");
            return returnArray;
        }
        else if(hour == 0) {
            List<String> returnArray = Arrays.asList("12", "AM");
            return returnArray;
        }
        if(hour > 12) {
            List<String> returnArray = Arrays.asList(getSingleDigitValue(hour-12), "PM");
            return returnArray;
        }
        else{
            List<String> returnArray = Arrays.asList(getSingleDigitValue(hour), "AM");
            return returnArray;
        }

    }

    public void addFlight(View view){
        new GetFlightsTask().execute();
    }

    private String getSingleDigitValue(int value){
        if(value < 10){
            return "0"+value;
        }
        else {
            return ""+value;
        }
    }
    @Override
    protected void onStop() {
        super.onStop();
        new RetrieveViewModelTask(this, "WRITE").execute();
    }

    public void getWeather(View view) {
        EditText editText = (EditText)findViewById(R.id.zip_code_input);
        String zipCode = editText.getText().toString();
        new GetWeatherTask(zipCode, this).execute();
    }

    public void openPlaylists(View view) {
        new GetPlaylistTask(itineraryWeather).execute();
    }
}