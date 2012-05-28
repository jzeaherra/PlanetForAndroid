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

public class TypesActivity extends ListActivity {
	
    private static final int ACTIVITY_CREATE=0;
    private static final int ACTIVITY_EDIT=1;
    
    private static final int INSERT_ID = Menu.FIRST;
    private static final int DELETE_ID = Menu.FIRST + 1;
    
    
	private PlanetDbAdapter mDbHelper;
	private Cursor mTypesCursor;
	
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.types_layout);

        mDbHelper = new PlanetDbAdapter(this);
        mDbHelper.open();
        fillData();
        registerForContextMenu(getListView());
    }

    private void fillData() {
        // Get all of the rows from the database and create the item list
        mTypesCursor = mDbHelper.fetchAllTypes();
        mTypesCursor.moveToFirst();
        //if (mTypesCursor.isAfterLast()){
        startManagingCursor(mTypesCursor);

        // Create an array to specify the fields we want to display in the list (only TITLE)
        String[] from = new String[]{PlanetDbAdapter.KEY_TYPES_NAME, PlanetDbAdapter.KEY_TYPES_DESCRIPTION};

        // and an array of the fields we want to bind those fields to (in this case just text1)
        int[] to = new int[]{R.id.name, R.id.description};
        
        
        

        // Now create a simple cursor adapter and set it to display
        SimpleCursorAdapter types = new SimpleCursorAdapter(this, R.layout.types_row, mTypesCursor, from, to);
 
        
        setListAdapter(types);
    
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
                createType();
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
                mDbHelper.deleteType(info.id);
                fillData();
                return true;
        }
        return super.onContextItemSelected(item);
    }

    private void createType() {
        Intent i = new Intent(this, TypeEdit.class);
        startActivityForResult(i, ACTIVITY_CREATE);
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        Intent i = new Intent(this, TypeEdit.class);
        i.putExtra(PlanetDbAdapter.KEY_TYPES_ROWID, id);


        startActivityForResult(i, ACTIVITY_EDIT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

                fillData();
        
    }
}

