package vn.conyeu.book.extapi;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LocalTest {

    public static void main(String[] args) {
        TruyenFull full = new TruyenFull();
        Object object = full.readBook("https://truyenfull.bio/so-13-pho-mink/").orElseThrow();
        log.warn("{}", object);

    }
}