/*
 * Copyright 2018, Team 26 CMPUT 301. University of Alberta - All Rights Reserved. You may use, distribute, or modify this code under the terms and coditions fo the Code of Student Behaviour at the University of Alberta.
 */

package com.cmput301w18t26.taskit;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;


import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Date;

/**
 * Represents a single Photo.
 * @author UAlberta-Cmput301-Team26 crew
 * @see PhotoList
 */
public class Photo {


    /**
     * The photo is stored here.
     */
    private Bitmap photo;

    /**
     * The stringified photo for the server is stored here.
     */
    private String photoString;

    /**
     * The UUID of the task this bid was placed on.
     */
    private String parentTask;

    /**
     * Metadata for sync. UUID for task.
     */
    private String UUID;

    /**
     * Metadata for sync. Timestamp task created/updated.
     */
    private Date timestamp;

    /**
     * Task owner username.
     */
    private String owner;

    public Bitmap getPhoto() {
        return photo;
    }

    public void setPhoto(Bitmap photo) {
        this.photo = photo;
    }

    public void setOwner(String o) {
        this.owner = o;
    }

    public void setOwner(User u) {this.owner = u.getOwner();}

    public String getOwner() {
        return this.owner;
    }

    public boolean isOwner(String s) {
        return this.owner.equals(s);
    }

    public boolean isOwner(User u) {
        return this.owner.equals(u.getOwner());
    }

    public String getUUID() {
        return UUID;
    }

    public void setUUID(String UUID) {
        this.UUID = UUID;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    /**
     * Check if a given task is the parent of this bid.
     * Compare using unique UUID.
     * @param t the task to check if is parent.
     * @return true if given task is the parent task, false o.w.
     */
    public boolean isParentTask(Task t) {
        return parentTask.equals(t.getUUID());
    }

    public boolean isParentTask(String uuid) {
        return parentTask.equals(uuid);
    }

    public String getParentTask() {
        return parentTask;
    }

    public void setParentTask(String parentTask) {
        this.parentTask = parentTask;
    }

    public void setParentTask(Task parentTask) {
        this.parentTask = parentTask.getUUID();
    }

    /**
     * We resize the photo substantially.
     */
    public void reduceFilesize() {
        // Resize
        int w = photo.getWidth();
        int h = photo.getHeight();
        Log.d("Photo","Photo input (width,height) = ("+Integer.toString(w)+","+Integer.toString(h)+")");
        int dest_w = 128;
        int dest_h = (int) ((dest_w/(double) w)*h);
        Log.d("Photo","Photo output (width,height) = ("+Integer.toString(dest_w)+","+Integer.toString(dest_h)+")");

        photo = Bitmap.createScaledBitmap(photo, dest_w, dest_h, false);
    }

    /**
     * Prior to viewing, we upscale the image considerably.
     */
    public void resizeForDisplay() {
        // Resize
        int w = photo.getWidth();
        int h = photo.getHeight();
        Log.d("Photo","Photo input (width,height) = ("+Integer.toString(w)+","+Integer.toString(h)+")");
        int dest_w = 750;
        int dest_h = (int) ((dest_w/(double) w)*h);
        Log.d("Photo","Photo output (width,height) = ("+Integer.toString(dest_w)+","+Integer.toString(dest_h)+")");
        photo = Bitmap.createScaledBitmap(photo, dest_w, dest_h, false);
    }

    public int getFilesize() {
        return photo.getByteCount();
    }

    /**
     * The server can store base64 encoded images. Perform the encoding.
     */
    public void Stringify() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        photo.compress(Bitmap.CompressFormat.JPEG, 30, baos);
        byte[] b = baos.toByteArray();
        String temp = null;
        try {
            System.gc();
            Log.d("Photo", "Photo being stringified to "+Integer.toString(b.length)+" bytes");
            temp = Base64.encodeToString(b, Base64.DEFAULT);
        } catch (Exception e) {
            e.printStackTrace();
        } catch (OutOfMemoryError e) {
            Log.d("Photo", "Photo too large to stringify");
        }
        photoString = temp;
    }

    /**
     * When loading from the server, need to convert from base64 encoding back to Bitmap.
     */
    public void ConvertFromString() {
        byte[] b;
        try {
            System.gc();
            b = Base64.decode(photoString, Base64.DEFAULT);
            photo = BitmapFactory.decodeByteArray(b, 0, b.length);
            Log.d("Photo", "Photo after convertfromstring to "+Integer.toString(photo.getByteCount())+" bytes");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
