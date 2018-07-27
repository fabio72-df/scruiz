package com.cursoandroid.firebaseapp.scruiz;

import java.util.ArrayList;
import java.util.Random;

public class NumeroAleaorio {

    private Random nrAleatorio = new Random();

    private static ArrayList<Integer> repeat_list = new ArrayList();

    private int nrMax, ultNr;

    private int nrSorteado;

    public NumeroAleaorio() {
    }
    public NumeroAleaorio(int nrSorteado) {
        this.nrSorteado = nrSorteado;
    }
    public int getNrSorteado() {
        int nrSorteado = nrSort(nrMax);
        return nrSorteado;
    }

    public void setNrMax(int nrMax) {
        this.nrMax = nrMax;
    }

    private int nrSort(int nrMax) {

        if (repeat_list.size() == nrMax) {
            ultNr = repeat_list.get(nrMax - 1);
            repeat_list = new ArrayList();
            repeat_list.add(ultNr);
        }

        int numero = nrAleatorio.nextInt(nrMax) + 1;

        while (true) {
            if (repeat_list.contains(numero)) {
                numero = nrAleatorio.nextInt(nrMax) + 1;
            } else {
                repeat_list.add(numero);
                break;
            }
        }
        return numero;
    }


}
