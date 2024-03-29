package com.alvindrakes.loginpage;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;


public class Login extends AppCompatActivity {
  
  private Button check;
  private EditText emailText;
  private EditText passwordText;
  private Button Sign_up_btn;
  
  //Firebase variables
  private FirebaseAuth mAuth;
  
  //Google variables
  private static final int RC_SIGN_IN = 9001;
  private static final String TAG = "GoogleActivity";
  GoogleSignInOptions gso;
  GoogleSignInClient mGoogleSignInClient;
  
  
  @Override
  protected void onCreate (Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    getSupportActionBar().hide();
    getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                         WindowManager.LayoutParams.FLAG_FULLSCREEN);
    setContentView(R.layout.login);
    
    //Initialize variables
    check = (Button) findViewById(R.id.login);
    emailText = (EditText) findViewById(R.id.email);
    passwordText = (EditText) findViewById(R.id.password);
    Sign_up_btn = (Button) findViewById(R.id.sign_up_button);
    
    Button googleSignIn = (Button) findViewById(R.id.google_signinBtn);
    
    mAuth = FirebaseAuth.getInstance();
    
    //Check if any space is left empty else login
    check.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick (View view) {
        if (TextUtils.isEmpty(emailText.getText().toString())) {
          emailText.setError("Field is empty");
        } else if (TextUtils.isEmpty(passwordText.getText().toString())) {
          passwordText.setError("Field is empty");
        } else {
          login();
        }
      }
    });
    
    gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestIdToken(
        "150473942816-5l8dnhdvh15m5ratk82eql5eqlro8f9r.apps.googleusercontent.com")
        .requestEmail()
        .build();
    
    mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
    
    googleSignIn.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick (View view) {
        signIn();
      }
    });
    
    
    Sign_up_btn.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick (View v) {
        Intent startIntent = new Intent(Login.this, SignupPage.class);
        startActivity(startIntent);
      }
    });
  }
  
  //Google sign in form
  private void signIn () {
    Intent signInIntent = mGoogleSignInClient.getSignInIntent();
    startActivityForResult(signInIntent, RC_SIGN_IN);
  }
  
  //Login with email/password
  private void login () {
    mAuth.signInWithEmailAndPassword(emailText.getText().toString(),
                                     passwordText.getText().toString())
        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
          @Override
          public void onComplete (@NonNull Task<AuthResult> task) {
            if (task.isSuccessful()) {
              // Sign in success, update UI with the signed-in user's information
              Log.d("LogIn", "signInWithEmail:success");
              Toast.makeText(Login.this, "Log in successfully.", Toast.LENGTH_SHORT).show();
              
              Intent intent = new Intent(Login.this, MainActivity.class);
              startActivity(intent);
              
            } else {
              // If sign in fails, display a message to the user.
              Log.w("LogIn", "signInWithEmail:failure", task.getException());
              Toast.makeText(Login.this,
                             task.getException().getLocalizedMessage(),
                             Toast.LENGTH_SHORT).show();
            }
            
            // ...
          }
        });
  }
  
  
  @Override
  public void onActivityResult (int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    
    // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
    if (requestCode == RC_SIGN_IN) {
      Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
      try {
        // Google Sign In was successful, authenticate with Firebase
        GoogleSignInAccount account = task.getResult(ApiException.class);
        firebaseAuthWithGoogle(account);
        
      } catch (ApiException e) {
        // Google Sign In failed, update UI appropriately
        Log.w(TAG, "Google sign in failed", e);
        Log.e("error", e.getMessage());
        Toast.makeText(this, "google " + e.getMessage(), Toast.LENGTH_SHORT).show();
        // ...
      }
    }
  }
  
  //Authenticate Firebase with Google
  private void firebaseAuthWithGoogle (final GoogleSignInAccount acct) {
    Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());
    
    AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
    FirebaseAuth.getInstance()
        .signInWithCredential(credential)
        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
          @Override
          public void onComplete (@NonNull Task<AuthResult> task) {
            if (task.isSuccessful()) {
              // Login to main page
              Intent mainIntent = new Intent(Login.this, LoginAuthentication.class);
              mainIntent.putExtra("auth", true);
              startActivity(mainIntent);
            } else {
              // If sign in fails, display a message to the user.
              Log.w(TAG, "signInWithCredential:failure", task.getException());
              Toast.makeText(Login.this,
                             "firebase " + task.getException().getMessage(),
                             Toast.LENGTH_SHORT).show();
            }
            
          }
        });
  }
  
  @Override
  protected void onStart () {
    super.onStart();
    FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    if (firebaseUser != null) {
      Toast.makeText(Login.this,
                     "Welcome back, " + firebaseUser.getDisplayName(),
                     Toast.LENGTH_SHORT).show();
      Intent mainIntent = new Intent(Login.this, MainActivity.class);
      startActivity(mainIntent);
    }
  }
}
