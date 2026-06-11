package com.andrei1058.spigot.sidebar.v1_21_R2;

import com.andrei1058.spigot.sidebar.*;
import dev.andrei1058.spigot.sidebar.cmn1.PlayerListImplCmn1;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;

@SuppressWarnings("unused")
public class PlayerListImpl implements VersionedTabGroup {

    private final String identifier;

    private Component prefix;
    private Component suffix;

    private PlayerTab.PushingRule pushingRule;
    private PlayerTab.NameTagVisibility visibility;

    private final PlayerListImplCmn1 handle;

    public PlayerListImpl(
            @NotNull WrappedSidebar sidebar,
            String identifier,
            SidebarLine prefix,
            SidebarLine suffix,
            PlayerTab.PushingRule pushingRule,
            PlayerTab.NameTagVisibility nameTagVisibility,
            @Nullable Collection<PlaceholderProvider> placeholders
    ) {
        this.handle = new PlayerListImplCmn1(
                sidebar,
                identifier,
                prefix,
                suffix,
                pushingRule,
                nameTagVisibility,
                placeholders
        );

        this.identifier = identifier;
    }

    @Override
    public void sendCreateToPlayer(Player player) {
        Scoreboard board = player.getScoreboard();

        if (board.getTeam(getIdentifier()) == null) {
            board.registerNewTeam(getIdentifier());
        }

        updateTeam(player);
    }

    @Override
    public void sendUserCreateToReceivers(@NotNull Player player) {
        for (Player receiver : handle.getSidebar().getReceivers()) {

            Team team = receiver.getScoreboard().getTeam(getIdentifier());

            if (team == null) {
                team = receiver.getScoreboard().registerNewTeam(getIdentifier());
            }

            team.addEntry(player.getName());
        }
    }

    @Override
    public void sendUpdateToReceivers() {
        handle.getSidebar()
                .getReceivers()
                .forEach(this::updateTeam);
    }

    @Override
    public void sendRemoveToReceivers() {
        for (Player player : handle.getSidebar().getReceivers()) {

            Team team = player.getScoreboard()
                    .getTeam(getIdentifier());

            if (team != null) {
                team.unregister();
            }
        }
    }

    @Override
    public boolean refreshContent() {
        return handle.refreshContent();
    }


    @Override
    public void add(@NotNull Player player) {
        for (Player receiver : handle.getSidebar().getReceivers()) {

            Team team = receiver.getScoreboard()
                    .getTeam(getIdentifier());

            if (team == null) {
                continue;
            }

            team.addEntry(player.getName());
        }
    }

    @Override
    public void remove(@NotNull Player player) {
        for (Player receiver : handle.getSidebar().getReceivers()) {

            Team team = receiver.getScoreboard()
                    .getTeam(getIdentifier());

            if (team != null) {
                team.removeEntry(player.getName());
            }
        }
    }

    @Override
    public void setSubject(@Nullable Player player) {
        this.handle.setPapiSubject(player);
    }

    @Override
    public @Nullable Player getSubject() {
        return this.handle.getPapiSubject();
    }

    @Override
    public void setPushingRule(@NotNull PushingRule rule) {
        this.handle.setPushingRule(rule);
        sendUpdateToReceivers();
    }

    @Override
    public void setNameTagVisibility(
            @NotNull NameTagVisibility nameTagVisibility
    ) {
        this.handle.setNameTagVisibility(nameTagVisibility);
        sendUpdateToReceivers();
    }

    @Override
    public String getIdentifier() {
        return handle.getId();
    }

    private void updateTeam(Player player) {
        Team team = player.getScoreboard().getTeam(getIdentifier());

        if (team == null) {
            return;
        }

        team.prefix(Component.text(handle.getPrefixText()));
        team.suffix(Component.text(handle.getSuffixText()));

        team.setOption(
                Team.Option.COLLISION_RULE,
                convertCollision(handle.getPushingRule())
        );

        team.setOption(
                Team.Option.NAME_TAG_VISIBILITY,
                convertVisibility(handle.getNameTagVisibility())
        );
    }

    private Team.OptionStatus convertCollision(PushingRule rule) {
        return switch (rule) {
            case ALWAYS -> Team.OptionStatus.ALWAYS;
            case NEVER -> Team.OptionStatus.NEVER;
            case PUSH_OTHER_TEAMS -> Team.OptionStatus.FOR_OTHER_TEAMS;
            case PUSH_OWN_TEAM -> Team.OptionStatus.FOR_OWN_TEAM;
        };
    }

    private Team.OptionStatus convertVisibility(
            NameTagVisibility visibility
    ) {
        return switch (visibility) {
            case ALWAYS -> Team.OptionStatus.ALWAYS;
            case NEVER -> Team.OptionStatus.NEVER;
            case HIDE_FOR_OTHER_TEAMS -> Team.OptionStatus.FOR_OTHER_TEAMS;
            case HIDE_FOR_OWN_TEAM -> Team.OptionStatus.FOR_OWN_TEAM;
        };
    }
}
