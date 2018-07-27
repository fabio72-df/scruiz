package com.cursoandroid.firebaseapp.scruiz.SubClasses;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.cursoandroid.firebaseapp.scruiz.BaseActivity;
import com.cursoandroid.firebaseapp.scruiz.IniciarTeste;
import com.cursoandroid.firebaseapp.scruiz.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;


/////////////////////////////////////////////////
/* Login2: Efetuar LOGIN com e-mail ou anônimo */
/////////////////////////////////////////////////

public class Login2 extends BaseActivity implements View.OnClickListener {

    private static final String TAG = "EmailPassword";
    private static final int RC_SIGN_IN = 9001;

    // [START declare_auth]
    private FirebaseAuth mAuth;
    private FirebaseDatabase mFirebase;
    // [END declare_auth]

    private int tipoLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // [START initialize_auth]
        mAuth = FirebaseAuth.getInstance();
        // [END initialize_auth]

        // Informação recebida de LoginPrimeiraActivity: 0 = normal (email) | 1 = "Login anonimo"
        Bundle extra = getIntent().getExtras();
        tipoLogin = extra.getInt("tipo");

        if (tipoLogin == 0) {
            setContentView(R.layout.activity_login2);
            //BARRA SUPERIOR
            Toolbar myToolbar = findViewById(R.id.my_toolbar);
            //setActionBar(myToolbar);
            setSupportActionBar(myToolbar);
            //myToolbar.setTitleTextColor(R.color.primaryTextColor);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (tipoLogin == 1) {
            signIn("scruiz@scruiz.br", "147036");
        } else {
            signInNormal();
        }

    }

    protected void signInNormal() {
        EditText loginText;
        EditText passText;

        loginText = (EditText) findViewById(R.id.login_page_social_login_text);
        passText = (EditText) findViewById(R.id.login_page_social_login_password);

        Typeface sRobotoThin = Typeface.createFromAsset(getAssets(),
                "font/roboto_thin.ttf");

        loginText.setTypeface(sRobotoThin);
        passText.setTypeface(sRobotoThin);


        TextView login, register, skip;

        login = (TextView) findViewById(R.id.login);
        register = (TextView) findViewById(R.id.register);
        skip = (TextView) findViewById(R.id.skip);

        login.setOnClickListener(this);
        register.setOnClickListener(this);
        skip.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v instanceof TextView) {
            TextView tv = (TextView) v;
            Toast.makeText(this, tv.getText(), Toast.LENGTH_SHORT).show();
        }
    }

    protected void signIn(String email, String password) {
        Log.d(TAG, "LOGIN ####:" + email);
        showProgressDialog();
        // [START sign_in_with_email]
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "LOGIN: Sucesso!");
                            FirebaseUser user = mAuth.getCurrentUser();
                            Toast.makeText(Login2.this, "Login com sucesso.", Toast.LENGTH_LONG).show();
                            startActivity(new Intent(Login2.this, IniciarTeste.class));
                            //
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "LOGIN FALHOU", task.getException());
                            // TODO
                            String erro = task.getException().getMessage();
                            if (erro.equals("There is no user record corresponding to this identifier. The user may have been deleted.")) {
                                Toast.makeText(Login2.this, "Usuário ainda não cadastradado.", Toast.LENGTH_LONG).show();
                                hideKeyboard(findViewById(R.id.email));
                                hideKeyboard(findViewById(R.id.password));
                            } else {
                                Toast.makeText(Login2.this, erro, Toast.LENGTH_LONG).show();
                            }
                        }
                        // [START_EXCLUDE]
                        if (!task.isSuccessful()) {
                            Toast.makeText(Login2.this, R.string.error_login_generico, Toast.LENGTH_SHORT).show();
                        }
                        hideProgressDialog();
                        // [END_EXCLUDE]
                    }
                });
        // [END sign_in_with_email]
    }

/// FIM DE TUDO
}
