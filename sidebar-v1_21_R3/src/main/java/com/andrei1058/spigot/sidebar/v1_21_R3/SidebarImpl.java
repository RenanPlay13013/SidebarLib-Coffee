package com.andrei1058.spigot.sidebar.v1_21_R3;

import com.andrei1058.spigot.sidebar.*;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Criteria;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

@SuppressWarnings("unused")
public class SidebarImpl extends WrappedSidebar {

    public SidebarImpl(@NotNull SidebarLine title, @NotNull Collection<SidebarLine> lines, Collection<PlaceholderProvider> placeholderProvider) {
        super(title, lines, placeholderProvider);
    }

    public ScoreLine createScore(SidebarLine line, int score, String color) {
        // ScoreLineImpl is an inner class of SidebarObjectiveImpl, so it cannot
        // be instantiated here directly. Callers should use
        // objective.createScore(...) instead. This method is intentionally left
        // unimplemented and must be wired through a SidebarObjectiveImpl instance.
        throw new UnsupportedOperationException(
                "Use SidebarObjectiveImpl#createScore(line, score, color) instead."
        );
    }

    public SidebarObjective createObjective(
            String name,
            Criteria criteria,
            SidebarLine title,
            int type
    ) {
        return new SidebarObjectiveImpl(name, criteria, title, type);
    }

    protected class SidebarObjectiveImpl implements SidebarObjective {

        private final String name;
        private final Criteria criteria;
        private final int slot;

        private SidebarLine displayName;
        private String cachedTitle = "";

        public SidebarObjectiveImpl(
                String name,
                Criteria criteria,
                SidebarLine displayName,
                int slot
        ) {
            this.name = name;
            this.criteria = criteria;
            this.displayName = displayName;
            this.slot = slot;
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
            return name;
        }

        public Criteria getCriteria() {
            return criteria;
        }

        public int getSlot() {
            return slot;
        }

        public String getCachedTitle() {
            return cachedTitle;
        }

        /**
         * Factory method — creates a ScoreLineImpl tied to this objective.
         */
        public ScoreLine createScore(SidebarLine line, int score, String color) {
            return new ScoreLineImpl(line, score, color);
        }

        @Override
        public boolean refreshTitle() {

            String newTitle = displayName.getTrimReplacePlaceholders(
                    getReceivers().isEmpty() ? null : getReceivers().getFirst(),
                    256,
                    getPlaceholders()
            );

            if (newTitle.equals(cachedTitle)) {
                return false;
            }

            cachedTitle = newTitle;
            return true;
        }

        @Override
        public void sendCreate(Player player) {

            // Ensure the title is populated before creating the objective.
            if (cachedTitle.isEmpty()) {
                refreshTitle();
            }

            Scoreboard board = player.getScoreboard();

            Objective objective = board.getObjective(name);

            if (objective == null) {
                objective = board.registerNewObjective(
                        name,
                        criteria,
                        cachedTitle
                );
            }

            objective.setDisplaySlot(
                    slot == 1
                    ? org.bukkit.scoreboard.DisplaySlot.PLAYER_LIST
                    : org.bukkit.scoreboard.DisplaySlot.SIDEBAR
            );
        }

        @Override
        public void sendUpdate() {

            for (Player player : getReceivers()) {

                Objective objective = player.getScoreboard()
                        .getObjective(name);

                if (objective != null) {
                    objective.displayName(Component.text(cachedTitle));
                }
            }
        }

        @Override
        public void sendRemove(Player player) {

            Objective objective = player.getScoreboard()
                    .getObjective(name);

            if (objective != null) {
                objective.unregister();
            }
        }

        // ------------------------------------------------------------------ //
        //  ScoreLineImpl — inner class of SidebarObjectiveImpl                //
        // ------------------------------------------------------------------ //

        public class ScoreLineImpl implements ScoreLine, Comparable<ScoreLine> {

            private int score;
            private SidebarLine text;
            private final String entry;

            /** Cached result of the last placeholder-resolved render. */
            private String cachedContent = "";

            public ScoreLineImpl(
                    @NotNull SidebarLine text,
                    int score,
                    @NotNull String color
            ) {
                this.text = text;
                this.score = score;
                this.entry = color;
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

                for (Player player : getReceivers()) {
                    updateLine(player);
                }
            }

            @Override
            public void sendCreateToAllReceivers() {
                getReceivers().forEach(this::createLine);
            }

            @Override
            public void sendCreate(Player player) {
                createLine(player);
            }

            /**
             * Replaces the line content and returns {@code true} only when the
             * rendered text actually changed.
             */
            @Override
            public boolean setContent(SidebarLine line) {

                String old = cachedContent;

                this.text = line;

                cachedContent = line.getTrimReplacePlaceholders(
                        getReceivers().isEmpty()
                        ? null
                        : getReceivers().getFirst(),
                        null,
                        getPlaceholders()
                );

                return !cachedContent.equals(old);
            }

            @Override
            public void sendRemove(Player player) {
                Scoreboard board = player.getScoreboard();

                board.resetScores(entry);

                Team team = board.getTeam(entry);

                if (team != null) {
                    team.unregister();
                }
            }

            public void sendRemoveToAllReceivers() {
                getReceivers().forEach(this::sendRemove);
            }

            public void sendUpdate(Player player) {
                updateLine(player);
            }

            public void sendUpdateToAllReceivers() {
                getReceivers().forEach(this::updateLine);
            }

            /**
             * Re-resolves placeholders and caches the result.
             * Returns {@code true} only when the content actually changed.
             */
            @Override
            public boolean refreshContent() {

                String newContent = text.getTrimReplacePlaceholders(
                        getReceivers().isEmpty()
                        ? null
                        : getReceivers().getFirst(),
                        null,
                        getPlaceholders()
                );

                if (newContent.equals(cachedContent)) {
                    return false;
                }

                cachedContent = newContent;
                return true;
            }

            @Override
            public int compareTo(@NotNull ScoreLine o) {
                return Integer.compare(score, o.getScoreAmount());
            }

            public String getColor() {
                return entry;
            }

            private void createLine(Player player) {
                Scoreboard board = player.getScoreboard();

                Team team = board.getTeam(entry);

                if (team == null) {
                    team = board.registerNewTeam(entry);
                    team.addEntry(entry);
                }

                updateTeam(team);

                // Use the enclosing SidebarObjectiveImpl's name directly.
                Objective objective = board.getObjective(
                        SidebarObjectiveImpl.this.getName()
                );

                if (objective != null) {
                    objective.getScore(entry).setScore(score);
                }
            }

            private void updateLine(Player player) {
                Scoreboard board = player.getScoreboard();

                Team team = board.getTeam(entry);

                if (team == null) {
                    return;
                }

                updateTeam(team);
            }

            private void updateTeam(Team team) {

                String content = text.getTrimReplacePlaceholders(
                        getReceivers().isEmpty()
                        ? null
                        : getReceivers().getFirst(),
                        null,
                        getPlaceholders()
                );

                if (content.length() <= 64) {
                    team.setPrefix(content);
                    team.setSuffix("");
                    return;
                }

                String prefix = content.substring(0, 64);

                String suffix = content.substring(64);

                if (suffix.length() > 64) {
                    suffix = suffix.substring(0, 64);
                }

                team.setPrefix(prefix);
                team.setSuffix(suffix);
            }
        } // end ScoreLineImpl
    } // end SidebarObjectiveImpl
} // end SidebarImpl