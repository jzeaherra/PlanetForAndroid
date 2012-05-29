package planet.android;

import java.io.File;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;

public class SiteEdit extends Activity {
	
	
    private static final int TAKE_PICTURE = 0;
    
    private String myImagePath;
    private Integer myTypeId;
    
	private EditText mNameText;
    private EditText mDescriptionText;
//    private EditText mTypeIdText;
    private Spinner mTypeSpinner;
//    private EditText mImageUrlText;
    private ImageView mSiteImage;
    private EditText mLatText;
    private EditText mLongiText;
    private EditText mZoomText;
    
    private Long mRowId;
    
    private PlanetDbAdapter mDbHelper;
	private Cursor mSitesCursor;
	private Cursor mTypesCursor;
	SimpleCursorAdapter adapter;
	
	    @Override
	    protected void onCreate(Bundle savedInstanceState) {
	    	super.onCreate(savedInstanceState);
	    	setContentView(R.layout.site_edit_layout);
	    	setTitle(R.string.site_edit);

	    	mDbHelper = new PlanetDbAdapter(this);
	    	mDbHelper.open();

	    	mNameText = (EditText) findViewById(R.id.name);
	    	mDescriptionText = (EditText) findViewById(R.id.description);
//	    	mTypeIdText = (EditText) findViewById(R.id.type_id);
	    	mTypeSpinner = (Spinner) findViewById(R.id.type_id_spinner);
//	    	mImageUrlText = (EditText) findViewById(R.id.image_url);
	    	mSiteImage = (ImageView) findViewById(R.id.siteImage);
	    	mLatText = (EditText) findViewById(R.id.lat);
	    	mLongiText = (EditText) findViewById(R.id.longi);
	    	mZoomText = (EditText) findViewById(R.id.zoom);

	    	Button submitButton = (Button) findViewById(R.id.submit);

	    	mRowId = null;

	    	mRowId = (savedInstanceState == null) ? null :
	    		(Long) savedInstanceState.getSerializable(PlanetDbAdapter.KEY_SITES_ROWID);
	    	if (mRowId == null) {
	    		Bundle extras = getIntent().getExtras();
	    		mRowId = extras != null ? extras.getLong(PlanetDbAdapter.KEY_SITES_ROWID)
	    				: null;        	
	    	}

	    	myImagePath = (savedInstanceState == null) ? null :
	    		(String) savedInstanceState.getSerializable("newImagePath");

	    	fillData(mRowId);     	


	    	submitButton.setOnClickListener(new View.OnClickListener() {
	    		public void onClick(View view) {
	    			setResult(RESULT_OK);
	    			finish();
	    		}
	    	});

	    	mSiteImage.setOnClickListener(new View.OnClickListener() {
	    		public void onClick(View v) {
	    			takePhoto();
	    		}
	    	});


	    	
	    	mTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
	    		
	    	    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
	    	    	
	    	    	mTypesCursor.moveToPosition(i);
	    	    	Integer id = mTypesCursor.getInt(mTypesCursor.getColumnIndex(PlanetDbAdapter.KEY_TYPES_ROWID));
//	    	    	mTypeIdText.setText(id.toString()); 
	    	    	myTypeId = id;

	    	    } 

