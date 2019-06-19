package br.edu.ifpb.app;

import br.edu.ifpb.domain.User;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Loader {

    public static void main(String[] args) throws IOException {
        User user = new User("Áthila");
        long startTime = System.currentTimeMillis();

//        int iteratorMin = 100;
//        int iteratorMax = 200;

//        int iteratorMin = 1000;
//        int iteratorMax = 2000;

        int iteratorMin = 2000;
        int iteratorMax = 3000;

        for(int i=iteratorMin; i<iteratorMax; i++){
            user.setCode(i);
            Socket socket = new Socket("localhost", 12345);
            ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
            outputStream.writeObject(user);
            System.out.println("Usuário de número " + i + " enviado.");
            socket.close();
        }
        long endTime = System.currentTimeMillis();
        long seconds = (endTime - startTime) / 1000;
        System.out.println("A inserção durou " + seconds + " segundos");
    }

    //Resultados encontrados:
    // 100 inserções sem uso de threads: 11 segundos
    // 1000 inserções sem uso de threads: 164 segundos
    // 1000 inserções com uso de threads: 0 segundos

}
