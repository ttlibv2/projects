package vn.conyeu.google.demo;

import vn.conyeu.google.drives.GMime;
import vn.conyeu.google.drives.builder.DriveQuery;
import vn.conyeu.google.query.BoolExpression;
import java.util.function.Function;

public class SearchFile {

    public static void main(String[] args) {
        Function<DriveQuery, BoolExpression> consumer = q -> q.name.eq("1222").and(q.mimeType.eq(GMime.AUDIO));
        
    }




}