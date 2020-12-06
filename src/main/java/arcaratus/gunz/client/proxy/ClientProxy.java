package arcaratus.gunz.client.proxy;

import arcaratus.gunz.client.handler.ClientTickHandler;
import arcaratus.gunz.client.handler.GunzKeyHandler;
import arcaratus.gunz.client.handler.RenderTickHandler;
import arcaratus.gunz.common.core.proxy.IProxy;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

public class ClientProxy implements IProxy
{
    @Override
    public void registerHandlers()
    {
        IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();
        modBus.addListener(this::clientSetup);

        IEventBus forgeBus = MinecraftForge.EVENT_BUS;
        forgeBus.addListener(ClientTickHandler::onClickInputEvent);
        forgeBus.addListener(RenderTickHandler::renderOverlay);
        forgeBus.addListener(GunzKeyHandler::onKeyInput);
    }

    private void clientSetup(FMLClientSetupEvent event)
    {
        GunzKeyHandler.registerKeys();
    }

    @Override
    public boolean isTheClientPlayer(LivingEntity entity) {
        return entity == Minecraft.getInstance().player;
    }

    @Override
    public PlayerEntity getClientPlayer() {
        return Minecraft.getInstance().player;
    }
}
