package planet.android;

import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.AdapterView.AdapterContextMenuInfo;

public class SitesActivity extends ListActivity {
	
    private static final int ACTIVITY_CREATE=0;
    private static final int ACTIVITY_EDIT=1;
    
    private static final int INSERT_ID = Menu.FIRST;
    private static final int DELETE_ID = Menu.FIRST + 1;
    
    
	private PlanetDbAdapter mDbHelper;
	private Cursor mSitesCursor;
	
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sites_layout);

        mDbHelper = new PlanetDbAdapter(this);
        mDbHelper.open();
        fillData();
        registerForContextMenu(getListView());
    }

    private void fillData() {
        // Get all of the rows from the database and create the item list
        mSitesCursor = mDbHelper.fetchAllSites();
        mSitesCursor.moveToFirst();
        //if (mSitesCursor.isAfterLast()){
        startManagingCursor(mSitesCursor);

        // Create an array to specify the fields we want to display in the list (only TITLE)
        String[] from = new String[]{PlanetDbAdapter.KEY_SITES_NAME, PlanetDbAdapter.KEY_SITES_DESCRIPTION,PlanetDbAdapter.KEY_SITES_IMAGE_URL};

        // and an array of the fields we want to bind those fields to (in this case just text1)
        int[] to = new int[]{R.id.name, R.id.description, R.id.site_photo};
        
        
        

        // Now create a simple cursor adapter and set it to display
        SimpleCursorAdapter sites = new SimpleCursorAdapter(this, R.layout.sites_row, mSitesCursor, from, to);
   
        
       
        
        // We create a ViewBinder, so that we can represent an image within our rows...
//        if (! sites.isEmpty()){
//        SimpleCursorAdapter.ViewBinder viewBinder = new SimpleCursorAdapter.ViewBinder()  {
//        	
//        	@Override
//            public abstract boolean setViewValue(View view, Cursor cursor, int columnIndex) {
//        		
//                ImageView image = (ImageView) view;
//                Bitmap bm;
//                BitmapFactory.Options options = new BitmapFactory.Options();
//          	    options.inSampleSize = 2;
//          	    try{
//          	    bm = BitmapFactory.decodeFile(cursor.getString(columnIndex), options);
//          	    image.setImageBitmap(bm);
//          	    }catch (Exception e) {
//          	    bm = BitmapFactory.decodeResource( getResources(), R.drawable.ic_launcher, options);
//          	    }
//          	    
//                return true;
//            }
//        };
//        
//        ImageView photo = (ImageView) findViewById(R.id.site_photo);
//        viewBinder.setViewValue(photo, mSitesCursor, 2);// mSitesCursor.getColumnIndex(PlanetDbAdapter.KEY_SITES_IMAGE_URL)
//        sites.setViewBinder(viewBinder);
//        }
        
        setListAdapter(sites);
    
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        menu.add(0, INSERT_ID, 0, R.string.menu_insert);
        return true;
    }

    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        switch(item.getItemId()) {
            case INSERT_ID:
                createSite();
                return true;
        }

        return super.onMenuItemSelected(featureId, item);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
            ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add(0, DELETE_ID, 0, R.string.menu_delete);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case DELETE_ID:
                AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
                mDbHelper.deleteSite(info.id);
                fillData();
                return true;
        }
        return super.onContextItemSelected(item);
    }

    private void createSite() {
        Intent i = new Intent(this, SiteEdit.class);
        startActivityForResult(i, ACTIVITY_CREATE);
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        Cursor c = mDbHelper.fetchSite( (int)id  );
        Intent i = new Intent(this, SiteEdit.class);
        i.putExtra(PlanetDbAdapter.KEY_SITES_ROWID, (int) id);
        i.putExtra(PlanetDbAdapter.KEY_SITES_NAME, c.getString( c.getColumnIndexOrThrow(PlanetDbAdapter.KEY_SITES_NAME )));
        i.putExtra(PlanetDbAdapter.KEY_SITES_DESCRIPTION, c.getString( c.getColumnIndexOrThrow(PlanetDbAdapter.KEY_SITES_DESCRIPTION )));
        i.putExtra(PlanetDbAdapter.KEY_SITES_TYPE_ID, c.getInt( c.getColumnIndexOrThrow(PlanetDbAdapter.KEY_SITES_TYPE_ID )));
        i.putExtra(PlanetDbAdapter.KEY_SITES_IMAGE_URL, c.getString( c.getColumnIndexOrThrow(PlanetDbAdapter.KEY_SITES_IMAGE_URL )));
        i.putExtra(PlanetDbAdapter.KEY_SITES_LAT, c.getLong( c.getColumnIndexOrThrow(PlanetDbAdapter.KEY_SITES_LAT )));
        i.putExtra(PlanetDbAdapter.KEY_SITES_LONG, c.getLong( c.getColumnIndexOrThrow(PlanetDbAdapter.KEY_SITES_LONG )));
        i.putExtra(PlanetDbAdapter.KEY_SITES_ZOOM, c.getLong( c.getColumnIndexOrThrow(PlanetDbAdapter.KEY_SITES_ZOOM )));

        startActivityForResult(i, ACTIVITY_EDIT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        Bundle extras = intent.getExtras();
        switch(requestCode) {
            case ACTIVITY_CREATE:
                String name = extras.getString(PlanetDbAdapter.KEY_SITES_NAME);
                String description = extras.getString(PlanetDbAdapter.KEY_SITES_DESCRIPTION);
                int type_id = extras.getInt(PlanetDbAdapter.KEY_SITES_TYPE_ID);
                String image_url = extras.getString(PlanetDbAdapter.KEY_SITES_IMAGE_URL);
                Long lat = extras.getLong(PlanetDbAdapter.KEY_SITES_LAT);
                Long longi = extras.getLong(PlanetDbAdapter.KEY_SITES_LONG);
                Long zoom = extras.getLong(PlanetDbAdapter.KEY_SITES_ZOOM);
                mDbHelper.createSite( name,  description,  type_id,  image_url, lat,  longi,  zoom);
                fillData();
                break;
            case ACTIVITY_EDIT:
                Integer rowId = extras.getInt(PlanetDbAdapter.KEY_SITES_ROWID);
                if (rowId != null) {
                    String editName = extras.getString(PlanetDbAdapter.KEY_SITES_NAME);
                    String editDescription = extras.getString(PlanetDbAdapter.KEY_SITES_DESCRIPTION);
                    int editType_id = extras.getInt(PlanetDbAdapter.KEY_SITES_TYPE_ID);
                    String editImage_url = extras.getString(PlanetDbAdapter.KEY_SITES_IMAGE_URL);
                    Long editLat = extras.getLong(PlanetDbAdapter.KEY_SITES_LAT);
                    Long editLongi = extras.getLong(PlanetDbAdapter.KEY_SITES_LONG);
                    Long editZoom = extras.getLong(PlanetDbAdapter.KEY_SITES_ZOOM);
                    mDbHelper.updateSite(rowId, editName, editDescription, editType_id,  editImage_url, editLat, editLongi, editZoom );
                }
                fillData();
                break;
        }
    }
}

