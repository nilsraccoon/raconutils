package lol.nils.raconutils.event;

import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AutoFriend {
    // §9§m-----------------------------------------------------§r§9
    //§r§eFriend request from §r§b[MVP§r§0+§r§b] AngelosGR§r§9
    //§r§a§l[ACCEPT]§r§8 - §r§c§l[DENY]§r§8 - §r§7§l[BLOCK]§r§9
    //§r§9§m-----------------------------------------------------§r
    private static final Pattern COLOR_CODE_PATTERN = Pattern.compile("§[0-9A-FK-ORa-fk-or]");

    public static String stripColorCodes(String input) {
        return COLOR_CODE_PATTERN.matcher(input).replaceAll("");
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    public void onServerChat(ClientChatReceivedEvent event) {
        String cleanMessage = stripColorCodes(event.message.getFormattedText());

        if (cleanMessage.startsWith("-----------------------------------------------------") && cleanMessage.contains("Friend request from")) {
            // todo config and toggle command
            Pattern pattern = Pattern.compile("Friend request from (?:\\[[^\\]]+\\] )*(\\w+)");
            Matcher matcher = pattern.matcher(cleanMessage);

            if(matcher.find()) {
                String username = matcher.group(1);
                Minecraft.getMinecraft().thePlayer.sendChatMessage("/friend add " + username);
            }
        }
    }

}
