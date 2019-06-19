package br.edu.ifpb.thread;

import br.edu.ifpb.dao.UsuarioDaoMySQL;
import br.edu.ifpb.dao.UsuarioDaoPostgreSQL;
import br.edu.ifpb.domain.User;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.sql.SQLException;

public class UserInserter implements Runnable {

    private final Socket socket;
    private final UsuarioDaoMySQL usuarioDaoMySQL;
    private final UsuarioDaoPostgreSQL usuarioDaoPostgreSQL;

    public UserInserter(Socket socket, UsuarioDaoMySQL usuarioDaoMySQL, UsuarioDaoPostgreSQL usuarioDaoPostgreSQL) {
        this.socket = socket;
        this.usuarioDaoMySQL = usuarioDaoMySQL;
        this.usuarioDaoPostgreSQL = usuarioDaoPostgreSQL;
    }

    public void run() {
        try {
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
            User user = (User) in.readObject();
            System.out.println("Usuario recebido para cadastro: " + user);
            usuarioDaoPostgreSQL.save(user);
            usuarioDaoMySQL.save(user);
            socket.close();
        } catch (ClassNotFoundException | IOException | SQLException e) {
            e.printStackTrace();
        }
    }

}
