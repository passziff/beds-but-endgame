package com.bedsbutendgame.sleep;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Difficulty;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.entity.ai.pathfinding.Path;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.zombie.Zombie;
import net.minecraft.world.phys.AABB;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

public final class SecuredSleepZone {
	private static final int HORIZONTAL_RADIUS = 20;
	private static final int VERTICAL_RADIUS = 10;
	private static final int MAX_SPAWN_CANDIDATES = 128;
	private static final int MAX_PATH_CHECKS = 64;

	private SecuredSleepZone() {
	}

	public static boolean isUnsafe(ServerPlayer player, BlockPos bedPos) {
		ServerLevel level = player.level();
		if (level.getDifficulty() == Difficulty.PEACEFUL) {
			return false;
		}

		BlockPos target = player.blockPosition();
		AABB area = new AABB(
				bedPos.getX() - HORIZONTAL_RADIUS,
				bedPos.getY() - VERTICAL_RADIUS,
				bedPos.getZ() - HORIZONTAL_RADIUS,
				bedPos.getX() + HORIZONTAL_RADIUS,
				bedPos.getY() + VERTICAL_RADIUS,
				bedPos.getZ() + HORIZONTAL_RADIUS
		);

		for (Monster monster : level.getEntitiesOfClass(Monster.class, area, Monster::isAlive)) {
			Path path = monster.getNavigation().createPath(target, 1);
			if (path != null && path.canReach()) {
				return true;
			}
		}

		Zombie probe = EntityType.ZOMBIE.create(level, EntitySpawnReason.NATURAL);
		if (probe == null) {
			return false;
		}

		Comparator<BlockPos> farthestFirst = Comparator
				.comparingLong((BlockPos pos) -> distanceSquared(pos, bedPos))
				.reversed();
		PriorityQueue<BlockPos> nearestCandidates = new PriorityQueue<>(MAX_SPAWN_CANDIDATES, farthestFirst);

		BlockPos min = bedPos.offset(-HORIZONTAL_RADIUS, -VERTICAL_RADIUS, -HORIZONTAL_RADIUS);
		BlockPos max = bedPos.offset(HORIZONTAL_RADIUS - 1, VERTICAL_RADIUS - 1, HORIZONTAL_RADIUS - 1);

		for (BlockPos candidate : BlockPos.betweenClosed(min, max)) {
			if (!level.hasChunkAt(candidate)) {
				continue;
			}
			if (!SpawnPlacements.isSpawnPositionOk(EntityType.ZOMBIE, level, candidate)) {
				continue;
			}
			if (!SpawnPlacements.checkSpawnRules(
					EntityType.ZOMBIE,
					level,
					EntitySpawnReason.NATURAL,
					candidate,
					level.getRandom()
			)) {
				continue;
			}

			BlockPos stored = candidate.immutable();
			if (nearestCandidates.size() < MAX_SPAWN_CANDIDATES) {
				nearestCandidates.add(stored);
			} else if (distanceSquared(stored, bedPos) < distanceSquared(nearestCandidates.peek(), bedPos)) {
				nearestCandidates.poll();
				nearestCandidates.add(stored);
			}
		}

		List<BlockPos> orderedCandidates = new ArrayList<>(nearestCandidates);
		orderedCandidates.sort(Comparator.comparingLong(pos -> distanceSquared(pos, bedPos)));

		int pathChecks = 0;
		for (BlockPos candidate : orderedCandidates) {
			probe.snapTo(
					candidate.getX() + 0.5,
					candidate.getY(),
					candidate.getZ() + 0.5,
					0.0F,
					0.0F
			);
			if (!probe.checkSpawnObstruction(level)) {
				continue;
			}

			Path path = probe.getNavigation().createPath(target, 1);
			pathChecks++;
			if (path != null && path.canReach()) {
				return true;
			}
			if (pathChecks >= MAX_PATH_CHECKS) {
				break;
			}
		}

		return false;
	}

	private static long distanceSquared(BlockPos first, BlockPos second) {
		long x = first.getX() - second.getX();
		long y = first.getY() - second.getY();
		long z = first.getZ() - second.getZ();
		return x * x + y * y + z * z;
	}
}
