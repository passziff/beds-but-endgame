package com.bedsbutendgame.sleep;

import com.bedsbutendgame.block.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BedBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BedPart;

public final class BedsideTableSleepCheck {
	private BedsideTableSleepCheck() {
	}

	public static boolean hasBedsideTable(Level level, BlockPos bedPos) {
		BlockState bedState = level.getBlockState(bedPos);
		if (!(bedState.getBlock() instanceof BedBlock)) {
			return true;
		}

		Direction facing = bedState.getValue(BedBlock.FACING);
		BlockPos headPos = bedState.getValue(BedBlock.PART) == BedPart.HEAD
				? bedPos
				: bedPos.relative(facing);

		Direction firstSide = facing.getAxis() == Direction.Axis.X
				? Direction.NORTH
				: Direction.EAST;
		Direction secondSide = firstSide.getOpposite();

		return level.getBlockState(headPos.relative(firstSide)).is(ModBlocks.BEDSIDE_TABLE)
				|| level.getBlockState(headPos.relative(secondSide)).is(ModBlocks.BEDSIDE_TABLE);
	}
}
