package com.cursoandroid.firebaseapp.scruiz;

public class Parametros {

    private String nmParm;
    private String txtParm;
    private int vlrParm;

    public Parametros() {
    }

    public Parametros(String nmParm, String txtParm, int vlrParm) {
        this.nmParm = nmParm;
        this.txtParm = txtParm;
        this.vlrParm = vlrParm;
    }

    public String getNmParametro() {
        return nmParm;
    }

    public void setNmParametro(String nmParametro) {
        this.nmParm = nmParametro;
    }

    public String getTxtParametro() {
        return txtParm;
    }

    public void setTxtParametro(String txtParametro) {
        this.txtParm = txtParametro;
    }

    public int getVlrParm() {
        return vlrParm;
    }

    public void setVlrParm(int vlrParm) {
        this.vlrParm = vlrParm;
    }
}
