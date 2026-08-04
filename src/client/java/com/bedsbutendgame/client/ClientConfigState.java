package com.bedsbutendgame.client;

public final class ClientConfigState {
	private static boolean securedSleepZone = true;
	private static boolean disablePhantoms = true;
	private static boolean nightmares = true;

	private ClientConfigState() {
	}

	public static void update(boolean securedZone, boolean phantomsDisabled, boolean nightmaresEnabled) {
		securedSleepZone = securedZone;
		disablePhantoms = phantomsDisabled;
		nightmares = nightmaresEnabled;
	}

	public static boolean securedSleepZone() {
		return securedSleepZone;
	}

	public static boolean disablePhantoms() {
		return disablePhantoms;
	}

	public static boolean nightmares() {
		return nightmares;
	}
}
