package com.willdaboy.maceswap.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.util.Hand;
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

        if (maceswap$inSequence
                || client.player == null
                || client.interactionManager == null
                || client.player.getInventory().selectedSlot != 4
                || !client.player.getMainHandStack().isOf(Items.MACE)) {
            return;
        }

        ItemStack axe = client.player.getInventory().getStack(3);
        if (!axe.isIn(ItemTags.AXES)) {
            return;
        }

        maceswap$inSequence = true;
        boolean originalSlot = false;
        try {
            // Hotbar slots are zero-indexed in code: slot 4 is index 3, slot 5 is index 4.
            client.player.getInventory().selectedSlot = 3;
            ((MinecraftClientInvoker) (Object) client).maceswap$invokeDoAttack();

            client.player.getInventory().selectedSlot = 4;
            ((MinecraftClientInvoker) (Object) client).maceswap$invokeDoAttack();
            originalSlot = true;
        } finally {
            client.player.getInventory().selectedSlot = 4;
            maceswap$inSequence = false;
        }

        cir.setReturnValue(originalSlot);
    }
}
