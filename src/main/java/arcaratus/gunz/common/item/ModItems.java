package arcaratus.gunz.common.item;

import arcaratus.gunz.common.Gunz;
import arcaratus.gunz.common.gunz.Rifle;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;

public class ModItems
{
    public static final Item PANTHER = new GunItem(unstackable(), new Rifle.Panther());

    private static Item.Properties defaultBuilder()
    {
        return new Item.Properties().group(Gunz.TAB_GUNZ);
    }

    private static Item.Properties unstackable()
    {
        return defaultBuilder().maxStackSize(1);
    }

    public static void registerItems(RegistryEvent.Register<Item> event)
    {
        IForgeRegistry<Item> r = event.getRegistry();
        register(r, "rifle_panther", PANTHER);
    }

    public static <V extends IForgeRegistryEntry<V>> void register(IForgeRegistry<V> reg, ResourceLocation name, IForgeRegistryEntry<V> thing)
    {
        reg.register(thing.setRegistryName(name));
    }

    public static <V extends IForgeRegistryEntry<V>> void register(IForgeRegistry<V> reg, String name, IForgeRegistryEntry<V> thing)
    {
        register(reg, new ResourceLocation(Gunz.MOD_ID, name), thing);
    }
}
