package client;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

/**
 * Клиент для запроса строк
 */
public class Client {

    public static void main(String[] args) throws IOException {
        //устанавливаем соединение
        InetSocketAddress socketAddress = new InetSocketAddress("127.0.0.1", 12345);
        final SocketChannel socketChannel = SocketChannel.open();
        socketChannel.connect(socketAddress);


        try (Scanner scanner = new Scanner(System.in)) {
            final ByteBuffer inputBuffer = ByteBuffer.allocate((2 << 10));
            String msg;
            // пока не введем end читаем и отправляем на сервер сообщения
            while (true) {
                System.out.println("Enter string...");
                msg = scanner.nextLine().trim() + "\r\n";
                if ("end\r\n".equals(msg))
                    break;
                socketChannel.write(
                        ByteBuffer.wrap(
                                msg.getBytes(StandardCharsets.UTF_8)));
                // ждем 1 с
                Thread.sleep(1000);
                // читаем ответ сервера
                int bytesCount = socketChannel.read(inputBuffer);
                //выводим результат на экран
                System.out.println("Result: " +
                        new String(inputBuffer.array(), 0, bytesCount, StandardCharsets.UTF_8).trim());
                inputBuffer.clear();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            //закрываем соединение
            socketChannel.close();
        }
    }

}
