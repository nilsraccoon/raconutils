package lol.nils.raconutils.event;

import lol.nils.raconutils.RaconUtils;
import lol.nils.raconutils.gui.ModuleConfig;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static lol.nils.raconutils.RaconUtils.prefix;

public class AutoFriend {
    public static boolean isEnabled() {
        return enabled;
    }

    public static void setEnabled(boolean enabled) {
        AutoFriend.enabled = enabled;
        ModuleConfig.setState("AutoFriend", enabled);
    }

    // §9§m-----------------------------------------------------§r§9
    //§r§eFriend request from §r§b[MVP§r§0+§r§b] AngelosGR§r§9
    //§r§a§l[ACCEPT]§r§8 - §r§c§l[DENY]§r§8 - §r§7§l[BLOCK]§r§9
    //§r§9§m-----------------------------------------------------§r
    private static boolean enabled = true;
    private static final Pattern COLOR_CODE_PATTERN = Pattern.compile("§[0-9A-FK-ORa-fk-or]");

    public static String stripColorCodes(String input) {
        return COLOR_CODE_PATTERN.matcher(input).replaceAll("");
    }

    public static void registerCommand() {
        ClientCommandHandler.instance.registerCommand(new CommandBase() {
            @Override
            public String getCommandName() {
                return "autofriend";
            }

            @Override
            public String getCommandUsage(ICommandSender sender) {
                return "/autofriend [toggle]";
            }

            @Override
            public void processCommand(ICommandSender sender, String[] args) throws CommandException {
                if (args.length > 0 && args[0].equalsIgnoreCase("toggle")) {
                    setEnabled(!enabled);
                    RaconUtils.config.get("autofriend", "enabled", true).set(enabled);
                    RaconUtils.config.save();
                    sender.addChatMessage(new ChatComponentText(prefix +
                            EnumChatFormatting.WHITE + "AutoFriend is now " +
                            (enabled ? EnumChatFormatting.GREEN + "enabled" : EnumChatFormatting.RED + "disabled")
                    ));
                } else {
                    sender.addChatMessage(new ChatComponentText(prefix +
                            EnumChatFormatting.WHITE + "AutoFriend is currently " +
                            (enabled ? EnumChatFormatting.GREEN + "enabled" : EnumChatFormatting.RED + "disabled")
                    ));
                }
            }

            @Override
            public int getRequiredPermissionLevel() {
                return 0;
            }
        });
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    public void onServerChat(ClientChatReceivedEvent event) {
        if (!enabled) return;
        String cleanMessage = stripColorCodes(event.message.getFormattedText());

        if (cleanMessage.startsWith("-----------------------------------------------------") && cleanMessage.contains("Friend request from")) {
            Pattern pattern = Pattern.compile("Friend request from (?:\\[[^\\]]+\\] )*(\\w+)");
            Matcher matcher = pattern.matcher(cleanMessage);

            if(matcher.find()) {
                String username = matcher.group(1);
                Minecraft.getMinecraft().thePlayer.sendChatMessage("/friend add " + username);
            }
        }
    }
}