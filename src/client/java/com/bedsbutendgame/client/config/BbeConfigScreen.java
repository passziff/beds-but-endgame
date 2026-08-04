package com.bedsbutendgame.client.config;

import com.bedsbutendgame.client.ClientConfigState;
import com.bedsbutendgame.config.BbeConfig;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.AbstractSliderButton;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;

import java.util.function.IntConsumer;

public final class BbeConfigScreen extends Screen {
	private static final int BUTTON_WIDTH = 240;
	private static final int BUTTON_HEIGHT = 20;

	private final Screen parent;
	private final boolean initialDisablePhantoms;
	private final int initialNightmareChance;

	private boolean pendingDisablePhantoms;
	private int pendingNightmareChance;
	private boolean resetSelected;

	private Button phantomsButton;
	private NightmareChanceSlider nightmareChanceSlider;
	private Button resetButton;

	public BbeConfigScreen(Screen parent) {
		super(Component.translatable("config.bedsbutendgame.title"));
		this.parent = parent;
		this.initialDisablePhantoms = ClientConfigState.disablePhantoms();
		this.initialNightmareChance = ClientConfigState.nightmareChance();
		this.pendingDisablePhantoms = initialDisablePhantoms;
		this.pendingNightmareChance = initialNightmareChance;
	}

	@Override
	protected void init() {
		int x = (this.width - BUTTON_WIDTH) / 2;

		this.phantomsButton = this.addRenderableWidget(Button.builder(
				disablePhantomsText(),
				button -> {
					pendingDisablePhantoms = !pendingDisablePhantoms;
					resetSelected = false;
					button.setMessage(disablePhantomsText());
				}
		).pos(x, 62).size(BUTTON_WIDTH, BUTTON_HEIGHT).build());

		this.nightmareChanceSlider = this.addRenderableWidget(new NightmareChanceSlider(
				x,
				86,
				BUTTON_WIDTH,
				BUTTON_HEIGHT,
				pendingNightmareChance,
				chance -> {
					pendingNightmareChance = chance;
					resetSelected = false;
				}
		));

		this.resetButton = this.addRenderableWidget(Button.builder(
				Component.translatable("config.bedsbutendgame.reset"),
				button -> resetToDefaults()
		).pos(x, 110).size(BUTTON_WIDTH, BUTTON_HEIGHT).build());

		this.addRenderableWidget(Button.builder(CommonComponents.GUI_DONE, button -> applyAndClose())
				.pos(x, this.height - 28).size(BUTTON_WIDTH, BUTTON_HEIGHT).build());

		boolean connected = this.minecraft.getConnection() != null;
		this.phantomsButton.active = connected;
		this.nightmareChanceSlider.active = connected;
		this.resetButton.active = connected;
	}

	@Override
	public void extractRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float delta) {
		super.extractRenderState(graphics, mouseX, mouseY, delta);
		graphics.centeredText(this.font, this.title, this.width / 2, 14, 0xFFFFFFFF);
	}

	@Override
	public void onClose() {
		this.minecraft.gui.setScreen(this.parent);
	}

	private void resetToDefaults() {
		pendingDisablePhantoms = BbeConfig.DEFAULT_DISABLE_PHANTOMS;
		pendingNightmareChance = BbeConfig.DEFAULT_NIGHTMARE_CHANCE;
		resetSelected = true;
		phantomsButton.setMessage(disablePhantomsText());
		nightmareChanceSlider.setChance(pendingNightmareChance);
	}

	private void applyAndClose() {
		if (this.minecraft.getConnection() != null) {
			if (resetSelected) {
				this.minecraft.getConnection().sendCommand("bbe config reset");
			} else {
				if (pendingDisablePhantoms != initialDisablePhantoms) {
					this.minecraft.getConnection().sendCommand(
							"bbe config disablePhantoms " + (pendingDisablePhantoms ? "on" : "off")
					);
				}
				if (pendingNightmareChance != initialNightmareChance) {
					this.minecraft.getConnection().sendCommand(
							"bbe config nightmareChance " + pendingNightmareChance
					);
				}
			}
		}
		this.minecraft.gui.setScreen(this.parent);
	}

	private Component disablePhantomsText() {
		return Component.translatable(
				"config.bedsbutendgame.option",
				Component.translatable("config.bedsbutendgame.disablePhantoms"),
				Component.translatable(pendingDisablePhantoms
						? "config.bedsbutendgame.on"
						: "config.bedsbutendgame.off")
		);
	}

	private static final class NightmareChanceSlider extends AbstractSliderButton {
		private final IntConsumer onChange;

		private NightmareChanceSlider(
				int x,
				int y,
				int width,
				int height,
				int chance,
				IntConsumer onChange
		) {
			super(x, y, width, height, Component.empty(), normalize(chance));
			this.onChange = onChange;
			updateMessage();
		}

		@Override
		protected void updateMessage() {
			setMessage(Component.translatable(
					"config.bedsbutendgame.nightmareChance",
					Component.literal(currentChance() + "%")
			));
		}

		@Override
		protected void applyValue() {
			int snappedChance = Math.max(0, Math.min(100, (int) Math.round(value * 20.0D) * 5));
			value = normalize(snappedChance);
			updateMessage();
			if (onChange != null) {
				onChange.accept(snappedChance);
			}
		}

		private void setChance(int chance) {
			value = normalize(chance);
			updateMessage();
		}

		private int currentChance() {
			return Math.max(0, Math.min(100, (int) Math.round(value * 100.0D)));
		}

		private static double normalize(int chance) {
			return Math.max(0, Math.min(100, chance)) / 100.0D;
		}
	}
}
