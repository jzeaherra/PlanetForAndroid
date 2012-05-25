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
    
    private Long mRowId;
    
    
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

        mRowId = null;
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            mRowId = extras.getLong(PlanetDbAdapter.KEY_SITES_ROWID);
            String name = extras.getString(PlanetDbAdapter.KEY_SITES_NAME);
            String description = extras.getString(PlanetDbAdapter.KEY_SITES_DESCRIPTION);
            String type_id = extras.getString(PlanetDbAdapter.KEY_SITES_TYPE_ID);
            String image_url = extras.getString(PlanetDbAdapter.KEY_SITES_IMAGE_URL);
            Long lat = extras.getLong(PlanetDbAdapter.KEY_SITES_LAT);
            Long longi = extras.getLong(PlanetDbAdapter.KEY_SITES_LONG);
            Long zoom = extras.getLong(PlanetDbAdapter.KEY_SITES_ZOOM);

            if (name != null) {
            	mNameText.setText(name);
            }
            if (description != null) {
            	mDescriptionText.setText(description);
            }
            if (type_id != null) {
            	mTypeIdText.setText(type_id);
            }
            if (image_url != null) {
            	mImageUrlText.setText(image_url);
            }
            if (lat != null) {
            	mLatText.setText(lat.toString());
            }
            if (longi != null) {
            	mLongiText.setText(longi.toString());
            }
            if (zoom != null) {
            	mZoomText.setText(zoom.toString());
            }
            
        }

        submitButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                Bundle bundle = new Bundle();

                bundle.putString(PlanetDbAdapter.KEY_SITES_NAME, mNameText.getText().toString());
                bundle.putString(PlanetDbAdapter.KEY_SITES_DESCRIPTION, mDescriptionText.getText().toString());
                bundle.putString(PlanetDbAdapter.KEY_SITES_TYPE_ID, mTypeIdText.getText().toString());
                bundle.putString(PlanetDbAdapter.KEY_SITES_IMAGE_URL, mImageUrlText.getText().toString());
                bundle.putLong(PlanetDbAdapter.KEY_SITES_LAT, Long.valueOf(mLatText.getText().toString()));
                bundle.putLong(PlanetDbAdapter.KEY_SITES_LONG, Long.valueOf(mLongiText.getText().toString()));
                bundle.putLong(PlanetDbAdapter.KEY_SITES_ZOOM, Long.valueOf(mZoomText.getText().toString()));

                if (mRowId != null) {
                    bundle.putLong(PlanetDbAdapter.KEY_SITES_ROWID, mRowId);
                }

                Intent mIntent = new Intent();
                mIntent.putExtras(bundle);
                setResult(RESULT_OK, mIntent);
                finish();
            }

        });
    }
}

