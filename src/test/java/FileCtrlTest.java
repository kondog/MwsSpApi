import japacomo.FileCtrlSet;
import japacomo.TakeSpecifiedProperty;
import org.junit.jupiter.api.Test;

public class FileCtrlTest {
    @Test
    public void testUnzipFile(){
        TakeSpecifiedProperty prop = new TakeSpecifiedProperty("src/main/resources/conf/us.config.properties");
        FileCtrlSet.setExtentionToFile("test", false);
        FileCtrlSet.setExtentionToFile("src/test/java/testresources/text.type.test.txt",false);
        FileCtrlSet.setExtentionToFile("src/test/java/testresources/test.tar.gz",false);
    }

    @Test
    public void testUnzipFile_FAT(){
        //sp-api report return gzip with FAT file system(Windows) when
        //file size is too big. like below:
        //OK puttern: test.tar.gz:     gzip compressed data, last modified: Fri Aug 12 01:43:59 2022, from Unix, original size modulo 2^32 2048
        //NG puttern: test_FAT.tar.gz: gzip compressed data, from FAT filesystem (MS-DOS, OS/2, NT), original size modulo 2^32 9854659
        //
        //so need to windows puttern.
        TakeSpecifiedProperty prop = new TakeSpecifiedProperty("src/main/resources/conf/us.config.properties");
        FileCtrlSet.setExtentionToFile("src/test/java/testresources/test_FAT.tar.gz",
                false);
    }



}
