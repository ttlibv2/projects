package vn.conyeu.ts.ticket_rest;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import vn.conyeu.commons.beans.ObjectMap;
import vn.conyeu.commons.utils.Objects;
import vn.conyeu.ts.domain.Ticket;
import vn.conyeu.ts.domain.TicketDetail;
import vn.conyeu.ts.dtocls.Errors;
import vn.conyeu.ts.dtocls.SendTicketDto;
import vn.conyeu.ts.dtocls.SendTicketDto.ExistState;
import vn.conyeu.ts.dtocls.TsVar;
import vn.conyeu.ts.odcore.domain.ClsPage;
import vn.conyeu.ts.service.OdService;
import vn.conyeu.ts.service.TicketService;
import vn.conyeu.ts.service.UserApiService;
import vn.conyeu.ts.ticket.domain.*;
import vn.conyeu.ts.ticket.service.OdTicket;
import vn.conyeu.ts.ticket.service.OdTicketService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Stream;

@Slf4j
@RestController
@PreAuthorize("isAuthenticated()")
@RequestMapping(TsVar.Rest.odTicket)
public class OdTicketRest extends OdBaseRest {
    private final TicketService ticketService;

    public OdTicketRest(OdService odService, UserApiService apiService, TicketService ticketService) {
        super(odService, apiService);
        this.ticketService = ticketService;
    }

    @GetMapping("search")
    public List<ClsTicket> findTicket(@RequestParam Map<String, Object> mapQuery, Pageable pg) {
        ClsPage clsPage = new ClsPage().limit(pg.getPageSize());
        return service().ticket().searchTicket(ObjectMap.clone(mapQuery), clsPage);
    }

    @GetMapping("get-by-sysid/{ticketId}")
    public ClsTicket findTicketById(@PathVariable Long ticketId) {
        Optional<Long> optional = ticketService.getTicketNumberById(ticketId);
        if (optional.isEmpty()) throw Errors.odTicketNotCreate(ticketId);
        else {
            Long ticketNumber = optional.get();
            return service().ticket().getByID(ticketNumber)
                    .orElseThrow(() -> Errors.noOdTicketId(ticketNumber));
        }
    }

    @GetMapping("get-by-uid/{ticketNumber}")
    public Ticket getTicketOnWeb(@PathVariable Long ticketNumber) {
        OdTicketService service = service();
        ClsTicket cls = service.ticket().getByID(ticketNumber).orElseThrow(() -> Errors.noOdTicketId(ticketNumber));
        Ticket ticket = OdTRHelper.fromClsTicket(cls).set("get-by-api", cls);

        Long partnerId = ticket.getOdPartnerId();
        if (partnerId != null) {
            Optional<ClsPartner> clsPartnerOptional = service.partner().findById(partnerId);
            clsPartnerOptional.ifPresent(p -> {
                ticket.setPhone(p.getPhone());
                ticket.setTaxCode(p.getVat());
                cls.set("cls_partner", p);

            });
        }
        return ticket;
    }

    @GetMapping("delete-follow/{ticketNumber}")
    public List<Long> deleteFollowExludeUser(@PathVariable Long ticketNumber) {
        return service().ticket().deleteFollow(ticketNumber);
    }

    @PostMapping("send")
    public Object sendTicket(@RequestBody @Valid SendTicketDto dto) {
        final Ticket ticket = ticketService.getById(dto.getTicketId());
        final String action = dto.getAction().toLowerCase();
        final List<String> segments = Stream.of(action.split(",")).map(String::trim).toList();
        final boolean isAll = "all".equals(action);

        TicketDetail detail = ticket.getDetail();

        final boolean
                isSend = ticket.isSendTicket(),
                isAddNote = detail.getNoteId() != null,
                isAttach = detail.getImageAt() != null,
                isSendMail = detail.isSendMail(),
                isTicketEmail = ticket.isEmailTicket(),
                isClose = detail.isClosed();

        if ((isAll && !isSend) || segments.contains("create_ticket")) {
            createTicket(service(), ticket, dto.getTicketState());
        }

        if ((isAll && !isAddNote) || segments.contains("add_note")) {
            createNote(service(), ticket, dto.getNoteState());
        }

        if ((isAll && !isAttach) || segments.contains("attach_image")) {
            attachFile(service(), ticket, dto.getImageBase64());
        }

        if ((isAll && isTicketEmail && !isSendMail) || segments.contains("send_email")) {
            sendEmail(service(), ticket);
        }

        if ((isAll && !isClose) || segments.contains("close_ticket")) {
            closeTicket(service(), ticket);
        }

        return ticket;
    }

