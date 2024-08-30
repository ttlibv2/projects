package vn.conyeu.google.drives;

public enum GMime {
    AUDIO("application/vnd.google-apps.audio"),

    /**
     * Google Docs
     */
    G_DOC("application/vnd.google-apps.document"),

    /**
     * Third-party shortcut
     */
    DRIVE_SDK("application/vnd.google-apps.drive-sdk"),

    /**
     * Google Drawings
     */
    DRAWING("application/vnd.google-apps.drawing"),

    /**
     * Google Drive file
     */
    FILE("application/vnd.google-apps.file"),

    /**
     * Google Drive folder
     */
    FOLDER("application/vnd.google-apps.folder"),

    /**
     * Google Forms
     */
    FORM("application/vnd.google-apps.form"),

    /**
     * Google Fusion Tables
     */
    FUSION_TABLE("application/vnd.google-apps.fusiontable"),

    /**
     * Google Jamboard
     */
    JAMBOARD("application/vnd.google-apps.jam"),

    /**
     * Email layout
     */
    EMAIL_LAYOUT("application/vnd.google-apps.mail-layout"),

    /**
     * Google My Maps
     */
    G_MAP("application/vnd.google-apps.map"),

    /**
     * Google Photos
     */
    G_PHOTO("application/vnd.google-apps.photo"),

    /**
     * Google Slides
     */
    G_SLIDE("application/vnd.google-apps.presentation"),

    /**
     * Google Apps Script
     */
    G_SCRIPT("application/vnd.google-apps.script"),

    /**
     * Shortcut
     */
    G_SHORTCUT("application/vnd.google-apps.shortcut"),

    /**
     * Google Sites
     */
    G_SITE("application/vnd.google-apps.site"),

    /**
     * Google Sheets
     */
    G_SHEET("application/vnd.google-apps.spreadsheet"),

    G_NONE("application/vnd.google-apps.unknown"),

    /**
     * Google Vids
     */
    G_VIDS("application/vnd.google-apps.vid"),

    G_VIDEO("application/vnd.google-apps.video");

    final String mime;

    GMime(String mime) {
        this.mime = mime;
    }

    public String getMime() {
        return mime;
    }
}