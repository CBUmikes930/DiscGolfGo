package com.steinbock.discgolfgo;

import java.util.List;

public class DiscModel {

    private String name;
    private String desc;
    private String type;
    private int speed;
    private int glide;
    private int turn;
    private int fade;
    private List<String> plastics;

    private boolean isExpanded;

    public DiscModel() {

    }

    public DiscModel(String name, String desc, String type) {
        this.name = name;
        this.desc = desc;
        this.type = type;
        isExpanded = false;
    }

    public DiscModel(String name, String desc, String type, int speed, int glide, int turn, int fade) {
        this(name, desc, type, speed, glide, turn, fade, null);
    }

    public DiscModel(String name, String desc, String type, int speed, int glide, int turn, int fade, List<String> plastics) {
        this.name = name;
        this.desc = desc;
        this.type = type;
        this.speed = speed;
        this.glide = glide;
        this.turn = turn;
        this.fade = fade;
        this.plastics = plastics;
        isExpanded = false;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public int getGlide() {
        return glide;
    }

    public void setGlide(int glide) {
        this.glide = glide;
    }

    public int getTurn() {
        return turn;
    }

    public void setTurn(int turn) {
        this.turn = turn;
    }

    public int getFade() {
        return fade;
    }

    public void setFade(int fade) {
        this.fade = fade;
    }

    public List<String> getPlastics() {
        return plastics;
    }

    public void setPlastics(List<String> plastics) {
        this.plastics = plastics;
    }

    public boolean isExpanded() {
        return isExpanded;
    }

    public void setExpanded(boolean expanded) {
        isExpanded = expanded;
    }
}
