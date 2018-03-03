package com.wynprice.secretroomsmod.intergration.malisisdoors;

import java.util.List;

import com.wynprice.secretroomsmod.base.interfaces.ISecretBlock;
import com.wynprice.secretroomsmod.base.interfaces.ISecretTileEntity;

import net.malisis.core.block.IComponent;
import net.malisis.core.renderer.RenderParameters;
import net.malisis.core.renderer.RenderType;
import net.malisis.core.renderer.element.Face;
import net.malisis.core.renderer.element.Shape;
import net.malisis.core.renderer.icon.Icon;
import net.malisis.core.renderer.icon.provider.IBlockIconProvider;
import net.malisis.core.renderer.icon.provider.IIconProvider;
import net.malisis.core.renderer.icon.provider.IItemIconProvider;
import net.malisis.core.util.EnumFacingUtils;
import net.malisis.doors.iconprovider.DoorIconProvider;
import net.malisis.doors.renderer.DoorRenderer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.MathHelper;

public class SecretDoorRenderer extends DoorRenderer
{
	public SecretDoorRenderer() 
	{
		super(true);
		registerFor(SecretMalisisTileEntityDoor.class);
	}
	
	@Override
	protected void initialize() {
		super.initialize();
		ensureBlock(SecretMalisisDoor.class);
		rp.calculateAOColor.set(true);
		rp.deductParameters.set(true);
	}
	
	@Override
	public void render() 
	{
		initialize(); ///REMOVE
		super.render();
	}
	
	@Override
	public void drawShape(Shape s, RenderParameters params) {
		if (s == null)
			return;

		s.applyMatrix();

		for (Face f : s.getFaces()) {
			
			if(world.getBlockState(pos).getBlock() instanceof ISecretBlock) {
				SecretRenderParameters sParams = new SecretRenderParameters(params, Icon.missing);
				RenderParameters sParamsFace = new RenderParameters(params); //Used to get the EnumFacing from the face
				Face getFacing = new Face(f, sParamsFace);
				getFacing.deductParameters();
				EnumFacing facing = sParamsFace.textureSide.get();
				facing = facing == null ? EnumFacing.SOUTH : facing; //was in build somewhere, just leave it here
				facing = EnumFacingUtils.getRealSide(blockState, facing);
				IBlockState mirrorState = ((ISecretTileEntity)world.getTileEntity(pos)).getMirrorState();
				List<BakedQuad> quadList = Minecraft.getMinecraft().getBlockRendererDispatcher().getModelForState(mirrorState).getQuads(mirrorState, facing, MathHelper.getPositionRandom(pos));
				Face controllFace = new Face(f, sParams);
				for(BakedQuad quad : quadList) {
					sParams.quadSprite.set(quad.getSprite());
					if(!quad.hasTintIndex()) {
						sParams.colorMultiplier.set(-1);
					} else {
						sParams.colorMultiplier.set(Minecraft.getMinecraft().getBlockColors().colorMultiplier(mirrorState, world, pos, quad.getTintIndex()));
					}
					drawFace(new Face(f, sParams), sParams);
				}
			} else {
				drawFace(f, params);
			}
			
		}
	}
	
	@Override
	protected Icon getIcon(Face face, RenderParameters params)
	{
		
		if (params.icon.get() != null)
			return params.icon.get();

		DoorIconProvider iconProvider0 = IComponent.getComponent(DoorIconProvider.class, block);
		if (iconProvider0 == null)  {
			if (params != null && params.icon.get() != null)
				return params.icon.get();

			IIconProvider iconProvider = getIconProvider(params);
			if (iconProvider instanceof IItemIconProvider && itemStack != null)
				return ((IItemIconProvider) iconProvider).getIcon(itemStack);

			if (iconProvider instanceof IBlockIconProvider && block != null)
			{
				EnumFacing side = params != null ? params.textureSide.get() : EnumFacing.SOUTH;
				if (params != null && shouldRotateIcon(params))
					side = EnumFacingUtils.getRealSide(blockState, side);

				IBlockIconProvider iblockp = (IBlockIconProvider) iconProvider;
				
				//SRM START
				if(iblockp instanceof SecretBlockIconProvider) {
					((SecretBlockIconProvider)iblockp).current_params.set(params);
				}
				//SRM STOP
				if (renderType == RenderType.BLOCK || renderType == RenderType.TILE_ENTITY)
					return iblockp.getIcon(world, pos, blockState, side);
				else if (renderType == RenderType.ITEM)
					return iblockp.getIcon(itemStack, side);
			}

			return iconProvider != null ? iconProvider.getIcon() : Icon.missing;
		}

		return iconProvider0.getIcon(topBlock, hingeLeft, params.textureSide.get());
	}
	
}