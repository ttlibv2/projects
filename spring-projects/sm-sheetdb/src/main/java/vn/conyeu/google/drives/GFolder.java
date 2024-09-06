package vn.conyeu.google.drives;

import com.google.api.client.http.AbstractInputStreamContent;
import com.google.api.client.http.ByteArrayContent;
import com.google.api.client.http.FileContent;
import com.google.api.client.http.InputStreamContent;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;
import vn.conyeu.google.drives.builder.AbstractGFile;
import vn.conyeu.google.drives.builder.FileBuilder;
import vn.conyeu.google.sheet.builder.ConsumerReturn;

import java.io.InputStream;
import java.nio.file.Path;
import java.util.Iterator;
import java.util.List;
import java.util.function.Function;

public class GFolder extends AbstractGFile {
    protected final boolean hasRoot;

    public GFolder(DriveService service, File model) {
        super(service, model);
        this.hasRoot = "My Drive".equalsIgnoreCase(model.getName());
    }

    public void delete()  {
        service.deleteById(getId());
    }

    /**
     * Gets a collection of all folders that are children of the current folder and have the given name.
     * @param name The name of the folders to find.
     * @param limit the maximum number of files to return per page
     * */
    public GPage<GFolder> getFolderByName(String name, int limit)  {
        String query = "mimeType='%s' and name='%s'".formatted(GMime.FOLDER.mime, name);
        return search(builder(false).pageSize(limit)
                .query(q -> q.all(q.folder(), q.name(name))))
                .mapTo(model -> (GFolder) model);
    }

    public GFolder getFolderById(String folderId)  {
        return newFolder(service.openById(folderId, "*"));
    }

    /**
     * Gets a collection of all folders that are children of the current folder.
     * @param limit the maximum number of files to return per page
     * */
    public GPage<GFolder> getFolders(int limit)  {
        return search(builder(false)
                .pageSize(limit).query(DriveQuery::folder))
                .mapTo(file -> (GFolder) file);
    }

    /**
     * Gets a collection of all files that are children of the current folder.
     * @param limit the maximum number of files to return per page
     * */
    public GPage<GFile> getFiles(int limit)  {
        return search(builder(false).query(DriveQuery::notFolder))
                .mapTo(file -> (GFile) file);
    }

    /**
     * Gets a collection of all files that are children of the current folder and have the given MIME type.
     * @param mimeType The MIME Type of the files to find.
     * */
    public GPage<GFile> getFilesByType(String mimeType)  {
        return search(builder(false).query(q -> q.isMime(mimeType)))
                .mapTo(file -> (GFile) file);
    }

    /**
     * Gets a collection of all files that are children of the current folder and have the given name.
     * @param name The name of the files to find.
     * */
    public GPage<GFile> getFilesByName(String name)  {
        return search(builder(false)
                .query(q -> q.all(q.notFolder(), q.name.eq(name))))
                .mapTo(file -> (GFile) file);
    }

    public GPage<AbstractGFile> search(SearchBuilder builder)  {
        FileList list = service.search(builder);
        String pageToken = list.getNextPageToken();
        List<AbstractGFile> models = convertModels(list.getFiles());
        return new GPage<>(models, list.getNextPageToken());
    }

    public GFolder addFolder(String name) {
        return addFolder(b -> b.name(name));
    }

    public GFolder addFolder(ConsumerReturn<FileBuilder> consumer) {
        File model = service.createFolder(b -> consumer.accept(b).parents(getId()));
        return newFolder(model);
    }

    public void deleteFolderByName(String name)  {
        GPage<GFolder> gPage = getFolderByName(name, 1);
        if(gPage.isEmpty()) throw new DriveException("The folder '%s' not exists.", name);
        deleteFolderById(gPage.get(0).getId());
    }

    public void deleteFolderById(String folderId)  {
        service.deleteById(folderId);
    }

    /**
     * Creates a file in the current folder with the given name, contents, and MIME type.
     *
     * @param name     The name of the new file.
     * @param mimeType The MIME type of the new file.
     * @param data     The content for the new file.
     */
    public GFile createFile(String name, GMime mimeType, String data) {
        return createFile(name, mimeType, data.getBytes());
    }

    /**
     * Creates a file in the current folder with the given name, contents, and MIME type.
     *
     * @param name     The name of the new file.
     * @param mimeType The MIME type of the new file.
     * @param data     The content for the new file.
     */
    public GFile createFile(String name, GMime mimeType, byte[] data) {
        ByteArrayContent content = new ByteArrayContent(mimeType.mime, data);
        return createFile(name, mimeType, content);
    }

    /**
     * Creates a file in the current folder with the given name, contents, and MIME type.
     *
     * @param name     The name of the new file.
     * @param mimeType The MIME type of the new file.
     * @param file     The content for the new file.
     */
    public GFile createFile(String name, GMime mimeType, java.io.File file) {
        FileContent content = new FileContent(mimeType.mime, file);
        return createFile(name, mimeType, content);
    }

    /**
     * Creates a file in the current folder with the given name, contents, and MIME type.
     *
     * @param name     The name of the new file.
     * @param mimeType The MIME type of the new file.
     * @param file     The content for the new file.
     */
    public GFile createFile(String name, GMime mimeType, Path file) {
        FileContent content = new FileContent(mimeType.mime, file.toFile());
        return createFile(name, mimeType, content);
    }

    /**
     * Creates a file in the current folder with the given name, contents, and MIME type.
     *
     * @param name     The name of the new file.
     * @param mimeType The MIME type of the new file.
     * @param data     The content for the new file.
     */
    public GFile createFile(String name, GMime mimeType, InputStream data) {
        InputStreamContent content = new InputStreamContent(mimeType.mime, data);
        return createFile(name, mimeType, content);
    }


    @Override
    protected void validateModel(File model) {
        if (!GHelper.isFolder(model)) {
            throw new DriveException("The file %s not folder", model.getName());
        }
    }

    private GFile createFile(final String name, final GMime mimeType, AbstractInputStreamContent content) {
        String newMime = mimeType == null ? content.getType() : mimeType.mime;
        return newFile(service.createFile(b -> b.name(name).mimeType(newMime).content(content)));
    }

    private GFolder newFolder(File model) {
        return new GFolder(service, model);
    }

    private GFile newFile(File model) {
        return new GFile(service, model);
    }

    private List<AbstractGFile> convertModels(List<File> models) {
        return models.stream().map(model -> GHelper.isFolder(model)
                ? newFolder(model) : newFile(model)).toList();
    }

    SearchBuilder builder(Boolean trashed) {
        SearchBuilder sb = new SearchBuilder();
        if(trashed != null) sb.query(q -> q.trashed.eq(trashed));
        return sb.pageSize(1000).orderBy("name")
                .query(q -> q.parents(getId()));

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

}