package arcaratus.gunz.common.network;

import arcaratus.gunz.common.item.IGun;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class PacketReload
{
    public static void encode(PacketReload msg, PacketBuffer buf) {}

    public static PacketReload decode(PacketBuffer buf)
    {
        return new PacketReload();
    }

    public static void handle(PacketReload msg, Supplier<NetworkEvent.Context> ctx)
    {
        if (ctx.get().getDirection().getReceptionSide().isServer())
        {
            ctx.get().enqueueWork(() ->
            {
                ServerPlayerEntity player = ctx.get().getSender();
                ItemStack stack = player.inventory.getCurrentItem();
                if (stack.isEmpty() || !(stack.getItem() instanceof IGun) || ((IGun) stack.getItem()).getReload(stack) >= 0)
                {
                    return;
                }

                ((IGun) stack.getItem()).reload(stack, player);
            });

            ctx.get().setPacketHandled(true);
        }
    }
}
