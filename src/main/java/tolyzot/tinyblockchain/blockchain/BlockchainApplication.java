package tolyzot.tinyblockchain.blockchain;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class BlockchainApplication extends SpringBootServletInitializer {

    @RequestMapping("/")
    public String home(){
        return "home page";
    }
    public static void main(String[] args) {
        SpringApplication.run(BlockchainApplication.class, args);
    }

    protected SpringApplicationBuilder configure(SpringApplicationBuilder application){
        return application.sources(BlockchainApplication.class);
    }
}
