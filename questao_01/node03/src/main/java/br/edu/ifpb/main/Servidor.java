package br.edu.ifpb.main;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;

/**
 * Aplicação servidora que recebe os parâmetros (números) do node01
 * através do node02 e resolve a equação submetendo o resutado para
 * node01 através do ponto de comunicação node02
 */
public class Servidor {

    public static void main(String[] args) throws IOException {
        new Servidor("localhost", 12340).executa(); // inicia o servidor
    }

    private int porta;
    private String host;

    public Servidor (String host, int porta) throws IOException {
        this.host = host;
        this.porta = porta;
    }

    public void executa () throws IOException {
        ServerSocket server = new ServerSocket();
        server.bind(new InetSocketAddress(this.host,this.porta));
        System.out.println("Aguardando conexão na porta 12340...");

        byte[] messageReceivedNode02 = new byte[8];
        int[] numbers;

        while(true){
            Socket socketAdjacentDireitaNode02 = server.accept();
            socketAdjacentDireitaNode02.getInputStream().read(messageReceivedNode02);
            numbers = convertByteArrayToIntArray(messageReceivedNode02);
            System.out.println("Numeros recebidos do node02: {" + numbers[0] + ", " + numbers[1] + "}.");

            int resultEquation = (int) (Math.pow(numbers[0], numbers[0]) + Math.pow(numbers[1], numbers[1]));
            socketAdjacentDireitaNode02.getOutputStream().write(intToBytes(resultEquation));
            System.out.println("Resultado da equação enviado para node02: " + resultEquation);

            socketAdjacentDireitaNode02.close();
        }
    }

    /**
     * Converte um array de byte em array de int
     * @param entrada array de byte para a conversão
     * @return array de int correspondente
     */
    private int[] convertByteArrayToIntArray(byte[] entrada) {
        int[] sin = new int[entrada.length];
        for(int i =0; i<entrada.length; i++) {
            sin[i] = entrada[i];
        }
        return sin;
    }

    /**
     * Converte um inteiro em um array de byte
     * @param i número a ser convertido
     * @return array de byte correspondente
     */
    private byte[] intToBytes(int i) {
        ByteBuffer bb = ByteBuffer.allocate(4);
        bb.putInt(i);
        return bb.array();
    }

    /**
     * Converte um array de byte em um inteiro
     * @param intBytes array de byte a ser convertido
     * @return array de byte correspondente
     */
    private int convertByteArrayToInt(byte[] intBytes){
        ByteBuffer byteBuffer = ByteBuffer.wrap(intBytes);
        return byteBuffer.getInt();
    }

}

