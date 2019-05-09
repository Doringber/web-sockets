package com.elector.Objects.General;

import java.lang.reflect.Field;
import java.util.List;

import static com.elector.Utils.Definitions.BREAK;
import static com.elector.Utils.Definitions.COMMA;
import static com.elector.Utils.Definitions.MINUS;

/**
 * Created by Shai on 2/19/2018.
 */

class ReportObject {
    public String printAsCsvRow(List<String> includeFields) {
        StringBuilder stringBuilder = new StringBuilder();
        try {
            Field[] fields = this.getClass().getDeclaredFields();
            for (String fieldName : includeFields) {
                for (Field field : fields) {
                    if (field.getName().equals(fieldName)) {
                        field.setAccessible(true);
                        Object value = field.get(this);
                        stringBuilder.append(value == null ? MINUS : value.toString()).append(COMMA);
                        break;
                    }
                }
            }
            stringBuilder.append(BREAK);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();}

}
