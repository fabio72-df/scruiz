package com.cursoandroid.firebaseapp.scruiz;

public class Usuarios {

    private String nmUsuario;
    private int pontosUsuario;

    public Usuarios() {
    }

    public Usuarios(String nmUsuario, Integer ptosUsuario) {
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
