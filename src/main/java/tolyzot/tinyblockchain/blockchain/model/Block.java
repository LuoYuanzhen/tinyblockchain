package tolyzot.tinyblockchain.blockchain.model;

import java.util.Date;
import java.util.List;

public class Block {
    private String previousHash;
    private Integer index;

    private long timeStamp;
    private Integer nonce;
    private List<Transaction> transactions;

    public Block(Integer index, String previousHash, List<Transaction> transactions, Integer proof){
        this.index = index;
        this.previousHash = previousHash;
        this.transactions = transactions;

        this.timeStamp = new Date().getTime();
        this.nonce = proof;

    }

    public Block(Integer index, String previousHash, List<Transaction> transactions, Integer proof, long timeStamp){
        this.index = index;
        this.previousHash = previousHash;
        this.transactions = transactions;
        this.timeStamp = timeStamp;
        this.nonce = proof;
    }

    public void setPreviousHash(String previousHash) {
        this.previousHash = previousHash;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public void setNonce(Integer nonce) {
        this.nonce = nonce;
    }

    public void setTransactions(List<Transaction> transactions) {
        this.transactions = transactions;
    }

    public String getPreviousHash() {
        return previousHash;
    }

    public Integer getIndex() {
        return index;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public Integer getNonce() {
        return nonce;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    @Override
    public String toString() {
        return "Block{" +
                "previousHash='" + previousHash + '\'' +
                ", index=" + index +
                ", timeStamp=" + timeStamp +
                ", nonce=" + nonce +
                ", transactions=" + transactions +
                '}';
    }
}
