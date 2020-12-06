package arcaratus.gunz.common.item;

import arcaratus.gunz.common.gunz.IGunz;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;

public interface IGun
{
    IGunz getGunz(ItemStack stack);

    void reload(ItemStack stack, PlayerEntity player);

    void setReload(ItemStack stack, int reload);

    int getReload(ItemStack stack);

    void setAmmo(ItemStack stack, int amount);

    int getAmmo(ItemStack stack);

    void setStoredAmmo(ItemStack stack, int amount);

    int getStoredAmmo(ItemStack stack);

    void setCooldown(ItemStack stack, int amount);

    int getCooldown(ItemStack stack);

    boolean canShoot(ItemStack stack);

    void setHeldDown(ItemStack stack, boolean heldDown);

    boolean getHeldDown(ItemStack stack);

    void setRecoil(ItemStack stack, double recoil);

    double getRecoil(ItemStack stack);
}
