package vn.conyeu.identity.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import vn.conyeu.address.domain.BaseAddress;

@Entity @Table @Getter @Setter
public class AccountAddress extends BaseAddress<AccountAddress> {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "accountId", nullable = false)
    private Account account;

}