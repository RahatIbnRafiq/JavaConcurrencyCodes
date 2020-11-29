import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

class WordCountHandler implements HttpHandler {
    private String sourceText;

    WordCountHandler(String sourceText) {
        this.sourceText = sourceText;
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        String query = httpExchange.getRequestURI().getQuery();
        String [] keyValue = query.split("=");
        String action = keyValue[0];
        String word = keyValue[1];
        if(!action.equals("word")) {
            httpExchange.sendResponseHeaders(400,0);
            return;
        }
        else {
            long wordCount = CountWord(word);
            byte[] response = Long.toString(wordCount).getBytes();
            httpExchange.sendResponseHeaders(200,response.length);
            OutputStream outputStream = httpExchange.getResponseBody();
            outputStream.write(response);
            outputStream.close();
        }
    }

    private long CountWord(String word) {
        long count = 0;
        int index = 0;

        while (index >=0) {
            index = this.sourceText.indexOf(word,index);
            if(index > -1) {
                count += 1;
                index += 1;
            }
        }
        return count;
    }
}

class WordCountServer {
    private String sourceTextLocation;
    private int numThreads;
    private String sourceText;
    private HttpServer httpServer;
    private int portAddress;
    WordCountServer(String sourceTextLocation, int numThreads, int portAddress) {
        this.sourceTextLocation = sourceTextLocation;
        this.numThreads = numThreads;
        this.portAddress = portAddress;
    }

    public void startServer() throws IOException {
        this.sourceText = new String(Files.readAllBytes(Paths.get(sourceTextLocation)));
        this.httpServer = HttpServer.create(new InetSocketAddress(this.portAddress),0);
        this.httpServer.createContext("/search", new WordCountHandler(this.sourceText));
        Executor executor = Executors.newFixedThreadPool(this.numThreads);
        this.httpServer.setExecutor(executor);
        this.httpServer.start();
    }
}


public class WordCountServerExample {

    public static void main(String[] args) throws IOException {
        WordCountServer wordCountServer = new WordCountServer("./resources/war_and_peace.txt", 10, 8000);
        wordCountServer.startServer();
    }
}
