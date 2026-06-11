package com.andrei1058.spigot.sidebar.v1_20_R1;

import com.andrei1058.spigot.sidebar.*;


import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.*;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import net.kyori.adventure.text.Component;

import java.util.Collection;

public class SidebarImpl extends WrappedSidebar {

    public SidebarImpl(@NotNull SidebarLine title, @NotNull Collection<SidebarLine> lines, Collection<PlaceholderProvider> placeholderProvider) {
        super(title, lines, placeholderProvider);
    }

    public ScoreLine createScore(SidebarLine line, int score, String color) {
        return new SidebarImpl.NarniaScoreLine(line, score, color);
    }

    public SidebarObjective createObjective(String name, Criteria criteria, SidebarLine title, int type) {
        return new NarniaSidebarObjective(name, criteria, title, type);
    }

    protected class NarniaSidebarObjective implements SidebarObjective {

        private final String name;
        private final Criteria criteria;
        private final int type;

        private SidebarLine displayName;
        private Component displayNameComp = Component.text("");

        public NarniaSidebarObjective(String name, Criteria criteria, SidebarLine displayName, int type) {
            this.name = name;
            this.criteria = criteria;
            this.displayName = displayName;
            this.type = type;
        }

        @Override
        public void setTitle(SidebarLine title) {
            this.displayName = title;
        }

        @Override
        public SidebarLine getTitle() {
            return displayName;
        }


        @Override
        public String getName() {
            return this.name;
        }

        @Override
        public boolean refreshTitle() {
            var newTitle = displayName.getTrimReplacePlaceholders(
                    getReceivers().isEmpty() ? null : getReceivers().getFirst(),
                    256,
                    getPlaceholders()
            );

            if (newTitle.equals(displayNameComp.toString())) {
                return false;
            }
            this.displayNameComp = Component.text(newTitle);
            return true;
        }


        @Override
        public void sendCreate(Player player) {
            Scoreboard scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();

            Objective objective = scoreboard.registerNewObjective(getName(), Criteria.DUMMY, displayNameComp.toString());

            objective.setDisplaySlot(DisplaySlot.SIDEBAR);

            player.setScoreboard(scoreboard);

            if (getName().equalsIgnoreCase("health")) {
                Objective health = scoreboard.registerNewObjective("health", Criteria.HEALTH, displayNameComp.toString());

                health.setDisplaySlot(DisplaySlot.BELOW_NAME);
            }
        }

        // must be called when updating the name
        public void sendUpdate() {
            String title = displayNameComp.toString();

            getReceivers().forEach(player -> {
                Scoreboard scoreboard = player.getScoreboard();

                Objective objective = scoreboard.getObjective(getName());

                if (objective != null) {
                    objective.displayName(Component.text(title));
                }
            });
        }


        @Override
        public void sendRemove(Player player) {
            Scoreboard scoreboard = player.getScoreboard();

            Objective objective = scoreboard.getObjective(getName());

            if (objective != null) objective.unregister();
        }
    }

    public class NarniaScoreLine implements ScoreLine, Comparable<ScoreLine> {

        private int score;
        private Component prefix = Component.text(""), suffix = Component.text("");
        private final TeamLine team;
        private SidebarLine text;

        public NarniaScoreLine(@NotNull SidebarLine text, int score, @NotNull String color) {
            this.score = score;
            this.text = text;
            this.team = new TeamLine(color);
        }

        @Override
        public SidebarLine getLine() {
            return text;
        }

        @Override
        public void setLine(SidebarLine line) {
            this.text = line;
        }

        @Override
        public int getScoreAmount() {
            return score;
        }

        @Override
        public void setScoreAmount(int score) {
            this.score = score;
        }

