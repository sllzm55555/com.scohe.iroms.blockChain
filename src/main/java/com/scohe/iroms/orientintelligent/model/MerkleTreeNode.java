package com.scohe.iroms.orientintelligent.model;

import com.scohe.iroms.orientintelligent.utils.SHAUtils;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MerkleTreeNode {

    /**
     * 左子节点
     */
    private MerkleTreeNode leftNode;
    /**
     * 右子节点
     */
    private MerkleTreeNode rightNode;
    /**
     * 节点的数据
     */
    private String data;
    /**
     * 节点的哈希值
     */
    private String hash;
    /**
     * 节点名
     */
    private String name;


    public MerkleTreeNode(String data) {
        this.data = data;
        this.hash = SHAUtils.getSHA256StrHutool(data);
        this.name = "[node: " + data + "]";
    }
}
