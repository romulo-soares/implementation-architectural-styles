package br.edu.ifpb.dao;

import br.edu.ifpb.domain.User;
import br.edu.ifpb.confactory.ConFactoryPostgreSQL;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class UsuarioDaoPostgreSQL {

    public void save(User user) throws SQLException {
        Connection con = null;
        try{
            con = ConFactoryPostgreSQL.getConnectionPostgres();
            String sql = "INSERT INTO tb_user (code,nome) VALUES (?,?)";
            PreparedStatement statement = con.prepareStatement(sql);
            statement.setInt(1, user.getCode());
            statement.setString(2, user.getNome());
            statement.executeUpdate();
        }finally {
            if(con != null) con.close();
        }
    }

}
