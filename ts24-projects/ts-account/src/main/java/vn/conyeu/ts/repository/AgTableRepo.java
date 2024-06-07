package vn.conyeu.ts.repository;

import vn.conyeu.ts.domain.AgTable;
import vn.conyeu.common.repository.LongUIdRepo;

import java.util.Optional;

public interface AgTableRepo extends LongUIdRepo<AgTable> {

    Optional<AgTable> findByCode(String agCode);
}