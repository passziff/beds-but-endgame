package com.bedsbutendgame.mixin;

import com.bedsbutendgame.config.ConfigManager;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.levelgen.PhantomSpawner;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PhantomSpawner.class)
public abstract class PhantomSpawnerMixin {
	@Inject(method = "tick", at = @At("HEAD"), cancellable = true)
	private void bedsbutendgame$disableInsomniaPhantoms(
			ServerLevel level,
			boolean spawnEnemies,
			CallbackInfo ci
	) {
		if (ConfigManager.disablePhantoms()) {
			ci.cancel();
		}
	}
}
