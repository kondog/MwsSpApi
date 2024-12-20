package japacomo;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

public class TakeSpecifiedProperty {
    private Properties properties;

    public TakeSpecifiedProperty(String propertyFilePath){
        Path path = Paths.get(propertyFilePath);
        if (!(Files.exists(path))){
            System.out.println(String.format("File Not Found.:%s", propertyFilePath));
        }
        properties = new Properties();
        try {
            properties.load(Files.newBufferedReader(Paths.get(propertyFilePath), StandardCharsets.UTF_8));
        } catch (IOException e) {
            System.out.println(String.format("File Not Found.:%s", propertyFilePath));
        }
    }

    public String getProperty(final String key) {
        return getProperty(key, "");
    }

    public String getProperty(final String key, final String defaultValue) {
        return this.properties.getProperty(key, defaultValue);
    }

    public String[] getPropertyAsArray(final String key, final String delimiter){
        String valueAsCSV = this.properties.getProperty(key);
        return valueAsCSV.split(delimiter);
    }


}
