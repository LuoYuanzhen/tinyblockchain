package tolyzot.tinyblockchain.blockchain.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import tolyzot.tinyblockchain.blockchain.model.Block;
import tolyzot.tinyblockchain.blockchain.server.BlockChain;
import tolyzot.tinyblockchain.blockchain.model.RespBean;
import tolyzot.tinyblockchain.blockchain.model.Transaction;

import java.util.List;

@RestController
public class BlockChainController {

    @Autowired
    private BlockChain blockChain;

    @Value("#{T(java.util.UUID).randomUUID().toString().replace('-', '')}")
    private String nodeAddressHash;

    @PostMapping("/transaction/new")
    public RespBean addTransaction(@RequestBody Transaction transaction){
        int blockIdx = blockChain.addTransaction(transaction.getSenderHash(),
                                                 transaction.getReceiverHash(),
                                                 transaction.getAmount());
        return RespBean.ok(String.format("Block %d added a transaction.", blockIdx));
    }

    @GetMapping("/mine")
    public RespBean mine(){
        Block previousBlock = blockChain.getLastBlock();
        int previousNonce;
        if (previousBlock == null){
            // The first block.
            previousNonce = -1;
        }
        else previousNonce = previousBlock.getNonce();

        //start mining.
        int proofNonce = blockChain.mine(previousNonce);

        //reward for miner
        blockChain.addTransaction("0", nodeAddressHash, 1.0);

        //add block
        Block block = blockChain.addBlock(proofNonce);

        return RespBean.ok(String.format("Miner %s mined a new block!", nodeAddressHash), block);
    }

    @GetMapping("/chain")
    public RespBean getChain(){
        List<Block> chain = blockChain.getChain();

        return RespBean.ok(String.format("Length of blockchain:%d", chain.size()), chain);
    }

    @PostMapping("/join")
    public RespBean joinNodes(@RequestBody List<String> nodeAddresses){
        for (String nodeAddress : nodeAddresses) {
            blockChain.registerNode(nodeAddress);
        }

        return RespBean.ok(String.format("%d nodes joined successfully!", nodeAddresses.size()), blockChain.getNodes());
    }

    @GetMapping("/longestChain")
    public RespBean runLongestChainRule(){
        boolean isChange = blockChain.longestChainRule();
        if (isChange){
            return RespBean.ok("Now the longest chain is the main chain.", blockChain.getChain());
        }
        return RespBean.ok("The main chain is the longest chain.", blockChain.getChain());
    }
}
