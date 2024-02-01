package com.mrmelon54.CompactUI.mixin.world;

import com.llamalad7.mixinextras.injector.WrapWithCondition;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.worldselection.WorldSelectionList;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(WorldSelectionList.WorldListEntry.class)
public class WorldListEntryMixin {
    @Unique
    private static final int ONE_THIRD = 10;
    @Unique
    private static final int TWO_THIRD = 21;

    @Redirect(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphics;drawString(Lnet/minecraft/client/gui/Font;Ljava/lang/String;IIIZ)I", ordinal = 0))
    private int moveRenderWorldName(GuiGraphics instance, Font font, String string, int i, int j, int k, boolean bl) {
        return instance.drawString(font, string, i - TWO_THIRD, j, k, bl);
    }

    @Redirect(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphics;blit(Lnet/minecraft/resources/ResourceLocation;IIFFIIII)V", ordinal = 0))
    private void moveRenderIcon(GuiGraphics instance, ResourceLocation resourceLocation, int i, int j, float f, float g, int k, int l, int m, int n) {
        instance.blit(resourceLocation, i, j, f, g, ONE_THIRD, ONE_THIRD, ONE_THIRD, ONE_THIRD);
    }

    @Redirect(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphics;fill(IIIII)V", ordinal = 0))
    private void moveRenderBg(GuiGraphics instance, int i, int j, int k, int l, int m) {
        instance.fill(i, j, k - TWO_THIRD - 1, l - TWO_THIRD - 1, m);
    }

    @Redirect(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphics;blitSprite(Lnet/minecraft/resources/ResourceLocation;IIII)V"))
    private void moveRenderBlitSprite(GuiGraphics instance, ResourceLocation resourceLocation, int i, int j, int k, int l) {
        instance.blitSprite(resourceLocation, i, j, k - TWO_THIRD - 1, l - TWO_THIRD - 1);
    }

    @WrapWithCondition(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphics;drawString(Lnet/minecraft/client/gui/Font;Ljava/lang/String;IIIZ)I", ordinal = 1))
    private boolean disableRenderLine2(GuiGraphics instance, Font font, String string, int i, int j, int k, boolean bl) {
        return false;
    }

    @WrapWithCondition(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphics;drawString(Lnet/minecraft/client/gui/Font;Lnet/minecraft/network/chat/Component;IIIZ)I", ordinal = 0))
    private boolean disableRenderLine3(GuiGraphics instance, Font font, Component component, int i, int j, int k, boolean bl) {
        return false;
    }

    @ModifyConstant(method = "mouseClicked", constant = @Constant(doubleValue = 32.0))
    private double injectedMouseClicked(double constant) {
        return ONE_THIRD;
    }
}
