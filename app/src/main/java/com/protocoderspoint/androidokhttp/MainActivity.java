package com.protocoderspoint.androidokhttp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


/**
 * Author by HUYNH NHAT MINH (ミン).
 * Email: minhhnce140197@fpt.edu.vn.
 * Date on 6/8/2021.
 * Company: FPT大学.
 */

public class MainActivity extends AppCompatActivity {

    //Declare variable
    private String myResponse;
    private ListView listView;
    private ArrayList<HashMap<String, String>> arrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        arrayList = new ArrayList<>();
        listView = (ListView) findViewById(R.id.listview);

        // Object HttpClient initialization HttpClient
        final OkHttpClient client = new OkHttpClient();
        String url = "https://protocoderspoint.com/jsondata/superheros.json";

        // Send request
        Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(new Callback() {

            /**
             * onFailure is called when a networking exception occurs.
             * when an unexpected exception occurs during request or response processing.
             */
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
            }

            /**
             * called when an HTTP response is received.
             */
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {

                if (response.isSuccessful()) {

                    myResponse = response.body().string();
                    MainActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {


                            try {
                                JSONObject reader = new JSONObject(myResponse);
                                JSONArray superheros = reader.getJSONArray("superheros"); // read data from file Json

                                System.out.println("json size is : " + superheros.length());

                                //The loop is used to iterate through all the elements in the json file,
                                //and add the elements in the HashMap of type key and value
                                for (int i = 0; i < superheros.length(); i++) {

                                    JSONObject hero = superheros.getJSONObject(i);
                                    String name = hero.getString("name");
                                    String power = hero.getString("power");
                                    //create a HashMap to store the data in key value pair
                                    HashMap<String, String> data = new HashMap<>();
                                    data.put("name", name);
                                    data.put("power", power);

                                    arrayList.add(data);//add the HashMap into arrayList

                                    //sets the adapter for listView
                                    ListAdapter adapter = new SimpleAdapter(MainActivity.this, arrayList, R.layout.listview_layout
                                            , new String[]{"name", "power"}, new int[]{R.id.name, R.id.power,});

                                    listView.setAdapter(adapter);
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }else {
                    Log.e("Response","Connect failed.");
                }
            }
        });
    }
}
