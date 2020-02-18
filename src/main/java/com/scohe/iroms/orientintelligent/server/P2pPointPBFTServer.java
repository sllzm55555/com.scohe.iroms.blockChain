package com.scohe.iroms.orientintelligent.server;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Strings;
import com.scohe.iroms.orientintelligent.constant.VoteEnum;
import com.scohe.iroms.orientintelligent.model.VoteInfo;
import com.scohe.iroms.orientintelligent.utils.SimpleMerkleTree;
import lombok.extern.slf4j.Slf4j;
import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;


@Component
@Slf4j
public class P2pPointPBFTServer {

    private int port = 9999;

    //所有连结到服务器的webSocket缓存器
    private List<WebSocket> localSockets = new ArrayList<>();

    @PostConstruct
    @Order(1)
    public void initServer(){
        final WebSocketServer webSocketServer = new WebSocketServer(new InetSocketAddress(port)) {
            @Override
            public void onOpen(WebSocket webSocket, ClientHandshake clientHandshake) {

                sendMessage(webSocket, "成都服务器成功创建连结");
                localSockets.add(webSocket);
            }

            @Override
            public void onClose(WebSocket webSocket, int i, String s, boolean b) {

                log.info(String.format("服务器[%s]断开连接", webSocket.getRemoteSocketAddress()));
                localSockets.remove(webSocket);
            }

            @Override
            public void onMessage(WebSocket webSocket, String msg) {
                log.info(String.format("成都服务器收到客户端消息[%s]", msg));

                //收到入库的消息则不再发送
                if(msg.endsWith("入库啦")){
                    return;
                }
                //如果收到的不是json化数据，说明双方在建立连接的过程中
                //发起投票
                if(!msg.startsWith("{")){
                    VoteInfo vi = creatVoteInfo(VoteEnum.PRE_PREPARE);
                    sendMessage(webSocket, JSON.toJSONString(vi));
                    log.info("成都服务器收到客户端的PBFT消息：" + JSON.toJSONString(vi));
                    return;
                }

                //如果收到的是JSON数据，表明已经进入到了PBFT投票阶段
                JSONObject jsonObject = JSON.parseObject(msg);

                if(!jsonObject.containsKey("code")){
                    log.info("成都客户端收到非法JSON数据");
                }

                int code = jsonObject.getIntValue("code");

                if(code == VoteEnum.PREPARE.getCode()){

                    VoteInfo voteInfo = JSON.parseObject(msg, VoteInfo.class);
                    if(!voteInfo.getHash().equals(SimpleMerkleTree.getTreeNodeHash(voteInfo.getList()))){
                        log.info("成都服务器收到错误的JSON化数据，hash不匹配");
                        return;
                    }

                    //校验成功，发送下一个阶段的数据
                    VoteInfo vi = creatVoteInfo(VoteEnum.COMMIT);
                    sendMessage(webSocket, JSON.toJSONString(vi));
                    log.info("成都服务器向客户端发送PBFT消息：" + JSON.toJSONString(vi));
                }
            }

            @Override
            public void onError(WebSocket webSocket, Exception e) {
                log.info(String.format("服务器[%s]连接出现错误", webSocket.getRemoteSocketAddress()));
                e.printStackTrace();
                localSockets.remove(webSocket);
            }

            @Override
            public void onStart() {
                log.info("成都服务器的webSocket端启动");
            }
        };
        webSocketServer.start();
        log.info(String.format("服务端开始监听端口：%s", port));
    }

    public void sendMessage(WebSocket webSocket, String message){
        log.info(String.format("发送给[%s]的消息是[%s]", webSocket.getRemoteSocketAddress().getPort(), message));
        webSocket.send(message);
    }

    public void broadcastMessage(String message){
        if(localSockets.isEmpty() || Strings.isNullOrEmpty(message)){
            return;
        }
        log.info("正在向所有客户端广播消息...");
        for (WebSocket localSocket : localSockets) {
            sendMessage(localSocket, message);
        }
        log.info("广播结束");

    }




    public List<WebSocket> getLocalSockets() {
        return localSockets;
    }

    public void setLocalSockets(List<WebSocket> localSockets) {
        this.localSockets = localSockets;
    }

    private VoteInfo creatVoteInfo(VoteEnum voteEnum){
        VoteInfo voteInfo = new VoteInfo();

        List<String> list = new ArrayList<>();
        list.add("区块链");
        list.add("密码学");
        list.add("共识算法");
        list.add("币设计");

        voteInfo.setCode(voteEnum.getCode());
        voteInfo.setList(list);
        voteInfo.setHash(SimpleMerkleTree.getTreeNodeHash(list));

        return voteInfo;
    }
}
