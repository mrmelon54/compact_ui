package com.mrmelon54.CompactUI.duck;

import net.minecraft.client.gui.screens.packs.TransferableSelectionList;

public interface ResourcePackScreenDuckProvider {
    TransferableSelectionList compact_ui$getSelectedPackListWidget();

    TransferableSelectionList compact_ui$getAvailablePackList();
}
