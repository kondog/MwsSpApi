package main.java.japacomo;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;

public class TakeSpecifiedProperty {
    private Properties properties;

    public TakeSpecifiedProperty(String propertyFilePath){
        properties = new Properties();
        try {
            properties.load(Files.newBufferedReader(Paths.get(propertyFilePath), StandardCharsets.UTF_8));
        } catch (IOException e) {
            // ファイル読み込みに失敗
            System.out.println(String.format("ファイルの読み込みに失敗しました。ファイル名:%s", propertyFilePath));
        }
    }

    /**
     * プロパティ値を取得する
     *
     * @param key キー
     * @return 値
     */
    public String getProperty(final String key) {
        return getProperty(key, "");
    }

    /**
     * プロパティ値を取得する
     *
     * @param key キー
     * @param defaultValue デフォルト値
     * @return キーが存在しない場合、デフォルト値
     *          存在する場合、値
     */
    public String getProperty(final String key, final String defaultValue) {
        return this.properties.getProperty(key, defaultValue);
    }



}
