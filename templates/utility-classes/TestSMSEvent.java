package {{projectpackage}}.test.util;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.Socket;
import java.net.UnknownHostException;

public class TestSMSEvent {

    public static void receiveSMS(String number, String message) {
        try {
            Socket socket = new Socket("10.0.2.2", 5554);
            socket.setKeepAlive(true);

            String str = "sms send " + number + " " + message;

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
