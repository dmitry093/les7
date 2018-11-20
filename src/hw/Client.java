package hw;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) {
        Socket socket = null;
        try {
            socket = new Socket("localhost", 8181);
            final Scanner in = new Scanner(socket.getInputStream());
            final PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            final Scanner console = new Scanner(System.in);

            Thread t1 = new Thread(new Runnable() {
                @Override
                public void run() {
                    while (true) {
                        String str = in.nextLine();
                        if (str.equals("/end")) {
                            out.println("/end");
                            break;
                        }
                        System.out.println("Server " + str);
                    }
                }
            });
            t1.start();

            Thread t2 = new Thread(new Runnable() {
                @Override
                public void run() {
                    while (true) {
                        System.out.println("Введите сообщение");
                        String str1 = console.nextLine();
                        System.out.println("Сообщение отправлено");
                        out.println(str1);
                    }
                }
            });
            t2.start();

            try {
                t1.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
