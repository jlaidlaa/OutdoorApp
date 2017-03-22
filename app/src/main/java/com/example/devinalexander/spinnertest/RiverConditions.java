package com.example.devinalexander.spinnertest;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.devinalexander.spinnertest.Utilities.NetworkUtils;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class RiverConditions extends AppCompatActivity {
    public ImageView img;
    private TextView upperView;
    private TextView rStats;
    private String geoLocation = "";

    //private TextView river;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_river_conditions);
        Intent intent = getIntent();
        String value = intent.getStringExtra("key");
        RiverLocation selectedLocation = (RiverLocation)intent.getSerializableExtra("selectedLocation");
        makeRiverSearchQuery(selectedLocation.getSiteId());
        img = (ImageView) findViewById(R.id.imageView4);
        upperView = (TextView) findViewById(R.id.location);
        rStats = (TextView) findViewById(R.id.rStats);


        Picasso.with(this).load(selectedLocation.getImageURLString()).into(img);

        upperView.setText(selectedLocation.getName());

        //river = (TextView)findViewById(R.id.riverText);
        //river.setText(selectedLocation.toString());
    }

    /**
     * This method retrieves the search text from the EditText, constructs the
     * URL (using {@link NetworkUtils}) for the github repository you'd like to find, displays
     * that URL in a TextView, and finally fires off an AsyncTask to perform the GET request using
     * our {@link riverTask}
     */
    private void makeRiverSearchQuery(String siteId) {
        //String siteId = "";
        URL riverSearchUrl = NetworkUtils.buildUrl(siteId);
        //mUrlDisplayTextView.setText(githubSearchUrl.toString());
        // COMPLETED (4) Create a new GithubQueryTask and call its execute method, passing in the url to query
        new riverTask().execute(riverSearchUrl);
    }

    // COMPLETED (1) Create a class called GithubQueryTask that extends AsyncTask<URL, Void, String>
    public class riverTask extends AsyncTask<URL, Void, String> {

        //let put a loading symbol here
        ProgressDialog pdog;

        //for the impatient people
        @Override
        protected void onPreExecute(){

            pdog = new ProgressDialog(RiverConditions.this);
            pdog.setMessage("Loading...");
            pdog.show();

        }
        // COMPLETED (2) Override the doInBackground method to perform the query. Return the results. (Hint: You've already written the code to perform the query)
        @Override
        protected String doInBackground(URL... params) {

            try {
                Thread.sleep(500);
            }
            catch (InterruptedException i){
                Log.d("error sleep", "thread error");
            }
            URL searchUrl = params[0];

            String riverSearchResults = ""; //string of JSON object from request

            String siteName = "";   //gage name
            String disDesc = "";   // discharge description
            String disValue = ""; // discharge amount
            String gageHeight = "";     //gage height, current water level
            String gageDesc = "";       // gage description
            double latitude = 0;
            double longitude = 0;

            try {
                riverSearchResults = NetworkUtils.getResponseFromHttpUrl(searchUrl);

                //convert https response to JSON
                JSONObject jObj = new JSONObject(riverSearchResults);

                siteName = getSiteName(jObj);
                longitude = getLongitude(jObj);
                latitude = getLatitude(jObj);
                disDesc = getDischargeDescription(jObj);
                disValue = getDischargeAmount(jObj);
                gageDesc = getGageDescription(jObj);
                gageHeight = getGageHeight(jObj);



            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException j) {
                j.printStackTrace();
            }
            //set geoLocation
            geoLocation = String.valueOf(latitude) + "," + String.valueOf(longitude);

            //return river info;
            return siteName + "\nLongitude " + longitude + "\nLatitude " +
                    latitude + "\n" + disDesc + " " + disValue + "\n" + gageDesc + " " + gageHeight;

        }

        // COMPLETED (3) Override onPostExecute to display the results in the TextView
        @Override
        protected void onPostExecute(String riverInfo) {

            //stop our progress dialog
            if(pdog != null){
                pdog.dismiss();
            }

            if (riverInfo!= null && !riverInfo.equals("")) {
                rStats.setText(riverInfo);
            }
        }
    }

    /*
   Parse out site name from JSON object returned by https request

   */
    public String getSiteName(JSONObject jObj) {

        final String obj_1 = "value";
        final String arr_1 = "timeSeries";
        final int arr_position = 0;
        final String obj_2 = "sourceInfo";
        final String obj_field = "siteName";
        String siteName = "";

        try {
            JSONObject first_obj = jObj.getJSONObject(obj_1);
            JSONArray first_arr = first_obj.getJSONArray(arr_1);
            JSONObject second_obj = first_arr.getJSONObject(arr_position);
            JSONObject third_obj = second_obj.getJSONObject(obj_2);
            siteName = third_obj.getString(obj_field);
        } catch (JSONException j) {
            j.printStackTrace();
        }

        return siteName;

    }

    /*
   Parse out longitude from JSON object returned by https request

   */
    public double getLongitude(JSONObject jsonObject) {
        final String obj_1 = "value";
        final String arr_1 = "timeSeries";
        final int arr_position = 0;
        final String obj_2 = "sourceInfo";
        final String obj_3 = "geoLocation";
        final String obj_4 = "geogLocation";
        final String field = "longitude";
        double longitude = 0;

        try {
            //get first JSON object with passed in json object
            JSONObject first_obj = jsonObject.getJSONObject(obj_1);

            //get JSON array from within object
            JSONArray first_arr = first_obj.getJSONArray(arr_1);

            //get object from first pos of array
            JSONObject second_obj = first_arr.getJSONObject(arr_position);

            //get object from within that object
            JSONObject third_obj = second_obj.getJSONObject(obj_2);

            // go into this object
            JSONObject fourth_obj = third_obj.getJSONObject(obj_3);

            // go get this last object
            JSONObject fifth_obj = fourth_obj.getJSONObject(obj_4);

            //access longitude field from this object
            longitude = fifth_obj.getDouble(field);

        } catch (JSONException j) {
            j.printStackTrace();
        }

        return longitude;
    }

    /*
   Parse out latitude from JSON object returned by https request

   */
    public double getLatitude(JSONObject jsonObject) {
        final String obj_1 = "value";
        final String arr_1 = "timeSeries";
        final int arr_position = 0;
        final String obj_2 = "sourceInfo";
        final String obj_3 = "geoLocation";
        final String obj_4 = "geogLocation";
        final String field = "latitude";
        double longitude = 0;

        try {
            //get first JSON object with passed in json object
            JSONObject first_obj = jsonObject.getJSONObject(obj_1);

            //get JSON array from within object
            JSONArray first_arr = first_obj.getJSONArray(arr_1);

            //get object from first pos of array
            JSONObject second_obj = first_arr.getJSONObject(arr_position);

            //get object from within that object
            JSONObject third_obj = second_obj.getJSONObject(obj_2);

            // go into this object
            JSONObject fourth_obj = third_obj.getJSONObject(obj_3);

            // go get this last object
            JSONObject fifth_obj = fourth_obj.getJSONObject(obj_4);

            //access longitude field from this object
            longitude = fifth_obj.getDouble(field);

        } catch (JSONException j) {
            j.printStackTrace();
        }

        return longitude;
    }

    /*
   Parse out discharge description from JSON object returned by https request

   */
    public String getDischargeDescription(JSONObject jsonObject) {
        final String obj_1 = "value";
        final String arr_1 = "timeSeries";
        final int arr_position = 0;
        final String obj_2 = "variable";
        final String obj_3 = "variableDescription";
        final String field = "latitude";
        String desc = "";
        try {
            //get first JSON object with passed in json object
            JSONObject first_obj = jsonObject.getJSONObject(obj_1);

            //get JSON array from within object
            JSONArray first_arr = first_obj.getJSONArray(arr_1);

            //get object from first pos of array
            JSONObject second_obj = first_arr.getJSONObject(arr_position);

            //Get for variable info, first variable is streamflow
            JSONObject flowVar = second_obj.getJSONObject(obj_2);

            //JSONArray flowDetail = flowVar.getJSONArray("variableCode");
            desc = flowVar.getString(obj_3);

        } catch (JSONException j) {
            j.printStackTrace();
        }

        return desc;

    }

    /*
   Parse out discharge amount from JSON object returned by https request

   */
    public String getDischargeAmount(JSONObject jsonObject) {
        final String obj_1 = "value";
        final String arr_1 = "timeSeries";
        final int arr_position = 0;
        final String obj_2 = "variableDescription";
        final String obj_3 = "values";
        final String obj_4 = "value";
        final String field = "value";
        String amount = "";
        try {


            //get first JSON object with passed in json object
            JSONObject first_obj = jsonObject.getJSONObject(obj_1);

            //get JSON array from within object
            JSONArray first_arr = first_obj.getJSONArray(arr_1);

            //get object from first pos of array
            JSONObject second_obj = first_arr.getJSONObject(arr_position);

            //Get for values array inside time series array
            JSONArray values = second_obj.getJSONArray(obj_3);

            //get first obj in that values array
            JSONObject outer_first_value = values.getJSONObject(arr_position);

            //get the value array in that object
            JSONArray value_array = outer_first_value.getJSONArray(obj_4);

            //get the first object in that value array
            JSONObject inner_first_value = value_array.getJSONObject(arr_position);

            //set discharge amount to that value field
            amount = inner_first_value.getString(field);


        } catch (JSONException j) {
            j.printStackTrace();
        }

        return amount;

    }

    /*
   Parse out gage description from JSON object returned by https request

   */
    public String getGageDescription(JSONObject jsonObject) {
        final String obj_1 = "value";
        final String arr_1 = "timeSeries";
        final int arr_position = 1;
        final String obj_2 = "variable";
        final String obj_3 = "variableName";
        final String field = "latitude";
        String gageDesc = "";
        try {
            //get first JSON object with passed in json object
            JSONObject first_obj = jsonObject.getJSONObject(obj_1);

            //get JSON array from within object
            JSONArray first_arr = first_obj.getJSONArray(arr_1);

            //get object from second pos of array
            JSONObject second_obj = first_arr.getJSONObject(arr_position);

            //Get for variable info, second variable is gage height
            JSONObject gage = second_obj.getJSONObject(obj_2);

            //JSONArray flowDetail = flowVar.getJSONArray("variableCode");
            gageDesc = gage.getString(obj_3);

        } catch (JSONException j) {
            j.printStackTrace();
        }

        return gageDesc;

    }

    /*
    Parse out gage height from JSON object returned by https request

    */
    public String getGageHeight(JSONObject jsonObject) {

        final String obj_1 = "value";
        final String arr_1 = "timeSeries";
        final int arr_position = 1;
        final int inner_arr_position = 0;
        final String obj_2 = "variableDescription";
        final String obj_3 = "values";
        final String obj_4 = "value";
        final String field = "value";
        String amount = "";

        try {

            //get first JSON object with passed in json object
            JSONObject first_obj = jsonObject.getJSONObject(obj_1);

            //get JSON array from within object
            JSONArray first_arr = first_obj.getJSONArray(arr_1);

            //get object from first pos of array
            JSONObject second_obj = first_arr.getJSONObject(arr_position);

            //Get for values array inside time series array
            JSONArray values = second_obj.getJSONArray(obj_3);

            //get first obj in that values array
            JSONObject outer_first_value = values.getJSONObject(inner_arr_position);

            //get the value array in that object
            JSONArray value_array = outer_first_value.getJSONArray(obj_4);

            //get the first object in that value array
            JSONObject inner_first_value = value_array.getJSONObject(inner_arr_position);

            //set discharge amount to that value field
            amount = inner_first_value.getString(field);


        } catch (JSONException j) {
            j.printStackTrace();
        }

        return amount;

    }

    /**
     * This method is called when the Open Location in Map button is clicked. It will open the
     * a map to the location represented by the variable addressString using implicit Intents.
     *
     * @param v Button that was clicked.
     */
    public void onClickOpenAddressButton(View v) {

        //This was not workin for some reason
        //Uri.Builder builder = new Uri.Builder();
        //builder.scheme("geo")
          //      .path("0,0")
            //    .query(geoLocation);

        //My work around
        Uri addressUri;// = builder.build();
        addressUri = Uri.parse("geo:"+geoLocation);


        showMap(addressUri);
    }


    /**
     * Code courtesy of udacity Android course, has been modified
     * This method will fire off an implicit Intent to view a location on a map.
     *
     * When constructing implicit Intents, you can use either the setData method or specify the
     * URI as the second parameter of the Intent's constructor,
     *
     *
     * @param geoLocation The Uri representing the location that will be opened in the map
     */
    private void showMap(Uri geoLocation) {

        /*
         * Again, we create an Intent with the action, ACTION_VIEW because we want to VIEW the
         * contents of this Uri.
         */
        Intent intent = new Intent(Intent.ACTION_VIEW);


        /*
         * Using setData to set the Uri of this Intent has the exact same affect as passing it in
         * the Intent's constructor. This is simply an alternate way of doing this.
         */
        intent.setData(geoLocation);


        // Verify that this Intent can be launched and then call startActivity
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }


}
