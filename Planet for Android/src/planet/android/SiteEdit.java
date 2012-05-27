package planet.android;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class SiteEdit extends Activity {
	
	
    private EditText mNameText;
    private EditText mDescriptionText;
    private EditText mTypeIdText;
    private EditText mImageUrlText;
    private EditText mLatText;
    private EditText mLongiText;
    private EditText mZoomText;
    
    private Integer mRowId;
    
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.site_edit_layout);
        setTitle(R.string.site_edit);

        mNameText = (EditText) findViewById(R.id.name);
        mDescriptionText = (EditText) findViewById(R.id.description);
        mTypeIdText = (EditText) findViewById(R.id.type_id);
        mImageUrlText = (EditText) findViewById(R.id.image_url);
        mLatText = (EditText) findViewById(R.id.lat);
        mLongiText = (EditText) findViewById(R.id.longi);
        mZoomText = (EditText) findViewById(R.id.zoom);

        Button submitButton = (Button) findViewById(R.id.submit);

        mRowId = (Integer) null;
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            mRowId = extras.getInt(PlanetDbAdapter.KEY_SITES_ROWID);
            String name = extras.getString(PlanetDbAdapter.KEY_SITES_NAME);
            String description = extras.getString(PlanetDbAdapter.KEY_SITES_DESCRIPTION);
            Integer type_id = extras.getInt(PlanetDbAdapter.KEY_SITES_TYPE_ID);
            String image_url = extras.getString(PlanetDbAdapter.KEY_SITES_IMAGE_URL);
            Long lat = extras.getLong(PlanetDbAdapter.KEY_SITES_LAT);
            Long longi = extras.getLong(PlanetDbAdapter.KEY_SITES_LONG);
            Long zoom = extras.getLong(PlanetDbAdapter.KEY_SITES_ZOOM);

            if (name != null) {
            	mNameText.setText(name);
            } else { mNameText.setText("Nombre");}
            if (description != null) {
            	mDescriptionText.setText(description);
            }else { mDescriptionText.setText("Descripción");}
            if (type_id != null) {
            	mTypeIdText.setText((type_id.toString()));
            }else { mTypeIdText.setText("Tipo");}
            if (image_url != null) {
            	mImageUrlText.setText(image_url);
            }else { mImageUrlText.setText("Fotografía");}
            if (lat != null) {
            	mLatText.setText(lat.toString());
            }else { mLatText.setText("Latitud");}
            if (longi != null) {
            	mLongiText.setText(longi.toString());
            }else { mLongiText.setText("Longitud");}
            if (zoom != null) {
            	mZoomText.setText(zoom.toString());
            }else { mZoomText.setText("Zoom");}
            
        }else{
        	mNameText.setText("Nombre");
        	mDescriptionText.setText("Descripción");
        	mTypeIdText.setText("Tipo");
        	mImageUrlText.setText("Fotografía");
        	mLatText.setText("Latitud");
        	mLongiText.setText("Longitud");
        	mZoomText.setText("Zoom");
        }

        submitButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                Bundle bundle = new Bundle();

                bundle.putString(PlanetDbAdapter.KEY_SITES_NAME, mNameText.getText().toString());
                bundle.putString(PlanetDbAdapter.KEY_SITES_DESCRIPTION, mDescriptionText.getText().toString());
                bundle.putInt(PlanetDbAdapter.KEY_SITES_TYPE_ID, Integer.valueOf(mTypeIdText.getText().toString()));
                bundle.putString(PlanetDbAdapter.KEY_SITES_IMAGE_URL, mImageUrlText.getText().toString());
                bundle.putLong(PlanetDbAdapter.KEY_SITES_LAT, Long.valueOf(mLatText.getText().toString()));
                bundle.putLong(PlanetDbAdapter.KEY_SITES_LONG, Long.valueOf(mLongiText.getText().toString()));
                bundle.putLong(PlanetDbAdapter.KEY_SITES_ZOOM, Long.valueOf(mZoomText.getText().toString()));

                if (mRowId != null) {
                    bundle.putInt(PlanetDbAdapter.KEY_SITES_ROWID, mRowId);
                }

                Intent mIntent = new Intent();
                mIntent.putExtras(bundle);
                setResult(RESULT_OK, mIntent);
                finish();
            }

        });
    }
//    private Uri imageUri;
//
//    public void takePhoto(View view) {
//        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
//        File photo = new File(Environment.getExternalStorageDirectory(),  "Pic.jpg");
//        intent.putExtra(MediaStore.EXTRA_OUTPUT,
//                Uri.fromFile(photo));
//        imageUri = Uri.fromFile(photo);
//        startActivityForResult(intent, TAKE_PICTURE);
//    }
//
//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        switch (requestCode) {
//        case TAKE_PICTURE:
//            if (resultCode == Activity.RESULT_OK) {
//                Uri selectedImage = imageUri;
//                getContentResolver().notifyChange(selectedImage, null);
//                ImageView imageView = (ImageView) findViewById(R.id.ImageView);
//                ContentResolver cr = getContentResolver();
//                Bitmap bitmap;
//                try {
//                     bitmap = android.provider.MediaStore.Images.Media
//                     .getBitmap(cr, selectedImage);
//
//                    imageView.setImageBitmap(bitmap);
//                    Toast.makeText(this, selectedImage.toString(),
//                            Toast.LENGTH_LONG).show();
//                } catch (Exception e) {
//                    Toast.makeText(this, "Failed to load", Toast.LENGTH_SHORT)
//                            .show();
//                    Log.e("Camera", e.toString());
//                }
//            }
//        }
//    }
}

