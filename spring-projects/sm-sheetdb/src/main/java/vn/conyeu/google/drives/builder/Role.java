package vn.conyeu.google.drives.builder;

public enum Role {

    /** The user owns the file or folder. */
    OWNER("owner"),

    /** Users who can organize files and folders within a shared drive */
    ORGANIZER("organizer"),

    /** Users who can edit, trash, and move content within a shared drive. */
    FILE_ORGANIGER("fileOrganizer"),

    /** Users who can access the file or folder are able to edit it. */
    WRITER("writer"),

    /**
     * Users who can access the file or folder are able only to view it, copy
     * it, or comment on it
     */
    COMMENTER("commenter"),

    /**
     * Users who can access the file or folder are able only to view it or copy
     * it.
     */
    READER("reader"),

    /** The user does not have any permissions for the file or folder. */
    NONE(null);

    final String value;

    Role(String value) {
        this.value = value;
    }

}