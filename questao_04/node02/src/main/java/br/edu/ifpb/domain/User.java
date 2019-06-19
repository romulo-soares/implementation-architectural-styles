package br.edu.ifpb.domain;

import java.io.Serializable;

public class User implements Serializable {

    private static final long serialVersionUID = 6529685098267757690L;

    private int code;
    private String nome;

    public User() {}

    public User(int code, String nome) {
        this.nome = nome;
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    @Override
    public String toString() {
        return "User{" +
                "code=" + code +
                ", nome='" + nome + '\'' +
                '}';
    }

}
