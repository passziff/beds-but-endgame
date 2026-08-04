package com.bedsbutendgame.client;

import com.bedsbutendgame.config.BbeConfig;

public final class ClientConfigState {
	private static boolean disablePhantoms = BbeConfig.DEFAULT_DISABLE_PHANTOMS;
	private static int nightmareChance = BbeConfig.DEFAULT_NIGHTMARE_CHANCE;

	private ClientConfigState() {
	}

	public static void update(boolean phantomsDisabled, int chance) {
		disablePhantoms = phantomsDisabled;
		nightmareChance = chance;
	}

	public static boolean disablePhantoms() {
		return disablePhantoms;
	}

	public static int nightmareChance() {
		return nightmareChance;
	}
}
