package br.edu.ifpb.main;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class Cliente {

    public static void main(String[] args) {

        try {
            new Cliente("localhost", 12345).executa(); // dispara cliente
        } catch (IOException t1) {
            System.out.println("Não foi possível comunicação com node01");
            try {
                new Cliente("localhost", 12340).executa(); // dispara cliente
            } catch (IOException t2) {
                System.out.println("Não foi possível comunicação com node03");
                try {
                    new Cliente("localhost", 12346).executa(); // dispara cliente
                } catch (IOException t3) {
                    System.out.println("Não foi possível comunicação com node02");
                }
            }
        }

    }

    private String host;
    private int porta;

    public Cliente (String host, int porta) {
        this.host = host;
        this.porta = porta;
    }

    public void executa() throws UnknownHostException, IOException {

        Random random = new Random();
        int x = random.nextInt(10) + 1; //+ 1 para evitar zero
        int y = random.nextInt(10) + 1; //+ 1 para evitar zero
        int op = random.nextInt(2) + 1; //gera um identificador de operação (1|--|2)

        Map<String, Integer> dataObject = new HashMap<>(); //objeto de transição entre nodes
        dataObject.put("x", x);
        dataObject.put("y", y);
        dataObject.put("op", op);

        Socket cliente = new Socket(this.host, this.porta);
        System.out.println("O cliente se conectou ao servidor!");

        ObjectOutputStream outputStream = new ObjectOutputStream(cliente.getOutputStream());
        outputStream.writeObject(dataObject);

        int result = cliente.getInputStream().read();
        System.out.println("Resposta processada nos nodes recebida.");

        System.out.println("Equação resolvida com resultado: " + result);

        outputStream.flush();
        outputStream.close();
        cliente.close();
    }

}
