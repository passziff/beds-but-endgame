package com.bedsbutendgame.sleep;

import com.bedsbutendgame.config.ConfigManager;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.clock.WorldClocks;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public final class NightmareManager {
	private static final int NIGHTMARE_DELAY_TICKS = 60;
	private static final long MIDNIGHT_TIME = 18000L;

	private static final Map<UUID, PendingNightmare> PENDING = new HashMap<>();
	private static final Set<UUID> LOCKED_OUT = new HashSet<>();

	private NightmareManager() {
	}

	public static void initialize() {
		ServerTickEvents.END_SERVER_TICK.register(NightmareManager::tick);
	}

	public static boolean isLockedOut(ServerPlayer player) {
		if (ConfigManager.nightmareChance() <= 0 || isDay(player.level())) {
			LOCKED_OUT.remove(player.getUUID());
			return false;
		}
		return LOCKED_OUT.contains(player.getUUID());
	}

	public static void onSleepStarted(ServerPlayer player, BlockPos bedPos) {
		int chance = ConfigManager.nightmareChance();
		if (chance <= 0) {
			return;
		}
		if (BedsideTableSleepCheck.hasSoulLantern(player.level(), bedPos)) {
			return;
		}
		if (player.getRandom().nextInt(100) >= chance) {
			return;
		}

		PENDING.put(player.getUUID(), new PendingNightmare(player.level().getGameTime() + NIGHTMARE_DELAY_TICKS));
	}

	private static void tick(MinecraftServer server) {
		int chance = ConfigManager.nightmareChance();
		for (ServerPlayer player : server.getPlayerList().getPlayers()) {
			if (chance <= 0 || isDay(player.level())) {
				LOCKED_OUT.remove(player.getUUID());
				PENDING.remove(player.getUUID());
			}
		}

		if (chance <= 0) {
			PENDING.clear();
			LOCKED_OUT.clear();
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
			LOCKED_OUT.add(player.getUUID());
			iterator.remove();
		}
	}

	private static boolean isDay(ServerLevel level) {
		long timeOfDay = Math.floorMod(level.getOverworldClockTime(), 24000L);
		return timeOfDay < 12000L;
	}

	private static void moveToMidnight(ServerLevel level) {
		long clockTime = level.getOverworldClockTime();
		long timeOfDay = Math.floorMod(clockTime, 24000L);
		if (timeOfDay < MIDNIGHT_TIME) {
			level.clockManager().setTotalTicks(
					level.registryAccess().getOrThrow(WorldClocks.OVERWORLD),
					clockTime + (MIDNIGHT_TIME - timeOfDay)
			);
		}
	}

	private record PendingNightmare(long triggerGameTime) {
	}
}
