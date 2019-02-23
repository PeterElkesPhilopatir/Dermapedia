package peter.com.task;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {
    RequestQueue queue;
    CallbackManager facebookCallbackManager;
    private FirebaseAuth mAuth;
    private static final int RC_SIGN_IN = 9001;
    private GoogleSignInClient mGoogleSignInClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
        setContentView(R.layout.activity_login);

        queue = Volley.newRequestQueue(this);
        initSocialControls();

        findViewById(R.id.btn_Login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText emailEditText = (EditText) findViewById(R.id.edt_email);
                EditText passwordEditText = (EditText) findViewById(R.id.edt_password);
                manualLogin(emailEditText.getText().toString(), passwordEditText.getText().toString());
            }
        });

        findViewById(R.id.txt_register).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString(getString(R.string.LOGIN_TYPE), getString(R.string.manual));
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }

    void manualLoginVolley(final String email, final String password) {
        HashMap<String, String> map = new HashMap<>();
        map.put("email", email);
        map.put("password", password);
        final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this);
        progressDialog.setTitle("Login");
        progressDialog.setMessage("Please Wait...");
        progressDialog.show();
        String url = "PUT YOUR URL HERE";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        Log.i("LOGIN_RESULT", response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                // the POST parameters:
                params.put("email", email);
                params.put("password", password);
                Log.i("LOGIN_CRED", params.get("email") + params.get("password"));
                return params;
            }
        };
        Log.i("LOGIN_REQUEST", stringRequest.getBodyContentType());
        queue.add(stringRequest);
    }

    void manualLogin(String email, String password) {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("LOGIN");
        progressDialog.setMessage("Please wait...");
        progressDialog.show();
        String url = "PUT YOUR URL HERE";
        Ion.with(LoginActivity.this)
                .load("POST", url)
                .setBodyParameter("email", email)
                .setBodyParameter("password", password)
                .asString()
                .setCallback(new FutureCallback<String>() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onCompleted(Exception e, String result) {
                        progressDialog.dismiss();
                        //check error
                        if (e != null) {
                            Log.e("ERROR", "" + e.getMessage());
                        } else {
                            Log.i("LOGIN_RESULT_ION", result);
                            try {
                                JSONObject jsonObject = new JSONObject(result);
                                boolean status = jsonObject.getBoolean("status");
                                if (status)
                                    Toast.makeText(LoginActivity.this, "LOGIN_SA7", Toast.LENGTH_SHORT).show();
                                else {
                                    Toast.makeText(LoginActivity.this, jsonObject.getString("error"), Toast.LENGTH_SHORT).show();

                                }
                            } catch (JSONException e1) {
                                e1.printStackTrace();
                            }

                        }
                    }
                });

    }

    void socialLogin(final FirebaseUser user, final String socialType) {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("LOGIN");
        progressDialog.setMessage("Please wait...");
        progressDialog.show();
        String url = "PUT YOUR URL HERE";
        Ion.with(LoginActivity.this)
                .load("POST", url)
                .setBodyParameter("social_id", user.getEmail())
                .setBodyParameter("social_type", socialType)
                .asString()
                .setCallback(new FutureCallback<String>() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onCompleted(Exception e, String result) {
                        progressDialog.dismiss();
                        //check error
                        if (e != null) {
                            Log.e("ERROR", "" + e.getMessage());
                        } else {
                            Log.i("LOGIN_RESULT_ION", result);
                            try {
                                JSONObject jsonObject = new JSONObject(result);
                                boolean status = jsonObject.getBoolean("status");
                                if (status)
                                    Toast.makeText(LoginActivity.this, "LOGIN_SA7", Toast.LENGTH_SHORT).show();
                                else {
                                    Toast.makeText(LoginActivity.this, jsonObject.getString("error"), Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
                                    Bundle bundle = new Bundle();
                                    bundle.putString(getString(R.string.display_name_key), user.getDisplayName());
                                    bundle.putString(getString(R.string.social_id_key), user.getEmail());
                                    bundle.putString(getString(R.string.social_type_key), socialType);
                                    bundle.putString(getString(R.string.social_image_key), user.getPhotoUrl().toString());
                                    bundle.putString(getString(R.string.LOGIN_TYPE), getString(R.string.social));
                                    intent.putExtras(bundle);
                                    startActivity(intent);
                                }
                            } catch (JSONException e1) {
                                e1.printStackTrace();
                            }

                        }
                    }
                });

    }

    private void handleFacebookAccessToken(AccessToken token) {
        Log.i("FACEBOOK_RESULT", "handleFacebookAccessToken:" + token);
        // [START_EXCLUDE silent]
//        showProgressDialog();
        // [END_EXCLUDE]

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.i("FACEBOOK_RESULT", "signInWithCredential:success");
                            try {
                                FirebaseUser user = mAuth.getCurrentUser();
                                Log.i("FACEBOOK_EMAIL", user.getEmail());
                                socialLogin(user, "Facebook");
                            } catch (Exception e) {
                                Toast.makeText(LoginActivity.this, "NO EMAIL", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.i("FACEBOOK_RESULT", "signInWithCredential:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }

                        // [START_EXCLUDE]
//                        hideProgressDialog();
                        // [END_EXCLUDE]
                    }
                });
    }

    @Override
    public void onStart() {
        super.onStart();
        initFirebase();

//        if (mVerificationInProgress && validatePhoneNumber()) {
//            startPhoneNumberVerification(peter_phoneNumber);
//        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                Log.i("FIREBASE_LOGIN", "Google sign in failed", e);
            }
        } else {
            facebookCallbackManager.onActivityResult(requestCode, resultCode, data);

        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.i("FIREBASE_LOGIN", "firebaseAuthWithGoogle:" + acct.getId());
        // [START_EXCLUDE silent]
        // [END_EXCLUDE]

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.i("FIREBASE_LOGIN", "signInWithCredential:success");
                            try {
                                FirebaseUser user = mAuth.getCurrentUser();
                                Log.i("GMAIL", user.getEmail());
                                socialLogin(user, "Google");
                            } catch (Exception e) {
                                Toast.makeText(LoginActivity.this, "NO EMAIL", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.i("FIREBASE_LOGIN", "signInWithCredential:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication Failed", Toast.LENGTH_SHORT).show();
                        }

                        // [START_EXCLUDE]
                        // [END_EXCLUDE]
                    }
                });
    }

    private void GoogleSignin() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    void initFirebase() {

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        mAuth = FirebaseAuth.getInstance();

        FirebaseUser currentUser = mAuth.getCurrentUser();


    }

    void initSocialControls() {
        findViewById(R.id.signInButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GoogleSignin();
            }
        });

        facebookCallbackManager = CallbackManager.Factory.create();
        LoginButton loginButton = findViewById(R.id.buttonFacebookLogin);
        loginButton.setReadPermissions("email", "public_profile");
        loginButton.registerCallback(facebookCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.i("FACEBOOK_RESULT", "facebook:onSuccess:" + loginResult);
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Log.i("FACEBOOK_RESULT", "facebook:onCancel");
            }

            @Override
            public void onError(FacebookException error) {
                Log.e("FACEBOOK_RESULT", "facebook:onError", error);
            }
        });


    }
}
