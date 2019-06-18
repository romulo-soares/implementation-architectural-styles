package br.edu.ifpb.main;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.util.Random;

/**
 * Esta aplicação é uma aplicação cliente e tem por finalidade gerar
 * dois numéros aleátorios para ser enviado ao node03 através do node02
 */
public class Cliente {

    public static void main(String[] args) throws UnknownHostException, IOException {
        new Cliente("localhost", 12345).executa(); // dispara cliente
    }

    private String host;
    private int porta;

    public Cliente (String host, int porta) {
        this.host = host;
        this.porta = porta;
    }

    /**
     * Gera dois números aleatórios
     * @return um array de int contendo os números aleatórios gerados
     */
    private int[] generateTwoRandom() {
        Random random = new Random();
        int num1 = random.nextInt(101);
        int num2 = random.nextInt(101);
        int[] numbers = {num1,num2};
        return numbers;
    }

    public void executa() throws UnknownHostException, IOException {
        Socket cliente = new Socket(this.host, this.porta);
        System.out.println("O cliente se conectou ao servidor!");

        int[] numbersRandom = generateTwoRandom();
        byte[] messageResponseNode02 = convertIntArrayToByteArray(numbersRandom);
        byte[] messageReceivedNode02 = new byte[8];

        cliente.getOutputStream().write(messageResponseNode02);
        System.out.println("Mensagem enviado apara node02");

        cliente.getInputStream().read(messageReceivedNode02);
        int result = convertByteArrayToInt(messageReceivedNode02);

        if (result == 0) System.out.println("Os números gerados foram iguais, resposta: " + result);
        else System.out.println("O resultado da equação recebida de node02: " + result);

        cliente.close();
    }

    /**
     * Converte um array de int em um array de byte
     * @param array array de inteiros para a conversão
     * @return array de bytes correpondentes
     */
    private byte[] convertIntArrayToByteArray(int[] array) {
        return new byte[]{(byte) array[0], (byte) array[1]};
    }

    private int convertByteArrayToInt(byte[] intBytes){
        ByteBuffer byteBuffer = ByteBuffer.wrap(intBytes);
        return byteBuffer.getInt();
    }

}
