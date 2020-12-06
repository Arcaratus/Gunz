package arcaratus.gunz.client.handler;

import arcaratus.gunz.common.Gunz;
import arcaratus.gunz.common.item.IGun;
import arcaratus.gunz.common.network.PacketHandler;
import arcaratus.gunz.common.network.PacketReload;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.client.util.InputMappings;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import org.lwjgl.glfw.GLFW;

public class GunzKeyHandler
{
    public static final KeyBinding KEY_RELOAD = new KeyBinding("key.gunz.reload", KeyConflictContext.GUI,
            InputMappings.Type.KEYSYM, GLFW.GLFW_KEY_R, Gunz.MOD_ID);

    public static void registerKeys()
    {
        ClientRegistry.registerKeyBinding(KEY_RELOAD);
    }

    public static void onKeyInput(InputEvent.KeyInputEvent event)
    {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.world != null)
        {
            if (KEY_RELOAD.getKey().getKeyCode() == event.getKey())
            {
                ItemStack stack = minecraft.player.inventory.getCurrentItem();
                if (!stack.isEmpty() && stack.getItem() instanceof IGun)
                {
                    PacketHandler.sendToServer(new PacketReload());
                }
            }
        }
    }
}
