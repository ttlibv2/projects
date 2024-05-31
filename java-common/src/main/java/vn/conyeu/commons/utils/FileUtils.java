package vn.conyeu.commons.utils;

import org.apache.commons.io.LineIterator;
import org.apache.commons.io.filefilter.IOFileFilter;

import java.io.*;
import java.math.BigInteger;
import java.net.URL;
import java.nio.charset.Charset;

import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.attribute.FileTime;
import java.time.*;
import java.time.chrono.ChronoLocalDate;
import java.time.chrono.ChronoLocalDateTime;
import java.time.chrono.ChronoZonedDateTime;
import java.util.*;
import java.util.function.BiPredicate;
import java.util.stream.Stream;
import java.util.zip.Checksum;

public final class FileUtils {

    public static String byteCountToDisplaySize(BigInteger size) {
        return org.apache.commons.io.FileUtils.byteCountToDisplaySize(size);
    }

    public static String byteCountToDisplaySize(long size) {
        return org.apache.commons.io.FileUtils.byteCountToDisplaySize(size);
    }

    public static String byteCountToDisplaySize(Number size) {
        return org.apache.commons.io.FileUtils.byteCountToDisplaySize(size);
    }

    public static Checksum checksum(File file, Checksum checksum) throws IOException {
        return org.apache.commons.io.FileUtils.checksum(file, checksum);
    }

    public static long checksumCRC32(File file) throws IOException {
        return org.apache.commons.io.FileUtils.checksumCRC32(file);
    }

    public static void cleanDirectory(File directory) throws IOException {
        org.apache.commons.io.FileUtils.cleanDirectory(directory);
    }

    public static boolean contentEquals(File file1, File file2) throws IOException {
        return org.apache.commons.io.FileUtils.contentEquals(file1, file2);
    }

    public static boolean contentEqualsIgnoreEOL(File file1, File file2, String charsetName) throws IOException {
        return org.apache.commons.io.FileUtils.contentEqualsIgnoreEOL(file1, file2, charsetName);
    }

    public static File[] convertFileCollectionToFileArray(Collection<File> files) {
        return org.apache.commons.io.FileUtils.convertFileCollectionToFileArray(files);
    }

    public static void copyDirectory(File srcDir, File destDir) throws IOException {
        org.apache.commons.io.FileUtils.copyDirectory(srcDir, destDir);
    }

    public static void copyDirectory(File srcDir, File destDir, boolean preserveFileDate) throws IOException {
        org.apache.commons.io.FileUtils.copyDirectory(srcDir, destDir, preserveFileDate);
    }

    public static void copyDirectory(File srcDir, File destDir, FileFilter filter) throws IOException {
        org.apache.commons.io.FileUtils.copyDirectory(srcDir, destDir, filter);
    }

    public static void copyDirectory(File srcDir, File destDir, FileFilter filter, boolean preserveFileDate) throws IOException {
        org.apache.commons.io.FileUtils.copyDirectory(srcDir, destDir, filter, preserveFileDate);
    }

    public static void copyDirectory(File srcDir, File destDir, FileFilter fileFilter, boolean preserveFileDate, CopyOption... copyOptions) throws IOException {
        org.apache.commons.io.FileUtils.copyDirectory(srcDir, destDir, fileFilter, preserveFileDate, copyOptions);
    }

    public static void copyDirectoryToDirectory(File sourceDir, File destinationDir) throws IOException {
        org.apache.commons.io.FileUtils.copyDirectoryToDirectory(sourceDir, destinationDir);
    }

    public static void copyFile(File srcFile, File destFile) throws IOException {
        org.apache.commons.io.FileUtils.copyFile(srcFile, destFile);
    }

    public static void copyFile(File srcFile, File destFile, boolean preserveFileDate) throws IOException {
        org.apache.commons.io.FileUtils.copyFile(srcFile, destFile, preserveFileDate);
    }

    public static void copyFile(File srcFile, File destFile, boolean preserveFileDate, CopyOption... copyOptions) throws IOException {
        org.apache.commons.io.FileUtils.copyFile(srcFile, destFile, preserveFileDate, copyOptions);
    }

