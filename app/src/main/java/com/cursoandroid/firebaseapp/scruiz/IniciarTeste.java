package com.cursoandroid.firebaseapp.scruiz;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.cursoandroid.firebaseapp.scruiz.ClassesDados.Questionario;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static android.view.View.GONE;

public class IniciarTeste extends BaseActivity {
    // TAG PARA TESTES
    private final String TAG = "#TESTE ", USUARIO_MASTER = "scruiz@scruiz.br";

    // -----[FIREBASE]-----
    private DatabaseReference mUsuarioReference;
    private DatabaseReference mQuestoesReference;

    // -----[UI]-----
    public TextView textViewOla, textViewPontos, textViewNrQuestao, textViewQuestao;
    public CheckBox checkBox1, checkBox2, checkBox3, checkBox4, checkBox5;
    private Button btnResposta, btnInc;
    private Handler handler = new Handler();

    // -----[Working]-----
    private NumeroAleaorio numeroAleaorio = new NumeroAleaorio();
    public int gdaPtosAnonimo, qstSorteada, cntTempo, pontos;
    public String gdaTxt;
    public ArrayList<Questoes> arrayQuestoes = new ArrayList<>();
    public Usuarios mClasseUsuarios;

    // -----[ON-CREATE]-----
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_iniciar_teste_tmp);

        // [Popular array Questionario]
        carregarQuestoes();

        // [BARRA SUPERIOR]
        Toolbar myToolbar = findViewById(R.id.toolbar_iniciarTeste);
        setSupportActionBar(myToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        // ***fim [BARRA SUPERIOR]

        // [UI] Inicializa Widgets
        // --TextView
        textViewOla = findViewById(R.id.textViewOla);
        textViewPontos = findViewById(R.id.textViewPontos);
        textViewNrQuestao = findViewById(R.id.textViewNrQuestao);
        textViewQuestao = findViewById(R.id.textViewQuestao);
        // --Check-boxies
        checkBox1 = findViewById(R.id.checkBox1);
        checkBox2 = findViewById(R.id.checkBox2);
        checkBox3 = findViewById(R.id.checkBox3);
        checkBox4 = findViewById(R.id.checkBox4);
        checkBox5 = findViewById(R.id.checkBox5);
        // --Botão RESPONDER
        btnResposta = findViewById(R.id.respButton);
        btnResposta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (btnResposta.getImeActionLabel().toString().equals("responder")){
                    if (corrigirQuestao(qstSorteada)) {
                        checkBox1.setClickable(false);
                        checkBox2.setClickable(false);
                        checkBox3.setClickable(false);
                        checkBox4.setClickable(false);
                        checkBox5.setClickable(false);
                        btnResposta.setBackgroundResource(R.drawable.ic_proxima);
                        btnResposta.setImeActionLabel("proxima", 2);
                    }
                } else {
                    checkBox1.setClickable(true);
                    checkBox2.setClickable(true);
                    checkBox3.setClickable(true);
                    checkBox4.setClickable(true);
                    checkBox5.setClickable(true);
                    btnResposta.setBackgroundResource(R.drawable.ic_responder);
                    btnResposta.setImeActionLabel("responder", 2);
                    exibirQuestao();
                }
            }
        });
        // --Botão INICIAR TESTE
        btnInc = findViewById(R.id.btnIniciar);
        btnInc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exibirQuestao();
            }
        });


        // ***fim [UI] Inicializa Widgets

    }

    @Override
    protected void onStart() {
        super.onStart();
        // [UI] Atribui conteúdos
        // ----------------------
        // Usuário (boas vindas)
        Intent i = getIntent();
        String userEmail = i.getStringExtra("userEmail");
        String gdaTxt = "Olá ";
        if (!userEmail.equals(USUARIO_MASTER)) {
            gdaTxt = gdaTxt + nomeCurto(userEmail);
        }
        textViewOla.setText(gdaTxt);
        // Pontuação
        String userId = i.getStringExtra("userId");
        atualizaPontos(userId);
        // Sumir QUESTÃO
        sumirQuestao();
        // --ProgreBar - Questão: NR & TEXTO
        final ProgressBar progressBar = findViewById(R.id.indeterminatePb);
        cntTempo = 0;
        progressBar.setVisibility(View.VISIBLE);
        // Start the lengthy operation in a background thread
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (cntTempo < 100) {
                    // Update the progress status
                    cntTempo += 1;
                    // Try to sleep the thread for 20 milliseconds
                    try {
                        Thread.sleep(20);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    // Update the progress bar
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            progressBar.setProgress(cntTempo);
                            if (cntTempo == 100) {
                                // Hide the progress bar from layout after finishing task
                                progressBar.setVisibility(GONE);
                            }
                        }
                    });
                }
            }
        }).start();// Start the operation
        // ***fim [UI] Attribui conteúdos

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    private String nomeCurto(String nome) {
        boolean fim = true;
        String nmCurto = "", letra;
        for (int i = 0; fim; i++) {
            if (i < nome.length()) {
                letra = nome.substring(i, i + 1);
                if (!letra.equals("@")) {
                    nmCurto = nmCurto + letra;
                } else {
                    fim = false;
                }
            } else {
                fim = false;
            }
        }
        return nmCurto;
    }

    // PONTUAÇÃO
    private void atualizaPontos(String userId) {
        mUsuarioReference = FirebaseDatabase.getInstance().getReference()
                .child("usuarios").child(userId);
        mUsuarioReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mClasseUsuarios = dataSnapshot.getValue(Usuarios.class);
                try {
                    gdaTxt = "Pontuação atual: ";
                    if (mClasseUsuarios.getNmUsuario().equals("anonimo")) {
                        gdaTxt = gdaTxt + gdaPtosAnonimo;
                        textViewPontos.setText(gdaTxt);
                    } else {
                        gdaTxt = gdaTxt + mClasseUsuarios.getpontosUsuario();
                        textViewPontos.setText(gdaTxt);
                    }
                } catch (NullPointerException nexc) {
                    Log.i(TAG, " -- NullPoint" + nexc);
                    gdaTxt = "Pontuação atual: " + "[Ops! Indisponível!]";
                    textViewPontos.setText(R.string.error_load_pontuacao);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                textViewPontos.setText(R.string.error_load_pontuacao);
            }
        });
    }

    // QUESTÕES
    public void exibirQuestao() {
        btnInc.setVisibility(GONE);
        textViewNrQuestao.setVisibility(View.VISIBLE);
        textViewQuestao.setVisibility(View.VISIBLE);
        checkBox1.setVisibility(View.VISIBLE);
        checkBox2.setVisibility(View.VISIBLE);
        checkBox3.setVisibility(View.VISIBLE);
        checkBox4.setVisibility(View.VISIBLE);
        checkBox5.setVisibility(View.VISIBLE);
        checkBox1.setChecked(false);
        checkBox2.setChecked(false);
        checkBox3.setChecked(false);
        checkBox4.setChecked(false);
        checkBox5.setChecked(false);
        checkBox1.setTextColor(Color.BLACK);
        checkBox2.setTextColor(Color.BLACK);
        checkBox3.setTextColor(Color.BLACK);
        checkBox4.setTextColor(Color.BLACK);
        checkBox5.setTextColor(Color.BLACK);
        btnResposta.setVisibility(View.VISIBLE);
        btnResposta.setImeActionLabel("responder", 1);
        if (Questionario.getArrayQuestoes() != null) {
            numeroAleaorio.setNrMax(Questionario.getArrayQuestoes().size());
            qstSorteada = numeroAleaorio.getNrSorteado();
            gdaTxt = getString(R.string.title_questao_nr) + " " +
                    Integer.toString(qstSorteada);
            SpannableString content = new SpannableString(gdaTxt);
            content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
            textViewNrQuestao.setText(content);
            // Texto da Questão (sorteada)
            gdaTxt = Questionario.getArrayQuestoes().get(qstSorteada).getTxtQuestao();
            textViewQuestao.setText(gdaTxt);
            gdaTxt = Questionario.getArrayQuestoes().get(qstSorteada).getTxtOpc1();
            checkBox1.setText(Questionario.getArrayQuestoes().get(qstSorteada).getTxtOpc1());
            checkBox2.setText(Questionario.getArrayQuestoes().get(qstSorteada).getTxtOpc2());
            gdaTxt = Questionario.getArrayQuestoes().get(qstSorteada).getTxtOpc3();
            if (!gdaTxt.isEmpty()) {
                checkBox3.setVisibility(View.VISIBLE);
                checkBox3.setText(gdaTxt);
            }else{
                checkBox3.setVisibility(GONE);
            }
            gdaTxt = Questionario.getArrayQuestoes().get(qstSorteada).getTxtOpc4();
            if (!gdaTxt.isEmpty()) {
                checkBox4.setVisibility(View.VISIBLE);
                checkBox4.setText(gdaTxt);
            }else{
                checkBox4.setVisibility(GONE);
            }
            gdaTxt = Questionario.getArrayQuestoes().get(qstSorteada).getTxtOpc5();
            if (!gdaTxt.isEmpty()) {
                checkBox5.setVisibility(View.VISIBLE);
                checkBox5.setText(gdaTxt);
            }else{
                checkBox5.setVisibility(GONE);
            }
        }
    }

    private void sumirQuestao() {
        textViewNrQuestao.setVisibility(View.INVISIBLE);
        textViewQuestao.setVisibility(View.INVISIBLE);
        checkBox1.setVisibility(View.INVISIBLE);
        checkBox2.setVisibility(View.INVISIBLE);
        checkBox3.setVisibility(View.INVISIBLE);
        checkBox4.setVisibility(View.INVISIBLE);
        checkBox5.setVisibility(View.INVISIBLE);
        btnResposta.setVisibility(View.INVISIBLE);
    }

    private void carregarQuestoes() {
        mQuestoesReference = FirebaseDatabase.getInstance().getReference().child("questoes");
        Query consultaQuestoes = mQuestoesReference;
        consultaQuestoes.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    Questoes questoes = child.getValue(Questoes.class);
                    arrayQuestoes.add(questoes);
                    Questionario.setArrayQuestoes(arrayQuestoes);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.i(TAG, "ERRO LEITURA QUESTOES--> " + databaseError.getMessage());
                Toast.makeText(IniciarTeste.this,
                        "Sistema Indisponível", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean corrigirQuestao(int qn) {
        Boolean acertou = true;
        int qtCertas = 0;
        int qtRespondidas = 0;
        if (Questionario.getArrayQuestoes().get(qn).isOpc1Certa()) {
            qtCertas += 1;
            if (!checkBox1.isChecked()) {
                acertou = false;
            }
        }
        if (Questionario.getArrayQuestoes().get(qn).isOpc2Certa()) {
            qtCertas += 1;
            if (!checkBox2.isChecked()) {
                acertou = false;
            }
        }
        if (Questionario.getArrayQuestoes().get(qn).isOpc3Certa()) {
            qtCertas += 1;
            if (!checkBox3.isChecked()) {
                acertou = false;
            }
        }
        if (Questionario.getArrayQuestoes().get(qn).isOpc4Certa()) {
            qtCertas += 1;
            if (!checkBox4.isChecked()) {
                acertou = false;
            }
        }
        if (Questionario.getArrayQuestoes().get(qn).isOpc5Certa()) {
            qtCertas += 1;
            if (!checkBox5.isChecked()) {
                acertou = false;
            }
        }
        // RESPOSTAS
        if (checkBox1.isChecked()) {
            qtRespondidas += 1;
        }
        if (checkBox2.isChecked()) {
            qtRespondidas += 1;
        }
        if (checkBox3.isChecked()) {
            qtRespondidas += 1;
        }
        if (checkBox4.isChecked()) {
            qtRespondidas += 1;
        }
        if (checkBox5.isChecked()) {
            qtRespondidas += 1;
        }
        gdaTxt = "";
        String gdaTxtAux;
        if (qtRespondidas == 0) {
            if (!Questionario.getArrayQuestoes().get(qn).isAceitaEmBranco()) {
                if (Questionario.getArrayQuestoes().get(qn).isInformarQtQuestoes()) {
                    gdaTxtAux = " opção.";
                    if (qtCertas > 1) {
                        gdaTxtAux = " opções.";
                    }
                    gdaTxt = "Assinale " + Integer.toString(qtCertas) + gdaTxtAux;
                } else {
                    gdaTxt = "Assinale sua resposta!";
                }
            }
            Toast.makeText(IniciarTeste.this, gdaTxt, Toast.LENGTH_SHORT).show();
            return false;
        }
        if ((qtRespondidas != qtCertas) && gdaTxt.equals("")) {
            if (Questionario.getArrayQuestoes().get(qn).isInformarQtQuestoes()) {
                gdaTxtAux = " opção.";
                if (qtCertas > 1) {
                    gdaTxtAux = " opções.";
                }
                gdaTxt = "Assinale " + Integer.toString(qtCertas) + gdaTxtAux;
            } else {
                gdaTxt = "Assinale sua resposta!";
            }
            Toast.makeText(IniciarTeste.this, gdaTxt, Toast.LENGTH_SHORT).show();
            return false;
        }
        if (gdaTxt.equals("")) {
            if (Questionario.getArrayQuestoes().get(qn).isOpc1Certa()) {
                checkBox1.setTextColor(Color.BLUE);
            }
            if (Questionario.getArrayQuestoes().get(qn).isOpc2Certa()) {
                checkBox2.setTextColor(Color.BLUE);
            }
            if (Questionario.getArrayQuestoes().get(qn).isOpc3Certa()) {
                checkBox3.setTextColor(Color.BLUE);
            }
            if (Questionario.getArrayQuestoes().get(qn).isOpc4Certa()) {
                checkBox4.setTextColor(Color.BLUE);
            }
            if (Questionario.getArrayQuestoes().get(qn).isOpc4Certa()) {
                checkBox5.setTextColor(Color.BLUE);
            }
            pontos = 0;
            if (acertou) {
                Toast.makeText(IniciarTeste.this, "Parabéns! Está CORRETO.", Toast.LENGTH_SHORT).show();
                pontos = 1;
            } else {
                Toast.makeText(IniciarTeste.this, "Ops! Continue tentando...", Toast.LENGTH_SHORT).show();
                if (mClasseUsuarios.getpontosUsuario() > 0) {
                    pontos = -1;
                }
            }
        }
        if (mClasseUsuarios.getNmUsuario().equals("anonimo")) {
            if (pontos < 0) {
                if (gdaPtosAnonimo > 0) {
                    gdaPtosAnonimo = gdaPtosAnonimo + pontos;
                }
            } else {
                gdaPtosAnonimo = gdaPtosAnonimo + pontos;
            }
            gdaTxt = "Pontuação atual: " + gdaPtosAnonimo;
            textViewPontos.setText(gdaTxt);
            gdaTxt = "";
            pontos = 1;
        }
        // ATUALIZA PONTUAÇÃO
        mUsuarioReference.child("pontosUsuario")
                .setValue(mClasseUsuarios.getpontosUsuario() + pontos);
        return true;
    }

// [ *** FIM *** ]
}
