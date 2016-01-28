package com.keegan.experiment;

/**
 * Created by ahmed on 9/06/14.
 */
public enum INTENT {

    FRAGMENT_ITEM_CANCELLED("FRAGMENT_ITEM_CANCELLED"),
    PICKED_CONTACT_INFO("PICKED_CONTACT_INFO"),
    PICKED_CONTACT_INFO_EXTRA_NAME("PICKED_CONTACT_INFO_EXTRA_NAME"),
    PICKED_CONTACT_INFO_EXTRA_PHONE_NUMBER("PICKED_CONTACT_INFO_EXTRA_PHONE_NUMBER"),
    PICK_CONTACT("PICK_CONTACT"),
    ;

    private final String name;

    private INTENT(String s) {
        name = s;
    }

    public boolean equalsName(String otherName){
        return (otherName == null)? false:name.equals(otherName);
    }

    public String toString(){
        return name;
    }
}
