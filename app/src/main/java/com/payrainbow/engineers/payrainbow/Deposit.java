package com.payrainbow.engineers.payrainbow;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.preference.PreferenceManager;
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
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class Deposit extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = "PayRainbow_APP" ;

    private FirebaseAuth mAuth;

    String amount ="";
    String phone_number ="";
    String tryamount ="";
    Button depositbtn;
    EditText deposit_amount;
    TextView status_view;
    String tryfinalTx_reference = "",
            tryvar_external_ref = "",
            tryvar_sender_reference ="";
    FirebaseUser user ;
    public ProgressDialog mProgressDialog;
    public String value;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        final String Token = preferences.getString("Token", "");
        setContentView(R.layout.activity_approve);
        mAuth = FirebaseAuth.getInstance();

         user = mAuth.getCurrentUser();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        deposit_amount = (EditText)findViewById(R.id.amount_text);


        status_view = (TextView)findViewById(R.id.status);

        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage("Please wait about 1-3mins........");
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.setCancelable(false);


        depositbtn = (Button)findViewById(R.id.deposit_btn);
        depositbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // amount = "2000";
                phone_number = "0700110561";
                int min = 1;
                int max = 999999999;
                amount = deposit_amount.getText().toString();
                tryamount = amount;
                Random r = new Random();
                int userid_token_refrence = r.nextInt(max - min + 1) + min;

               tryvar_sender_reference = user.getEmail()+"_"+user.getUid()+"_"+userid_token_refrence ;
                Volley_Deposit_step_one(phone_number,amount,user.getEmail()+"_"+user.getUid()+"_"+userid_token_refrence,Token);

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
                  Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG).setAction("Action", null).show();

                // user.getEmail();
                amount = "2000";
                phone_number = "0700110561";

               // Volley_Deposit_step_one(phone_number,amount,user.getEmail()+"_"+user.getUid()+"_"+userid_token_refrence,Token);
                //number
                //amount
                //external_ref
                //sender_ref
            }
        });
    }

    public void showProgressDialog(){
        mProgressDialog.show();
    }

    public void hideProgressDialog(){
        mProgressDialog.hide();
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

        if (id == R.id.home) {
            // Handle the camera action
            //  Toast.makeText(Deposit.this, "Login+Home",Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(Deposit.this, MainActivity.class);
            startActivity(intent);

        } else if (id == R.id.login) {

            //  Toast.makeText(Deposit.this, "Login+Login",Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(Deposit.this,Login.class);
            startActivity(intent);

        } else if (id == R.id.deposit) {

            //  Toast.makeText(Deposit.this, "Login+Approve",Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(Deposit.this,Deposit.class);
            startActivity(intent);

        } else if (id == R.id.dashboard) {
            // Toast.makeText(Deposit.this, "Login+Dashboard",Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(Deposit.this,DashCard.class);
            startActivity(intent);

        } else if (id == R.id.Register) {
            Toast.makeText(Deposit.this, "Log Out Please...", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(Deposit.this, Login.class);
            startActivity(intent);

        } else if (id == R.id.Pay) {
            // Toast.makeText(Deposit.this, "Pay Activity",Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(Deposit.this,Pay.class);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    public void Volley_Deposit_step_one(String var_sender_phone, String var_amount, final String var_external_ref, String var_sender_reference ){

        showProgressDialog();

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
                            // Toast.makeText(Deposit.this, status, Toast.LENGTH_SHORT).show();

                            try {
                               // status_view.setText("Step 1");

                               // final String finalTx_reference = tx_reference;
                                final int secs = 20;

                                final String finalRequest_id = request_id;
                                Deposit.this.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        new CountDownTimer((secs +1)*1000,1000){

                                            @Override
                                            public void onTick(long millisUntilFinished) {
                                                Log.d(TAG, String.valueOf(millisUntilFinished +"**"+finalRequest_id+"**"+var_external_ref));
                                            }

                                            @Override
                                            public void onFinish() {

                                                Volley_Deposit_step_two(finalRequest_id, var_external_ref);
                                                //   Volley_Deposit_step_three(finalTx_reference,var_external_ref);
                                               // hideProgressDialog();
                                            }
                                        }.start();

                                    }
                                });
                            }catch (Exception e){
                                status_view.setText("Step 1_failed");
                            }


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

    public void Volley_Deposit_step_two(final String var_request_id, final String var_external_ref){


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
                           // Log.d(TAG, tx_reference);

                           // Toast.makeText(Deposit.this, tx_status, Toast.LENGTH_SHORT).show();

                            final String finalTx_reference = tx_reference;
                            final int secs = 30;

                            Deposit.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    new CountDownTimer((secs +1)*1000,1000){

                                        @Override
                                        public void onTick(long millisUntilFinished) {
                                            Log.d(TAG, String.valueOf(millisUntilFinished));
                                        }

                                        @Override
                                        public void onFinish() {
                                            tryfinalTx_reference = finalTx_reference;
                                            tryvar_external_ref = var_external_ref;
                                         //   Volley_Deposit_step_three(finalTx_reference,var_external_ref);

                                            //Send to DB
                                            Volley_Deposit_server(finalTx_reference,var_external_ref,user.getEmail(),phone_number,var_request_id,tryamount);


                                        }
                                    }.start();

                                }
                            });


                            Log.d(TAG, tx_reference);
                            // status_view.setText("Step 2");



                        } catch (JSONException e) {
                            e.printStackTrace();
                        }




                    }
                    return Response.success(responseString, HttpHeaderParser.parseCacheHeaders(response));
                    //return Response.success(request_id);

                }
            };

            stringRequest.setRetryPolicy(new RetryPolicy() {
                @Override
                public int getCurrentTimeout() {
                    return 5000;
                }

                @Override
                public int getCurrentRetryCount() {
                    return 50000;
                }

                @Override
                public void retry(VolleyError error) throws VolleyError {

                }
            });

            requestQueue.add(stringRequest);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void Volley_Deposit_server(String var_tx_reference, String var_external_ref, String email, String phone_number, String var_request_id,String var_amount) {

        try {
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            String URL = "http://payrainbow.com/depox_server.php";
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("email", email);
            jsonBody.put("phone", phone_number);
            jsonBody.put("merchant_code", "337");
            jsonBody.put("amount", var_amount);
            jsonBody.put("request_id", var_request_id);
            jsonBody.put("tx_reference", var_tx_reference);
            jsonBody.put("external_ref", var_external_ref);
            jsonBody.put("sender_reference", tryvar_sender_reference);

            final String requestBody = jsonBody.toString();
            // final String requestBodyJson = jsonBody.toString();


            StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.i("Payrainbow_server", response);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("Payrainbow_server", error.toString());
                }
            }) {
                @Override
                public String getBodyContentType() {
                    return "application/json; charset=utf-8";
                }

                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    HashMap<String, String> params = new HashMap<String, String>();
                    //params.put("Authorization-Key", "CV8FJVBHM6KXHEHUPYGWJJKFYEEWUQN8CKTAXNGRWRUQN9YAHN3");
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

                    String tx_status = "";
                    String status = "";


                    if (response != null) {
                        responseString = String.valueOf(response.statusCode);
                        // can get more details such as response.headers
                        try {

                            json = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }

                        Log.d(TAG, "Done"+json);
                      //  Toast.makeText(Deposit.this,json.toString(), Toast.LENGTH_SHORT).show();

                        value = json;

                        RemoveDialogue(value.toString());


                    }
                    return Response.success(responseString, HttpHeaderParser.parseCacheHeaders(response));
                    //return Response.success(request_id);

                }
            };

            requestQueue.add(stringRequest);

            hideProgressDialog();
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

                            final String finalTx_status = tx_status;
                            final int secs = 5;

                            Deposit.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    new CountDownTimer((secs +1)*1000,1000){

                                        @Override
                                        public void onTick(long millisUntilFinished) {
                                            Log.d(TAG, String.valueOf(millisUntilFinished));
                                        }

                                        @Override
                                        public void onFinish() {
                                            Log.d(TAG, finalTx_status);
                                                }
                                    }.start();

                                }
                            });
                            final int secs2 =60;
                            Deposit.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    new CountDownTimer((secs2 +1)*1000,1000){

                                        @Override
                                        public void onTick(long millisUntilFinished) {
                                            Log.d(TAG, String.valueOf(millisUntilFinished));
                                        }

                                        @Override
                                        public void onFinish() {
                                            Log.d(TAG, finalTx_status);
                                            Log.d(TAG, tryfinalTx_reference + "__"+tryvar_external_ref);

                                            if(finalTx_status =="PENDING"){
                                                Volley_Deposit_step_three(tryfinalTx_reference,tryvar_external_ref);
                                            }else if(finalTx_status =="") {
                                                Toast.makeText(Deposit.this, "Failed", Toast.LENGTH_SHORT).show();
                                            }else if(finalTx_status =="SUCCEEDED") {
                                                Toast.makeText(Deposit.this, finalTx_status, Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    }.start();

                                }
                            });


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

    public void Display(String textdata){
        //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG).setAction("Action", null).show();
        Toast.makeText(Deposit.this, textdata, Toast.LENGTH_SHORT).show();
        status_view.setText("Done");

        hideProgressDialog();

    }

    public void RemoveDialogue(String datav){
      //  hideProgressDialog();
        //Snackbar.make(view, datav, Snackbar.LENGTH_LONG).setAction("Action", null).show();

      //  Toast.makeText(this, datav, Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(Deposit.this,DashCard.class);
        intent.putExtra("Status_Poketi",datav);
        startActivity(intent);
    }


}
