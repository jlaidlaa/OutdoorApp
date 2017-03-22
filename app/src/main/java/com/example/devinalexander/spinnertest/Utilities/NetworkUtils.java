/**package com.example.devinalexander.spinnertest.Utilities;

/**
 * Created by jlaidlaw on 3/19/17.
 */
/*
public class NetworkUtils {
}
*/
/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.devinalexander.spinnertest.Utilities;
//package com.example.android.datafrominternet.utilities;

import android.net.Uri;

import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

/**
 * These utilities will be used to communicate with the network.
 */
public class NetworkUtils {

    final static String GITHUB_BASE_URL =
            "https://waterservices.usgs.gov/nwis/iv/";


    //"?format=json&sites=02037500&parameterCd=00010,00060,00065&siteStatus=all";


    //"https://api.github.com/search/repositories";

    final static String key_1 = "format";
    final static String val_1 = "json";
    final static String key_2 = "sites";
    //final static String val_2 = "02037500";
    final static String key_3 = "parameterCd";
    final static String val_3 = "00010,00060,00065";
    final static String key_4 = "siteStatus";
    final static String val_4 = "all";
    //final static String PARAM_QUERY = "q";

    /*
     * The sort field. One of stars, forks, or updated.
     * Default: results are sorted by best match if no field is specified.
     */
    //final static String PARAM_SORT = "sort";
    //jfinal static String sortBy = "stars";

    /**
     * Builds the URL used to query GitHub.
     *
     * @param siteId The keyword that will be queried for.
     * @return The URL to use to query the GitHub.
     */
    public static URL buildUrl(String siteId) {
        Uri builtUri;
       builtUri = Uri.parse(GITHUB_BASE_URL).buildUpon()
                .appendQueryParameter(key_1, val_1)
                .appendQueryParameter(key_2, siteId)
                .appendQueryParameter(key_3, val_3)
                .appendQueryParameter(key_4, val_4)
                .build();

        //.appendQueryParameter(PARAM_QUERY, githubSearchQuery)
        //.appendQueryParameter(PARAM_SORT, sortBy)
        //.build();

        URL url = null;
        try {
            url = new URL((builtUri.toString().replaceAll("%2C", ",")));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }

    /**
     * This method returns the entire result from the HTTP response.
     *
     * @param url The URL to fetch the HTTP response from.
     * @return The contents of the HTTP response.
     * @throws IOException Related to network and stream reading
     */
    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        String json = null;
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");
            String newJson = "";

            boolean hasInput = scanner.hasNext();

            if (hasInput) {
                newJson += scanner.next();//return scanner.next();
            } else {
                return null;
            }

            json = newJson.toString();
            //JSONObject jObj = new JSONObject(json);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            urlConnection.disconnect();
        }
        return json;
    }
}
