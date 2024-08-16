package vn.conyeu.book.api;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import vn.conyeu.commons.beans.ObjectMap;
import vn.conyeu.book.domain.BookDto;
import vn.conyeu.book.domain.ChapterDto;
import vn.conyeu.book.service.BookDtoService;
import vn.conyeu.book.service.ChapterDtoService;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/ebook")
public class Test {
    final TruyenFull truyenFull;
    final ChapterDtoService chapterService;
    final BookDtoService bookService;
    final Path ROOT = Paths.get("D:\\book_all");
    final Map<String, BookDto> URL_BOOK = new HashMap<>();

    static Document chapterDef;

    public Test(TruyenFull truyenFull, ChapterDtoService chapterService, BookDtoService bookService) {
        this.truyenFull = truyenFull;
        this.chapterService = chapterService;
        this.bookService = bookService;
    }

    @GetMapping("find-book")
    public Object demo(@RequestParam Map<String, Object> params) {
        ObjectMap map = ObjectMap.fromMap(params);

        String bookUrl = map.getString("url");
        Long fromPage = map.getLong("fromPage", null);

        if(URL_BOOK.containsKey(bookUrl)) {
            return URL_BOOK.get(bookUrl);
        }

        Optional<BookDto> optional = truyenFull.findBook(bookUrl);
        if(optional.isEmpty()) return "%s::%s".formatted("e404", bookUrl);

        log.info("FindBook -> {}", bookUrl);

        BookDto book = optional.get();

        // save info
        bookService.save(book);

        // find chapter -> save
        Long totalPage = book.getTotalPage();
        for(long page=2;page<=totalPage;page++) {
            log.info("get-chapter-link -> {}::{}", page, bookUrl);
            List<ChapterDto> chapters = truyenFull.getChapterLinks(bookUrl, page);
            chapters.forEach(book::addChapter);

            chapterService.saveAll(chapters);

            // save page
            book.setCurrentPage(page);
            bookService.save(book);
        }


        return bookUrl;
    }

    @GetMapping("chapter/get-html-from-db")
    public Object findChapter(@RequestParam Map<String, Object> params, Pageable pageable) {
        ObjectMap map = ObjectMap.fromMap(params);
        Long bookId = map.getLong("bookId");

        Page<ChapterDto> chapterPage = chapterService.findExtractHtml(bookId, pageable);
        for(ChapterDto dto : chapterPage) {
            String chapterUrl = dto.getFullUrl();
            Optional<ChapterDto> optional = truyenFull.findChapter(chapterUrl);
            if(optional.isEmpty()) {
                dto.setLogError("empty");
            }

            else {
                ChapterDto chapter = optional.get();
                if(chapter.getLogError() == null) {
                    dto.setTitle(chapter.getTitle());
                    dto.setContentHtml(chapter.getContentHtml());
                }
            }

            dto.setGetHtml(true);
            chapterService.save(dto);
        }

        return chapterPage.getSize();
    }

    @GetMapping("chapter/url/get-html-from-url")
    public Object findChapterHtmlFromUrl(@RequestParam String url) {
        return truyenFull.findChapter(url);
    }



    @GetMapping("save-html")
    public Object saveHtml(@RequestParam Map<String, Object> params, Pageable pageable) {
        ObjectMap map = ObjectMap.fromMap(params);

        Long bookId = map.getLong("bookId");
        String bookSeo = map.getString("bookSeo");

        // create book folder
        Path bookDir = resolvePath(bookSeo);

        Document html = getChapterDef();

        List<ChapterDto> chapterPage = chapterService.findSaveHtml(bookId, pageable).getContent();

        for(ChapterDto chapter:chapterPage) {
            Document doc = html.clone();
            doc.selectFirst("#chapter-title").text(chapter.getTitle());
            doc.selectFirst("#chapter-html").html(chapter.getContentHtml());

            String chapterNextUrl = truyenFull.getChapterUrlNext(chapter.getFullUrl())+".html";
            doc.selectFirst("#chapter-link a").attr("href", "./"+chapterNextUrl);

            String seoAlias = chapter.getSeoUrl()+".html";
            writeString(bookDir.resolve(seoAlias), doc);

            // update chapter
            chapter.setSaveHtml(true);
            chapterService.save(chapter);
        }

        return chapterPage.size();


    }

    private static Document getChapterDef() {
        if(chapterDef == null) {
//            try {
               // File file = ResourceUtils.getFile("classpath:/chapter_def.html");
                chapterDef = Jsoup.parse("<!doctype html>\n" +
                        "<html lang=\"en\">\n" +
                        "<head>\n" +
                        "    <meta charset=\"utf-8\">\n" +
                        "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1\">\n" +
                        "    <link rel=\"stylesheet\" href=\"./SourceCodePro.ttf\">\n" +
                        "    <link rel=\"stylesheet\" href=\"./bootstrap.css\" >\n" +
                        "    <link rel=\"stylesheet\" href=\"./style.css\" >\n" +
                        "    <title></title>\n" +
                        "</head>\n" +
                        "<body class=\"container fs-1 m-3\">\n" +
                        "    <h1 id=\"chapter-title\"></h1>\n" +
                        "    <div id=\"chapter-html\"></div>\n" +
                        "    <div id=\"chapter-link\">\n" +
                        "        <a href=\"\">Xem tiếp</a>\n" +
                        "    </div>\n" +
                        "</body>\n" +
                        "</html>");
                //chapterDef = Jsoup.parse(file);
                return chapterDef;
//            }
         //   catch (IOException e) {
           //     throw new RuntimeException(e);
           // }

        }
//
        return chapterDef.clone();
    }

    private Path resolvePath(String path)  {
        try {
            Path newPath = ROOT.resolve(path);
            if (Files.notExists(newPath)) {
                Files.createDirectories(newPath);
            }
            return newPath;
        }catch (IOException exp) {
            throw new RuntimeException(exp);
        }
    }

    private void writeString(Path path, Document html) {
        if(Files.isDirectory(path)) {
            throw new RuntimeException("The path " + path + "is dir");
        }

        try {
            if (Files.exists(path)) {
                Files.delete(path);
            }

            Files.writeString(path, html.html(), StandardCharsets.UTF_8, StandardOpenOption.CREATE_NEW);
        }catch (IOException exp) {
            throw new RuntimeException(exp);
        }

    }

}