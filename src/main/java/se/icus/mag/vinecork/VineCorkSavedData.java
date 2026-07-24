/*
 * Copyright © Magnus Ihse Bursie 2026.
 * This file is released under LGPLv3. See LICENSE for full license details.
 */
package se.icus.mag.vinecork;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.longs.LongIterator;
import it.unimi.dsi.fastutil.longs.LongOpenHashSet;
import it.unimi.dsi.fastutil.longs.LongSet;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.datafix.DataFixTypes;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.saveddata.SavedDataType;

public final class VineCorkSavedData extends SavedData {
    private static final Codec<VineCorkSavedData> CODEC =
            RecordCodecBuilder.create(instance -> instance.group(Codec.LONG
                            .listOf()
                            .optionalFieldOf("positions", List.of())
                            .forGetter(VineCorkSavedData::positionsForCodec))
                    .apply(instance, VineCorkSavedData::new));

    private static final SavedDataType<VineCorkSavedData> TYPE = new SavedDataType<>(
            Identifier.fromNamespaceAndPath("vine-cork", "corked_vines"),
            VineCorkSavedData::new,
            CODEC,
            DataFixTypes.LEVEL);

    private final LongSet corkedPositions;

    private VineCorkSavedData() {
        this.corkedPositions = new LongOpenHashSet();
    }

    private VineCorkSavedData(List<Long> positions) {
        this.corkedPositions = new LongOpenHashSet();
        for (long position : positions) {
            this.corkedPositions.add(position);
        }
    }

    public boolean isCorked(BlockPos pos) {
        return this.corkedPositions.contains(pos.asLong());
    }

    public boolean markCorked(BlockPos pos) {
        if (this.corkedPositions.add(pos.asLong())) {
            this.setDirty();
            return true;
        }

        return false;
    }

    public void clearCorked(BlockPos pos) {
        if (this.corkedPositions.remove(pos.asLong())) {
            this.setDirty();
        }
    }

    public static VineCorkSavedData get(ServerLevel level) {
        return level.getDataStorage().computeIfAbsent(TYPE);
    }

    private List<Long> positionsForCodec() {
        List<Long> positions = new ArrayList<>(this.corkedPositions.size());
        LongIterator iterator = this.corkedPositions.iterator();
        while (iterator.hasNext()) {
            positions.add(iterator.nextLong());
        }

        return positions;
    }
}
