import {BaseModel} from "./base-model";
import {JsonObject} from "./common";

export abstract class ClsModel extends BaseModel {
  name?: string;
  display_name?: string;
  model_id?: number;
}

export class ClsAssign  extends ClsModel {
    static from(json: JsonObject):  ClsAssign{
        return BaseModel.fromJson(ClsAssign, json);
    }
}

export class ClsCategorySub  extends ClsModel {
  static from(json: JsonObject):  ClsCategorySub{
    return BaseModel.fromJson(ClsCategorySub, json);
  }
}

export class ClsCategory  extends ClsModel {
  static from(json: JsonObject):  ClsCategory{
    return BaseModel.fromJson(ClsCategory, json);
  }
}

export class ClsPartner  extends ClsModel {
  static from(json: JsonObject):  ClsPartner{
    return BaseModel.fromJson(ClsPartner, json);
  }
}

export class ClsPriority  extends ClsModel {
  static from(json: JsonObject):  ClsPriority{
    return BaseModel.fromJson(ClsPriority, json);
  }
}

export class ClsRepiled  extends ClsModel {
  static from(json: JsonObject):  ClsRepiled{
    return BaseModel.fromJson(ClsRepiled, json);
  }
}


export class ClsStage  extends ClsModel {
  static from(json: JsonObject):  ClsStage{
    return BaseModel.fromJson(ClsStage, json);
  }
}



export class ClsSubjectType  extends ClsModel {
  static from(json: JsonObject):  ClsSubjectType{
    return BaseModel.fromJson(ClsSubjectType, json);
  }
}

export class ClsTag  extends ClsModel {
  static from(json: JsonObject):  ClsTag{
    return BaseModel.fromJson(ClsTag, json);
  }
}

export class ClsTeam  extends ClsModel {
  static from(json: JsonObject):  ClsTeam{
    return BaseModel.fromJson(ClsTeam, json);
  }
}

export class ClsTeamHead extends ClsModel  {
  static from(json: JsonObject):  ClsTeamHead{
    return BaseModel.fromJson(ClsTeamHead, json);
  }
}

export class ClsTicketType  extends ClsModel {
  static from(json: JsonObject):  ClsTicketType{
    return BaseModel.fromJson(ClsTicketType, json);
  }
}

export class ClsTopic extends ClsModel  {
  static from(json: JsonObject):  ClsTopic{
    return BaseModel.fromJson(ClsTopic, json);
  }
}

export class ClsProduct extends ClsModel  {
  static from(json: JsonObject):  ClsProduct{
    return BaseModel.fromJson(ClsProduct, json);
  }
}

