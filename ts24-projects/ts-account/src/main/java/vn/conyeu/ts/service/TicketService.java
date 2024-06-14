package vn.conyeu.ts.service;

import jakarta.persistence.criteria.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import vn.conyeu.common.exception.BadRequest;
import vn.conyeu.commons.beans.ObjectMap;
import vn.conyeu.ts.domain.Ticket;
import vn.conyeu.ts.domain.TicketDetail;
import vn.conyeu.ts.dtocls.TicketFindOption;
import vn.conyeu.ts.repository.TicketDetailRepo;
import vn.conyeu.ts.repository.TicketRepo;
import vn.conyeu.common.service.LongUIdService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TicketService extends LongUIdService<Ticket, TicketRepo> {
    private final TicketDetailRepo detailRepo;

    public TicketService(TicketRepo ticketRepo, TicketDetailRepo detailRepo){
        super(ticketRepo);
        this.detailRepo = detailRepo;
    }

    public Optional<Long> getTicketNumberById(Long ticketId) {
        if(!existsById(ticketId)) throw noId(ticketId);
        else return detailRepo.findTicketNumberById(ticketId);
    }

    @Override
    public Optional<Ticket> findById(Long ticketId) {
        Optional<Ticket> optional = super.findById(ticketId);
        if(optional.isPresent() && optional.get().getTemplate() != null) {
            throw new BadRequest("is_ticket_template")
                    .message("The ticket `%s` is template", ticketId);
        }
        return optional;
    }

    @Override
    public Page<Ticket> findAll(ObjectMap search, Pageable pageable) {
        TicketFindOption option = search.asObject(TicketFindOption.class);
        return findAll(option, pageable);
    }

    public Page<Ticket> findAll(TicketFindOption option, Pageable pageable) {
        Specification<Ticket> spec = (rt, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if(option.getCreatedMin() != null) {
                Path<LocalDateTime> ct = rt.get("createdAt");
                predicates.add(cb.lessThanOrEqualTo(ct, option.getCreatedMin()));
            }

            if(option.getCreatedMax() != null) {
                Path<LocalDateTime> ct = rt.get("createdAt");
                predicates.add(cb.greaterThanOrEqualTo(ct, option.getCreatedMax()));
            }

            if(option.getUpdatedMin() != null) {
                Path<LocalDateTime> ct = rt.get("updatedAt");
                predicates.add(cb.lessThanOrEqualTo(ct, option.getUpdatedMin()));
            }

            if(option.getUpdatedMax() != null) {
                Path<LocalDateTime> ct = rt.get("updatedAt");
                predicates.add(cb.greaterThanOrEqualTo(ct, option.getUpdatedMax()));
            }

            if(option.getUserId() != null) {
                Path<Long> ct = rt.get("user").get("id");
                predicates.add(cb.equal(ct, option.getUserId()));
            }

            if(option.getIsTemplate() != null) {
                Path<Long> ct = rt.get("template");
                predicates.add(ct.isNotNull());
            }

            //----------
            Path<TicketDetail> detail = rt.get("detail");

            if(option.getIsReport() != null) {
                Path<LocalDateTime> ct = detail.get("report");
                predicates.add(option.getIsReport() ? ct.isNotNull(): ct.isNull());
            }

            if(option.getIsSend() != null) {
                Path<LocalDateTime> ct = detail.get("sendAt");
                predicates.add(option.getIsSend() ? ct.isNotNull() : ct.isNull());
            }

            if(option.getIsNote() != null) {
                Path<LocalDateTime> ct = detail.get("noteAt");
                predicates.add(option.getIsNote() ? ct.isNotNull() : ct.isNull());
            }

            if(option.getIsAttach() != null) {
                Path<LocalDateTime> ct = detail.get("attachAt");
                predicates.add(option.getIsAttach() ? ct.isNotNull() : ct.isNull());
            }

            if(option.getIsClose() != null) {
                Path<LocalDateTime> ct = detail.get("closedAt");
                predicates.add(option.getIsClose() ? ct.isNotNull() : ct.isNull());
            }

            return cb.and(predicates.toArray(Predicate[]::new));
        };
        return findAll(spec, pageable);
    }


}