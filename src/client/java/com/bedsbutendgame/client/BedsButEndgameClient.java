package com.bedsbutendgame.client;

import com.bedsbutendgame.network.ConfigSyncPayload;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;

public final class BedsButEndgameClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		ClientPlayNetworking.registerGlobalReceiver(ConfigSyncPayload.TYPE, (payload, context) ->
				ClientConfigState.update(
						payload.disablePhantoms(),
						payload.nightmareChance()
				)
		);
	}
}
