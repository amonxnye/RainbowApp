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
import android.text.Html;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.ContentViewEvent;
import com.crashlytics.android.answers.CustomEvent;
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

import io.fabric.sdk.android.Fabric;

public class Login extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, GoogleApiClient.OnConnectionFailedListener{
private static final int RC_SIGN_IN = 9001;
    private static final String TAG = "PayRainbow_APP" ;
    public ProgressDialog mProgressDialog;
    public TextView register,forget;
    EditText Emailtext,Passwordtext;
    private GoogleApiClient mGoogleApiClient;
    private FirebaseAuth mAuth;
    FirebaseUser currentUser = mAuth.getInstance().getCurrentUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Fabric.with(this, new Crashlytics());
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

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

        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage("Loading........");
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.setCancelable(false);

        final Button Login = (Button)findViewById(R.id.Emailloginbtn);
        Emailtext = (EditText) findViewById(R.id.email);
        Passwordtext = (EditText)findViewById(R.id.password);

        register = (TextView)findViewById(R.id.registerlink);
        forget = (TextView)findViewById(R.id.forgetlink);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this, Register.class);
                startActivity(intent);
            }
        });

        forget.setMovementMethod(LinkMovementMethod.getInstance());



        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Login.setEnabled(true);
                EmailsignIn(Emailtext.getText().toString(),Passwordtext.getText().toString());
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
      if(currentUser.getUid().isEmpty()){

      }else {
          try {
              logUser();

              Intent intent = new Intent(Login.this,DashCard.class);
            //  intent.putExtra("Status_Poketi",datav);
              startActivity(intent);

          }catch (Exception e){
             // RemoveDialogue("Failed");
          }
      }
      // TODO: Move this to where you establish a user session



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
                Toast.makeText(this, "Logging....", Toast.LENGTH_SHORT).show();
                firebaseAuthWithGoogle(account);
                //Toast.makeText(this, "Loggedin", Toast.LENGTH_SHORT).show();

            } else {
                // Google Sign In failed, update UI appropriately
                // ...
                Toast.makeText(Login.this, "Failed", Toast.LENGTH_SHORT).show();
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
                            Toast.makeText(Login.this, "Loggedin", Toast.LENGTH_SHORT).show();
                            // TODO: Use your own attributes to track content views in your app
                            Answers.getInstance().logCustom(new CustomEvent("Logins")

                                    .putCustomAttribute("User ID", user.getUid())
                                    .putCustomAttribute("email", user.getEmail())
                                    .putCustomAttribute("type", "Google")

                            );
                            Intent intent = new Intent(Login.this,DashCard.class);
                            startActivity(intent);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(Login.this, "Authentication failed.",Toast.LENGTH_SHORT).show();
                            // updateUI(null);
                            // TODO: Use your own attributes to track content views in your app
                            Answers.getInstance().logCustom(new CustomEvent("Logins")
                                    .putCustomAttribute("User ID", "null")
                                    .putCustomAttribute("email", "null")
                                    .putCustomAttribute("type", "failedGoogle")

                            );
                            //  Toast.makeText(MainActivity.this, "LoggedOut", Toast.LENGTH_SHORT).show();
                        }

                        // ...
                    }
                });
    }

    private void signIn() {
       // showProgressDialog();
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
        mGoogleApiClient.connect();
      //  hideProgressDialog();

    }

    private void logUser() {
        // TODO: Use the current user's information
        // You can call any combination of these three methods
        Crashlytics.setUserIdentifier(currentUser.getUid().toString());
        Crashlytics.setUserEmail(currentUser.getEmail().toString());
        Crashlytics.setUserName(currentUser.getDisplayName().toString());
    }
    public void showProgressDialog(){
        mProgressDialog.show();
    }

    public void hideProgressDialog(){
        mProgressDialog.hide();
    }

    private void EmailsignIn(String email, String password) {
        Log.d(TAG, "signIn:" + email);
        if (!validateForm()) {
            return;
        }

        showProgressDialog();

        // [START sign_in_with_email]
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                          //  updateUI(user);
                            Toast.makeText(Login.this, "Loggedin", Toast.LENGTH_SHORT).show();
                            // TODO: Use your own attributes to track content views in your app
                            Answers.getInstance().logCustom(new CustomEvent("Logins")

                                    .putCustomAttribute("User ID", user.getUid())
                                    .putCustomAttribute("email", user.getEmail())
                                    .putCustomAttribute("type", "EmailPassword")

                            );
                            Intent intent = new Intent(Login.this,DashCard.class);
                            startActivity(intent);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(Login.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            // TODO: Use your own attributes to track content views in your app
                            Answers.getInstance().logCustom(new CustomEvent("Logins")
                                    .putCustomAttribute("User ID", "null")
                                    .putCustomAttribute("email", "null")
                                    .putCustomAttribute("type", "failedEmailPassword")

                            );
                           // updateUI(null);
                        }

                        // [START_EXCLUDE]
                        if (!task.isSuccessful()) {

                          //  mStatusTextView.setText(R.string.auth_failed);
                        }
                        hideProgressDialog();
                        // [END_EXCLUDE]
                    }
                });
        // [END sign_in_with_email]
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
          //  Toast.makeText(Login.this, "Login+Home",Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(Login.this, MainActivity.class);
            startActivity(intent);

        } else if (id == R.id.login) {

          //  Toast.makeText(Login.this, "Login+Login",Toast.LENGTH_SHORT).show();

        } else if (id == R.id.deposit) {

          //  Toast.makeText(Login.this, "Login+Approve",Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(Login.this,Deposit.class);
            startActivity(intent);

        } else if (id == R.id.dashboard) {
           // Toast.makeText(Login.this, "Login+Dashboard",Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(Login.this,DashCard.class);
            startActivity(intent);

        } else if (id == R.id.Register) {
            Intent intent = new Intent(Login.this, Register.class);
            startActivity(intent);

        } else if (id == R.id.Pay) {
           // Toast.makeText(Login.this, "Pay Activity",Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(Login.this,Pay.class);
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
