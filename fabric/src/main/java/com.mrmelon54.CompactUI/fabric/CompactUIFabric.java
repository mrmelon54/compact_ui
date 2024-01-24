package com.mrmelon54.CompactUI.fabric;

import com.mrmelon54.CompactUI.CompactUI;
import net.fabricmc.api.ModInitializer;

public class CompactUIFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        CompactUI.init();
    }
}
