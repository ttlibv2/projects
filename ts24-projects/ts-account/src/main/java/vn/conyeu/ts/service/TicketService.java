package vn.conyeu.ts.service;

import org.springframework.stereotype.Service;
import vn.conyeu.ts.domain.Ticket;
import vn.conyeu.ts.repository.TicketDetailRepo;
import vn.conyeu.ts.repository.TicketRepo;
import vn.conyeu.common.service.LongUIdService;

import java.util.Optional;

@Service
public class TicketService extends LongUIdService<Ticket, TicketRepo> {
    private final TicketDetailRepo detailRepo;

    public TicketService(TicketRepo ticketRepo, TicketDetailRepo detailRepo) {
        super(ticketRepo);
        this.detailRepo = detailRepo;
    }

    public Optional<Long> getTicketNumberById(Long ticketId) {
        if(!existsById(ticketId)) throw noId(ticketId);
        else return detailRepo.findTicketNumberById(ticketId);
    }
}