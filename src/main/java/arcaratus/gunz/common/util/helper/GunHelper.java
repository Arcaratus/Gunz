package arcaratus.gunz.common.util.helper;

import arcaratus.gunz.common.gunz.IGunz;
import arcaratus.gunz.common.item.IGun;
import arcaratus.gunz.common.network.PacketShoot;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.network.play.NetworkPlayerInfo;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.merchant.villager.VillagerEntity;
import net.minecraft.entity.monster.CreeperEntity;
import net.minecraft.entity.monster.SkeletonEntity;
import net.minecraft.entity.monster.ZombieEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.entity.projectile.ProjectileHelper;
import net.minecraft.item.ArrowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.Direction;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.*;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.Random;

public class GunHelper
{
    public final static double MAX_RANGE = 64;
    public final static double MAX_RANGE_SQUARED = 4096;

    public static void shoot(PacketShoot msg, ServerPlayerEntity player)
    {
        if (!player.isSpectator())
        {
            World world = player.world;
            ItemStack stack = player.inventory.getCurrentItem();
            if (!stack.isEmpty() && stack.getItem() instanceof IGun)
            {
                IGun gun = (IGun) stack.getItem();
                IGunz gunz = gun.getGunz(stack);

                player.rotationYaw = msg.getPlayerYaw();
                player.rotationPitch = msg.getPlayerPitch();

                if (!world.isRemote)
                    world.playSound(null, player.getPosX(), player.getPosY(), player.getPosZ(), gunz.shootSound(), SoundCategory.PLAYERS, 0.6F, 1 + world.rand.nextFloat() * 0.2F);

                Vector3d eyeVec = player.getEyePosition(1);
                Vector3d lookVec = player.getLook(1).scale(MAX_RANGE);
                Vector3d combined = eyeVec.add(lookVec);

                ClientPlayerEntity clientPlayer = Minecraft.getInstance().player;
                NetworkPlayerInfo npi = clientPlayer.connection.getPlayerInfo(player.getGameProfile().getId());

                AxisAlignedBB aabb = player.getBoundingBox().expand(lookVec).grow(1);
                EntityRayTraceResult rayTraceResult = ProjectileHelper.rayTraceEntities(player, eyeVec, combined, aabb, e -> e instanceof LivingEntity, MAX_RANGE_SQUARED);

                if (rayTraceResult == null)
                {
                    System.out.println("MISS");
                    if (world.isRemote)
                    {
                        BlockRayTraceResult blockRayTrace = rayTrace(world, player);
                        if (blockRayTrace.getType() == RayTraceResult.Type.BLOCK)
                        {
                            renderHitSparks(world, blockRayTrace.getPos(), blockRayTrace.getHitVec(), blockRayTrace.getFace());
                        }
                    }

                    applyRecoil(stack, gun, gunz, player);
                    return;
                }

                Vector3d hitVec = rayTraceResult.getHitVec();
                double range = eyeVec.distanceTo(hitVec);
                LivingEntity target = (LivingEntity) rayTraceResult.getEntity();

                if (!world.isRemote)
                {

//                    shootEntity(player, target, hitVec, gunz.damageAtRange(range));
                }

//                applyRecoil(stack, gun, gunz, player);
            }
        }
    }

    public static void shoot(ItemStack stack, IGun gun, IGunz gunz, PlayerEntity player)
    {
        World world = player.world;
        //        if (!world.isRemote)
        {
            world.playSound(null, player.getPosX(), player.getPosY(), player.getPosZ(), gunz.shootSound(), SoundCategory.PLAYERS, 0.6F, 1 + world.rand.nextFloat() * 0.2F);

            Vector3d eyeVec = player.getEyePosition(1);
            Vector3d lookVec = player.getLook(1).scale(MAX_RANGE);
            Vector3d combined = eyeVec.add(lookVec);
            AxisAlignedBB aabb = player.getBoundingBox().expand(lookVec).grow(1);
            EntityRayTraceResult rayTraceResult = ProjectileHelper.rayTraceEntities(player, eyeVec, combined, aabb, e -> e instanceof LivingEntity, MAX_RANGE_SQUARED);

            if (rayTraceResult == null)
            {
                System.out.println("MISS");
                if (world.isRemote)
                {
                    BlockRayTraceResult blockRayTrace = rayTrace(world, player);
                    if (blockRayTrace.getType() == RayTraceResult.Type.BLOCK)
                    {
                        renderHitSparks(world, blockRayTrace.getPos(), blockRayTrace.getHitVec(), blockRayTrace.getFace());
                    }
                }

                applyRecoil(stack, gun, gunz, player);
                return;
            }

            Vector3d hitVec = rayTraceResult.getHitVec();
            double range = eyeVec.distanceTo(hitVec);
            LivingEntity target = (LivingEntity) rayTraceResult.getEntity();

            if (!world.isRemote)
            {
                shootEntity(player, target, hitVec, gunz.damageAtRange(range));
            }
            //            PacketHandler.sendToServer(new PacketShoot(target.getEntityId(), hitVec.getX(), hitVec.getY(), hitVec.getZ(), gunz.damageAtRange(distance)));
        }

        if (world.isRemote)
        {
            applyRecoil(stack, gun, gunz, player);
        }
    }

