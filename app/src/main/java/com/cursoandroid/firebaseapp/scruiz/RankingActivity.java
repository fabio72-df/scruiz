package com.cursoandroid.firebaseapp.scruiz;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.ArrayList;
import java.util.EventListener;

public class RankingActivity extends BaseActivity {

    // FirebaseDatabase p/ "ouvidores"
    private DatabaseReference mDatabase;

    // TODO
    private String[] nomes = new String[10];
    private int[] pontos = new int[10];

    private ArrayList<String> nmUsuarios = new ArrayList<>();
    private ArrayList<Integer> ptosUsuarios = new ArrayList<>();

    private UsuariosRk mClasseUsuariosRk;

    public TextView txtRanking1, txtRanking2, txtRanking3, txtRanking4, txtRanking5, txtRanking6, txtRanking7, txtRanking8, txtRanking9, txtRanking10;
    public TextView txtRanking11, txtRanking22, txtRanking33, txtRanking44, txtRanking55, txtRanking66, txtRanking77, txtRanking88, txtRanking99, txtRanking1010;
    public TableRow trsep1, trsep2, trsep3, trsep4, trsep5, trsep6, trsep7, trsep8, trsep9, trsep10;

    // TODO VER SE PRECISA DE idxTab
    // Working
    private int cntRanking;
    private int idxTab = 0;
    private String gdaText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rk);

        //BARRA SUPERIOR
        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        myToolbar.setTitleTextColor(R.color.primaryTextColor);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        txtRanking1 = findViewById(R.id.textViewRk1);
        txtRanking11 = findViewById(R.id.textViewRk11);
        txtRanking2 = findViewById(R.id.textViewRk2);
        txtRanking22 = findViewById(R.id.textViewRk22);
        txtRanking3 = findViewById(R.id.textViewRk3);
        txtRanking33 = findViewById(R.id.textViewRk33);
        txtRanking4 = findViewById(R.id.textViewRk4);
        txtRanking44 = findViewById(R.id.textViewRk44);
        txtRanking5 = findViewById(R.id.textViewRk5);
        txtRanking55 = findViewById(R.id.textViewRk55);
        txtRanking6 = findViewById(R.id.textViewRk6);
        txtRanking66 = findViewById(R.id.textViewRk66);
        txtRanking7 = findViewById(R.id.textViewRk7);
        txtRanking77 = findViewById(R.id.textViewRk77);
        txtRanking8 = findViewById(R.id.textViewRk8);
        txtRanking88 = findViewById(R.id.textViewRk88);
        txtRanking9 = findViewById(R.id.textViewRk9);
        txtRanking99 = findViewById(R.id.textViewRk99);
        txtRanking10 = findViewById(R.id.textViewRk10);
        txtRanking1010 = findViewById(R.id.textViewRk1010);
        trsep1 = findViewById(R.id.trsep1);
        trsep2 = findViewById(R.id.trsep2);
        trsep3 = findViewById(R.id.trsep3);
        trsep4 = findViewById(R.id.trsep4);
        trsep5 = findViewById(R.id.trsep5);
        trsep6 = findViewById(R.id.trsep6);
        trsep7 = findViewById(R.id.trsep7);
        trsep8 = findViewById(R.id.trsep8);
        trsep9 = findViewById(R.id.trsep9);
        trsep10 = findViewById(R.id.trsep10);

        txtRanking1.setVisibility(View.INVISIBLE);
        txtRanking2.setVisibility(View.INVISIBLE);
        txtRanking3.setVisibility(View.INVISIBLE);
        txtRanking4.setVisibility(View.INVISIBLE);
        txtRanking5.setVisibility(View.INVISIBLE);
        txtRanking6.setVisibility(View.INVISIBLE);
        txtRanking7.setVisibility(View.INVISIBLE);
        txtRanking8.setVisibility(View.INVISIBLE);
        txtRanking9.setVisibility(View.INVISIBLE);
        txtRanking10.setVisibility(View.INVISIBLE);
        txtRanking11.setVisibility(View.INVISIBLE);
        txtRanking22.setVisibility(View.INVISIBLE);
        txtRanking33.setVisibility(View.INVISIBLE);
        txtRanking44.setVisibility(View.INVISIBLE);
        txtRanking55.setVisibility(View.INVISIBLE);
        txtRanking66.setVisibility(View.INVISIBLE);
        txtRanking77.setVisibility(View.INVISIBLE);
        txtRanking88.setVisibility(View.INVISIBLE);
        txtRanking99.setVisibility(View.INVISIBLE);
        txtRanking1010.setVisibility(View.INVISIBLE);
        trsep1.setVisibility(View.INVISIBLE);
        trsep2.setVisibility(View.INVISIBLE);
        trsep3.setVisibility(View.INVISIBLE);
        trsep4.setVisibility(View.INVISIBLE);
        trsep5.setVisibility(View.INVISIBLE);
        trsep6.setVisibility(View.INVISIBLE);
        trsep7.setVisibility(View.INVISIBLE);
        trsep8.setVisibility(View.INVISIBLE);
        trsep9.setVisibility(View.INVISIBLE);
        trsep10.setVisibility(View.INVISIBLE);


        showProgressDialog();

        mDatabase = FirebaseDatabase.getInstance().getReference();
        final Query qry = mDatabase.child("usuarios").orderByChild("pontosUsuario").limitToLast(11);

        qry.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                mClasseUsuariosRk = dataSnapshot.getValue(UsuariosRk.class);

                if (!mClasseUsuariosRk.getNmUsuario().equals("") && mClasseUsuariosRk.getPontosUsuario() < 1000000) {
                    if (mClasseUsuariosRk.getPontosUsuario() != null) {
                        nomes[idxTab] = mClasseUsuariosRk.getNmUsuario();
                    } else {
                        nomes[idxTab] = "";
                    }
                    if (mClasseUsuariosRk.getPontosUsuario() != null) {
                        pontos[idxTab] = mClasseUsuariosRk.getPontosUsuario();
                    } else {
                        pontos[idxTab] = 0;
                    }
                    idxTab = idxTab + 1;
                }
                ordenaArray(nomes, pontos);
                atualizaScreen();
                hideProgressDialog();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }

        });

    }

    private void ordenaArray(String[] nomes, int[] pontos) {

        for (int pass = 1; pass < nomes.length; pass++) {

            for (int element = 0; element < nomes.length - 1; element++) {

                if (pontos[element] < pontos[element + 1]) {
                    trocar(nomes, pontos, element, element + 1);
                }

            }
        }

    }

    private void trocar(String[] nomes, int[] pontos, int first, int second) {

        String gdaNome = nomes[first];
        int gdaPontos = pontos[first];

        nomes[first] = nomes[second];
        pontos[first] = pontos[second];

        nomes[second] = gdaNome;
        pontos[second] = gdaPontos;

    }

    private void atualizaScreen() {

        cntRanking = 0;

        if (nomes[0] != null) {
            gdaText = "1º - " + nomes[0];
            txtRanking1.setText(gdaText);
            gdaText = Integer.toString(pontos[0]);
            txtRanking11.setText(gdaText);
            txtRanking1.setVisibility(View.VISIBLE);
            txtRanking11.setVisibility(View.VISIBLE);
            trsep1.setVisibility(View.VISIBLE);
            cntRanking++;
        }
        if (nomes[1] != null) {
            gdaText = "2º - " + nomes[1];
            txtRanking2.setText(gdaText);
            gdaText = Integer.toString(pontos[1]);
            txtRanking22.setText(gdaText);
            txtRanking2.setVisibility(View.VISIBLE);
            txtRanking22.setVisibility(View.VISIBLE);
            trsep2.setVisibility(View.VISIBLE);
            cntRanking++;
        }
        if (nomes[2] != null) {
            gdaText = "3º - " + nomes[2];
            txtRanking3.setText(gdaText);
            gdaText = Integer.toString(pontos[2]);
            txtRanking33.setText(gdaText);
            txtRanking3.setVisibility(View.VISIBLE);
            txtRanking33.setVisibility(View.VISIBLE);
            trsep3.setVisibility(View.VISIBLE);
            cntRanking++;
        }
        if (nomes[3] != null) {
            gdaText = "4º - " + nomes[3];
            txtRanking4.setText(gdaText);
            gdaText = Integer.toString(pontos[3]);
            txtRanking44.setText(gdaText);
            txtRanking4.setVisibility(View.VISIBLE);
            txtRanking44.setVisibility(View.VISIBLE);
            trsep4.setVisibility(View.VISIBLE);
            cntRanking++;
        }
        if (nomes[4] != null) {
            gdaText = "5º - " + nomes[4];
            txtRanking5.setText(gdaText);
            gdaText = Integer.toString(pontos[4]);
            txtRanking55.setText(gdaText);
            txtRanking5.setVisibility(View.VISIBLE);
            txtRanking55.setVisibility(View.VISIBLE);
            trsep5.setVisibility(View.VISIBLE);
            cntRanking++;
        }
        if (nomes[5] != null) {
            gdaText = "6º - " + nomes[5];
            txtRanking6.setText(gdaText);
            gdaText = Integer.toString(pontos[5]);
            txtRanking66.setText(gdaText);
            txtRanking6.setVisibility(View.VISIBLE);
            txtRanking66.setVisibility(View.VISIBLE);
            trsep6.setVisibility(View.VISIBLE);
            cntRanking++;
        }
        if (nomes[6] != null) {
            gdaText = "7º - " + nomes[6];
            txtRanking7.setText(gdaText);
            gdaText = Integer.toString(pontos[6]);
            txtRanking77.setText(gdaText);
            txtRanking7.setVisibility(View.VISIBLE);
            txtRanking77.setVisibility(View.VISIBLE);
            trsep7.setVisibility(View.VISIBLE);
            cntRanking++;
        }
        if (nomes[7] != null) {
            gdaText = "8º - " + nomes[7];
            txtRanking8.setText(gdaText);
            gdaText = Integer.toString(pontos[7]);
            txtRanking88.setText(gdaText);
            txtRanking8.setVisibility(View.VISIBLE);
            txtRanking88.setVisibility(View.VISIBLE);
            trsep8.setVisibility(View.VISIBLE);
            cntRanking++;
        }
        if (nomes[8] != null) {
            gdaText = "9º - " + nomes[8];
            txtRanking9.setText(gdaText);
            gdaText = Integer.toString(pontos[8]);
            txtRanking99.setText(gdaText);
            txtRanking9.setVisibility(View.VISIBLE);
            txtRanking99.setVisibility(View.VISIBLE);
            trsep9.setVisibility(View.VISIBLE);
            cntRanking++;
        }
        if (nomes[9] != null) {
            gdaText = "10º - " + nomes[9];
            txtRanking10.setText(gdaText);
            gdaText = Integer.toString(pontos[9]);
            txtRanking1010.setText(gdaText);
            txtRanking10.setVisibility(View.VISIBLE);
            txtRanking1010.setVisibility(View.VISIBLE);
            trsep10.setVisibility(View.VISIBLE);
            cntRanking++;
        }

        gdaText = "TOP " + cntRanking;
        TextView tituloPpl = findViewById(R.id.tituloPrincipal);
        tituloPpl.setText(gdaText);
    }

////////////////
} // FIM DE TUDO