    public static void copyFile(File srcFile, File destFile, CopyOption... copyOptions) throws IOException {
        org.apache.commons.io.FileUtils.copyFile(srcFile, destFile, copyOptions);
    }

    public static long copyFile(File input, OutputStream output) throws IOException {
        return org.apache.commons.io.FileUtils.copyFile(input, output);
    }

    public static void copyFileToDirectory(File srcFile, File destDir) throws IOException {
        org.apache.commons.io.FileUtils.copyFileToDirectory(srcFile, destDir);
    }

    public static void copyFileToDirectory(File sourceFile, File destinationDir, boolean preserveFileDate) throws IOException {
        org.apache.commons.io.FileUtils.copyFileToDirectory(sourceFile, destinationDir, preserveFileDate);
    }

    public static void copyInputStreamToFile(InputStream source, File destination) throws IOException {
        org.apache.commons.io.FileUtils.copyInputStreamToFile(source, destination);
    }

    public static void copyToDirectory(File sourceFile, File destinationDir) throws IOException {
        org.apache.commons.io.FileUtils.copyToDirectory(sourceFile, destinationDir);
    }

    public static void copyToDirectory(Iterable<File> sourceIterable, File destinationDir) throws IOException {
        org.apache.commons.io.FileUtils.copyToDirectory(sourceIterable, destinationDir);
    }

    public static void copyToFile(InputStream inputStream, File file) throws IOException {
        org.apache.commons.io.FileUtils.copyToFile(inputStream, file);
    }

    public static void copyURLToFile(URL source, File destination) throws IOException {
        org.apache.commons.io.FileUtils.copyURLToFile(source, destination);
    }

    public static void copyURLToFile(URL source, File destination, int connectionTimeoutMillis, int readTimeoutMillis) throws IOException {
        org.apache.commons.io.FileUtils.copyURLToFile(source, destination, connectionTimeoutMillis, readTimeoutMillis);
    }

    public static File createParentDirectories(File file) throws IOException {
        return org.apache.commons.io.FileUtils.createParentDirectories(file);
    }

    public static File current() {
        return org.apache.commons.io.FileUtils.current();
    }

    public static File delete(File file) throws IOException {
        return org.apache.commons.io.FileUtils.delete(file);
    }

    public static void deleteDirectory(File directory) throws IOException {
        org.apache.commons.io.FileUtils.deleteDirectory(directory);
    }

    public static boolean deleteQuietly(File file) {
        return org.apache.commons.io.FileUtils.deleteQuietly(file);
    }

    public static boolean directoryContains(File directory, File child) throws IOException {
        return org.apache.commons.io.FileUtils.directoryContains(directory, child);
    }

    public static void forceDelete(File file) throws IOException {
        org.apache.commons.io.FileUtils.forceDelete(file);
    }

    public static void forceDeleteOnExit(File file) throws IOException {
        org.apache.commons.io.FileUtils.forceDeleteOnExit(file);
    }

    public static void forceMkdir(File directory) throws IOException {
        org.apache.commons.io.FileUtils.forceMkdir(directory);
    }

    public static void forceMkdirParent(File file) throws IOException {
        org.apache.commons.io.FileUtils.forceMkdirParent(file);
    }

    public static File getFile(File directory, String... names) {
        return org.apache.commons.io.FileUtils.getFile(directory, names);
    }

    public static File getFile(String... names) {
        return org.apache.commons.io.FileUtils.getFile(names);
    }

    public static File getTempDirectory() {
        return org.apache.commons.io.FileUtils.getTempDirectory();
    }

    public static String getTempDirectoryPath() {
        return org.apache.commons.io.FileUtils.getTempDirectoryPath();
    }

    public static File getUserDirectory() {
        return org.apache.commons.io.FileUtils.getUserDirectory();
    }

    public static String getUserDirectoryPath() {
        return org.apache.commons.io.FileUtils.getUserDirectoryPath();
    }

    public static boolean isDirectory(File file, LinkOption... options) {
        return org.apache.commons.io.FileUtils.isDirectory(file, options);
    }

    public static boolean isEmptyDirectory(File directory) throws IOException {
        return org.apache.commons.io.FileUtils.isEmptyDirectory(directory);
    }

