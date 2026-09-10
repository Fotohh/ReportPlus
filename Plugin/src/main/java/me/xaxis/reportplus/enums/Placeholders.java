package me.xaxis.reportplus.enums;

public enum Placeholders {
    REPORT_ID("%report_id%"),
    REPORTER("%player_reporter%"),
    REPORTED("%reported_player%"),
    REASON("%reason%"),
    REPORT_STATE("%report_state%"),
    REPORT_TYPE("%report_type%"),
    TIMESTAMP("%timestamp%"),
    CURRENT_PAGE("%current_page%"),
    TOTAL_PAGES("%total_pages%"),
    PLAYER("%player%"),
    ;

    private final String value;

    Placeholders(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static String replace(String string, Placeholders placeholder, String value) {
        String message;
        message = string.replace(placeholder.value, value);
        return message;
    }

    @Override
    public String toString() {
        return value;
    }
}
