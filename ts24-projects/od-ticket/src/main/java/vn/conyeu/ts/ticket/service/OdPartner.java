package vn.conyeu.ts.ticket.service;

import org.springframework.data.domain.Page;
import vn.conyeu.common.exception.BaseException;
import vn.conyeu.commons.beans.ObjectMap;
import vn.conyeu.commons.utils.Objects;
import vn.conyeu.ts.odcore.domain.ClsSearch;
import vn.conyeu.ts.ticket.domain.ClsPartner;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import vn.conyeu.ts.odcore.domain.ClsApiCfg;
public class OdPartner extends OdTicketClient<ClsPartner> {

    static class PartnerFilterOption {}

    public OdPartner(ClsApiCfg apiConfig) {
        super(apiConfig);
    }

    @Override
    public String getModel() {
        return "res.partner";
    }

    /**
     * POST `search_read`
     * Tìm kiếm thông tin người liên hệ
     * @param filter {[key: string]: any}
     */
    public Page<ClsPartner> search(ClsSearch filter) {
        return searchRead(filter, this::fixPartner);
    }

    /**
     * Lấy thông tin người liên hệ bởi id
     * @param partnerId number
     * */
    public Optional<ClsPartner> findById(Long partnerId) {
        List<ClsPartner> all = read(Collections.singletonList(partnerId));
        if(all.isEmpty()) return Optional.empty();
        else return fixPartner(all.get(0)).asOptional();
    }

    public ClsPartner create(ClsPartner p) {
        Long compId = cfg.getClsUser().getCompany_Uid();
        return create(compId, p);
    }
   public ClsPartner create(Long companyId, ClsPartner p) {
        p.setActive(true);
        p.setType("contact");
        p.setIs_company(Objects.equals(p.getCompany_type(), "company"));
        p.setCompany_id(companyId);
        p.set("partner_gid", 0)
                //.set("customer", true)
                .set("lang", "en_US")
                //.set("company_id", companyId)
                //.set("is_company", Objects.equals(p.getCompany_type(), "company"))
                .set("property_account_receivable_id", 9)
                .set("property_account_payable_id", 71);

        checkErrorPartner(p);

       Long partnerId = createAndReturnMap(p).getLong("result");
       return findById(partnerId).orElseThrow(() -> BaseException.e500("no_id")
               .message("Đã xảy ra lỗi lấy thông tin có mã %s", partnerId));
    }

    private void checkErrorPartner(ClsPartner p) {
        ObjectMap error = ObjectMap.create();

        if(Objects.isBlank(p.getName()))
            error.set("name", "Tên không được trống.");

        if(Objects.isBlank(p.getEmail()))
            error.set("email", "E-mail không được trống.");

        if(p.getIs_company() && Objects.isBlank(p.getVat())) {
            error.set("vat", "MST không được trống.");
        }

        if(!error.isEmpty()) {
            String params = String.join("|", error.keySet());
            throw BaseException.e400("miss_param").arguments(error)
                    .message("Lỗi dữ liệu đầu vào ở [%s]", params);
        }
    }

    private ClsPartner fixPartner(ClsPartner p) {
        if(Objects.equals(p.getPhone(),"false")) p.setPhone(null);
        if(Objects.equals(p.getMobile(),"false")) p.setMobile(null);
        if(Objects.equals(p.getEmail(), "false")) p.setEmail(null);

        if(p.getIs_company()) {
            p.setCompany_id(p.getId());
            p.setCompany_name(p.getName());
        }

        else
        {
            p.setCustomer_id(p.getId());
            p.setCustomer_name(p.getName());

            Object[] company = Objects.toObjectArray(p.getParent_id());
            if (Objects.notEmpty(company) && company.length == 2) {
                p.setCompany_id(Long.parseLong(String.valueOf(company[0])));
                p.setCompany_name(String.valueOf(company[1]));
            }
        }

        return p;
    }

    @Override
    protected Class<ClsPartner> getDomainCls() {
        return ClsPartner.class;
    }

    @Override
    protected Function<ObjectMap, ClsPartner> mapToObject() {
        return ClsPartner::from;
    }
}