package at.helpch.placeholderapi.expansion.permission;

import net.milkbowl.vault.permission.Permission;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.jetbrains.annotations.NotNull;

public class PermissionHandler {

    private boolean hasVaultSupport;
    private Permission vaultPermission;

    public PermissionHandler() {
        if (Bukkit.getPluginManager().isPluginEnabled("Vault")) {
            final RegisteredServiceProvider<Permission> rsp = Bukkit.getServicesManager().getRegistration(Permission.class);

            if (rsp != null) {
                this.hasVaultSupport = true;
                this.vaultPermission = rsp.getProvider();
            }
        }
    }

    public boolean has(@NotNull final OfflinePlayer player, @NotNull final String permission) {
        if (this.hasVaultSupport) {
            if (player.isOnline()) {
                return this.vaultPermission.has(player.getPlayer(), permission);
            }

            return this.vaultPermission.playerHas(null, player, permission);
        }

        return player.isOnline() && player.getPlayer().hasPermission(permission);
    }

}
