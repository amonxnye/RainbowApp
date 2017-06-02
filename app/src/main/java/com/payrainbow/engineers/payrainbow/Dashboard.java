package com.payrainbow.engineers.payrainbow;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Iterator;

public class Dashboard extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = "PayRainbow_APP";
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        TextView Balance = (TextView) findViewById(R.id.balance_text);
        TextView Purchases = (TextView) findViewById(R.id.purchases_text);
        TextView Card = (TextView) findViewById(R.id.card_text);

        Button Getbtn = (Button)findViewById(R.id.getbtn);
        Button Postbtn = (Button)findViewById(R.id.postbtn);

        FirebaseUser currentUser = mAuth.getInstance().getCurrentUser();

        // updateUI(currentUser);

        // Balance.setText(GetText("balance", currentUser.getEmail().toString()));
        // Purchases.setText(GetText("purchases", currentUser.getEmail().toString()));
        //Card.setText(GetText("card", currentUser.getEmail().toString()));

        // new SendDeviceDetails();
        new SendData().execute();

        Getbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SendData().execute();

            }
        });

        Postbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SendData_Post().execute();

            }
        });


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                //  new SendData();
            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
            Toast.makeText(Dashboard.this, "Dashboard+Home", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(Dashboard.this, MainActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_gallery) {

            Toast.makeText(Dashboard.this, "Dashboard+Login", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(Dashboard.this, Login.class);
            startActivity(intent);

        } else if (id == R.id.nav_slideshow) {

            Toast.makeText(Dashboard.this, "Dashboard+Approve", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(Dashboard.this, ApproveActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_manage) {
            Toast.makeText(Dashboard.this, "Dashboard+Dashboard", Toast.LENGTH_SHORT).show();

        } else if (id == R.id.nav_share) {
           // new SendData();
        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /****************************Methods of Data Sending*****************/


    public class SendData extends AsyncTask<String, Void, String> {

        protected void onPreExecute(){}

        protected String doInBackground(String... arg0) {

            try {

                URL url = new URL("http://payrainbow.com/userdata_app.php"); // here is your URL path

                JSONObject postDataParams = new JSONObject();
                postDataParams.put("email", "amonxnye@gmail.com");
                postDataParams.put("data", "balance");
                Log.e("params",postDataParams.toString());

                //Log.d("params",postDataParams.toString());
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(15000 /* milliseconds */);
                conn.setConnectTimeout(15000 /* milliseconds */);
                conn.setRequestMethod("GET");
                conn.setDoInput(true);
                conn.setDoOutput(true);

                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                writer.write(getPostDataString(postDataParams));
               // writer.write(postDataParams);
                writer.flush();
                writer.close();
                os.close();

                int responseCode=conn.getResponseCode();

                if (responseCode == HttpURLConnection.HTTP_OK) {

                    BufferedReader in=new BufferedReader(
                            new InputStreamReader(
                                    conn.getInputStream()));
                    StringBuffer sb = new StringBuffer("");
                    String line="";

                    while((line = in.readLine()) != null) {

                        sb.append(line);
                        break;
                    }

                    in.close();
                    return sb.toString();

                }
                else {
                    return new String("false : "+responseCode);
                }
            }
            catch(Exception e){
                return new String("Exception: " + e.getMessage());
            }

        }

        @Override
        protected void onPostExecute(String result) {
            Toast.makeText(getApplicationContext(), result,
                    Toast.LENGTH_LONG).show();
            Log.d(TAG, "DataCheck:onSuccess:" + result);

        }
    }

    public class SendData_Post extends AsyncTask<String, Void, String> {

        protected void onPreExecute(){}

        protected String doInBackground(String... arg0) {

            try {

                URL url = new URL("http://payrainbow.com/userdata_app_post.php"); // here is your URL path

                JSONObject postDataParams = new JSONObject();
                postDataParams.put("email", "amonxnye@gmail.com");
                postDataParams.put("data", "balance");
                Log.e("params",postDataParams.toString());

                //Log.d("params",postDataParams.toString());
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(15000 /* milliseconds */);
                conn.setConnectTimeout(15000 /* milliseconds */);
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);

                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                // writer.write(getPostDataString(postDataParams));
                writer.write(String.valueOf(postDataParams));
                writer.flush();
                writer.close();
                os.close();

                int responseCode=conn.getResponseCode();

                if (responseCode == HttpURLConnection.HTTP_OK) {

                    BufferedReader in=new BufferedReader(
                            new InputStreamReader(
                                    conn.getInputStream()));
                    StringBuffer sb = new StringBuffer("");
                    String line="";

                    while((line = in.readLine()) != null) {

                        sb.append(line);
                        break;
                    }

                    in.close();
                    return sb.toString();

                }
                else {
                    return new String("false : "+responseCode);
                }
            }
            catch(Exception e){
                return new String("Exception: " + e.getMessage());
            }

        }

        @Override
        protected void onPostExecute(String result) {
            Toast.makeText(getApplicationContext(), result,
                    Toast.LENGTH_LONG).show();
            Log.d(TAG, "DataCheck:onSuccess:" + result);

        }
    }

//    public class SendDeviceDetails extends AsyncTask<String, Void, String> {
//
//        String email,data;
//
//        public SendDeviceDetails(String emailx, String datax) {
//            email = emailx;
//            data = datax;
//        }
//
//        @Override
//        protected String doInBackground(String... params) {
//
//            // These two need to be declared outside the try/catch
//
//            String response = null;
//             Log.d(TAG,email);
//             Log.d(TAG, data);
//
//            try {
//                // Construct the URL for the OpenWeatherMap query
//                // Possible parameters are avaiable at OWM's forecast API page, at
//                URL url = new URL("http://payrainbow.com/userdata_app.php");
//                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//                conn.setReadTimeout(10000);
//                conn.setConnectTimeout(15000);
//                conn.setRequestMethod("POST");
//                conn.setDoInput(true);
//                conn.setDoOutput(true);
//
//                //   Intent intent = getIntent();
//
//                // String email = intent.getStringExtra("email");
//
//                Uri.Builder builder = new Uri.Builder()
//                        .appendQueryParameter("email", email)
//                        .appendQueryParameter("data", data);
//                String query = builder.build().getEncodedQuery();
//
//                OutputStream os = conn.getOutputStream();
//                // InputStream is = conn.getInputStream();
//                BufferedWriter writer = new BufferedWriter(
//                        new OutputStreamWriter(os, "UTF-8"));
//                writer.write(query);
//                writer.flush();
//                writer.close();
//                os.close();
//
//                conn.connect();
//
//                // Toast.makeText(PayActivity.this,"Payment Done...", Toast.LENGTH_SHORT).show();
//
//
//                // Read the input stream into a String
//                InputStream inputStream = conn.getInputStream();
//                StringBuffer buffer = new StringBuffer();
//                if (inputStream == null) {
//                    // Nothing to do.
//                    return null;
//                }
//                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
//
//                String line;
//                while ((line = reader.readLine()) != null) {
//                    // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
//                    // But it does make debugging a *lot* easier if you print out the completed
//                    // buffer for debugging.
//                    buffer.append(line + "\n");
//                }
//
//                if (buffer.length() == 0) {
//                    // Stream was empty.  No point in parsing.
//                    return null;
//                }
//                response = buffer.toString();
//                Log.d(TAG, response);
//                // return response;
//
//                Toast.makeText(Dashboard.this, response, Toast.LENGTH_SHORT).show();
//
//            } catch (Exception e) {
//                  Log.e(TAG,e.toString());
//               //  Toast.makeText(Dashboard.this, "No data Currently", Toast.LENGTH_SHORT).show();
//                // If the code didn't successfully get the weather data, there's no point in attemping
//                // to parse it.
//                return "0";
//            }
//            return response;
//        }
//
//        @Override
//        protected void onPostExecute(String result) {
//            //    Toast.makeText(getApplicationContext(), result,Toast.LENGTH_LONG).show();
//            Log.d(TAG, "DataCheck:onSuccess:" + result);
//            Toast.makeText(Dashboard.this, result.toString(), Toast.LENGTH_SHORT).show();
//
//        }
//
//    }


    // Create GetText Metod
//    public String GetText(String datax, String email) throws UnsupportedEncodingException {
//        // Get user defined values
//        //   batch = batch_number.getText().toString();
//
//
//        // Create data variable for sent values to server
//
//        String data = URLEncoder.encode("email", "UTF-8") + "=" + URLEncoder.encode("amonxnye@gmail.com", "UTF-8");
//
//        data += "&" + URLEncoder.encode("data", "UTF-8") + "="
//                + URLEncoder.encode("balance", "UTF-8");
///*
//        data += "&" + URLEncoder.encode("user", "UTF-8")
//                + "=" + URLEncoder.encode(Login, "UTF-8");
//
//        data += "&" + URLEncoder.encode("pass", "UTF-8")
//                + "=" + URLEncoder.encode(Pass, "UTF-8");
//*/
//        String text = "";
//        BufferedReader reader = null;
//
//        // Send data
//        try {
//
//            // Defined URL  where to send data
//            URL url = new URL("http://payrainbow.com/userdata_app.php");
//
//            // Send POST data request
//
//            URLConnection conn = url.openConnection();
//            conn.setDoOutput(true);
//            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
//            wr.write(data);
//            wr.flush();
//
//            // Get the server response
//
//            reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
//            StringBuilder sb = new StringBuilder();
//            String line = null;
//
//            // Read Server Response
//            while ((line = reader.readLine()) != null) {
//                // Append server response in string
//                sb.append(line + "\n");
//            }
//
//
//            text = sb.toString();
//        } catch (Exception ex) {
//
//        } finally {
//            try {
//
//                reader.close();
//            } catch (Exception ex) {
//            }
//        }
//
//        // Show response on activity
//        // content.setText( text  );
//        Log.d(TAG, text.toString());
//
//        //send data to intent
//     /*   Intent intent = new Intent(getBaseContext(),"Activity.this");
//        intent.putExtra("result_data", text);
//        startActivity(intent);
//        */
//
//        return text;
//    }

//
//    public class SendData extends AsyncTask<String, Void, String> {
//
//
//        protected void onPreExecute() {
//        }
//
//        protected String doInBackground(String... arg0) {
//
//            try {
//
//                URL url = new URL("http://payrainbow.com/userdata_app.php"); // here is your URL path
//
//                JSONObject postDataParams = new JSONObject();
//                postDataParams.put("email", "amonxnye@gmal.com");
//                postDataParams.put("data", "balance");
//                Log.e("params", postDataParams.toString());
//
//                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//                conn.setReadTimeout(15000 /* milliseconds */);
//                conn.setConnectTimeout(15000 /* milliseconds */);
//                conn.setRequestMethod("GET");
//                conn.setDoInput(true);
//                conn.setDoOutput(true);
//
//                OutputStream os = conn.getOutputStream();
//                BufferedWriter writer = new BufferedWriter(
//                        new OutputStreamWriter(os, "UTF-8"));
//                writer.write(getPostDataString(postDataParams));
//
//                writer.flush();
//                writer.close();
//                os.close();
//
//                int responseCode = conn.getResponseCode();
//
//                if (responseCode == HttpURLConnection.HTTP_OK) {
//
//                    BufferedReader in = new BufferedReader(
//                            new InputStreamReader(
//                                    conn.getInputStream()));
//                    StringBuffer sb = new StringBuffer("");
//                    String line = "";
//
//                    while ((line = in.readLine()) != null) {
//
//                        sb.append(line);
//                        break;
//                    }
//
//                    in.close();
//                    return sb.toString();
//
//                } else {
//                    return new String("false : " + responseCode);
//                }
//            } catch (Exception e) {
//                return new String("Exception: " + e.getMessage());
//            }
//
//        }
//
//        @Override
//        protected void onPostExecute(String result) {
//        //    Toast.makeText(getApplicationContext(), result,Toast.LENGTH_LONG).show();
//            Log.d(TAG, "DataCheck:onSuccess:" + result);
//            Toast.makeText(Dashboard.this, result.toString(), Toast.LENGTH_SHORT).show();
//
//        }
//    }
//
    public String getPostDataString(JSONObject params) throws Exception {

        StringBuilder result = new StringBuilder();
        boolean first = true;

        Iterator<String> itr = params.keys();

        while (itr.hasNext()) {

            String key = itr.next();
            Object value = params.get(key);

            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(key, "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(value.toString(), "UTF-8"));

        }
        return result.toString();
    }

//    public class NewDataMethod extends AsyncTask<String, Void, String> {
//
//
//
//        @Override
//        protected void onPreExecute () {
//        }
//
//        @Override
//        protected String doInBackground (String...arg0){
//
///*
//       if(byGetOrPost == 0){ //means by Get Method
//
//            try{
//                String username = (String)arg0[0];
//                String password = (String)arg0[1];
//                String link = "http://ec2-54-89-162-255.compute-1.amazonaws.com/app.php";
//
//                URL url = new URL(link);
//                HttpClient client = new DefaultHttpClient();
//                HttpGet request = new HttpGet();
//                request.setURI(new URI(link));
//                HttpResponse response = client.execute(request);
//                BufferedReader in = new BufferedReader(new
//                        InputStreamReader(response.getEntity().getContent()));
//
//                StringBuffer sb = new StringBuffer("");
//                String line="";
//
//                while ((line = in.readLine()) != null) {
//                    sb.append(line);
//                    break;
//                }
//
//                in.close();
//                return sb.toString();
//            } catch(Exception e){
//                return new String("Exception: " + e.getMessage());
//            }
//        } else{
//            */
//            try {
//                // String username = (String)arg0[0];
//                //  String password = (String)arg0[1];
//
//                String link = "http://payrainbow.com/userdata_app.php";
//
//                String data = URLEncoder.encode("email", "UTF-8") + "=" +
//                        URLEncoder.encode("amonxnye@gmail.com", "UTF-8");
//
//                data += "&" + URLEncoder.encode("data", "UTF-8") + "=" +
//                        URLEncoder.encode("balance", "UTF-8");
//
//                URL url = new URL(link);
//                URLConnection conn = url.openConnection();
//
//                conn.setDoOutput(true);
//                OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
//
//                wr.write(data);
//                wr.flush();
//
//                BufferedReader reader = new BufferedReader(new
//                        InputStreamReader(conn.getInputStream()));
//
//                StringBuilder sb = new StringBuilder();
//                String line = null;
//
//                // Read Server Response
//                while ((line = reader.readLine()) != null) {
//                    sb.append(line);
//                    break;
//                }
//
//                return sb.toString();
//            } catch (Exception e) {
//                return new String("Exception: " + e.getMessage());
//            }
//       /* }
//      return  sb.toString();*/
//        }
//
//        @Override
//        protected void onPostExecute (String result){
//            // this.statusField.setText("Login Successful");
//            Toast.makeText(Dashboard.this, result.toString(), Toast.LENGTH_SHORT).show();
//            //Toast.makeText(, "Hello"+result.toString(), Toast.LENGTH_SHORT).show();
//           // Intent intent = new Intent(getBaseContext(), ResultsActivity.class);
//           // intent.putExtra("result_data", result);
//           // startActivity(intent);
//        }
//    }

    /****************************Methods of Data Sending End*****************/
}

