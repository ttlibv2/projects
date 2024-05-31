package vn.conyeu.ts.service;

import org.springframework.stereotype.Service;
import vn.conyeu.ts.domain.Ticket;
import vn.conyeu.ts.repository.TicketDetailRepo;
import vn.conyeu.ts.repository.TicketRepo;
import vn.conyeu.common.service.LongIdService;

@Service
public class TicketService extends LongIdService<Ticket, TicketRepo> {
    private final TicketDetailRepo detailRepo;

    public TicketService(TicketRepo ticketRepo, TicketDetailRepo detailRepo) {
        super(ticketRepo);
        this.detailRepo = detailRepo;
    }

}