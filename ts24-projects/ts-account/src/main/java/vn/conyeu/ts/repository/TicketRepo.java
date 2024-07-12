package vn.conyeu.ts.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import vn.conyeu.ts.domain.Ticket;
import vn.conyeu.common.repository.LongUIdRepo;

import java.util.Optional;

public interface TicketRepo extends LongUIdRepo<Ticket> {

    @Query("select e.detail.ticketNumber from #{#entityName} e where e.id=?1")
    Optional<Long> findTicketNumberById(Long ticketId);

    @Query(value = "select e from #{#entityName} e where e.user.id=?1",
    countQuery = "select count(e) from #{#entityName} e where e.user.id=?1")
    Page<Ticket> findAll(Long userId, Pageable pageable);
}