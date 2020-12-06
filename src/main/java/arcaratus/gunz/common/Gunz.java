package arcaratus.gunz.common;

import arcaratus.gunz.client.proxy.ClientProxy;
import arcaratus.gunz.common.core.handler.ModSounds;
import arcaratus.gunz.common.core.proxy.IProxy;
import arcaratus.gunz.common.item.ModItems;
import arcaratus.gunz.common.network.PacketHandler;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(Gunz.MOD_ID)
public class Gunz
{
    public static final String MOD_ID = "gunz";

    public static IProxy proxy = new IProxy() {};

    public static final ItemGroup TAB_GUNZ = new ItemGroup(MOD_ID)
    {
        @Override
        public ItemStack createIcon()
        {
            return new ItemStack(ModItems.PANTHER);
        }
    };

    public Gunz()
    {
        DistExecutor.callWhenOn(Dist.CLIENT, () -> () -> proxy = new ClientProxy());
        proxy.registerHandlers();

        IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();
        modBus.addListener(this::commonSetup);
        modBus.addGenericListener(Item.class, ModItems::registerItems);
        modBus.addGenericListener(SoundEvent.class, ModSounds::registerSounds);

        IEventBus forgeBus = MinecraftForge.EVENT_BUS;
        
    }

    private void commonSetup(FMLCommonSetupEvent event)
    {
        PacketHandler.init();
    }
}
