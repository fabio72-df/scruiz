package com.cursoandroid.firebaseapp.scruiz.SubClasses;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.cursoandroid.firebaseapp.scruiz.R;

public class LoginPrimeiraActivity extends AppCompatActivity implements
        View.OnClickListener{

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_primeira);
        //BARRA SUPERIOR
        Toolbar myToolbar = findViewById(R.id.toolbarLoginPrimeira);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        findViewById(R.id.email_sign_in_google).setOnClickListener(this);
        findViewById(R.id.email_sign_in_email).setOnClickListener(this);
        findViewById(R.id.email_sign_in_anonimo).setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        // GOOGLE
        if (i == R.id.email_sign_in_google) {
            //Toast.makeText(LoginPrimeiraActivity.this,"Em implementação", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(LoginPrimeiraActivity.this, Login1.class);
            startActivity(intent);
        // E-MAIL
        }else if(i == R.id.email_sign_in_email){
            Intent intent = new Intent(LoginPrimeiraActivity.this, Login2.class);
            intent.putExtra("tipo", 0);
            startActivity(intent);
        // ANÔNIMO
        }else if(i == R.id.email_sign_in_anonimo){
            alertar();
        }
    }

    public void alertar() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        // Mensagem
        builder.setTitle("Aviso:");
        builder.setMessage("Logando como 'anônimo' você não vai acumular os pontos para o ranking.");
        // Add the buttons
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // LOGAR COMO ANONIMO --- tipo = 1
                Intent intent = new Intent(LoginPrimeiraActivity.this, Login2.class);
                intent.putExtra("tipo", 1);
                startActivity(intent);
            }
        });
        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // NÃO FAZ NADA
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();

    }

}
