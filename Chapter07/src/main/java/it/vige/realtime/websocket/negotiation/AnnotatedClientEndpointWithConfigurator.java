package it.vige.realtime.websocket.negotiation;

import static java.util.concurrent.TimeUnit.SECONDS;

import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;

import javax.websocket.ClientEndpoint;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;

@ClientEndpoint(subprotocols = {"foo", "bar", "configured-proto"}, configurator = ClientConfigurator.class)
public class AnnotatedClientEndpointWithConfigurator {

    private static final BlockingDeque<String> MESSAGES = new LinkedBlockingDeque<>();

    public static String message() throws InterruptedException {
        return MESSAGES.pollFirst(3, SECONDS);
    }

    @OnOpen
    public void onOpen(final Session session) {
        session.getAsyncRemote().sendText("hi");
    }

    @OnMessage
    public void onMessage(final String message) {
        MESSAGES.add(message);
    }

    @OnClose
    public void onClose() {
        MESSAGES.add("CLOSED");
    }

}
