package com.cursoandroid.firebaseapp.scruiz.ClassesDados;

import com.cursoandroid.firebaseapp.scruiz.Questoes;
import java.util.ArrayList;

public class Questionario {

    private static ArrayList<Questoes> arrayQuestoes;

    public Questionario() {
    }

    public static ArrayList<Questoes> getArrayQuestoes() {
        return arrayQuestoes;
    }

    public static void setArrayQuestoes(ArrayList<Questoes> arrayQuestoes) {
        Questionario.arrayQuestoes = arrayQuestoes;
    }
}
