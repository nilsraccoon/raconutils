package lol.nils.raconutils.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import org.lwjgl.input.Keyboard;

public class KeybindHandler {
    private static final KeyBinding OPEN_GUI = new KeyBinding("Open ClickGUI", Keyboard.KEY_END, "RaconUtils");

    public static void initialize() {
        ClientRegistry.registerKeyBinding(OPEN_GUI);
    }

    @SubscribeEvent
    public void onKeyInput(InputEvent.KeyInputEvent event) {
        if (OPEN_GUI.isPressed()) {
            Minecraft mc = Minecraft.getMinecraft();
            if (mc.currentScreen instanceof ClickGui) {
                mc.displayGuiScreen(null);
            } else {
                mc.displayGuiScreen(new ClickGui());
            }
        }
    }
} 