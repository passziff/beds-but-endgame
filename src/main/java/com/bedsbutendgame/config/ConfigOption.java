package com.bedsbutendgame.config;

public enum ConfigOption {
	SECURED_SLEEP_ZONE("securedSleepZone"),
	DISABLE_PHANTOMS("disablePhantoms"),
	NIGHTMARES("nightmares");

	private final String commandName;

	ConfigOption(String commandName) {
		this.commandName = commandName;
	}

	public String commandName() {
		return commandName;
	}
}
