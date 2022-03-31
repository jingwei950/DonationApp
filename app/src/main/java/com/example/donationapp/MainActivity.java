package com.example.donationapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.Placeholder;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import javax.crypto.SealedObject;

public class MainActivity extends AppCompatActivity {
//    private Button donateButton;
//    private RadioGroup paymentMethod;
//    private ProgressBar progressBar;
//    private NumberPicker amountPicker;
//    private NumberPicker amountPickerLand;
//    private int totalDonated = 0;
//    private TextView tvTotal;
//    private EditText etAmount;
//    private TextView tvName;
//    private EditText etName;
//    private DataManager dm;
//    private Donator donator;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_main);

        //Test
        TextView name;
        //Test 2
        TextView password;
        
        //Detach & attach for updating the fragment views
        if (savedInstanceState == null) {
            FirstFragment firstFragment = FirstFragment.newInstance();
            SecondFragment secondFragment = SecondFragment.newInstance();
            getSupportFragmentManager().beginTransaction()
//                    .replace(R.id.fragmentContainerView, firstFragment)
                    .replace(R.id.fragmentContainerView2, secondFragment)
//                    .detach(firstFragment)
//                    .attach(firstFragment)
//                    .detach(secondFragment)
//                    .attach(secondFragment)
                    .commit();
//            refreshFrag(secondFragment, getSupportFragmentManager());
        }




//        dm = new DataManager(this);
//
//        //Create the button
//        donateButton = (Button) findViewById(R.id.donateButton);
//
//        if(donateButton != null){
//            Log.v("Donate", "Really got the donate button");
//        }
//
//        paymentMethod = (RadioGroup) findViewById(R.id.paymentMethod);
//        progressBar = (ProgressBar) findViewById(R.id.progressBar);
////        amountPicker = new NumberPicker(MainActivity.this);
//        amountPicker = (NumberPicker) findViewById(R.id.amountPicker);
//
//        if(amountPicker == null){
//            Log.v("test", "Number picker null");
//        }
//        //Set the min and max values for the amount picker
////        amountPicker.setMinValue(0);
////        amountPicker.setMaxValue(1000);
////
////        //Set the max of progress bar to 10000
////        progressBar.setMax(10000);
    }

//    public void donateButtonPressed(View view){
//        Log.v("DonateButton", "Donate button pressed");
//
//        etAmount = (EditText) findViewById(R.id.etAmount);
//        String method = "";
//        int amount = 0;
//        String editAmount = etAmount.getText().toString();
//
//        //Handle which user input is used
//        if(etAmount.getText().toString().equals("")){ //When the user uses amount picker
//            amount = amountPicker.getValue();
//        }
//        else if (amountPicker.getValue() == 0){ //When the user uses edit text
//            amount = Integer.parseInt(editAmount);
//        }
//
//        int radioId = paymentMethod.getCheckedRadioButtonId();
//        totalDonated = totalDonated + amount;
//
//        //Handle the method chosen
//        if(radioId == R.id.rbPaypal){
//            method = "Paypal";
//        }
//        else{
//            method = "Direct";
//        }
//
//        //Set the total amount donated to progress bar
//        progressBar.setProgress(totalDonated);
//
//        //Edit text: etName
//        etName = findViewById(R.id.etName);
//        //Text view: tvTotal
//        tvTotal = findViewById(R.id.tvTotal);
//
//        //Set the text of text view with total amount
//        tvTotal.setText("Total amount: " + totalDonated);
//
//        Log.v("Donate", "Donate Pressed with amount " + amount + ", method: " + method);
//        Log.v("Donate", "Current total " + totalDonated);
//
//        //Convert amount to string for database
//        String stringAmount = Integer.toString(amount);
//        //Insert record
//        dm.insert(etName.getText().toString(), stringAmount);
//
//        //Reset input fields back to 0 and blank
//        amountPicker.setValue(0);
//        etAmount.setText("");
//        etName.setText("");
//
//        //Display all the data in database
//        showData(dm.selectAll());
//
////        dm.delete("john");
////        showData(dm.selectAll());
//
//
////        showData(dm.searchName(searchName));
//    }
//
//
//    public void showData(Cursor c){
////        ArrayList<String> names = new ArrayList<String>();
//
//        ArrayList<Donator> donators = new ArrayList<Donator>();
//
//        //For storing all names without amount
//        List<String> uniqueName = new ArrayList<String>();
//
//        //For storing unique name with its value
//        List<Donator> uniqueNameValue = new ArrayList<Donator>();
//
//        String donatorName;
//        String donatorAmount;
//
//        while (c.moveToNext())
//        {
//            Log.i("displayRecords", c.getString(1)+ " " +c.getString(2));
//
//            donatorName = c.getString(1);
//            donatorAmount = c.getString(2);
//            donators.add(new Donator(donatorName, donatorAmount));
//        }
//
//        //Loop thru and store unique names
//        for(int i = 0; i < donators.size(); i++){
//            String name = donators.get(i).getName(); //Get the name in object ArrayList
//            if(!uniqueName.contains(name)) { //If the uniqueName array does not contain the name in donator
//                uniqueName.add(name); //Add the name
//                uniqueNameValue.add(new Donator(name, "0")); //Add uniqueName and value of 0 to uniqueNameValue array
//            }
//        }
//
//        //Add total amount of the donators with unique names, make sure not duplicate names
//        for(int k = 0; k < donators.size(); k++) {
//            String orignalArrayNames = donators.get(k).getName(); 			//Get the name of donators
//            int currAmount = Integer.parseInt(donators.get(k).getAmount()); //Get the amount of the donator donates
//
//            for(int j = 0; j < uniqueNameValue.size(); j++) {
//                String uniqueNames = uniqueNameValue.get(j).getName(); //Get the unique name
//
//                //Check both the array for their name matches, if match, calculate the total that unique name donated
//                if(uniqueNames.equals(orignalArrayNames)) {
////            		System.out.println("Name in uniqueNameValue array: " + uniqueNames);
//                    int prevAmount = Integer.parseInt(uniqueNameValue.get(j).getAmount()); //Get the previous amount
//
////            		System.out.println("Previous amount: " + prevAmount + " Current amount: " + currAmount);
//                    int totalAmount = prevAmount + currAmount; //Add the previous amount and current amount
//                    String totalAmountString = Integer.toString(totalAmount); //Convert the Integer into String in order to store in the object
//
//                    uniqueNameValue.get(j).setAmount(totalAmountString); //Set the converted amount into the object
//
////            		System.out.println("Total amount: " + uniqueNameValue.get(j).getAmount());
//                }
//            }
//        }
//        Log.i("uniqueNameValueArray", "Name 1: \n" + uniqueNameValue.get(0) + "\n" + "Name 2: \n" + uniqueNameValue.get(1) + "\n" + "Name 3: \n" + uniqueNameValue.get(2));
//        Log.i("DonatorArray", ""+donators);
//    }
//    public static void refreshFrag(SecondFragment secondFragment, FragmentManager fragmentManager){
//    //            SecondFragment secondFragment = SecondFragment.newInstance();
//        fragmentManager.beginTransaction()
//                .detach(secondFragment)
//                .attach(secondFragment).commit();
//    }

}
