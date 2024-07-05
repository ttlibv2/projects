import { Chanel } from "./chanel";
import { Software } from "./software";
import { GroupHelp } from "./group-help";
import { Question } from "./question";
import { BaseModel } from "./base-model";
import { AssignObject, JsonObject } from "./common";
import * as cls from "./od-cls";
import { Template, Templates } from "./template";
import { LoggerService } from "ts-logger";
import { Asserts, Objects } from "ts-helper";

const {isArray, isObject, notEmpty} = Objects;

function setData(object: any,  target: any, field: string, itemCb: (item: any) => any) {
   if(field in object) {
      const fieldData = object[field];
      if(isArray(fieldData)) target[field] = fieldData.map(item => itemCb(item));
      else if(isObject(fieldData)) {
         const arrays: any[] = Object.keys(fieldData).map(k => [k, (<any[]>fieldData[k]).map(i => itemCb(i))]);
         target[field] = new Map(arrays);
      }
      else throw new Error(`The field data ${fieldData} not support type for field ${field}`);
      
   }

}



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
   ls_team_head: cls.ClsTeamHead[] = [];
   ls_template: Templates = new Templates();

   static from(json: JsonObject, logger?: LoggerService): Catalog {
      return new Catalog().update(json);
   }

   override update(object: AssignObject<this>): this {
      //super.update(object);
      setData(object, this, 'ls_chanel', item => Chanel.from(item));
      setData(object, this, 'ls_software', item => Software.from(item));
      setData(object, this, 'ls_group_help', item => GroupHelp.from(item));
      setData(object, this, 'ls_question', item => Question.from(item));
      setData(object, this, 'ls_helpdesk_team', item => cls.ClsTeam.from(item));
      setData(object, this, 'ls_assign', item => cls.ClsAssign.from(item));
      setData(object, this, 'ls_product', item => cls.ClsProduct.from(item));

      setData(object, this, 'ls_subject_type', item => cls.ClsSubjectType.from(item));
      setData(object, this, 'ls_repiled_status', item => cls.ClsRepiled.from(item));
      setData(object, this, 'ls_stage', item => cls.ClsStage.from(item));
      setData(object, this, 'ls_category', item => cls.ClsCategory.from(item));
      setData(object, this, 'ls_category_sub', item => cls.ClsCategorySub.from(item));
      setData(object, this, 'ls_ticket_tag', item => cls.ClsTag.from(item));
      setData(object, this, 'ls_priority', item => cls.ClsPriority.from(item));
      setData(object, this, 'ls_ticket_type', item => cls.ClsTicketType.from(item));
      setData(object, this, 'ls_topic', item => cls.ClsTopic.from(item));
      setData(object, this, 'ls_team_head', item => cls.ClsTeamHead.from(item));
      setData(object, this, 'ls_template', item => Template.from(item));

      if('ls_template' in object) {
         const ls = object['ls_template'];
         this.ls_template = Templates.fromAny(ls);
      }

      return this;
   }

  


}
