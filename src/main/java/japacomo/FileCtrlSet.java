package japacomo;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.compress.compressors.gzip.GzipCompressorInputStream;
import org.apache.commons.compress.utils.IOUtils;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.logging.Level;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.tika.Tika;
import org.apache.tika.io.TikaInputStream;
import org.apache.tika.metadata.Metadata;

public class FileCtrlSet {
    static LoggingJapacomo lj = new LoggingJapacomo();
    static Logger logger = LoggingJapacomo.logger;

    private FileCtrlSet(){}
    private static String decideContentType(Path src) throws IOException{
        System.out.println("decideContentType" + src.toAbsolutePath());
        String contentType = "";
        //            String contentType = Files.probeContentType(src);
//            InputStream is = Files.newInputStream(src);
//            contentType =
//                    URLConnection.guessContentTypeFromStream(new BufferedInputStream(is));
//            is.close();

        Tika tika = new Tika();
        InputStream is = Files.newInputStream(src);
        TikaInputStream tis = TikaInputStream.get(is);
        contentType = tika.detect(tis);
        logger.log(Level.INFO, contentType);

//        MimeUtil.registerMimeDetector("eu.medsea.mimeutil.detector.MagicMimeMimeDetector");
//        Collection<?> mimeTypes = MimeUtil.getMimeTypes(src.toString());
//        for(var data: mimeTypes){
//            contentType = data.toString();
//            logger.log(Level.FINE, contentType);
//        }

        return contentType;
    }
    public static void unzipFile(String srcStr, String dstStr, Boolean deleteSrcFile) {
        Path src = Paths.get(srcStr);
        Path dst = Paths.get(dstStr);
        if (!Files.exists(src)){
            logger.log(Level.INFO, "file not found," + srcStr);
            return;
        }
        try {
            if (Files.exists(dst)){Files.delete(dst);}

            String contentType = decideContentType(src);

            switch(contentType) {
                case "application/octet-stream":
                    if (Files.size(src) <= 1.5 * 1024 * 1024) {
                        unzipUnixGzipFile(src, dstStr);
                    } else {
                        logger.log(Level.INFO, "file size over, no decompress" + srcStr);
                        return;
                    }
                    break;
                case "application/gzip":
                case "application/x-gzip":
                    if (Files.size(src) <= 1.5 * 1024 * 1024) {
                        unzipWindowsGzipFile(src, dstStr);
                    } else {
                        logger.log(Level.INFO, "file size over, no decompress" + srcStr);
                        return;
                    }
                    break;
                default:
                    logger.log(Level.INFO, "file " + srcStr + " is not gz,but " + contentType);
                    Files.copy(src, dst);
            }
            if(deleteSrcFile){Files.delete(src);}
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private static void unzipUnixGzipFile(Path src, String dstStr){
        try{
            InputStream fi = Files.newInputStream(src);
            InputStream gzi = new GzipCompressorInputStream(fi);
            ArchiveInputStream in = new TarArchiveInputStream(gzi);

            ArchiveEntry entry;
            while ((entry = in.getNextEntry()) != null) {
                if (!in.canReadEntryData(entry)) {
                    continue;
                }

                File file = new File(dstStr);
                if (entry.isDirectory()) {
                    if (!file.isDirectory() && !file.mkdirs()) {
                        throw new IOException("failed to create directory " + file);
                    }
                } else {
                    File parent = file.getParentFile();
                    if (!parent.isDirectory() && !parent.mkdirs()) {
                        throw new IOException("failed to create directory " + parent);
                    }
                    try (OutputStream o = Files.newOutputStream(file.toPath())) {
                        IOUtils.copy(in, o);
                    }
                }
            }
        } catch (IOException e) {
            logger.log(Level.SEVERE, "IOException in unzipUnixGzipFile!" +
                    "src:" + src +
                    "dst:" + dstStr
            );
            throw new UncheckedIOException(e);
        }
    }

    private static void unzipWindowsGzipFile(Path src, String dstStr){
        try {
            InputStream fi = Files.newInputStream(src);
            InputStream gzi = new GzipCompressorInputStream(fi);

            File file = new File(dstStr);
            File parent = file.getParentFile();
            if (!parent.isDirectory() && !parent.mkdirs()) {
                throw new IOException("failed to create directory " + parent);
            }
            try (OutputStream o = Files.newOutputStream(file.toPath())) {
                IOUtils.copy(gzi, o);
            }
        } catch (IOException e) {
            logger.log(Level.SEVERE, "IOException in unzipWindowsGzipFile!" +
                    "src:" + src +
                    "dst:" + dstStr
            );
            throw new UncheckedIOException(e);
        }
    }

}
