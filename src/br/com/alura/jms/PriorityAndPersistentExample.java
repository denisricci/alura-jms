package br.com.alura.jms;

import javax.jms.DeliveryMode;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.TextMessage;
import javax.naming.NamingException;

public class PriorityAndPersistentExample {
	
	public static void main(String[] args) throws NamingException, JMSException {
		JmsBuilder builder = new JmsBuilder();				
		MessageProducer producer = builder.newConnection().from("LOG").buildMessageProducer();
		TextMessage textMessageWarning = builder.createTextMessage("WARNING ...");
		TextMessage textMessageInfo = builder.createTextMessage("INFO ...");
		TextMessage textMessageError = builder.createTextMessage("Error ...");
		TextMessage textMessageDebug = builder.createTextMessage("Debug ...");
		
		producer.send(textMessageWarning, DeliveryMode.NON_PERSISTENT, 8, 90000);
		producer.send(textMessageError, DeliveryMode.NON_PERSISTENT, 9, 90000);
		producer.send(textMessageDebug, DeliveryMode.NON_PERSISTENT, 6, 90000);
		producer.send(textMessageInfo, DeliveryMode.NON_PERSISTENT, 7, 90000);
		
		builder.closeAll();
	}
}
