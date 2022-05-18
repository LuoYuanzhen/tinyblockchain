package tolyzot.tinyblockchain.blockchain;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import tolyzot.tinyblockchain.blockchain.model.Block;
import tolyzot.tinyblockchain.blockchain.server.BlockChain;
import tolyzot.tinyblockchain.blockchain.model.Transaction;
import tolyzot.tinyblockchain.blockchain.utils.CryptoUtils;

import java.net.URL;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class BlockchainApplicationTests {

    @Test
    void contextLoads() {
    }
    public static String getRandomHashString(){
        String random = "" + new Random().nextInt();
        return CryptoUtils.SHA256(random);
    }

    public static List<Transaction> getRandomTransactions(Integer n){
        List<Transaction> transactions = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            String randomSender = getRandomHashString();
            String randomReceiver = getRandomHashString();
            double amount = new Random().nextDouble();
            transactions.add(new Transaction(randomSender, randomReceiver, amount));
        }
        return transactions;
    }

    @Test
    public void testBlockChain(){
        int nb = 4;
        BlockChain blockChain = new BlockChain();

        String hashForPrevious = "1";
        for (int i = 0; i < nb; i++) {
            int nt = 3;
            List<Transaction> transactions = getRandomTransactions(nt);
            for (Transaction transaction : transactions) {
                int blockIdx = blockChain.addTransaction(transaction.getSenderHash(),
                        transaction.getReceiverHash(),
                        transaction.getAmount());
                assertEquals(blockIdx, i);
            }
            Block block = blockChain.addBlock(new Random().nextInt());

            int blockIdx = block.getIndex();
            String hash = BlockChain.calculateHash(block);
            String previousHash = block.getPreviousHash();
            System.out.println(String.format("Block:%d \n\t hash:%s \n\t previousHash:%s", blockIdx, hash, previousHash));

            assertEquals(hashForPrevious, previousHash);
            assertEquals(blockIdx, i);

            hashForPrevious = hash;
        }
    }

    @Test
    public void testMining(){
        BlockChain blockChain = new BlockChain();
        int nb = 4;
        int nonce = 0;
        for (int i = 0; i < nb; i++) {
            List<Transaction> transactions = getRandomTransactions(4);
            for (Transaction transaction : transactions) {
                blockChain.addTransaction(transaction.getSenderHash(),
                        transaction.getReceiverHash(),
                        transaction.getAmount());
            }
            nonce = blockChain.mine(nonce);
            blockChain.addBlock(nonce);
        }
    }

    @Test
    public void testBlockJSON(){
        int n = 3;
        for (int i = 0; i < n; i++) {
            List<Transaction> transactions = getRandomTransactions(10);
            Block block = new Block(i, getRandomHashString(), transactions, 100+i);
            System.out.println(BlockChain.calculateHash(block));
        }
    }

    @Test
    public void testURLParse(){
        String address = "http://192.168.1.10:8080";
        try {
            URL url = new URL(address);
            System.out.println(url.getHost()+ "," + url.getPort());
        }catch (Exception e){

        }
    }

    @Test
    public void testSHA256(){
        int n = 10;
        for (int i = 0; i < n; i++) {
            String random = "" + new Random().nextInt();
            System.out.println(random + ":" + CryptoUtils.SHA256(random));
        }
    }

    @Test
    public void testUUID4(){
        String uuid = UUID.randomUUID().toString().replace("-", "");
        System.out.println(uuid);
    }

    @Test
    public void testOne(){
        String s = "";

    }

    public String longestPalindrome(String s) {
        if (s.length() < 2){
            return s;
        }
        int n = s.length();
        int maxlen = 1, begin = 0;

        boolean[][] state = new boolean[n][n];
        for (int i = 0; i<n; i++){
            state[i][i] = true;
        }

        for (int l=2; l<=n; l++){
            for (int i=0; i<n; i++){
                int j = i + l -1;

                if (j >= n){
                    break;
                }

                state[i][j] = state[i+1][j-1] && s.charAt(i) == s.charAt(j);

                if (state[i][j]){
                    if (l > maxlen){
                        maxlen = l;
                        begin = i;
                    }
                }
            }
        }

        Map map = new HashMap<Character, Integer>();

        return s.substring(begin, begin+maxlen);

    }

    public void expand(){
        Scanner s = new Scanner(System.in);
        int n = s.nextInt();//原矩阵维度
        int k = s.nextInt();//扩大K倍
        int[][] arr = new int[n][n];
        for(int i = 0;i<n;i++){
            for(int j = 0;j<n;j++){
                arr[i][j] = s.nextInt();
            }
        }
        int m = n*k;//新矩阵大小
        int[][] res = new int[m][n];

        for(int i = 0;i < n;i++) {
            for (int j = 0; j < n; j++) {
                for (int z = 0; z < k; z++) {
                    res[i * k + z][j] = arr[i][j];
                }
            }
        }

        for (int i = 0; i < m; i ++){
            for (int j = 0; j < n; j++){
                for (int z = 0; z < k; z++) {
                    System.out.print(res[i][j] + " ");
                }
            }
            System.out.println();
        }


    }
    
    public void repaceSpace(){
        Scanner scanner = new Scanner(System.in);
        String str = scanner.nextLine();

        StringBuilder ans = new StringBuilder();
        for (int i = 0; i < str.length(); i++){
            if (str.charAt(i) == ' '){
                ans.append("%20");
            }else{
                ans.append(str.charAt(i));
            }
        }

        System.out.println(ans.toString());
    }

    public static void main(String[] args) {
        new BlockchainApplicationTests().repaceSpace();
    }
}
