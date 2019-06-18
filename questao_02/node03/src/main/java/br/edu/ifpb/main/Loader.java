package br.edu.ifpb.main;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;

public class Loader {

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        new Loader("localhost", 12340).executa(); // inicia o servidor
    }

    private int porta;
    private String host;

    public Loader (String host, int porta) throws IOException {
        this.host = host;
        this.porta = porta;
    }

    public void executa () throws IOException, ClassNotFoundException {
        ServerSocket server = new ServerSocket();
        server.bind(new InetSocketAddress(this.host, this.porta));
        System.out.println("Aguardando conexão na porta 12340...");

        while (true) {
            Socket socket = server.accept();

            ObjectInputStream inputStreamServer = new ObjectInputStream(socket.getInputStream());
            System.out.println("Nova conexão com o cliente " + server.getInetAddress().getHostAddress());

            Map<String, Integer> dataObject = (Map<String, Integer>) inputStreamServer.readObject();
            System.out.println("Objeto de dados recebido: " + dataObject);

            Integer op = dataObject.get("op");

            final int NULL_RESULT = -1;
            int finalResult = NULL_RESULT;


            if (op == 2) {
                int valueX = dataObject.get("x");
                int valueY = dataObject.get("y");
                int resultOp2 = op2(valueX, valueY);
                finalResult = resultOp2;
                System.out.println("Operação op2 processada");
            } else {
                Socket node01 = new Socket(this.host, 12345);
                System.out.println("Operação do tipo op1 não é avaliada para o node.");
                delegateTo(node01, dataObject);
                System.out.println("Operação delegada para um node responsável");
                int resultOp1 = receiverResultProcessamentOp2Of(node01);
                System.out.println("Resultado do processamento da operação delegada (op1) recebido");
                finalResult = resultOp1;
                node01.close();
            }
            socket.getOutputStream().write(finalResult);
            System.out.println("Resultado da operação enviada para o cliente: " + finalResult);
            socket.close();
        }
    }

    private void delegateTo(Socket socket, Map<String, Integer> dataObject) throws IOException {
        ObjectOutputStream outputStreamClient = new ObjectOutputStream(socket.getOutputStream());
        outputStreamClient.writeObject(dataObject);
    }

    private int receiverResultProcessamentOp2Of(Socket socket) throws IOException {
        return socket.getInputStream().read();
    }

    private int op2(int x, int y) {
        return (2*(x/y));
    }

}
