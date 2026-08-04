package com.bedsbutendgame.network;

import com.bedsbutendgame.config.ConfigManager;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;

public final class ConfigNetworking {
	private ConfigNetworking() {
	}

	public static void initialize() {
		PayloadTypeRegistry.clientboundPlay().register(ConfigSyncPayload.TYPE, ConfigSyncPayload.CODEC);
		ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> send(handler.getPlayer()));
	}

	public static void send(ServerPlayer player) {
		ServerPlayNetworking.send(player, currentPayload());
	}

	public static void broadcast(MinecraftServer server) {
		ConfigSyncPayload payload = currentPayload();
		for (ServerPlayer player : server.getPlayerList().getPlayers()) {
			ServerPlayNetworking.send(player, payload);
		}
	}

	private static ConfigSyncPayload currentPayload() {
		return new ConfigSyncPayload(
				ConfigManager.disablePhantoms(),
				ConfigManager.nightmares()
		);
	}
}
