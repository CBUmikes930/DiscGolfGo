package com.steinbock.discgolfgo;

import java.util.List;

public class CategoryModel {

    private List<DiscModel> discs;
    private String category;
    private boolean isExpanded;

    public CategoryModel(List<DiscModel> discs, String category) {
        this.discs = discs;
        this.category = category;
        this.isExpanded = false;
    }

    public List<DiscModel> getDiscs() {
        return discs;
    }

    public void setDiscs(List<DiscModel> discs) {
        this.discs = discs;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public boolean isExpanded() {
        return isExpanded;
    }

    public void setExpanded(boolean expanded) {
        isExpanded = expanded;
    }
}
