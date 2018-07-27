package com.cursoandroid.firebaseapp.scruiz;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.TextView;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class LoginActivity extends BaseActivity implements
        View.OnClickListener {

    private static final String TAG = "EmailPassword";
    private static final int RC_SIGN_IN = 9001;

    // [UI]
    private TextView mUsuarioLogado;
    private AutoCompleteTextView mEmailEditView;
    private EditText mPasswordEditView;

    // [START declare_auth]
    private FirebaseAuth mAuth;
    private FirebaseDatabase mFirebase;
    // [END declare_auth]

    private GoogleSignInClient mGoogleSignInClient;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Text e Edit Views
        mUsuarioLogado = findViewById(R.id.usuario_logado);
        mEmailEditView = findViewById(R.id.email);
        mPasswordEditView = findViewById(R.id.password);
        // TODO
        // VER SE É NECESSÁRIO (ACIONAR AUTOMÁTICO QUANDO ACABA DE PREENCHER
        mPasswordEditView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                    return true;
                }
                return false;
            }
        });
        // Buttons
        findViewById(R.id.email_sign_in_button).setOnClickListener(this);
        findViewById(R.id.email_sign_in_cadastrar).setOnClickListener(this);
        findViewById(R.id.email_sign_out_button).setOnClickListener(this);
        findViewById(R.id.email_sign_out_button).setVisibility(View.GONE);
        // TODO CONFIGURAR E-MAIL DE VERIFICACAO
        //findViewById(R.id.verify_email_button).setOnClickListener(this);
        findViewById(R.id.email_sign_in_esqueci).setOnClickListener(this);
        findViewById(R.id.email_sign_in_google).setOnClickListener(this);
        findViewById(R.id.sign_anonimo_button).setOnClickListener(this);

        // [START initialize_auth]
        mAuth = FirebaseAuth.getInstance();
        // [END initialize_auth]

        //BARRA SUPERIOR
        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        myToolbar.setTitleTextColor(R.color.primaryTextColor);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        // GOOGLE SIGN-IN
        // [START config_signin]
        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        // [END config_signin]
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
    }

    // [START on_start_check_user]
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
        // TODO
        //////     GOOGLE - BOTÃO DESATIVADO TEMPORARIAMENTE
        findViewById(R.id.email_sign_in_google).setVisibility(View.GONE);
        ////////////////////////////////////////////////////

    }

    // [END on_start_check_user]
    private void createAccount(final String email, String password) {
        Log.d(TAG, "CADASTRAR CONTA:" + email);
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
                            Log.d(TAG, "CADASTRO OK");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                            mFirebase = FirebaseDatabase.getInstance();
                            DatabaseReference tbUsuarios = mFirebase.getReference().child("usuarios").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
                            tbUsuarios.child("nmUsuario").setValue(email);
                            tbUsuarios.child("pontosUsuario").setValue(0);
                            Toast.makeText(LoginActivity.this, "Cadastro efetuado com sucesso.", Toast.LENGTH_LONG).show();
                            startActivity(new Intent(LoginActivity.this, Principal.class));
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "CADASTRO FALHOU:", task.getException());
                            String erro = task.getException().getMessage();
                            if (erro.equals("The email address is already in use by another account.")) {
                                Toast.makeText(LoginActivity.this, "Email já cadastradado.", Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(LoginActivity.this, erro, Toast.LENGTH_LONG).show();
                            }
                            updateUI(null);
                            startActivity(new Intent(LoginActivity.this, Principal.class));
                        }
                        // [START_EXCLUDE]
                        hideProgressDialog();
                        // [END_EXCLUDE]
                    }
                });
        // [END create_user_with_email]
    }

    protected void signIn(String email, String password) {
        Log.d(TAG, "LOGIN:" + email);
        if (email != "scruiz@scruiz.br") {
            if (!validateForm()) {
                return;
            }
        }
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
                            Toast.makeText(LoginActivity.this, "Login com sucesso.", Toast.LENGTH_LONG).show();
                            startActivity(new Intent(LoginActivity.this, Principal.class));
                            //
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "LOGIN FALHOU", task.getException());
                            // TODO
                            String erro = task.getException().getMessage();
                            if (erro.equals("There is no user record corresponding to this identifier. The user may have been deleted.")) {
                                Toast.makeText(LoginActivity.this, "Usuário ainda não cadastradado.", Toast.LENGTH_LONG).show();
                                hideKeyboard(findViewById(R.id.email));
                                hideKeyboard(findViewById(R.id.password));
                            } else {
                                Toast.makeText(LoginActivity.this, erro, Toast.LENGTH_LONG).show();
                            }
                            updateUI(null);
                        }
                        // [START_EXCLUDE]
                        if (!task.isSuccessful()) {
                            mUsuarioLogado.setText(R.string.error_login_generico);
                        }
                        hideProgressDialog();
                        // [END_EXCLUDE]
                    }
                });
        // [END sign_in_with_email]
    }

    private void signOut() {
        mAuth.signOut();
        updateUI(null);
        // Google sign out
        mGoogleSignInClient.signOut().addOnCompleteListener(this,
                new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        updateUI(null);
                    }
                });
        startActivity(new Intent(LoginActivity.this, Principal.class));
    }

    // E-MAIL ESQUECI A SENHA
    private void sendEmailVerification() {
        String email = mEmailEditView.getText().toString();
        if (TextUtils.isEmpty(email)) {
            mEmailEditView.setError("Campo obrigatório!");
            return;
        } else {
            mEmailEditView.setError(null);
        }
        //
        showProgressDialog();
        //
        mAuth = FirebaseAuth.getInstance();
        mAuth.sendPasswordResetEmail(mEmailEditView.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(LoginActivity.this, "Email de redefinição de senha enviado.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(LoginActivity.this, "Falha no envio do email. Tente novamente.", Toast.LENGTH_SHORT).show();
                }
            }
        });
        // [START_EXCLUDE]
        hideProgressDialog();
        // [END_EXCLUDE]
    }

    private boolean validateForm() {
        boolean valid = true;
        String email = mEmailEditView.getText().toString();
        if (TextUtils.isEmpty(email)) {
            mEmailEditView.setError("Campo obrigatório!");
            valid = false;
        } else {
            mEmailEditView.setError(null);
        }
        String password = mPasswordEditView.getText().toString();
        if (TextUtils.isEmpty(password)) {
            mPasswordEditView.setError("Campo obrigatório!");
            valid = false;
        } else {
            mPasswordEditView.setError(null);
        }
        return valid;
    }

    private void updateUI(FirebaseUser user) {
        hideProgressDialog();
        if (user != null) {
            if (user.getEmail().equals("scruiz@scruiz.br")) {
                mUsuarioLogado.setText("Logado como: anonimo");
            } else {
                mUsuarioLogado.setText(getString(R.string.emailpassword_status_fmt,
                        user.getEmail(), "LOGADO"));
            }
            // TODO ---  VERIFICAR BOTOES ---
            findViewById(R.id.email).setVisibility(View.GONE);
            findViewById(R.id.password).setVisibility(View.GONE);
            findViewById(R.id.email_sign_in_button).setVisibility(View.GONE);
            findViewById(R.id.email_sign_in_cadastrar).setVisibility(View.GONE);
            findViewById(R.id.email_sign_in_esqueci).setVisibility(View.GONE);
            findViewById(R.id.email_sign_in_google).setVisibility(View.GONE);
            findViewById(R.id.sign_anonimo_button).setVisibility(View.GONE);
            findViewById(R.id.email_sign_out_button).setVisibility(View.VISIBLE);
        } else {
            mUsuarioLogado.setText(R.string.signed_out);
            findViewById(R.id.email).setActivated(true);
            findViewById(R.id.password).setActivated(true);
            findViewById(R.id.email).setVisibility(View.VISIBLE);
            findViewById(R.id.password).setVisibility(View.VISIBLE);
            // TODO ---  VERIFICAR BOTOES ---
            findViewById(R.id.email_sign_in_button).setVisibility(View.VISIBLE);
            findViewById(R.id.email_sign_in_cadastrar).setVisibility(View.VISIBLE);
            findViewById(R.id.email_sign_in_esqueci).setVisibility(View.VISIBLE);
            // TODO ---  LOGIN GOOGLE DESATIVADO TEMPORARIAMENTE
            //findViewById(R.id.email_sign_in_google).setVisibility(View.VISIBLE);
            findViewById(R.id.email_sign_out_button).setVisibility(View.GONE);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
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
                // [START_EXCLUDE]
                updateUI(null);
                // [END_EXCLUDE]
            }
        }
    }

    // [END onactivityresult]
    // [START auth_with_google]
    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());
        // [START_EXCLUDE silent]
        showProgressDialog();
        // [END_EXCLUDE]
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                            mFirebase = FirebaseDatabase.getInstance();
                            DatabaseReference tbUsuarios = mFirebase.getReference().child("usuarios").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
                            tbUsuarios.child("nmUsuario").setValue(user.getEmail().toString());
                            tbUsuarios.child("pontosUsuario").setValue(0);
                            startActivity(new Intent(LoginActivity.this, Principal.class));
                        } else {
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            updateUI(null);
                        }
                        // [START_EXCLUDE]
                        hideProgressDialog();
                        // [END_EXCLUDE]
                    }
                });
    }

    // [END auth_with_google]
    // [START signin]
    private void signInGoogle() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    // [END signin]
    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.email_sign_in_button) {
            signIn(mEmailEditView.getText().toString(), mPasswordEditView.getText().toString());
        } else if (i == R.id.email_sign_in_cadastrar) {
            createAccount(mEmailEditView.getText().toString(), mPasswordEditView.getText().toString());
        } else if (i == R.id.email_sign_in_esqueci) {
            sendEmailVerification();
        } else if (i == R.id.email_sign_in_google) {
            signInGoogle();
        } else if (i == R.id.email_sign_out_button) {
            signOut();
        } else if (i == R.id.sign_anonimo_button) {
            alertar();
        }
    }

    public void alertar() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        // Mensagem
        builder.setTitle("Aviso:");
        builder.setMessage("Logando como 'anônimo' sua pontuação não será cumulativa.");
        // Add the buttons
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                signIn("scruiz@scruiz.br", "147036");
            }
        });
        /* builder.setNegativeButton("Not ok!", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User cancelled the dialog
            }
        }); */
        AlertDialog dialog = builder.create();
        dialog.show();
    }

///// [fim de tudo /////
}
