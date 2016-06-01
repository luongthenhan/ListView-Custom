package com.example.thenhan.listview_custom;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import vn.khmt.entity.Book;

public class MainActivity extends AppCompatActivity {

    ListView listView;
    ArrayList<Book> bookList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView = (ListView) findViewById(R.id.listView);

        // Run web service
        new CallWS().execute("http://192.168.43.32:8080/restful-heroku/book/all");
    }

    private class CallWS extends AsyncTask<String, Void, String> {
        private String jsonResponse;
        private ProgressDialog dialog = new ProgressDialog(MainActivity.this);

        @Override
        protected String doInBackground(String... urls) {
            try {
                // WARNING ------------------------------------------------------------
                // You must use actual IP addresses, do not enter " localhost:8080/..."
                // try something like: http://192.168.1.70:8080/Service/Method?Args
                // solution uses Java.Net class (Apache.HttpClient is now deprecated)
                // STEP1. Create a HttpURLConnection object releasing REQUEST to given site
                URL url = new URL(urls[0]); //argument supplied in the call to AsyncTask
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestProperty("User-Agent", "");
                urlConnection.setRequestMethod("GET");
                urlConnection.setDoInput(true);
                urlConnection.connect();

                // STEP2. wait for incoming RESPONSE stream, place data in a buffer
                InputStream isResponse = urlConnection.getInputStream();
                BufferedReader responseBuffer = new BufferedReader(new
                        InputStreamReader(isResponse));

                // STEP3. Arriving JSON fragments are concatenate into a StringBuilder
                String myLine = "";
                StringBuilder strBuilder = new StringBuilder();
                while ((myLine = responseBuffer.readLine()) != null) {
                    strBuilder.append(myLine);
                }
                //show response (JSON encoded data)
                jsonResponse = strBuilder.toString();
                Log.e("RESPONSE", jsonResponse);
            } catch (Exception e) {
                Log.e("RESPONSE Error", e.getMessage());
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            dialog.dismiss();

            if (jsonResponse != null) {
                Log.e("BOOK", jsonResponse);

                // Parsing json string to Book objects
                Gson gson = new Gson();
                Type listType = new TypeToken<ArrayList<Book>>() {}.getType();
                bookList = gson.fromJson(jsonResponse, listType);
                Log.e("BOOK", bookList.size() + "");
                listView.setAdapter(new BookAdapter(MainActivity.this, bookList));
            } else {
                Toast.makeText(getApplicationContext(),
                        "Error", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected void onPreExecute() {
            dialog.setMessage("Please wait..");
            dialog.show();
        }

        @Override
        protected void onProgressUpdate(Void... values) {
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
