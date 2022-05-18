package tolyzot.tinyblockchain.blockchain.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import tolyzot.tinyblockchain.blockchain.model.Block;
import tolyzot.tinyblockchain.blockchain.model.Transaction;

import java.util.ArrayList;
import java.util.List;

public class HttpUtils {
    public static List<Block> getBlockchainByRequest(String address) {
        HttpClient httpClient = HttpClients.createDefault();

        HttpGet httpGet = new HttpGet(address);
        try {
            HttpResponse response = httpClient.execute(httpGet);
            if (response.getStatusLine().getStatusCode() == 200){
                String content = EntityUtils.toString(response.getEntity());
                // Get JSON
                JSONObject jsonObject = (JSONObject) JSONObject.parse(content);
                JSONArray jsonArrayChain = jsonObject.getJSONArray("obj");
                /***
                 * I always get the nonce being null in this way, I didn't understand why.
                 * So I had to use the stupidest way to get the blockchain from JSON.
                List<Block> tempBlockchain = JSON.parseArray(jsonObject.getString("obj"), Block.class);
                for (Block block : tempBlockchain) {
                    System.out.println(block);
                }
                 ***/

                // Stupidest way to get blockchain from JSON.
                List<Block> blockchain = new ArrayList<>();
                for (int i = 0; i < jsonArrayChain.size(); i++) {
                    JSONObject jsonBlock = jsonArrayChain.getJSONObject(i);
                    int index = jsonBlock.getInteger("index");
                    int nonce = jsonBlock.getInteger("nonce");
                    long timeStamp = jsonBlock.getLong("timeStamp");
                    String previousHash = jsonBlock.getString("previousHash");

                    String strTransactions = jsonBlock.getString("transactions");
                    List<Transaction> transactions = JSON.parseArray(strTransactions, Transaction.class);

                    Block block = new Block(index, previousHash, transactions, nonce, timeStamp);
                    blockchain.add(block);
                }

                return blockchain;
            }
        }catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException("Error while get request.");
        }
        return null;
    }

    public static void main(String[] args) {
        List<Block> blockchain = getBlockchainByRequest("http://127.0.0.1:8080/chain");
        for (Block block : blockchain) {
            System.out.println(block);
        }
    }
}

