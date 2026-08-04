package com.bedsbutendgame.command;

import com.bedsbutendgame.config.ConfigManager;
import com.bedsbutendgame.config.ConfigOption;
import com.bedsbutendgame.network.ConfigNetworking;
import com.mojang.brigadier.Command;
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
								.then(settingNode(ConfigOption.NIGHTMARES)))
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
		return source.getServer().isSingleplayer()
				|| source.permissions().hasPermission(Permissions.COMMANDS_MODERATOR);
	}

	private static int showConfig(CommandContext<CommandSourceStack> context) {
		context.getSource().sendSuccess(() -> Component.translatable(
				"commands.bedsbutendgame.config.status",
				state(ConfigManager.disablePhantoms()),
				state(ConfigManager.nightmares())
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

	private static Component state(boolean enabled) {
		return Component.translatable(enabled ? "config.bedsbutendgame.on" : "config.bedsbutendgame.off");
	}
}
