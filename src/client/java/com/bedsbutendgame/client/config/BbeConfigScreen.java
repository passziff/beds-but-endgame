package com.bedsbutendgame.client.config;

import com.bedsbutendgame.client.ClientConfigState;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public final class BbeConfigScreen extends Screen {
	private static final int BUTTON_WIDTH = 240;
	private static final int BUTTON_HEIGHT = 20;

	private final Screen parent;
	private Button securedZoneButton;
	private Button phantomsButton;
	private Button nightmaresButton;

	public BbeConfigScreen(Screen parent) {
		super(Component.translatable("config.bedsbutendgame.title"));
		this.parent = parent;
	}

	@Override
	protected void init() {
		int left = (width - BUTTON_WIDTH) / 2;
		int top = height / 2 - 50;

		securedZoneButton = addRenderableWidget(Button.builder(
				securedZoneText(),
				button -> sendToggle("securedSleepZone", ClientConfigState.securedSleepZone())
		).bounds(left, top, BUTTON_WIDTH, BUTTON_HEIGHT).build());

		phantomsButton = addRenderableWidget(Button.builder(
				disablePhantomsText(),
				button -> sendToggle("disablePhantoms", ClientConfigState.disablePhantoms())
		).bounds(left, top + 24, BUTTON_WIDTH, BUTTON_HEIGHT).build());

		nightmaresButton = addRenderableWidget(Button.builder(
				nightmaresText(),
				button -> sendToggle("nightmares", ClientConfigState.nightmares())
		).bounds(left, top + 48, BUTTON_WIDTH, BUTTON_HEIGHT).build());

		addRenderableWidget(Button.builder(
				Component.translatable("gui.done"),
				button -> onClose()
		).bounds(left, top + 88, BUTTON_WIDTH, BUTTON_HEIGHT).build());

		boolean connected = minecraft.getConnection() != null;
		securedZoneButton.active = connected;
		phantomsButton.active = connected;
		nightmaresButton.active = connected;
	}

	@Override
	public void tick() {
		securedZoneButton.setMessage(securedZoneText());
		phantomsButton.setMessage(disablePhantomsText());
		nightmaresButton.setMessage(nightmaresText());
	}

	@Override
	public void extractRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float delta) {
		super.extractRenderState(graphics, mouseX, mouseY, delta);
		drawCentered(graphics, title, 24, 0xFFFFFFFF, true);
		drawCentered(
				graphics,
				Component.translatable("config.bedsbutendgame.server_note"),
				44,
				0xFFA0A0A0,
				false
		);
	}

	@Override
	public void onClose() {
		minecraft.gui.setScreen(parent);
	}

	private void sendToggle(String option, boolean currentValue) {
		if (minecraft.getConnection() != null) {
			minecraft.getConnection().sendCommand(
					"bbe config " + option + " " + (currentValue ? "off" : "on")
			);
		}
	}

	private Component securedZoneText() {
		return optionText("securedSleepZone", ClientConfigState.securedSleepZone());
	}

	private Component disablePhantomsText() {
		return optionText("disablePhantoms", ClientConfigState.disablePhantoms());
	}

	private Component nightmaresText() {
		return optionText("nightmares", ClientConfigState.nightmares());
	}

	private static Component optionText(String option, boolean enabled) {
		return Component.translatable(
				"config.bedsbutendgame.option",
				Component.translatable("config.bedsbutendgame." + option),
				Component.translatable(enabled ? "config.bedsbutendgame.on" : "config.bedsbutendgame.off")
		);
	}

	private void drawCentered(
			GuiGraphicsExtractor graphics,
			Component text,
			int y,
			int color,
			boolean shadow
	) {
		graphics.text(font, text, (width - font.width(text)) / 2, y, color, shadow);
	}
}
