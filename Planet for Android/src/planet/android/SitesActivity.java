package planet.android;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.SimpleCursorAdapter.ViewBinder;

public class SitesActivity extends ListActivity {
	
	
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
        startManagingCursor(mSitesCursor);

        // Create an array to specify the fields we want to display in the list (only TITLE)
        String[] from = new String[]{PlanetDbAdapter.KEY_SITES_NAME, PlanetDbAdapter.KEY_SITES_DESCRIPTION,PlanetDbAdapter.KEY_SITES_IMAGE_URL};

        // and an array of the fields we want to bind those fields to (in this case just text1)
        int[] to = new int[]{R.id.name, R.id.description, R.id.img};
        
        
        

        // Now create a simple cursor adapter and set it to display
        SimpleCursorAdapter sites = new SimpleCursorAdapter(this, R.layout.sites_row, mSitesCursor, from, to);
   
        // We create a ViewBinder, so that we can represent an image within our rows...
        ViewBinder viewBinder = new ViewBinder() {

            public boolean setViewValue(View view, Cursor cursor,
                    int columnIndex) {
                ImageView image = (ImageView) view;
                BitmapFactory.Options options = new BitmapFactory.Options();
          	    options.inSampleSize = 2;
          	    Bitmap bm = BitmapFactory.decodeFile(cursor.getString(columnIndex), options);
          	    image.setImageBitmap(bm);
                return true;
            }
        };
        
        ImageView image = (ImageView) findViewById(R.id.img);
        viewBinder.setViewValue(image, mSitesCursor, mSitesCursor.getColumnIndex(PlanetDbAdapter.KEY_SITES_IMAGE_URL));
        sites.setViewBinder(viewBinder);
        
        
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
                createNote();
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
                mDbHelper.deleteNote(info.id);
                fillData();
                return true;
        }
        return super.onContextItemSelected(item);
    }

    private void createNote() {
        Intent i = new Intent(this, NoteEdit.class);
        startActivityForResult(i, ACTIVITY_CREATE);
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        Cursor c = mSitesCursor;
        c.moveToPosition(position);
        Intent i = new Intent(this, NoteEdit.class);
        i.putExtra(NotesDbAdapter.KEY_ROWID, id);
        i.putExtra(NotesDbAdapter.KEY_TITLE, c.getString(
                c.getColumnIndexOrThrow(NotesDbAdapter.KEY_TITLE)));
        i.putExtra(NotesDbAdapter.KEY_BODY, c.getString(
                c.getColumnIndexOrThrow(NotesDbAdapter.KEY_BODY)));
        startActivityForResult(i, ACTIVITY_EDIT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        Bundle extras = intent.getExtras();
        switch(requestCode) {
            case ACTIVITY_CREATE:
                String title = extras.getString(NotesDbAdapter.KEY_TITLE);
                String body = extras.getString(NotesDbAdapter.KEY_BODY);
                mDbHelper.createNote(title, body);
                fillData();
                break;
            case ACTIVITY_EDIT:
                Long rowId = extras.getLong(NotesDbAdapter.KEY_ROWID);
                if (rowId != null) {
                    String editTitle = extras.getString(NotesDbAdapter.KEY_TITLE);
                    String editBody = extras.getString(NotesDbAdapter.KEY_BODY);
                    mDbHelper.updateNote(rowId, editTitle, editBody);
                }
                fillData();
                break;
        }
    }
}
    }
}
