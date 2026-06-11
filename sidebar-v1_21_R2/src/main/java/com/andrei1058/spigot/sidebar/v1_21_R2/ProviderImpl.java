package com.andrei1058.spigot.sidebar.v1_21_R2;

import com.andrei1058.spigot.sidebar.*;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Criteria;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;

@SuppressWarnings("unused")
public class ProviderImpl extends SidebarProvider {
    private static SidebarProvider instance;

    @Override
    public Sidebar createSidebar(SidebarLine title, Collection<SidebarLine> lines, Collection<PlaceholderProvider> placeholderProviders) {
        return new SidebarImpl(title, lines, placeholderProviders);
    }

    @Override
    public SidebarObjective createObjective(
            @NotNull WrappedSidebar sidebar,
            String name,
            boolean health,
            SidebarLine title,
            int type
    ) {
        return ((SidebarImpl) sidebar).createObjective(
                name,
                health ? Criteria.HEALTH : Criteria.DUMMY,
                title,
                type
        );
    }

    @Override
    public ScoreLine createScoreLine(WrappedSidebar sidebar, SidebarLine line, int score, String color) {
        return ((SidebarImpl)sidebar).createScore(line, score, color);
    }

    @Override
    public void sendScore(
            @NotNull WrappedSidebar sidebar,
            String playerName,
            int score
    ) {
        if (sidebar.getHealthObjective() == null) {
            return;
        }

        String objectiveName = sidebar.getHealthObjective().getName();

        for (Player player : sidebar.getReceivers()) {
            org.bukkit.scoreboard.Objective objective = player
                    .getScoreboard()
                    .getObjective(objectiveName);

            if (objective != null) {
                objective.getScore(playerName).setScore(score);
            }
        }
    }

    @Override
    public VersionedTabGroup createPlayerTab(WrappedSidebar sidebar, String identifier, SidebarLine prefix, SidebarLine suffix, PlayerTab.PushingRule pushingRule, PlayerTab.NameTagVisibility nameTagVisibility, @Nullable Collection<PlaceholderProvider> placeholders) {
        return new PlayerListImpl(sidebar, identifier, prefix, suffix, pushingRule, nameTagVisibility, placeholders);
    }

    @Override
    public void sendHeaderFooter(
            Player player,
            String header,
            String footer
    ) {
        player.sendPlayerListHeaderAndFooter(
                Component.text(header),
                Component.text(footer)
        );
    }

    // Em SidebarImpl
    public ScoreLine createScore(SidebarObjective objective, SidebarLine line, int score, String color) {
        return ((SidebarImpl.SidebarObjectiveImpl) objective).createScore(line, score, color);
    }

    public static SidebarProvider getInstance() {
        return null == instance ? instance = new ProviderImpl() : instance;
    }
}
