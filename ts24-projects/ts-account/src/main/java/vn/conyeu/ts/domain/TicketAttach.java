package vn.conyeu.ts.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import vn.conyeu.common.domain.LongUIdDate;

//@formatter:off
@Entity @Table
@Getter @Setter @NoArgsConstructor
@DynamicInsert @DynamicUpdate
@JsonIgnoreProperties({"ticket"})
@AttributeOverride(name = "id", column = @Column(name = "attachId"))
//@formatter:on
public class TicketAttach extends LongUIdDate<TicketAttach> {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ticketId", nullable = false)
    private Ticket ticket;

    @JsonProperty("file_name")
    @Column(length = 150, nullable = false)
    private String fileName;

    @JsonProperty("file_path")
    @Column(length = 350, nullable = false)
    private String filePath;

    @JsonProperty("md5")
    @Column(length = 50, nullable = false)
    private String md5Hash;

    @ColumnDefault("0")
    private Boolean attach;

    @JsonProperty("ts_fid")
    private Long externalId;

}