package com.general.mbts4ma.view.framework.util;

import java.net.InetAddress;

public abstract class HardwareUtil {

	public static synchronized String getComputerName() {
		try {
			return InetAddress.getLocalHost().getHostName();
		} catch (Exception e) {
			return null;
		}
	}

}
