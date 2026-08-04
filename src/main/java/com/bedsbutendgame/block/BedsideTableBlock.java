package com.bedsbutendgame.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.EnumMap;
import java.util.Map;

public final class BedsideTableBlock extends HorizontalDirectionalBlock {
	public static final MapCodec<BedsideTableBlock> CODEC = simpleCodec(BedsideTableBlock::new);

	private static final double[][] WOODEN_BOXES = {
			{1.0, 0.0, 0.0, 3.0, 16.0, 2.0},
			{3.0, 2.0, 0.0, 14.0, 16.0, 2.0},
			{14.0, 0.0, 0.0, 16.0, 16.0, 2.0},
			{1.0, 2.0, 2.0, 16.0, 6.2, 14.0},
			{0.6, 6.2, 2.0, 16.0, 9.0, 14.0},
			{1.0, 9.0, 2.0, 16.0, 9.8, 14.0},
			{0.6, 9.8, 2.0, 16.0, 13.3, 14.0},
			{1.0, 13.3, 2.0, 16.0, 16.0, 14.0},
			{1.0, 0.0, 14.0, 3.0, 16.0, 16.0},
			{3.0, 2.0, 14.0, 14.0, 16.0, 16.0},
			{14.0, 0.0, 14.0, 16.0, 16.0, 16.0}
	};


	private static final Map<Direction, VoxelShape> OUTLINE_SHAPES = createShapes();

	public BedsideTableBlock(Properties properties) {
		super(properties);
		registerDefaultState(stateDefinition.any().setValue(FACING, Direction.NORTH));
	}

	@Override
	protected MapCodec<? extends HorizontalDirectionalBlock> codec() {
		return CODEC;
	}

	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		return defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
	}

	@Override
	protected VoxelShape getShape(
			BlockState state,
			BlockGetter level,
			BlockPos pos,
			CollisionContext context
	) {
		return OUTLINE_SHAPES.get(state.getValue(FACING));
	}

	@Override
	protected VoxelShape getCollisionShape(
			BlockState state,
			BlockGetter level,
			BlockPos pos,
			CollisionContext context
	) {
		return Shapes.block();
	}

	@Override
	protected VoxelShape getOcclusionShape(BlockState state) {
		return Shapes.empty();
	}

	@Override
	protected boolean useShapeForLightOcclusion(BlockState state) {
		return false;
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(FACING);
	}

	private static Map<Direction, VoxelShape> createShapes() {
		EnumMap<Direction, VoxelShape> shapes = new EnumMap<>(Direction.class);
		shapes.put(Direction.NORTH, createShape(Direction.NORTH));
		shapes.put(Direction.SOUTH, createShape(Direction.SOUTH));
		shapes.put(Direction.EAST, createShape(Direction.EAST));
		shapes.put(Direction.WEST, createShape(Direction.WEST));
		return Map.copyOf(shapes);
	}

	private static VoxelShape createShape(Direction facing) {
		VoxelShape shape = Shapes.empty();
		for (double[] box : WOODEN_BOXES) {
			shape = Shapes.or(shape, boxForFacing(box, facing));
		}
		return shape.optimize();
	}

	private static VoxelShape boxForFacing(double[] box, Direction facing) {
		double minX = box[0];
		double minY = box[1];
		double minZ = box[2];
		double maxX = box[3];
		double maxY = box[4];
		double maxZ = box[5];

		return switch (facing) {
			case WEST -> Block.box(minX, minY, minZ, maxX, maxY, maxZ);
			case NORTH -> Block.box(16.0 - maxZ, minY, minX, 16.0 - minZ, maxY, maxX);
			case EAST -> Block.box(16.0 - maxX, minY, 16.0 - maxZ, 16.0 - minX, maxY, 16.0 - minZ);
			case SOUTH -> Block.box(minZ, minY, 16.0 - maxX, maxZ, maxY, 16.0 - minX);
			default -> throw new IllegalArgumentException("Bedside Table only supports horizontal facing");
		};
	}
}
