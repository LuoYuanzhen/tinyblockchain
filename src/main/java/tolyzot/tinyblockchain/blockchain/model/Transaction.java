package tolyzot.tinyblockchain.blockchain.model;

public class Transaction {
    private String senderHash;
    private String receiverHash;
    private Double amount;

    public Transaction(String senderHash, String receiverHash, Double amount){
        this.senderHash = senderHash;
        this.receiverHash = receiverHash;
        this.amount = amount;
    }

    public String getSenderHash() {
        return senderHash;
    }

    public String getReceiverHash() {
        return receiverHash;
    }

    public Double getAmount() {
        return amount;
    }

    public void setSenderHash(String senderHash) {
        this.senderHash = senderHash;
    }

    public void setReceiverHash(String receiverHash) {
        this.receiverHash = receiverHash;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "senderHash='" + senderHash + '\'' +
                ", receiverHash='" + receiverHash + '\'' +
                ", amount=" + amount +
                '}';
    }
}
