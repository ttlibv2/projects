package vn.conyeu.ts.ticket.service;

import vn.conyeu.ts.odcore.domain.ClsApiCfg;
import vn.conyeu.ts.odcore.service.OdApp;
import vn.conyeu.ts.odcore.service.OdClient;

public class TicketApp extends OdApp {

   TicketApp(ClsApiCfg clsCfg, OdClient client) {
      super(clsCfg, client);
   }

   public final String getAppUID() {
      return "od.ticket";
   }

   /**
    * Returns {@link OdCategory}
    */
   public OdCategory category() {
      //return service(OdCategory.class, () -> new OdCategory(clsConfig));
      return new OdCategory(this);
   }







}