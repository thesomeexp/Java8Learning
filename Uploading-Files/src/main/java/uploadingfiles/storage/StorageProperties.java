package uploadingfiles.storage;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author someexp
 * @date 2021/5/31
 */
@ConfigurationProperties("storage")
public class StorageProperties {

    /**
     * Folder location for storing files
     */
    private String location = "upload-dir";

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

}
