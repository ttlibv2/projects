package vn.conyeu.ts.ticket.service;

import lombok.SneakyThrows;
import org.apache.commons.io.IOUtils;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.Assert;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;

import vn.conyeu.commons.beans.ObjectMap;
import vn.conyeu.ts.odcore.domain.ClsApiCfg;
import vn.conyeu.ts.ticket.domain.ClsFile;

import java.io.InputStream;
import java.util.Base64;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;

public class OdFile extends OdTicketClient<ClsFile> {

    public OdFile(ClsApiCfg apiConfig) {
        super(apiConfig);
    }

    public String getModel() {
        throw new UnsupportedOperationException();
    }

    public String getBasePath() {
        throw new UnsupportedOperationException();
    }

    /**
     * Delete attachment
     *
     * @param fileID Long
     */
    public boolean delete_file(Long fileID) {
        String url = "/mail/attachment/delete";
        sendPost(ObjectMap.setNew("attachment_id", fileID), url);
        return true;
    }

    /**
     * Add file
     * @param thread_model String
     * @param thread_id Long
     * @param fileName String
     * @param data byte[]
     * @return ClsFile
     * */
    @SneakyThrows
    public ClsFile add_file(String thread_model, Object thread_id, String fileName, byte[] data) {
        MultiValueMap<String, Object> bodyBuilder = new LinkedMultiValueMap<>();
        bodyBuilder.add("ufile", new ByteArrayResource(data) {
            @Override
            public long contentLength() {
                return data.length;
            }

            @Override
            public String getFilename() {
                return fileName;
            }

        });
        bodyBuilder.add("csrf_token", cfg.getCsrfToken());
        bodyBuilder.add("is_pending", true);
        bodyBuilder.add("thread_id", thread_id);
        bodyBuilder.add("thread_model", thread_model);

        return applyDefaultBuilder().build().post()
                .uri("/mail/attachment/upload")
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .header(HttpHeaders.COOKIE, cfg.getCookieValue())
                .body(BodyInserters.fromMultipartData(bodyBuilder))
                .retrieve().bodyToMono(ClsFile.class)
                .blockOptional().orElseThrow();
    }

    public List<ClsFile> add_files(String thread_model, Long thread_id, ObjectMap dataObj) {
        Assert.hasLength(thread_model, "@thread_model");
        Assert.notNull(thread_id, "@thread_id");

        if(dataObj == null || dataObj.isEmpty()) {
            return new LinkedList<>();
        }

        List<ClsFile> allFile = new LinkedList<>();
        for(String fileName: dataObj.keySet()) {
            byte[] data = fixDataFile(dataObj.get(fileName));
            Assert.notNull(data, "data of file "+fileName+" is null.");
            allFile.add(add_file(thread_model, thread_id, fileName, data));
        }

        return allFile;
    }

    @SneakyThrows
    private byte[] fixDataFile(Object data) {
        if(data instanceof byte[] bytes) return bytes;
        if(data instanceof String str) return Base64.getDecoder().decode(str);
        if(data instanceof InputStream is) return IOUtils.toByteArray(is);
        else throw new IllegalArgumentException("Data not support "+data.getClass());
    }


    @Override
    protected Class<ClsFile> getDomainCls() {
        return ClsFile.class;
    }

    @Override
    protected Function<ObjectMap, ClsFile> mapToObject() {
        return object -> object.asObject(ClsFile.class);
    }
}