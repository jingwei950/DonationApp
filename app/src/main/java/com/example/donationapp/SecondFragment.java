package com.example.donationapp;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class SecondFragment extends Fragment {

    private DataManager dm;
    
    public static SecondFragment newInstance(){

        //Create a new SecondFragment object
        SecondFragment f = new SecondFragment();
        return f;
    }

    public SecondFragment(){
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_second, container, false);
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View v, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(v, savedInstanceState);

        //Database
        dm = new DataManager(getContext());

        //Add data to array list
        addData(dm.selectAll(), v);
    }

    public void addData(Cursor c, View v){

        String donatorName;
        String donatorAmount;
        String donatorMethod;

        ListView myList;

        //For storing donators with their name, amount donated and donation method
        ArrayList<Donator> donators = new ArrayList<Donator>();

        //For storing all names without amount
        List<String> uniqueName = new ArrayList<String>();

        //For storing unique name with its value
        List<Donator> uniqueNameValue = new ArrayList<Donator>();

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

        //Get the list view
        myList = (ListView) v.findViewById(R.id.listView1);
        //Get the array adapter
        ArrayAdapter adapter = new ArrayAdapter<Donator>(getActivity(),android.R.layout.simple_list_item_1, uniqueNameValue);
        //Set the array adapter to the list view
        myList.setAdapter(adapter);
        //Notify the adapter when the data changes
        adapter.notifyDataSetChanged();
    }
}