package albertcastineira;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class Folder {
    private String name;
    private String path;
    private LocalDateTime lastModifiedTime;
    private String author = "admin";
    private int diskBlockCount = 4096;
    private int hardLinksCount = 1;
    private String permissions;
    private ArrayList<File> FILES = new ArrayList<>();
    Folder(String folderName, String path, String author, String hardLinksCount) {
        this.name = folderName;
        this.lastModifiedTime = LocalDateTime.now();
        if(author != null) this.author = author;
        if(hardLinksCount != null) this.hardLinksCount = Integer.parseInt(hardLinksCount);
        setDefaultPermissions();
        this.path = path;

    }

    private void setDefaultPermissions() {
        permissions = "drw-r--r--";
    }

    // drwxr-xr-x 2 user user  4096 Oct 30 08:30 directory1
    public String getName() {
        return this.name;
    }

    public String getLastModifiedTime() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        return this.lastModifiedTime.format(formatter);
    }

    public String getAuthor() {
        return this.author;
    }

    public int getDiskBlockCount() {
        return this.diskBlockCount;
    }

    public int getHardLinksCount() {
        return this.hardLinksCount;
    }


    public String getPermissions() {
        return this.permissions;
    }

    public String getPath() {
        return this.path;

    }
}