	    	    public void onNothingSelected(AdapterView<?> adapterView) {
	    	        return;
	    	    } 
	    	}); 

    }
	    
	    private void saveState() {

	    	String name = mNameText.getText().toString();
	    	String description = mDescriptionText.getText().toString();
//	    	Integer typeId = Integer.valueOf(mTypeIdText.getText().toString());
	    	Integer typeId = myTypeId;
//	    	String imageUrl = mImageUrlText.getText().toString();
	    	String imageUrl = myImagePath;
	    	Long lat = Long.valueOf(mLatText.getText().toString());
	    	Long longi = Long.valueOf(mLongiText.getText().toString());
	    	Long zoom = Long.valueOf(mZoomText.getText().toString());

	    	if (mRowId == null) {
	    		Long id = mDbHelper.createSite(name, description, typeId, imageUrl, lat, longi, zoom);
	    		if (id > 0) {
	    			mRowId = id;
	    		}
	    	} else {
	    		mDbHelper.updateSite(mRowId,name, description, typeId, imageUrl, lat, longi, zoom);
	    	}

	    }

	    
	    public void fillData(Long mRowId) {
	    	
	    	String name = null ;
	    	String description = null ;
	    	Integer type_id = null ;
	    	String image_url = null ;
	    	Long lat = null ;
	    	Long longi = null ;
	    	Long zoom = null ;
	    	
	    	
	    	if (mRowId != null) {
	    		mSitesCursor = mDbHelper.fetchSite(mRowId);
	    		mSitesCursor.moveToFirst();
	  	        startManagingCursor(mSitesCursor);

	              name = mSitesCursor.getString(mSitesCursor.getColumnIndex(PlanetDbAdapter.KEY_SITES_NAME));
	              description = mSitesCursor.getString(mSitesCursor.getColumnIndex(PlanetDbAdapter.KEY_SITES_DESCRIPTION));
	              type_id = mSitesCursor.getInt(mSitesCursor.getColumnIndex(PlanetDbAdapter.KEY_SITES_TYPE_ID));
	              image_url = mSitesCursor.getString(mSitesCursor.getColumnIndex(PlanetDbAdapter.KEY_SITES_IMAGE_URL));
	              lat = mSitesCursor.getLong(mSitesCursor.getColumnIndex(PlanetDbAdapter.KEY_SITES_LAT));
	              longi = mSitesCursor.getLong(mSitesCursor.getColumnIndex(PlanetDbAdapter.KEY_SITES_LONG));
	              zoom = mSitesCursor.getLong(mSitesCursor.getColumnIndex(PlanetDbAdapter.KEY_SITES_ZOOM));
	    	}
	      
	    	if (myImagePath != null){ image_url = myImagePath;}
	    	
            if (name != null) {
            	mNameText.setText(name);
            } else { mNameText.setText("Nombre");}
            if (description != null) {
            	mDescriptionText.setText(description);
            }else { mDescriptionText.setText("Descripci�n");}
            if (type_id != null) {
//            	mTypeIdText.setText((type_id.toString()));
            	myTypeId = type_id;
//            }else { mTypeIdText.setText("Tipo");}
            }
            if (image_url != null) {
//            	mImageUrlText.setText(image_url);
            	myImagePath = image_url;
            	File turu = new File(image_url);
            	if ( turu.exists() ) {
               		Uri tururu = Uri.fromFile(turu);
               		mSiteImage.setImageURI(tururu);
               		}
            		else {mSiteImage.setImageResource( R.drawable.ic_launcher );}    
//            }else { mImageUrlText.setText("Fotograf�a");}
            }
            if (lat != null) {
            	mLatText.setText(lat.toString());
            }else { mLatText.setText("0");}
            if (longi != null) {
            	mLongiText.setText(longi.toString());
            }else { mLongiText.setText("0");}
            if (zoom != null) {
            	mZoomText.setText(zoom.toString());
            }else { mZoomText.setText("0");}
            
//		We fill up the spinner....
            mTypesCursor=mDbHelper.fetchAllTypes();
            mTypesCursor.moveToFirst();
            startManagingCursor(mTypesCursor);
            
            String[] from = new String[]{PlanetDbAdapter.KEY_TYPES_NAME};
            int[] to = new int[]{android.R.id.text1};
            adapter = new SimpleCursorAdapter(this, android.R.layout.simple_spinner_item, mTypesCursor, from, to );
            adapter.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item);
            mTypeSpinner.setAdapter(adapter);
            
//		And try to select the previously selected item in the database.
            // We don't do anything if we can't, so the first item is preselected.
            if (type_id != null){
            	while (true){
            		if (type_id == mTypesCursor.getInt(mTypesCursor.getColumnIndex(PlanetDbAdapter.KEY_TYPES_ROWID))){
                		mTypeSpinner.setSelection( mTypesCursor.getPosition());
            		}
            	if (!mTypesCursor.moveToNext()){break;}
            	}
            }
        }


    public void takePhoto(){

		Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
		long captureTime = System.currentTimeMillis();
//        String photoPath = Environment.getExternalStorageDirectory() + "/DCIM/Camera/Planet" + captureTime + ".jpg";
        String photoPath = "/sdcard/DCIM/Camera/Planet" + captureTime + ".jpg";
        File photo = new File( photoPath);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photo));
        myImagePath = photoPath; 
        startActivityForResult(intent, TAKE_PICTURE);
    }
    
//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
////        switch (requestCode) {
////        case TAKE_PICTURE:
////            if (resultCode == Activity.RESULT_OK) {
////        		Uri tururu = Uri.parse(mImageUrlText.getText().toString());
////        		if (new File(tururu.toString()).exists() ) {mSiteImage.setImageURI(tururu);}
////        		else {mSiteImage.setImageResource( R.drawable.ic_launcher );}    
////            }
////        }
//    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        saveState();
        outState.putSerializable(PlanetDbAdapter.KEY_SITES_ROWID, mRowId);
        outState.putSerializable("newImagePath", myImagePath);
    }
    @Override
    protected void onPause() {
        super.onPause();
        saveState();
    }
    @Override
    protected void onResume() {
        super.onResume();
        fillData(mRowId);
    }
}

