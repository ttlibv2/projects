package vn.conyeu.ts.ticket.service;

import vn.conyeu.commons.beans.ObjectMap;
import vn.conyeu.commons.utils.Asserts;
import vn.conyeu.commons.utils.Objects;
import vn.conyeu.ts.odcore.domain.ClsApiCfg;
import vn.conyeu.ts.ticket.domain.ClsFile;
import vn.conyeu.ts.ticket.domain.ClsFileMap;
import vn.conyeu.ts.ticket.domain.ClsMessage;

import java.util.List;
import java.util.function.Function;

public class OdMessage extends OdTicketClient<ClsMessage> {
    private final OdFile odFile;

    public OdMessage(ClsApiCfg apiConfig, OdFile odFile) {
        super(apiConfig);
        this.odFile = Asserts.notNull(odFile, "OdFile");
    }

    public String getModel() {
        throw new UnsupportedOperationException();
    }

    public String getBasePath() {
        throw new UnsupportedOperationException();
    }

    @Override
    protected String getApiUrl() {
       return getApiUrlNoWeb();
    }

    /**
     * Create new note && add file
     * @param clsNote ClsMessage
     * */
    public ClsMessage create(ClsMessage clsNote, ObjectMap fileObj) {
        clsNote.setAttachObj(fileObj);
        return create(clsNote);
    }

    /**
     * Create new note
     * @param clsNote ClsMessage
     * */
    public ClsMessage create(ClsMessage clsNote) {
        boolean hasNotAttach = !clsNote.attachObj().isEmpty();

        if(hasNotAttach) {
            ObjectMap objectMap = clsNote.attachObj();
            List<ClsFile> allFile = odFile.add_files(clsNote.getModel(), clsNote.getRes_id(), objectMap);
           // hasNotAttach = !allFile.isEmpty();

            // update attachment_id
            clsNote.setAttachment_ids(allFile.stream().map(ClsFile::getId).toList());

            // update file result to object
            ClsFileMap fileMap = new ClsFileMap();
            for(ClsFile clsFile:allFile) fileMap.put(clsFile.getFilename(), clsFile);
            clsNote.setFileMap(fileMap);
        }

        clsNote = clsNote.validateSend();

        ObjectMap argBody = ObjectMap
                .setNew("post_data", clsNote.cloneCreate())
                .set("thread_id", clsNote.getRes_id())
                .set("thread_model", clsNote.getModel());

        ObjectMap mapData = sendPost(argBody, "/mail/message/post").getMap("result");
        return clsNote.updateFromMap(mapData);
    }

    /**
     * Delete note
     * @param noteID Long
     */
    public boolean delete(Long noteID) {
        ClsMessage clsNote = new ClsMessage();
        clsNote.setBody("");
        clsNote.setAttachment_ids(new Object[0]);
        update(noteID, clsNote);
        return true;
    }

    /**
     * Update note
     * @param noteID Long
     * @param cls   ClsMessage
     */
    public ClsMessage update(Long noteID, ClsMessage cls) {
        Asserts.notNull(noteID, "@noteID");

        if(!cls.attachObj().isEmpty()) {
            List<ClsFile> allFile = odFile.add_files(cls.getModel(), cls.getRes_id(), cls.attachObj());
            List<Long> fileID = allFile.stream().map(ClsFile::getId).toList();
            cls.setAttachment_ids(fileID);
        }

        String url = "/mail/message/update_content";
        return sendPost( ObjectMap.create()
                .set("body", Objects.firstNotNull(cls.getBody(), ""))
                .set("attachment_ids",  Objects.firstNotNull(cls.getAttachment_ids(), new Object[1]))
                .set("message_id", noteID), url)
                .getMap("result").updateTo(cls);
    }


    @Override
    protected Class<ClsMessage> getDomainCls() {
        return ClsMessage.class;
    }

    @Override
    protected Function<ObjectMap, ClsMessage> mapToObject() {
        return ClsMessage::from;
    }
}