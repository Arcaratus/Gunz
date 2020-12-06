package arcaratus.gunz.common.util.helper;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.EntityDamageSource;
import net.minecraft.util.math.vector.Vector3d;

import javax.annotation.Nullable;

public class DamageSourceShot extends EntityDamageSource
{
    private Vector3d hitVec;

    public DamageSourceShot(String type, PlayerEntity player, Vector3d hitVec)
    {
        super(type, player);
        this.hitVec = hitVec;
    }

    public static DamageSourceShot shot(PlayerEntity player, Vector3d hitVec)
    {
        return new DamageSourceShot("shot", player, hitVec);
    }

    public static DamageSourceShot headshot(PlayerEntity player, Vector3d hitVec)
    {
        return new DamageSourceShot("headshot", player, hitVec);
    }

    @Override
    public boolean isDifficultyScaled()
    {
        return false;
    }

    @Nullable
    @Override
    public Vector3d getDamageLocation()
    {
        return hitVec;
    }

    @Override
    public String toString()
    {
        return "DamageSourceShot (" + this.damageSourceEntity + ")";
    }
}
