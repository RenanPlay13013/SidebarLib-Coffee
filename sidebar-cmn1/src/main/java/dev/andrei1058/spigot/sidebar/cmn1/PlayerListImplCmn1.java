package dev.andrei1058.spigot.sidebar.cmn1;

import com.andrei1058.spigot.sidebar.*;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.Collection;

public class PlayerListImplCmn1 {

    private PlayerTab.PushingRule pushingRule;
    private PlayerTab.NameTagVisibility nameTagVisibility;

    private final SidebarLine prefix;
    private String prefixText = " ";

    private final SidebarLine suffix;
    private String suffixText = " ";

    private final WrappedSidebar sidebar;
    private final String id;

    private Player papiSubject;
    private final Collection<PlaceholderProvider> placeholders;

    public PlayerListImplCmn1(
            @NotNull WrappedSidebar sidebar,
            String identifier,
            SidebarLine prefix,
            SidebarLine suffix,
            PlayerTab.PushingRule pushingRule,
            PlayerTab.NameTagVisibility nameTagVisibility,
            @Nullable Collection<PlaceholderProvider> placeholders
    ) {
        this.sidebar = sidebar;
        this.id = identifier;
        this.prefix = prefix;
        this.suffix = suffix;
        this.pushingRule = pushingRule;
        this.nameTagVisibility = nameTagVisibility;
        this.placeholders = placeholders;
    }

    public PlayerTab.PushingRule getPushingRule() {
        return pushingRule;
    }

    public void setPushingRule(PlayerTab.PushingRule pushingRule) {
        this.pushingRule = pushingRule;
    }

    public PlayerTab.NameTagVisibility getNameTagVisibility() {
        return nameTagVisibility;
    }

    public void setNameTagVisibility(PlayerTab.NameTagVisibility nameTagVisibility) {
        this.nameTagVisibility = nameTagVisibility;
    }

    public SidebarLine getPrefix() {
        return prefix;
    }

    public String getPrefixText() {
        return prefixText;
    }

    public void setPrefixText(String prefixText) {
        this.prefixText = prefixText;
    }

    public SidebarLine getSuffix() {
        return suffix;
    }

    public String getSuffixText() {
        return suffixText;
    }

    public void setSuffixText(String suffixText) {
        this.suffixText = suffixText;
    }

    public WrappedSidebar getSidebar() {
        return sidebar;
    }

    public String getId() {
        return id;
    }

    public Player getPapiSubject() {
        return papiSubject;
    }

    public void setPapiSubject(Player papiSubject) {
        this.papiSubject = papiSubject;
    }

    public Collection<PlaceholderProvider> getPlaceholders() {
        return placeholders;
    }

    public boolean refreshContent() {
        String newPrefix = prefix.getTrimReplacePlaceholders(
                papiSubject,
                256,
                placeholders
        );

        String newSuffix = suffix.getTrimReplacePlaceholders(
                papiSubject,
                256,
                placeholders
        );

        if (newPrefix.equals(prefixText)
                && newSuffix.equals(suffixText)) {
            return false;
        }

        prefixText = newPrefix;
        suffixText = newSuffix;

        return true;
    }
}