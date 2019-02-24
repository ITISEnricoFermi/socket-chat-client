/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package socket.client;

import java.io.*;
import java.net.*;
import static java.lang.Thread.sleep;

/**
 *
 * @author 5G
 */
public class SocketClient {

    //socket
    private Socket connessione = null;

    //input
    private InputStream in = null;
    private InputStreamReader input = null;
    private BufferedReader sIN = null;

    //output
    private OutputStream out = null;
    private PrintWriter sOUT = null;
    private Ui mask = null;

    SocketClient(String server, int porta) {
        //interface
        mask = new Ui("Client");
        mask.setVisible(true);

        try {
            connessione = new Socket(server, porta);
            System.out.println("connessione aperta");

            //input
            in = connessione.getInputStream();
            input = new InputStreamReader(in);
            sIN = new BufferedReader(input);

            //output
            out = connessione.getOutputStream();
            sOUT = new PrintWriter(out);

            //action listener for send button
            mask.send.addActionListener(e -> {
                //append message to chatBox
                String messageOUT = mask.field.getText();
                mask.sendMessage(mask.actor, messageOUT);

                //send message and empty buffer
                sOUT.println(messageOUT);
                sOUT.flush();
                System.out.println("sending -> " + messageOUT);
            });
        } catch (IOException ex) {
            System.err.println(ex);
        }
    }

    void process() throws IOException, InterruptedException {
        String message;
        while (true) {
            if (sIN.ready()) {
                System.out.print("received <- ");
                message = sIN.readLine();
                System.out.println(message);
                mask.getMessage("Server", message);
            }
            sleep(100);

        }
    }

    public static void main(String[] args) {
        //create client object
        SocketClient client = new SocketClient("localhost", 3333);

        //process messages
        try {
            client.process();
        } catch (Exception ex) {
            System.err.println(ex);
        }
    }
}
