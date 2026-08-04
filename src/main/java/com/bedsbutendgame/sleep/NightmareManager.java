package com.bedsbutendgame.sleep;

import com.bedsbutendgame.config.ConfigManager;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

public final class NightmareManager {
	public static final int NIGHTMARE_CHANCE_PERCENT = 35;
	private static final int NIGHTMARE_DELAY_TICKS = 60;
	private static final long MIDNIGHT_TIME = 18000L;
	private static final String LOCKOUT_TAG = "bedsbutendgame.nightmare_lockout";

	private static final Map<UUID, PendingNightmare> PENDING = new HashMap<>();

	private NightmareManager() {
	}

	public static void initialize() {
		ServerTickEvents.END_SERVER_TICK.register(NightmareManager::tick);
	}

	public static boolean isLockedOut(ServerPlayer player) {
		if (!ConfigManager.nightmares() || player.level().isDay()) {
			player.removeTag(LOCKOUT_TAG);
			return false;
		}
		return player.getTags().contains(LOCKOUT_TAG);
	}

	public static void onSleepStarted(ServerPlayer player, BlockPos bedPos) {
		if (!ConfigManager.nightmares()) {
			return;
		}
		if (BedsideTableSleepCheck.hasSoulLantern(player.level(), bedPos)) {
			return;
		}
		if (player.getRandom().nextInt(100) >= NIGHTMARE_CHANCE_PERCENT) {
			return;
		}

		PENDING.put(player.getUUID(), new PendingNightmare(player.level().getGameTime() + NIGHTMARE_DELAY_TICKS));
	}

	private static void tick(MinecraftServer server) {
		for (ServerPlayer player : server.getPlayerList().getPlayers()) {
			if (!ConfigManager.nightmares() || player.level().isDay()) {
				player.removeTag(LOCKOUT_TAG);
				PENDING.remove(player.getUUID());
			}
		}

		if (!ConfigManager.nightmares()) {
			PENDING.clear();
			return;
		}

		Iterator<Map.Entry<UUID, PendingNightmare>> iterator = PENDING.entrySet().iterator();
		while (iterator.hasNext()) {
			Map.Entry<UUID, PendingNightmare> entry = iterator.next();
			ServerPlayer player = server.getPlayerList().getPlayer(entry.getKey());
			if (player == null || !player.isSleeping()) {
				iterator.remove();
				continue;
			}
			if (player.level().getGameTime() < entry.getValue().triggerGameTime()) {
				continue;
			}

			ServerLevel level = player.level();
			moveToMidnight(level);
			player.stopSleepInBed(true, true);
			player.sendSystemMessage(Component.translatable("sleep.bedsbutendgame.nightmare"));
			player.playSound(SoundEvents.SOUL_SAND_STEP, 0.8F, 0.7F);
			player.addTag(LOCKOUT_TAG);
			iterator.remove();
		}
	}

	private static void moveToMidnight(ServerLevel level) {
		long dayTime = level.getDayTime();
		long timeOfDay = Math.floorMod(dayTime, 24000L);
		if (timeOfDay < MIDNIGHT_TIME) {
			level.setDayTime(dayTime + (MIDNIGHT_TIME - timeOfDay));
		}
	}

	private record PendingNightmare(long triggerGameTime) {
	}
}
