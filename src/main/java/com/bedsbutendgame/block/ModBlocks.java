package com.bedsbutendgame.block;

import com.bedsbutendgame.BedsButEndgame;
import net.fabricmc.fabric.api.creativetab.v1.CreativeModeTabEvents;
import net.minecraft.references.BlockItemId;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;

import java.util.function.Function;

public final class ModBlocks {
	private static final BlockItemId BEDSIDE_TABLE_ID = createId("bedside_table");

	public static final Block BEDSIDE_TABLE = register(
			BEDSIDE_TABLE_ID,
			BedsideTableBlock::new,
			BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_PLANKS).noOcclusion()
	);

	private ModBlocks() {
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

	public static void initialize() {
		CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.FUNCTIONAL_BLOCKS)
				.register(output -> output.accept(BEDSIDE_TABLE.asItem()));
	}
}