    public static boolean isFileNewer(File file, ChronoLocalDate chronoLocalDate) {
        return org.apache.commons.io.FileUtils.isFileNewer(file, chronoLocalDate);
    }

    public static boolean isFileNewer(File file, ChronoLocalDate chronoLocalDate, LocalTime localTime) {
        return org.apache.commons.io.FileUtils.isFileNewer(file, chronoLocalDate, localTime);
    }

    public static boolean isFileNewer(File file, ChronoLocalDate chronoLocalDate, OffsetTime offsetTime) {
        return org.apache.commons.io.FileUtils.isFileNewer(file, chronoLocalDate, offsetTime);
    }

    public static boolean isFileNewer(File file, ChronoLocalDateTime<?> chronoLocalDateTime) {
        return org.apache.commons.io.FileUtils.isFileNewer(file, chronoLocalDateTime);
    }

    public static boolean isFileNewer(File file, ChronoLocalDateTime<?> chronoLocalDateTime, ZoneId zoneId) {
        return org.apache.commons.io.FileUtils.isFileNewer(file, chronoLocalDateTime, zoneId);
    }

    public static boolean isFileNewer(File file, ChronoZonedDateTime<?> chronoZonedDateTime) {
        return org.apache.commons.io.FileUtils.isFileNewer(file, chronoZonedDateTime);
    }

    public static boolean isFileNewer(File file, Date date) {
        return org.apache.commons.io.FileUtils.isFileNewer(file, date);
    }

    public static boolean isFileNewer(File file, File reference) {
        return org.apache.commons.io.FileUtils.isFileNewer(file, reference);
    }

    public static boolean isFileNewer(File file, FileTime fileTime) throws IOException {
        return org.apache.commons.io.FileUtils.isFileNewer(file, fileTime);
    }

    public static boolean isFileNewer(File file, Instant instant) {
        return org.apache.commons.io.FileUtils.isFileNewer(file, instant);
    }

    public static boolean isFileNewer(File file, long timeMillis) {
        return org.apache.commons.io.FileUtils.isFileNewer(file, timeMillis);
    }

    public static boolean isFileNewer(File file, OffsetDateTime offsetDateTime) {
        return org.apache.commons.io.FileUtils.isFileNewer(file, offsetDateTime);
    }

    public static boolean isFileOlder(File file, ChronoLocalDate chronoLocalDate) {
        return org.apache.commons.io.FileUtils.isFileOlder(file, chronoLocalDate);
    }

    public static boolean isFileOlder(File file, ChronoLocalDate chronoLocalDate, LocalTime localTime) {
        return org.apache.commons.io.FileUtils.isFileOlder(file, chronoLocalDate, localTime);
    }

    public static boolean isFileOlder(File file, ChronoLocalDate chronoLocalDate, OffsetTime offsetTime) {
        return org.apache.commons.io.FileUtils.isFileOlder(file, chronoLocalDate, offsetTime);
    }

    public static boolean isFileOlder(File file, ChronoLocalDateTime<?> chronoLocalDateTime) {
        return org.apache.commons.io.FileUtils.isFileOlder(file, chronoLocalDateTime);
    }

    public static boolean isFileOlder(File file, ChronoLocalDateTime<?> chronoLocalDateTime, ZoneId zoneId) {
        return org.apache.commons.io.FileUtils.isFileOlder(file, chronoLocalDateTime, zoneId);
    }

    public static boolean isFileOlder(File file, ChronoZonedDateTime<?> chronoZonedDateTime) {
        return org.apache.commons.io.FileUtils.isFileOlder(file, chronoZonedDateTime);
    }

    public static boolean isFileOlder(File file, Date date) {
        return org.apache.commons.io.FileUtils.isFileOlder(file, date);
    }

    public static boolean isFileOlder(File file, File reference) {
        return org.apache.commons.io.FileUtils.isFileOlder(file, reference);
    }

    public static boolean isFileOlder(File file, FileTime fileTime) throws IOException {
        return org.apache.commons.io.FileUtils.isFileOlder(file, fileTime);
    }

    public static boolean isFileOlder(File file, Instant instant) {
        return org.apache.commons.io.FileUtils.isFileOlder(file, instant);
    }

