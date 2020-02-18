// RouteOverlay.java
// Draws route on MapView.
package com.deitel.routetracker;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.location.Location;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;

public class RouteOverlay extends Overlay 
{
   private List<Location> locations; // stores Location tracking data
   private Paint pathPaint; // Paint information for the Path
   private Paint positionPaint; // Paint information for current position
   private final int POSITION_MARKER = 10; // marker frequency
  
   public RouteOverlay() 
   {
      // Paint for drawing Path as a red line with a width of 5
      pathPaint = new Paint();
      pathPaint.setAntiAlias(true);
      pathPaint.setColor(Color.RED);
      pathPaint.setStyle(Paint.Style.STROKE);
      pathPaint.setStrokeWidth(5);
      locations = new ArrayList<Location>(); // initialize points
      
      // Paint for drawing black circle every POSITION_MARKER Locations
      positionPaint = new Paint();
      positionPaint.setAntiAlias(true);
      positionPaint.setStyle(Paint.Style.FILL);
   } // end RouteOverlay constructor

   // add new Location to List of Locations
   public void addPoint(Location location) 
   {
      locations.add(location);
   } // end method addPoint

   // reset the Overlay for tracking a new route
   public void reset()
   {
      locations.clear(); // delete all prior Locations
   } // end method reset

   // draw this Overlay on top of the given MapView
   @Override
   public void draw(Canvas canvas, MapView mapView, boolean shadow) 
   {
      super.draw(canvas, mapView, shadow); // call super's draw method
      Path newPath = new Path(); // get a new Path
      Location previous = null; // initialize previous Location to null
      
      // for each Location
      for (int i = 0; i < locations.size(); ++i) 
      {
         Location location = locations.get(i);

         // convert Location to GeoPoint
         Double newLatitude = location.getLatitude() * 1E6;
         Double newLongitude = location.getLongitude() * 1E6;
         GeoPoint newPoint = new GeoPoint(newLatitude.intValue(),
            newLongitude.intValue());

         // convert the GeoPoint to point on the screen
         Point newScreenPoints = new Point();
         mapView.getProjection().toPixels(newPoint, newScreenPoints);

         if (previous != null) // if this is not the first Location
         {
            // get GeoPoint for the previous Location
            Double oldLatitude = previous.getLatitude() * 1E6;
            Double oldLongitude = previous.getLongitude() * 1E6;
            GeoPoint oldPoint = new GeoPoint(oldLatitude.intValue(),
               oldLongitude.intValue());

            // convert the GeoPoint to point on the screen
            Point oldScreenPoints = new Point();
            mapView.getProjection().toPixels(oldPoint, oldScreenPoints);

            // add the new point to the Path
            newPath.quadTo(oldScreenPoints.x, oldScreenPoints.y,
               (newScreenPoints.x + oldScreenPoints.x) / 2,
               (newScreenPoints.y + oldScreenPoints.y) / 2);

            // possibly draw a black dot for current position 
            if ((i % POSITION_MARKER) == 0)
               canvas.drawCircle(newScreenPoints.x, newScreenPoints.y, 10, 
                  positionPaint);
         } // end if
         else 
         {
            // move to the first Location
            newPath.moveTo(newScreenPoints.x, newScreenPoints.y);
         } // end else
         
         previous = location; // store location
      } // end for

      canvas.drawPath(newPath, pathPaint); // draw the path
   } // end method draw
} // end class RouteOverlay


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