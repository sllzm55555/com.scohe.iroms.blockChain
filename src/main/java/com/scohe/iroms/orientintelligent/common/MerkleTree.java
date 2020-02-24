package com.scohe.iroms.orientintelligent.common;

import com.scohe.iroms.orientintelligent.utils.SHAUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MerkleTree {

    private List<MerkleTreeNode> treeNodeList;
    private MerkleTreeNode rootNode;

    public MerkleTree(List<String> contents) {
        createMerkleTree(contents);
    }

    private void createMerkleTree(List<String> contents){

        if(contents.isEmpty()){
            return;
        }

        treeNodeList = new ArrayList<>();

        List<MerkleTreeNode> leafNodeList = createLeafNodeList(contents);
        treeNodeList.addAll(leafNodeList);

        List<MerkleTreeNode> parentTreeNodeList = createParentTreeNodeList(leafNodeList);
        treeNodeList.addAll(parentTreeNodeList);

        while (parentTreeNodeList.size() > 1){
            List<MerkleTreeNode> temp = createParentTreeNodeList(parentTreeNodeList);
            treeNodeList.addAll(temp);
            parentTreeNodeList = temp;
        }
        rootNode = parentTreeNodeList.get(0);
    }

    private List<MerkleTreeNode> createParentTreeNodeList(List<MerkleTreeNode> treeNodes){

        List<MerkleTreeNode> parentNodes = new ArrayList<>();

        if(treeNodes.isEmpty()){
            return parentNodes;
        }

        int size = treeNodes.size();

        for (int i = 0; i < size - 1; i += 2) {
            MerkleTreeNode parentNode = createParentTreeNode(treeNodes.get(i), treeNodes.get(i + 1));
            parentNodes.add(parentNode);
        }

        if(size % 2 != 0){
            MerkleTreeNode parentNode = createParentTreeNode(treeNodes.get(size - 1), null);
            parentNodes.add(parentNode);
        }
        return parentNodes;
    }

    private MerkleTreeNode createParentTreeNode(MerkleTreeNode leftNode, MerkleTreeNode rightNode){

        MerkleTreeNode parentNode = new MerkleTreeNode();
        parentNode.setLeftNode(leftNode);
        parentNode.setRightNode(rightNode);

        String hash = leftNode.getHash();
        String name = String.format("[单节点 %s 的父节点]", leftNode.getName());

        if(rightNode != null){
            hash = SHAUtils.getSHA256StrHutool(hash + rightNode.getHash());
            name = String.format("[%s和%s 的父节点]", leftNode.getName(), rightNode.getName());
        }

        parentNode.setHash(hash);
        parentNode.setData(hash);
        parentNode.setName(name);

        return parentNode;
    }

    private List<MerkleTreeNode> createLeafNodeList(List<String> contents){
        List<MerkleTreeNode> list = new ArrayList<>();

        if(contents.isEmpty()){
            return list;
        }

        for (String content : contents) {
            MerkleTreeNode merkleTreeNode = new MerkleTreeNode(content);
            list.add(merkleTreeNode);
        }
        return list;
    }

    public void traverseTreeNodes(){
        Collections.reverse(treeNodeList);
        MerkleTreeNode root = treeNodeList.get(0);
        traverseTreeNodes(root);
    }

    private void traverseTreeNodes(MerkleTreeNode treeNode){
        System.out.println(treeNode.getName());

        if(treeNode.getLeftNode() != null){
            traverseTreeNodes(treeNode.getLeftNode());
        }

        if(treeNode.getRightNode() != null){
            traverseTreeNodes(treeNode.getRightNode());
        }

    }

    public List<MerkleTreeNode> getTreeNodeList() {
        if(treeNodeList == null){
            return null;
        }
        Collections.reverse(treeNodeList);
        return treeNodeList;
    }

    public void setTreeNodeList(List<MerkleTreeNode> treeNodeList) {
        this.treeNodeList = treeNodeList;
    }

    public MerkleTreeNode getRootNode() {
        return rootNode;
    }

    public void setRootNode(MerkleTreeNode rootNode) {
        this.rootNode = rootNode;
    }
}
