package lol.nils.raconutils.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.util.EnumChatFormatting;

import java.awt.*;

public abstract class Module {
    private final String name;
    private boolean enabled;
    private boolean hovered;

    public Module(String name, boolean defaultState) {
        this.name = name;
        this.enabled = defaultState;
        this.hovered = false;
    }

    public void draw(int x, int y, int width, int mouseX, int mouseY) {
        hovered = mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + 15;

        Gui.drawRect(x, y, x + width, y + 15, 
            hovered ? new Color(40, 40, 40, 200).getRGB() : new Color(30, 30, 30, 200).getRGB());

        Minecraft.getMinecraft().fontRendererObj.drawString(name, x + 3, y + 3, -1);

        int buttonX = x + width - 15;
        Gui.drawRect(buttonX, y + 2, buttonX + 11, y + 13, 
            enabled ? new Color(0, 255, 0, 200).getRGB() : new Color(255, 0, 0, 200).getRGB());
    }

    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (mouseButton == 0 && hovered) {
            enabled = !enabled;
            onToggle(enabled);
        }
    }

    public abstract void onToggle(boolean state);

    public boolean isEnabled() {
        return enabled;
    }

    public String getName() {
        return name;
    }
} 