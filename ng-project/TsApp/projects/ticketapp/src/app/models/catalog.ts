import { Chanel } from "./chanel";
import { Software } from "./software";
import { GroupHelp } from "./group-help";
import { Question } from "./question";
import { BaseModel } from "./base-model";
import { AssignObject, JsonObject } from "./common";
import * as cls from "./od-cls";
import { Templates } from "./template";
import { LoggerService } from "ts-ui/logger";


export class Catalog extends BaseModel {
   ls_chanel: Chanel[];// = [];
   ls_software: Software[];// = [];
   ls_group_help: GroupHelp[];// = [];
   ls_question: Question[];// = [];


   ls_helpdesk_team: cls.ClsTeam[];// = [];
   ls_assign: cls.ClsAssign[];// = [];
   ls_product: cls.ClsProduct[];// = [];
   ls_subject_type: cls.ClsSubjectType[];// = [];
   ls_repiled_status: cls.ClsRepiled[];// = [];
   ls_stage: cls.ClsStage[];// = [];
   ls_category: cls.ClsCategory[];// = [];
   ls_category_sub: cls.ClsCategorySub[];// = [];
   ls_ticket_tag: cls.ClsTag[];// = [];
   ls_priority: cls.ClsPriority[];// = [];
   ls_ticket_type: cls.ClsTicketType[];// = [];
   ls_topic: cls.ClsTopic[];// = [];
   ls_team_head: cls.ClsTeamHead[];// = [];
   ls_template: Templates = new Templates();

   set_ls_chanel(data: AssignObject<Chanel>[]) {
      this.ls_chanel = (data || []).map(item => Chanel.from(item));
   }


   set_ls_software(data: AssignObject<Software>[]) {
      this.ls_software = (data || []).map(item => Software.from(item));
   }


   set_ls_group_help(data: AssignObject<GroupHelp>[]) {
      this.ls_group_help = (data || []).map(item => GroupHelp.from(item));
   }


   set_ls_question(data: AssignObject<Question>[]) {
      this.ls_question = (data || []).map(item => Question.from(item));
   }


   set_ls_helpdesk_team(data: AssignObject<cls.ClsTeam>[]) {
      this.ls_helpdesk_team = (data || []).map(item => cls.ClsTeam.from(item));
   }

   set_ls_assign(data: AssignObject<cls.ClsAssign>[]) {
      this.ls_assign = (data || []).map(item => cls.ClsAssign.from(item));
   }

   set_ls_subject_type(data: AssignObject<cls.ClsSubjectType>[]) {
      this.ls_subject_type = (data || []).map(item => cls.ClsSubjectType.from(item));
   }

   set_ls_repiled_status(data: AssignObject<cls.ClsRepiled>[]) {
      this.ls_repiled_status = (data || []).map(item => cls.ClsRepiled.from(item));
   }

   set_ls_stage(data: AssignObject<cls.ClsStage>[]) {
      this.ls_stage = (data || []).map(item => cls.ClsStage.from(item));
   }

   set_ls_category(data: AssignObject<cls.ClsCategory>[]) {
      this.ls_category = (data || []).map(item => cls.ClsCategory.from(item));
   }

   set_ls_category_sub(data: AssignObject<cls.ClsCategorySub>[]) {
      this.ls_category_sub = (data || []).map(item => cls.ClsCategorySub.from(item));
   }

   set_ls_ticket_tag(data: AssignObject<cls.ClsTag>[]) {
      this.ls_ticket_tag = (data || []).map(item => cls.ClsTag.from(item));
   }

   set_ls_priority(data: AssignObject<cls.ClsPriority>[]) {
      this.ls_priority = (data || []).map(item => cls.ClsPriority.from(item));
   }

   set_ls_ticket_type(data: AssignObject<cls.ClsTicketType>[]) {
      this.ls_ticket_type = (data || []).map(item => cls.ClsTicketType.from(item));
   }

   set_ls_topic(data: AssignObject<cls.ClsTopic>[]) {
      this.ls_topic = (data || []).map(item => cls.ClsTopic.from(item));
   }

   set_ls_team_head(data: AssignObject<cls.ClsTeamHead>[]) {
      this.ls_team_head = (data || []).map(item => cls.ClsTeamHead.from(item));
   }

   set_ls_template(data: any) {
      this.ls_template = Templates.fromAny(data);
   }



   static from(json: JsonObject, logger?: LoggerService): Catalog {
      return new Catalog().update(json);
   }

}
