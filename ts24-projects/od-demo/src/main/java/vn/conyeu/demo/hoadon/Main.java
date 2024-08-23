package vn.conyeu.demo.hoadon;

import vn.conyeu.commons.utils.DateHelper;
import vn.conyeu.commons.utils.MapperHelper;
import vn.conyeu.demo.hoadon.cls.ClsBienLai;
import vn.conyeu.demo.hoadon.help.XmlHelper;

import java.awt.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Stream;


public class Main {
    static Path folder = Paths.get("C:\\Users\\tuannq\\Desktop\\BL\\20240703\\BLP");

    public static void main(String[] args) throws IOException {
        try(Stream<Path> pathList = Files.list(folder)) {
            for(Path path: pathList.toList()) {
                ClsBienLai cls = XmlHelper.parseBL(path);
                cls.setMaCaiDat("3cfb1a83-322e-4a27-87a1-5486703c65e3");
                cls.setMaCongTy("5189340d-36a1-4a1e-a927-878ff022679e");

                String manhan = cls.getNgayThangNam().replaceAll("/", "")+cls.getSoBatDau().substring(4)+ cls.getMASOTHUENBAN();
                cls.setMANHANHOADON(manhan);
                cls.setInvoiceId(manhan);

                System.out.println(cls.writeSql());
            }
        }
    }
}