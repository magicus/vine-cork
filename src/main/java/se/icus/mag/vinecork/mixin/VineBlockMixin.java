/*
 * Copyright © Magnus Ihse Bursie 2026.
 * This file is released under LGPLv3. See LICENSE for full license details.
 */
package se.icus.mag.vinecork.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.VineBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import se.icus.mag.vinecork.VineCorkSavedData;

@Mixin(VineBlock.class)
public abstract class VineBlockMixin {
    @Inject(method = "randomTick", at = @At("HEAD"), cancellable = true)
    private void vineCork$cancelRandomTickIfCorked(
            BlockState state, ServerLevel level, BlockPos pos, RandomSource random, CallbackInfo ci) {
        if (VineCorkSavedData.get(level).isCorked(pos)) {
            ci.cancel();
        }
    }
}
