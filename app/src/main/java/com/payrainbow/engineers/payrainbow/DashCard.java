package com.payrainbow.engineers.payrainbow;

import android.app.ProgressDialog;
import android.content.Intent;
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
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class DashCard extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener  {

    private static final String TAG = "PayRainbow_APP";
    private FirebaseAuth mAuth;
    FirebaseUser currentUser = mAuth.getInstance().getCurrentUser();
    TextView Balancex ;
    TextView Purchases ;
    TextView Card ;
    public ProgressDialog mProgressDialog;
    //Button Payx;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_card);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage("Loading........");
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.setCancelable(false);

        Balancex = (TextView) findViewById(R.id.balance_text);
        Purchases = (TextView) findViewById(R.id.purchases_text);
        Card = (TextView) findViewById(R.id.amount_text);
       // Payx = (Button) findViewById(R.id.paybtn);


        purchases();
        card();
        balance();


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
            }
        });
/*
        Payx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(v, "Processing Payment ....", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        */
    }
    public void showProgressDialog(){
        mProgressDialog.show();
    }

    public void hideProgressDialog(){
        mProgressDialog.hide();
    }


    public void purchases(){
        showProgressDialog();
        final String[] responsex = new String[1];
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);
        String url ="http://payrainbow.com/userdata_app.php?email="+currentUser.getEmail().toString()+"&data=purchases";

// Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        Purchases.setText(response.toString());
                        responsex[0] = response;
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Balancex.setText("Retry");
            }
        });
// Add the request to the RequestQueue.
        queue.add(stringRequest);
        // return responsex.toString() ;
        hideProgressDialog();
    }

    public void card(){
        showProgressDialog();
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);
        String url ="http://payrainbow.com/userdata_app.php?email="+currentUser.getEmail().toString()+"&data=card";

// Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        Card.setText(response.toString());
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Balancex.setText("Retry");
            }
        });
// Add the request to the RequestQueue.
        queue.add(stringRequest);
        hideProgressDialog();

    }

    public void balance(){
        showProgressDialog();
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);
        String url ="http://payrainbow.com/userdata_app.php?email="+currentUser.getEmail().toString()+"&data=balance";

// Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        Balancex.setText(response.toString());
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Balancex.setText("Retry");
            }
        });
// Add the request to the RequestQueue.
        queue.add(stringRequest);
        hideProgressDialog();
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.home) {
            // Handle the camera action
           // Toast.makeText(DashCard.this, "Dashboard+Home", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(DashCard.this, MainActivity.class);
            startActivity(intent);
        } else if (id == R.id.login) {

           // Toast.makeText(DashCard.this, "Dashboard+Login", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(DashCard.this, Login.class);
            startActivity(intent);

        } else if (id == R.id.deposit) {

           // Toast.makeText(DashCard.this, "Dashboard+Approve", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(DashCard.this, Deposit.class);
            startActivity(intent);

        } else if (id == R.id.dashboard) {
           // Toast.makeText(DashCard.this, "Dashboard+Dashboard", Toast.LENGTH_SHORT).show();
            //Toast.makeText(DashCard.this, "Cardview",Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(DashCard.this, DashCard.class);
            startActivity(intent);
        } else if (id == R.id.Register) {
            Toast.makeText(DashCard.this, "Log Out Please...", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(DashCard.this, Login.class);
            startActivity(intent);
        } else if (id == R.id.Pay) {

            Intent intent = new Intent(DashCard.this, Pay.class);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
