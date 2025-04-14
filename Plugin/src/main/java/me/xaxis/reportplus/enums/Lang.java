package me.xaxis.reportplus.enums;

import java.util.List;

public enum Lang {
    NO_PERMISSION("Lang.CommandMessages.no_permission", "&4You do not have permission to execute this command!"),
    SENDER_NOT_PLAYER("Lang.CommandMessages.sender_not_player", "&4Only players can execute this command!"),
    PREFIX("Lang.FormattingMessages.prefix", "&2[&6Report+&2]&7&r "),
    INVALID_PLAYER("Lang.PlayerMessages.invalid_player", "&4That player is either offline or does not exist!"),
    STRING_NOT_INT("Lang.MiscellaneousMessages.string_not_int", "&4You must enter a integer not a string!"),
    NOT_ENOUGH_ARGS("Lang.CommandMessages.not_enough_args", "&4You have not provided the correct amount of arguments!"),
    SUCCESSFUL_REPORT("Lang.PlayerMessages.successful_report", "&aSuccessfully reported %s with hacking type being %s!"),
    REPORT_ALERT("Lang.PlayerMessages.report_alert", "&7-------&4Report Alert&7-------\n&f - Player: %s\n&f - Reporter: %s\n&f - Type: %s\n&f - Timestamp: %s"),
    CANNOT_REPORT_SELF("Lang.PlayerMessages.cannot_report_self", "&4You cannot report yourself!"),
    INVALID_REPORT_USAGE("Lang.CommandMessages.invalid_report_usage", "&4Invalid Usage! Usage: /report <target>"),
    REMOVED_REPORT("Lang.PlayerMessages.removed_report", "&aYou have removed the report from the list!"),
    SET_REPORT_AS_RESOLVED("Lang.PlayerMessages.set_report_as_resolved", "&aYou have set the report as resolved!"),
    GUI_OPTIONS_TITLE("GUI.gui_options.title", "&2Report Options"),
    GUI_OPTIONS_CLOSE("GUI.gui_options.close", "&cClose Inventory"),
    GUI_OPTIONS_DELETE("GUI.gui_options.delete", "&cDelete Report"),
    GUI_OPTIONS_RESOLVE("GUI.gui_options.resolve", "&aSet Report as Resolved"),
    GUI_ITEM_GO_BACK("GUI.gui_item.go_back", "&7Go Back"),
    TOGGLED_REPORT_OFF("Lang.PlayerMessages.toggled_report_off", "&aYou have toggled report alerts off!"),
    TOGGLED_REPORT_ON("Lang.PlayerMessages.toggled_report_on", "&aYou have toggled report alerts on!"),
    REPORT_NOT_FOUND("Lang.PlayerMessages.report_not_found", "&4That report does not exist!"),
    DELETED_REPORT("Lang.PlayerMessages.deleted_report", "&aYou have deleted the report!"),
    REPORT_ALREADY_RESOLVED("Lang.PlayerMessages.report_already_resolved", "&4That report has already been resolved!"),
    REPORT_LIST_TITLE("GUI.report_list.title", "&2Report List"),
    REPORT_LIST_ITEM_PLAYER_LORE("GUI.report_list.item.player.lore", List.of("&7Report Type: &6%s", "&7Reporter: &6%s", "&7Player: &6%s", "&7Timestamp: &6%s", "&7State: &6%s")),
    REPORT_LIST_ITEM_NO_REPORTS("GUI.report_list.item.no_reports", "&4There are no reports!"),
    REPORT_LIST_ITEM_FILTER_OUT_RESOLVED("GUI.report_list.item.filter_out_resolved", "&7Filter out resolved reports"),
    REPORT_LIST_ITEM_FILTER_OUT_OPEN("GUI.report_list.item.filter_out_open", "&7Filter out open reports"),
    REPORT_LIST_ITEM_SHOW_ALL("GUI.report_list.item.show_all", "&7Show all reports"),
    REPORT_LIST_ITEM_NEXT_PAGE("GUI.report_list.item.next_page", "&7Next Page"),
    REPORT_LIST_ITEM_PREVIOUS_PAGE("GUI.report_list.item.previous_page", "&7Previous Page"),
    REPORT_LIST_ITEM_CURRENT_PAGE("GUI.report_list.item.current_page", "&7Current Page: &6%s"),
    ;

    public Object getDefaultValue(){
        return value;
    }

    public String getPath(){
        return path;
    }

    private final String path;
    private final Object value;

    Lang(String path, Object defaultValue){
        this.path = path;
        this.value = defaultValue;
    }
}
