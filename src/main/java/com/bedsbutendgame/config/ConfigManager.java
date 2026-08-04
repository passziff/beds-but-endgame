package com.bedsbutendgame.config;

import com.bedsbutendgame.BedsButEndgame;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;

public final class ConfigManager {
	private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
	private static final Path CONFIG_PATH = FabricLoader.getInstance()
			.getConfigDir()
			.resolve("bedsbutendgame.json");

	private static BbeConfig config = new BbeConfig();

	private ConfigManager() {
	}

	public static synchronized void load() {
		if (!Files.exists(CONFIG_PATH)) {
			save();
			return;
		}

		try (Reader reader = Files.newBufferedReader(CONFIG_PATH)) {
			BbeConfig loaded = GSON.fromJson(reader, BbeConfig.class);
			config = loaded == null ? new BbeConfig() : loaded;
		} catch (Exception exception) {
			BedsButEndgame.LOGGER.warn("Could not read the Beds, but Endgame config. Restoring defaults.", exception);
			config = new BbeConfig();
			save();
		}
	}

	public static synchronized void save() {
		try {
			Files.createDirectories(CONFIG_PATH.getParent());
			try (Writer writer = Files.newBufferedWriter(CONFIG_PATH)) {
				GSON.toJson(config, writer);
			}
		} catch (IOException exception) {
			BedsButEndgame.LOGGER.error("Could not save the Beds, but Endgame config", exception);
		}
	}

	public static synchronized boolean disablePhantoms() {
		return config.disablePhantoms;
	}

	public static synchronized boolean nightmares() {
		return config.nightmares;
	}

	public static synchronized boolean get(ConfigOption option) {
		return switch (option) {
			case DISABLE_PHANTOMS -> config.disablePhantoms;
			case NIGHTMARES -> config.nightmares;
		};
	}

	public static synchronized void set(ConfigOption option, boolean enabled) {
		switch (option) {
			case DISABLE_PHANTOMS -> config.disablePhantoms = enabled;
			case NIGHTMARES -> config.nightmares = enabled;
		}
		save();
	}
}
