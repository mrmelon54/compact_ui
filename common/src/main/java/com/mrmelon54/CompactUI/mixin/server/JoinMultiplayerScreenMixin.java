package com.mrmelon54.CompactUI.mixin.server;

import com.mrmelon54.CompactUI.duck.MultiplayerScreenDuckProvider;
import net.minecraft.client.gui.screens.multiplayer.JoinMultiplayerScreen;
import net.minecraft.client.gui.screens.multiplayer.ServerSelectionList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(JoinMultiplayerScreen.class)
public class JoinMultiplayerScreenMixin implements MultiplayerScreenDuckProvider {

    @Shadow
    protected ServerSelectionList serverSelectionList;

    @Override
    public ServerSelectionList compact_ui$getServerListWidget() {
        return serverSelectionList;
    }
}
