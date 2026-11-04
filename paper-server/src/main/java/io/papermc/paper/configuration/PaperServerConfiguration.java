package io.papermc.paper.configuration;

public class PaperServerConfiguration implements ServerConfiguration {

    @Override
    public boolean isProxyOnlineMode() {
        return org.bukkit.Bukkit.getOnlineMode();
    }

    @Override
    public boolean isProxyEnabled() {
        return false; // Tissue - no proxy support
    }
}
