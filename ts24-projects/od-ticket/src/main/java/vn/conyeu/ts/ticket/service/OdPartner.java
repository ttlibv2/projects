package vn.conyeu.ts.ticket.service;

import org.springframework.util.StringUtils;
import vn.conyeu.common.exception.BaseException;
import vn.conyeu.commons.beans.ObjectMap;
import vn.conyeu.commons.utils.Objects;
import vn.conyeu.ts.odcore.domain.ClsApiConfig;
import vn.conyeu.ts.odcore.domain.ClsPage;
import vn.conyeu.ts.ticket.domain.ClsFilterOption;
import vn.conyeu.ts.ticket.domain.ClsPartner;
import vn.conyeu.ts.ticket.domain.ClsSearchReadOption;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

public class OdPartner extends OdTicketCore<ClsPartner> {

    public OdPartner(ClsApiConfig apiConfig) {
        super(apiConfig);
    }

    @Override
    public String getModel() {
        return "res.partner";
    }

//    @Override
//    public String getBasePath() {
//        return "call_kw/res.partner";
//    }


    /**
     * POST `search_read`
     * Tìm kiếm thông tin người liên hệ
     * @param filter {[key: string]: any}
     * @param page {[key: string]: any}
     */
    public List<ClsPartner> search(ObjectMap filter, ClsPage page) {
        ClsSearchReadOption option = new ClsSearchReadOption(filter, page);
        return searchRead(option).stream().map(this::fixPartner).collect(Collectors.toList());
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

   public ClsPartner create(Long companyId, ClsPartner p) {
        p.setActive(true);
        p.setType("contact");
        p.set("partner_gid", 0)
                .set("customer", true)
                .set("lang", "en_US")
                .set("company_id", companyId)
                .set("is_company", Objects.equals(p.getCompany_type(), "company"))
                .set("property_account_receivable_id", 9)
                .set("property_account_payable_id", 71);

        checkErrorPartner(p);

       Long partnerId = createAndReturnMap(p).getLong("result");
       return findById(partnerId).orElseThrow(() -> BaseException.e500("no_id")
               .message("Đã xảy ra lỗi lấy thông tin có mã %s", partnerId));
    }

    private void checkErrorPartner(ClsPartner p) {
        ObjectMap error = ObjectMap.create();

        if(StringUtils.isEmpty(p.getName()))
            error.set("name", "Tên không được trống.");

        if(StringUtils.isEmpty(p.getEmail()))
            error.set("email", "E-mail không được trống.");

        if(p.getIs_company() && StringUtils.isEmpty(p.getVat())) {
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
            p.setComp_id(p.getId());
            p.setComp_name(p.getName());
        }

        else
        {
            p.setCustomer_id(p.getId());
            p.setCustomer_name(p.getName());

            Object[] company = Objects.toObjectArray(p.getParent_id());
            if (Objects.notEmpty(company) && company.length == 2) {
                p.setComp_id(Long.parseLong(String.valueOf(company[0])));
                p.setComp_name(String.valueOf(company[1]));
            }
        }

        return p;
    }

    public List<ClsPartner> find(ClsFilterOption filterOption) {
        return searchRead(filterOption);
    }

    public List<ClsPartner> find(ObjectMap searchObj) {
        return searchRead(searchObj);
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