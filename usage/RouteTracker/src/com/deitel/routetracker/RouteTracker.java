// RouteTracker.java
// Main MapActivity for the RouteTracker app.
package com.deitel.routetracker;

import android.app.AlertDialog;
import android.content.Context;
import android.location.Criteria;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.PowerManager;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.FrameLayout;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;

public class RouteTracker extends MapActivity 
{
   private LocationManager locationManager; // gives location data
   private MapView mapView; // displays a Google map
   private MapController mapController; // manages map pan/zoom
   private Location previousLocation; // previous reported location
   private RouteOverlay routeOverlay; // Overlay that shows route on map
   private long distanceTraveled; // total distance the user traveled
   private BearingFrameLayout bearingFrameLayout; // rotates the MapView
   private boolean tracking; // whether app is currently tracking
   private long startTime; // time (in milliseconds) when tracking starts
   private PowerManager.WakeLock wakeLock; // used to prevent device sleep
   private boolean gpsFix; // whether we have a GPS fix for accurate data
   
   private static final double MILLISECONDS_PER_HOUR = 1000 * 60 * 60;
   private static final double MILES_PER_KILOMETER = 0.621371192;
   private static final int MAP_ZOOM = 18; // Google Maps supports 1-21
   
   // Called when the activity is first created
   @Override
   public void onCreate(Bundle savedInstanceState) 
   {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.main);

      // create new MapView using your Google Maps API key
      bearingFrameLayout = new BearingFrameLayout(this, 
         getResources().getString(R.string.google_maps_api_key));
      
      // add bearingFrameLayout to mainLayout
      FrameLayout mainLayout = 
         (FrameLayout) findViewById(R.id.mainLayout);
      mainLayout.addView(bearingFrameLayout, 0);
   
      // get the MapView and MapController
      mapView = bearingFrameLayout.getMapview();
      mapController = mapView.getController(); // get MapController
      mapController.setZoom(MAP_ZOOM); // zoom in the map

      // create map Overlay
      routeOverlay = new RouteOverlay(); 

      // add the RouteOverlay overlay
      mapView.getOverlays().add(routeOverlay);

      distanceTraveled = 0; // initialize distanceTraveled to 0

