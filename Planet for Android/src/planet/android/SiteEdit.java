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
            String created_at = extras.getString(PlanetDbAdapter.KEY_SITES_CREATED_AT);
            String updated_at = extras.getString(PlanetDbAdapter.KEY_SITES_UPDATED_AT);
            String lat = extras.getString(PlanetDbAdapter.KEY_SITES_LAT);
            String longi = extras.getString(PlanetDbAdapter.KEY_SITES_LONG);
            String zoom = extras.getString(PlanetDbAdapter.KEY_SITES_ZOOM);
            String last_sync = extras.getString(PlanetDbAdapter.KEY_SITES_LAST_SYNC);

            if (title != null) {
                mTitleText.setText(title);
            }
            if (body != null) {
                mBodyText.setText(body);
            }
        }

        confirmButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                Bundle bundle = new Bundle();

                bundle.putString(NotesDbAdapter.KEY_TITLE, mTitleText.getText().toString());
                bundle.putString(NotesDbAdapter.KEY_BODY, mBodyText.getText().toString());
                if (mRowId != null) {
                    bundle.putLong(NotesDbAdapter.KEY_ROWID, mRowId);
                }

                Intent mIntent = new Intent();
                mIntent.putExtras(bundle);
                setResult(RESULT_OK, mIntent);
                finish();
            }

        });
    }
}
}
