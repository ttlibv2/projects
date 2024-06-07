package vn.conyeu.ts.repository;

import org.springframework.data.jpa.repository.Query;
import vn.conyeu.ts.domain.TicketDetail;
import vn.conyeu.common.repository.LongUIdRepo;

import java.util.Optional;

public interface TicketDetailRepo extends LongUIdRepo<TicketDetail> {

    @Query("select e.ticketNumber from #{#entityName} e where e.id=?1")
    Optional<Long> findTicketNumberById(Long ticketId);
}