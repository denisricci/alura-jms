package br.com.alura.jms;

import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.TextMessage;
import javax.naming.NamingException;

/**
 * 
 * Classe que exemplifica um simples produtor de mensagens para uma fila MQ, 
 * o nome financeiro está registrado no arquivo jndi.properties
 * que se encontra na pasta src do projeto.
 * 
 * 
 * @author Denis Ricci
 *
 */

public class SimpleQueueProducer {
		
	public static void main(String[] args) throws NamingException, JMSException {
		
		JmsBuilder builder = new JmsBuilder();
		
		MessageProducer producer = builder.newConnection().from("financeiro").buildMessageProducer();						
		
		for(int i = 0; i <= 1000 ;i++){
			TextMessage textMessage = builder.createTextMessage("Mensagem" + i);
			producer.send(textMessage);
		}		
		
		builder.closeAll();
	}

}
