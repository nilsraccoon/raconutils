package lol.nils.raconutils;

import lol.nils.raconutils.event.AutoFriend;
import lol.nils.raconutils.event.BlockManager;
import lol.nils.raconutils.gui.KeybindHandler;
import lol.nils.raconutils.gui.ModuleConfig;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.common.config.Configuration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(modid = lol.nils.raconutils.RaconUtils.MODID, version = lol.nils.raconutils.RaconUtils.VERSION)
public class RaconUtils
{
    public static final String MODID = "raconutils";
    public static final String VERSION = "1.1";
    public static final String prefix = EnumChatFormatting.GRAY + "[" + EnumChatFormatting.DARK_PURPLE + "rac" + EnumChatFormatting.LIGHT_PURPLE + "onut" + EnumChatFormatting.WHITE + "ils" + EnumChatFormatting.GRAY + "] ";
    public static Logger logger = LogManager.getLogger(RaconUtils.MODID);
    public static Configuration config;
    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        config = new Configuration(event.getSuggestedConfigurationFile());
        loadConfig();
        ModuleConfig.initialize(config);
    }

    private void loadConfig() {
        config.load();
        
        if (config.hasChanged()) {
            config.save();
        }
    }

    @EventHandler
    public void init(FMLInitializationEvent event)
    {
        MinecraftForge.EVENT_BUS.register(new AutoFriend());
        AutoFriend.registerCommand();
        BlockManager.initialize(config);
        KeybindHandler.initialize();
        MinecraftForge.EVENT_BUS.register(new KeybindHandler());
        logger.debug("Registered AutoFriend event listener and command");
        logger.debug("Registered BlockManager");
        logger.debug("Registered KeybindHandler");
    }
}