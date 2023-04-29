package me.xaxis.reportplus.enums;

public enum Perms {

    PLAYER_REPORT("ReportPlus.player_report"),
    REPORT_ALERT("ReportPlus.report_alert"),
    LIST_REPORTS("ReportPlus.list_reports"),
    ;
    private final String permission;
    Perms(String permission){
        this.permission = permission;
    }
    public String getPermission() {
        return permission;
    }
}
