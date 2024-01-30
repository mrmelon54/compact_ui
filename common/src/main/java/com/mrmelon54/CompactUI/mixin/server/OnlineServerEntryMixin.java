package com.mrmelon54.CompactUI.mixin.server;

import com.mrmelon54.CompactUI.CompactUI;
import com.mrmelon54.CompactUI.duck.MultiplayerScreenDuckProvider;
import net.minecraft.Util;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.multiplayer.JoinMultiplayerScreen;
import net.minecraft.client.gui.screens.multiplayer.ServerSelectionList;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSequence;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(ServerSelectionList.OnlineServerEntry.class)
public abstract class OnlineServerEntryMixin {
    @Shadow
    protected abstract boolean canJoin();

    @Shadow
    @Final
    private JoinMultiplayerScreen screen;

    @Shadow
    protected abstract void swap(int i, int j);

    @Shadow
    private long lastClickTime;
    @Unique
    private static final ResourceLocation JOIN_HIGHLIGHTED_SPRITE = new ResourceLocation(CompactUI.MOD_ID, "transferable_list/select_highlighted");
    @Unique
    private static final ResourceLocation JOIN_SPRITE = new ResourceLocation(CompactUI.MOD_ID, "transferable_list/select");
    @Unique
    private static final ResourceLocation MOVE_UP_HIGHLIGHTED_SPRITE = new ResourceLocation(CompactUI.MOD_ID, "transferable_list/move_up_highlighted");
    @Unique
    private static final ResourceLocation MOVE_UP_SPRITE = new ResourceLocation(CompactUI.MOD_ID, "transferable_list/move_up");
    @Unique
    private static final ResourceLocation MOVE_DOWN_HIGHLIGHTED_SPRITE = new ResourceLocation(CompactUI.MOD_ID, "transferable_list/move_down_highlighted");
    @Unique
    private static final ResourceLocation MOVE_DOWN_SPRITE = new ResourceLocation(CompactUI.MOD_ID, "transferable_list/move_down");

    @Unique
    private static final int ONE_THIRD = 10;
    @Unique
    private static final int TWO_THIRD = 21;

    @Redirect(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/Font;split(Lnet/minecraft/network/chat/FormattedText;I)Ljava/util/List;"))
    private List<FormattedCharSequence> redirectMotd(Font instance, FormattedText formattedText, int i) {
        return List.of();
    }

    @Redirect(method = "drawIcon", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphics;blit(Lnet/minecraft/resources/ResourceLocation;IIFFIIII)V"))
    private void redirectDrawIconBlit(GuiGraphics instance, ResourceLocation resourceLocation, int i, int j, float f, float g, int k, int l, int m, int n) {
        instance.blit(resourceLocation, i + TWO_THIRD, j, f, g, ONE_THIRD, ONE_THIRD, ONE_THIRD, ONE_THIRD);
    }

    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphics;fill(IIIII)V"), cancellable = true)
    private void injectedRenderTail(GuiGraphics guiGraphics, int i, int j, int k, int l, int m, int n, int o, boolean bl, float f, CallbackInfo ci) {
        guiGraphics.fill(k + TWO_THIRD, j, k + 31, j + ONE_THIRD, 0xa0909090);
        int u = n - k;
        if (this.canJoin())
            guiGraphics.blitSprite(u < 31 && u > TWO_THIRD ? JOIN_HIGHLIGHTED_SPRITE : JOIN_SPRITE, k + TWO_THIRD, j, ONE_THIRD, ONE_THIRD);
        if (i > 0)
            guiGraphics.blitSprite(u < ONE_THIRD ? MOVE_UP_HIGHLIGHTED_SPRITE : MOVE_UP_SPRITE, k, j, ONE_THIRD, ONE_THIRD);
        if (i < this.screen.getServers().size() - 1)
            guiGraphics.blitSprite(u < TWO_THIRD && u > ONE_THIRD ? MOVE_DOWN_HIGHLIGHTED_SPRITE : MOVE_DOWN_SPRITE, k + ONE_THIRD, j, ONE_THIRD, ONE_THIRD);

        ci.cancel();
    }

    @Inject(method = "mouseClicked", at = @At("HEAD"), cancellable = true)
    private void injectedMouseClicked(double mouseX, double mouseY, int button, CallbackInfoReturnable<Boolean> cir) {
        if (this.screen instanceof MultiplayerScreenDuckProvider duck) {
            double d = mouseX - (double) duck.compact_ui$getServerListWidget().getRowLeft();
            if (d <= 32) {
                if (d < 32 && d > TWO_THIRD && this.canJoin()) {
                    this.screen.setSelected((ServerSelectionList.OnlineServerEntry) (Object) this);
                    this.screen.joinSelectedServer();
                    cir.setReturnValue(true);
                    cir.cancel();
                    return;
                }

                int i = duck.compact_ui$getServerListWidget().children().indexOf((ServerSelectionList.OnlineServerEntry) (Object) this);
                if (d < ONE_THIRD && i > 0) {
                    this.swap(i, i - 1);
                    cir.setReturnValue(true);
                    cir.cancel();
                    return;
                }
                if (d < TWO_THIRD && d > ONE_THIRD && i < this.screen.getServers().size() - 1) {
                    this.swap(i, i + 1);
                    cir.setReturnValue(true);
                    cir.cancel();
                    return;
                }
            }
        }

        this.screen.setSelected((ServerSelectionList.OnlineServerEntry) (Object) this);
        if (Util.getMillis() - this.lastClickTime < 250L) {
            this.screen.joinSelectedServer();
        }

        this.lastClickTime = Util.getMillis();
        cir.setReturnValue(false);
        cir.cancel();
    }
}
