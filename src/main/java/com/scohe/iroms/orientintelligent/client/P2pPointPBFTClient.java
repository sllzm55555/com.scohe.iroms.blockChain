package com.scohe.iroms.orientintelligent.client;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Strings;
import com.scohe.iroms.orientintelligent.constant.VoteEnum;
import com.scohe.iroms.orientintelligent.model.VoteInfo;
import com.scohe.iroms.orientintelligent.utils.SimpleMerkleTree;
import lombok.extern.slf4j.Slf4j;
import org.java_websocket.WebSocket;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
public class P2pPointPBFTClient {

    private String wsUrl = "ws://localhost:9999/";

    //所有客户端WebSocket的连接池缓存
    private List<WebSocket> localSockets = new ArrayList<>();

    @PostConstruct
    @Order(2)
    public void connectPeer(){

        try {
            WebSocketClient webSocketClient = new WebSocketClient(new URI(wsUrl)) {
                @Override
                public void onOpen(ServerHandshake serverHandshake) {
                    sendMessage(this, "成都客户端成功创建");
                    localSockets.add(this);
                }

                @Override
                public void onMessage(String msg) {
                    log.info(String.format("成都客户端成功收到服务器发送的消息[%s]", msg));

                    //如果收到的不是json化数据，则说明不是PBFT阶段
                    if(!msg.startsWith("{")){
                        return;
                    }

                    //如果收到的是JSON数据，则说明已经是PBFT阶段
                    JSONObject jsonObject = JSON.parseObject(msg);

                    if(!jsonObject.containsKey("code")){
                        log.info("成都客户端收到非法json数据");
                        return;
                    }

                    int code = jsonObject.getIntValue("code");

                    if(code == VoteEnum.PRE_PREPARE.getCode()){

                        VoteInfo voteInfo = JSON.parseObject(msg, VoteInfo.class);

                        if(!voteInfo.getHash().equals(SimpleMerkleTree.getTreeNodeHash(voteInfo.getList()))){
                            log.info("成都客户端收到非法json数据 hash错误");
                            return;
                        }

                        //校验成功，发送下一阶段数据
                        VoteInfo vi = creatVoteInfo(VoteEnum.PREPARE);

                        sendMessage(this, JSON.toJSONString(vi));
                        log.info("成都客户端发送PBFT数据：" + JSON.toJSONString(vi));
                        return;

                    }

                    if(code == VoteEnum.COMMIT.getCode()){
                        VoteInfo voteInfo = JSON.parseObject(msg, VoteInfo.class);

                        if(!voteInfo.getHash().equals(SimpleMerkleTree.getTreeNodeHash(voteInfo.getList()))){
                            log.info("成都客户端收到非法json数据 hash错误");
                            return;
                        }

                        //校验成功，检验节点确认个数是否有效
                        if(getConnecttedNodeCount() >= getLeastNodeCount()){
                            sendMessage(this, "成都客户端区块数据开始入库啦");
                            log.info("成都客户端区块数据开始入库啦");
                        }
                    }
                }

                @Override
                public void onClose(int i, String s, boolean b) {
                    log.info("成都客户端关闭");
                    localSockets.remove(this);
                }

                @Override
                public void onError(Exception e) {
                    log.info("成都客户端出错");
                    localSockets.remove(this);
                }
            };

            webSocketClient.connect();

        } catch (URISyntaxException e) {
            log.error("成都客户端连接错误: " + e.getMessage());
        }

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


    private int getConnecttedNodeCount(){
        return localSockets.size();
    }

    private int getLeastNodeCount(){

        //恶意节点数量为n时， 总节点数量必须大于3n + 1

        return 1;

    }
}