    public static boolean isFileOlder(File file, long timeMillis) {
        return org.apache.commons.io.FileUtils.isFileOlder(file, timeMillis);
    }

    public static boolean isFileOlder(File file, OffsetDateTime offsetDateTime) {
        return org.apache.commons.io.FileUtils.isFileOlder(file, offsetDateTime);
    }

    public static boolean isRegularFile(File file, LinkOption... options) {
        return org.apache.commons.io.FileUtils.isRegularFile(file, options);
    }

    public static boolean isSymlink(File file) {
        return org.apache.commons.io.FileUtils.isSymlink(file);
    }

    public static Iterator<File> iterateFiles(File directory, IOFileFilter fileFilter, IOFileFilter dirFilter) {
        return org.apache.commons.io.FileUtils.iterateFiles(directory, fileFilter, dirFilter);
    }

    public static Iterator<File> iterateFiles(File directory, String[] extensions, boolean recursive) {
        return org.apache.commons.io.FileUtils.iterateFiles(directory, extensions, recursive);
    }

    public static Iterator<File> iterateFilesAndDirs(File directory, IOFileFilter fileFilter, IOFileFilter dirFilter) {
        return org.apache.commons.io.FileUtils.iterateFilesAndDirs(directory, fileFilter, dirFilter);
    }

    public static long lastModified(File file) throws IOException {
        return org.apache.commons.io.FileUtils.lastModified(file);
    }

    public static FileTime lastModifiedFileTime(File file) throws IOException {
        return org.apache.commons.io.FileUtils.lastModifiedFileTime(file);
    }

    public static long lastModifiedUnchecked(File file) {
        return org.apache.commons.io.FileUtils.lastModifiedUnchecked(file);
    }

    public static LineIterator lineIterator(File file) throws IOException {
        return org.apache.commons.io.FileUtils.lineIterator(file);
    }

    public static LineIterator lineIterator(File file, String charsetName) throws IOException {
        return org.apache.commons.io.FileUtils.lineIterator(file, charsetName);
    }

    public static Collection<File> listFiles(File directory, IOFileFilter fileFilter, IOFileFilter dirFilter) {
        return org.apache.commons.io.FileUtils.listFiles(directory, fileFilter, dirFilter);
    }

    public static Collection<File> listFiles(File directory, String[] extensions, boolean recursive) {
        return org.apache.commons.io.FileUtils.listFiles(directory, extensions, recursive);
    }

    public static Collection<File> listFilesAndDirs(File directory, IOFileFilter fileFilter, IOFileFilter dirFilter) {
        return org.apache.commons.io.FileUtils.listFilesAndDirs(directory, fileFilter, dirFilter);
    }

    public static void moveDirectory(File srcDir, File destDir) throws IOException {
        org.apache.commons.io.FileUtils.moveDirectory(srcDir, destDir);
    }

    public static void moveDirectoryToDirectory(File source, File destDir, boolean createDestDir) throws IOException {
        org.apache.commons.io.FileUtils.moveDirectoryToDirectory(source, destDir, createDestDir);
    }

    public static void moveFile(File srcFile, File destFile) throws IOException {
        org.apache.commons.io.FileUtils.moveFile(srcFile, destFile);
    }

    public static void moveFile(File srcFile, File destFile, CopyOption... copyOptions) throws IOException {
        org.apache.commons.io.FileUtils.moveFile(srcFile, destFile, copyOptions);
    }

    public static void moveFileToDirectory(File srcFile, File destDir, boolean createDestDir) throws IOException {
        org.apache.commons.io.FileUtils.moveFileToDirectory(srcFile, destDir, createDestDir);
    }

    public static void moveToDirectory(File src, File destDir, boolean createDestDir) throws IOException {
        org.apache.commons.io.FileUtils.moveToDirectory(src, destDir, createDestDir);
    }

    public static OutputStream newOutputStream(File file, boolean append) throws IOException {
        return org.apache.commons.io.FileUtils.newOutputStream(file, append);
    }

    public static FileInputStream openInputStream(File file) throws IOException {
        return org.apache.commons.io.FileUtils.openInputStream(file);
    }

    public static FileOutputStream openOutputStream(File file) throws IOException {
        return org.apache.commons.io.FileUtils.openOutputStream(file);
    }

