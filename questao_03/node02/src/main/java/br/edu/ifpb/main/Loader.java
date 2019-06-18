package br.edu.ifpb.main;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;

public class Loader {

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        new Loader("localhost", 12302).executa();
    }

    private String host;
    private int porta;

    public Loader (String host, int porta) {
        this.host = host;
        this.porta = porta;
    }

    public void executa() throws IOException, ClassNotFoundException {

        ServerSocket server = new ServerSocket(this.porta);
        System.out.println("Aguardando conexao na porta 12302");

            while (true) {
                Socket accept = server.accept();
                System.out.println("Cliente conectado");

                ObjectInputStream inputStreamServer = new ObjectInputStream(accept.getInputStream());
                Map<String, Integer> dataObject = (Map<String, Integer>) inputStreamServer.readObject();

                System.out.println("Objeto de dados recebido: " + dataObject);

                Socket socket = null;
                Integer node = dataObject.get("node");
                if (node == 1) socket = new Socket(this.host, 12303);
                else if (node == 3) socket = new Socket(this.host, 12304);

                dataObject.replace("node", 2);
                Integer result = delegateTo(socket, dataObject);
                System.out.println("Operação delegada para um node avaliável");

                ObjectOutputStream outputStreamServer = new ObjectOutputStream(accept.getOutputStream());
                outputStreamServer.writeInt(result);
                outputStreamServer.flush();
            }

        }

    private Integer delegateTo(Socket socket, Map<String, Integer> dataObect) throws IOException {
        ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
        outputStream.writeObject(dataObect);

        ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());
        Integer result = inputStream.readInt();

        outputStream.close();
        inputStream.close();
        return result;
    }

}
