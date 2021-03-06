package com.payrainbow.engineers.payrainbow;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.ContentViewEvent;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import io.fabric.sdk.android.Fabric;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, GoogleApiClient.OnConnectionFailedListener {
    // Choose an arbitrary request code value
    private static final String TAG = "PayRainbow_APP" ;
    private FirebaseAuth mAuth;
    //FirebaseUser currentUser = mAuth.getInstance().getCurrentUser();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

      //  Button Dashboardx = (Button)findViewById(R.id.Dashboard);
       // Button Approve = (Button)findViewById(R.id.Approve);
        Button Login = (Button)findViewById(R.id.Login);
        Button Logout = (Button)findViewById(R.id.logoutbtn);

        Logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "Logout.",Toast.LENGTH_SHORT).show();
                Answers.getInstance().logContentView(new ContentViewEvent()
                        .putContentName("Log-Out")
                        .putContentType("Logout")

                );
                FirebaseAuth.getInstance().signOut();
                Toast.makeText(MainActivity.this, "LoggedOut.",Toast.LENGTH_LONG).show();
            }
        });

        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "Login.",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MainActivity.this, Login.class);
                startActivity(intent);
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
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
          //  Toast.makeText(MainActivity.this, "MainActivity+Home",Toast.LENGTH_SHORT).show();

        } else if (id == R.id.login) {

          //  Toast.makeText(MainActivity.this, "MainActivity+Login",Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(MainActivity.this, Login.class);
            startActivity(intent);

        } else if (id == R.id.deposit) {

           // Toast.makeText(MainActivity.this, "MainActivity+Approve",Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(MainActivity.this, Deposit.class);
            startActivity(intent);
        } else if (id == R.id.dashboard) {
          //  Toast.makeText(MainActivity.this, "MainActivity+Dashboard",Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(MainActivity.this, DashCard.class);
            startActivity(intent);

        } else if (id == R.id.Register) {
           /* Toast.makeText(MainActivity.this, "Cardview",Toast.LENGTH_SHORT).show();
*/
            Intent intent = new Intent(MainActivity.this, Register.class);
            startActivity(intent);

        } else if (id == R.id.Pay) {
          //  Toast.makeText(MainActivity.this, "Pay Activity",Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(MainActivity.this,Pay.class);
            startActivity(intent);

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d(TAG, "onConnectionFailed:" + connectionResult);
    }

}
