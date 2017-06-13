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
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class Register extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, GoogleApiClient.OnConnectionFailedListener{
    private static final int RC_SIGN_IN = 9001;
    private GoogleApiClient mGoogleApiClient;
    private static final String TAG = "PayRainbow_APP" ;
    private FirebaseAuth mAuth;
    EditText Emailtext,Passwordtext;
    public ProgressDialog mProgressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();


        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage("Loading........");
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.setCancelable(false);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // Configure sign-in to request the user's ID, email address, and basic
// profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
// Build a GoogleApiClient with access to the Google Sign-In API and the
// options specified by gso.
        // [START build_client]
        // Build a GoogleApiClient with access to the Google Sign-In API and the
        // options specified by gso.
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        // [START customize_button]
        // Set the dimensions of the sign-in button.
        SignInButton signInButton = (SignInButton) findViewById(R.id.sign_in_button);
        signInButton.setSize(SignInButton.SIZE_WIDE);

        Button Register = (Button)findViewById(R.id.EmailRegbtn);
        Emailtext = (EditText) findViewById(R.id.emailReg);
        Passwordtext = (EditText)findViewById(R.id.passwordReg);

        Register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createAccount(Emailtext.getText().toString(),Passwordtext.getText().toString());
            }
        });

        mAuth = FirebaseAuth.getInstance();

        //Button google = (Button)findViewById(R.id.sign_in_button);
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
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
    }

    /*
  @Override
  public void onStart() {
      super.onStart();
      // Check if user is signed in (non-null) and update UI accordingly.
      FirebaseUser currentUser = mAuth.getCurrentUser();

     // updateUI(currentUser);
  }
*/
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = result.getSignInAccount();
                Toast.makeText(this, "Registering....", Toast.LENGTH_SHORT).show();
                firebaseAuthWithGoogle(account);
                //Toast.makeText(this, "Loggedin", Toast.LENGTH_SHORT).show();

            } else {
                // Google Sign In failed, update UI appropriately
                // ...
                Toast.makeText(Register.this, "Authentication failed", Toast.LENGTH_SHORT).show();
            }
        }
    }
    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            // updateUI(user);
                            sendEmailVerification();
                          //  Toast.makeText(Register.this, "Registered Successfully", Toast.LENGTH_SHORT).show();
                          //  Intent intent = new Intent(Register.this,Login.class);
                           // startActivity(intent);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(Register.this, "Authentication failed.",Toast.LENGTH_SHORT).show();
                            // updateUI(null);
                            //  Toast.makeText(MainActivity.this, "LoggedOut", Toast.LENGTH_SHORT).show();
                        }

                        // ...
                    }
                });
    }
    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
        mGoogleApiClient.connect();
    }

    private void createAccount(String email, String password) {
        Log.d(TAG, "createAccount:" + email);
        if (!validateForm()) {
            return;
        }

        showProgressDialog();

        // [START create_user_with_email]
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                          //  updateUI(user);
                            sendEmailVerification();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(Register.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                          //  updateUI(null);
                        }

                        // [START_EXCLUDE]
                        hideProgressDialog();
                        // [END_EXCLUDE]
                    }
                });
        // [END create_user_with_email]
    }

    private boolean validateForm() {
        boolean valid = true;

        String email = Emailtext.getText().toString();
        if (TextUtils.isEmpty(email)) {
            Emailtext.setError("Required.");
            valid = false;
        } else {
            Emailtext.setError(null);
        }

        String password = Passwordtext.getText().toString();
        if (TextUtils.isEmpty(password)) {
            Passwordtext.setError("Required.");
            valid = false;
        } else {
            Passwordtext.setError(null);
        }

        return valid;
    }

    private void sendEmailVerification() {
        // Disable button
      //  findViewById(R.id.verify_email_button).setEnabled(false);

        // Send verification email
        // [START send_email_verification]
        final FirebaseUser user = mAuth.getCurrentUser();
        user.sendEmailVerification()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        // [START_EXCLUDE]
                        // Re-enable button
                      //  findViewById(R.id.verify_email_button).setEnabled(true);

                        if (task.isSuccessful()) {
                            Toast.makeText(Register.this,
                                    "Verification email sent to " + user.getEmail(),
                                    Toast.LENGTH_SHORT).show();

                              Intent intent = new Intent(Register.this,Login.class);
                             startActivity(intent);
                        } else {
                            Log.e(TAG, "sendEmailVerification", task.getException());
                            Toast.makeText(Register.this,
                                    "Failed to send verification email.",
                                    Toast.LENGTH_SHORT).show();
                        }
                        // [END_EXCLUDE]
                    }
                });
        // [END send_email_verification]
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
            //  Toast.makeText(Register.this, "Login+Home",Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(Register.this, MainActivity.class);
            startActivity(intent);

        } else if (id == R.id.login) {

            //  Toast.makeText(Register.this, "Login+Login",Toast.LENGTH_SHORT).show();

        } else if (id == R.id.deposit) {

            //  Toast.makeText(Register.this, "Login+Approve",Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(Register.this,Deposit.class);
            startActivity(intent);

        } else if (id == R.id.dashboard) {
            // Toast.makeText(Register.this, "Login+Dashboard",Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(Register.this,DashCard.class);
            startActivity(intent);

        } else if (id == R.id.Register) {
          Toast.makeText(Register.this, "Reister",Toast.LENGTH_SHORT).show();

//            Intent intent = new Intent(Register.this, Register.class);
//            startActivity(intent);


        } else if (id == R.id.Pay) {
            // Toast.makeText(Register.this, "Pay Activity",Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(Register.this,Pay.class);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }



}
