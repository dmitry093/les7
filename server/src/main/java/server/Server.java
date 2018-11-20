package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

public class Server {
    private ConcurrentHashMap<String, ClientHandler> clients;

    public Server() {
        AuthService.connect();
        clients = new ConcurrentHashMap<>();
        ServerSocket server = null;
        Socket socket = null;

        try {
            server = new ServerSocket(8183);
            System.out.println("Сервер запущен!");

            while (true) {
                socket = server.accept();
                System.out.println("Клиент подключился");
//                clients.add(new ClientHandler(this, socket));
                new ClientHandler(this, socket);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                server.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            AuthService.disconnect();
        }
    }

    public void broadcastMsg(String msg) {
        for (ClientHandler o : clients.values()) {
            o.sendMsg(msg);
        }
    }

    public void privateMessage(String destination, String message, ClientHandler from){
        if (clients.containsKey(destination)){
            clients.get(destination).sendMsg(from.nick + "[private]: " + message);
        }
        else{
            from.sendMsg("Не найден собеседник с ником " + destination);
        }
    }

    public void subscribe(String nickName, ClientHandler clientHandler) {
        clients.put(nickName, clientHandler);
    }

    public void unsubscribe(ClientHandler clientHandler) {
        clients.remove(clientHandler);
    }


}
