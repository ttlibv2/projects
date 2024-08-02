import { BaseModel } from "./base-model";
import { AssignObject, JsonObject } from "./common";

export type ClsOperator = 'like' | 'not_like' | 'equal' | 'not_equal' | 'is_set' | 'not_is_set';

export class ClsSearch<E = any> extends BaseModel {

  operator: ClsOperator = 'like';
  limit: number = 20;
  offset: number = 0;
  data: E;


  static from<E>(json: AssignObject<ClsSearch>): ClsSearch<E> {
    return new ClsSearch().update(json);
  }

}

export abstract class ClsModel extends BaseModel {
  name?: string;
  display_name?: string;
  id?: number;

}

export class ClsAssign extends ClsModel {

  static fromList(res: any[]): ClsAssign[] {
    return res.flatMap(item => ClsAssign.from(item));
  }

  static from(json: AssignObject<ClsAssign>): ClsAssign {
    return BaseModel.fromJson(ClsAssign, json);
  }
}

export class ClsCategorySub extends ClsModel {
  static from(json: AssignObject<ClsCategorySub>): ClsCategorySub {
    return BaseModel.fromJson(ClsCategorySub, json);
  }
}

export class ClsCategory extends ClsModel {
  static from(json: AssignObject<ClsCategory>): ClsCategory {
    return BaseModel.fromJson(ClsCategory, json);
  }
}


export class ClsPartner extends ClsModel {
  email?: string;
  phone?: string;
  vat?: string;
  type?: string;
  company_type?: 'person' | 'company';
  street?: string;
  mobile?: string;
  company_id?: number;
  company_name?: string;
  customer_id?: number;
  customer_name?: string;
  parent_id?: any;

  static from(json: AssignObject<ClsPartner>): ClsPartner {
    return BaseModel.fromJson(ClsPartner, json);
  }
}

export class ClsPriority extends ClsModel {
  static from(json: AssignObject<ClsPriority>): ClsPriority {
    return BaseModel.fromJson(ClsPriority, json);
  }
}

export class ClsReplied extends ClsModel {
  static from(json: AssignObject<ClsReplied>): ClsReplied {
    return BaseModel.fromJson(ClsReplied, json);
  }
}


export class ClsStage extends ClsModel {
  static from(json: AssignObject<ClsStage>): ClsStage {
    return BaseModel.fromJson(ClsStage, json);
  }
}



export class ClsSubjectType extends ClsModel {
  static from(json: AssignObject<ClsSubjectType>): ClsSubjectType {
    return BaseModel.fromJson(ClsSubjectType, json);
  }
}

export class ClsTag extends ClsModel {
  static from(json: AssignObject<ClsTag>): ClsTag {
    return BaseModel.fromJson(ClsTag, json);
  }
}

export class ClsTeam extends ClsModel {
  members: ClsAssign[];
  team_members: number[];
  team_head: ClsTeamHead;

  static from(json: AssignObject<ClsTeam>): ClsTeam {
    return BaseModel.fromJson(ClsTeam, json);
  }

  override update(object: JsonObject | Partial<ClsTeam>): this {
    super.update(object);

    if ('ls_team_member' in object) {
      const ls: any[] = object['ls_team_member'];
      this.members = ls.flatMap(i => ClsAssign.from(i));
    }

    if ('team_head' in object) {
      const ls: any = object['team_head'];
      this.team_head = ClsTeamHead.from(ls);
    }

    return this;
  }
}

export class ClsTeamHead extends ClsModel {
  static from(json: AssignObject<ClsTeamHead>): ClsTeamHead {
    return BaseModel.fromJson(ClsTeamHead, json);
  }
}

export class ClsTicketType extends ClsModel {
  static from(json: AssignObject<ClsTicketType>): ClsTicketType {
    return BaseModel.fromJson(ClsTicketType, json);
  }
}

export class ClsTopic extends ClsModel {
  static from(json: AssignObject<ClsTopic>): ClsTopic {
    return BaseModel.fromJson(ClsTopic, json);
  }
}

export class ClsProduct extends ClsModel {
  static from(json: AssignObject<ClsProduct>): ClsProduct {
    return BaseModel.fromJson(ClsProduct, json);
  }
}



export class ClsUser extends ClsModel {
  email: string;
  
  static from(json: AssignObject<ClsUser>): ClsUser {
    return BaseModel.fromJson(ClsUser, json);
  }
}