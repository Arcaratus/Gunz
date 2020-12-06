package arcaratus.gunz.common.item;

import arcaratus.gunz.common.core.handler.ModSounds;
import arcaratus.gunz.common.gunz.IGunz;
import arcaratus.gunz.common.network.PacketHandler;
import arcaratus.gunz.common.network.PacketShoot;
import arcaratus.gunz.common.util.helper.ItemNBTHelper;
import arcaratus.gunz.common.util.helper.GunHelper;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nonnull;
import java.util.List;

public class GunItem extends Item implements IGun
{
    public static final String TAG_RELOAD = "Reload";
    public final static String TAG_AMMO = "Ammo";
    public static final String TAG_STORED_AMMO = "StoredAmmo";
    public static final String TAG_COOLDOWN = "Cooldown";
    public static final String TAG_HELD_DOWN = "HeldDown";
    public static final String TAG_RECOIL = "Recoil";

    private IGunz gunz;

    private boolean reloading;

    public GunItem(Properties prop, IGunz gunz)
    {
        super(prop);
        this.gunz = gunz;

        reloading = false;
    }

    @Override
    public boolean canPlayerBreakBlockWhileHolding(BlockState state, World worldIn, BlockPos pos, PlayerEntity player)
    {
        return false;
    }

    @Override
    public void inventoryTick(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected)
    {
        boolean heldDown = getHeldDown(stack);
        if (!heldDown && getRecoil(stack) > 0)
        {
            setRecoil(stack, getRecoil(stack) - 0.75);
        }

        if (getCooldown(stack) > 0)
        {
            setCooldown(stack, getCooldown(stack) - 1);
            return;
        }

        if (entityIn instanceof PlayerEntity)
        {
            PlayerEntity player = (PlayerEntity) entityIn;
            if (isSelected)
            {
                if (getReload(stack) > 0)
                {
                    setReload(stack, Math.max(getReload(stack) - 1, 0));
                    return;
                }
                else if (getReload(stack) == 0)
                {
                    int ammo = getStoredAmmo(stack) + getAmmo(stack) >= gunz.maxAmmo() ? gunz.maxAmmo() - getAmmo(stack) : getStoredAmmo(stack);
                    setAmmo(stack, getAmmo(stack) + ammo);
                    setStoredAmmo(stack, getStoredAmmo(stack) - ammo);
                    setReload(stack, -1);
                }

                if (Minecraft.getInstance().gameSettings.keyBindAttack.isKeyDown() && canShoot(stack))
                {
                    if (gunz.holdFire() || !heldDown)
                    {
//                        GunHelper.shoot(stack, this, gunz, player);
                        if (!worldIn.isRemote)
                            PacketHandler.sendToServer(new PacketShoot(player.rotationYaw, player.rotationPitch));
                        setCooldown(stack, 2);
                        setAmmo(stack, getAmmo(stack) - 1);
                        if (worldIn.isRemote)
                        {
                            GunHelper.applyRecoil(stack, this, gunz, player);
                        }

                        if (!heldDown)
                        {
                            setHeldDown(stack, true);
                        }
                    }
                }
                else if (heldDown)
                {
                    setHeldDown(stack, false);
                }
            }
            else
            {
                setReload(stack, -1);
            }
        }
    }

    @Override
    public void onPlayerStoppedUsing(ItemStack stack, World worldIn, LivingEntity entityLiving, int timeLeft)
    {

    }

    @Nonnull
    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, @Nonnull Hand hand)
    {
        ItemStack stack = player.getHeldItem(hand);
        return ActionResult.resultPass(stack);
    }

    @Override
    public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged)
    {
        return slotChanged;
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void addInformation(ItemStack stack, World world, List<ITextComponent> tooltip, ITooltipFlag flags)
    {
        tooltip.add(new TranslationTextComponent("gunz.ammo").append(new StringTextComponent(getAmmo(stack) + " / " + gunz.maxAmmo() + " : " + getStoredAmmo(stack))));
    }

    @Override
    public IGunz getGunz(ItemStack stack)
    {
        return gunz;
    }

    @Override
    public void reload(ItemStack stack, PlayerEntity player)
    {
        if (getStoredAmmo(stack) > 0 && getAmmo(stack) < gunz.maxAmmo())
        {
            setReload(stack, gunz.reloadTime());
            if (!player.world.isRemote)
                player.world.playSound(null, player.getPosX(), player.getPosY(), player.getPosZ(), ModSounds.PANTHER_RELOAD, SoundCategory.PLAYERS, 1, 1);
        }
    }

    @Override
    public void setReload(ItemStack stack, int reload)
    {
        ItemNBTHelper.setInt(stack, TAG_RELOAD, reload);
    }

    @Override
    public int getReload(ItemStack stack)
    {
        return ItemNBTHelper.getInt(stack, TAG_RELOAD, -1);
    }

    @Override
    public void setAmmo(ItemStack stack, int amount)
    {
        ItemNBTHelper.setInt(stack, TAG_AMMO, Math.max(amount, 0));
    }

    @Override
    public int getAmmo(ItemStack stack)
    {
        return ItemNBTHelper.getInt(stack, TAG_AMMO, gunz.maxAmmo());
    }

    @Override
    public void setStoredAmmo(ItemStack stack, int amount)
    {
        ItemNBTHelper.setInt(stack, TAG_STORED_AMMO, amount);
    }

    @Override
    public int getStoredAmmo(ItemStack stack)
    {
        return ItemNBTHelper.getInt(stack, TAG_STORED_AMMO, gunz.maxAmmo() * 4);
    }

    @Override
    public void setCooldown(ItemStack stack, int amount)
    {
        ItemNBTHelper.setInt(stack, TAG_COOLDOWN, amount);
    }

    @Override
    public int getCooldown(ItemStack stack)
    {
        return ItemNBTHelper.getInt(stack, TAG_COOLDOWN, 0);
    }

    @Override
    public void setHeldDown(ItemStack stack, boolean heldDown)
    {
        ItemNBTHelper.setBoolean(stack, TAG_HELD_DOWN, heldDown);
    }

    @Override
    public boolean getHeldDown(ItemStack stack)
    {
        return ItemNBTHelper.getBoolean(stack, TAG_HELD_DOWN, false);
    }

    @Override
    public boolean canShoot(ItemStack stack)
    {
        return getAmmo(stack) > 0 && getReload(stack) < 0 && getCooldown(stack) <= 0;
    }

    @Override
    public void setRecoil(ItemStack stack, double recoil)
    {
        ItemNBTHelper.setDouble(stack, TAG_RECOIL, Math.max(recoil, 0));
    }

    @Override
    public double getRecoil(ItemStack stack)
    {
        return ItemNBTHelper.getDouble(stack, TAG_RECOIL, 0);
    }
}
