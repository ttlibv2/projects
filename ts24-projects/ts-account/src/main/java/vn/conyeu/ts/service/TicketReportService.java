package vn.conyeu.ts.service;

import org.springframework.stereotype.Service;
import vn.conyeu.ts.domain.TicketReport;
import vn.conyeu.ts.repository.TicketReportRepo;
import vn.conyeu.common.service.LongIdService;

@Service
public class TicketReportService extends LongIdService<TicketReport, TicketReportRepo> {

    public TicketReportService(TicketReportRepo domainRepo) {
        super(domainRepo);
    }
}