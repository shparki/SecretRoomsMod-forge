package com.wynprice.secretroomsmod.blocks;

import com.wynprice.secretroomsmod.base.BaseFakeBlock;
import com.wynprice.secretroomsmod.base.interfaces.ISecretTileEntity;
import com.wynprice.secretroomsmod.render.fakemodels.FakeBlockModel;
import com.wynprice.secretroomsmod.render.fakemodels.OneWayGlassFakeModel;
import com.wynprice.secretroomsmod.render.fakemodels.TrueSightFaceDiffrentModel;
import com.wynprice.secretroomsmod.render.fakemodels.TrueSightModel;

import net.minecraft.block.BlockDirectional;
import net.minecraft.block.BlockGlass;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.property.ExtendedBlockState;
import net.minecraftforge.common.property.IUnlistedProperty;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class OneWayGlass extends BaseFakeBlock
{
	public OneWayGlass() 
	{
		super("one_way_glass", Material.GLASS);
		this.setDefaultState(this.blockState.getBaseState().withProperty(BlockDirectional.FACING, EnumFacing.DOWN));
		this.setHardness(0.5f);
		this.translucent = true;
    }
	
	@Override
	public boolean isOpaqueCube(IBlockState state) {
		return false;
	}
	
	@Override
	public boolean shouldSideBeRendered(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos,
			EnumFacing side) 
	{
		if(blockState.getValue(BlockDirectional.FACING) != side && ((blockAccess.getBlockState(pos.offset(side)).getBlock() instanceof BlockGlass && blockAccess.getBlockState(pos.offset(side)).getBlockFaceShape(blockAccess, pos, side.getOpposite()) == BlockFaceShape.SOLID) || blockAccess.getBlockState(pos.offset(side)).getBlock() == this)) {
			return false;
		}
		return super.shouldSideBeRendered(blockState, blockAccess, pos, side);
	}
	
	@Override
	public boolean doesSideBlockRendering(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing face) {
		if(world.getBlockState(pos.offset(face)).getBlock() instanceof BlockGlass && world.getBlockState(pos.offset(face)).getBlockFaceShape(world, pos, face.getOpposite()) == BlockFaceShape.SOLID) {
			return true;
		}
		return false;
	}
	
	@SideOnly(Side.CLIENT)
	@Override
	public TrueSightModel phaseTrueModel(TrueSightModel model) {
		return new TrueSightFaceDiffrentModel(model, FakeBlockModel.getModel(Blocks.GLASS.getDefaultState()));
	}
	
	@SideOnly(Side.CLIENT)
	@Override
	public FakeBlockModel phaseModel(FakeBlockModel model) {
		return new OneWayGlassFakeModel(model);
	}
	
	@Override
	public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face) {
		return face != state.getValue(BlockDirectional.FACING) ? BlockFaceShape.SOLID : ((ISecretTileEntity)worldIn.getTileEntity(pos)).getMirrorState().getBlockFaceShape(worldIn, pos, face);
	}
	
	@Override
	public IBlockState getStateFromMeta(int meta) {
		return this.getDefaultState().withProperty(BlockDirectional.FACING, EnumFacing.getFront(meta));
	}
	
	@Override
	public int getMetaFromState(IBlockState state) {
		return state.getValue(BlockDirectional.FACING).getIndex();
	}
	
	@Override
	protected BlockStateContainer createBlockState() {
    	return new ExtendedBlockState(this, new IProperty[]{BlockDirectional.FACING}, new IUnlistedProperty[] {RENDER_PROPERTY});    
	}
	
	@SideOnly(Side.CLIENT)
    @Override
    public boolean canRenderInLayer(IBlockState state, BlockRenderLayer layer) 
    {
		return layer == BlockRenderLayer.CUTOUT;
    }
	
	@Override
	public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer,
			ItemStack stack) 
	{
		EnumFacing facing = placer.getEntityData().getBoolean("glassDirection") ? EnumFacing.getDirectionFromEntityLiving(pos, placer).getOpposite() : EnumFacing.getDirectionFromEntityLiving(pos, placer);
		worldIn.setBlockState(pos, state.withProperty(BlockDirectional.FACING, facing), 3);
		super.onBlockPlacedBy(worldIn, pos, state, placer, stack);
	}

}
