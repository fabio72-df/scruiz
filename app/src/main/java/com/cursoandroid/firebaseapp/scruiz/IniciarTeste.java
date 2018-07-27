package com.cursoandroid.firebaseapp.scruiz;

import android.app.AlertDialog;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class IniciarTeste extends BaseActivity implements
        View.OnClickListener {

    // TODO -- TESTE -- Carregar todas as questões
    private ArrayList<Questoes> setQuestoes = new ArrayList<Questoes>();
    private ListView mListView;
    private FirebaseListAdapter mAdapter;
    // TODO - Nova forma de referenciar o Banco de Dados (melhor)
    private DatabaseReference mUsuarioReference;
    private DatabaseReference mQuestoesReference;

    // DEFAULT DE QUANTIDADE DE QUESTÕES
    private final int QT_QUESTOES_DEFAULT = 21;
    // ALERT DIALOG
    private AlertDialog alerta;
    private boolean SEMAFORO_DIALOG_ABERTO = true;
    // CONTROLES DOS CHECKBOX DAS QUESTÕES
    private static final int[][] CHECK_BOX_STATES = new int[][]{
            new int[]{-android.R.attr.state_enabled}, // desabilitado
            new int[]{android.R.attr.state_checked}, // marcado
            new int[]{-android.R.attr.state_checked}, // desmarcado
            new int[]{} // default
    };
    private static final int[] CHECK_BOX_COLORS = new int[]{
            Color.GRAY,
            Color.CYAN,
            Color.BLUE,
            Color.BLUE
    };
    // FirbaseAuth
    private FirebaseAuth minhaAuth = FirebaseAuth.getInstance();
    private String userId;
    // FirebaseDatabase p/"ouvidores"
    private FirebaseDatabase mFirebase;
    // FirebaseDatabase p/UPDATE (referencia Genérica)
    private DatabaseReference mDatabaseReference;

    // Minhas Classes de dados
    private Parametros mClasseParametros = new Parametros();
    private Usuarios mClasseUsuarios = new Usuarios();
    private Questoes mClasseQuestoes = new Questoes();

    // UI
    private TextView textViewOla, textViewPontos, textViewNrQuestao, textViewQuestao;
    private TextView textViewRegras1, textViewRegras2, textViewRegras3;
    private CheckBox checkBox1, checkBox2, checkBox3, checkBox4, checkBox5;
    private Button respButton;
    // Working
    private String gdaText;
    private int qtQuestoes, pontos;
    private int gdaPtosAnonimo;
    private String qstSorteada = "000";
    private NumeroAleaorio nrAl = new NumeroAleaorio();


    //////////     ON CREATE  ///////////
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_iniciar_teste_tmp);

        // ToDo NOVO
        // usuário
        userId = minhaAuth.getUid();
        mUsuarioReference = FirebaseDatabase.getInstance().getReference()
                .child("usuarios").child(userId);
        // questões
        mQuestoesReference = FirebaseDatabase.getInstance().getReference()
                .child("questoes");
        showProgressDialog();
        // TODO -- teste -- Carregar ARRAYLIST
        carregarArrayList();
        //
        // todo novo -- FIM --

        // [UI] Inicializa Widgets
        textViewOla = findViewById(R.id.textViewOla);
        textViewPontos = findViewById(R.id.textViewPontos);
        textViewNrQuestao = findViewById(R.id.textViewNrQuestao);
        textViewQuestao = findViewById(R.id.textViewQuestao);
        checkBox1 = findViewById(R.id.checkBox1);
        checkBox2 = findViewById(R.id.checkBox2);
        checkBox3 = findViewById(R.id.checkBox3);
        checkBox4 = findViewById(R.id.checkBox4);
        checkBox5 = findViewById(R.id.checkBox5);
        //BARRA SUPERIOR
        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        myToolbar.setTitleTextColor(R.color.primaryTextColor);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        // BOTAO RESPONDER / PROXIMA
        respButton = findViewById(R.id.respButton);
        respButton.setOnClickListener(this);
        respButton.setText("responder");
        respButton.setVisibility(View.INVISIBLE);
        /// zera pontos do anonimo
        gdaPtosAnonimo = 0;

    }

    // [  P R I N C I P A L  ]
    protected void onStart() {
        super.onStart();

        // LER PARAMETRO QT-QUESTOES
        // 001 = qtQuestões
        lerParametros("001");
        // FIM LER:: PARAMETRO QT-QUESTOES

        resetOpcoes();
        textViewOla.setVisibility(View.INVISIBLE);
        textViewPontos.setVisibility(View.INVISIBLE);
        textViewQuestao.setVisibility(View.INVISIBLE);
        textViewNrQuestao.setVisibility(View.INVISIBLE);

        // Atualiza: NOME DO USUÁRIO e PONTUAÇÃO na tela
        mClasseUsuarios.setNmUsuario("");
        updateViewOla();
        //
        respButton.setVisibility(View.VISIBLE);
        updateViewQuestao();

    }

    //// 'alert dialog' REGRAS DO QUIZ
    public void alertar() {
        //LayoutInflater é utilizado para inflar nosso layout em uma view.
        //-pegamos nossa instancia da classe
        LayoutInflater li = getLayoutInflater();
        //inflamos o layout alerta.xml na view
        View view = li.inflate(R.layout.alerta, null);

        textViewRegras1 = view.findViewById(R.id.textViewrRegras1);
        textViewRegras2 = view.findViewById(R.id.textViewrRegras2);
        textViewRegras3 = view.findViewById(R.id.textViewrRegras3);
        if (qtQuestoes == 0) {
            qtQuestoes = QT_QUESTOES_DEFAULT;
        }
        gdaText = ". Base: < " + Integer.toString(qtQuestoes) + " > questões;";
        textViewRegras1.setText(gdaText);
        gdaText = "";
        textViewRegras2.setText(". Ordem de apresentação: ALEATÓRIA;");
        textViewRegras3.setText(". Pontuação:\n  ACERTOS -> (+) 1 ponto\n  ERROS   -> (-) 1 ponto");

        //definimos para o botão do layout um clickListener
        view.findViewById(R.id.btOk).setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                //desfaz o alerta.
                alerta.dismiss();
            }
        });
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("REGRAS:");
        builder.setView(view);
        alerta = builder.create();
        alerta.show();
    }

    private void updateViewOla() {
        FirebaseUser currentUser = minhaAuth.getCurrentUser();
        if (currentUser != null) {
            mFirebase = FirebaseDatabase.getInstance();
            DatabaseReference tbUsuarios = mFirebase.getReference().child("usuarios").child(currentUser.getUid());
            tbUsuarios.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    mClasseUsuarios = dataSnapshot.getValue(Usuarios.class);
                    try {
                        if (mClasseUsuarios.getNmUsuario().equals("anonimo")) {
                            gdaText = "Olá. [logado como 'anônimo']";
                            textViewOla.setText(gdaText);
                            gdaText = "Pontuação atual: " + gdaPtosAnonimo + " ponto(s).";
                            textViewPontos.setText(gdaText);
                        } else {
                            gdaText = "Olá " + mClasseUsuarios.getNmUsuario() + ".";
                            textViewOla.setText(gdaText);
                            gdaText = "Pontuação atual: " + mClasseUsuarios.getpontosUsuario() + " ponto(s).";
                            textViewPontos.setText(gdaText);
                        }
                    } catch (NullPointerException nexc) {
                        gdaText = "Olá " + "[ERRO]" + ".";
                        textViewOla.setText(gdaText);
                        gdaText = "Pontuação atual: " + "[ERRO]" + " ponto(s).";
                        textViewPontos.setText(gdaText);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });
        } else {
            textViewOla.setText("Olá. [usuário off-line]");
        }
        textViewOla.setVisibility(View.VISIBLE);
        textViewPontos.setVisibility(View.VISIBLE);
    }

    private void updateViewQuestao() {

        // Definir qual será a questão a ser apresentada
        if (qtQuestoes == 0) {
            qtQuestoes = QT_QUESTOES_DEFAULT;
        }

        nrAl.setNrMax(qtQuestoes);
        int nrQuestao = nrAl.getNrSorteado();

        int nrAlNovo = nrQuestao - 1;

        if (nrQuestao < 10) {
            qstSorteada = "00" + Integer.toString(nrQuestao);
        } else {
            qstSorteada = "0" + Integer.toString(nrQuestao);
        }

        /*mFirebase = FirebaseDatabase.getInstance();
        DatabaseReference tbQuestoes = mFirebase.getReference().child("questoes").child(qstSorteada);
        tbQuestoes.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) { */

                // mClasseQuestoes = dataSnapshot.getValue(Questoes.class);

                mClasseQuestoes = setQuestoes.get(nrAlNovo);

                gdaText = getString(R.string.title_questao_nr) + qstSorteada;
                SpannableString content = new SpannableString(gdaText);
                content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
                textViewNrQuestao.setText(content);

                textViewQuestao.setText(mClasseQuestoes.getTxtQuestao());

                checkBox1.setText(mClasseQuestoes.getTxtOpc1());
                checkBox2.setText(mClasseQuestoes.getTxtOpc2());

                textViewNrQuestao.setVisibility(View.VISIBLE);
                textViewQuestao.setVisibility(View.VISIBLE);
                checkBox1.setVisibility(View.VISIBLE);
                checkBox2.setVisibility(View.VISIBLE);
                checkBox3.setVisibility(View.VISIBLE);
                checkBox4.setVisibility(View.VISIBLE);
                checkBox5.setVisibility(View.VISIBLE);

                if (mClasseQuestoes.getTxtOpc3().length() > 0) {
                    checkBox3.setText(mClasseQuestoes.getTxtOpc3());
                } else {
                    checkBox3.setVisibility(View.GONE);
                }
                if (mClasseQuestoes.getTxtOpc4().length() > 0) {
                    checkBox4.setText(mClasseQuestoes.getTxtOpc4());
                } else {
                    checkBox4.setVisibility(View.GONE);
                }
                if (mClasseQuestoes.getTxtOpc5().length() > 0) {
                    checkBox5.setText(mClasseQuestoes.getTxtOpc5());
                } else {
                    checkBox5.setVisibility(View.GONE);
                }
                hideProgressDialog();

           /* }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });*/
    }

    private boolean corrigirQuestao() {
        Boolean acertou = true;
        int qtCertas = 0;
        int qtRespondidas = 0;
        if (mClasseQuestoes.isOpc1Certa()) {
            qtCertas += 1;
            if (!checkBox1.isChecked()) {
                acertou = false;
            }
        }
        if (mClasseQuestoes.isOpc2Certa()) {
            qtCertas += 1;
            if (!checkBox2.isChecked()) {
                acertou = false;
            }
        }
        if (mClasseQuestoes.isOpc3Certa()) {
            qtCertas += 1;
            if (!checkBox3.isChecked()) {
                acertou = false;
            }
        }
        if (mClasseQuestoes.isOpc4Certa()) {
            qtCertas += 1;
            if (!checkBox4.isChecked()) {
                acertou = false;
            }
        }
        if (mClasseQuestoes.isOpc5Certa()) {
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

        gdaText = "";
        String gdaTextAux;
        if (qtRespondidas == 0) {
            if (!mClasseQuestoes.isAceitaEmBranco()) {
                if (mClasseQuestoes.isInformarQtQuestoes()) {
                    gdaTextAux = " opção.";
                    if (qtCertas > 1) {
                        gdaTextAux = " opções.";
                    }
                    gdaText = "Assinale " + Integer.toString(qtCertas) + gdaTextAux;
                } else {
                    gdaText = "Assinale sua resposta!";
                }
            }
            Toast.makeText(IniciarTeste.this, gdaText, Toast.LENGTH_SHORT).show();
            return false;
        }

        if ((qtRespondidas != qtCertas) && gdaText.equals("")) {
            if (mClasseQuestoes.isInformarQtQuestoes()) {
                gdaTextAux = " opção.";
                if (qtCertas > 1) {
                    gdaTextAux = " opções.";
                }
                gdaText = "Assinale " + Integer.toString(qtCertas) + gdaTextAux;
            } else {
                gdaText = "Assinale sua resposta!";
            }
            Toast.makeText(IniciarTeste.this, gdaText, Toast.LENGTH_SHORT).show();
            return false;
        }

        if (gdaText.equals("")) {
            if (mClasseQuestoes.isOpc1Certa()) {
                checkBox1.setTextColor(Color.BLUE);
            }
            if (mClasseQuestoes.isOpc2Certa()) {
                checkBox2.setTextColor(Color.BLUE);
            }
            if (mClasseQuestoes.isOpc3Certa()) {
                checkBox3.setTextColor(Color.BLUE);
            }
            if (mClasseQuestoes.isOpc4Certa()) {
                checkBox4.setTextColor(Color.BLUE);
            }
            if (mClasseQuestoes.isOpc5Certa()) {
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
            gdaText = "Pontuação atual: " + gdaPtosAnonimo + " ponto(s).";
            textViewPontos.setText(gdaText);
            gdaText = "";
            pontos = 1;
        }

        // ATUALIZA PONTUAÇÃO
        mUsuarioReference.child("pontosUsuario")
                .setValue(mClasseUsuarios.getpontosUsuario() + pontos);

        return true;

    }

    private void resetOpcoes() {

        checkBox1.setVisibility(View.INVISIBLE);
        checkBox2.setVisibility(View.INVISIBLE);
        checkBox3.setVisibility(View.INVISIBLE);
        checkBox4.setVisibility(View.INVISIBLE);
        checkBox5.setVisibility(View.INVISIBLE);

        checkBox1.setTextColor(Color.BLACK);
        checkBox2.setTextColor(Color.BLACK);
        checkBox3.setTextColor(Color.BLACK);
        checkBox4.setTextColor(Color.BLACK);
        checkBox5.setTextColor(Color.BLACK);

        checkBox1.setChecked(false);
        checkBox2.setChecked(false);
        checkBox3.setChecked(false);
        checkBox4.setChecked(false);
        checkBox5.setChecked(false);

        // limpa buttonTintList
        checkBox1.setButtonTintList(null);
        checkBox1.setButtonTintList(new ColorStateList(CHECK_BOX_STATES, CHECK_BOX_COLORS));
        checkBox2.setButtonTintList(null);
        checkBox2.setButtonTintList(new ColorStateList(CHECK_BOX_STATES, CHECK_BOX_COLORS));
        checkBox3.setButtonTintList(null);
        checkBox3.setButtonTintList(new ColorStateList(CHECK_BOX_STATES, CHECK_BOX_COLORS));
        checkBox4.setButtonTintList(null);
        checkBox4.setButtonTintList(new ColorStateList(CHECK_BOX_STATES, CHECK_BOX_COLORS));
        checkBox5.setButtonTintList(null);
        checkBox5.setButtonTintList(new ColorStateList(CHECK_BOX_STATES, CHECK_BOX_COLORS));

    }

    public void onClick(View v) {
        if (respButton.getText() == "responder") {
            if (corrigirQuestao()) {
                checkBox1.setClickable(false);
                checkBox2.setClickable(false);
                checkBox3.setClickable(false);
                checkBox4.setClickable(false);
                checkBox5.setClickable(false);
                respButton.setBackgroundResource(R.drawable.ic_proxima);
                respButton.setText("próxima");
            }
        } else {
            checkBox1.setClickable(true);
            checkBox2.setClickable(true);
            checkBox3.setClickable(true);
            checkBox4.setClickable(true);
            checkBox5.setClickable(true);
            respButton.setBackgroundResource(R.drawable.ic_responder);
            respButton.setText("responder");

            resetOpcoes();
            updateViewQuestao();

        }
    }

    private void lerParametros(String parm) {
        // LER PARAMETRO QT-QUESTOES
        mFirebase = FirebaseDatabase.getInstance();
        DatabaseReference tbParametros = mFirebase.getInstance().getReference().child("parametros").child(parm);
        tbParametros.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mClasseParametros = dataSnapshot.getValue(Parametros.class);
                qtQuestoes = mClasseParametros.getVlrParm();
                /// 'alert dialog' REGRAS
                if (SEMAFORO_DIALOG_ABERTO) {
                    alertar();
                    SEMAFORO_DIALOG_ABERTO = false;
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                // VALOR DEFAULT
                qtQuestoes = QT_QUESTOES_DEFAULT;
            }
        });
        // FIM LER:: PARAMETRO QT-QUESTOES
    }

    // TODO - teste - Carregar ARRAYLIST
    private void carregarArrayList() {
        Query query = mQuestoesReference;
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                setQuestoes.add(dataSnapshot.getValue(Questoes.class));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        //Set the value for the views
        ListView lista = (ListView) findViewById(R.id.listview);
        List<Questoes> questoes = setQuestoes;
        ArrayAdapter<Questoes> adapter = new ArrayAdapter<Questoes>(this, android.R.layout.simple_list_item_1, questoes);
        lista.setAdapter(adapter);
        //...
    }

/// [ FIM DE TUDO ///
}
