package vn.conyeu.google.drives;

import com.google.api.client.http.AbstractInputStreamContent;
import com.google.api.client.http.ByteArrayContent;
import com.google.api.client.http.FileContent;
import com.google.api.client.http.InputStreamContent;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;
import vn.conyeu.commons.utils.Asserts;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

public class GFolder extends AbstractGFile {
    protected boolean hasRoot;

    GFolder(Drive service, File model) {
        super(service, model);
    }

    public void delete() throws IOException {
        files.delete(getId()).execute();
    }

    /**
     * Gets a collection of all folders that are children of the current folder and have the given name.
     * @param name The name of the folders to find.
     * @param limit the maximum number of files to return per page
     * */
    public GPage<GFolder> getFolderByName(String name, int limit) throws IOException {
        String query = "mimeType='%s' and name='%s'".formatted(GMime.FOLDER.mime, name);
        return search(new SearchBuilder().query(query).pageSize(limit)).mapTo(model -> (GFolder) model);
    }

    public GFolder getFolderById(String folderId) throws IOException {
        return newFolder(files.get(folderId).setFields("*").execute());
    }

    /**
     * Gets a collection of all folders that are children of the current folder.
     * @param limit the maximum number of files to return per page
     * */
    public GPage<GFolder> getFolders(int limit) throws IOException {
        String query = "mimeType='%s'".formatted(GMime.FOLDER.mime);
        return search(new SearchBuilder().query(query).pageSize(limit).orderBy("name"))
                .mapTo(file -> (GFolder) file);
    }

    /**
     * Gets a collection of all files that are children of the current folder.
     * @param limit the maximum number of files to return per page
     * */
    public GPage<GFile> getFiles(int limit) throws IOException {
        String query = "mimeType != '%s'".formatted(GMime.FOLDER.mime);
        return search(new SearchBuilder().query(query).pageSize(limit).orderBy("name"))
                .mapTo(file -> (GFile) file);
    }

    /**
     * Gets a collection of all files that are children of the current folder and have the given MIME type.
     * @param mimeType The MIME Type of the files to find.
     * */
    public GPage<GFile> getFilesByType(String mimeType) throws IOException {
        String query = "mimeType='%s' and mimeType != '%s'".formatted(mimeType, GMime.FOLDER.mime);
        return search(new SearchBuilder().query(query).pageSize(1000).orderBy("name"))
                .mapTo(file -> (GFile) file);
    }

    /**
     * Gets a collection of all files that are children of the current folder and have the given name.
     * @param name The name of the files to find.
     * */
    public GPage<GFile> getFilesByName(String name) throws IOException {
        String query = "name = '%s' and mimeType != '%s'".formatted(name, GMime.FOLDER.mime);
        return search(new SearchBuilder().query(query).pageSize(1000).orderBy("name"))
                .mapTo(file -> (GFile) file);
    }

    public GPage<AbstractGFile> search(SearchBuilder builder) throws IOException {
        Asserts.notBlank(builder.query, "The query not blank");
        FileList list = files.list().setFields(builder.fields)
                .setQ(builder.buildQuery())
                .setPageSize(builder.pageSize).setPageToken(builder.pageToken)
                .setOrderBy(builder.orderBy).execute();

        String pageToken = list.getNextPageToken();
        List<AbstractGFile> models = convertModels(list.getFiles());
        return new GPage<>(models, list.getNextPageToken());
    }

    public GFolder addFolder(String name) {
        return addFolder(name, null);
    }

    public GFolder addFolder(String name, Consumer<File> fileConsumer) {
        File file = new File();
        if (fileConsumer != null) fileConsumer.accept(file);

        file.setName(name).setMimeType(GMime.FOLDER.mime);
        file.setParents(List.of(getId()));

        try {
            File model = files.create(file).execute();
            return newFolder(model);
        } catch (IOException e) {
            throw new DriveException(e);
        }
    }

    public void deleteFolderByName(String name) throws IOException {
        GPage<GFolder> gPage = getFolderByName(name, 1);
        if(gPage.isEmpty()) throw new DriveException("The folder '%s' not exists.", name);
        deleteFolderById(gPage.get(0).getId());
    }

    public void deleteFolderById(String folderId) throws IOException {
        files.delete(folderId).execute();
    }

    /**
     * Creates a file in the current folder with the given name, contents, and MIME type.
     *
     * @param name     The name of the new file.
     * @param mimeType The MIME type of the new file.
     * @param data     The content for the new file.
     */
    public GFile createFile(String name, String mimeType, String data) {
        return createFile(name, mimeType, data.getBytes());
    }

