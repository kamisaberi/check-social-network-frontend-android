package com.narij.checkv2.model;

import java.util.Objects;

public class ContactModel {

    public String id;
    public String name;
    public String mobileNumber;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ContactModel that = (ContactModel) o;
        return Objects.equals(mobileNumber, that.mobileNumber);
    }

    @Override
    public int hashCode() {

        return Objects.hash(mobileNumber);
    }
}
