package me.xaxis.reportplus.enums;

public enum PH {
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

    private final String placeholder;

    PH(String placeholder) {
        this.placeholder = placeholder;
    }

    public String getPlaceholder() {
        return placeholder;
    }

    @Override
    public String toString() {
        return placeholder;
    }
}
