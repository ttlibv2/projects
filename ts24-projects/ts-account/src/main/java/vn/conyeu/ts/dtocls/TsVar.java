package vn.conyeu.ts.dtocls;

public interface TsVar {

    interface Rest {
        String tsApiPrefix = "/ts-api";
        String tsAgColumn = tsApiPrefix + "/ag-column";
        String tsAgTable = tsApiPrefix + "/ag-table";
        String tsApiInfo = tsApiPrefix + "/api-info";
        String tsCatalog = tsApiPrefix + "/catalog";
        String tsGroupHelp = tsApiPrefix + "/group-help";
        String tsUser = tsApiPrefix + "/user";;
        String tsUserApi = tsApiPrefix + "/user-api";
        String tsTicket = tsApiPrefix + "/ticket";
        String tsSoftware = tsApiPrefix + "/software";
        String tsSetting = tsApiPrefix + "/setting";
        String tsQuestion = tsApiPrefix + "/question";
        String tsChanel = tsApiPrefix + "/chanel";

        String odTicketApiPrefix = "/od-api";
        String odUser = odTicketApiPrefix + "/od.user";
        String odTicket = odTicketApiPrefix + "/od.ticket";
        String odCatalog =  odTicketApiPrefix + "/od.catalog";
        String odPartner = odTicketApiPrefix + "/od.partner";
    }
}