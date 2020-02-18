package com.scohe.iroms.orientintelligent.constant;

/**
 * @author sllzm
 * @version
 * @date
 */
public enum VoteEnum {

    PRE_PREPARE("节点将自己生成区块", 100),
    PREPARE("节点收到请求生成区块的信息，进入准备状态，并对外广播该状态", 200),
    COMMIT("每个节点收到超过2f + 1个不同节点的commit消息后，认为该区块已经达成一致，即进入commit状态，并将其持久化到区块链数据库中", 400);

    private String msg;
    private int code;

    public static VoteEnum find(int code){
        for (VoteEnum ve : VoteEnum.values()) {
            if(ve.code == code){
                return ve;
            }
        }
        return null;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    VoteEnum(String message, int code) {
        this.msg = message;
        this.code = code;
    }
}
