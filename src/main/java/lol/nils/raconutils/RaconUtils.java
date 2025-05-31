package lol.nils.raconutils;

import lol.nils.raconutils.event.AutoFriend;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(modid = lol.nils.raconutils.RaconUtils.MODID, version = lol.nils.raconutils.RaconUtils.VERSION)
public class RaconUtils
{
    public static final String MODID = "raconutils";
    public static final String VERSION = "1.0";
    Logger logger = LogManager.getLogger(RaconUtils.MODID);
    @EventHandler
    public void init(FMLInitializationEvent event)
    {
        MinecraftForge.EVENT_BUS.register(new AutoFriend());
        logger.debug("Registered AutoFriend event listener");
    }
}
