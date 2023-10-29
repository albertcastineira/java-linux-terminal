package albertcastineira;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Folder {
    private String name;
    private LocalDateTime lastModifiedTime;
    public
    Folder(String folderName) {
        this.name = folderName;
        this.lastModifiedTime = LocalDateTime.now();
    }

    public String getName() {
        return name;
    }

    public String getLastModifiedTime() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        return lastModifiedTime.format(formatter);
    }
}
