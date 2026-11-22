package com.willdaboy.maceswap.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.entity.player.PlayerInventory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MinecraftClient.class)
public abstract class MinecraftClientMixin {
    @Unique
    private boolean maceswap$inSequence;

    @Inject(method = "doAttack", at = @At("HEAD"), cancellable = true)
    private void maceswap$swapAttack(CallbackInfoReturnable<Boolean> cir) {
        MinecraftClient client = (MinecraftClient) (Object) this;

        if (maceswap$inSequence || client.player == null || client.interactionManager == null) {
            return;
        }

        PlayerInventory inventory = client.player.getInventory();
        PlayerInventoryAccessor slotAccess = (PlayerInventoryAccessor) inventory;
        int originalSlot = slotAccess.maceswap$getSelectedSlot();

        // Hotbar slots are zero-indexed: sword = slot 2 (index 1),
        // axe = slot 4 (index 3), mace = slot 5 (index 4).
        boolean holdingMace = originalSlot == 4
                && client.player.getMainHandStack().isOf(Items.MACE);
        boolean holdingSword = originalSlot == 1
                && client.player.getMainHandStack().isIn(ItemTags.SWORDS);

        if (!holdingMace && !holdingSword) {
            return;
        }

        ItemStack axe = inventory.getStack(3);
        if (!axe.isIn(ItemTags.AXES)) {
            return;
        }

        maceswap$inSequence = true;
        try {
            slotAccess.maceswap$setSelectedSlot(3);
            ((MinecraftClientInvoker) (Object) client).maceswap$invokeDoAttack();

            slotAccess.maceswap$setSelectedSlot(originalSlot);
            ((MinecraftClientInvoker) (Object) client).maceswap$invokeDoAttack();
        } finally {
            slotAccess.maceswap$setSelectedSlot(originalSlot);
            maceswap$inSequence = false;
        }

        cir.setReturnValue(true);
    }
}
