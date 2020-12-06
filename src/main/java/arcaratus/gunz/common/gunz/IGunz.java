package arcaratus.gunz.common.gunz;

import net.minecraft.util.SoundEvent;

public interface IGunz
{
    // Name
    String name();

    // Clip size
    int maxAmmo();

    // Time to reload - reload speed in ticks
    int reloadTime();

    // True for hold to auto-fire
    boolean holdFire();

    // Time between consecutive shots in ticks
    int fireRate();

    // Damage at range
    float damageAtRange(double range);

    // Level of penetration
    int penetration();

    // Recoil

    // Amount it recoils by (the heat) -> (C * randDouble()) * recoil()
    double recoil();

    // The recoil pattern of the weapon
    double[] recoilPattern();


    // Sounds

    // Shoot sound
    SoundEvent shootSound();
}
