package br.edu.ifpb.main;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class Loader {

    public static void main(String[] args) throws IOException {
        new Loader("localhost", 12303).executa();
    }

    private String host;
    private int porta;

    public Loader (String host, int porta) {
        this.host = host;
        this.porta = porta;
    }

    public void executa() throws IOException {
        Random random = new Random();
        int x = random.nextInt(10) + 1; //+ 1 para evitar zero
        int y = random.nextInt(10) + 1; //+ 1 para evitar zero
        int node = random.nextInt(2) + 1; //gera um identificador de operação (1|--|2)

        Map<String, Integer> dataObject = new HashMap<>(); //objeto de transição entre nodes
        dataObject.put("x", x);
        dataObject.put("y", y);
        dataObject.put("node", node);

        Socket cliente = new Socket(this.host, this.porta);
        System.out.println("O cliente se conectou ao servidor!");

        ObjectOutputStream outputStream = new ObjectOutputStream(cliente.getOutputStream());
        outputStream.writeObject(dataObject);
        System.out.println("Objeto de dados enviado: " + dataObject);
        System.out.println("Mensagem enviada para node responsável");

        ObjectInputStream inputStream = new ObjectInputStream(cliente.getInputStream());
        Integer result = inputStream.readInt();
        System.out.println("Resultado da operação recebido");
        System.out.println("O resultado é: " + result);

    }

}
