package com.bedsbutendgame.block;

import com.bedsbutendgame.BedsButEndgame;
import net.fabricmc.fabric.api.creativetab.v1.CreativeModeTabEvents;
import net.fabricmc.fabric.api.registry.FlammableBlockRegistry;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.references.BlockItemId;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;
import java.util.Set;
import java.util.function.Function;

public final class ModBlocks {
	public static final Block BEDSIDE_TABLE = registerBedsideTable("bedside_table", Blocks.OAK_PLANKS);
	public static final Block SPRUCE_BEDSIDE_TABLE = registerBedsideTable("spruce_bedside_table", Blocks.SPRUCE_PLANKS);
	public static final Block BIRCH_BEDSIDE_TABLE = registerBedsideTable("birch_bedside_table", Blocks.BIRCH_PLANKS);
	public static final Block JUNGLE_BEDSIDE_TABLE = registerBedsideTable("jungle_bedside_table", Blocks.JUNGLE_PLANKS);
	public static final Block ACACIA_BEDSIDE_TABLE = registerBedsideTable("acacia_bedside_table", Blocks.ACACIA_PLANKS);
	public static final Block DARK_OAK_BEDSIDE_TABLE = registerBedsideTable("dark_oak_bedside_table", Blocks.DARK_OAK_PLANKS);
	public static final Block MANGROVE_BEDSIDE_TABLE = registerBedsideTable("mangrove_bedside_table", Blocks.MANGROVE_PLANKS);
	public static final Block CHERRY_BEDSIDE_TABLE = registerBedsideTable("cherry_bedside_table", Blocks.CHERRY_PLANKS);
	public static final Block PALE_OAK_BEDSIDE_TABLE = registerBedsideTable("pale_oak_bedside_table", Blocks.PALE_OAK_PLANKS);
	public static final Block BAMBOO_BEDSIDE_TABLE = registerBedsideTable("bamboo_bedside_table", Blocks.BAMBOO_PLANKS);
	public static final Block CRIMSON_BEDSIDE_TABLE = registerBedsideTable("crimson_bedside_table", Blocks.CRIMSON_PLANKS);
	public static final Block WARPED_BEDSIDE_TABLE = registerBedsideTable("warped_bedside_table", Blocks.WARPED_PLANKS);

	public static final List<Block> BEDSIDE_TABLES = List.of(
			BEDSIDE_TABLE,
			SPRUCE_BEDSIDE_TABLE,
			BIRCH_BEDSIDE_TABLE,
			JUNGLE_BEDSIDE_TABLE,
			ACACIA_BEDSIDE_TABLE,
			DARK_OAK_BEDSIDE_TABLE,
			MANGROVE_BEDSIDE_TABLE,
			CHERRY_BEDSIDE_TABLE,
			PALE_OAK_BEDSIDE_TABLE,
			BAMBOO_BEDSIDE_TABLE,
			CRIMSON_BEDSIDE_TABLE,
			WARPED_BEDSIDE_TABLE
	);

	private static final List<Block> FLAMMABLE_BEDSIDE_TABLES = List.of(
			BEDSIDE_TABLE,
			SPRUCE_BEDSIDE_TABLE,
			BIRCH_BEDSIDE_TABLE,
			JUNGLE_BEDSIDE_TABLE,
			ACACIA_BEDSIDE_TABLE,
			DARK_OAK_BEDSIDE_TABLE,
			MANGROVE_BEDSIDE_TABLE,
			CHERRY_BEDSIDE_TABLE,
			PALE_OAK_BEDSIDE_TABLE,
			BAMBOO_BEDSIDE_TABLE
	);

	private static final Set<Block> BEDSIDE_TABLE_SET = Set.copyOf(BEDSIDE_TABLES);

	private ModBlocks() {
	}

	private static Block registerBedsideTable(String name, Block planks) {
		return register(
				createId(name),
				BedsideTableBlock::new,
				BlockBehaviour.Properties.ofFullCopy(planks).noOcclusion()
		);
	}

	private static BlockItemId createId(String name) {
		var id = BedsButEndgame.id(name);
		return BlockItemId.create(id, id);
	}

	private static Block register(
			BlockItemId id,
			Function<BlockBehaviour.Properties, Block> blockFactory,
			BlockBehaviour.Properties properties
	) {
		Block block = Registry.register(
				BuiltInRegistries.BLOCK,
				id.block(),
				blockFactory.apply(properties.setId(id.block()))
		);

		BlockItem item = new BlockItem(
				block,
				new Item.Properties().useBlockDescriptionPrefix().setId(id.item())
		);
		Registry.register(BuiltInRegistries.ITEM, id.item(), item);

		return block;
	}

	public static boolean isBedsideTable(BlockState state) {
		return BEDSIDE_TABLE_SET.contains(state.getBlock());
	}

	public static void initialize() {
		FlammableBlockRegistry flammableBlocks = FlammableBlockRegistry.getDefaultInstance();
		FLAMMABLE_BEDSIDE_TABLES.forEach(block -> flammableBlocks.add(block, 5, 20));

		CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.FUNCTIONAL_BLOCKS)
				.register(output -> BEDSIDE_TABLES.forEach(block -> output.accept(block.asItem())));
	}
}
