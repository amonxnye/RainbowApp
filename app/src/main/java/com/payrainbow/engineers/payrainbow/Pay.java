package com.payrainbow.engineers.payrainbow;

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
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

public class Pay extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener  {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarx);
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
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
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
            Toast.makeText(Pay.this, "Login+Home",Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(Pay.this, MainActivity.class);
            startActivity(intent);

        } else if (id == R.id.login) {

            Toast.makeText(Pay.this, "Login+Login",Toast.LENGTH_SHORT).show();

        } else if (id == R.id.deposit) {

            Toast.makeText(Pay.this, "Login+Approve",Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(Pay.this,Deposit.class);
            startActivity(intent);

        } else if (id == R.id.dashboard) {
            Toast.makeText(Pay.this, "Login+Dashboard",Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(Pay.this,DashCard.class);
            startActivity(intent);

        } else if (id == R.id.Pay) {
           /* Toast.makeText(Pay.this, "Cardview",Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(Pay.this, DashCard.class);
            startActivity(intent);
*/

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
