package LANcomm;
import java.io.*;
import java.net.*;
import java.util.*;
 
public class EchoClient {
    public static void main(String[] args) throws IOException {
 
        String hostName = args[0];
        int portNumber = Integer.parseInt(args[1]);
 
        try (
            Socket echoSocket = new Socket(hostName, portNumber);
            PrintWriter out =
                new PrintWriter(echoSocket.getOutputStream(), true);
            BufferedReader in =
                new BufferedReader(
                    new InputStreamReader(echoSocket.getInputStream()));
            BufferedReader stdIn =
                new BufferedReader(
                    new InputStreamReader(System.in))
        ) {
            String userInput;
            while ((userInput = stdIn.readLine()) != null) {
                out.println(userInput);
                System.out.println("echo: " + in.readLine());
            }
        } catch (UnknownHostException e) {
            System.err.println("Don't know about host " + hostName);
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for the connection to " +
                hostName);
            System.exit(1);
        } 
    }
    public static ArrayList<InetAddress> getNeighborhoodIPs() {
	    InetAddress localhost;
	    ArrayList<InetAddress> IPs = new ArrayList<InetAddress>();
		try {
			localhost = InetAddress.getLocalHost();

		    byte[] ip = localhost.getAddress();
		    String output;
		
		    for (int i = 1; i <= 254; i++) {
	            ip[3] = (byte)i;
	            InetAddress address;
				address = InetAddress.getByAddress(ip);
	
	            if (address.isReachable(100))
	            {
	                output = address.toString().substring(1);
	                System.out.print(output + " is on the network");
	                IPs.add(address);
	            }
		    }
		} catch (IOException e) {
			e.printStackTrace();
		}
		return IPs;
    }
}