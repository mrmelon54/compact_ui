package com.mrmelon54.CompactUI.forge;

import dev.architectury.platform.forge.EventBuses;
import com.mrmelon54.CompactUI.CompactUI;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(CompactUI.MOD_ID)
public class CompactUIForge {
    public CompactUIForge() {
        // Submit our event bus to let architectury register our content on the right time
        EventBuses.registerModEventBus(CompactUI.MOD_ID, FMLJavaModLoadingContext.get().getModEventBus());
        CompactUI.init();
    }
}
