package com.nowcoder.service;

import org.apache.commons.lang.CharUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;


/**
 * Created by ZGH on 2017/4/24.
 */
@Service
public class SensitiveService implements InitializingBean{

    private static final Logger logger = LoggerFactory.getLogger(SensitiveService.class);

    @Override
    public void afterPropertiesSet() throws Exception {
        rootNode = new TrieNode();
        try{
            InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("SensitiveWords.txt");
            InputStreamReader read = new InputStreamReader(is);
            BufferedReader bufferedReader = new BufferedReader(read);
            String lineTxt;
            while ((lineTxt = bufferedReader.readLine()) != null){
                addWord(lineTxt.trim());
            }
            read.close();

        }catch (Exception e){

            logger.error("添加敏感词失败",e.getMessage());
        }
    }

    private class TrieNode{
        //
        private boolean end = false;
        //
        Map<Character, TrieNode> subNodes = new HashMap<Character, TrieNode>();

        public void addSubNode(Character key, TrieNode node){
            subNodes.put(key, node);

        }
        TrieNode getSubNode(Character key){
            return subNodes.get(key);
        }

        boolean isKeyWordEnd(){
            return end;
        }

        void setKeyWordEnd(boolean end){
            this.end = end;
        }

    }

    //增加关键词
    private void addWord(String lineTxt){
        TrieNode tmpNode = rootNode;

        for (int i=0; i<lineTxt.length(); ++i){
            Character c = lineTxt.charAt(i);
            TrieNode node = tmpNode.getSubNode(c);
            if (node == null){
                node = new TrieNode();
                tmpNode.addSubNode(c, node);
            }
            tmpNode = node;

            if (i == lineTxt.length()-1){
                tmpNode.setKeyWordEnd(true);
            }
        }
    }

    //判断字符
    public boolean isSymbol(char c){
        int ic = (int)c;
        return !CharUtils.isAsciiAlphanumeric(c) && ((ic<0x2E80)||(ic>0x9FFF)) ;
    }


    private TrieNode rootNode = new TrieNode();

    //过滤关键词
    public String filter(String text){
        if (StringUtils.isBlank(text)){
            return text;
        }
        StringBuilder result = new StringBuilder();
        String replacement = "***";
        TrieNode tmpNode = rootNode;
        int begin = 0;
        int position = 0;
        while (position < text.length()){
            char c = text.charAt(position);
            if (isSymbol(c)){
                if (tmpNode == rootNode) {
                    result.append(c);
                    ++begin;
                }
                ++position;
                continue;
            }
            tmpNode = tmpNode.getSubNode(c);
            if (tmpNode == null){
                result.append(text.charAt(position));
                position = position+1;
                begin = position;
                tmpNode = rootNode;
            }else if (tmpNode.isKeyWordEnd()){
                result.append(replacement);
                position = position+1;
                begin = position;
                tmpNode = rootNode;
            }else {
                ++position;
            }
        }
        result.append(text.substring(begin));
        return result.toString();
    }

    public static void main(String[] args){
       SensitiveService  s = new SensitiveService();
        s.addWord("word");
        s.addWord("暴力");
        s.addWord("赌博");
        System.out.println(s.filter("暴暴暴暴暴暴暴暴暴暴力^&情").toString());

    }


}
