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

import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.BlockFence;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.BlockWall;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

// unlike CrystalBlockStairs, this can't just call up to a vanilla
// constructor with different args - BlockFenceGate's only constructor
// takes a BlockPlanks.EnumType, which is wood-specific and has no path
// to a plain Material/color. the entire class body below is the actual
// vanilla BlockFenceGate implementation, re-parented onto BlockHorizontal
// with the wood-type constructor swapped for Material - this is the
// standard, community-verified way to make a non-wood fence gate in
// 1.12.2, since Forge never added a hook for it
public class CrystalBlockFenceGate extends BlockHorizontal {

    public static final PropertyBool OPEN = PropertyBool.create("open");
    public static final PropertyBool POWERED = PropertyBool.create("powered");
    public static final PropertyBool IN_WALL = PropertyBool.create("in_wall");

    protected static final AxisAlignedBB AABB_COLLIDE_Z_AXIS = new AxisAlignedBB(0.0D, 0.0D, 0.375D, 1.0D, 1.0D, 0.625D);
    protected static final AxisAlignedBB AABB_COLLIDE_X_AXIS = new AxisAlignedBB(0.375D, 0.0D, 0.0D, 0.625D, 1.0D, 1.0D);
    protected static final AxisAlignedBB AABB_COLLIDE_Z_AXIS_IN_WALL = new AxisAlignedBB(0.0D, 0.0D, 0.375D, 1.0D, 0.8125D, 0.625D);
    protected static final AxisAlignedBB AABB_COLLIDE_X_AXIS_IN_WALL = new AxisAlignedBB(0.375D, 0.0D, 0.0D, 0.625D, 0.8125D, 1.0D);
    protected static final AxisAlignedBB AABB_CLOSED_SELECTED_Z_AXIS = new AxisAlignedBB(0.0D, 0.0D, 0.375D, 1.0D, 1.5D, 0.625D);
    protected static final AxisAlignedBB AABB_CLOSED_SELECTED_X_AXIS = new AxisAlignedBB(0.375D, 0.0D, 0.0D, 0.625D, 1.5D, 1.0D);

    public CrystalBlockFenceGate(Material material) {
        super(material);
        setSoundType(SoundType.STONE);
        setDefaultState(
            blockState.getBaseState()
                .withProperty(OPEN, false)
                .withProperty(POWERED, false)
                .withProperty(IN_WALL, false));
        useNeighborBrightness = true;
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        state = getActualState(state, source, pos);
        boolean inWall = state.getValue(IN_WALL);
        boolean zAxis = state.getValue(FACING)
            .getAxis() == EnumFacing.Axis.X;
        if (inWall) {
            return zAxis ? AABB_COLLIDE_X_AXIS_IN_WALL : AABB_COLLIDE_Z_AXIS_IN_WALL;
        }
        return zAxis ? AABB_COLLIDE_X_AXIS : AABB_COLLIDE_Z_AXIS;
    }

    // sets IN_WALL when a wall sits on either side along the gate's axis
    // - the same detection vanilla's wood gates use, just renamed away
    // from the private canFenceGateConnectTo vanilla keeps on
    // BlockFenceGate itself, since we don't have access to that
    @Override
    public IBlockState getActualState(IBlockState state, IBlockAccess world, BlockPos pos) {
        EnumFacing.Axis axis = state.getValue(FACING)
            .getAxis();
        boolean neighborIsWall = axis == EnumFacing.Axis.Z
            ? (canConnectTo(world, pos, EnumFacing.WEST) || canConnectTo(world, pos, EnumFacing.EAST))
            : (canConnectTo(world, pos, EnumFacing.NORTH) || canConnectTo(world, pos, EnumFacing.SOUTH));
        return neighborIsWall ? state.withProperty(IN_WALL, true) : state;
    }

