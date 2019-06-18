package br.edu.ifpb.main;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;

public class Loader {

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        new Loader("localhost", 12303).executa();
    }

    private String host;
    private int porta;

    public Loader (String host, int porta) {
        this.host = host;
        this.porta = porta;
    }

    public void executa() throws IOException, ClassNotFoundException {

        ServerSocket server = new ServerSocket(this.porta);
        System.out.println("Aguardando conexão na porta 12303");

        while (true){
            Socket accept = server.accept();
            System.out.println("Cliente conecatdo");

            ObjectInputStream inputStreamServer = new ObjectInputStream(accept.getInputStream());
            Map<String, Integer> dataObject = (Map<String, Integer>) inputStreamServer.readObject();

            System.out.println("Objeto de dados recebido: " + dataObject);

            Integer node = dataObject.get("node");

            Socket socket = null;
            dataObject.replace("node", 3);
            if (node == 1) socket = new Socket(this.host, 12302);
            else if (node == 2) socket = new Socket(this.host, 12304);

            Integer result = delegateTo(socket, dataObject);
            System.out.println("Operação delegada para um node responsável");

            ObjectOutputStream outputStreamServer = new ObjectOutputStream(accept.getOutputStream());
            outputStreamServer.writeInt(result);
            outputStreamServer.flush();
        }
    }

    private Integer delegateTo(Socket socket, Map<String, Integer> dataObject) throws IOException {
        ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
        outputStream.writeObject(dataObject);

        ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());
        Integer result = inputStream.readInt();

        outputStream.close();
        inputStream.close();
        return result;
    }

}
