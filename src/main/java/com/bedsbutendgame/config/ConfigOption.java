package com.bedsbutendgame.config;

public enum ConfigOption {
	DISABLE_PHANTOMS("disablePhantoms");

	private final String commandName;

	ConfigOption(String commandName) {
		this.commandName = commandName;
	}

	public String commandName() {
		return commandName;
	}
}
