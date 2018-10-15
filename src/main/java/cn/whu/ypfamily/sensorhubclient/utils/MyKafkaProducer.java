package cn.whu.ypfamily.sensorhubclient.utils;

import java.util.Properties;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;

public class MyKafkaProducer {
	
	private String servers;
	private String topic;
	private Producer<String, String> producer;
	
	public MyKafkaProducer(String servers, String topic) {
		this.servers = servers;
		this.topic = topic;
	}
	
	public void create() {
		this.producer = createProducer();
	}
	
	public void close() {
		this.producer.close();
	}
	
	public void SendMessage(String key, String value) {
		producer.send(new ProducerRecord<String, String>(this.topic, key, value));
	}
	
	private Producer<String, String> createProducer() {
		Properties props = new Properties();
		props.put("bootstrap.servers", this.servers);
		props.put("acks", "all"); // ack方式，all，会等所有的commit最慢的方式
		props.put("retries", 0); // 失败是否重试，设置会有可能产生重复数据
		props.put("batch.size", 16384); // 对于每个partition的batch buffer大小
		props.put("linger.ms", 1);  // 等多久，如果buffer没满，比如设为1，即消息发送会多1ms的延迟
		props.put("buffer.memory", 33554432); // 整个producer可以用于buffer的内存大小
		props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
		props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

		return new KafkaProducer<>(props);
	}
}
