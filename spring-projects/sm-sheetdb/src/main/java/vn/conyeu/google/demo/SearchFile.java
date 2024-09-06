package vn.conyeu.google.demo;

import lombok.extern.slf4j.Slf4j;
import vn.conyeu.google.drives.DriveQuery;
import vn.conyeu.google.drives.GMime;
import vn.conyeu.google.drives.DriveQuery.*;
import vn.conyeu.google.query.BoolExpression;
import java.util.function.Function;

@Slf4j
public class SearchFile {

    public static void main(String[] args) {
        Function<DriveQuery, BoolExpression> consumer = q -> q.name.eq("1222")
                .andAnyOf(q.mimeType.eq(GMime.AUDIO).or(q.mimeType.eq(GMime.FILE)))
                .and(q.memberCount.eq(123444));


        BoolExpression expr = consumer.apply(new DriveQuery());
        DriveSerializer serializer = new DriveSerializer().handle(expr);
        log.warn("{}", serializer.toString());


    }




}