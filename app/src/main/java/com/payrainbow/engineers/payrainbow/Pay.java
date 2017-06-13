package com.payrainbow.engineers.payrainbow;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Pay extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener  {

    private static final String TAG = "PayRainbow_APP";
    private FirebaseAuth mAuth;
    FirebaseUser currentUser = mAuth.getInstance().getCurrentUser();

    EditText codex,cardx,amountx;
    String code ="";
    String card = "";
    String amount="";
    public ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarx);
        setSupportActionBar(toolbar);

        final Button paybutton = (Button)findViewById(R.id.paybtn);
        Button codebutton = (Button)findViewById(R.id.codebtn);
          codex = (EditText)findViewById(R.id.code_number);
         cardx = (EditText)findViewById(R.id.card_number);
         amountx = (EditText)findViewById(R.id.amount_text);

        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage("Loading........");
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.setCancelable(false);



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

     /*   FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
*/

        paybutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               paybutton.setEnabled(true);
                getpay();

               // Toast.makeText(Pay.this, "Pay Hello World", Toast.LENGTH_SHORT).show();
            }
        });

        codebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getcode();

            }
        });
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.home) {
            // Handle the camera action
           // Toast.makeText(Pay.this, "Login+Home",Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(Pay.this, MainActivity.class);
            startActivity(intent);

        } else if (id == R.id.login) {

            Toast.makeText(Pay.this, "Login+Login",Toast.LENGTH_SHORT).show();

        } else if (id == R.id.deposit) {

          //  Toast.makeText(Pay.this, "Login+Approve",Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(Pay.this,Deposit.class);
            startActivity(intent);

        } else if (id == R.id.dashboard) {
           // Toast.makeText(Pay.this, "Login+Dashboard",Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(Pay.this,DashCard.class);
            startActivity(intent);

        } else if (id == R.id.Register) {
            Toast.makeText(Pay.this, "Log Out Please...", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(Pay.this, Login.class);
            startActivity(intent);

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void showProgressDialog(){
        mProgressDialog.show();
    }

    public void hideProgressDialog(){
        mProgressDialog.hide();
    }

    public void getcode(){

        showProgressDialog();
        final String[] responsex = new String[1];
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);
        String url ="http://payrainbow.com/authy_aws_app.php?email="+currentUser.getEmail().toString();

// Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                      //  Purchases.setText(response.toString());
                      //  responsex[0] = response;
                        Toast.makeText(Pay.this, "Code Sent", Toast.LENGTH_SHORT).show();
                        hideProgressDialog();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
               // Balancex.setText("Retry");
                Toast.makeText(Pay.this, "Code Failed", Toast.LENGTH_SHORT).show();
                hideProgressDialog();
            }
        });
// Add the request to the RequestQueue.
        queue.add(stringRequest);
        // return responsex.toString() ;
    }

    public void getpay(){

        showProgressDialog();
        card = cardx.getText().toString();
        code = codex.getText().toString();
        amount = amountx.getText().toString();

        Log.d(TAG,"**"+card+"**"+code+"**"+amount+"**");

        final String[] responsex = new String[1];
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);
        String url ="http://payrainbow.com/app_pay.php?email="+currentUser.getEmail().toString()+"&card="+card+"&amount="+amount+"&code="+code;

// Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        //  Purchases.setText(response.toString());
                        //  responsex[0] = response;
                        //  responsex[0] = response;
                        Log.d(TAG,response.toString());
                        //Toast.makeText(Pay.this, response.toString(), Toast.LENGTH_SHORT).show();
                        Toast.makeText(Pay.this, response.toString(), Toast.LENGTH_LONG).show();
                        hideProgressDialog();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // Balancex.setText("Retry");
                Toast.makeText(Pay.this, "PaymentFailed", Toast.LENGTH_SHORT).show();
                hideProgressDialog();
            }
        });
// Add the request to the RequestQueue.
        queue.add(stringRequest);
        // return responsex.toString() ;
        }

}
