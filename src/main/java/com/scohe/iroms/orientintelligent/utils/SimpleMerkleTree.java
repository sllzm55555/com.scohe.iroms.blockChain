package com.scohe.iroms.orientintelligent.utils;

import java.util.List;

/**
 * @author sllzm
 * @date 2020/2/18
 */
public class SimpleMerkleTree {

    public static String getTreeNodeHash(List<String> list){
        return new MerkleTree(list).getRootNode().getHash();
    }
}
