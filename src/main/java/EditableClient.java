
import java.io.*;
import java.net.*;
import java.util.*;



public class EditableClient {
    /*
     * To change this license header, choose License Headers in Project Properties.
     * To change this template file, choose Tools | Templates
     * and open the template in the editor.
     */


    // import java.util.scanner;

        public static void main(String argv[])
        {
            // importing Scanner class to get "mail to,mail from, subject and message" from the user
            Scanner in = new Scanner(System.in);
            // A class which contains a certain picture. The picture has been encoded to base64
            PictureSending newPicture = new PictureSending();



            try{
                // Establishing a connection with a server by typing server address and port number
                Socket socketClient= new Socket("127.0.0.1",25);
                // A message to the user which tells the user that the connection has been established
                System.out.println("Client: "+"Connection established successfully ");

                BufferedReader bufferedReader =
                        new BufferedReader(new InputStreamReader(socketClient.getInputStream()));
                // to send new commands to the server
                BufferedWriter writer=
                        new BufferedWriter(new OutputStreamWriter(socketClient.getOutputStream()));
                // serverMsg to check the response from the server
                // s to send new commands to the server
                String msgInServer,sending;
                while((msgInServer = bufferedReader.readLine()) != null){
                    System.out.println("Server: " + msgInServer);
                    // When the server gives us a new message which starts with 220, we know then
                    // that the connection has been established and we have to greet the server by
                    // HELO/EHLO something
                    if (msgInServer.startsWith("220")){
                        sending = "HELO localhost";
                        writer.write(sending+"\r\n");
                        writer.flush(); // .flush send a command to the server
                    }
                    // When the server gives a message that starts with 250 n-62, we know that
                    // the server has said "Pleased to meet you" and waiting for the command MAIL FORM:
                    if (msgInServer.startsWith("250 n-62")){
                        System.out.print("MAIL FROM: ");
                        sending = "MAIL FROM: " + in.nextLine();
                        writer.write(sending+"\r\n");
                        writer.flush();
                        continue;
                    }

                    // When the server gives a message that starts with 250 2.1.0, we know that
                    // the server has accepted the sender mail and waiting for the command RCPT TO:
                    else if (msgInServer.startsWith("250 2.1.0")){
                        System.out.print("MAIL TO: ");
                        sending = "RCPT TO: " + in.nextLine();
                        writer.write(sending+"\r\n");
                        writer.flush();
                        continue;
                    }
                    // When the server gives a message that starts with 250 2.1.5, we know that
                    // the server has accepted the recipient mail and waiting for the command DATA
                    else if (msgInServer.startsWith("250 2.1.5")){
                        sending = "DATA";
                        writer.write(sending+"\r\n");
                        writer.flush();
                        continue;
                    }
                    // by typing DATA we get an option to type subject, message and chose a picture
                    else if (msgInServer.startsWith("354")){
                        System.out.println("Subject");
                        String sub = in.nextLine();
                        System.out.println("Chose a name for the sent picture");
                        String picName = in.nextLine();
                        System.out.println("Type message");
                        // The method below stands for sending a picture, typing subject and name the sent picture
                        String msg = in.nextLine();
                        sending =  "Subject:" + sub+"\n" +
                                "MIME-Version: 1.0\n" +
                                "Content-Type:multipart/mixed;boundary=\"KkK170891tpbkKk__FV_KKKkkkjjwq\"\n" +
                                "--KkK170891tpbkKk__FV_KKKkkkjjwq\n" +
                                "Content-Type:application/octet-stream;name=\""+picName+".jpg\"\n" +
                                "Content-Transfer-Encoding:base64\n" +
                                "Content-Disposition:attachment;filename=\""+ picName+".jpg\"\n\n" +
                                newPicture.pictureSending+"\n\n" +
                                "--KkK170891tpbkKk__FV_KKKkkkjjwq--"+"\n";

//                                    s = in.nextLine();
                        writer.write(sending+msg+"\r\n");
                        writer.flush();

                        sending = ".";
                        writer.write(sending+"\r\n");
                        writer.flush();


                    }
                }
            }catch(Exception e){e.printStackTrace();}
        }


}
