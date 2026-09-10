package me.xaxis.reportplus.reports;

import org.bukkit.Material;

import java.util.List;

public record ReportType(
        String id,
        Material material,
        String displayName,
        List<String> lore,
        int slot)
{

}