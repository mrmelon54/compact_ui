package com.mrmelon54.CompactUI.mixin.server;

import net.minecraft.client.gui.screens.multiplayer.ServerSelectionList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(ServerSelectionList.class)
public class ServerSelectionListMixin {
    @ModifyVariable(method = "<init>", at = @At("HEAD"), ordinal = 3, argsOnly = true)
    static private int injectedEntryHeight(int entryHeight) {
        return (entryHeight - 4) / 3 + 4;
    }
}
