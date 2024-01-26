package com.mrmelon54.CompactUI.mixin.server;

import net.minecraft.client.gui.screens.multiplayer.ServerSelectionList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(ServerSelectionList.Entry.class)
public class ServerEntryMixin {
    @Unique
    private static final int ONE_THIRD = 10;
    @Unique
    private static final int TWO_THIRD = 21;

    // TODO: compact server list entries
}