    private Ticket createTicket(OdTicketService service, Ticket ticket, ExistState state) {
        if (!ticket.isSendTicket()) {
            OdTicket odTicket = service.ticket();

            // create ticket
            ClsTicket clsTicket = OdTRHelper.fromTicket(ticket);
            ClsTicket clsNew = odTicket.createNew(clsTicket);

            // delete follow partner id
            if (!ticket.isEmailTicket()) {
                Long excludeUserId = odTicket.cfg().getClsUser().getPartner_id();
                List<Long> followIds = clsNew.getFollowPartnerIds().stream().filter(id -> Objects.notEqual(id, excludeUserId)).toList();
                odTicket.deleteFollow(clsNew.getId(), followIds);
            }

            return saveTicket(ticket, clsNew, TicketAction.CREATE_TICKET);
        }//
        else {
            switch (state) {
                case CREATE -> {
                    TicketDetail oldDetail = ticket.setNewDetail();
                    createTicket(service, ticket, ExistState.NONE);
                }
                case DELETE -> {
                    TicketDetail oldDetail = ticket.setNewDetail();
                    deleteTicket(service, oldDetail.getTicketNumber(), oldDetail);
                    createTicket(service, ticket, ExistState.NONE);
                }
                case UPDATE -> {
                    updateTicket(service, ticket);
                }
            }
            return ticket;
        }
    }

    private Ticket updateTicket(OdTicketService service, Ticket ticket) {
        validateTicketNoSend(ticket, "Không thể cập nhật");

        // nếu ticket lấy từ web thì ko cập nhật
        if (ticket.isWeb()) {
            log.warn("Ticket `{}` này lấy từ web nên không cho cập nhật.",
                    ticket.getDetail().getTicketNumber());
            return ticket;
        }

        Long ticketNum = ticket.getDetail().getTicketNumber();
        ClsTicket clsTicket = OdTRHelper.fromTicket(ticket);
        ClsTicket clsNew = service.ticket().updateTicket(ticketNum, clsTicket);
        return saveTicket(ticket, clsNew, TicketAction.UPDATE_TICKET);
    }

    private TicketDetail deleteTicket(OdTicketService service, Long ticketNumber, TicketDetail oldDetail) {
        service.ticket().deleteTicket(ticketNumber);
        return oldDetail;
    }

    private Ticket closeTicket(OdTicketService service, Ticket ticket) {
        validateTicketNoSend(ticket, "Không thể đóng");
        Long ticketNum = ticket.getDetail().getTicketNumber();
        ClsTicket clsTicket = service.ticket().closeTicket(ticketNum);
        return saveTicket(ticket, clsTicket, TicketAction.CLOSE_TICKET);
    }

    private Ticket createNote(OdTicketService service, Ticket ticket, ExistState state) {
        validateTicketNoSend(ticket, "Không thể thêm ghi chú");

        Supplier<ClsMessage> messageSupplier = () -> {
            Long ticketNum = ticket.getDetail().getTicketNumber();
            ClsMessage clsMsg = ClsMessage.forTicket(ticketNum);

            String noteHtml = ticket.getNoteHtml();
            String bodyHtml = ticket.getBodyHtml();
            clsMsg.setBody(bodyHtml + "<br/>" + "-".repeat(20) + "<br/>" + noteHtml);
            return clsMsg;
        };

        Long noteId = ticket.getDetail().getNoteId();
        Long ticketNum = ticket.getDetail().getTicketNumber();

        if(noteId != null && state == ExistState.NONE) {
            throw Errors.odNoteHasCreate(ticketNum, noteId);
        }

        if(noteId == null) {
            ClsMessage clsMsg = messageSupplier.get();
            ClsMessage clsNew = service.ticket().createNote(ticketNum, clsMsg);
            return saveTicket(ticket, clsNew, TicketAction.ADD_NOTE);
        }
        else {
            switch (state) {
                case UPDATE -> {
                    ClsMessage clsMessage = messageSupplier.get();
                    ClsMessage clsNew = service.ticket().updateNote(noteId, clsMessage);
                    saveTicket(ticket, clsNew, TicketAction.UPDATE_NOTE);
                }
                case DELETE -> {
                    service.ticket().deleteNote(noteId);
                    createNote(service, ticket, ExistState.NONE);
                }
                case CREATE -> {
                    ticket.getDetail().resetNote();
                    createNote(service, ticket, ExistState.NONE);
                }
            }

            return ticket;
        }
    }

