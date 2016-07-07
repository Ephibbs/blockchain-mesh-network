package LANcomm;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

public class Broadcasts {

    private final Runnable receiver;
    private final Runnable sender;
    private boolean run = true;

    public Broadcasts(LANChat parent) {
        receiver = new Runnable() {
            public void run() {
                byte data[] = new byte[0];
                DatagramSocket socket = null;
                try {
                    socket = new DatagramSocket(Globals.UDPPORT);
                } catch (SocketException ex) {
                    ex.printStackTrace();
                    parent.quit();
                }
                DatagramPacket packet = new DatagramPacket(data, data.length);
                while (run) {
                    try {
                        socket.receive(packet);
                    } catch (IOException ex) {
                        ex.printStackTrace();
                        parent.quit();
                    }
                    parent.newAddress(packet.getAddress());
                }
            }
        };
        sender = new Runnable() {
            public void run() {
                byte data[] = new byte[0];
                DatagramSocket socket = null;
                try {
                    socket = new DatagramSocket();
                } catch (SocketException ex) {
                    ex.printStackTrace();
                    parent.quit();
                }
                DatagramPacket packet = new DatagramPacket(
                        data, 
                        data.length, 
                        Globals.broadcastAddress, 
                        Globals.UDPPORT);
                while (run) {
                    try {
                        socket.send(packet);
                        Thread.sleep(Globals.UDPINTERVAL);
                    } catch (IOException ex) {
                        ex.printStackTrace();
                        parent.quit();
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                        parent.quit();
                    }
                }
            }
        };
        new Thread(receiver).start();
        new Thread(sender).start();
    }

    public void quit() {
        run = false;
    }
}