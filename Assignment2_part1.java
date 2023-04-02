package com.company;


import java.io.BufferedReader;
        import java.io.ByteArrayInputStream;
        import java.io.IOException;
        import java.io.InputStreamReader;
        import java.net.DatagramPacket;
        import java.net.DatagramSocket;
        import java.net.InetAddress;
        import java.util.Date;
        import java.util.Scanner;

public class Assignment2_part1 {


    public static void main(String[] args) throws Exception
    {

        Scanner scanner = new Scanner(System.in);

        // Port number to access
        System.out.println("Port number");
        int portInput = scanner.nextInt();

        // Server to Ping
        System.out.println("IP address");
        InetAddress server = InetAddress.getByName(scanner.next());
        // Create a datagram socket for sending and receiving UDP packets
        System.out.println("Number of packets ");
        int packetsInput = scanner.nextInt();

        System.out.println("Maximum timeout in ms");
        int timeInput = scanner.nextInt();

        System.out.println("Waiting time between sending packets in ms");
        int waitingInput = scanner.nextInt();


        DatagramSocket socket = new DatagramSocket(portInput);

        int sequence_number = 0;

        while (sequence_number < packetsInput) {
            // Timestamp in ms when we send it
            Date now = new Date();
            long msSend = now.getTime();
            // Create string to send, and transfer i to a Byte Array
            String str = "PING " + sequence_number + " " + msSend + " \n";
            byte[] buf = new byte[1024];
            buf = str.getBytes();
            // Create a datagram packet to send as an UDP packet.
            DatagramPacket ping = new DatagramPacket(buf, buf.length, server, portInput);

            // Send the Ping datagram to the specified server
            socket.send(ping);

            try {

                socket.setSoTimeout(timeInput);

                DatagramPacket response = new DatagramPacket(new byte[1024], 1024);

                socket.receive(response);

                now = new Date();
                long msReceived = now.getTime();

                Display(response, msReceived - msSend);
            // Print which packet has timed out
            } catch (IOException e) {

                System.out.println("Timeout for packet " + sequence_number);
            }

            sequence_number ++;
            Thread.sleep(waitingInput);
        }
    }

    private static void Display(DatagramPacket request, long delayTime) throws Exception
    {
        // Obtain references to the packet's array of bytes
        byte[] buf = request.getData();

        // Wrap the bytes in a byte array input stream,
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(buf);

        // Wrap the byte array output stream in an input stream reader
        InputStreamReader isr = new InputStreamReader(byteArrayInputStream);


        BufferedReader bufferedReader = new BufferedReader(isr);


        String line = bufferedReader.readLine();

        // Print host address and data received from it. 
        System.out.println(
                "Received from " +
                        request.getAddress().getHostAddress() +
                        ": " +
                        new String(line) + " Time: " + delayTime + "ms" );
    }
}