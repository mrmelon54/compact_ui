package com.mrmelon54.CompactUI.mixin.world;

import com.mrmelon54.CompactUI.duck.SingleplayerScreenDuckProvider;
import net.minecraft.client.gui.screens.worldselection.SelectWorldScreen;
import net.minecraft.client.gui.screens.worldselection.WorldSelectionList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(SelectWorldScreen.class)
public class SelectWorldScreenMixin implements SingleplayerScreenDuckProvider {
    @Shadow
    private WorldSelectionList list;

    @Override
    public WorldSelectionList compact_ui$getWorldListWidget() {
        return this.list;
    }
}
