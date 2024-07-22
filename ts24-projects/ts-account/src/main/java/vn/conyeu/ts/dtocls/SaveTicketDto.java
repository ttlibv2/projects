package vn.conyeu.ts.dtocls;

import vn.conyeu.ts.odcore.domain.ClsUser;
import vn.conyeu.ts.ticket.domain.*;

import java.io.Serializable;
import java.util.List;

public class SaveTicketDto implements Serializable {
    private List<Long> chanels;
    private Long software;
    private Long groupHelp;
    private Long supportHelp;
    private String softname;
    private ClsHelpdeskTeam supportTeam;
    private ClsUser assign;
    private ClsSubjectType subjectType;
    private ClsRepiledStatus repliedStatus;
    private ClsCategory category;
    private ClsCategorySub categorySub;
    private ClsTeamHead teamHead;
    private ClsTicketType ticketType;
    private ClsTicketPriority priority;
    private List<ClsTicketTag> tags;


}