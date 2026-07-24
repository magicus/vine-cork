/*
 * Copyright © Magnus Ihse Bursie 2026.
 * This file is released under LGPLv3. See LICENSE for full license details.
 */
package se.icus.mag.vinecork;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.BlockHitResult;

public class VineCorkMod implements ModInitializer {
    @Override
    public void onInitialize() {
        UseBlockCallback.EVENT.register(VineCorkMod::onUseBlock);
    }

    private static InteractionResult onUseBlock(
            Player player, Level level, InteractionHand hand, BlockHitResult hitResult) {
        if (level.isClientSide() || player.isSpectator()) return InteractionResult.PASS;

        ItemStack stack = player.getItemInHand(hand);
        if (!stack.is(Items.SHEARS)) return InteractionResult.PASS;

        BlockPos pos = hitResult.getBlockPos();
        if (!level.getBlockState(pos).is(Blocks.VINE)) return InteractionResult.PASS;

        ServerLevel serverLevel = (ServerLevel) level;
        VineCorkSavedData savedData = VineCorkSavedData.get(serverLevel);
        if (!savedData.markCorked(pos)) return InteractionResult.PASS;

        serverLevel.playSound(null, pos, SoundEvents.SHEARS_SNIP, SoundSource.BLOCKS, 1.0F, 1.0F);
        stack.hurtAndBreak(1, player, hand.asEquipmentSlot());
        return InteractionResult.SUCCESS;
    }
}
