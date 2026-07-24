/*
 * Copyright © Magnus Ihse Bursie 2026.
 * This file is released under LGPLv3. See LICENSE for full license details.
 */
package se.icus.mag.vinecork.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.VineBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import se.icus.mag.vinecork.VineCorkSavedData;

@Mixin(BlockBehaviour.class)
public abstract class BlockBehaviourMixin {
    @Inject(method = "onPlace", at = @At("TAIL"))
    private void vineCork$clearMarkerOnPlace(
            BlockState state, Level level, BlockPos pos, BlockState oldState, boolean movedByPiston, CallbackInfo ci) {
        if (!(level instanceof ServerLevel serverLevel)) return;

        BlockBehaviour block = (BlockBehaviour) (Object) this;
        if (!(block instanceof VineBlock)) return;

        VineCorkSavedData.get(serverLevel).clearCorked(pos);
    }

    @Inject(method = "affectNeighborsAfterRemoval", at = @At("HEAD"))
    private void vineCork$clearMarkerOnRemoval(
            BlockState state, ServerLevel level, BlockPos pos, boolean movedByPiston, CallbackInfo ci) {
        BlockBehaviour block = (BlockBehaviour) (Object) this;
        if (!(block instanceof VineBlock)) return;

        VineCorkSavedData.get(level).clearCorked(pos);
    }
}
