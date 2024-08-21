package com.acme.sample.shared.domain.model.valueobjects;

public class Address {
    private String street;
    private String number;
    private String city;
    private String state;
    private String zipCode;
    private String country;


    public Address() {
        this.street = "";
        this.number = "";
        this.city = "";
        this.state = "";
        this.zipCode = "";
        this.country = "";
    }


    public Address(String street, String number, String city, String state, String zipCode, String country) {
        this.street = street;
        this.number = number;
        this.city = city;
        this.state = state;
        this.zipCode = zipCode;
        this.country = country;
    }

    public String getAddressAsString() {
        return String.format("%s %s, %s, %s, %s, %s", street, number, city, state, zipCode, country);
    }
}
