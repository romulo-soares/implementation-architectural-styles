package br.edu.ifpb.main;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;

public class Loader {

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        new Loader(12304).executa();
    }

    private int porta;

    public Loader (int porta) {
        this.porta = porta;
    }

    public void executa() throws IOException, ClassNotFoundException {

        ServerSocket server = new ServerSocket(this.porta);
        System.out.println("Aguardando comexão na porta 12304");

        while (true) {
            Socket accept = server.accept();
            System.out.println("Cliente conectado");

            ObjectInputStream inputStream = new ObjectInputStream(accept.getInputStream());
            Map<String, Integer> dataObject = (Map<String, Integer>) inputStream.readObject();

            System.out.println("Objeto de dados recebido: " + dataObject);

            Integer x = dataObject.get("x");
            Integer y = dataObject.get("y");
            Integer node = dataObject.get("node");
            final Integer NULL_RESULT = -1;
            Integer result = NULL_RESULT;

            if (node == 2) result = sum(x, y);
            else if (node == 3) result = diff(x, y);

            System.out.println("Operação executada com resultado: " + result);

            ObjectOutputStream outputStream = new ObjectOutputStream(accept.getOutputStream());
            outputStream.writeInt(result);
            System.out.println("Resultado enviado para o cliente");
            outputStream.flush();
            inputStream.close();
            outputStream.close();
        }
    }

    private Integer sum(Integer x, Integer y) {
        return (x + y);
    }

    private Integer diff(Integer x, Integer y) {
        return (x -y);
    }

}
