package vn.conyeu.ts.ticket.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import vn.conyeu.commons.beans.ObjectMap;
import vn.conyeu.ts.odcore.domain.ClsModel;

import java.util.Set;

@Data
@EqualsAndHashCode(callSuper = false)
public class ClsHelpdeskTeam extends ClsModel<ClsHelpdeskTeam> {
    static final Set<String> FIELD_IGNORE = Set.of("team_head_obj");

    Integer id;
    String name;
    String display_name;
    String email_sender;
    String team_email;
    Object team_head; // object[] | ObjectMap
    Integer[] team_members;

    Object[] sh_resource_calendar_id;
    Integer sla_count;

    //ClsTeamHead team_head_obj;

    public ClsHelpdeskTeam setTeam_head(Object team_head) {
        //this.team_head_obj = ClsTeamHead.from(objectToMap(team_head));
        this.team_head = ClsTeamHead.from(objectToMap(team_head));
        return this;
    }

    public static ClsHelpdeskTeam from(Object team_id) {
        if(team_id == null) return null;
        else return from(objectToMap(team_id));
    }

    public Integer getTeamHeadId() {
        return getObjectID(team_head);
    }


    public static ClsHelpdeskTeam from(ObjectMap obj) {
        ClsHelpdeskTeam team = obj.asObject(ClsHelpdeskTeam.class);
        team.team_head = objectToMap(obj.get("team_head"));
        team.sh_resource_calendar_id = null;
        return team;
    }

    @JsonIgnore
    public ClsTeamHead getClsTeamHead() {
        return team_head instanceof ClsTeamHead head ? head : null;
    }


}