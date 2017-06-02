package com.payrainbow.engineers.payrainbow;

import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

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
    SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
    final String Token = preferences.getString("Token", "");
    FirebaseAuth mAuth;
    FirebaseUser user = mAuth.getCurrentUser();
    String amount ="";
    String phone_number ="";

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

               // user.getEmail();
                amount = "2000";
                phone_number = "0700110561";
                Volley_Deposit_step_one(phone_number,amount,user.getEmail()+"_"+user.getUid(),Token);
                //number
                //amount
                //external_ref
                //sender_ref
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


  public void Volley_Deposit_step_one(String var_sender_phone, String var_amount, final String var_external_ref, String var_sender_reference ){


      try {
          RequestQueue requestQueue = Volley.newRequestQueue(this);
          String URL = "http://pocketmoney.infosis.uk/Api/";
          JSONObject jsonBody = new JSONObject();
          jsonBody.put("action", "initiate_mobile_money_payments");
          jsonBody.put("merchant_code", "337");
          jsonBody.put("sender_reference", var_sender_reference);
          jsonBody.put("sender_phone", var_sender_phone);
          jsonBody.put("amount", var_amount);
          jsonBody.put("external_ref", var_external_ref);

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
                          status_code = obj.getString("status_code");
                          message = obj.getString("message");
                          request_id = obj.getString("request_id");

                         // Log.i("Poketi", obj);
                          Log.d(TAG, request_id);
                          Toast.makeText(Deposit.this, status, Toast.LENGTH_SHORT).show();
                          Volley_Deposit_step_two(request_id, var_external_ref);

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

    public void Volley_Deposit_step_two(String var_request_id, final String var_external_ref){


        try {
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            String URL = "http://pocketmoney.infosis.uk/Api/";
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("action", "initiate_mobile_money_payments_confirmed");
            jsonBody.put("sender_phone", "256700110561");
            jsonBody.put("request_id", var_request_id);

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
                    String tx_status = "";
                    String tx_reference = "";
                    String message = "";
                    String action = "";



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
                            tx_reference = obj.getString("tx_reference");
                            tx_status = obj.getString("tx_status");
                            message = obj.getString("message");
                            status_code = obj.getString("status_code");
                            action =  obj.getString("action");

                            // Log.i("Poketi", obj);
                            Log.d(TAG, tx_reference);
                            Toast.makeText(Deposit.this, tx_status, Toast.LENGTH_SHORT).show();
                            Volley_Deposit_step_three(tx_reference,var_external_ref);

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

    public void Volley_Deposit_step_three(String var_tx_reference,String var_external_ref ){



        try {
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            String URL = "http://pocketmoney.infosis.uk/Api/";
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("action", "mobile_money_payments_check_status");
            jsonBody.put("merchant_code", "337");
            jsonBody.put("tx_reference", var_tx_reference);
            jsonBody.put("external_ref", var_external_ref);

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
                    String tx_status = "";
                    String amount = "";
                    String payer_phone = "";
                    String customer_reference = "";
                    String external_ref = "";
                    String tx_reference = "";

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
                            // Response to Check Transaction Request

                            status = obj.getString("status");
                            tx_status = obj.getString("tx_status");
                            amount = obj.getString("amount");
                            payer_phone = obj.getString("payer_phone");
                            customer_reference = obj.getString("customer_reference");
                            tx_reference = obj.getString("tx_reference");
                            external_ref = obj.getString("external_ref");
                            status_code = obj.getString("status_code");
                            message = obj.getString("message");



                            // Log.i("Poketi", obj);
                            Log.d(TAG, tx_status);

                            Toast.makeText(Deposit.this, tx_status, Toast.LENGTH_SHORT).show();

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
