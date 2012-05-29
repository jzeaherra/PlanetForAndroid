package planet.android;

	import android.content.ContentValues;
	import android.content.Context;
	import android.database.Cursor;
	import android.database.SQLException;
	import android.database.sqlite.SQLiteDatabase;
	import android.database.sqlite.SQLiteOpenHelper;
	import android.util.Log;

	/**
	 * Simple notes database access helper class. Defines the basic CRUD operations
	 * for the notepad example, and gives the ability to list all notes as well as
	 * retrieve or modify a specific note.
	 * 
	 * This has been improved from the first version of this tutorial through the
	 * addition of better error handling and also using returning a Cursor instead
	 * of using a collection of inner classes (which is less scalable and not
	 * recommended).
	 */
	public class PlanetDbAdapter {

		
//		Tabla:  "sites"
//		    string   "name"
//		    text     "description"
//		    integer  "type_id"
//		    string   "image_url"
//		    datetime "created_at",                       
//		    datetime "updated_at",                        
//		    integer  "user_id"
//		    string   "image_file_name"
//		    string   "image_content_type"
//		    string   "image_file_size"
//		    datetime "image_updated_at"
//		    float    "lat"
//		    float    "long"
//		    float    "zoom"
//		    datetime "last_sync"
//
//		Tabla: "trips"
//		    string   "name"
//		    text     "description"
//		    date     "date"
//		    integer  "user_id"
//		    datetime "created_at",  
//		    datetime "updated_at", 
//		    datetime "last_sync"
//
//		Tabla: "types"
//		    string   "name"
//		    text     "description"
//		    datetime "created_at", 
//		    datetime "updated_at",
//		    datetime "last_sync" 
		

		// TABLE SITES
	    public static final String KEY_SITES_ROWID = "_id";
	    public static final String KEY_SITES_NAME = "name";
	    public static final String KEY_SITES_DESCRIPTION = "description";
	    public static final String KEY_SITES_TYPE_ID = "type_id";
	    public static final String KEY_SITES_IMAGE_URL = "image_url";
	    public static final String KEY_SITES_CREATED_AT = "created_at";                       
	    public static final String KEY_SITES_UPDATED_AT =  "updated_at";                        
	    public static final String KEY_SITES_LAT =  "lat";
	    public static final String KEY_SITES_LONG =  "long";
	    public static final String KEY_SITES_ZOOM =  "zoom";
	    public static final String KEY_SITES_LAST_SYNC = "last_sync";

	    //TABLE TRIPS
//	    string   "name"
//	    text     "description"
//	    date     "date"
//	    integer  "user_id"
//	    datetime "created_at",  
//	    datetime "updated_at", 
//	    datetime "last_sync"

	    //TABLE TYPES
	    public static final String KEY_TYPES_ROWID = "_id";
	    public static final String KEY_TYPES_NAME = "name";
	    public static final String KEY_TYPES_DESCRIPTION =  "description";
	    public static final String KEY_TYPES_CREATED_AT = "created_at"; 
	    public static final String KEY_TYPES_UPDATED_AT = "updated_at";
	    public static final String KEY_TYPES_LAST_SYNC =  "last_sync";
	    
	    private static final String TAG = "NotesDbAdapter";
	    private DatabaseHelper mDbHelper;
	    private SQLiteDatabase mDb;

	    
	    private static final String DATABASE_NAME = "planet";
	    private static final String DATABASE_TABLE_TYPES = "types";
	    private static final String DATABASE_TABLE_SITES = "sites";

	    private static final int DATABASE_VERSION = 3;
	    /**
	     * Database creation sql statement
	     */
	    private static final String DATABASE_CREATE_SITES =
	        "create table " + DATABASE_TABLE_SITES + "(" +
	        		KEY_SITES_ROWID + " integer primary key autoincrement, "+
	        		KEY_SITES_NAME + " text not null, "+
	        		KEY_SITES_DESCRIPTION + " text not null, "+
	        		KEY_SITES_TYPE_ID + " integer not null, "+
	        		KEY_SITES_IMAGE_URL + " text, "+
	        		KEY_SITES_CREATED_AT + " text not null, "+
	        		KEY_SITES_UPDATED_AT + " text not null, "+
	        		KEY_SITES_LAT + " real not null, "+
	        		KEY_SITES_LONG + " real not null, "+
	        		KEY_SITES_ZOOM + " real not null, "+
	        		KEY_SITES_LAST_SYNC + " text not null "+
	        		");";
	    private static final String DATABASE_CREATE_TYPES=
	    	"create table " + DATABASE_TABLE_TYPES + "(" +
	        		KEY_TYPES_ROWID + " integer primary key autoincrement, "+
	        		KEY_TYPES_NAME + " text not null, "+
	        		KEY_TYPES_DESCRIPTION + " text not null, "+
	        		KEY_TYPES_CREATED_AT + " real not null, "+
	        		KEY_TYPES_UPDATED_AT + " text not null, "+
	        		KEY_TYPES_LAST_SYNC + " text not null "+
	        		");";



	    private final Context mCtx;

	    private static class DatabaseHelper extends SQLiteOpenHelper {

	        DatabaseHelper(Context context) {
	            super(context, DATABASE_NAME, null, DATABASE_VERSION);
	        }

	        @Override
	        public void onCreate(SQLiteDatabase db) {

	            db.execSQL(DATABASE_CREATE_SITES);
	            db.execSQL(DATABASE_CREATE_TYPES);
	        }

	        @Override
	        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	            Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
	                    + newVersion + ", which will destroy all old data");
	            db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE_SITES);
	            db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE_TYPES);
	            onCreate(db);
	        }
	    }

	    /**
	     * Constructor - takes the context to allow the database to be
	     * opened/created
	     * 
	     * @param ctx the Context within which to work
	     */
	    public PlanetDbAdapter(Context ctx) {
	        this.mCtx = ctx;
	    }

	    /**
	     * Open the notes database. If it cannot be opened, try to create a new
	     * instance of the database. If it cannot be created, throw an exception to
	     * signal the failure
	     * 
	     * @return this (self reference, allowing this to be chained in an
	     *         initialization call)
	     * @throws SQLException if the database could be neither opened or created
	     */
	    public PlanetDbAdapter open() throws SQLException {
	        mDbHelper = new DatabaseHelper(mCtx);
	        mDb = mDbHelper.getWritableDatabase();
	        return this;
	    }

	    public void close() {
	        mDbHelper.close();
	    }


	    /**
	     * Create a new site or type
	     * @return rowId or -1 if failed
	     */

	    public Long createSite(String name, String description, int type_id, String image_url, long lat, long longi, long zoom) {
	        ContentValues initialValues = new ContentValues();
	        initialValues.put(KEY_SITES_NAME, name);
	        initialValues.put(KEY_SITES_DESCRIPTION, description);
	        initialValues.put(KEY_SITES_TYPE_ID, type_id);
	        initialValues.put(KEY_SITES_IMAGE_URL, image_url);
	        initialValues.put(KEY_SITES_CREATED_AT, "0");   // To fix..
	        initialValues.put(KEY_SITES_UPDATED_AT, "0");   // To fix..
	        initialValues.put(KEY_SITES_LAT, lat);
	        initialValues.put(KEY_SITES_LONG, longi);
	        initialValues.put(KEY_SITES_ZOOM, zoom);
	        initialValues.put(KEY_SITES_LAST_SYNC, "0");// To fix..

	        return mDb.insert(DATABASE_TABLE_SITES, null, initialValues);
	    }

	    public long createType(String name, String description) {
	        ContentValues initialValues = new ContentValues();
	        initialValues.put(KEY_TYPES_NAME, name);
	        initialValues.put(KEY_TYPES_DESCRIPTION, description);
	        initialValues.put(KEY_TYPES_CREATED_AT, "0");// To fix..
	        initialValues.put(KEY_TYPES_UPDATED_AT, "0");// To fix..
	        initialValues.put(KEY_TYPES_LAST_SYNC, "0");// To fix..

	        return mDb.insert(DATABASE_TABLE_TYPES, null, initialValues);
	    }

	    /**
	     * Delete the site or type with the given rowId
	     * 
	     * @param rowId id of note to delete
	     * @return true if deleted, false otherwise
	     */
	    public boolean deleteSite(long rowId) {

	        return mDb.delete(DATABASE_TABLE_SITES, KEY_SITES_ROWID + "=" + rowId, null) > 0;
	    }

	    public boolean deleteType(long rowId) {

	        return mDb.delete(DATABASE_TABLE_TYPES, KEY_TYPES_ROWID + "=" + rowId, null) > 0;
	    }

	    /**
	     * Return a Cursor over the list of all Sites / Types
	     * 
	     * @return Cursor over all notes
	     */
	    public Cursor fetchAllSites() {

	        return mDb.query(DATABASE_TABLE_SITES, new String[] {KEY_SITES_ROWID, KEY_SITES_NAME,
	                KEY_SITES_DESCRIPTION, KEY_SITES_IMAGE_URL}, null, null, null, null, null);
	    }
	    public Cursor fetchAllTypes() {

	        return mDb.query(DATABASE_TABLE_TYPES, new String[] {KEY_TYPES_ROWID, KEY_TYPES_NAME,
	                KEY_TYPES_DESCRIPTION}, null, null, null, null, null);
	    }
	    
	    /**
	     * Return a Cursor positioned at the Site / Type that matches the given rowId
	     * 
	     * @param rowId id of note to retrieve
	     * @return Cursor positioned to matching note, if found
	     * @throws SQLException if note could not be found/retrieved
	     */

	    public Cursor fetchSite(long rowId) throws SQLException {

	        Cursor mCursor =

	            mDb.query(true, DATABASE_TABLE_SITES, new String[] {KEY_SITES_ROWID,KEY_SITES_NAME,KEY_SITES_DESCRIPTION,KEY_SITES_TYPE_ID,KEY_SITES_IMAGE_URL,
	            		KEY_SITES_CREATED_AT, KEY_SITES_UPDATED_AT,KEY_SITES_LAT,KEY_SITES_LONG,KEY_SITES_ZOOM,KEY_SITES_LAST_SYNC} , KEY_SITES_ROWID + "=" + rowId, null,
	                    null, null, null, null);
	        if (mCursor != null) {
	            mCursor.moveToFirst();
	        }
	        return mCursor;

	    }
	    public Cursor fetchType(long rowId) throws SQLException {

	        Cursor mCursor =

	            mDb.query(true, DATABASE_TABLE_TYPES, new String[] {KEY_TYPES_ROWID,KEY_TYPES_NAME,KEY_TYPES_DESCRIPTION,KEY_TYPES_CREATED_AT,KEY_TYPES_UPDATED_AT,
	            		KEY_TYPES_LAST_SYNC} , KEY_TYPES_ROWID + "=" + rowId, null,
	                    null, null, null, null);
	        if (mCursor != null) {
	            mCursor.moveToFirst();
	        }
	        return mCursor;
	    }
	    /**
	     * Update the site / type using the details provided. The note to be updated is
	     * specified using the rowId.

	     * @return true if the site / type was successfully updated, false otherwise
	     */

	    public boolean updateSite(Long rowId, String name, String description, int type_id, String image_url, long lat, long longi, long zoom) {
	        ContentValues args = new ContentValues();
	        args.put(KEY_SITES_NAME, name);
	        args.put(KEY_SITES_DESCRIPTION, description);
	        args.put(KEY_SITES_TYPE_ID, type_id);
	        args.put(KEY_SITES_IMAGE_URL, image_url);
	        args.put(KEY_SITES_CREATED_AT, "0");   // To fix..
	        args.put(KEY_SITES_UPDATED_AT, "0");   // To fix..
	        args.put(KEY_SITES_LAT, lat);
	        args.put(KEY_SITES_LONG, longi);
	        args.put(KEY_SITES_ZOOM, zoom);
	        args.put(KEY_SITES_LAST_SYNC, "0");   // To fix..
	        
	        return mDb.update(DATABASE_TABLE_SITES, args, KEY_SITES_ROWID + "=" + rowId, null) > 0;
	    }
	    public boolean updateType(Long rowId, String name, String description) {
	        ContentValues args = new ContentValues();
	        args.put(KEY_TYPES_NAME, name);
	        args.put(KEY_TYPES_DESCRIPTION, description);
	        args.put(KEY_TYPES_CREATED_AT, "0");   // To fix..
	        args.put(KEY_TYPES_UPDATED_AT, "0");   // To fix..
	        args.put(KEY_TYPES_LAST_SYNC, "0");   // To fix..
	        
	        return mDb.update(DATABASE_TABLE_TYPES, args, KEY_TYPES_ROWID + "=" + rowId, null) > 0;
	    }
}


