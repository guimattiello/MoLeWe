package {{projectpackage}}.test.util;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.Socket;
import java.net.UnknownHostException;
import com.robotium.solo.Solo;

public class TestLocationProvider {

    public static void sendLocation(Solo solo, double[] latitude, double[] longitude) {
        for (int i = 0; i < latitude.length; i++) {
            TestLocationProvider.sendLocation(latitude[i], longitude[i]);

            solo.sleep(1000);
        }
    }

    public static void sendLocation(double latitude, double longitude) {
        try {
            Socket socket = new Socket("10.0.2.2", 5554);
            socket.setKeepAlive(true);

            String str = "geo fix " + longitude + " " + latitude;

            Writer w = new OutputStreamWriter(socket.getOutputStream());

            w.write(str + "\r\n");
            w.flush();
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
