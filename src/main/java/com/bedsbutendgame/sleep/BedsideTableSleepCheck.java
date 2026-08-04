package com.bedsbutendgame.sleep;

import com.bedsbutendgame.block.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BedBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BedPart;

import java.util.List;

public final class BedsideTableSleepCheck {
	private BedsideTableSleepCheck() {
	}

	public static boolean hasBedsideTable(Level level, BlockPos bedPos) {
		return !findBedsideTables(level, bedPos).isEmpty();
	}

	public static boolean hasSoulLantern(Level level, BlockPos bedPos) {
		for (BlockPos tablePos : findBedsideTables(level, bedPos)) {
			if (level.getBlockState(tablePos.above()).is(Blocks.SOUL_LANTERN)) {
				return true;
			}
		}
		return false;
	}

	public static List<BlockPos> findBedsideTables(Level level, BlockPos bedPos) {
		BlockState bedState = level.getBlockState(bedPos);
		if (!(bedState.getBlock() instanceof BedBlock)) {
			return List.of();
		}

		Direction facing = bedState.getValue(BedBlock.FACING);
		BlockPos headPos = bedState.getValue(BedBlock.PART) == BedPart.HEAD
				? bedPos
				: bedPos.relative(facing);

		Direction firstSide = facing.getAxis() == Direction.Axis.X
				? Direction.NORTH
				: Direction.EAST;
		BlockPos firstPos = headPos.relative(firstSide);
		BlockPos secondPos = headPos.relative(firstSide.getOpposite());
		boolean firstValid = level.getBlockState(firstPos).is(ModBlocks.BEDSIDE_TABLE);
		boolean secondValid = level.getBlockState(secondPos).is(ModBlocks.BEDSIDE_TABLE);

		if (firstValid && secondValid) {
			return List.of(firstPos, secondPos);
		}
		if (firstValid) {
			return List.of(firstPos);
		}
		if (secondValid) {
			return List.of(secondPos);
		}
		return List.of();
	}
}
