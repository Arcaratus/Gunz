package arcaratus.gunz.client.handler;

import arcaratus.gunz.common.item.IGun;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.MainWindow;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Some code stolen from Mekanism
 * https://github.com/mekanism/Mekanism
 */
public class RenderTickHandler
{
    private static final Minecraft minecraft = Minecraft.getInstance();
    private static ClientPlayerEntity player = minecraft.player;

    private static final EquipmentSlotType[] EQUIPMENT_ORDER = new EquipmentSlotType[] {EquipmentSlotType.MAINHAND, EquipmentSlotType.OFFHAND};
    private static final float HUD_SCALE = 0.6F;

    @SubscribeEvent
    public static void renderOverlay(RenderGameOverlayEvent.Post event)
    {
        if (event.getType() == RenderGameOverlayEvent.ElementType.HOTBAR)
        {
            if (!player.isSpectator())
            {
                int count = 0;
                Map<EquipmentSlotType, List<ITextComponent>> renderStrings = new LinkedHashMap<>();
                for (EquipmentSlotType slotType : EQUIPMENT_ORDER)
                {
                    ItemStack stack = player.getItemStackFromSlot(slotType);
                    if (stack.getItem() instanceof IGun)
                    {
                        IGun gun = (IGun) stack.getItem();
                        List<ITextComponent> list = new ArrayList<>();
                        list.add(new TranslationTextComponent("gunz.ammo").append(new StringTextComponent(gun.getAmmo(stack) + " / " + gun.getGunz(stack).maxAmmo() + " : " + gun.getStoredAmmo(stack))));
                        renderStrings.put(slotType, list);
                        count++;
                    }
                }

                int start = renderStrings.size() * 2 + count * 9;
                boolean left = true;
                MainWindow window = event.getWindow();
                int y = window.getScaledHeight();
                MatrixStack matrix = event.getMatrixStack();
                matrix.push();
                matrix.scale(HUD_SCALE, HUD_SCALE, HUD_SCALE);
                for (Map.Entry<EquipmentSlotType, List<ITextComponent>> entry : renderStrings.entrySet())
                {
                    for (ITextComponent text : entry.getValue())
                    {
                        drawString(window, matrix, text, left, (int) (y * (1 / HUD_SCALE)) - start, 0xC8C8C8);
                        start -= 9;
                    }
                    start -= 2;
                }
                matrix.pop();
            }
        }
    }

    private static void drawString(MainWindow window, MatrixStack matrix, ITextComponent text, boolean leftSide, int y, int color)
    {
        FontRenderer font = minecraft.fontRenderer;
        // Note that we always offset by 2 pixels when left or right aligned
        if (leftSide)
        {
            font.func_243246_a(matrix, text, 2, y, color);
        }
        else
        {
            int width = font.getStringPropertyWidth(text) + 2;
            font.func_243246_a(matrix, text, window.getScaledWidth() - width, y, color);
        }
    }
}
