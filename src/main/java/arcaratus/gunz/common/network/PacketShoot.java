package arcaratus.gunz.common.network;

import arcaratus.gunz.common.util.helper.GunHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class PacketShoot
{
//    private final int entityId;
//    private final double hitVecX;
//    private final double hitVecY;
//    private final double hitVecZ;
//    private final float damage;
    private final float playerYaw;
    private final float playerPitch;

    public PacketShoot(float playerYaw, float playerPitch
//            int entityId, double hitVecX, double hitVecY, double hitVecZ, float damage
    )
    {
//        this.entityId = entityId;
//        this.hitVecX = hitVecX;
//        this.hitVecY = hitVecY;
//        this.hitVecZ = hitVecZ;
//        this.damage = damage;
        this.playerYaw = playerYaw;
        this.playerPitch = playerPitch;
    }

    public static void encode(PacketShoot msg, PacketBuffer buf)
    {
//        buf.writeVarInt(msg.entityId);
//        buf.writeDouble(msg.hitVecX);
//        buf.writeDouble(msg.hitVecY);
//        buf.writeDouble(msg.hitVecZ);
//        buf.writeFloat(msg.damage);
        buf.writeFloat(msg.playerYaw);
        buf.writeFloat(msg.playerPitch);
    }

    public static PacketShoot decode(PacketBuffer buf)
    {
        return new PacketShoot(buf.readFloat(), buf.readFloat());
//        return new PacketShoot(buf.readVarInt(), buf.readDouble(), buf.readDouble(), buf.readDouble(), buf.readFloat());
    }

    public static void handle(PacketShoot msg, Supplier<NetworkEvent.Context> ctx)
    {
        if (ctx.get().getDirection().getReceptionSide().isServer())
        {
            ctx.get().enqueueWork(() ->
            {
                ServerPlayerEntity player = ctx.get().getSender();
                ItemStack stack = player.inventory.getCurrentItem();
                if (stack.isEmpty())
                {
                    System.out.println("ya fucked up");
                    return;
                }

//                Entity e = Minecraft.getInstance().world.getEntityByID(msg.entityId);
//                if (e instanceof LivingEntity)
//                {
//                    GunHelper.shootEntity(player, (LivingEntity) e, new Vector3d(msg.hitVecX, msg.hitVecY, msg.hitVecZ), msg.damage);
//                }
                GunHelper.shoot(msg, player);
            });

            ctx.get().setPacketHandled(true);
        }
    }

    public float getPlayerYaw()
    {
        return playerYaw;
    }

    public float getPlayerPitch()
    {
        return playerPitch;
    }
}
