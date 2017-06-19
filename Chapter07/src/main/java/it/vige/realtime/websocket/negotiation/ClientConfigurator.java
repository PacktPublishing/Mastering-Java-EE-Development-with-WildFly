package it.vige.realtime.websocket.negotiation;

import static io.undertow.util.Headers.SEC_WEB_SOCKET_PROTOCOL_STRING;

import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import javax.websocket.ClientEndpointConfig.Configurator;
import javax.websocket.HandshakeResponse;

public class ClientConfigurator extends Configurator {

    public static volatile String sentSubProtocol;
    private static volatile String receivedSubProtocol;
    private static volatile CountDownLatch receiveLatch = new CountDownLatch(1);

    @Override
    public void beforeRequest(Map<String, List<String>> headers) {
        if (headers.containsKey(SEC_WEB_SOCKET_PROTOCOL_STRING)) {
            sentSubProtocol = headers.get(SEC_WEB_SOCKET_PROTOCOL_STRING).get(0);
            headers.put(SEC_WEB_SOCKET_PROTOCOL_STRING, Collections.singletonList("configured-proto"));
        } else {
            sentSubProtocol = null;
        }
    }

    @Override
    public void afterResponse(HandshakeResponse hr) {
        Map<String, List<String>> headers = hr.getHeaders();
        if (headers.containsKey(SEC_WEB_SOCKET_PROTOCOL_STRING.toLowerCase(Locale.ENGLISH))) {
            receivedSubProtocol = headers.get(SEC_WEB_SOCKET_PROTOCOL_STRING.toLowerCase(Locale.ENGLISH)).get(0);
        } else {
            receivedSubProtocol = null;
        }
        receiveLatch.countDown();
    }

    public static String receivedSubProtocol() {
        try {
            receiveLatch.await(5, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return receivedSubProtocol;
    }
}
