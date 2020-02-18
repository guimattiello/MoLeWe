package {{projectpackage}}.test.util;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.Socket;
import java.net.UnknownHostException;

public class TestAccelerationEvent {

    public static void changeAccelerationState(String axisX, String axisY, String axisZ) {
        try {
            Socket socket = new Socket("10.0.2.2", 5554);
            socket.setKeepAlive(true);

            String str = "sensor set acceleration " + axisX + ":" + axisY + ":" + axisZ;

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
