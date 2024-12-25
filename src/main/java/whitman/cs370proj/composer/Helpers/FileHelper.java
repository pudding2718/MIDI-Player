package whitman.cs370proj.composer.Helpers;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;

public final class FileHelper {
    public static boolean writeString(File file, String content) {
        if (file == null) return false;

        if (file.exists())
            if (!file.delete())
                return false;

        try {
            if (!file.createNewFile())
                return false;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        var filePath = file.getAbsolutePath();

        try (var out = new PrintWriter(filePath)) {
            out.println(content);
        } catch (FileNotFoundException e) {
            return false;
        }

        return true;
    }

    public static String readAllContent(File file) throws IOException, NullPointerException {
        if (file == null) throw new NullPointerException();
        if (!file.exists()) throw new FileNotFoundException();
        if (!file.canRead()) throw new IOException();

        return Files.readString(Path.of(file.getAbsolutePath()));
    }
}
