package vn.conyeu.identity.service;

import org.springframework.stereotype.Service;
import vn.conyeu.identity.domain.Device;
import vn.conyeu.identity.repository.DeviceRepo;
import vn.conyeu.common.service.StringIdService;

@Service
public class DeviceService extends StringIdService<Device, DeviceRepo> {

    public DeviceService(DeviceRepo domainRepo) {
        super(domainRepo);
    }
}