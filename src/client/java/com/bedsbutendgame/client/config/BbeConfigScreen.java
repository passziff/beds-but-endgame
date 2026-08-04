package com.bedsbutendgame.client.config;

import com.bedsbutendgame.client.ClientConfigState;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;

public final class BbeConfigScreen extends Screen {
	private static final int BUTTON_WIDTH = 240;
	private static final int BUTTON_HEIGHT = 20;

	private final Screen parent;
	private Button phantomsButton;
	private Button nightmaresButton;

	public BbeConfigScreen(Screen parent) {
		super(Component.translatable("config.bedsbutendgame.title"));
		this.parent = parent;
	}

	@Override
	protected void init() {
		int x = (this.width - BUTTON_WIDTH) / 2;

		this.phantomsButton = this.addRenderableWidget(Button.builder(
				disablePhantomsText(),
				button -> sendToggle("disablePhantoms", ClientConfigState.disablePhantoms())
		).pos(x, 72).size(BUTTON_WIDTH, BUTTON_HEIGHT).build());

		this.nightmaresButton = this.addRenderableWidget(Button.builder(
				nightmaresText(),
				button -> sendToggle("nightmares", ClientConfigState.nightmares())
		).pos(x, 96).size(BUTTON_WIDTH, BUTTON_HEIGHT).build());

		this.addRenderableWidget(Button.builder(CommonComponents.GUI_DONE, button -> this.onClose())
				.pos(x, this.height - 28).size(BUTTON_WIDTH, BUTTON_HEIGHT).build());

		boolean connected = this.minecraft.getConnection() != null;
		this.phantomsButton.active = connected;
		this.nightmaresButton.active = connected;
	}

	@Override
	public void tick() {
		this.phantomsButton.setMessage(disablePhantomsText());
		this.nightmaresButton.setMessage(nightmaresText());
	}

	@Override
	public void extractRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float delta) {
		super.extractRenderState(graphics, mouseX, mouseY, delta);
		graphics.centeredText(this.font, this.title, this.width / 2, 14, 0xFFFFFFFF);
		graphics.centeredText(
				this.font,
				Component.translatable("config.bedsbutendgame.server_note"),
				this.width / 2,
				34,
				0xFFAAAAAA
		);
	}

	@Override
	public void onClose() {
		this.minecraft.gui.setScreen(this.parent);
	}

	private void sendToggle(String option, boolean currentValue) {
		if (this.minecraft.getConnection() != null) {
			this.minecraft.getConnection().sendCommand(
					"bbe config " + option + " " + (currentValue ? "off" : "on")
			);
		}
	}

	private static Component disablePhantomsText() {
		return optionText("disablePhantoms", ClientConfigState.disablePhantoms());
	}

	private static Component nightmaresText() {
		return optionText("nightmares", ClientConfigState.nightmares());
	}

	private static Component optionText(String option, boolean enabled) {
		return Component.translatable(
				"config.bedsbutendgame.option",
				Component.translatable("config.bedsbutendgame." + option),
				Component.translatable(enabled ? "config.bedsbutendgame.on" : "config.bedsbutendgame.off")
		);
	}
}
