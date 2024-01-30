package com.mrmelon54.CompactUI.mixin.pack;

import com.mrmelon54.CompactUI.CompactUI;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.ObjectSelectionList;
import net.minecraft.client.gui.screens.packs.PackSelectionModel;
import net.minecraft.client.gui.screens.packs.TransferableSelectionList;
import net.minecraft.client.gui.screens.packs.TransferableSelectionList.PackEntry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.repository.PackCompatibility;
import net.minecraft.util.FormattedCharSequence;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PackEntry.class)
public abstract class PackEntryMixin extends ObjectSelectionList.Entry<PackEntry> {
    @Unique
    private static final ResourceLocation SELECT_HIGHLIGHTED_SPRITE = new ResourceLocation(CompactUI.MOD_ID, "transferable_list/select_highlighted");
    @Unique
    private static final ResourceLocation SELECT_SPRITE = new ResourceLocation(CompactUI.MOD_ID, "transferable_list/select");
    @Unique
    private static final ResourceLocation UNSELECT_HIGHLIGHTED_SPRITE = new ResourceLocation(CompactUI.MOD_ID, "transferable_list/unselect_highlighted");
    @Unique
    private static final ResourceLocation UNSELECT_SPRITE = new ResourceLocation(CompactUI.MOD_ID, "transferable_list/unselect");
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

    @Shadow
    @Final
    private PackSelectionModel.Entry pack;

    @Shadow
    @Final
    private FormattedCharSequence nameDisplayCache;

    @Shadow
    protected abstract boolean showHoverOverlay();

    @Shadow
    @Final
    protected Minecraft minecraft;

    @Shadow
    @Final
    private TransferableSelectionList parent;

    @Shadow
    @Final
    private FormattedCharSequence incompatibleNameDisplayCache;

    @Shadow
    protected abstract boolean handlePackSelection();

    @Inject(method = "render", at = @At("HEAD"), cancellable = true)
    private void compactRender(GuiGraphics guiGraphics, int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean hovered, float tickDelta, CallbackInfo ci) {
        PackCompatibility compatibility = this.pack.getCompatibility();
        if (compact_ui$notCompatible(compatibility)) {
            guiGraphics.fill(x - 1, y - 1, x + entryWidth - 9, y + entryHeight + 1, 0xff770000);
        }

        guiGraphics.blit(this.pack.getIconTexture(), this.pack.canSelect() ? x : x + TWO_THIRD, y, 0f, 0f, ONE_THIRD, ONE_THIRD, ONE_THIRD, ONE_THIRD);
        FormattedCharSequence name = this.nameDisplayCache;

        //noinspection ConstantValue
        if (this.showHoverOverlay() && (this.minecraft.options.touchscreen().get() || hovered || this.parent.getSelected() == (Object) this && this.parent.isFocused())) {
            guiGraphics.fill(this.pack.canSelect() ? x : x + TWO_THIRD, y, x + (this.pack.canSelect() ? ONE_THIRD : 31), y + ONE_THIRD, 0xa0909090);
            int relX = mouseX - x;
            int relY = mouseY - y;
            if (compact_ui$notCompatible(compatibility)) {
                name = this.incompatibleNameDisplayCache;
            }

            if (this.pack.canSelect()) {
                guiGraphics.blitSprite(relX < ONE_THIRD ? SELECT_HIGHLIGHTED_SPRITE : SELECT_SPRITE, x, y, ONE_THIRD, ONE_THIRD);
            } else {
                if (this.pack.canUnselect()) guiGraphics.blitSprite(relX < ONE_THIRD ? UNSELECT_HIGHLIGHTED_SPRITE : UNSELECT_SPRITE, x, y, ONE_THIRD, ONE_THIRD);
                if (this.pack.canMoveUp()) guiGraphics.blitSprite(relX < TWO_THIRD && relX > ONE_THIRD ? MOVE_UP_HIGHLIGHTED_SPRITE : MOVE_UP_SPRITE, x + ONE_THIRD, y, ONE_THIRD, ONE_THIRD);
                if (this.pack.canMoveDown()) guiGraphics.blitSprite(relX < 32 && relX > TWO_THIRD ? MOVE_DOWN_HIGHLIGHTED_SPRITE : MOVE_DOWN_SPRITE, x + TWO_THIRD, y, ONE_THIRD, ONE_THIRD);
            }
        }
        guiGraphics.drawString(this.minecraft.font, name, x + (this.pack.canSelect() ? ONE_THIRD : 32) + 2, y + 1, 0xffffff);
        ci.cancel();
    }

    @Inject(method = "mouseClicked", at = @At("HEAD"), cancellable = true)
    private void compactMouseClicked(double mouseX, double mouseY, int button, CallbackInfoReturnable<Boolean> cir) {
        this.parent.getRowLeft();
        double f = mouseX - (double) this.parent.getRowLeft();
        double g = mouseY - (double) this.parent.getRowTop(this.parent.children().indexOf((PackEntry) (Object) this));
        if (this.showHoverOverlay()) {
            this.parent.screen.clearSelected();
            if (f <= ONE_THIRD && this.pack.canSelect()) {
                this.handlePackSelection();
                cir.setReturnValue(true);
                return;
            }

            if (f <= ONE_THIRD && this.pack.canUnselect()) {
                this.pack.unselect();
                cir.setReturnValue(true);
                return;
            }

            if (f > ONE_THIRD && f <= TWO_THIRD && this.pack.canMoveUp()) {
                this.pack.moveUp();
                cir.setReturnValue(true);
                return;
            }

            if (f > TWO_THIRD && g <= 32 && this.pack.canMoveDown()) {
                this.pack.moveDown();
                cir.setReturnValue(true);
            }
        }
    }

    @Unique
    private static boolean compact_ui$notCompatible(PackCompatibility compatibility) {
        return !compact_ui$isCompatible(compatibility);
    }

    @Unique
    private static boolean compact_ui$isCompatible(PackCompatibility compatibility) {
        return CompactUI.hasNRPW() || compatibility.isCompatible();
    }
}
