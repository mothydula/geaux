package com.example.geaux;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;

import com.google.gson.Gson;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import static com.example.geaux.MainActivity.model;

public class RetrieveViewModelTask  extends AsyncTask<Object, Void, JSONObject> {
    private Activity mainActivity;
    private String mode;
    public RetrieveViewModelTask(Activity mainActivity, String mode) {
        this.mainActivity = mainActivity;
        this.mode = mode;
    }

    private String read(Context context, String fileName) {
        //Read the storage.json file if it exists
        try {
            FileInputStream fis = context.openFileInput(fileName);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader bufferedReader = new BufferedReader(isr);
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                sb.append(line);
            }
            fis.close();
            isr.close();
            bufferedReader.close();
            return sb.toString();
        } catch (FileNotFoundException fileNotFound) {
            return null;
        } catch (IOException ioException) {
            return null;
        }
    }

    private boolean create(Context context, String fileName, String jsonString){
        //Create the storage.json file if it doesn't exist
        String FILENAME = "storage.json";
        try {
            FileOutputStream fos = context.openFileOutput(fileName,Context.MODE_PRIVATE);
            if (jsonString != null) {
                fos.write(jsonString.getBytes());
            }
            fos.close();
            return true;
        } catch (FileNotFoundException fileNotFound) {
            return false;
        } catch (IOException ioException) {
            return false;
        }

    }

    private boolean write(Context context){
        //Write to storage.json
        Gson gson = new Gson();
        String jsonString = gson.toJson(model);
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput("storage.json", Context.MODE_PRIVATE));
            outputStreamWriter.write(jsonString);
            outputStreamWriter.close();
        } catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
            return false;
        }
        return true;
    }

    public boolean isFilePresent(Context context, String fileName) {
        System.out.println(context.getFilesDir().getAbsolutePath());
        String path = context.getFilesDir().getAbsolutePath() + "/" + fileName;
        File file = new File(path);
        return file.exists();
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected JSONObject doInBackground(Object... objects) {
        boolean isFilePresent = isFilePresent(mainActivity.getBaseContext(), "storage.json");
        if(isFilePresent) {
            if(this.mode == "READ") {
                String jsonString = read(mainActivity.getBaseContext(), "storage.json");
                model = new Gson().fromJson(jsonString, MyViewModel.class);
            }
            else if (this.mode == "WRITE") {
                write(mainActivity.getBaseContext());
            }
            //do the json parsing here and do the rest of functionality of app
        } else {
            boolean isFileCreated = create(mainActivity.getBaseContext(), "storage.json", "{}");
            if(isFileCreated) {
               write(mainActivity.getBaseContext());
            } else {
                //show error or try again.
            }
        }
        return null;
    }

    @Override
    protected void onPostExecute(JSONObject jsonObject) {
        if(mainActivity instanceof MainActivity){
            mainActivity.findViewById(R.id.itineraries).setVisibility(View.VISIBLE);
            mainActivity.findViewById(R.id.new_itinerary).setVisibility(View.VISIBLE);
        }
    }
}
