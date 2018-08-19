package com.cursoandroid.firebaseapp.scruiz.utils;

import android.content.Context;
import android.os.AsyncTask;
import android.view.Gravity;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.cursoandroid.firebaseapp.scruiz.R;
import com.cursoandroid.firebaseapp.scruiz.interfaces.ProgressBarInterface;

import java.lang.ref.WeakReference;

public class MinhaProgressBar extends AsyncTask<Object, Object, String> {

    private final int PROGRESSO = 20;
    private ProgressBarInterface progressBarInterface;
    private WeakReference<ProgressBar> progressBar;
    private WeakReference<TextView> texto;
    private static int total = 0;

    public MinhaProgressBar(Context context, ProgressBar progressBar, TextView texto, ProgressBarInterface progressBarInterface) {
        this.progressBar = new WeakReference<>(progressBar);
        this.texto = new WeakReference<>(texto);
        this.progressBarInterface = progressBarInterface;
    }

    @Override
    protected void onPreExecute() {
        total = 0;
        progressBar.get().setProgress(0);
        progressBar.get().setVisibility(View.VISIBLE);
        texto.get().setText("0%");
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(Object... params) {
        try {
            Thread.sleep(1000);

            int qtCiclos = 100 / PROGRESSO;
            for (int i = 0; i < qtCiclos; i++) {
                publishProgress();
                Thread.sleep(1000);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onProgressUpdate(Object... values) {
        total += PROGRESSO;
        progressBar.get().incrementProgressBy(PROGRESSO);
        String gdaText = "Carregando informações [" + total + "%" + "]";
        texto.get().setText(gdaText);
        super.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(String result) {

        progressBar.get().setVisibility(ProgressBar.INVISIBLE);
        texto.get().setText(R.string.pronto);
        texto.get().setGravity(Gravity.CENTER_HORIZONTAL);

        if (progressBarInterface != null) {
            progressBarInterface.retornoProgressBar(true, "FIM Progress");
        }

        super.onPostExecute(result);

    }
}
