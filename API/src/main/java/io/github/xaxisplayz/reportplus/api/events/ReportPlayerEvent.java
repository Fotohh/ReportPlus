package io.github.xaxisplayz.reportplus.api.events;

import lombok.Getter;
import me.xaxis.reportplus.reports.Report;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

/**
 * This event is called everytime a player submits a new report.
 * In return {@link #report} is returned, the {@link #reporter} is returned, and {@link #player} is returned.
 */
@Getter

public class ReportPlayerEvent extends Event {

    private static final HandlerList handlerList = new HandlerList();
    /**
     * The report that was just submitted.
     */
    private final Report report;
    /**
     * The player who submitted the report.
     */
    private final Player reporter;
    /**
     * The player that was reported
     */
    private final Player player;

    public ReportPlayerEvent(Report report) {
        if(report == null) throw new RuntimeException("Report was null! Do you have ReportPlus in your plugins folder?");
        this.report = report;
        this.reporter = Bukkit.getPlayer(report.getReporterUUID());
        this.player = Bukkit.getPlayer(report.getPlayerUUID());
    }

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return handlerList;
    }

    public static HandlerList getHandlerList() {
        return handlerList;
    }
}
