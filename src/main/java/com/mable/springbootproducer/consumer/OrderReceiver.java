package com.mable.springbootproducer.consumer;

import java.util.Map;

import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import com.mable.springbootproducer.constants.ConstantsSetting;
import com.mable.springbootproducer.entity.Order;
import com.rabbitmq.client.Channel;

@Component
public class OrderReceiver {


	/*@RabbitListener(bindings = @QueueBinding(
			value = @Queue(value = "order-queue", durable = "true"),
			exchange = @Exchange(name = "order-exchange", durable = "true", type = "topic"),
			key = "order.#")
			) */ //绑定exchange/queue
	
	@RabbitListener(bindings = @QueueBinding(
			value = @Queue(value = ConstantsSetting.RABBIT_QUEUE_NAME, 
							durable = "true"), 
			exchange = @Exchange(value = ConstantsSetting.RABBIT_EXCHANGE, 
					durable = "true",
					type = ConstantsSetting.RABBIT_EXCHANGE_TYPE,
					ignoreDeclarationExceptions = ConstantsSetting.RABBIT_EXCHANGE_IGNORE_DECLARATION), 
			key = ConstantsSetting.RABBIT_ROUTING_KEY)
			)
	@RabbitHandler //标识，有消息过来，要处理
	public void onOrderMessage(@Payload Order order, 
			@Headers Map<String, Object> headers, 
			Channel channel) throws Exception {
		
		//消费者操作
		System.out.println("-----------收到消息-----------");
		System.out.println("订单ID：" + order.getId());
		Long deliveryTag = (Long) headers.get(AmqpHeaders.DELIVERY_TAG);
		//手工ACK
		channel.basicAck(deliveryTag, false);
	}
}
