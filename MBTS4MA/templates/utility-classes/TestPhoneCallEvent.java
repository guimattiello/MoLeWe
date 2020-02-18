package {{projectpackage}}.test.util;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.Socket;
import java.net.UnknownHostException;

public class TestPhoneCallEvent {

    public final class PhoneCallActions {

        public static final String call = "call";
        public static final String accept = "accept";
        public static final String busy = "busy";
        public static final String cancel = "cancel";
        public static final String data = "data";
        public static final String hold = "hold";
        public static final String list = "list";
        public static final String voice = "voice";
        public static final String status = "status";

    }

    public final class PhoneCallStates {

        public static final String unregistered = "unregistered";
        public static final String home = "home";
        public static final String roaming = "roaming";
        public static final String searching = "searching";
        public static final String denied = "denied";
        public static final String off = "off";
        public static final String on = "on";

    }

    public static void changeState(String action, String state) {
        try {
            Socket socket = new Socket("10.0.2.2", 5554);
            socket.setKeepAlive(true);

            String str = "gsm " + action + " " + state;

            Writer w = new OutputStreamWriter(socket.getOutputStream());

            w.write(str + "\r\n");
            w.flush();
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void goBack() {
        try {
            Socket socket = new Socket("10.0.2.2", 5554);
            socket.setKeepAlive(true);

            // String str = "event send 1:158:1 1:158:0";
            String str = "event send EV_KEY:KEY_BACK:1 EV_KEY:KEY_BACK:0";

            Writer w = new OutputStreamWriter(socket.getOutputStream());

            w.write(str + "\r\n");
            w.flush();
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void phoneCallAction(String action, String number) {
        try {
            Socket socket = new Socket("10.0.2.2", 5554);
            socket.setKeepAlive(true);

            String str = "gsm " + action + " " + number;

            Writer w = new OutputStreamWriter(socket.getOutputStream());

            w.write(str + "\r\n");
            w.flush();
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void receivePhoneCall(String number) {
        try {
            Socket socket = new Socket("10.0.2.2", 5554);
            socket.setKeepAlive(true);

            String str = "gsm call " + number;

            Writer w = new OutputStreamWriter(socket.getOutputStream());

            w.write(str + "\r\n");
            w.flush();
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void cancelPhoneCall(String number) {
        try {
            Socket socket = new Socket("10.0.2.2", 5554);
            socket.setKeepAlive(true);

            String str = "gsm cancel " + number;

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
