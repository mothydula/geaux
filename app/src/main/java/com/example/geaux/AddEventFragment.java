package com.example.geaux;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import static com.example.geaux.Itinerary.dateSet;
import static com.example.geaux.Itinerary.timeSet;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddEventFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddEventFragment extends Fragment {
    public Activity containerActivity = null;
    public static boolean textCountNonZero = false;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AddEventFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AddEventFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AddEventFragment newInstance(String param1, String param2) {
        AddEventFragment fragment = new AddEventFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_event, container, false);
    }

    public void setContainerActivity(Activity containerActivity) {
        this.containerActivity = containerActivity;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //Create reference for adding event button
        Button addEventButton = (Button)this.containerActivity.findViewById(R.id.add_event_button);
        //Set this button to invisible until all of the required inputs are set
        addEventButton.setVisibility(View.INVISIBLE);
        //Create reference for the input for event description
        EditText descriptionInput = (EditText)this.containerActivity.findViewById(R.id.description_input);
        descriptionInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                //Check to see if the input is empty or not
                    if(count == 0) {
                        textCountNonZero = false;
                        addEventButton.setVisibility(View.INVISIBLE);
                    }
                    else {
                        textCountNonZero = true;
                        if(dateSet && timeSet)
                            addEventButton.setVisibility(View.VISIBLE);
                    }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    public void showDatePickerDialog(View view){

    }

    public void showTimePickerDialog(View view){

    }

    public void addFlight(View view){

    }

    @Override
    public void onStop() {
        super.onStop();
        new RetrieveViewModelTask(this.containerActivity, "WRITE").execute();
    }
}