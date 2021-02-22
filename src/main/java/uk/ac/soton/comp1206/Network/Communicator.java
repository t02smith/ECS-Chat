package uk.ac.soton.comp1206.Network;

import java.util.ArrayList;

import com.neovisionaries.ws.client.WebSocket;
import com.neovisionaries.ws.client.WebSocketAdapter;
import com.neovisionaries.ws.client.WebSocketException;
import com.neovisionaries.ws.client.WebSocketFactory;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import uk.ac.soton.comp1206.Utility.Listeners.MessageListener;

public class Communicator {
    private static final Logger logger = LogManager.getLogger(Communicator.class);

    private WebSocket ws;
    private String server;

    private ArrayList<MessageListener> msgListeners = new ArrayList<>();

    public Communicator(String server) {
        this.server = server;        
        this.connect(server);
    }

    public void connect(String server) {
        if (this.ws != null) {
            logger.info("testing");
            this.ws.disconnect();
            logger.info("Disconnecting from: {}", this.server);
        }

        var socketFactory = new WebSocketFactory();
        
        try {
            logger.info("Attempting to connect to {}", server);
            this.ws = socketFactory.createSocket(server);
            this.ws.connect();
            logger.info("Connected to " + server);
            
            ws.addListener(new WebSocketAdapter() {
                @Override
                public void onTextMessage(WebSocket websocket, String message) throws Exception {
                    Communicator.this.receive(websocket, message);
                }
            });
        } catch (WebSocketException e) {
            logger.error("Failed to connect to {}. If it is an ecs server make sure to connect to the VPN.", server);
            if (!server.equals("ws://echo.websocket.org")) this.connect("ws://echo.websocket.org");       
        } catch (Exception e) {
            logger.error("Socket error: " + e.getMessage());
            System.exit(1);
        }
    }

    public void addListener(MessageListener ml) {
        this.msgListeners.add(ml);
    }

    public void send(String message) {
        logger.info("Sending message: " + message);
        this.ws.sendText(message);
    }

    private void receive(WebSocket webSocket, String message) {
        logger.info("Received: " + message);

        for (MessageListener ml: this.msgListeners) {
            ml.receiveMessage(message);
        }
    }

}