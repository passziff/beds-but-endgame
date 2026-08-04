package com.bedsbutendgame.mixin;

import com.bedsbutendgame.sleep.BedsideTableSleepCheck;
import com.mojang.datafixers.util.Either;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Unit;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ServerPlayer.class)
public abstract class ServerPlayerMixin {
	@Inject(
			method = "startSleepInBed",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/server/level/ServerPlayer;setRespawnPosition(Lnet/minecraft/server/level/ServerPlayer$RespawnConfig;Z)V",
					shift = At.Shift.AFTER
			),
			cancellable = true
	)
	private void bedsbutendgame$requireBedsideTable(
			BlockPos bedPos,
			CallbackInfoReturnable<Either<Player.BedSleepingProblem, Unit>> cir
	) {
		ServerPlayer player = (ServerPlayer) (Object) this;
		if (!BedsideTableSleepCheck.hasBedsideTable(player.level(), bedPos)) {
			cir.setReturnValue(Either.left(new Player.BedSleepingProblem(
					Component.translatable("sleep.bedsbutendgame.missing_bedside_table")
			)));
		}
	}
}
