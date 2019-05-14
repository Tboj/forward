package com.jtb.forward.service;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

/**
 * @auther: jtb
 * @date: 2019/1/7 16:22
 * @description:
 */
public class RecRouterTask implements Runnable {

    public static Integer sourcePort = null;
    public static Integer destPost = null;
    private ByteBuffer read1Buffer = ByteBuffer.allocate(1024);
    private ByteBuffer read2Buffer = ByteBuffer.allocate(1024);
    private static boolean rec1IsBinding = false;
    private static boolean rec2IsBinding = false;

    private static final int REPLYCLIENT1 = 10060;

    private Vector<Map<String, Long>> addrList = new Vector<>();

    @Override
    public void run() {

        DatagramChannel datagramChannel = null;
        try {
            datagramChannel = DatagramChannel.open();
            datagramChannel.configureBlocking(false);
        } catch (IOException e) {
            e.printStackTrace();
        }
        new Thread(new RecClient2()).start();

        while (! Thread.interrupted()) {
            read1Buffer.clear();
            try {
                rec1(datagramChannel);
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                Thread.sleep(2000L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void rec1(DatagramChannel datagramChannel) throws IOException {
        if (! rec1IsBinding) {
            datagramChannel.bind(new InetSocketAddress(10051));
            rec1IsBinding = true;
        }
        if (datagramChannel == null) {
            return;
        }
        InetSocketAddress socketAddress = (InetSocketAddress) datagramChannel.receive(read1Buffer);
        if (socketAddress != null) {
            int position = read1Buffer.position();
            byte[] bytes = new byte[position];
            System.arraycopy(read1Buffer.array(), 0, bytes, 0, bytes.length);
            String recClient1Msg = new String(bytes, StandardCharsets.UTF_8);
            System.out.println("rec client1: " + socketAddress.getAddress() + ": " + socketAddress.getPort() +  ": " + recClient1Msg);
            sourcePort = socketAddress.getPort();
            sendToClient2(datagramChannel, socketAddress, recClient1Msg);

            Map<String, Long> addrCacheMap = new HashMap<>();
            addrCacheMap.put(socketAddress.getPort() + ":" + recClient1Msg.split(":")[1], System.currentTimeMillis());
            addrList.add(addrCacheMap);

        }

    }

    private static void sendToClient2(DatagramChannel datagramChannel, InetSocketAddress socketAddress, String recClient1Msg) throws IOException {
        String[] recClient1MsgArr = recClient1Msg.split(":");
        String sendStr = socketAddress.getPort() + ":" + recClient1MsgArr[1];
        System.out.println("send server To 2: " + socketAddress.getAddress() + ": " + socketAddress.getPort() +  ": " + sendStr);

        datagramChannel.send(ByteBuffer.wrap(sendStr.getBytes()), new InetSocketAddress("127.0.0.1", Integer.valueOf(recClient1MsgArr[0])));
    }

    class RecClient2 implements Runnable {



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
                read2Buffer.clear();
                if (datagramChannel != null) {
                    if (! rec2IsBinding) {
                        try {
                            datagramChannel.bind(new InetSocketAddress(10052));
                            rec2IsBinding = true;
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                    try {
                        InetSocketAddress socketAddress = (InetSocketAddress) datagramChannel.receive(read2Buffer);
                        if (socketAddress != null) {
                            int position = read2Buffer.position();
                            byte[] bytes = new byte[position];
                            System.arraycopy(read2Buffer.array(), 0, bytes, 0, bytes.length);
                            String recClient2Msg = new String(bytes, StandardCharsets.UTF_8);
                            String[] recClient2MsgArr = recClient2Msg.split(":");
                            System.out.println("rec client2: " + socketAddress.getAddress() + ": " + socketAddress.getPort() +  ": " + recClient2Msg);
                            for (Map<String, Long> addrMsgMap : addrList) {

                                for(Map.Entry<String, Long> entry : addrMsgMap.entrySet()) {
                                    String inetSocketAddress = entry.getKey();
                                    if (recClient2MsgArr[0].equals(inetSocketAddress.split(":")[0])) { // recClient2MsgArr[0] 为client1端口
                                        // 匹配正确，返回客户端1
                                        datagramChannel.send(ByteBuffer.wrap(recClient2Msg.getBytes()),
                                                new InetSocketAddress("127.0.0.1", REPLYCLIENT1));
                                        System.out.println("send server to client1: " + recClient2Msg);

                                    }
                                }
                            }

                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            }
        }
    }

}
