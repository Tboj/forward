package com.jtb.forward.service;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.charset.StandardCharsets;

/**
 * @auther: jtb
 * @date: 2019/1/7 17:19
 * @description:
 */
public class ClientTask implements Runnable {

    private ByteBuffer readServerBuffer = ByteBuffer.allocate(1024);
    private static boolean recServerIsBinding = false;
    private static final int REPLYSERVERTOCLIENT1 = 10061;

    @Override
    public void run() {
        DatagramChannel datagramChannel = null;
        try {
            datagramChannel = DatagramChannel.open();
            datagramChannel.configureBlocking(false);
        } catch (IOException e) {
            e.printStackTrace();
        }

        while (! Thread.interrupted()) {
            readServerBuffer.clear();

            if (datagramChannel == null) {
                return;
            }
            try {
                if (!recServerIsBinding) {
                    datagramChannel.bind(new InetSocketAddress(REPLYSERVERTOCLIENT1));
                    recServerIsBinding = true;
                }
                InetSocketAddress socketAddress = (InetSocketAddress) datagramChannel.receive(readServerBuffer);
                if (socketAddress != null) {
                    int position = readServerBuffer.position();
                    byte[] bytes = new byte[position];
                    System.arraycopy(readServerBuffer.array(), 0, bytes, 0, bytes.length);
                    String recServerByClient1Msg = new String(bytes, StandardCharsets.UTF_8);
                    System.out.println("rec server by client2: " + socketAddress.getAddress() + ": " + socketAddress.getPort() +  ": " + recServerByClient1Msg);

                    String sendServerToClient1 = recServerByClient1Msg.split(":")[0] + ":" + "B" + ":" +  REPLYSERVERTOCLIENT1;
                    datagramChannel.send(ByteBuffer.wrap(sendServerToClient1.getBytes()), new InetSocketAddress("127.0.0.1", 10052));
                    System.out.println("send server by client2: " + recServerByClient1Msg);

                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
