package br.edu.ifpb.app;

import br.edu.ifpb.dao.UsuarioDaoMySQL;
import br.edu.ifpb.dao.UsuarioDaoPostgreSQL;
import br.edu.ifpb.domain.User;
import br.edu.ifpb.thread.UserInserter;

import java.io.IOException;

import java.io.ObjectInputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Loader {

    public static void main(String[] args) throws IOException, SQLException, ClassNotFoundException {
        new Loader("localhost", 12345).executa();
    }

    private String host;
    private int porta;
    private UsuarioDaoPostgreSQL usuarioDaoPostgreSQL;
    private UsuarioDaoMySQL usuarioDaoMySQL;

    public Loader (String host, int porta) {
        this.host = host;
        this.porta = porta;
        this.usuarioDaoPostgreSQL = new UsuarioDaoPostgreSQL();
        this.usuarioDaoMySQL = new UsuarioDaoMySQL();
    }

    public void executa() throws IOException, SQLException, ClassNotFoundException {
        ServerSocket serverSocket = new ServerSocket();
        serverSocket.bind(new InetSocketAddress(this.host, this.porta));
        System.out.println("Aguardando conexão na porta 12345");

        //Para ver sem thread executar linha abaixo
//        insertionNoThread(serverSocket);
        //Para ver com thread executar linha abaixo
        insertionWithThread(serverSocket);
    }

    private void insertionNoThread(ServerSocket serverSocket) throws IOException, SQLException, ClassNotFoundException {
        while(true){
            Socket socket = serverSocket.accept();
            System.out.println("Cliente conectado");

            ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
            User user = (User) objectInputStream.readObject();
            System.out.println("Usuario recebido para cadastro: " + user);

            this.usuarioDaoPostgreSQL.save(user);
            this.usuarioDaoMySQL.save(user);
            socket.close();
        }
    }

    private void insertionWithThread(ServerSocket serverSocket) throws IOException {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        while(true){
            Socket socket = serverSocket.accept();
            System.out.println("Cliente conectado");
            executorService.execute(new UserInserter(socket, this.usuarioDaoMySQL, this.usuarioDaoPostgreSQL));
        }
    }

}
