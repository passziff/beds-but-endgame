package com.bedsbutendgame.config;

import com.bedsbutendgame.BedsButEndgame;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
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
			JsonObject json = JsonParser.parseReader(reader).getAsJsonObject();
			BbeConfig loaded = new BbeConfig();
			boolean migrated = false;

			if (json.has("disablePhantoms")) {
				loaded.disablePhantoms = json.get("disablePhantoms").getAsBoolean();
			}

			if (json.has("nightmareChance")) {
				loaded.nightmareChance = clampChance(json.get("nightmareChance").getAsInt());
			} else if (json.has("nightmares")) {
				loaded.nightmareChance = json.get("nightmares").getAsBoolean()
						? BbeConfig.DEFAULT_NIGHTMARE_CHANCE
						: 0;
				migrated = true;
			}

			config = loaded;
			if (migrated || !json.has("nightmareChance")) {
				save();
			}
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

	public static synchronized int nightmareChance() {
		return config.nightmareChance;
	}

	public static synchronized boolean get(ConfigOption option) {
		return switch (option) {
			case DISABLE_PHANTOMS -> config.disablePhantoms;
		};
	}

	public static synchronized void set(ConfigOption option, boolean enabled) {
		switch (option) {
			case DISABLE_PHANTOMS -> config.disablePhantoms = enabled;
		}
		save();
	}

	public static synchronized void setNightmareChance(int chance) {
		config.nightmareChance = clampChance(chance);
		save();
	}

	public static synchronized void reset() {
		config = new BbeConfig();
		save();
	}

	private static int clampChance(int chance) {
		return Math.max(0, Math.min(100, chance));
	}
}