        @Override
        public void sendCreateToAllReceivers() {
            for (Player player : getReceivers()) {

                Scoreboard board = player.getScoreboard();

                Team team = board.getTeam(getColor());

                if (team == null) {
                    team = board.registerNewTeam(getColor());
                }

                team.prefix(Component.text(prefix.toString()));
                team.suffix(Component.text(suffix.toString()));

                String entry = getColor();

                if (!team.hasEntry(entry)) {
                    team.addEntry(entry);
                }

                Objective objective = board.getObjective(
                        getSidebarObjective().getName()
                );

                if (objective != null) {
                    objective.getScore(entry)
                            .setScore(getScoreAmount());
                }
            }
        }

        @Override
        public void sendCreate(Player player) {
            Scoreboard board = player.getScoreboard();

            Team team = board.getTeam(getColor());

            if (team == null) {
                team = board.registerNewTeam(getColor());
            }

            team.prefix(net.kyori.adventure.text.Component.text(prefix.toString()));
            team.suffix(net.kyori.adventure.text.Component.text(suffix.toString()));

            String entry = getColor();

            if (!team.hasEntry(entry)) {
                team.addEntry(entry);
            }

            Objective objective = board.getObjective(
                    getSidebarObjective().getName()
            );

            if (objective != null) {
                objective.getScore(entry)
                        .setScore(getScoreAmount());
            }
        }

        @Override
        public void sendRemove(Player player) {
            Scoreboard board = player.getScoreboard();

            String entry = getColor();

            Objective objective = board.getObjective(
                    getSidebarObjective().getName()
            );

            if (objective != null) {
                board.resetScores(entry);
            }

            Team team = board.getTeam(entry);

            if (team != null) {
                team.unregister();
            }
        }

        public void sendRemoveToAllReceivers() {
            getReceivers().forEach(this::sendRemove);
        }

        @Override
        public void sendUpdate(Player player) {
            Scoreboard board = player.getScoreboard();

            Team team = board.getTeam(getColor());

            if (team == null) {
                return;
            }

            team.prefix(net.kyori.adventure.text.Component.text(prefix.toString()));
            team.suffix(net.kyori.adventure.text.Component.text(suffix.toString()));
        }

        @Contract(pure = true)
        public boolean setContent(@NotNull SidebarLine line) {
            var oldPrefix = this.prefix;
            var oldSuffix = this.suffix;

            String content = line.getTrimReplacePlaceholders(
                    getReceivers().isEmpty() ? null : getReceivers().getFirst(),
                    null,
                    getPlaceholders()
            );

            if (content.length() > 256) {
                this.prefix = Component.text(content.substring(0, 256));

                if (content.charAt(255) == ChatColor.COLOR_CHAR) {
                    this.prefix = Component.text(content.substring(0, 255));
                    setSuffix(content.substring(255));
                } else {
                    setSuffix(content.substring(256));
                }
            } else {
                this.prefix = Component.text(content);
                this.suffix = Component.empty();
            }

            return !oldPrefix.equals(this.prefix)
                    || !oldSuffix.equals(this.suffix);
        }

        public void setSuffix(@NotNull String secondPart) {
            if (secondPart.isEmpty()) {
                this.suffix = Component.text("");
                return;
            }
            secondPart = org.bukkit.ChatColor.getLastColors(this.prefix.toString()) + secondPart;
            this.suffix = Component.text(secondPart.length() > 256 ? secondPart.substring(0, 256) : secondPart);
        }

        public void sendUpdateToAllReceivers() {
            getReceivers().forEach(this::sendUpdate);
        }

        public int compareTo(@NotNull ScoreLine o) {
            return Integer.compare(score, o.getScoreAmount());
        }

        public String getColor() {
            String entry = team.getEntry();

            return entry.charAt(0) == ChatColor.COLOR_CHAR
                   ? entry
                   : ChatColor.COLOR_CHAR + entry;
        }

        @Override
        public boolean refreshContent() {
            return setContent(getLine());
        }

        private class TeamLine {

            private final String color;

            public TeamLine(String color) {
                this.color = color;
            }

            public String getEntry() {
                return color;
            }
        }
    }
}