    private Ticket attachFile(OdTicketService service, Ticket ticket, ObjectMap base64Images) {
        validateTicketNoSend(ticket, "Không thể đính kèm");

        String images = ticket.getImages();
        if (Objects.isBlank(images)) {
            log.info("Ticket [%s] not images attach".formatted(ticket.getId()));
            return ticket;
        }

        // extract image text to map
        Set<String> names = Set.of(images.split(";"));
        ObjectMap imageMap = new ObjectMap();
        for (String imageName : names) {
            imageName = SendTicketDto.prepareImageName(imageName, ".png");
            String base64 = base64Images.getString(imageName);
            if (Objects.isBlank(base64)) throw Errors.invalidImageAttach(ticket.getId(), names);
            else imageMap.set(imageName, base64);
        }

        ticket.setImageBase64(imageMap);

        Long ticketNum = ticket.getDetail().getTicketNumber();
        ClsMessage clsNote = service.ticket().createNote(ticketNum, imageMap);
        return saveTicket(ticket, clsNote, TicketAction.ATTACH_FILE);
    }

    private Ticket sendEmail(OdTicketService service, Ticket ticket) {
        validateTicketNoSend(ticket, "Không thể gửi email.");

        if (!ticket.isEmailTicket()) {
            throw Errors.noEmailTicket(ticket.getId());
        }

        Long ticketNum = ticket.getDetail().getTicketNumber();
        Long[] partnerId = new Long[]{ticket.getOdPartnerId()};

        ClsMailComposeMsg msg = service.ticket().actionReply(ticketNum, partnerId,
                ticket.getSubject(), ticket.getContentEmail());

        return saveTicket(ticket, msg, TicketAction.SEND_MAIL);
    }

    private void validateTicketNoSend(Ticket ticket, String message) {
        if (!ticket.isSendTicket()) throw Errors
                .odTicketNotCreate(ticket.getId(), message);
    }

    private Ticket saveTicket(Ticket ticket, Object clsNew, TicketAction action) {
        TicketDetail detail;

        switch (action) {
            case CREATE_TICKET -> {
                ClsTicket cls = (ClsTicket) clsNew;
                OdTRHelper.updateTicket(ticket, cls);
                detail = ticket.getDetail();
                detail.setTicketNumber(cls.getId());
                detail.setTicketText(cls.getName());
                detail.setSendAt(cls.getCreateAt());
            }
            case UPDATE_TICKET -> {
                ClsTicket cls = (ClsTicket) clsNew;
                OdTRHelper.updateTicket(ticket, cls);
                detail = ticket.getDetail();
                detail.setModifyAt(cls.getWriteAt());
            }

            case DELETE_TICKET -> {
                ClsTicket cls = (ClsTicket) clsNew;
                ticket.getDetail().setDeleteAt(LocalDateTime.now());
            }
            case ADD_NOTE -> {
                ClsMessage cls = (ClsMessage) clsNew;
                ticket.setEditNote(false);
                detail = ticket.getDetail();
                detail.setNoteId(cls.getId());
                detail.setNoteAt(cls.getDate());
            }
            case UPDATE_NOTE -> {
            }
            case ATTACH_FILE -> {
                ClsMessage cls = (ClsMessage) clsNew;
                ClsFileMap fileMap = cls.getFileMap();

                if (fileMap.isEmpty()) {
                    throw new IllegalArgumentException("attach_file -> file is empty");
                }

                ObjectMap attachMap = new ObjectMap();
                for (String fileName : fileMap.keySet()) {
                    ClsFile clsFile = fileMap.get(fileName);
                    attachMap.set(fileName, clsFile);
                }

                detail = ticket.getDetail();
                detail.setImageAt(LocalDateTime.now());
                detail.getAttach().set(attachMap);
            }
            case SEND_MAIL -> {
                ClsMailComposeMsg clsMsg = (ClsMailComposeMsg) clsNew;
                detail = ticket.getDetail();
                detail.setMailAt(LocalDateTime.now());
                detail.setMailId(clsMsg.getId());
            }

            case UPDATE_STAGE, CLOSE_TICKET -> {
                ClsTicket cls = (ClsTicket) clsNew;
                OdTRHelper.updateTicket(ticket, cls);
            }

            default -> throw Errors.invalidAction(action);
        }

        return ticketService.save(ticket);


    }

}