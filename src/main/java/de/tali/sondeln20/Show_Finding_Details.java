package de.tali.sondeln20;

        import android.content.Intent;
        import android.graphics.Bitmap;
        import android.graphics.BitmapFactory;
        import android.net.Uri;
        import android.os.Environment;
        import android.provider.MediaStore;
        import android.support.v7.app.AppCompatActivity;
        import android.os.Bundle;
        import android.view.View;
        import android.widget.ImageView;
        import android.widget.TextView;

        import java.io.File;
        import java.io.FileNotFoundException;
        import java.io.IOException;
        import java.io.InputStream;

public class Show_Finding_Details extends AppCompatActivity {

    private Finding finding;
    private Uri uri;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show__finding__details);
        //get data from database for id
        DatabaseHandler db = new DatabaseHandler(this);
        Intent i = getIntent();
        getSupportActionBar().hide();
        int idValue = i.getIntExtra("id",0);
        finding = db.getFinding(idValue);
        db.close();
        //initialize ui objectcs
        TextView txtViewName = (TextView)findViewById(R.id.txtName);
        TextView txtLat = (TextView)findViewById(R.id.txtLat);
        TextView txtLong = (TextView)findViewById(R.id.txtLong);
        ImageView iv = (ImageView)findViewById(R.id.imageViewDetails);
        // set Text
        txtViewName.setText(""+finding.getName());
        txtLat.setText(""+finding.getLatitude());
        txtLong.setText(""+finding.getLongitude());
        if(finding.getPicture()!= null)
        {
            uri = Uri.parse(finding.getPicture());
            Bitmap bitmap = null;
            try
            {
                bitmap = getThumbnail(uri);
            } catch (IOException e) {
                e.printStackTrace();
            }
            iv.setImageBitmap(bitmap);
        }

    }

    public Bitmap getThumbnail(Uri uri) throws FileNotFoundException, IOException {
        InputStream input = this.getContentResolver().openInputStream(uri);

        BitmapFactory.Options onlyBoundsOptions = new BitmapFactory.Options();
        onlyBoundsOptions.inJustDecodeBounds = true;
        onlyBoundsOptions.inDither=true;//optional
        onlyBoundsOptions.inPreferredConfig=Bitmap.Config.ARGB_8888;//optional
        BitmapFactory.decodeStream(input, null, onlyBoundsOptions);
        input.close();
        if ((onlyBoundsOptions.outWidth == -1) || (onlyBoundsOptions.outHeight == -1))
            return null;

        int originalSize = (onlyBoundsOptions.outHeight > onlyBoundsOptions.outWidth) ? onlyBoundsOptions.outHeight : onlyBoundsOptions.outWidth;

        double ratio = (originalSize > 500) ? (originalSize /500) : 1.0;

        BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
        bitmapOptions.inSampleSize = getPowerOfTwoForSampleRatio(ratio);
        bitmapOptions.inDither=true;//optional
        bitmapOptions.inPreferredConfig=Bitmap.Config.ARGB_8888;//optional
        input = this.getContentResolver().openInputStream(uri);
        Bitmap bitmap = BitmapFactory.decodeStream(input, null, bitmapOptions);
        input.close();
        return bitmap;
    }

    private static int getPowerOfTwoForSampleRatio(double ratio){
        int k = Integer.highestOneBit((int)Math.floor(ratio));
        if(k==0) return 1;
        else return k;
    }


    public void open_map(View view)
    {

        open(finding.getLatitude(),finding.getLongitude(),finding.getName());

    }

    public void delete_finding(View view)
    {
        DatabaseHandler db = new DatabaseHandler(this);
        db.deleteFinding(finding);
        db.close();

        finish();
    }

    public void export_finding(View view)
    {

        Intent email = new Intent(Intent.ACTION_SEND);
        email.putExtra(Intent.EXTRA_EMAIL, new String[]{"nico.kreutzer@web.de"});
        email.putExtra(Intent.EXTRA_SUBJECT, "Objekt:" +finding.getName());
        email.putExtra(Intent.EXTRA_TEXT, "Longitude:" +finding.getLongitude()+"\n" +
                                          "Latitiude:" +finding.getLatitude()+"\n"
        );
        email.putExtra(Intent.EXTRA_STREAM, uri);
        email.setType("image/png");
        email.setType("message/rfc822");

        startActivity(Intent.createChooser(email, "Choose an Email client"));
    }



    public void open(double latitude, double longitude,String name)
    {
        String label = name;
        String uriBegin = "geo:" + latitude + "," + longitude;
        String query = latitude + "," + longitude + "(" + label + ")";
        String encodedQuery = Uri.encode(query);
        String uriString = uriBegin + "?q=" + encodedQuery + "&z=16";
        Uri uri = Uri.parse(uriString);
        Intent intent = new Intent(android.content.Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }

}
