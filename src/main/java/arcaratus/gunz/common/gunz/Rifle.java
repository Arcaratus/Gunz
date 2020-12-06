package arcaratus.gunz.common.gunz;

import arcaratus.gunz.common.core.handler.ModSounds;
import net.minecraft.util.SoundEvent;

public class Rifle extends GunzBase
{
    public Rifle(String name, int maxAmmo, int reloadTime, boolean holdFire, int fireRate, int penetration, double recoil, double[] recoilPattern, SoundEvent shootSound)
    {
        super("rifle_" + name, maxAmmo, reloadTime, holdFire, fireRate, penetration, recoil, recoilPattern, shootSound);
    }

    public static class Panther extends Rifle
    {
        public static final double[] RECOIL_PATTERN = new double[] { 2, 1 };

        public Panther()
        {
            super("panther", 30, 54, true, 2, 1, 0.2, RECOIL_PATTERN, ModSounds.PANTHER_SHOOT);
        }
    }
}
