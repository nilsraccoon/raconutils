package lol.nils.raconutils.gui;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;

import java.util.HashMap;
import java.util.Map;

public class ModuleConfig {
    private static Configuration config;
    private static final Map<String, Boolean> moduleStates = new HashMap<>();

    public static void initialize(Configuration configuration) {
        config = configuration;
        loadStates();
    }

    public static void loadStates() {
        Property prop = config.get("modules", "moduleStates", new String[0], "Module states");
        String[] states = prop.getStringList();
        
        for (String state : states) {
            String[] parts = state.split(":");
            if (parts.length == 2) {
                moduleStates.put(parts[0], Boolean.parseBoolean(parts[1]));
            }
        }
    }

    public static void saveStates() {
        String[] states = new String[moduleStates.size()];
        int i = 0;
        for (Map.Entry<String, Boolean> entry : moduleStates.entrySet()) {
            states[i++] = entry.getKey() + ":" + entry.getValue();
        }
        
        config.get("modules", "moduleStates", new String[0], "Module states").set(states);
        config.save();
    }

    public static boolean getState(String moduleName) {
        return moduleStates.getOrDefault(moduleName, false);
    }

    public static void setState(String moduleName, boolean state) {
        moduleStates.put(moduleName, state);
        saveStates();
    }
} 