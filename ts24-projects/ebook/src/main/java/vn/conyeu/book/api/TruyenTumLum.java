package vn.conyeu.book.api;

import lombok.extern.slf4j.Slf4j;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;
import vn.conyeu.book.domain.ChapterDto;
import vn.conyeu.commons.beans.ObjectMap;

import java.util.Optional;

@Slf4j
public class TruyenTumLum extends BookClient {

    public String getBaseRoot() {
        return "https://truyentumlum.com";
    }

    public Optional<ChapterDto> findChapter(String link) {
        Document html = getHtml(link);

        Element chapterEl = first(html, "#chapter-detail");
        if(chapterEl == null) return Optional.empty();

        Element chapterC = first(chapterEl, "#chapter-content");
        String contentC = removeBlank("chapter", chapterC);

        extractChapterScript(html);

        ChapterDto dto = new ChapterDto();
        dto.setFullUrl(link);
        dto.setTitle(get_text(chapterEl, ".chapter"));
        dto.setContentHtml(contentC);



        return Optional.of(dto);
    }

    protected String removeBlank(String page, Element element) {
        for(int index=0;index<element.childNodeSize();index++) {
            Node childNode = element.childNode(index);
            if(childNode instanceof Element el) {
                if(el.text().trim().isEmpty()) childNode.remove();
            }
        }

        return element.html();
    }

    protected ObjectMap extractChapterScript(Document html) {
        Elements els = select(html, "script:contains(ajaxNovelCommentSendUrl)", false);
        for (Element el:els) log.warn("{}", el.data());
        return null;
    }
}