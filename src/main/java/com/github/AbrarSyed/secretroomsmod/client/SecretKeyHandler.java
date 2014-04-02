package com.github.AbrarSyed.secretroomsmod.client;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;

import com.github.AbrarSyed.secretroomsmod.common.SecretRooms;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.InputEvent.KeyInputEvent;

public class SecretKeyHandler
{
    private static final IChatComponent TOWARDS = new ChatComponentText(EnumChatFormatting.YELLOW + "-- !!! OneWayBlock facing set to Towards !!! --");
    private static final IChatComponent AWAY = new ChatComponentText(EnumChatFormatting.YELLOW + "-- !!! OneWayBlock facing set to Away !!! --");
    
    boolean oldState = false;
    
    @SubscribeEvent
	public void keyPress(KeyInputEvent event)
	{
        boolean currentState = ProxyClient.key_OneWayFace.getIsKeyPressed();
        if (currentState && currentState != oldState)
        {
            // FIRE AT WILL
            
            EntityPlayer player = FMLClientHandler.instance().getClient().thePlayer;

            if (player == null || player.worldObj == null || Minecraft.getMinecraft().currentScreen != null)
                return;

            SecretRooms.proxy.onKeyPress(player.getUniqueID());

            if (SecretRooms.proxy.getFaceTowards(player.getUniqueID()))
            {
                player.addChatMessage(TOWARDS);
            }
            else
            {
                player.addChatMessage(AWAY);
            }
        }
	}
}
