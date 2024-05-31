package vn.conyeu.ts.repository;

import vn.conyeu.ts.domain.AgTable;
import vn.conyeu.common.repository.LongIdRepo;

import java.util.Optional;

public interface AgTableRepo extends LongIdRepo<AgTable> {

    Optional<AgTable> findByCode(String agCode);
}