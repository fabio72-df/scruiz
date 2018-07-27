package com.cursoandroid.firebaseapp.scruiz;

import java.util.ArrayList;

public class UsuariosRk {

    private String nmUsuario;
    private Integer pontosUsuario;

    public UsuariosRk() {
    }

    public UsuariosRk(String nmUsuario, Integer pontosUsuario) {
        this.nmUsuario = nmUsuario;
        this.pontosUsuario = pontosUsuario;
    }

    public String getNmUsuario() {
        return nmUsuario;
    }

    public void setNmUsuario(String nmUsuario) {
        this.nmUsuario = nmUsuario;
    }

    public Integer getPontosUsuario() {
        return pontosUsuario;
    }

    public void setPontosUsuario(Integer pontosUsuario) {
        this.pontosUsuario = pontosUsuario;
    }
}
