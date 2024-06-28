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
        String tsSoftware = tsApiPrefix + "/software";
        String tsSetting = tsApiPrefix + "/setting";
        String tsQuestion = tsApiPrefix + "/question";
        String tsChanel = tsApiPrefix + "/chanel";
        String tsTicket = tsApiPrefix + "/ticket";
        String tsTicketTemplate = tsApiPrefix + "/template";

        String odTicketApiPrefix = "/od-api";
        String odUser = odTicketApiPrefix + "/user";
        String odTicket = odTicketApiPrefix + "/ticket";
        String odCatalog =  odTicketApiPrefix + "/catalog";
        String odPartner = odTicketApiPrefix + "/partner";

        String tsTest = tsApiPrefix + "/tests";
    }
}