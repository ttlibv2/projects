package vn.conyeu.ts.dtocls;

import lombok.Getter;
import lombok.Setter;
import vn.conyeu.ts.domain.Chanel;
import vn.conyeu.ts.domain.Ticket;
import java.util.List;

@Getter @Setter
public class TicketDto extends Ticket {
    private List<Chanel> chanels;
    private Chanel firstChanel;
}