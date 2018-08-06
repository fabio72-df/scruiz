package com.cursoandroid.firebaseapp.scruiz;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.cursoandroid.firebaseapp.scruiz.SubClasses.LoginPrimeiraActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class Principal extends AppCompatActivity {

    private static final String TAG = "Principal";

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseUser mUser;

    // MENU PRINCIPAL - BARRA INFERIOR
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            mUser = mAuth.getCurrentUser();
            switch (item.getItemId()) {
                case R.id.iniciar_teste:
                    if (verificaLog()) {
                        Intent i = new Intent(Principal.this, IniciarTeste.class);
                        i.putExtra("userEmail", mUser.getEmail());
                        i.putExtra("userId", mUser.getUid());
                        startActivity(i);
                    } else {
                        startActivity(new Intent(Principal
                                .this, LoginPrimeiraActivity.class));
                    }
                    return true;
                case R.id.login:
                    // TODO
                    startActivity(new Intent(Principal
                            .this, LoginPrimeiraActivity.class));
                    return true;
                case R.id.sobre:
                    // TODO
                    //startActivity(new Intent(Principal.this, SobreActivity.class));
                    startActivity(new Intent(Principal.this, RankingActivity.class));
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        //BARRA SUPERIOR
        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        myToolbar.setTitleTextColor(getResources().getColor(R.color.primaryTextColor));

    }

    private boolean verificaLog() {
        if (mUser == null) {
            return false;
        } else {
            return true;
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_login, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.ppl_menu1:
                startActivity(new Intent(Principal.this, SobreActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
