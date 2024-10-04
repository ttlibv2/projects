package vn.conyeu.book.api;

import lombok.extern.slf4j.Slf4j;
import vn.conyeu.book.domain.ChapterDto;

@Slf4j
public class MainTest {
    static String chapterLink = "https://truyentumlum.com/thi-thien-dao/chuong-2725-quy-an/";

    public static void main(String[] args) {
        TruyenTumLum tt = new TruyenTumLum();
        ChapterDto chapter = tt.findChapter(chapterLink).orElse(null);
        log.warn("{}", chapter);
    }
}