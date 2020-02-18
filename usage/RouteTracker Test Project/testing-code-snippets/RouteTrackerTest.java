package com.deitel.routetracker.test;

import android.test.ActivityInstrumentationTestCase2;
import com.deitel.routetracker.R;
import com.deitel.routetracker.RouteTracker;
import com.deitel.routetracker.test.util.*;
import com.deitel.routetracker.test.adapters.*;
import com.general.mbts4ma.erunner.*;
import com.robotium.solo.Solo;


public class RouteTrackerTest extends ActivityInstrumentationTestCase2 {

    private Solo solo;

    private boolean actualTest = false;

    @SuppressWarnings("unchecked")
    public RouteTrackerTest() {
        super(RouteTracker.class);
    }

    @Override
    public void setUp() throws Exception {
        solo = new Solo(getInstrumentation(), getActivity());
    }
    
public void testCES1() {
    String ces = "displayTrackingScreen, pressMenu, displayOptions, pressBack, displayTrackingScreen, pressStartTracking, listenToGps, readUpdatedLocation, updateScreenMap, listenToGps, pressStopTracking, showDistanceAndAvgSpeed, pressOk, displayTrackingScreen, receiveCallAndAccept, pressBack";
    
    new EventRunner().executeCompleteEventSequence(new RouteTrackerAdapter(solo), ces);
}

public void testCES2() {
    String ces = "displayTrackingScreen, changeWiFiData, pressStartTracking, listenToGps, pressStopTracking, showDistanceAndAvgSpeed, pressOk, displayTrackingScreen, pressBack";
    
    new EventRunner().executeCompleteEventSequence(new RouteTrackerAdapter(solo), ces);
}

public void testCES3() {
    String ces = "displayTrackingScreen, pressMenu, displayOptions, selectMap, displayMap, displayTrackingScreen, pressBack";
    
    new EventRunner().executeCompleteEventSequence(new RouteTrackerAdapter(solo), ces);
}

public void testCES4() {
    String ces = "displayTrackingScreen, pressMenu, displayOptions, selectSatellite, displaySatellite, displayTrackingScreen, pressBack";
    
    new EventRunner().executeCompleteEventSequence(new RouteTrackerAdapter(solo), ces);
}

public void testCES5() {
    String ces = "displayTrackingScreen, pressMenu, displayOptions, pressBack, displayTrackingScreen, pressStartTracking, listenToGps, sleepFor30Seconds, pressStopTracking, showDistanceAndAvgSpeed, pressOk, displayTrackingScreen, pressBack";
    
    new EventRunner().executeCompleteEventSequence(new RouteTrackerAdapter(solo), ces);
}
    
    @Override
    public void tearDown() throws Exception {
        solo.finishOpenedActivities();
    }

}
