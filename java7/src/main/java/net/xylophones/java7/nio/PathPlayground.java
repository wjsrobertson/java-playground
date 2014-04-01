package net.xylophones.java7.nio;

import java.io.IOException;
import java.nio.file.*;

public class PathPlayground {

    public static void main(String[] args) throws IOException, InterruptedException {
        Path path = Paths.get("D:/temp");
        WatchService watchService = path.getFileSystem().newWatchService();

        path.register(watchService,
                StandardWatchEventKinds.ENTRY_CREATE, StandardWatchEventKinds.ENTRY_DELETE, StandardWatchEventKinds.ENTRY_MODIFY);

        while (true) {
            WatchKey take = watchService.take();
            for (WatchEvent event: take.pollEvents()) {
                System.out.println(event.kind() + ": " + event.context());
            }
            take.reset();
        }
    }

    private static void doPathStuff() {
        Path path = Paths.get("D:/temp");
        System.out.println("exists?: " + Files.exists(path));
    }

}
