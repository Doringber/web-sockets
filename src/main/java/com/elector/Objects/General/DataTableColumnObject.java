package com.elector.Objects.General;

import static com.elector.Utils.Definitions.DATA_TABLE_RENDER_REGULAR;
import static com.elector.Utils.Definitions.EMPTY;

/**
 * Created by Sigal on 9/9/2017.
 */
public class DataTableColumnObject {
    private String name;
    private boolean isDropdown;
    private String dropdownId;
    private boolean editable;
    private int renderer;
    private int inputType;

    public DataTableColumnObject(String name, boolean isDropdown, boolean editable) {
        this.name = name;
        this.isDropdown = isDropdown;
        this.editable = editable;
    }

    public DataTableColumnObject(String name, boolean isDropdown, String dropdownId, boolean editable) {
        this(name, isDropdown, editable);
        this.dropdownId = dropdownId;
    }

    public DataTableColumnObject(String name, boolean isDropdown, boolean editable, int renderer) {
        this(name, isDropdown, EMPTY, editable);
        this.renderer = renderer;
    }

    public DataTableColumnObject(String name, boolean isDropdown, String dropdownId, boolean editable, int renderer, int inputType) {
        this(name, isDropdown, editable, renderer);
        this.inputType = inputType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isDropdown() {
        return isDropdown;
    }

    public void setDropdown(boolean dropdown) {
        isDropdown = dropdown;
    }

    public String getDropdownId() {
        return dropdownId;
    }

    public void setDropdownId(String dropdownId) {
        this.dropdownId = dropdownId;
    }

    public boolean isEditable() {
        return editable;
    }

    public void setEditable(boolean editable) {
        this.editable = editable;
    }

    public int getRenderer() {
        return renderer;
    }

    public void setRenderer(int renderer) {
        this.renderer = renderer;
    }

    public int getInputType() {
        return inputType;
    }

    public void setInputType(int inputType) {
        this.inputType = inputType;
    }
}
