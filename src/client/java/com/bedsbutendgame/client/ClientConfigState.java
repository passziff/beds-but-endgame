package com.bedsbutendgame.client;

public final class ClientConfigState {
	private static boolean disablePhantoms = true;
	private static boolean nightmares = true;

	private ClientConfigState() {
	}

	public static void update(boolean phantomsDisabled, boolean nightmaresEnabled) {
		disablePhantoms = phantomsDisabled;
		nightmares = nightmaresEnabled;
	}

	public static boolean disablePhantoms() {
		return disablePhantoms;
	}

	public static boolean nightmares() {
		return nightmares;
	}
}