    /**
     * Creates a file in the current folder with the given name, contents, and MIME type.
     *
     * @param name     The name of the new file.
     * @param mimeType The MIME type of the new file.
     * @param data     The content for the new file.
     */
    public GFile createFile(String name, String mimeType, byte[] data) {
        ByteArrayContent content = new ByteArrayContent(mimeType, data);
        return createFile(name, mimeType, content);
    }

    /**
     * Creates a file in the current folder with the given name, contents, and MIME type.
     *
     * @param name     The name of the new file.
     * @param mimeType The MIME type of the new file.
     * @param file     The content for the new file.
     */
    public GFile createFile(String name, String mimeType, java.io.File file) {
        FileContent content = new FileContent(mimeType, file);
        return createFile(name, mimeType, content);
    }

    /**
     * Creates a file in the current folder with the given name, contents, and MIME type.
     *
     * @param name     The name of the new file.
     * @param mimeType The MIME type of the new file.
     * @param file     The content for the new file.
     */
    public GFile createFile(String name, String mimeType, Path file) {
        FileContent content = new FileContent(mimeType, file.toFile());
        return createFile(name, mimeType, content);
    }

    /**
     * Creates a file in the current folder with the given name, contents, and MIME type.
     *
     * @param name     The name of the new file.
     * @param mimeType The MIME type of the new file.
     * @param data     The content for the new file.
     */
    public GFile createFile(String name, String mimeType, InputStream data) {
        InputStreamContent content = new InputStreamContent(mimeType, data);
        return createFile(name, mimeType, content);
    }


    @Override
    protected void validateModel(File model) {
        if (!GHelper.isFolder(model)) {
            throw new DriveException("The file %s not folder", model.getName());
        }
    }

    private GFile createFile(String name, String mimeType, AbstractInputStreamContent content) {
        if (mimeType == null) mimeType = content.getType();
        File meta = new File().setName(name).setMimeType(mimeType);
        try {
            File model = files.create(meta, content).execute();
        } catch (IOException exp) {
            throw new DriveException(exp);
        }
        return new GFile(drive, model);
    }

    private GFolder newFolder(File model) {
        return new GFolder(drive, model);
    }

    private GFile newFile(File model) {
        return new GFile(drive, model);
    }

    private List<AbstractGFile> convertModels(List<File> models) {
        return models.stream().map(model -> GHelper.isFolder(model)
                ? newFolder(model) : newFile(model)).toList();
    }

    public static class GPage<E> implements Iterable<E> {
        final List<E> list;
        final String pageToken;

        public GPage(List<E> list, String pageToken) {
            this.list = list;
            this.pageToken = pageToken;
        }

        public String getPageToken() {
            return pageToken;
        }

        @Override
        public Iterator<E> iterator() {
            return list.iterator();
        }

        public <C> GPage<C> mapTo(Function<E, C> mapper) {
            List<C> listC = list.stream().map(mapper).toList();
            return new GPage<>(listC, pageToken);
        }

        public boolean isEmpty() {
            return list.isEmpty();
        }

        public E get(int index) {
            return list.get(index);
        }

        public int getSize() {
            return list.size();
        }
    }

    public class SearchBuilder {
        public String fields;
        public Integer pageSize;
        private String query;
        private String pageToken;
        private String orderBy;

        public String buildQuery() {
            return buildQuery(false);
        }

        public String buildQuery(boolean includeTrashed) {
            if(!query.contains("in parents")) query += " and '%s' in parents".formatted(getId());
            if(!includeTrashed) query += " and trashed=false";
            return query;
        }

        /**
         * Set the fields
         *
         * @param fields the value
         */
        public SearchBuilder fields(String fields) {
            this.fields = fields;
            return this;
        }

        /**
         * Set the pageSize
         *
         * @param pageSize the value
         */
        public SearchBuilder pageSize(Integer pageSize) {
            this.pageSize = pageSize;
            return this;
        }

        /**
         * Set the query
         *
         * @param query the value
         */
        public SearchBuilder query(String query) {
            this.query = query + " and trashed=false";
            return this;
        }

        /**
         * Set the pageToken
         *
         * @param pageToken the value
         */
        public SearchBuilder pageToken(String pageToken) {
            this.pageToken = pageToken;
            return this;
        }

        /**
         * Set the orderBy
         *
         * @param orderBy the value
         */
        public SearchBuilder orderBy(String orderBy) {
            this.orderBy = orderBy;
            return this;
        }
    }
}