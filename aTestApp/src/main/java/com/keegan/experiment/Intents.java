package com.keegan.experiment;

/**
 * Created by ahmed on 9/06/14.
 */
public enum Intents {

    LOGIN_SUCCESS("LOGIN_SUCCESS"),
    LOGIN_FAIL("LOGIN_FAIL"),
    FRAGMENT_ITEM_CANCELLED("FRAGMENT_ITEM_CANCELLED"),
    RELOAD_PROFILE("RELOAD_PROFILE"),
    PICKED_CONTACT_INFO("PICKED_CONTACT_INFO"),
    PICKED_CONTACT_INFO_EXTRA_NAME("PICKED_CONTACT_INFO_EXTRA_NAME"),
    PICKED_CONTACT_INFO_EXTRA_PHONE_NUMBER("PICKED_CONTACT_INFO_EXTRA_PHONE_NUMBER"),
    PICK_CONTACT("PICK_CONTACT"),;

    private final String name;

    private Intents(String s) {
        name = s;
    }

    public boolean equalsName(String otherName) {
        return (otherName == null) ? false : name.equals(otherName);
    }

    public String toString() {
        return name;
    }
}
