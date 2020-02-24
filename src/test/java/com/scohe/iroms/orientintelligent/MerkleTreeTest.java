package com.scohe.iroms.orientintelligent;

import com.scohe.iroms.orientintelligent.common.MerkleTree;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.List;

public class MerkleTreeTest {

    @Test
    public void testMerkleTree(){
        List<String> content = Arrays.asList("密码学", "p2p网络协议", "区块存储", "共识算法");

        MerkleTree merkleTree = new MerkleTree(content);
        merkleTree.traverseTreeNodes();

    }

    @Test
    public void testMerkleTree2(){
        List<String> content = Arrays.asList("密码学", "p2p网络协议", "区块存储", "共识算法", "币设计");

        MerkleTree merkleTree = new MerkleTree(content);
        merkleTree.traverseTreeNodes();

    }
}
