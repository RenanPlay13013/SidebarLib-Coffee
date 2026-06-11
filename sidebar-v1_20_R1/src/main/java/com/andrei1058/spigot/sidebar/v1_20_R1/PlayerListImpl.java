package com.andrei1058.spigot.sidebar.v1_20_R1;

import com.andrei1058.spigot.sidebar.*;
import net.minecraft.network.chat.Component;
import net.minecraft.world.scores.Team;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.Collection;

public class PlayerListImpl implements VersionedTabGroup {

    private Team.CollisionRule pushingRule;
    private final SidebarLine prefix;
    private Component prefixComp = Component.literal("");
    private final SidebarLine suffix;
    private Component suffixComp = Component.literal("");
    private final WrappedSidebar sidebar;
    private final String id;
    private Team.Visibility nameTagVisibility = Team.Visibility.ALWAYS;
    private Player papiSubject = null;
    private final Collection<PlaceholderProvider> placeholders;

    public PlayerListImpl(
            @NotNull WrappedSidebar sidebar,
            String identifier,
            SidebarLine prefix,
            SidebarLine suffix,
            PushingRule pushingRule,
            NameTagVisibility nameTagVisibility,
            @Nullable Collection<PlaceholderProvider> placeholders
    ) {
        this.suffix = suffix;
        this.prefix = prefix;
        this.sidebar = sidebar;
        this.setPushingRule(pushingRule);
        this.setNameTagVisibility(nameTagVisibility);
        this.id = identifier;
        this.placeholders = placeholders;
    }


    @Override
    public void sendCreateToPlayer(Player player) {

    }

    @Override
    public void sendUserCreateToReceivers(Player player) {

    }

    @Override
    public void sendUpdateToReceivers() {

    }

    @Override
    public void sendRemoveToReceivers() {

    }

    @Override
    public boolean refreshContent() {
        var newPrefix = prefix.getTrimReplacePlaceholders(getSubject(), 256, this.placeholders);
        var newSuffix = suffix.getTrimReplacePlaceholders(getSubject(), 256, this.placeholders);

        if (newPrefix.equals(prefixComp.getString()) && newSuffix.equals(suffixComp.getString())) {
            return false;
        }

        this.prefixComp = Component.literal(newPrefix);
        this.suffixComp = Component.literal(newSuffix);
        return true;
    }

    @Override
    public String getIdentifier() {
        return id;
    }

    @Override
    public void add(Player player) {

    }

    @Override
    public void remove(Player player) {

    }

    @Override
    public void setSubject(@Nullable Player papiSubject) {
        this.papiSubject = papiSubject;
    }

    @Override
    public @org.jetbrains.annotations.Nullable Player getSubject() {
        return papiSubject;
    }

    @Override
    public void setPushingRule(@NotNull PushingRule rule) {
        switch (rule) {
            case NEVER -> this.pushingRule = Team.CollisionRule.NEVER;
            case ALWAYS -> this.pushingRule = Team.CollisionRule.ALWAYS;
            case PUSH_OTHER_TEAMS -> this.pushingRule = Team.CollisionRule.PUSH_OTHER_TEAMS;
            case PUSH_OWN_TEAM -> this.pushingRule = Team.CollisionRule.PUSH_OWN_TEAM;
        }
        if (null != this.id) {
            sendUpdateToReceivers();
        }
    }

    @Override
    public void setNameTagVisibility(@NotNull NameTagVisibility nameTagVisibility) {
        switch (nameTagVisibility) {
            case NEVER -> this.nameTagVisibility = Team.Visibility.NEVER;
            case ALWAYS -> this.nameTagVisibility = Team.Visibility.ALWAYS;
            case HIDE_FOR_OTHER_TEAMS -> this.nameTagVisibility = Team.Visibility.HIDE_FOR_OTHER_TEAMS;
            case HIDE_FOR_OWN_TEAM -> this.nameTagVisibility = Team.Visibility.HIDE_FOR_OWN_TEAM;
        }
        if (null != id){
            sendUpdateToReceivers();
        }
    }
}
