package arcaratus.gunz.common.core.handler;

import arcaratus.gunz.common.Gunz;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.registries.IForgeRegistry;

public class ModSounds
{
    public static final SoundEvent PANTHER_SHOOT = makeSoundEvent("panther_shoot");
    public static final SoundEvent PANTHER_RELOAD = makeSoundEvent("panther_reload");

    private static SoundEvent makeSoundEvent(String name)
    {
        ResourceLocation loc = new ResourceLocation(Gunz.MOD_ID, name);
        return new SoundEvent(loc).setRegistryName(loc);
    }

    public static void registerSounds(RegistryEvent.Register<SoundEvent> event)
    {
        IForgeRegistry<SoundEvent> r = event.getRegistry();
        r.register(PANTHER_SHOOT);
        r.register(PANTHER_RELOAD);
    }
}
