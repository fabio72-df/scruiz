package com.cursoandroid.firebaseapp.scruiz;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.cursoandroid.firebaseapp.scruiz.subclasses.LoginPrimeiraActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class Principal extends AppCompatActivity {

    private static final String TAG = "Principal";
    private static final String USUARIO_SCRUIZ = "scruiz@scruiz.br";
    private String msgLogado;

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseUser mUser = mAuth.getCurrentUser();

    private TextView tvLogado;
    private String gdaText;

    // MENU PRINCIPAL - BARRA INFERIOR
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                // Se LOGADO: InicarTeste
                // senão: LoginPrimeiraActivity
                case R.id.iniciar_teste:
                    if (mUser != null){
                        Intent i = new Intent(Principal.this, IniciarTeste.class);
                        i.putExtra("userEmail", mUser.getEmail());
                        i.putExtra("userId", mUser.getUid());
                        startActivity(i);
                    } else {
                        startActivity(new Intent(Principal.this,
                                LoginPrimeiraActivity.class));
                    }
                    return true;
                // LOGIN
                case R.id.login:
                    // TODO
                    startActivity(new Intent(Principal.this,
                            LoginPrimeiraActivity.class));
                    return true;
                // TODO - Ranking -- -- -- -- --
                // SOBRE
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

        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        //BARRA SUPERIOR
        Toolbar myToolbar = findViewById(R.id.my_toolbar_ppl);
        setSupportActionBar(myToolbar);
        myToolbar.setTitleTextColor(getResources().getColor(R.color.primaryTextColor));

    }

    @Override
    protected void onStart() {
        super.onStart();

        tvLogado = findViewById(R.id.tvLogado);
        if (mUser != null){
            try{
                gdaText = mUser.getEmail();
                if (gdaText.equals(USUARIO_SCRUIZ)){
                    gdaText = " [anônimo]";
                }else{
                    gdaText = " [" + gdaText + "]";
                }
            }catch (NullPointerException exc){
                gdaText = "";
            }
            gdaText = "LOGADO" + gdaText;
            tvLogado.setText(gdaText);
        }else{
            tvLogado.setText("off-line");
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
                alertaLogout();
                return true;
            case R.id.ppl_menu2:
                startActivity(new Intent(Principal.this, SobreActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    public void alertaLogout() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        // Mensagem
        builder.setTitle("Atenção");
        builder.setMessage("Efetuar o LOGOUT?:");
        // Add the buttons
        builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                mAuth.signOut();
                mUser = mAuth.getCurrentUser();
                Toast.makeText( Principal.this,
                        "Logout efetuado", Toast.LENGTH_SHORT).show();
                tvLogado.setText("off-line");
            }
        });
        builder.setNegativeButton("Não", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // NÃO FAZ NADA
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
