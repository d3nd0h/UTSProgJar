/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clientuts;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author fendy
 */
public class ClientUTS {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        try {
            //init
            Socket socket = new Socket("10.151.34.155", 6666);
            InputStream is = socket.getInputStream();
            OutputStream os = socket.getOutputStream();
            boolean flag=false; //untuk menandai sudah ada respond code atau tidak
            
            //talking-talking
            while (true) {
                byte[] buf = new byte[1];
                is.read(buf);
                String cmd = new String(buf);
                while (!cmd.endsWith("\n")) {
                    is.read(buf);
                    cmd += new String(buf);
                }
                System.out.print("server: " + cmd);
                
                if(cmd.equals("Hash:\n")) {
                    byte[] dum = new byte[1];
                    is.read(dum);
                    String arguments = new String(dum);
                    while (!arguments.endsWith("\n")) {
                        is.read(dum);
                        arguments += new String(dum);
                    }
                    System.out.print(arguments);
                    arguments = arguments.trim();
                    
                    String[] arg = arguments.split(":");
                    int len = Integer.parseInt(arg[1]);
                    
                    String respond = "Hash:";
                    for(int i=0;i<len;i++) {
                        is.read(dum);
                        respond += new String(dum);
                    }
                    
                    respond += "\n";
                    System.out.print("respond: " + respond);
                    os.write(respond.getBytes());
                    os.flush();
                    
                    is.read(dum);
                    is.read(dum); //somehow servernya return line kosong
                    arguments = new String(dum);
                    while (!arguments.endsWith("\n")) {
                        is.read(dum);
                        arguments += new String(dum);
                    }
                    
                    String[] status = arguments.trim().split(" ");
                    //for(String stat : status)System.out.println(stat);
                    if(status[0].equals("666")){
                        System.out.print(arguments);
                        break;
                    } else {
                        System.out.println("Error " + status[0] + "\n");
                    }
                } else if(cmd.endsWith("?\n")) {
                    cmd = cmd.trim();
                    String[] inputs = cmd.split(" ");
                    int bil1 = Integer.parseInt(inputs[0]);
                    int bil2 = Integer.parseInt(inputs[2]);
                    int res;
                    
                    switch (inputs[1]) {
                        case "+":
                            res = bil1+bil2;
                            break;
                        case "-":
                            res = bil1-bil2;
                            break;
                        case "x":
                            res = bil1*bil2;
                            break;
                        default:
                            res = bil1%bil2;
                            break;
                    }
                    
                    String respond = "result:"+ Integer.toString(res) + "\n";
                    System.out.print("respond: " + respond);
                    os.write(respond.getBytes());
                    os.flush();
                } else if(cmd.endsWith("NRP\\n\n")) {
                    Scanner scanner = new Scanner(System.in);
                    String username = scanner.nextLine();
                    username = "username:" + username + "\n";
                    os.write(username.getBytes());
                    os.flush();
                }
            }
            os.close();
            is.close();
            socket.close();
        } catch (IOException ex) {
            Logger.getLogger(ClientUTS.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
}
