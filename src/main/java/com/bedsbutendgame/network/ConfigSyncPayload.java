package com.bedsbutendgame.network;

import com.bedsbutendgame.BedsButEndgame;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

public record ConfigSyncPayload(
		boolean disablePhantoms,
		boolean nightmares
) implements CustomPacketPayload {
	public static final Type<ConfigSyncPayload> TYPE = new Type<>(BedsButEndgame.id("config_sync"));
	public static final StreamCodec<RegistryFriendlyByteBuf, ConfigSyncPayload> CODEC = StreamCodec.composite(
			ByteBufCodecs.BOOL,
			ConfigSyncPayload::disablePhantoms,
			ByteBufCodecs.BOOL,
			ConfigSyncPayload::nightmares,
			ConfigSyncPayload::new
	);

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}
}
