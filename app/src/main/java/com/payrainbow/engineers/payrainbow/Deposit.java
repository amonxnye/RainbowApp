package com.payrainbow.engineers.payrainbow;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.JsonReader;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Deposit extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = "PayRainbow_APP" ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_approve);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

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
              //  Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                VolleyTest();
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
            Toast.makeText(Deposit.this, "Approve+Home",Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_gallery) {

            Toast.makeText(Deposit.this, "Approve+Login",Toast.LENGTH_SHORT).show();

        } else if (id == R.id.nav_slideshow) {

            Toast.makeText(Deposit.this, "Approve+Approve",Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_manage) {
            Toast.makeText(Deposit.this, "Approve+Dashboard",Toast.LENGTH_SHORT).show();

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


  public void VolleyTest(){


      try {
          RequestQueue requestQueue = Volley.newRequestQueue(this);
          String URL = "http://pocketmoney.infosis.uk/Api/";
          JSONObject jsonBody = new JSONObject();
          jsonBody.put("action", "initiate_mobile_money_payments");
          jsonBody.put("merchant_code", "337");
          jsonBody.put("sender_reference", "1000");
          jsonBody.put("sender_phone", "256700110561");
          jsonBody.put("amount", "2000");
          jsonBody.put("external_ref", "0001");

          final String requestBody = jsonBody.toString();
         // final String requestBodyJson = jsonBody.toString();


          StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
              @Override
              public void onResponse(String response) {
                  Log.i("Poketi", response);
              }
          }, new Response.ErrorListener() {
              @Override
              public void onErrorResponse(VolleyError error) {
                  Log.e("Poketi", error.toString());
              }
          }) {
              @Override
              public String getBodyContentType() {
                  return "application/json; charset=utf-8";
              }

              @Override
              public Map<String, String> getHeaders() throws AuthFailureError {
                  HashMap<String, String> params = new HashMap<String, String>();
                  params.put("Authorization-Key", "CV8FJVBHM6KXHEHUPYGWJJKFYEEWUQN8CKTAXNGRWRUQN9YAHN3");
                  return params;
              }

              @Override
              public byte[] getBody() throws AuthFailureError {
                  try {
                      return requestBody == null ? null : requestBody.getBytes("utf-8");
                  } catch (UnsupportedEncodingException uee) {
                      VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", requestBody, "utf-8");
                      return null;
                  }
              }

              @Override
              protected Response<String> parseNetworkResponse(NetworkResponse response) {
                  String responseString = "";
                  String json = "";
                  String status = "";
                  String status_code = "";
                  String action = "";
                  String message = "";
                  String request_id = "";


                  if (response != null) {
                      responseString = String.valueOf(response.statusCode);
                      // can get more details such as response.headers
                      try {

                          json = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
                      } catch (UnsupportedEncodingException e) {
                          e.printStackTrace();
                      }
                      Log.d(TAG, json);

                      JSONObject obj = null;
                      try {
                          obj = new JSONObject(json);
                      } catch (JSONException e) {
                          e.printStackTrace();
                      }

                      try {


                          obj = new JSONObject(json);
                           status = obj.getString("status");
                          request_id = obj.getString("request_id");

                         // Log.i("Poketi", obj);
                          Log.d(TAG, request_id);

                      } catch (JSONException e) {
                          e.printStackTrace();
                      }




                  }
                  return Response.success(responseString, HttpHeaderParser.parseCacheHeaders(response));
                  //return Response.success(request_id);

              }
          };

          requestQueue.add(stringRequest);
      } catch (JSONException e) {
          e.printStackTrace();
      }
  }


}
