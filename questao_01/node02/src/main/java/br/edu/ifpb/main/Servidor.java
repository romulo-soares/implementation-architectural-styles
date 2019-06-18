package br.edu.ifpb.main;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;

/**
 * Aplicação servidora como ponto central de comunicação entre node01 e node02
 */
public class Servidor {

    public static void main(String[] args) throws IOException {
        new Servidor("localhost", 12345).executa(); // inicia o servidor
    }

    private int porta;
    private String host;

    public Servidor (String host, int porta) throws IOException {
        this.host = host;
        this.porta = porta;
    }

    public void executa () throws IOException {
        ServerSocket server = new ServerSocket();
        server.bind(new InetSocketAddress(this.host, this.porta));
        System.out.println("Aguardando conexão na porta 12345...");

        byte[] messageReceivedNode01 = new byte[8];
        byte[] messageReceivedNode03 = new byte[8];
        int[] numbers;

        while (true) {
            Socket socketAdjacenteDireitaNode01 = server.accept(); // aceita um cliente (node01)
            System.out.println("Nova conexão com o cliente " + server.getInetAddress().getHostAddress());
            socketAdjacenteDireitaNode01.getInputStream().read(messageReceivedNode01);
            numbers = convertByteArrayToIntArray(messageReceivedNode01);
            System.out.println("Numeros recebidos do node01: {" + numbers[0] + ", " + numbers[1] + "}.");

            Socket socketAdjacenteEsquerdaNode03 = new Socket("localhost",12340);

            if(numbers[0] != numbers[1]){
                socketAdjacenteEsquerdaNode03.getOutputStream().write(convertIntArrayToByteArray(numbers));
                System.out.println("Números enviados para o node03");
            } else {
                socketAdjacenteDireitaNode01.getOutputStream().write(intToBytes(0));
                System.out.println("Os números são iguais. Array de número zero enviado para node01");
            }

            socketAdjacenteEsquerdaNode03.getInputStream().read(messageReceivedNode03);
            socketAdjacenteDireitaNode01.getOutputStream().write(messageReceivedNode03);
            socketAdjacenteEsquerdaNode03.close();
            socketAdjacenteDireitaNode01.close();
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
     * Converte um array de int em um array de byte
     * @param array array de inteiros para a conversão
     * @return array de bytes correspondente
     */
    private byte[] convertIntArrayToByteArray(int[] array) {
        return new byte[]{(byte) array[0], (byte) array[1]};
    }

    /**
     * Converte um inteiro para aray de byte
     * @param i número inteiro a ser convertido
     * @return array de byte correspondente
     */
    private byte[] intToBytes(int i) {
        ByteBuffer bb = ByteBuffer.allocate(4);
        bb.putInt(i);
        return bb.array();
    }
}
