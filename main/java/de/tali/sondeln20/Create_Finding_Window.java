package de.tali.sondeln20;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Environment;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.sql.Blob;
import java.sql.Date;
import java.text.SimpleDateFormat;


public class Create_Finding_Window extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private double latitude, longitude;
    TextView txt_latitide;
    TextView txt_longitude;
    private String findingUri;
    private LocationManager locationManager;
    private LocationListener locationListener;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private ImageView selectedImagePreview;
    private Uri imageFileUri;
    private Switch sw;
    private static final int SELECT_PICTURE = 2;
    private static final int TAKE_PICTURE = 1;
    private String pictureName;
    EditText et_name;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create__finding__window);
        getSupportActionBar().hide();

        selectedImagePreview = (ImageView) findViewById(R.id.imageView);
        txt_latitide = (TextView) findViewById(R.id.txt_latitude);
        txt_longitude = (TextView) findViewById(R.id.txt_longitude);
        et_name = (EditText) findViewById(R.id.edit_name);
        sw = (Switch) findViewById(R.id.switch1);


        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        {

        }
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                longitude = location.getLongitude();
                latitude = location.getLatitude();

                txt_longitude.setText("" + longitude);
                txt_latitide.setText("" + latitude);
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationManager.requestLocationUpdates("gps", 5000, 0, locationListener);


    }


    public void take_pic(View view) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, TAKE_PICTURE);

    }


    public void open_photo_gallery(View view) {
        //Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        //if (i.resolveActivity(getPackageManager()) != null) {
        //   startActivityForResult(i, 1);
        //}

        Intent i = new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(i, "Select Picture"), SELECT_PICTURE);


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 2) {
                Uri imageUri = data.getData();
                findingUri = imageUri.toString();
                try {
                    Bitmap bitmap = getThumbnail(imageUri);
                    selectedImagePreview.setImageBitmap(bitmap);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (requestCode == 1) {

                Bitmap picture = (Bitmap) data.getExtras().get("data");
                String picname = "" + System.currentTimeMillis();
                findingUri = (MediaStore.Images.Media.insertImage(getContentResolver(), picture, picname, null)).toString();


                //  File destination = new File(Environment.getExternalStorageDirectory(),System.currentTimeMillis() +".jpg");
                // File destination = new File ("content://media/external/images/media/", System.currentTimeMillis() +".jpg");

                // saveOnSDCard(picture,destination);
                // findingUri = destination.toURI().toString();
                // pictureName =  destination.getName();

                selectedImagePreview.setImageBitmap(picture);


            }


        }
    }




 /*   public void  saveOnSDCard(Bitmap bitmapPic,File dest)
    {
        FileOutputStream fos = null;
        try
        {
           // fos = new FileOutputStream(dest);
           // bitmapPic.compress(Bitmap.CompressFormat.PNG,0,fos);



        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }*/


    public Bitmap getThumbnail(Uri uri) throws FileNotFoundException, IOException {
        InputStream input = this.getContentResolver().openInputStream(uri);

        BitmapFactory.Options onlyBoundsOptions = new BitmapFactory.Options();
        onlyBoundsOptions.inJustDecodeBounds = true;
        onlyBoundsOptions.inDither = true;//optional
        onlyBoundsOptions.inPreferredConfig = Bitmap.Config.ARGB_8888;//optional
        BitmapFactory.decodeStream(input, null, onlyBoundsOptions);
        input.close();
        if ((onlyBoundsOptions.outWidth == -1) || (onlyBoundsOptions.outHeight == -1))
            return null;

        int originalSize = (onlyBoundsOptions.outHeight > onlyBoundsOptions.outWidth) ? onlyBoundsOptions.outHeight : onlyBoundsOptions.outWidth;

        double ratio = (originalSize > 500) ? (originalSize / 500) : 1.0;

        BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
        bitmapOptions.inSampleSize = getPowerOfTwoForSampleRatio(ratio);
        bitmapOptions.inDither = true;//optional
        bitmapOptions.inPreferredConfig = Bitmap.Config.ARGB_8888;//optional
        input = this.getContentResolver().openInputStream(uri);
        Bitmap bitmap = BitmapFactory.decodeStream(input, null, bitmapOptions);
        input.close();
        return bitmap;
    }

    private static int getPowerOfTwoForSampleRatio(double ratio) {
        int k = Integer.highestOneBit((int) Math.floor(ratio));
        if (k == 0) return 1;
        else return k;
    }


    public void refresh_position(View view) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        if (sw.isChecked())
            locationManager.requestLocationUpdates("gps", 5000, 0, locationListener);
        else {


            setupLocationRequest();
            setupGoogleApiClient();
            mGoogleApiClient.connect();


        }

    }


    public void finding_save(View view) {
        String name = et_name.getText().toString();
        if (name.matches(""))
            Toast.makeText(getApplication(), "Bitte Namen für Fundstück eintragen", Toast.LENGTH_LONG).show();
        if (txt_latitide.getText().toString().matches("Latitude :"))
            Toast.makeText(getApplication(), "Warten auf GPS Signal", Toast.LENGTH_LONG).show();
        if (txt_longitude.getText().toString().matches("Longitude :"))
            Toast.makeText(getApplication(), "Warten auf GPS Signal", Toast.LENGTH_LONG).show();
        else {
            DatabaseHandler db = new DatabaseHandler(this);
            Finding finding = new Finding();
            finding.setName(name);
            finding.setLatitude(Double.parseDouble(txt_latitide.getText().toString()));
            finding.setLongitude(Double.parseDouble(txt_longitude.getText().toString()));
            finding.setPicture(findingUri);
            db.addFinding(finding);
            finish();
        }
    }


    private void setupGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }


    private void setupLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(5000);
        mLocationRequest.setFastestInterval(2000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(mLocationRequest);
        PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient, builder.build());
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Location mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if(mLastLocation != null)
        {
            txt_longitude.setText(""+mLastLocation.getLongitude());
            txt_latitide.setText(""+mLastLocation.getLatitude());
        }


    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}