      // register listener for trackingToggleButton
      ToggleButton trackingToggleButton = 
         (ToggleButton) findViewById(R.id.trackingToggleButton);
      trackingToggleButton.setOnCheckedChangeListener(
         trackingToggleButtonListener);
   } // end onCreate
   
   // called when Activity becoming visible to the user
   @Override
   public void onStart() 
   {
      super.onStart(); // call super's onStart method
      
      // create Criteria object to specify location provider's settings
      Criteria criteria = new Criteria(); 
      criteria.setAccuracy(Criteria.ACCURACY_FINE); // fine location data
      criteria.setBearingRequired(true); // need bearing to rotate map
      criteria.setCostAllowed(true); // OK to incur monetary cost
      criteria.setPowerRequirement(Criteria.POWER_LOW); // try to conserve
      criteria.setAltitudeRequired(false); // don't need altitude data

      // get the LocationManager
      locationManager = 
         (LocationManager) getSystemService(LOCATION_SERVICE);
      
      // register listener to determine whether we have a GPS fix
      locationManager.addGpsStatusListener(gpsStatusListener);
      
      // get the best provider based on our Criteria
      String provider = locationManager.getBestProvider(criteria, true);

      // listen for changes in location as often as possible
      locationManager.requestLocationUpdates(provider, 0, 0,
          locationListener);
     
      // get the app's power manager
      PowerManager powerManager = 
         (PowerManager) getSystemService(Context.POWER_SERVICE);
      
      // get a wakelock preventing the device from sleeping
      wakeLock = powerManager.newWakeLock(
         PowerManager.PARTIAL_WAKE_LOCK, "No sleep");
      wakeLock.acquire(); // acquire the wake lock

      bearingFrameLayout.invalidate(); // redraw the BearingFrameLayout
   } // end method onStart
   
   // called when Activity is no longer visible to the user
   @Override
   public void onStop()
   {
      super.onStop(); // call the super method
      wakeLock.release(); // release the wakelock
   } // end method onStop

   // update location on map   
   public void updateLocation(Location location) 
   {
      if (location != null && gpsFix) // location not null; have GPS fix
      {
         // add the given Location to the route
         routeOverlay.addPoint(location); 

         // if there is a previous location
         if (previousLocation != null) 
         {
            // add to the total distanceTraveled
            distanceTraveled += location.distanceTo(previousLocation);
         } // end if

         // get the latitude and longitude
         Double latitude = location.getLatitude() * 1E6;
         Double longitude = location.getLongitude() * 1E6;

         // create GeoPoint representing the given Locations
         GeoPoint point = 
            new GeoPoint(latitude.intValue(), longitude.intValue());

         // move the map to the current location
         mapController.animateTo(point);

         // update the compass bearing
         bearingFrameLayout.setBearing(location.getBearing());
         bearingFrameLayout.invalidate(); // redraw based on bearing
      } // end if
      
      previousLocation = location;
   } // end method updateLocation

   // responds to events from the LocationManager
   private final LocationListener locationListener = 
      new LocationListener() 
   {
      // when the location is changed
      public void onLocationChanged(Location location) 
      {
         gpsFix = true; // if getting Locations, then we have a GPS fix
         
         if (tracking) // if we're currently tracking
            updateLocation(location); // update the location
      } // end onLocationChanged

      public void onProviderDisabled(String provider) 
      {
      } // end onProviderDisabled

      public void onProviderEnabled(String provider) 
      {
      } // end onProviderEnabled

      public void onStatusChanged(String provider, 
         int status, Bundle extras) 
      {
      } // end onStatusChanged
   }; // end locationListener

   // determine whether we have GPS fix
   GpsStatus.Listener gpsStatusListener = new GpsStatus.Listener() 
   {
      public void onGpsStatusChanged(int event) 
      {
         if (event == GpsStatus.GPS_EVENT_FIRST_FIX) 
         {
            gpsFix = true;
            Toast results = Toast.makeText(RouteTracker.this, 
               getResources().getString(R.string.toast_signal_acquired), 
               Toast.LENGTH_SHORT);
            
            // center the Toast in the screen
            results.setGravity(Gravity.CENTER, 
               results.getXOffset() / 2, results.getYOffset() / 2);     
            results.show(); // display the results
         } // end if
      } // end method on GpsStatusChanged
   }; // end anonymous inner class

   // Google terms of use require this method to return
   // true if you're displaying route information like driving directions
   @Override
   protected boolean isRouteDisplayed() 
   {
      return false; // we aren't displaying route information
   } // end method isRouteDisplayed

   // create the Activity's menu from a menu resource XML file
   @Override
   public boolean onCreateOptionsMenu(Menu menu) 
   {
      super.onCreateOptionsMenu(menu);
      MenuInflater inflater = getMenuInflater();
      inflater.inflate(R.menu.route_tracker_menu, menu);
      return true;
   } // end method onCreateOptionsMenu
   
   // handle choice from options menu
   @Override
   public boolean onOptionsItemSelected(MenuItem item) 
   {
      // perform appropriate task based on 
      switch (item.getItemId()) 
      {
         case R.id.mapItem: // the user selected "Map"
            mapView.setSatellite(false); // display map image
            return true;
         case R.id.satelliteItem: // the user selected "Satellite"
            mapView.setSatellite(true); // display satellite image
            return true;
         default:
            return super.onOptionsItemSelected(item);
      } // end switch
   } // end method onOptionsItemSelected
        
   // listener for trackingToggleButton's events
   OnCheckedChangeListener trackingToggleButtonListener = 
      new OnCheckedChangeListener()
      {
         // called when user toggles tracking state
         @Override
         public void onCheckedChanged(CompoundButton buttonView,
            boolean isChecked)
         { 
            // if app is currently tracking
            if (!isChecked)
            {
               tracking = false; // just stopped tracking locations
            
               // compute the total time we were tracking
               long milliseconds = System.currentTimeMillis() - startTime;
               double totalHours = milliseconds / MILLISECONDS_PER_HOUR;
               
               // create a dialog displaying the results
               AlertDialog.Builder dialogBuilder = 
                  new AlertDialog.Builder(RouteTracker.this);
               dialogBuilder.setTitle(R.string.results);
         
               double distanceKM = distanceTraveled / 1000.0;
               double speedKM = distanceKM / totalHours;
               double distanceMI = distanceKM * MILES_PER_KILOMETER;
               double speedMI = distanceMI / totalHours;
               
               // display distanceTraveled traveled and average speed
               dialogBuilder.setMessage(String.format(
                  getResources().getString(R.string.results_format), 
                  distanceKM, distanceMI, speedKM, speedMI));
               dialogBuilder.setPositiveButton(
                  R.string.button_ok, null);
               dialogBuilder.show(); // display the dialog
            } // end if
            else  
            {   
               tracking = true; // app is now tracking
               startTime = System.currentTimeMillis(); // get current time
               routeOverlay.reset(); // reset for new route
               bearingFrameLayout.invalidate(); // clear the route
               previousLocation = null; // starting a new route
            } // end else
         } // end method onCheckChanged
      }; // end anonymous inner class    
} // end class RouteTracker


/**************************************************************************
 * (C) Copyright 1992-2012 by Deitel & Associates, Inc. and               *
 * Pearson Education, Inc. All Rights Reserved.                           *
 *                                                                        *
 * DISCLAIMER: The authors and publisher of this book have used their     *
 * best efforts in preparing the book. These efforts include the          *
 * development, research, and testing of the theories and programs        *
 * to determine their effectiveness. The authors and publisher make       *
 * no warranty of any kind, expressed or implied, with regard to these    *
 * programs or to the documentation contained in these books. The authors *
 * and publisher shall not be liable in any event for incidental or       *
 * consequential damages in connection with, or arising out of, the       *
 * furnishing, performance, or use of these programs.                     *
 **************************************************************************/
