package de.tali.sondeln20;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

public class All_Finding_Map extends FragmentActivity implements OnMapReadyCallback  {

    private GoogleMap mMap;
    private List<Finding> Findinglist;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all__finding__map);

        DatabaseHandler db = new DatabaseHandler(this);
        Findinglist = db.getAllFinding();
        db.close();
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    void Get_Data_For_Markers()
    {
        for(int i = 0; i<Findinglist.size(); i++)
        {
            String name = Findinglist.get(i).getName();
            Double longitude = Findinglist.get(i).getLongitude();
            Double latitide = Findinglist.get(i).getLatitude();
            LatLng pos = new LatLng(latitide,longitude);
            mMap.addMarker(new MarkerOptions().position(pos).title(name));



        }
        LatLng lastposi = new LatLng(Findinglist.get(Findinglist.size()-1).getLatitude(),
                Findinglist.get(Findinglist.size()-1).getLongitude());
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(lastposi,12.0f));

    }




    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        Get_Data_For_Markers();

    }
}
