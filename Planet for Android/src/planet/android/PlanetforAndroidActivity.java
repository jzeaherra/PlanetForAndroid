package planet.android;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;

public class PlanetforAndroidActivity extends TabActivity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        
        TabHost tabHost = getTabHost();
        
        // Tab for Types
        TabSpec typespec = tabHost.newTabSpec("Types");
        // setting Title and Icon for the Tab
        typespec.setIndicator("Types", getResources().getDrawable(R.drawable.icon_types_tab));
        Intent photosIntent = new Intent(this, TypesActivity.class);
        typespec.setContent(photosIntent);
 
        // Tab for Sites
        TabSpec sitespec = tabHost.newTabSpec("Sites");
        sitespec.setIndicator("Sites", getResources().getDrawable(R.drawable.icon_sites_tab));
        Intent songsIntent = new Intent(this, SitesActivity.class);
        sitespec.setContent(songsIntent);
 
        // Tab for Trips
        TabSpec tripspec = tabHost.newTabSpec("Trips");
        tripspec.setIndicator("Trips", getResources().getDrawable(R.drawable.icon_trips_tab));
        Intent videosIntent = new Intent(this, TripsActivity.class);
        tripspec.setContent(videosIntent);
 
        // Adding all TabSpec to TabHost
        tabHost.addTab(typespec); // Add tab Types
        tabHost.addTab(sitespec); // Add tab Sites
        tabHost.addTab(tripspec); // Add tab Trips
    }
}