    public static void shootEntity(PlayerEntity shooter, LivingEntity target, Vector3d hitVec, float damage)
    {
        World world = target.world;
        System.out.println("L: " + target);
        if (target instanceof VillagerEntity || target instanceof PlayerEntity || target instanceof ZombieEntity || target instanceof SkeletonEntity || target instanceof CreeperEntity)
        {
            double headStart = target.getPosition().getY() + 0.8 * target.getSize(target.getPose()).height - 0.1;
            if (hitVec.getY() > headStart)
            {
                System.out.println("Dinked: " + headStart + ", " + hitVec.getY());
                if (!world.isRemote)
                {
                    world.playSound(target.getPosX(), target.getPosY(), target.getPosZ(), SoundEvents.ENTITY_PLAYER_ATTACK_CRIT, SoundCategory.PLAYERS, 1, 1, false);
                }
                else
                {
                    shooter.world.playSound(shooter, shooter.getPosX(), shooter.getPosYEye(), shooter.getPosZ(), SoundEvents.ENTITY_ARROW_HIT_PLAYER, SoundCategory.PLAYERS, 0.18F, 0.45F);
                }

                target.attackEntityFrom(DamageSourceShot.headshot(shooter, hitVec), damage * 2);
                target.hurtResistantTime = 0;
                return;
            }
        }

        if (!world.isRemote)
        {
            world.playSound(target.getPosX(), target.getPosY(), target.getPosZ(), SoundEvents.ENTITY_PLAYER_ATTACK_STRONG, SoundCategory.PLAYERS, 1, 1, false);
        }

        target.attackEntityFrom(DamageSourceShot.shot(shooter, hitVec), damage);
        target.hurtResistantTime = 0;

    }

    // Applies one "instance" of recoil
    public static void applyRecoil(ItemStack stack, IGun gun, IGunz gunz, PlayerEntity shooter)
    {
        Random random = shooter.world.getRandom();
        double recoil = gun.getRecoil(stack) + 0.75;
        double[] recoilPattern = new double[] {0, 0.5, 0.2, 1, 0.3, 1.6, 0.4, 2.2, 0.8, 3, 0.7, 3.4, 0.6, 3, 0.5, 2.8, 0.4, 3, 0.7, 3.4};
        //gunz.recoilPattern(); // Every (i, i+1) -> (yaw, pitch)
        int idx = (int) Math.min(recoil, 9); // Recoil ranges from [0, 9]

        double modifier = shooter.isCrouching() ? 0.8 : shooter.isAirBorne ? 1.2 : 1;
        shooter.rotationYaw += modifier * (randSign(random) * recoilPattern[2 * idx] + randSign(random) * (0.2 * random.nextDouble())); // Horizontal
        shooter.rotationPitch -= modifier * (recoilPattern[2 * idx + 1] + randSign(random) * (0.2 * random.nextDouble())); // Vertical

        gun.setRecoil(stack, recoil);
    }

    @OnlyIn(Dist.CLIENT)
    public static void renderHitSparks(World world, BlockPos pos, Vector3d hitVec, Direction face)
    {
        Random rand = world.rand;
        double d0 = (hitVec.getX() + 0.1 * face.toVector3f().getX());// + (rand.nextDouble() - 0.5D) * 0.2D;
        double d1 = (hitVec.getY() + 0.1 * face.toVector3f().getY());// + (rand.nextDouble() - 0.5D) * 0.2D;
        double d2 = hitVec.getZ() + 0.1 * face.toVector3f().getZ();// + (rand.nextDouble() - 0.5D) * 0.2D;
        world.addParticle(ParticleTypes.SMOKE, d0, d1, d2, 0.0D, 0.0D, 0.0D);
    }

    private static double randSign(Random random)
    {
        return random.nextBoolean() ? 1 : -1;
    }

    public static BlockRayTraceResult rayTrace(World worldIn, PlayerEntity player)
    {
        float f = player.rotationPitch;
        float f1 = player.rotationYaw;
        Vector3d vector3d = player.getEyePosition(1.0F);
        float f2 = MathHelper.cos(-f1 * ((float) Math.PI / 180F) - (float) Math.PI);
        float f3 = MathHelper.sin(-f1 * ((float) Math.PI / 180F) - (float) Math.PI);
        float f4 = -MathHelper.cos(-f * ((float) Math.PI / 180F));
        float f5 = MathHelper.sin(-f * ((float) Math.PI / 180F));
        Vector3d vector3d1 = vector3d.add((double) f3 * f4 * MAX_RANGE, (double) f5 * MAX_RANGE, (double) f2 * f4 * MAX_RANGE);
        return worldIn.rayTraceBlocks(new RayTraceContext(vector3d, vector3d1, RayTraceContext.BlockMode.OUTLINE, RayTraceContext.FluidMode.NONE, player));
    }
}
