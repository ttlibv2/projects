package vn.conyeu.address.service;

import org.springframework.stereotype.Service;
import vn.conyeu.address.domain.Address;
import vn.conyeu.address.repository.AddressRepo;
import vn.conyeu.common.service.LongUIdService;

@Service
public class AddressService extends LongUIdService<Address, AddressRepo> {

    public AddressService(AddressRepo domainRepo) {
        super(domainRepo);
    }
}