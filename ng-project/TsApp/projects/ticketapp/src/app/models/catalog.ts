import { Chanel } from "./chanel";
import { Software } from "./software";
import { GroupHelp } from "./group-help";
import { Question } from "./question";
import { BaseModel } from "./base-model";
import { JsonObject } from "./common";
import * as cls from "./od-cls";

export class Catalog extends BaseModel {
   ls_chanel: Chanel[] = [];
   ls_software: Software[] = [];
   ls_group_help: GroupHelp[] = [];
   ls_question: Question[]= [];


   ls_helpdesk_team: cls.ClsTeam[] = [];
   ls_assign: cls.ClsAssign[] = [];
   ls_product: cls.ClsProduct[] = [];
   ls_subject_type: cls.ClsSubjectType[] = [];
   ls_repiled_status: cls.ClsRepiled[]= [];
   ls_stage: cls.ClsStage[] = [];
   ls_category: cls.ClsCategory[]= [];
   ls_category_sub: cls.ClsCategorySub[]= [];
   ls_ticket_tag: cls.ClsTag[]= [];
   ls_priority: cls.ClsPriority[]= [];
   ls_ticket_type: cls.ClsTicketType[]= [];
   ls_topic: cls.ClsTopic[]= [];
  

   static from(json: JsonObject): Catalog {
      return new Catalog().update(json);
   }

}
