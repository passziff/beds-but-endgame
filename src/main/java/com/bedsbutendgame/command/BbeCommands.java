package com.bedsbutendgame.command;

import com.bedsbutendgame.config.BbeConfig;
import com.bedsbutendgame.config.ConfigManager;
import com.bedsbutendgame.config.ConfigOption;
import com.bedsbutendgame.network.ConfigNetworking;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.permissions.Permissions;

public final class BbeCommands {
	private BbeCommands() {
	}

	public static void initialize() {
		CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> dispatcher.register(
				Commands.literal("bbe")
						.then(Commands.literal("config")
								.executes(BbeCommands::showConfig)
								.then(settingNode(ConfigOption.DISABLE_PHANTOMS))
								.then(Commands.literal("nightmareChance")
										.requires(BbeCommands::canChangeConfig)
										.then(Commands.argument("percent", IntegerArgumentType.integer(0, 100))
												.executes(BbeCommands::setNightmareChance)))
								.then(Commands.literal("reset")
										.requires(BbeCommands::canChangeConfig)
										.executes(BbeCommands::resetConfig)))
		));
	}

	private static com.mojang.brigadier.builder.LiteralArgumentBuilder<CommandSourceStack> settingNode(
			ConfigOption option
	) {
		return Commands.literal(option.commandName())
				.requires(BbeCommands::canChangeConfig)
				.then(Commands.literal("on").executes(context -> setOption(context, option, true)))
				.then(Commands.literal("off").executes(context -> setOption(context, option, false)));
	}

	private static boolean canChangeConfig(CommandSourceStack source) {
		var server = source.getServer();
		return server == null
				|| server.isSingleplayer()
				|| source.permissions().hasPermission(Permissions.COMMANDS_MODERATOR);
	}

	private static int showConfig(CommandContext<CommandSourceStack> context) {
		context.getSource().sendSuccess(() -> Component.translatable(
				"commands.bedsbutendgame.config.status",
				state(ConfigManager.disablePhantoms()),
				Component.literal(ConfigManager.nightmareChance() + "%")
		), false);
		return Command.SINGLE_SUCCESS;
	}

	private static int setOption(
			CommandContext<CommandSourceStack> context,
			ConfigOption option,
			boolean enabled
	) {
		ConfigManager.set(option, enabled);
		ConfigNetworking.broadcast(context.getSource().getServer());
		context.getSource().sendSuccess(() -> Component.translatable(
				"commands.bedsbutendgame.config.changed",
				Component.translatable("config.bedsbutendgame." + option.commandName()),
				state(enabled)
		), true);
		return Command.SINGLE_SUCCESS;
	}

	private static int setNightmareChance(CommandContext<CommandSourceStack> context) {
		int chance = IntegerArgumentType.getInteger(context, "percent");
		ConfigManager.setNightmareChance(chance);
		ConfigNetworking.broadcast(context.getSource().getServer());
		context.getSource().sendSuccess(() -> Component.translatable(
				"commands.bedsbutendgame.config.nightmare_chance_changed",
				Component.literal(chance + "%")
		), true);
		return Command.SINGLE_SUCCESS;
	}

	private static int resetConfig(CommandContext<CommandSourceStack> context) {
		ConfigManager.reset();
		ConfigNetworking.broadcast(context.getSource().getServer());
		context.getSource().sendSuccess(() -> Component.translatable(
				"commands.bedsbutendgame.config.reset",
				state(BbeConfig.DEFAULT_DISABLE_PHANTOMS),
				Component.literal(BbeConfig.DEFAULT_NIGHTMARE_CHANCE + "%")
		), true);
		return Command.SINGLE_SUCCESS;
	}

	private static Component state(boolean enabled) {
		return Component.translatable(enabled ? "config.bedsbutendgame.on" : "config.bedsbutendgame.off");
	}
}
