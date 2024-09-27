package vn.conyeu.ts.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import vn.conyeu.common.domain.LongUIdDate;

//@formatter:off
@Entity @Table
@Getter @Setter @NoArgsConstructor
@DynamicInsert @DynamicUpdate
@AttributeOverride(name = "id", column = @Column(name = "reportId"))
//@formatter:on
public class TicketReport extends LongUIdDate<TicketReport> {
    private String chanelStatus;
    private String contentSupport;
    private String fullName;
    private String sortName;
    private String ticketNumber;
    private String ticketId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId", nullable = false)
    private TsUser user;


}