package com.cbm.cbmapplication.item;

public class ListItem {
    private String[] mData;

    public ListItem(String[] data) {
        mData = data;
    }

    public ListItem(String id, String passwd, String type) {
        mData = new String[3];

        mData[0] = id;

        mData[1] = passwd;

        mData[2] = type;
    }

    public String[] getData() {

        return mData;
    }

    public String getData(int index) {

        return mData[index];

    }

    public void setData(String[] data) {

        mData = data;

    }
}