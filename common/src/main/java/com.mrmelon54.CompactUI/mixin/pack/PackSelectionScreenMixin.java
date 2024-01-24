package com.mrmelon54.CompactUI.mixin.pack;

import com.mrmelon54.CompactUI.duck.ResourcePackScreenDuckProvider;
import net.minecraft.client.gui.screens.packs.PackSelectionScreen;
import net.minecraft.client.gui.screens.packs.TransferableSelectionList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(PackSelectionScreen.class)
public class PackSelectionScreenMixin implements ResourcePackScreenDuckProvider {
    @Shadow
    private TransferableSelectionList selectedPackList;

    @Shadow
    private TransferableSelectionList availablePackList;

    @Override
    public TransferableSelectionList compact_ui$getSelectedPackListWidget() {
        return selectedPackList;
    }

    @Override
    public TransferableSelectionList compact_ui$getAvailablePackList() {
        return availablePackList;
    }
}
