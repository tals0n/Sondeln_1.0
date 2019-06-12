package de.tali.sondeln20;

import java.sql.Blob;

/**
 * Created by Tali on 01.06.2016.
 */
public class Finding
{
    private String _picture;
    private int _id;
    private String _name;
    private double _longitude;
    private double _latitude;


    public Finding(int id, String name , double latitude , double longitude, String picture)
    {
        this._id = id;
        this._name = name;
        this._latitude = latitude;
        this._longitude = longitude;
        this._picture = picture;
    }

    public Finding() {

    }


    public int getid() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }



    public String getName() {
        return _name;
    }

    public void setName(String name) {
        this._name = name;
    }



    public double getLongitude() {
        return _longitude;
    }

    public void setLongitude(double longitude) {
        this._longitude = longitude;
    }



    public double getLatitude() {
        return _latitude;
    }

    public void setLatitude(double latitude) {
        this._latitude = latitude;
    }



    public String getPicture() {
        return _picture;
    }

    public void setPicture(String picture) {
        this._picture = picture;
    }


}