    private boolean canConnectTo(IBlockAccess world, BlockPos pos, EnumFacing facing) {
        // this specifically checks "is the neighbor a wall", not general
        // fence connectivity - routing this through canBeConnectedTo()
        // used to accidentally match plain fences too (since fences can
        // connect to other fences), which dropped the gate into the
        // shorter in_wall model whenever a fence sat next to it instead
        // of only when an actual wall did
        Block block = world.getBlockState(pos.offset(facing))
            .getBlock();
        return block instanceof BlockWall;
    }

    @Override
    public boolean canBeConnectedTo(IBlockAccess world, BlockPos pos, EnumFacing facing) {
        Block connector = world.getBlockState(pos.offset(facing))
            .getBlock();
        return connector instanceof BlockFence || connector instanceof BlockWall;
    }

    @Override
    public IBlockState withRotation(IBlockState state, Rotation rotation) {
        return state.withProperty(FACING, rotation.rotate(state.getValue(FACING)));
    }

    @Override
    public IBlockState withMirror(IBlockState state, Mirror mirror) {
        return state.withRotation(mirror.toRotation(state.getValue(FACING)));
    }

    @Override
    public boolean canPlaceBlockAt(World world, BlockPos pos) {
        return world.getBlockState(pos.down())
            .getMaterial()
            .isSolid() && super.canPlaceBlockAt(world, pos);
    }

    @Nullable
    @Override
    public AxisAlignedBB getCollisionBoundingBox(IBlockState state, IBlockAccess world, BlockPos pos) {
        if (state.getValue(OPEN)) {
            return NULL_AABB;
        }
        return state.getValue(FACING)
            .getAxis() == EnumFacing.Axis.Z ? AABB_CLOSED_SELECTED_Z_AXIS : AABB_CLOSED_SELECTED_X_AXIS;
    }

    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    @Override
    public boolean isFullCube(IBlockState state) {
        return false;
    }

    @Override
    public boolean isPassable(IBlockAccess world, BlockPos pos) {
        return world.getBlockState(pos)
            .getValue(OPEN);
    }

    @Override
    public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY,
        float hitZ, int meta, EntityLivingBase placer) {
        boolean powered = world.isBlockPowered(pos);
        return getDefaultState().withProperty(FACING, placer.getHorizontalFacing())
            .withProperty(OPEN, powered)
            .withProperty(POWERED, powered)
            .withProperty(IN_WALL, false);
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player,
        EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (state.getValue(OPEN)) {
            state = state.withProperty(OPEN, false);
            world.setBlockState(pos, state, 10);
        } else {
            EnumFacing playerFacing = EnumFacing.fromAngle(player.rotationYaw);
            if (state.getValue(FACING) == playerFacing.getOpposite()) {
                state = state.withProperty(FACING, playerFacing);
            }
            state = state.withProperty(OPEN, true);
            world.setBlockState(pos, state, 10);
        }
        world.playEvent(player, state.getValue(OPEN) ? 1008 : 1014, pos, 0);
        return true;
    }

    @Override
    public void neighborChanged(IBlockState state, World world, BlockPos pos, Block blockIn, BlockPos fromPos) {
        if (world.isRemote) {
            return;
        }
        boolean powered = world.isBlockPowered(pos);
        if (state.getValue(POWERED) == powered) {
            return;
        }
        world.setBlockState(pos, state.withProperty(POWERED, powered)
            .withProperty(OPEN, powered), 2);
        if (state.getValue(OPEN) != powered) {
            world.playEvent(null, powered ? 1008 : 1014, pos, 0);
        }
    }

    @SideOnly(Side.CLIENT)
    @Override
    public boolean shouldSideBeRendered(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing side) {
        return true;
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return getDefaultState().withProperty(FACING, EnumFacing.getHorizontal(meta))
            .withProperty(OPEN, (meta & 4) != 0)
            .withProperty(POWERED, (meta & 8) != 0);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        int meta = state.getValue(FACING)
            .getHorizontalIndex();
        if (state.getValue(POWERED)) {
            meta |= 8;
        }
        if (state.getValue(OPEN)) {
            meta |= 4;
        }
        return meta;
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, FACING, OPEN, POWERED, IN_WALL);
    }
}
