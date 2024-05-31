package vn.conyeu.ts.ticket.domain;

import lombok.EqualsAndHashCode;
import vn.conyeu.ts.odcore.domain.ClsModel;

@EqualsAndHashCode(callSuper = false)
public class ClsFile extends ClsModel<ClsFile> {
 private    Long id;
    private   String name;
    private   String filename;
    private  String mimetype;
    private  Long size;

    /**
     * Returns the id
     */
    public Long getId() {
        return id;
    }

    /**
     * Set the id
     *
     * @param id the value
     */
    public ClsFile setId(Long id) {
        this.id = id;
        return this;
    }

    /**
     * Returns the name
     */
    public String getName() {
        return name;
    }

    /**
     * Set the name
     *
     * @param name the value
     */
    public ClsFile setName(String name) {
        this.name = name;
        return this;
    }

    /**
     * Returns the filename
     */
    public String getFilename() {
        return filename;
    }

    /**
     * Set the filename
     *
     * @param filename the value
     */
    public ClsFile setFilename(String filename) {
        this.filename = filename;
        return this;
    }

    /**
     * Returns the mimetype
     */
    public String getMimetype() {
        return mimetype;
    }

    /**
     * Set the mimetype
     *
     * @param mimetype the value
     */
    public ClsFile setMimetype(String mimetype) {
        this.mimetype = mimetype;
        return this;
    }

    /**
     * Returns the size
     */
    public Long getSize() {
        return size;
    }

    /**
     * Set the size
     *
     * @param size the value
     */
    public ClsFile setSize(Long size) {
        this.size = size;
        return this;
    }
}