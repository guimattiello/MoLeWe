package com.deitel.routetracker.test.adapters;

import com.general.mbts4ma.erunner.*;
import com.robotium.solo.Solo;

public class RouteTrackerAdapter {

    private Solo solo = null;

    public RouteTrackerAdapter(Solo solo) {
        this.solo = solo;
    }

@Event(label = "displayTrackingScreen")
public boolean displayTrackingScreen() {

}

@Event(label = "pressMenu")
public boolean pressMenu() {
new Thread(new Runnable() {
    public void run() {
        getInstrumentation().sendKeyDownUpSync(KeyEvent.KEYCODE_MENU);
    }
}).start();
}

@Event(label = "displayOptions")
public boolean displayOptions() {

}

@Event(label = "pressBack")
public boolean pressBack() {
solo.goBack();
}

@Event(label = "pressStartTracking")
public boolean pressStartTracking() {
solo.clickOnButton(solo.getString(R.string.button_start_tracking));
}

@Event(label = "listenToGps")
public boolean listenToGps() {

}

@Event(label = "readUpdatedLocation")
public boolean readUpdatedLocation() {
TestLocationProvider.sendLocation(solo, new double[] { 43.723180, 44.697660 }, new double[] { 10.396677, 10.631054 });
}

@Event(label = "updateScreenMap")
public boolean updateScreenMap() {

}

@Event(label = "pressStopTracking")
public boolean pressStopTracking() {
solo.clickOnButton(solo.getString(R.string.button_stop_tracking));
}

@Event(label = "showDistanceAndAvgSpeed")
public boolean showDistanceAndAvgSpeed() {

}

@Event(label = "pressOk")
public boolean pressOk() {
solo.clickOnButton(solo.getString(R.string.button_ok));
}

@Event(label = "receiveCallAndAccept")
public boolean receiveCallAndAccept() {
TestPhoneCallEvent.phoneCallAction(PhoneCallActions.call, "12349876");

solo.sleep(2000);

TestPhoneCallEvent.phoneCallAction(PhoneCallActions.accept, "12349876");

solo.sleep(1000);

TestPhoneCallEvent.phoneCallAction(PhoneCallActions.cancel, "12349876");

solo.sleep(5000);

TestPhoneCallEvent.goBack();
}

@Event(label = "pressBack")
public boolean pressBack() {
solo.goBack();
}

@Event(label = "changeWiFiData")
public boolean changeWiFiData() {
solo.setWiFiData(false);
}

@Event(label = "selectMap")
public boolean selectMap() {
solo.clickOnMenuItem(solo.getString(R.string.menuitem_map));
}

@Event(label = "displayMap")
public boolean displayMap() {

}

@Event(label = "selectSatellite")
public boolean selectSatellite() {
solo.clickOnMenuItem(solo.getString(R.string.menuitem_satellite));
}

@Event(label = "displaySatellite")
public boolean displaySatellite() {

}

@Event(label = "sleepFor30Seconds")
public boolean sleepFor30Seconds() {
solo.sleep(30000);
}

}
