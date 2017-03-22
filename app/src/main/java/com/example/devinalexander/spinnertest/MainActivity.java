package com.example.devinalexander.spinnertest;

import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private List<RiverLocation> list;
    private Button riverConfirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        riverConfirm = (Button)findViewById(R.id.riverConfirm);

        /*
        The asset manager allows for the reading of the .txt input file. It is locate in /main/assets/
        The Manager reads the file and passes it to an InputStream. The InputStream is passed
        to class RiverFileReader and an array is returned containing the sorted rivers.
         */
        AssetManager assManager = getApplicationContext().getAssets();

        InputStream is = null;
        try {
            is = assManager.open("rivers2.txt");
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        RiverFileReader rfr = new RiverFileReader();
        list = rfr.readRiverFile(is);

        /*
        This ArrayList was created to hold Strings rather than RiverLocations.
         */
        ArrayList<String> string = new ArrayList<String>();

        for (int i = 0; i < list.size(); i++){
            string.add(list.get(i).toString());
        }

        //This list was created to be passed to the spinner object.
        List<String> rivers = string;

        for (int i = 0; i < rivers.size(); i++){
            System.out.println(rivers.get(i));
        }

        /*
        The array adapter sets up the conditions for the spinner to be used as a drop-down menu.
         */
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_dropdown_item_1line, rivers);

        //adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

       /*
        final Spinner spinner = (Spinner) findViewById(R.id.riverSpinner);
        spinner.setAdapter(adapter);

        */
        final AutoCompleteTextView textView = (AutoCompleteTextView) findViewById(R.id.aCTextView);
        textView.setThreshold(1);
        textView.setAdapter(adapter);

        /*
        The onClickListener for the Button.
         */
        riverConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                //String river = spinner.getSelectedItem().toString();
                String river = textView.getText().toString();
                RiverLocation selectedRiver = null;
                for (int i = 0; i < list.size(); i++){
                    if (river.equals(list.get(i).toString())) {
                        selectedRiver = list.get(i);
                    }
                }

                Toast.makeText(getApplicationContext(), river, Toast.LENGTH_SHORT).show();

                Intent myIntent = new Intent(MainActivity.this, RiverConditions.class);
                myIntent.putExtra("selectedLocation", selectedRiver); //Send RiverLocation to next Activity
                myIntent.putExtra("key", river);
                MainActivity.this.startActivity(myIntent);
            }
        });



    }




}
