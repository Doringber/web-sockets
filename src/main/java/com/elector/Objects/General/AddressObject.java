package com.elector.Objects.General;


import java.util.Arrays;
import java.util.List;

import static com.elector.Utils.Definitions.COMMA;
import static com.elector.Utils.Definitions.SPACE;

public class AddressObject {
    private String street;
    private String number;
    private String city;

    public AddressObject (String address) {
        List<String> tokens = Arrays.asList(address.split(SPACE));
        int numberIndex = 0;
        for (int i = 0; i < tokens.size(); i++) {
            if (tokens.get(i).contains(COMMA)) {
                numberIndex = i;
                break;
            }
        }
        StringBuilder streetBuilder = new StringBuilder();
        for (int i = 0; i < numberIndex; i++) {
            streetBuilder.append(tokens.get(i));
        }
        this.street = streetBuilder.toString();
        this.number = tokens.get(numberIndex);

        StringBuilder cityBuilder = new StringBuilder();
        for (int i = (numberIndex + 1); i < tokens.size(); i++) {
            cityBuilder.append(tokens.get(i));
        }
        this.city = cityBuilder.toString();

    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getGoogleMapsFormatAddress () {
        return String.format("%s null, %s, null, ישראל, %s, null, null", this.number, this.street, this.city);

    }
}
