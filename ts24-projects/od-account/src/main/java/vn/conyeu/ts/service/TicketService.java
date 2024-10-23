package vn.conyeu.ts.service;

import jakarta.persistence.criteria.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import vn.conyeu.common.exception.BadRequest;
import vn.conyeu.commons.beans.ObjectMap;
import vn.conyeu.commons.utils.Objects;
import vn.conyeu.identity.helper.IdentityHelper;
import vn.conyeu.ts.domain.Ticket;
import vn.conyeu.ts.domain.TicketDetail;
import vn.conyeu.ts.domain.TsUser;
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
    public Page<Ticket> findAll(Pageable pageable) {
        Long userId = IdentityHelper.extractUserId();
        return findAll(userId, pageable);
    }

    public Page<Ticket> findAll(Long userId, Pageable pageable) {
        return repo().findAll(userId, pageable);
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
                predicates.add(cb.greaterThanOrEqualTo(ct, option.getCreatedMin()));
            }

            if(option.getCreatedMax() != null) {
                Path<LocalDateTime> ct = rt.get("createdAt");
                predicates.add(cb.lessThanOrEqualTo(ct, option.getCreatedMax()));
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

            //----------
            Path<TicketDetail> detail = rt.get("detail");

            if(option.getIsReport() != null) {
                Path<LocalDateTime> ct = detail.get("reportAt");
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
                Path<LocalDateTime> ct = detail.get("imageAt");
                predicates.add(option.getIsAttach() ? ct.isNotNull() : ct.isNull());
            }

            if(option.getIsClose() != null) {
                Path<LocalDateTime> ct = detail.get("closedAt");
                predicates.add(option.getIsClose() ? ct.isNotNull() : ct.isNull());
            }

            // build predicates
            return cb.and(predicates.toArray(Predicate[]::new));
        };
        return findAll(spec, pageable);
    }


    public Ticket saveTicket(Long userId, ObjectMap object) {
        Long ticketId = object.getLong("ticket_id");

        // create new
        if(ticketId == null) {
            Ticket ticket = object.asObject(Ticket.class);
            ticket.setUser(new TsUser(userId));
            //ticket.setOdImage(imagesToMap(object.getString("images")));
            return createNew(ticket);
        }

        // update
        else {
            Optional<Ticket> optional = findById(ticketId);
            if(optional.isEmpty()) throw noId(ticketId);

            Ticket ticket = optional.get();
            TicketDetail detail = ticket.getDetail();

            if(!Objects.equals(ticket.getUserId(), userId)) {
                throw new BadRequest("user_invalid").message("Ticket này khong dung userId [%s]", userId);
            }

            ticket.assignFromMap(object);

           // if(object.containsKey("images")) {
          //      String images = object.getString("images");
          //      ticket.setOdImage(imagesToMap(images));
          //  }

            return saveAndReturn(ticket);
        }
    }

    private ObjectMap imagesToMap(String images) {
        if(Objects.isBlank(images)) return null;

        ObjectMap map = new ObjectMap();
        for(String segment:images.split(";")) {
            String[] paths = segment.split("::");
            map.set(paths[0], paths.length > 1 ? paths[1] : "null", false);
        }

        return map;
    }
}