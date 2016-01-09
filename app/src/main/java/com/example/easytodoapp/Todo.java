package com.example.easytodoapp;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

/**
 * Created by AL_META on 12/07/2015.
 */

public class Todo {

    private UUID mId;
    private String mTitle;
    private Date mDate;
    private boolean mSolved;
    private String mContact;

    public Todo() {
        this(UUID.randomUUID());
    }

    public Todo(UUID id) {
        mId = id;
        mDate = new Date();
    }

    public UUID getID() {
        return mId;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public Date getDate() {
        return mDate;
    }

    public void setDate(Date date) {
        mDate = date;
    }

    public boolean isSolved() {
        return mSolved;
    }

    public void setSolved(boolean solved) {
        mSolved = solved;
    }

    public String getContact() {
        return mContact;
    }

    public void setContact(String contact) {
        mContact = contact;
    }

    public String getFormattedDate() {
        String format = "EEEE, MMM d, yyyy";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format, Locale.US);
        return simpleDateFormat.format(mDate);
    }

    public String getFormattedTime() {
        String format = "HH:mm";
        SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.US);
        return sdf.format(mDate);
    }


}

