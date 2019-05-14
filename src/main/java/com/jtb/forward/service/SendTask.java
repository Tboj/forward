package com.jtb.forward.service;

import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.charset.StandardCharsets;

/**
 * @auther: jtb
 * @date: 2019/1/7 16:22
 * @description:
 */
@Service
public class SendTask {

    public static Integer port = 10051; // web传
    public static Integer destPort = 10061; // web传
    public static String msg = null;
    private ByteBuffer readBuffer = ByteBuffer.allocate(1024);

    private static final int RECREPLYCLIENT1 = 10060;
    private static boolean rec1IsBinding = false;
    private static String replyMsg = null;

    public String sendAndRec() throws IOException, InterruptedException {
        replyMsg = null;
        // 接受server msg 线程
        new Thread(() -> {
            DatagramChannel datagramChannel = null;
            try {
                datagramChannel = DatagramChannel.open();
                datagramChannel.configureBlocking(false);
            } catch (IOException e) {
                e.printStackTrace();
            }
            while (! Thread.interrupted()) {
                if (! rec1IsBinding) {
                    try {
                        assert datagramChannel != null;
                        datagramChannel.bind(new InetSocketAddress(RECREPLYCLIENT1));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    rec1IsBinding = true;
                }
                readBuffer.clear();
                try {
                    // 接收server返回的报文
                    InetSocketAddress socketAddress = (InetSocketAddress) datagramChannel.receive(readBuffer);
                    if (socketAddress != null) {
                        int position = readBuffer.position();
                        byte[] bytes = new byte[position];
                        System.arraycopy(readBuffer.array(), 0, bytes, 0, bytes.length);
                        String msg = new String(bytes, StandardCharsets.UTF_8);
                        System.out.println("client1 rec: " + socketAddress.getAddress() + ": " + socketAddress.getPort() +  ": " + msg);
                        replyMsg = msg.split(":")[1];
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }).start();

        if (msg != null && port != null) {
            try {
                // 发送至指定server
                DatagramChannel datagramChannel = DatagramChannel.open();
                datagramChannel.configureBlocking(false);
                datagramChannel.send(ByteBuffer.wrap(msg.getBytes()), new InetSocketAddress("127.0.0.1", port));

                System.out.println("client1,send: " + msg);

                msg = null;
                port = null;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (replyMsg != null) {
            return replyMsg;
        } else {
            Thread.sleep(2000L);
            if (replyMsg != null) {
                return replyMsg;
            } else {
                return "error";
            }
        }
    }

    public static void main(String[] args) throws IOException {


    }

}
