package com.mrmelon54.CompactUI.mixin.pack;

import net.minecraft.client.gui.screens.packs.TransferableSelectionList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(TransferableSelectionList.class)
public class TransferableSelectionListMixin {
    @ModifyConstant(method = "<init>", constant = @Constant(intValue = 36))
    private static int injectedItemHeight(int constant) {
        return (constant - 4) / 3 + 4;
    }
}
