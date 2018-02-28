package com.wynprice.secretroomsmod.handler;

import com.wynprice.secretroomsmod.SecretItems;
import com.wynprice.secretroomsmod.SecretRooms5;
import com.wynprice.secretroomsmod.base.interfaces.ISecretBlock;
import com.wynprice.secretroomsmod.render.fakemodels.SecretBlockModel;
import com.wynprice.secretroomsmod.render.fakemodels.SwitchProbeRenderChange;

import net.minecraft.block.Block;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.init.Blocks;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * Used to switch the models on bake event
 * @author Wyn Price
 *
 */
public class ProbeSwitchRenderHander 
{
	 @SubscribeEvent
	 public void onModelBake(ModelBakeEvent e) 
	 {
		 for(ModelResourceLocation model : e.getModelRegistry().getKeys())
		 {
	            if(model.getResourceDomain().equals(SecretRooms5.MODID) && (model.getResourcePath().equals(SecretItems.SWITCH_PROBE.getRegistryName().getResourcePath())
	            		|| model.getResourcePath().equals(SecretItems.PROGRAMMABLE_SWITCH_PROBE.getRegistryName().getResourcePath())))
	            	e.getModelRegistry().putObject(model, new SwitchProbeRenderChange(e.getModelRegistry().getObject(model)));
	            
	            if(!model.getVariant().equals("inventory"))
	            {
	            	Block block = Block.REGISTRY.getObject(new ResourceLocation(model.getResourceDomain(), model.getResourcePath()));
	            	if(block instanceof ISecretBlock)
	            		e.getModelRegistry().putObject(model, new SecretBlockModel(e.getModelRegistry().getObject(model)));
	            }
		 }
		 
	 }
}
