package vn.conyeu.ts.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import vn.conyeu.common.domain.LongUIdDate;

//@formatter:off
@Entity @Table
@Getter @Setter @NoArgsConstructor
@DynamicInsert @DynamicUpdate
@AttributeOverride(name = "id", column = @Column(name = "attachId"))
//@formatter:on
public class AttachFile extends LongUIdDate<AttachFile> {

    @JsonProperty("file_name")
    @Column(length = 150, nullable = false)
    private String fileName;

    @JsonProperty("local_path")
    @Column(length = 500)
    private String localPath;

    @JsonProperty("google_path")
    @Column(length = 150)
    private String googlePath;

    @JsonProperty("md5")
    @Column(length = 50, nullable = false)
    private String md5Hash;

    @JsonProperty("ts_fid")
    private Long tsId;

    @Column(nullable = false, length = 50)
    private String model;

    private Long modelId;

    private String mimetype;

    private Long size;

    @Transient
    private String base64;
}