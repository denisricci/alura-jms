package br.com.alura.jms;

import java.util.Scanner;

import javax.jms.JMSException;
import javax.jms.TextMessage;
import javax.naming.NamingException;

/**
 * 
 * Classe que exemplifica um simples consumidor de um topico MQ, o client ID identifica o consumidor
 * caso se queira testar 2 consumidores para o mesmo topico, basta variar o clientID
 * 
 * @author Denis Ricci
 *
 */

public class SimpleTopicConsumer {

	@SuppressWarnings("resource")
	public static void main(String[] args) throws NamingException, JMSException {

		JmsBuilder builder = new JmsBuilder();
		builder.newConnection().from("loja").withClientId("Estoque").buildTopicSubscriber("Estoque", m -> {
			TextMessage text = (TextMessage) m;
			try {
				System.out.println("Mensagem recebida " + text.getText());
			} catch (JMSException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});

		// utilizado apenas para o programa continar executando.
		new Scanner(System.in).nextLine();

		builder.closeAll();
	}

}
