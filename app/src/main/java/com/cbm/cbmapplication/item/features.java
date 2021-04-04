package com.cbm.cbmapplication.item;

public class features {
    private String[] mData;

    public features(String[] data) {
        mData = data;
    }

    public features(String email) {
        mData = new String[1];

        mData[0] = email;
    }
}