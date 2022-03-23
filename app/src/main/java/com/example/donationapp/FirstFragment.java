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
    private NumberPicker amountPickerLand;
    private int totalDonated = 0;
    private TextView tvTotal;
    private EditText etAmount;
    private TextView tvName;
    EditText etName;
    private DataManager dm;
    private Donator donator;
    private RadioButton paypal;
    private RadioButton direct;

    public static FirstFragment newInstance(){

        //Create a new FirstFragment object
        FirstFragment f = new FirstFragment();
        return f;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       View v = inflater.inflate(R.layout.fragment_first, container, false);

        FragmentManager fragmentManager;

        dm = new DataManager(getContext());

        etName = (EditText) v.findViewById(R.id.etName);
        etAmount = (EditText) v.findViewById(R.id.etAmount);

        //Create the button
//        donateButton = (Button) container.findViewById(R.id.donateButton);
//        donateButton = (Button) v.findViewById(R.id.donateButton);

        if(donateButton != null){
            Log.v("Donate", "Really got the donate button");
        }


        paymentMethod = (RadioGroup) v.findViewById(R.id.paymentMethod);
        progressBar = (ProgressBar) v.findViewById(R.id.progressBar);
//        amountPicker = new NumberPicker(MainActivity.this);
        amountPicker = (NumberPicker) v.findViewById(R.id.amountPicker);

        if(amountPicker == null){
            Log.v("test", "Number picker null");
        }
        //Set the min and max values for the amount picker
        amountPicker.setMinValue(0);
        amountPicker.setMaxValue(1000);

        //Set the max of progress bar to 10000
        progressBar.setMax(10000);

        // Inflate the layout for this fragment
        return v;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        donateButton = (Button) view.findViewById(R.id.donateButton);

        donateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Create new secondFragment instance when a button is clicked
                SecondFragment secondFragment = new SecondFragment();
                donateButtonPressed(view,etName,etAmount);
                refreshFrag(secondFragment, newInstance());
            }
        });
    }


    public void donateButtonPressed(View view, EditText etName, EditText etAmount){

        String method = "";
        int amount = 0;
        String editAmount = etAmount.getText().toString();

        Log.i("editText", ""+editAmount);

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
        dm.insert(etName.getText().toString(), stringAmount);

        //Reset input fields back to 0 and blank
        amountPicker.setValue(0);
        etAmount.setText("");
        etName.setText("");

        //Display all the data in database
        showData(dm.selectAll());

//        dm.delete("");
//        showData(dm.selectAll());
    }

    public void refreshFrag(SecondFragment secondFragment, FirstFragment firstFragment){
        getFragmentManager().beginTransaction()
                .detach(firstFragment)
                .attach(firstFragment)
                .commit();
        getFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainerView2, secondFragment)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
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

        while (c.moveToNext())
        {
            Log.i("displayRecords", c.getString(1)+ " " +c.getString(2));

            donatorName = c.getString(1);
            donatorAmount = c.getString(2);
            donators.add(new Donator(donatorName, donatorAmount));
        }

        //Loop thru and store unique names
        for(int i = 0; i < donators.size(); i++){
            String name = donators.get(i).getName(); //Get the name in object ArrayList
            if(!uniqueName.contains(name)) { //If the uniqueName array does not contain the name in donator
                uniqueName.add(name); //Add the name
                uniqueNameValue.add(new Donator(name, "0")); //Add uniqueName and value of 0 to uniqueNameValue array
            }
        }

        //Add total amount of the donators with unique names, make sure no duplicate names
        for(int k = 0; k < donators.size(); k++) {
            String orignalArrayNames = donators.get(k).getName(); 			//Get the name of donators
            int currAmount = Integer.parseInt(donators.get(k).getAmount()); //Get the amount of the donator donates

            for(int j = 0; j < uniqueNameValue.size(); j++) {
                String uniqueNames = uniqueNameValue.get(j).getName(); //Get the unique name

                //Check both the array for their name matches, if match, calculate the total that unique name donated
                if(uniqueNames.equals(orignalArrayNames)) {
//            		System.out.println("Name in uniqueNameValue array: " + uniqueNames);
                    int prevAmount = Integer.parseInt(uniqueNameValue.get(j).getAmount()); //Get the previous amount

//            		System.out.println("Previous amount: " + prevAmount + " Current amount: " + currAmount);
                    int totalAmount = prevAmount + currAmount; //Add the previous amount and current amount
                    String totalAmountString = Integer.toString(totalAmount); //Convert the Integer into String in order to store in the object

                    uniqueNameValue.get(j).setAmount(totalAmountString); //Set the converted amount into the object

//            		System.out.println("Total amount: " + uniqueNameValue.get(j).getAmount());
                }
            }
        }
        for(int b = 0; b<uniqueNameValue.size();b++){
            Log.i("uniqueNameValueArray",  ""+uniqueNameValue.get(b));
        }

        Log.i("DonatorArray", ""+donators);
    }
}