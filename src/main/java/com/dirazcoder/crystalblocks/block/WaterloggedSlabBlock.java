/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2026 DirazCoder
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package com.dirazcoder.crystalblocks.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.SlabType;
import net.minecraft.world.item.context.BlockPlaceContext;

// apparently SlabBlock doesn't even waterlog on its own, very stupid, but
// fixed up here. vanilla slabs like stone/cobble implement
// SimpleWaterloggedBlock themselves, SlabBlock just doesn't. can't edit
// SlabBlock so this subclass bolts the interface on and wires up the 3
// methods it actually needs: what fluid's here, what state to place with
// (so it starts wet if placed underwater), and what to do on a neighbor
// update (so the water keeps flowing/dries out properly)
public class WaterloggedSlabBlock extends SlabBlock implements SimpleWaterloggedBlock {

    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

    public WaterloggedSlabBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(TYPE, SlabType.BOTTOM)
                .setValue(WATERLOGGED, false));
    }

    // ngl first version of this had an extra builder.add(WATERLOGGED) in a
    // createBlockStateDefinition override here, turns out SlabBlock's own
    // super call already adds WATERLOGGED (every slab tracks it whether or
    // not it can actually hold water), so adding it twice threw "duplicate
    // property" and crashed the game on launch. don't need the override at
    // all, deleted it

    @Override
    public FluidState getFluidState(BlockState state) {
        return state.getValue(WATERLOGGED)
                ? Fluids.WATER.getSource(false)
                : super.getFluidState(state);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockState state = super.getStateForPlacement(context);
        if (state == null) {
            return null;
        }
        // double slab = two halves merged into one full block, can't hold water
        boolean isDouble = state.getValue(TYPE) == SlabType.DOUBLE;
        boolean placedInWater = context.getLevel().getFluidState(context.getClickedPos()).getType() == Fluids.WATER;
        return state.setValue(WATERLOGGED, !isDouble && placedInWater);
    }

    @Override
    public BlockState updateShape(BlockState state, Direction direction, BlockState neighborState,
                                   LevelAccessor level, BlockPos pos, BlockPos neighborPos) {
        if (state.getValue(WATERLOGGED)) {
            level.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
        }
        return super.updateShape(state, direction, neighborState, level, pos, neighborPos);
    }
}
