package lol.nils.raconutils.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Panel {
    private final String title;
    private int x, y;
    private final int width;
    private final int height;
    private boolean expanded;
    private final List<Module> modules;
    private int scrollOffset;
    private final Minecraft mc = Minecraft.getMinecraft();

    public Panel(String title, int x, int y, int width, int height) {
        this.title = title;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.expanded = true;
        this.modules = new ArrayList<>();
        this.scrollOffset = 0;
    }

    public void draw(int mouseX, int mouseY) {
        Gui.drawRect(x, y, x + width, y + height, new Color(30, 30, 30, 200).getRGB());
        mc.fontRendererObj.drawString(title, x + 3, y + 3, -1);

        if (expanded) {
            Gui.drawRect(x, y + height, x + width, y + height + (modules.size() * 15),
                new Color(20, 20, 20, 200).getRGB());

            int moduleY = y + height;
            for (int i = scrollOffset; i < modules.size() && moduleY < y + height + 150; i++) {
                Module module = modules.get(i);
                module.draw(x, moduleY, width, mouseX, mouseY);
                moduleY += 15;
            }
        }
    }

    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (isMouseOver(mouseX, mouseY)) {
            if (mouseButton == 0) {
                expanded = !expanded;
            }
        } else if (expanded) {
            int moduleY = y + height;
            for (int i = scrollOffset; i < modules.size() && moduleY < y + height + 150; i++) {
                Module module = modules.get(i);
                if (mouseX >= x && mouseX <= x + width && 
                    mouseY >= moduleY && mouseY <= moduleY + 15) {
                    module.mouseClicked(mouseX, mouseY, mouseButton);
                    break;
                }
                moduleY += 15;
            }
        }
    }

    public void handleScroll(int dWheel) {
        if (expanded) {
            scrollOffset = Math.max(0, Math.min(scrollOffset - (dWheel > 0 ? 1 : -1), 
                Math.max(0, modules.size() - 10)));
        }
    }

    public boolean isMouseOver(int mouseX, int mouseY) {
        return mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + height;
    }

    public void addModule(Module module) {
        modules.add(module);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
} 