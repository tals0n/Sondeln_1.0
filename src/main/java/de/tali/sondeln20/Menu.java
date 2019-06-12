package de.tali.sondeln20;


import android.app.FragmentManager;
import android.app.Service;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

import com.google.android.gms.maps.MapFragment;

public class Menu extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);


        getSupportActionBar().hide();
    }

    public void create_finding(View view)
    {
        Intent i = new Intent(this, Create_Finding_Window.class);
        startActivity(i);


    }
    public void show_finding(View view)
    {
        Intent i = new Intent(this, Show_Findings_Window.class);
        startActivity(i);
    }

    public void map_show_all(View view)
    {
        Intent i = new Intent(this,All_Finding_Map.class);
        startActivity(i);
    }

    public void map_overlay(View view)
    {
        Intent i = new Intent(this,OverlayMap.class);
        startActivity(i);
    }

    public void close_application(View view)
    {
        finish();
    }
}
