package me.xaxis.reportplus.enums;

public enum Lang {
    NO_PERMISSION("Lang.CommandMessages.no_permission", "&4You do not have permission to execute this command!"),
    SENDER_NOT_PLAYER("Lang.CommandMessages.sender_not_player", "&4Only players can execute this command!"),
    PREFIX("Lang.FormattingMessages.prefix", "&2[&6Report+&2]&7&r "),
    INVALID_PLAYER("Lang.PlayerMessages.invalid_player", "&4That player is either offline or does not exist!"),
    STRING_NOT_INT("Lang.MiscellaneousMessages.string_not_int", "&4You must enter a integer not a string!"),
    NOT_ENOUGH_ARGS("Lang.CommandMessages.not_enough_args", "&4You have not provided the correct amount of arguments!"),
    SUCCESSFUL_REPORT("Lang.PlayerMessages.successful_report", "&aSuccessfully reported %s with hacking type being %s!"),
    REPORT_ALERT("Lang.PlayerMessages.report_alert", "&7-------&4Report Alert&7-------\n&f - Player: %s\n&f - Reporter: %s\n&f - Type: %s\n&f - Timestamp: %s"),
    REPORT_LIST_GUI_TITLE("Lang.GUI.report_list_title", "&cList of Reports!")
            ;

    public String getDefaultValue(){
        return value;
    }

    public String getPath(){
        return path;
    }

    private final String path;
    private final String value;

    Lang(String path, String defaultValue){
        this.path = path;
        this.value = defaultValue;
    }
}
