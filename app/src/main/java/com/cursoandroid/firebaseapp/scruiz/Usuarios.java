package com.cursoandroid.firebaseapp.scruiz;

public class Usuarios {

    private String nmUsuario;
    private int pontosUsuario;

    private Usuarios() {
    }

    public Usuarios(String nmUsuario, int ptosUsuario) {
        this.nmUsuario = nmUsuario;
        this.pontosUsuario = ptosUsuario;
    }

    public String getNmUsuario() {
        return nmUsuario;
    }

    public void setNmUsuario(String nmUsuario) {
        this.nmUsuario = nmUsuario;
    }

    public int getpontosUsuario() {
        return pontosUsuario;
    }

    public void setpontosUsuario(int pontosUsuario) {
        this.pontosUsuario = pontosUsuario;
    }
}
