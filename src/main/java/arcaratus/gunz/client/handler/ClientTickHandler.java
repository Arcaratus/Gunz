package arcaratus.gunz.client.handler;

import arcaratus.gunz.common.item.IGun;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class ClientTickHandler
{
    @SubscribeEvent
    public static void onClickInputEvent(InputEvent.ClickInputEvent event)
    {
        PlayerEntity player = Minecraft.getInstance().player;
        if (event.isAttack() && player != null)
        {
            ItemStack currentStack = player.inventory.getCurrentItem();
            if (!currentStack.isEmpty() && currentStack.getItem() instanceof IGun)
            {
                event.setCanceled(true);
                event.setSwingHand(false);
            }
        }
    }
}
