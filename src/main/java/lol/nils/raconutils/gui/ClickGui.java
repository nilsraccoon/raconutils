package lol.nils.raconutils.gui;

import lol.nils.raconutils.event.AutoFriend;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ClickGui extends GuiScreen {
    private static final ResourceLocation BACKGROUND = new ResourceLocation("raconutils", "textures/gui/background.png");
    private final List<Panel> panels;
    private Panel draggedPanel;
    private int dragX, dragY;
    private final ExecutorService executor;

    public ClickGui() {
        this.panels = new ArrayList<>();
        this.executor = Executors.newSingleThreadExecutor();
        initializePanels();
    }

    private void initializePanels() {
        Panel fun = new Panel("Fun", 10, 10, 100, 15);
        
        fun.addModule(new Module("AutoFriend", ModuleConfig.getState("AutoFriend")) {
            @Override
            public void onToggle(boolean state) {
                ModuleConfig.setState("AutoFriend", state);
                AutoFriend.setEnabled(state);
            }
        });

        fun.addModule(new Module("AutoBlorp", ModuleConfig.getState("AutoBlorp")) {
            @Override
            public void onToggle(boolean state) {
                ModuleConfig.setState("AutoBlorp", state);
                executor.submit(() -> {
                    try {
                        java.net.URL url = new java.net.URL("http://localhost:50931/toggle?mod=autoblorp&value=" + state);
                        java.net.HttpURLConnection conn = (java.net.HttpURLConnection) url.openConnection();
                        conn.setRequestMethod("GET");
                        conn.getResponseCode();
                        conn.disconnect();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
            }
        });

        panels.add(fun);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        ScaledResolution sr = new ScaledResolution(mc);
        drawDefaultBackground();
        for (Panel panel : panels) {
            panel.draw(mouseX, mouseY);
        }
        if (draggedPanel != null) {
            draggedPanel.draw(mouseX, mouseY);
        }
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);

        if (mouseButton == 0) {
            for (Panel panel : panels) {
                if (panel.isMouseOver(mouseX, mouseY)) {
                    draggedPanel = panel;
                    dragX = mouseX - panel.getX();
                    dragY = mouseY - panel.getY();
                    break;
                }
            }
        }
        for (Panel panel : panels) {
            panel.mouseClicked(mouseX, mouseY, mouseButton);
        }
    }

    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state) {
        super.mouseReleased(mouseX, mouseY, state);
        draggedPanel = null;
    }

    @Override
    public void handleMouseInput() throws IOException {
        super.handleMouseInput();
        int dWheel = Mouse.getDWheel();
        if (dWheel != 0) {
            for (Panel panel : panels) {
                if (panel.isMouseOver(Mouse.getX(), Mouse.getY())) {
                    panel.handleScroll(dWheel);
                }
            }
        }
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

    @Override
    public void onGuiClosed() {
        super.onGuiClosed();
        executor.shutdown();
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        if (keyCode == org.lwjgl.input.Keyboard.KEY_END) {
            mc.displayGuiScreen(null);
            return;
        }
        super.keyTyped(typedChar, keyCode);
    }
} 