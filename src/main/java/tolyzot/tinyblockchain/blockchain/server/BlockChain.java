package tolyzot.tinyblockchain.blockchain.server;

import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import tolyzot.tinyblockchain.blockchain.model.Block;
import tolyzot.tinyblockchain.blockchain.model.Transaction;
import tolyzot.tinyblockchain.blockchain.utils.CryptoUtils;
import tolyzot.tinyblockchain.blockchain.utils.HttpUtils;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class BlockChain {
    private List<Block> chain;
    private List<Transaction> current_transactions;
    private Set<String> nodes;

    public BlockChain(){
        this.chain = new ArrayList<>();
        this.current_transactions = new ArrayList<>();
        this.nodes = new HashSet<>();
    }

    public Block addBlock(Integer proof){
        String previousHash;
        if (this.chain.size() == 0){
            previousHash = "1";
        } else {
            previousHash = calculateHash(this.chain.get(this.chain.size() - 1));
        }

        Block block = new Block(this.chain.size(), previousHash, this.current_transactions, proof);
        this.chain.add(block);
        this.current_transactions = new ArrayList<>();

        return block;
    }

    public Block getBlock(Integer index){
        if (index < 0 || index >= this.chain.size()){
            throw new RuntimeException("out of index range:" + index);
        }

        return this.chain.get(index);
    }

    public Block getLastBlock(){
        if (this.chain.size() == 0)
            return null;
        return this.chain.get(this.chain.size() - 1);
    }

    public Integer addTransaction(String senderHash, String receiverHash, Double amount){
        this.current_transactions.add(new Transaction(senderHash, receiverHash, amount));

        return this.chain.size();
    }

    public Integer mine(Integer previousNonce){
        int nonce = 1;
        while (!isProofOfWork(previousNonce, nonce)){
            nonce ++;
        }
        return nonce;
    }

    public boolean validChain(List<Block> chain){
        Block previousBlock = chain.get(0);
        int idx = 1;

        while (idx < chain.size()){
            Block block = chain.get(idx);

            if (!block.getPreviousHash().equals(calculateHash(previousBlock))){
                return false;
            }

            if (!isProofOfWork(previousBlock.getNonce(), block.getNonce())){
                return false;
            }

            previousBlock = block;
            idx ++;
        }

        return true;
    }

    public boolean longestChainRule(){
        Set<String> tempNodes = this.nodes;
        List<Block> longestBlockchain = null;

        int maxLength = this.chain.size();

        for(String nodeAddress: tempNodes){
            List<Block> blockchain = HttpUtils.getBlockchainByRequest(nodeAddress+"/chain");
            if (blockchain != null){
                if (blockchain.size() > maxLength && validChain(blockchain)){
                    maxLength = blockchain.size();
                    longestBlockchain = blockchain;
                }
            }
        }

        if (longestBlockchain != null){
            this.chain = longestBlockchain;
            return true;
        }

        return false;
    }

    public List<Block> getChain(){
        return this.chain;
    }

    public Set<String> getNodes(){
        return this.nodes;
    }

    public void registerNode(String address){
        this.nodes.add(address);
    }

    public static String calculateHash(Block block){
        String jsonString = JSONObject.toJSONString(block);
        return CryptoUtils.SHA256(jsonString);
    }

    public static boolean isProofOfWork(Integer previousNonce, Integer nonce){
        int proof = previousNonce + nonce;
        String hash = CryptoUtils.SHA256(""+proof);
        return hash.substring(0, 4).equals("0000");
    }

}