    public static FileOutputStream openOutputStream(File file, boolean append) throws IOException {
        return org.apache.commons.io.FileUtils.openOutputStream(file, append);
    }

    public static byte[] readFileToByteArray(File file) throws IOException {
        return org.apache.commons.io.FileUtils.readFileToByteArray(file);
    }

    public static String readFileToString(File file, Charset charsetName) throws IOException {
        return org.apache.commons.io.FileUtils.readFileToString(file, charsetName);
    }

    public static String readFileToString(File file, String charsetName) throws IOException {
        return org.apache.commons.io.FileUtils.readFileToString(file, charsetName);
    }

    public static List<String> readLines(File file, Charset charset) throws IOException {
        return org.apache.commons.io.FileUtils.readLines(file, charset);
    }

    public static List<String> readLines(File file, String charsetName) throws IOException {
        return org.apache.commons.io.FileUtils.readLines(file, charsetName);
    }

    public static long sizeOf(File file) {
        return org.apache.commons.io.FileUtils.sizeOf(file);
    }

    public static BigInteger sizeOfAsBigInteger(File file) {
        return org.apache.commons.io.FileUtils.sizeOfAsBigInteger(file);
    }

    public static long sizeOfDirectory(File directory) {
        return org.apache.commons.io.FileUtils.sizeOfDirectory(directory);
    }

    public static BigInteger sizeOfDirectoryAsBigInteger(File directory) {
        return org.apache.commons.io.FileUtils.sizeOfDirectoryAsBigInteger(directory);
    }

    public static Stream<File> streamFiles(File directory, boolean recursive, String... extensions) throws IOException {
        return org.apache.commons.io.FileUtils.streamFiles(directory, recursive, extensions);
    }

    public static File toFile(URL url) {
        return org.apache.commons.io.FileUtils.toFile(url);
    }

    public static File[] toFiles(URL... urls) {
        return org.apache.commons.io.FileUtils.toFiles(urls);
    }

    public static void touch(File file) throws IOException {
        org.apache.commons.io.FileUtils.touch(file);
    }

    public static URL[] toURLs(File... files) throws IOException {
        return org.apache.commons.io.FileUtils.toURLs(files);
    }

    public static boolean waitFor(File file, int seconds) {
        return org.apache.commons.io.FileUtils.waitFor(file, seconds);
    }

    public static void write(File file, CharSequence data, Charset charset) throws IOException {
        org.apache.commons.io.FileUtils.write(file, data, charset);
    }

    public static void write(File file, CharSequence data, Charset charset, boolean append) throws IOException {
        org.apache.commons.io.FileUtils.write(file, data, charset, append);
    }

    public static void write(File file, CharSequence data, String charsetName) throws IOException {
        org.apache.commons.io.FileUtils.write(file, data, charsetName);
    }

    public static void write(File file, CharSequence data, String charsetName, boolean append) throws IOException {
        org.apache.commons.io.FileUtils.write(file, data, charsetName, append);
    }

    public static void writeByteArrayToFile(File file, byte[] data) throws IOException {
        org.apache.commons.io.FileUtils.writeByteArrayToFile(file, data);
    }

    public static void writeByteArrayToFile(File file, byte[] data, boolean append) throws IOException {
        org.apache.commons.io.FileUtils.writeByteArrayToFile(file, data, append);
    }

    public static void writeByteArrayToFile(File file, byte[] data, int off, int len) throws IOException {
        org.apache.commons.io.FileUtils.writeByteArrayToFile(file, data, off, len);
    }

    public static void writeByteArrayToFile(File file, byte[] data, int off, int len, boolean append) throws IOException {
        org.apache.commons.io.FileUtils.writeByteArrayToFile(file, data, off, len, append);
    }

    public static void writeLines(File file, Collection<?> lines) throws IOException {
        org.apache.commons.io.FileUtils.writeLines(file, lines);
    }

    public static void writeLines(File file, Collection<?> lines, boolean append) throws IOException {
        org.apache.commons.io.FileUtils.writeLines(file, lines, append);
    }

    public static void writeLines(File file, Collection<?> lines, String lineEnding) throws IOException {
        org.apache.commons.io.FileUtils.writeLines(file, lines, lineEnding);
    }

