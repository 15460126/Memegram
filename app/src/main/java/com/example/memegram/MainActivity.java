package com.example.memegram;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.pm.PackageManager;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import static android.Manifest.permission.CAMERA;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    private EditText txtEmail;
    private EditText txtPass;
    private Button btnBut;
    private ConstraintLayout viewMain;
    private TextView txtButt;
    private static final int MY_PERMISSIONS=100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        txtEmail = findViewById(R.id.txtEmail);
        txtPass = findViewById(R.id.txtPass);
        btnBut = findViewById(R.id.btnBut);
        viewMain = findViewById(R.id.Container);
        txtButt = findViewById(R.id.txtButt);

        txtPass.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_GO) {
                    loginUser();
                }
                return false;
            }
        });
        txtEmail.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                changeButtonClickableStatus(event);
                return false;
            }
        });
        btnBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(AnimationUtils.loadAnimation(MainActivity.this, R.anim.click_animation));

                loginUser();
            }
        });
        txtButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mayRequestPermission();
            }
        });
    }

    public void loginUser() {
        String email = txtEmail.getText().toString();
        String password = txtPass.getText().toString();
        if (email.length() > 0 && password.length() > 0) {

        }

        if (userCanLoginIn(email, password)) {
            authenticateUser(email, password);
        }
    }
private boolean mayRequestPermission(){
        if (ContextCompat.checkSelfPermission(MainActivity.this,CAMERA) == PackageManager.PERMISSION_GRANTED){
            return true;
        }else {
            ActivityCompat.requestPermissions(MainActivity.this,new String[]{CAMERA},MY_PERMISSIONS);
            return false;
        }
}
    private boolean userCanLoginIn(String email, String password) {
        if (email.length() < 1) {
            txtEmail.setError("Error compa, no hay nada");
        }
        if (password.length() < 1) {
            txtPass.setError("No lo haga compa");
        }
        if (email.length() > 0 && password.length() > 0) {
            return false;
        } else {
            return true;

        }
    }

    public void changeButtonClickableStatus(KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_UP) {
            if (txtPass.getText().toString().length() > 0 && txtEmail.getText().toString().length() > 0) {
                btnBut.setEnabled(false);
            } else {
                btnBut.setEnabled(true);
            }
        }
    }

    public void authenticateUser(String email, String password) {
     /*  final ProgressDialog progressDialog=new ProgressDialog(MainActivity.this);
       progressDialog.setMessage("Iniciando Sesión");
       progressDialog.setCancelable(false);
       progressDialog.show();*/
        AndroidNetworking.post("https://calm-headland-52897.herokuapp.com")
                .addBodyParameter("email", email)
                .addBodyParameter("password", password)
                .setTag("Test")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    String userName = "null";

                    @Override
                    public void onResponse(JSONObject response) {
                       //progressDialog.dismiss();
                        try {
                            userName = response.getString("nombre");
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Snackbar.make(viewMain, "Error Iniciando Sesión Knal", Snackbar.LENGTH_LONG).show();
                        }
                        if (userName == "null") {
                            Snackbar.make(viewMain, "Error iniciando sesión, viejo", Snackbar.LENGTH_SHORT).show();
                        } else {
                        Toast.makeText(MainActivity.this,"Error compa",Toast.LENGTH_SHORT ).show();
                        }
                    }

                    @Override
                    public void onError(ANError error) {
                        Log.e("Error", error.toString());
                        Snackbar.make(viewMain,"Error iniciando sesión",Snackbar.LENGTH_SHORT).show();
                    }
                });

    }
}

