package com.steinbock.discgolfgo;

public class ScoresheetModel {

    private String name;
    private int[] scores;

    public ScoresheetModel(String name) {
        this.name = name;
        this.scores = new int[9];
    }

    public ScoresheetModel(String name, int[] scores) {
        this.name = name;
        this.scores = scores;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int[] getScores() {
        return scores;
    }

    public void setScores(int[] scores) {
        this.scores = scores;
    }

    public int getScore(int i) {
        return scores[i];
    }

    public void setScore(int i, int val) {
        scores[i] = val;
    }

    public int incrementScore(int i) {
        return ++scores[i];
    }

    public int getTotal() {
        int sum = 0;
        for (int i = 0; i < scores.length; i++) {
            sum += scores[i];
        }
        return sum;
    }
}
