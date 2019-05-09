package com.elector.Objects.Entities;

import com.elector.Objects.General.BaseContactGroupObject;

public class CustomGroupObject extends BaseContactGroupObject {
    private int size;
    private boolean inSurvey;

    public CustomGroupObject () {
    }

    public CustomGroupObject (int oid) {
        this.oid = oid;
    }


    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public boolean isInSurvey() {
        return inSurvey;
    }

    public void setInSurvey(boolean inSurvey) {
        this.inSurvey = inSurvey;
    }

    public void increaseSize () {
        this.size++;
    }

    public void decreaseSize () {
        this.size--;
    }
}
