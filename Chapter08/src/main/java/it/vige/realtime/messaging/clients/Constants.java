package it.vige.realtime.messaging.clients;

public interface Constants {

	String USER_NAME = "my-user";
	String USER_PASSWORD = "my-pass";
	
	String QUEUE_NAME = "gps_coordinates";
	String QUEUE_LOOKUP = "java:/jms/queue/GPS";

	String TOPIC_NAME = "bus_stops";
	String TOPIC_LOOKUP = "java:/jms/topic/BUS";

	String REMOTE_QUEUE_NAME = "questionary";
	String REMOTE_EXPORTED_QUEUE_LOOKUP = "java:/jboss/exported/jms/queue/Questionary";
	String REMOTE_QUEUE_LOOKUP = "java:/jms/queue/Questionary";
	
	String CONNECTION_FACTORY = "jms/RemoteConnectionFactory";
}
