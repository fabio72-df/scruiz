package com.cursoandroid.firebaseapp.scruiz;

public class Questoes {

    private boolean aceitaEmBranco;
    private boolean informarQtQuestoes;
    private boolean opc1Certa;
    private boolean opc2Certa;
    private boolean opc3Certa;
    private boolean opc4Certa;
    private boolean opc5Certa;
    private String txtFonte;
    private String txtOpc1;
    private String txtOpc2;
    private String txtOpc3;
    private String txtOpc4;
    private String txtOpc5;
    private String txtQuestao;

    public Questoes() {
    }

    public Questoes(boolean aceitaEmBranco, boolean informarQtQuestoes, boolean opc1Certa, boolean opc2Certa, boolean opc3Certa, boolean opc4Certa, boolean opc5Certa, String txtFonte, String txtOpc1, String txtOpc2, String txtOpc3, String txtOpc4, String txtOpc5, String txtQuestao) {
        this.aceitaEmBranco = aceitaEmBranco;
        this.informarQtQuestoes = informarQtQuestoes;
        this.opc1Certa = opc1Certa;
        this.opc2Certa = opc2Certa;
        this.opc3Certa = opc3Certa;
        this.opc4Certa = opc4Certa;
        this.opc5Certa = opc5Certa;
        this.txtFonte = txtFonte;
        this.txtOpc1 = txtOpc1;
        this.txtOpc2 = txtOpc2;
        this.txtOpc3 = txtOpc3;
        this.txtOpc4 = txtOpc4;
        this.txtOpc5 = txtOpc5;
        this.txtQuestao = txtQuestao;
    }

    public boolean isAceitaEmBranco() {
        return aceitaEmBranco;
    }

    public void setAceitaEmBranco(boolean aceitaEmBranco) {
        this.aceitaEmBranco = aceitaEmBranco;
    }

    public boolean isInformarQtQuestoes() {
        return informarQtQuestoes;
    }

    public void setInformarQtQuestoes(boolean informarQtQuestoes) {
        this.informarQtQuestoes = informarQtQuestoes;
    }

    public boolean isOpc1Certa() {
        return opc1Certa;
    }

    public void setOpc1Certa(boolean opc1Certa) {
        this.opc1Certa = opc1Certa;
    }

    public boolean isOpc2Certa() {
        return opc2Certa;
    }

    public void setOpc2Certa(boolean opc2Certa) {
        this.opc2Certa = opc2Certa;
    }

    public boolean isOpc3Certa() {
        return opc3Certa;
    }

    public void setOpc3Certa(boolean opc3Certa) {
        this.opc3Certa = opc3Certa;
    }

    public boolean isOpc4Certa() {
        return opc4Certa;
    }

    public void setOpc4Certa(boolean opc4Certa) {
        this.opc4Certa = opc4Certa;
    }

    public boolean isOpc5Certa() {
        return opc5Certa;
    }

    public void setOpc5Certa(boolean opc5Certa) {
        this.opc5Certa = opc5Certa;
    }

    public String getTxtFonte() {
        return txtFonte;
    }

    public void setTxtFonte(String txtFonte) {
        this.txtFonte = txtFonte;
    }

    public String getTxtOpc1() {
        return txtOpc1;
    }

    public void setTxtOpc1(String txtOpc1) {
        this.txtOpc1 = txtOpc1;
    }

    public String getTxtOpc2() {
        return txtOpc2;
    }

    public void setTxtOpc2(String txtOpc2) {
        this.txtOpc2 = txtOpc2;
    }

    public String getTxtOpc3() {
        return txtOpc3;
    }

    public void setTxtOpc3(String txtOpc3) {
        this.txtOpc3 = txtOpc3;
    }

    public String getTxtOpc4() {
        return txtOpc4;
    }

    public void setTxtOpc4(String txtOpc4) {
        this.txtOpc4 = txtOpc4;
    }

    public String getTxtOpc5() {
        return txtOpc5;
    }

    public void setTxtOpc5(String txtOpc5) {
        this.txtOpc5 = txtOpc5;
    }

    public String getTxtQuestao() {
        return txtQuestao;
    }

    public void setTxtQuestao(String txtQuestao) {
        this.txtQuestao = txtQuestao;
    }
}
