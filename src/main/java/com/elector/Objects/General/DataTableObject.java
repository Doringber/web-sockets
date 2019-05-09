package com.elector.Objects.General;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Sigal on 9/8/2017.
 */
public class DataTableObject {
    private int type;
    private String name;
    private String id;
    private List<Integer> buttons;
    private List<DataTableColumnObject> columns;

    public DataTableObject(int type, String name, String id, List<Integer> buttons, List<DataTableColumnObject> columns) {
        this.type = type;
        this.name = name;
        this.id = id;
        this.buttons = buttons;
        this.columns = columns;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<Integer> getButtons() {
        return buttons;
    }

    public void setButtons(List<Integer> buttons) {
        this.buttons = buttons;
    }

    public List<DataTableColumnObject> getColumns() {
        return columns;
    }

    public void setColumns(List<DataTableColumnObject> columns) {
        this.columns = columns;
    }
}
