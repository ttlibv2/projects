package vn.conyeu.ts.ticket_rest;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import vn.conyeu.commons.beans.ObjectMap;
import vn.conyeu.ts.domain.Ticket;
import vn.conyeu.ts.domain.TicketDetail;
import vn.conyeu.ts.dtocls.Errors;
import vn.conyeu.ts.dtocls.SendTicketDto;
import vn.conyeu.ts.dtocls.TsVar;
import vn.conyeu.ts.odcore.domain.ClsPage;
import vn.conyeu.ts.service.OdService;
import vn.conyeu.ts.service.TicketService;
import vn.conyeu.ts.service.UserApiService;
import vn.conyeu.ts.ticket.domain.*;
import vn.conyeu.ts.ticket.service.OdTicketService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
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
        if(optional.isEmpty()) throw Errors.odTicketNotCreate(ticketId);
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
        if(partnerId != null ) {
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
    public List<Long> deleteFollowExludeUser( @PathVariable Long ticketNumber) {
        return service().ticket().deleteFollow(ticketNumber);
    }

    @PostMapping("send")
    public Object sendTicket(@RequestBody @Valid SendTicketDto dto) {
        final Ticket ticket = ticketService.getById(dto.getTicketId());
        final String action = dto.getAction().toLowerCase();
        final List<String> segments = Stream.of(action.split(",")).map(String::trim).toList();
        final boolean isAll = "all".equals(action);

        if(isAll || segments.contains("create_ticket") || !ticket.isSendTicket()) {
            createTicket(service(), ticket, dto.isUpdateTicket());
        }

        if(isAll || segments.contains("add_note") || dto.isUpdateNote()) {
            createNote(service(), ticket, dto.getImageBase64(), dto.isUpdateNote());
        }

        if(isAll || segments.contains("attach_image") || dto.isUpdateNote()) {
            attachFileForTicket(service(), ticket, dto.getImageBase64());
        }

        if (isAll || segments.contains("send_email")) {
            sendEmailTicket(service(), ticket);
        }

        if(isAll || segments.contains("close_ticket")) {
            closeTicket(service(), ticket);
        }

        return ticket;
    }

    private void createTicket(OdTicketService service, Ticket ticket, boolean isUpdateTicket) {
        if(!ticket.isSendTicket()) {
            ClsTicket clsTicket = OdTRHelper.fromTicket(ticket);
            ClsTicket clsNew = service.ticket().createNew(clsTicket);
            service.ticket().deleteFollow(clsNew.getId());
            saveTicket(ticket, clsNew, TicketAction.CREATE_TICKET);
        }//
        else if(isUpdateTicket && ticket.isEditTicket()) {
            updateTicket(service, ticket);
        }
    }

    private void updateTicket(OdTicketService service, Ticket ticket) {
        validateTicketNoSend(ticket, "Không thể cập nhật");

        // nếu ticket lấy từ web thì ko cập nhật
        if(ticket.isWeb()) {
            log.warn("Ticket `{}` này lấy từ web nên không cho cập nhật.",
                    ticket.getDetail().getTicketNumber());
            return;
        }

        Long ticketNum = ticket.getDetail().getTicketNumber();
        ClsTicket clsTicket = OdTRHelper.fromTicket(ticket);
        ClsTicket clsNew = service.ticket().updateTicket(ticketNum, clsTicket);
        saveTicket(ticket, clsNew, TicketAction.UPDATE_TICKET);
    }

    private Ticket closeTicket(OdTicketService service, Ticket ticket) {
        validateTicketNoSend(ticket, "Không thể đóng");
        Long ticketNum = ticket.getDetail().getTicketNumber();
        ClsTicket clsTicket = service.ticket().closeTicket(ticketNum);
        return saveTicket(ticket, clsTicket, TicketAction.CLOSE_TICKET);
    }

    private void createNote(OdTicketService service, Ticket ticket, ObjectMap imageBase64, boolean isUpdateNote) {
        validateTicketNoSend(ticket, "Không thể thêm ghi chú");

        Long ticketNum = ticket.getDetail().getTicketNumber();
        Long noteId = ticket.getDetail().getNoteId();
        if(noteId != null && !isUpdateNote) {
            throw Errors.odNoteHasCreate(ticketNum, noteId) ;
        }

        //-- create | update
        ClsMessage clsMsg = ClsMessage.forTicket(ticketNum);

        //------ edit ngày 10/03/2023 -> thêm body vào note
        String noteHtml = ticket.getNoteHtml();
        String bodyHtml = ticket.getBodyHtml();
        clsMsg.setBody(bodyHtml+"<br/>"+"-".repeat(20)+"<br/>"+noteHtml);

        //--> create
        if(noteId == null) {
            ClsMessage clsNew = service.ticket().createNote(ticketNum, clsMsg);
            saveTicket(ticket, clsNew, TicketAction.ADD_NOTE);
        }

        //--> update
        else if(ticket.isEditNote()) {
            ClsMessage clsNew = service.ticket().updateNote(noteId, clsMsg);
            saveTicket(ticket, clsNew, TicketAction.UPDATE_NOTE);
        }

    }

    private Ticket attachFileForTicket(OdTicketService service, Ticket ticket, ObjectMap imageBase64) {
        validateTicketNoSend(ticket, "Không thể đính kèm");

        ObjectMap odImg = ticket.getOdImage();
        if (odImg.isEmpty() || ticket.isUpFile()) return ticket;

        // only send file not create
        Set<String> allFile = odImg.keySet().stream().filter(k -> odImg.get(k) == null).collect(Collectors.toSet());
        if (allFile.isEmpty()) return ticket;

        // filter file_name
        ObjectMap imageObj = imageBase64.get(allFile, false);
        if (allFile.size() != imageObj.size()) {
            List<String> names = imageObj.keySet().stream().filter(k -> !allFile.contains(k)).toList();
             throw Errors.invalidImageAttach(ticket.getId(), names) ;
        }

        Long ticketNum = ticket.getDetail().getTicketNumber();
        ClsMessage clsNote = service.ticket().createNote(ticketNum, imageObj);
        return saveTicket(ticket, clsNote, TicketAction.ATTACH_FILE);
    }

    private void sendEmailTicket(OdTicketService service, Ticket ticket) {
        validateTicketNoSend(ticket, "Không thể gửi email.");

        if(!ticket.isEmailTicket()) {
            throw Errors.noEmailTicket(ticket.getId());
        }

        Long ticketNum = ticket.getDetail().getTicketNumber();
        Long[] partnerId = new Long[] { ticket.getOdPartnerId()};

        Long emailId = service.ticket().actionReply(ticketNum, partnerId,
                ticket.getSubject(), ticket.getContentEmail());

        ticket.getDetail().setMailAt(LocalDateTime.now());
        ticket.getDetail().setMailId(emailId);
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
            case UPDATE_NOTE ->  {}
            case ATTACH_FILE -> {
                ClsMessage cls = (ClsMessage) clsNew;
                detail = ticket.getDetail();
                detail.setImageAt(LocalDateTime.now());

                ClsFileMap fileMap = cls.getFileMap();

                if (fileMap.isEmpty()) {
                    throw new IllegalArgumentException("attach_file -> file is empty");
                }

                ObjectMap odImg = ticket.getOdImage();
                for (String fileName : fileMap.keySet()) {
                    odImg.set(fileName, fileMap.get(fileName).getId());
                }
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