package vn.conyeu.google.drives.builder;

import vn.conyeu.google.drives.builder.QueryClass.*;

public class DriveQuery {
    private final QueryClass cls = new QueryClass();
    public final Name name = cls.name;
    public final MimeType mimeType = cls.mimeType;
    public final FullText fullText = cls.fullText;
    public final ModifiedTime modifiedTime = cls.modifiedTime;
    public final ViewedByMeTime viewedByMeTime = cls.viewedByMeTime;
    public final Trashed trashed = cls.trashed;
    public final Starred starred = cls.starred;
    public final Parents parents = cls.parents;
    public final Owners owners = cls.owners;
    public final Writters writters = cls.writters;
    public final Readers readers = cls.readers;
    public final SharedWithMe sharedWithMe = cls.sharedWithMe;
    public final CreatedTime createdTime = cls.createdTime;
    public final Properties properties = cls.properties;
    public final AppProperties appProperties = cls.appProperties;
    public final Visibility visibility = cls.visibility;
    public final Hidden hidden = cls.hidden;
    public final MemberCount memberCount = cls.memberCount;
    public final OrganizerCount organizerCount = cls.organizerCount;
}