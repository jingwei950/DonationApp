package com.example.donationapp;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class FirstFragment extends Fragment {

    private Button donateButton;
    private RadioGroup paymentMethod;
    private ProgressBar progressBar;
    private NumberPicker amountPicker;
    private int totalDonated = 0;
    private TextView tvTotal;
    private EditText etAmount;
    private EditText etName;
    private DataManager dm;

    public static FirstFragment newInstance(){

        //Create a new FirstFragment object
        FirstFragment f = new FirstFragment();
        return f;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
       // Inflate the layout for this fragment
       View v = inflater.inflate(R.layout.fragment_first, container, false);
       return v;
    }

    @Override
    public void onViewCreated(@NonNull View v, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(v, savedInstanceState);

        dm = new DataManager(getContext());

        //Edit text for name
        etName = (EditText) v.findViewById(R.id.etName);
        //Edit text for amount
        etAmount = (EditText) v.findViewById(R.id.etAmount);
        //Radio group that consists of payment methods: Paypal & direct
        paymentMethod = (RadioGroup) v.findViewById(R.id.paymentMethod);
        //Progress bar to show target amount and current progress donated
        progressBar = (ProgressBar) v.findViewById(R.id.progressBar);
        //Number picker for user to pick an amount
        amountPicker = (NumberPicker) v.findViewById(R.id.amountPicker);
        //Create the button
        donateButton = (Button) v.findViewById(R.id.donateButton);

        //Set the min and max values for the amount picker
        amountPicker.setMinValue(0);
        amountPicker.setMaxValue(1000);

        //Set the max of progress bar to 10000
        progressBar.setMax(10000);

        //Set the onclick listener for the donate button
        donateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Create new secondFragment instance when a button is clicked
                SecondFragment secondFragment = new SecondFragment();
                //Run the function to process of adding to the database
                donateButtonPressed(view,etName,etAmount);
                //Refresh the fragment on screen to get the latest amount each donator donates
                refreshFrag(secondFragment);
            }
        });
    }

    public void donateButtonPressed(View view, EditText etName, EditText etAmount){

        String method = "";
        int amount = 0;
        String editAmount = etAmount.getText().toString();


        //Handle which user input is used
        if(etAmount.getText().toString().equals("")){ //When the user uses amount picker
            amount = amountPicker.getValue();
        }
        else if (amountPicker.getValue() == 0){ //When the user uses edit text
            amount = Integer.parseInt(editAmount);
        }

        int radioId = paymentMethod.getCheckedRadioButtonId();
        totalDonated = totalDonated + amount;

        //Handle the method chosen
        if(radioId == R.id.rbPaypal){
            method = "Paypal";
        }
        else{
            method = "Direct";
        }

        //Set the total amount donated to progress bar
        progressBar.setProgress(totalDonated);

        //Text view: tvTotal
        tvTotal = getView().findViewById(R.id.tvTotal);

        String name = etName.getText().toString();
        Log.i("editName", "" + name);

        //Set the text of text view with total amount
        tvTotal.setText("Total amount: " + totalDonated);

        Log.v("Donate", "Donate Pressed with amount " + amount + ", method: " + method);
        Log.v("Donate", "Current total " + totalDonated);

        //Convert amount to string for database
        String stringAmount = Integer.toString(amount);
        //Insert record
        dm.insert(etName.getText().toString(), stringAmount, method);

        //Reset input fields back to 0 and blank
        amountPicker.setValue(0);
        etAmount.setText("");
        etName.setText("");

        //Display all the data in database
        showData(dm.selectAll());

//        dm.delete("");
//        showData(dm.selectAll());
    }

    public void refreshFrag(SecondFragment secondFragment){
        //Refresh the second fragment for history to update after user donate
        getFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainerView2, secondFragment) //"Refresh" the fragment
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE) //Animation of fade
                .commit();
    }


    public void showData(Cursor c){

        //For storing all donators from the database before processing
        ArrayList<Donator> donators = new ArrayList<Donator>();

        //For storing all names without amount
        List<String> uniqueName = new ArrayList<String>();

        //For storing unique name with its value
        List<Donator> uniqueNameValue = new ArrayList<Donator>();

        String donatorName;
        String donatorAmount;
        String donatorMethod;

        while (c.moveToNext())
        {
            Log.i("displayRecords", c.getString(1)+ " " +c.getString(2));

            donatorName = c.getString(1);
            donatorAmount = c.getString(2);
            donatorMethod = c.getString(3);
            donators.add(new Donator(donatorName, donatorAmount, donatorMethod));
        }

        //Loop thru and store unique names
        for(int i = 0; i < donators.size(); i++){
            String name = donators.get(i).getName(); //Get the name in object ArrayList
            if(!uniqueName.contains(name)) { //If the uniqueName array does not contain the name in donator
                uniqueName.add(name); //Add the name
                uniqueNameValue.add(new Donator(name, "0", "")); //Add uniqueName and value of 0 to uniqueNameValue array
            }
        }

        //Add total amount of the donators with unique names, make sure no duplicate names
        for(int k = 0; k < donators.size(); k++) {
            String orignalArrayNames = donators.get(k).getName(); 			//Get the name of donators
            int currAmount = Integer.parseInt(donators.get(k).getAmount()); //Get the amount of the donator donates
            String currMethod = donators.get(k).getMethod();

            for(int j = 0; j < uniqueNameValue.size(); j++) {
                String uniqueNames = uniqueNameValue.get(j).getName(); //Get the unique name

                //Check both the array for their name matches, if match, calculate the total that unique name donated
                if(uniqueNames.equals(orignalArrayNames)) {

                    int prevAmount = Integer.parseInt(uniqueNameValue.get(j).getAmount()); //Get the previous amount
                    int totalAmount = prevAmount + currAmount; //Add the previous amount and current amount
                    String totalAmountString = Integer.toString(totalAmount); //Convert the Integer into String in order to store in the object

                    uniqueNameValue.get(j).setAmount(totalAmountString); //Set the converted amount into the object
                    uniqueNameValue.get(j).setMethod(currMethod);
                }
            }
        }
    }
}