    public static void writeLines(File file, Collection<?> lines, String lineEnding, boolean append) throws IOException {
        org.apache.commons.io.FileUtils.writeLines(file, lines, lineEnding, append);
    }

    public static void writeLines(File file, String charsetName, Collection<?> lines) throws IOException {
        org.apache.commons.io.FileUtils.writeLines(file, charsetName, lines);
    }

    public static void writeLines(File file, String charsetName, Collection<?> lines, boolean append) throws IOException {
        org.apache.commons.io.FileUtils.writeLines(file, charsetName, lines, append);
    }

    public static void writeLines(File file, String charsetName, Collection<?> lines, String lineEnding) throws IOException {
        org.apache.commons.io.FileUtils.writeLines(file, charsetName, lines, lineEnding);
    }

    public static void writeLines(File file, String charsetName, Collection<?> lines, String lineEnding, boolean append) throws IOException {
        org.apache.commons.io.FileUtils.writeLines(file, charsetName, lines, lineEnding, append);
    }

    public static void writeStringToFile(File file, String data, Charset charset) throws IOException {
        org.apache.commons.io.FileUtils.writeStringToFile(file, data, charset);
    }

    public static void writeStringToFile(File file, String data, Charset charset, boolean append) throws IOException {
        org.apache.commons.io.FileUtils.writeStringToFile(file, data, charset, append);
    }

    public static void writeStringToFile(File file, String data, String charsetName) throws IOException {
        org.apache.commons.io.FileUtils.writeStringToFile(file, data, charsetName);
    }

    public static void writeStringToFile(File file, String data, String charsetName, boolean append) throws IOException {
        org.apache.commons.io.FileUtils.writeStringToFile(file, data, charsetName, append);
    }

    /**
     * Copy the contents of the given input File into a new byte array.
     * @param in the file to copy from
     * @return the new byte array that has been copied to
     * @throws IOException in case of I/O errors
     */
    public static byte[] copyToByteArray(File in) throws IOException {
        Asserts.notNull(in, "No input File specified");
        return copyToByteArray(Files.newInputStream(in.toPath()));
    }

    /**
     * Copy the contents of the given InputStream into a new byte array.
     * Closes the stream when done.
     * @param in the stream to copy from (may be {@code null} or empty)
     * @return the new byte array that has been copied to (possibly empty)
     * @throws IOException in case of I/O errors
     */
    public static byte[] copyToByteArray(InputStream in) throws IOException {
        if (in == null) {return new byte[0];}
        try (in) {return in.readAllBytes();}
    }

    /**
     * Copy the contents of the given Reader into a String.
     * Closes the reader when done.
     * @param in the reader to copy from (may be {@code null} or empty)
     * @return the String that has been copied to (possibly empty)
     * @throws IOException in case of I/O errors
     */
    public static String copyToString( Reader in) throws IOException {
        if (in == null) {return "";}
        StringWriter out = new StringWriter(BUFFER_SIZE);
        copy(in, out);
        return out.toString();
    }

    /**
     * Copy the contents of the given Reader to the given Writer.
     * Closes both when done.
     * @param in the Reader to copy from
     * @param out the Writer to copy to
     * @return the number of characters copied
     * @throws IOException in case of I/O errors
     */
    public static int copy(Reader in, Writer out) throws IOException {
        Asserts.notNull(in, "No Reader specified");
        Asserts.notNull(out, "No Writer specified");

        try {
            int charCount = 0;
            char[] buffer = new char[BUFFER_SIZE];
            int charsRead;
            while ((charsRead = in.read(buffer)) != -1) {
                out.write(buffer, 0, charsRead);
                charCount += charsRead;
            }
            out.flush();
            return charCount;
        }
        finally {
            close(in);
            close(out);
        }
    }

    /**
     * Attempt to close the supplied {@link Closeable}, silently swallowing any
     * exceptions.
     * @param closeable the {@code Closeable} to close
     */
    private static void close(Closeable closeable) {
        try {closeable.close();}
        catch (IOException ignored) {}
    }

    /**
     * The default buffer size used when copying bytes.
     */
    public static final int BUFFER_SIZE = 8192;
}