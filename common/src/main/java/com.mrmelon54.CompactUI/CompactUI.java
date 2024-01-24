package com.mrmelon54.CompactUI;

import net.fabricmc.loader.api.FabricLoader;

public class CompactUI {
    public static final String MOD_ID = "compact_ui";

    public static void init() {
    }

    public static boolean hasNRPW() {
        return FabricLoader.getInstance().isModLoaded("no-resource-pack-warnings");
    }
}
