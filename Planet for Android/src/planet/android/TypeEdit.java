package planet.android;


import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class TypeEdit extends Activity {
    
	private EditText mNameText;
    private EditText mDescriptionText;
    
    private Long mRowId;
    
    private PlanetDbAdapter mDbHelper;
	private Cursor mTypesCursor;

	    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.type_edit_layout);
        setTitle(R.string.type_edit);
      
        mDbHelper = new PlanetDbAdapter(this);
        mDbHelper.open();
        
        mNameText = (EditText) findViewById(R.id.name);
        mDescriptionText = (EditText) findViewById(R.id.description);


        Button submitButton = (Button) findViewById(R.id.submit);

        mRowId = null;
        
        mRowId = (savedInstanceState == null) ? null :
            (Long) savedInstanceState.getSerializable(PlanetDbAdapter.KEY_TYPES_ROWID);
        if (mRowId == null) {
            Bundle extras = getIntent().getExtras();
            mRowId = extras != null ? extras.getLong(PlanetDbAdapter.KEY_TYPES_ROWID)
                                    : null;        	
        }

        
        fillData(mRowId);     	
                

        submitButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {

                setResult(RESULT_OK);
                finish();
            }

        });
        
    }
	     private void saveState() {

             String name = mNameText.getText().toString();
             String description = mDescriptionText.getText().toString();

             
         	if (mRowId == null) {
                 Long id = mDbHelper.createType(name, description);
                 if (id > 0) {
                     mRowId = id;
                 }
             } else {
                 mDbHelper.updateType(mRowId,name, description);
             }


	     }

	    
	    public void fillData(Long mRowId) {
	    	
	    	String name = null ;
	    	String description = null ;

	    	
	    	if (mRowId != null) {
	    		mTypesCursor = mDbHelper.fetchType(mRowId);
	    		mTypesCursor.moveToFirst();
	  	        startManagingCursor(mTypesCursor);

	              name = mTypesCursor.getString(mTypesCursor.getColumnIndex(PlanetDbAdapter.KEY_TYPES_NAME));
	              description = mTypesCursor.getString(mTypesCursor.getColumnIndex(PlanetDbAdapter.KEY_TYPES_DESCRIPTION));
	    	}
	      
            if (name != null) {
            	mNameText.setText(name);
            } else { mNameText.setText("Nombre");}
            if (description != null) {
            	mDescriptionText.setText(description);
            }else { mDescriptionText.setText("Descripción");}
            
        }



    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        saveState();
        outState.putSerializable(PlanetDbAdapter.KEY_TYPES_ROWID, mRowId);
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

