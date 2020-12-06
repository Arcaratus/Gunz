package arcaratus.gunz.common.gunz;

import net.minecraft.util.SoundEvent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public abstract class GunzBase implements IGunz
{
    private final String NAME;
    private final int MAX_AMMO;
    private final int RELOAD_TIME;
    private final boolean HOLD_FIRE;
    private final int FIRE_RATE;
    private final int PENETRATION;
    private final double RECOIL;
    private final double[] RECOIL_PATTERN;

    private final SoundEvent SHOOT_SOUND;

    public GunzBase(String name, int maxAmmo, int reloadTime, boolean holdFire, int fireRate, int penetration, double recoil, double[] recoilPattern, SoundEvent shootSound)
    {
        NAME = name;
        MAX_AMMO = maxAmmo;
        RELOAD_TIME = reloadTime;
        HOLD_FIRE = holdFire;
        FIRE_RATE = fireRate;
        PENETRATION = penetration;
        RECOIL = recoil;
        RECOIL_PATTERN = recoilPattern;

        SHOOT_SOUND = shootSound;
    }

    @Override
    public String name()
    {
        return NAME;
    }

    @Override
    public int maxAmmo()
    {
        return MAX_AMMO;
    }

    @Override
    public int reloadTime()
    {
        return RELOAD_TIME;
    }

    @Override
    public boolean holdFire()
    {
        return HOLD_FIRE;
    }

    @Override
    public int fireRate()
    {
        return FIRE_RATE;
    }

    @Override
    public float damageAtRange(double range)
    {
        return 5;
    }

    @Override
    public int penetration()
    {
        return PENETRATION;
    }

    @Override
    public double recoil()
    {
        return RECOIL;
    }

    @Override
    public double[] recoilPattern()
    {
        return RECOIL_PATTERN;
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public SoundEvent shootSound()
    {
        return SHOOT_SOUND;
    }
}
