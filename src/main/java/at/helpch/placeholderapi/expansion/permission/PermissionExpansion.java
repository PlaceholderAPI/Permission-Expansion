package at.helpch.placeholderapi.expansion.permission;

import me.clip.placeholderapi.PlaceholderAPIPlugin;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

@SuppressWarnings("unused")
public final class PermissionExpansion extends PlaceholderExpansion {

    private final PermissionHandler permissionHandler = new PermissionHandler();
    private final List<String> placeholders = Arrays.asList(
            "%permission_count_permission nodes%",
            "%permission_has_permission.node%",
            "%permission_has-all_permission nodes%",
            "%permission_has-any_permission nodes%",
            "%permission_has-none_permission nodes%",
            "%permission_missing_permission nodes%"
    );

    @Contract("_ -> !null")
    @NotNull
    private String bool(final boolean bool) {
        return bool ? PlaceholderAPIPlugin.booleanTrue() : PlaceholderAPIPlugin.booleanFalse();
    }

    @Contract("_ -> !null")
    @NotNull
    private Stream<String> stream(@NotNull final String permissions) {
        return Arrays.stream(permissions.split(" "));
    }

    @Override
    public @NotNull String getIdentifier() {
        return "permission";
    }

    @Override
    public @NotNull String getAuthor() {
        return "HelpChat";
    }

    @Override
    public @NotNull String getVersion() {
        return "1.0.0";
    }

    @NotNull
    @Override
    public List<String> getPlaceholders() {
        return placeholders;
    }

    @Override
    public boolean canRegister() {
        return true;
    }

    @Contract("null, _ -> null")
    @Override
    public @Nullable String onRequest(@Nullable final OfflinePlayer player, @NotNull final String params) {
        if (player == null) {
            return null;
        }

        final String[] parts = params.split("_", 2);

        if (parts.length == 1) {
            return null;
        }

        final String action = parts[0].toLowerCase();
        final String permissions = parts[1];

        if (permissions.length() == 0) {
            return null;
        }

        switch (action) {
            case "count": {
                return Long.toString(stream(permissions).filter(permission -> permissionHandler.has(player, permission)).count());
            }

            case "has": {
                return bool(permissionHandler.has(player, permissions));
            }

            case "has-all": {
                return bool(stream(permissions).allMatch(permission -> permissionHandler.has(player, permission)));
            }

            case "has-any": {
                return bool(stream(permissions).anyMatch(permission -> permissionHandler.has(player, permission)));
            }

            case "has-none": {
                return bool(stream(permissions).noneMatch(permission -> permissionHandler.has(player, permission)));
            }

            case "missing": {
                return Long.toString(stream(permissions).filter(permission -> !permissionHandler.has(player, permission)).count());
            }

            default: {
                return null;
            }
        }
    }

}
