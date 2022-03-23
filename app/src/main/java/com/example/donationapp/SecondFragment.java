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
    View v;
    
    public static SecondFragment newInstance(){

        //Create a new SecondFragment object
        SecondFragment f = new SecondFragment();
        return f;
    }

    public SecondFragment(){
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("onCreate", "onCreate ran");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.i("onCreate", "onCreateView ran");

        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_second, container, false);

        //Database
        dm = new DataManager(getContext());

        //Add data to array list
        addData(dm.selectAll(), v);

        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    public void addData(Cursor c, View v){

        String donatorName;
        String donatorAmount;
//        String listview_array[] = new String[0];
        List<Donator> listview_array = new ArrayList<Donator>();
        ListView myList;

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

        myList = (ListView) v.findViewById(R.id.listView1);
        ArrayAdapter adapter = new ArrayAdapter<Donator>(getActivity(),android.R.layout.simple_list_item_1, uniqueNameValue);
        myList.setAdapter(adapter);
        adapter.notifyDataSetChanged();

    }
}