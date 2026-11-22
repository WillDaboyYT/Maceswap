package com.willdaboy.maceswap;

import net.fabricmc.api.ClientModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class MaceSwapClient implements ClientModInitializer {
    public static final String MOD_ID = "maceswap";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitializeClient() {
        LOGGER.info("MaceSwap loaded. Axe: hotbar slot 4; mace: hotbar slot 5.");
    }
}
