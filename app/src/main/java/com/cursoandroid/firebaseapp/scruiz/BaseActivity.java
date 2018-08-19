package com.cursoandroid.firebaseapp.scruiz;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.cursoandroid.firebaseapp.scruiz.interfaces.ProgressBarInterface;
import com.cursoandroid.firebaseapp.scruiz.utils.MinhaProgressBar;


public class BaseActivity extends AppCompatActivity {

    // ProgressBar
    public MinhaProgressBar mpb;
    protected ProgressBar mProgressBar;
    private TextView textViewPB;
    // fim: ProgressBar

    public ProgressDialog mProgressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        // ProgressBar
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
        textViewPB = (TextView) findViewById(R.id.textView_Carregando);
        // ProgressBar *** fim ***

    }

    @Override
    protected void onStart() {
        super.onStart();
        // Animation
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.anim);
        ImageView mLogo = findViewById(R.id.img_LogoSplash);
        mLogo.setImageResource(R.drawable.te_transp_amarelo);
        mLogo.setAlpha(1.0F);
        mLogo.startAnimation(animation);
        // *** fim *** Animation

        // ProgressBar
        // (::para receber retorno enviar Interface (ProgressBarInterface)
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
        textViewPB = (TextView) findViewById(R.id.textView_Carregando);
        ProgressBarInterface pbi = new ProgressBarInterface() {
            @Override
            public void retornoProgressBar(boolean isSucesso, String msg) {
                Intent intent = new Intent(BaseActivity.this, Principal.class);
                startActivity(intent);
            }
        };
        mpb = new MinhaProgressBar(this, mProgressBar, textViewPB, pbi);
        mpb.execute();
        // ---[fim}--- progressBar();

    }

    public void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage(getString(R.string.loading));
            mProgressDialog.setIndeterminate(true);
        }
        mProgressDialog.show();
    }
    public void hideProgressDialog() {
        // TODO
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }
    public void hideKeyboard(View view) {
        final InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        hideProgressDialog();
    }

/*
////////////////////////////////////////////////////////////////////////////////////////////////////
FIM: BaseActivity
////////////////////////////////////////////////////////////////////////////////////////////////////
 */
}
