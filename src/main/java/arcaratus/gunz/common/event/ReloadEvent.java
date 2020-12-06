package arcaratus.gunz.common.event;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerEvent;

import javax.annotation.Nonnull;

public class ReloadEvent extends PlayerEvent
{
    private final ItemStack gun;
    private final World world;

    public ReloadEvent(PlayerEntity player, @Nonnull ItemStack gun, World world)
    {
        super(player);
        this.gun = gun;
        this.world = world;
    }

    @Nonnull
    public ItemStack getGun()
    {
        return gun;
    }

    public World getWorld()
    {
        return world;
    }
}
