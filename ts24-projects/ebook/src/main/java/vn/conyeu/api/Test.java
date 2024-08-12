package vn.conyeu.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.conyeu.domain.ChapterDto;
import vn.conyeu.service.ChapterDtoService;

import java.nio.file.Path;
import java.util.List;

@RestController
@RequestMapping("/ebook/test")
public class Test {
    final  TruyenFull truyenFull;
    final ChapterDtoService chapterService;

    public Test(TruyenFull truyenFull, ChapterDtoService chapterService) {
        this.truyenFull = truyenFull;
        this.chapterService = chapterService;
    }

    @GetMapping("demo")
    public Object demo() {
        truyenFull.findBook(null);
        return "xong";
    }

    @GetMapping("html")
    public Object saveHtml() {
        List<ChapterDto> chapters = chapterService.findAll();
    }

    private void saveToHtml(BookClient bookClient, Path bookDir, ChapterDto dto, String chapterNextLink) {
        //log.warn("saveToHtml: {}", dto.getChapterUrl());
        String errorLog = null;
        Integer state = 1;

        try {
            //String href = BookUtil.buildChapterLocalLink(dto.getSortIndex() + 1);
            String bodyHtml = fixBodyHtml(bookClient, "", dto.getContentHtml());
            String bo

            Document html = BookUtil.getChapterDef().clone();
            html.selectFirst("#chapter-title").text(dto.getTitle());
            html.selectFirst("#chapter-html").html(bodyHtml);

            if(chapterNextLink != null) {
                String chapterNext = chapterNextLink + ".html";
                html.selectFirst("#chapter-link > a").attr("href", chapterNext);
            }

            String fName = BookUtil.buildChapterLocalLink(dto.getSortIndex());
            Path chapterPath = bookDir.resolve(dto.getAlias()+".html");

            Objects.appendLines(chapterPath, html.html());

        }catch (Exception exp) {
            errorLog = exp.getClass().getName()+"::"+exp.getMessage();
            state = -1;
        }

        // update export state ==> save_db
        dto.setExportState(state);
        dto.setErrorLog(errorLog);
        chapterDtoSrv.save(dto);
    }
}