package br.com.alura.jms;

import java.util.Scanner;

import javax.jms.JMSException;
import javax.jms.ObjectMessage;
import javax.naming.NamingException;

import br.com.alura.jms.model.Pedido;

/**
 * 
 * Classe que exemplifica um simples consumidor de uma Dead Letter Queue que é
 * uma fila MQ onde ficam registradas as mensagem que não puderam serem
 * entregues devido a nao confirmacao do consumidor
 * 
 * @author Denis Ricci
 *
 */

public class DeadLetterQueueConsumer {

	@SuppressWarnings("resource")
	public static void main(String[] args) throws NamingException, JMSException {
		JmsBuilder builder = new JmsBuilder();
		builder.newConnection().from("DLQ").buildMessageConsumer(m -> {
			ObjectMessage objectMessage = (ObjectMessage) m;
			try {
				Pedido pedido = (Pedido) objectMessage.getObject();
				System.out.println("Dead letter queue message " + pedido.getCodigo());
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
