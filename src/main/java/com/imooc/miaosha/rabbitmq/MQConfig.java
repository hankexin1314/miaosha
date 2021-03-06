package com.imooc.miaosha.rabbitmq;


import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class MQConfig {

    public static final String QUEUE = "queue";
    public static final String MIAOSHA_QUEUE = "miaosha.queue";
    public static final String TOPIC_QUEUE1 = "topicQueue1";
    public static final String TOPIC_QUEUE2 = "topicQueue2";
    public static final String HEADER_QUEUE = "header.queue";
    public static final String TOPIC_EXCHANGE = "topicExchange";
    public static final String FANOUT_EXCHANGE = "fanoutExchange";
    public static final String HEADERS_EXCHANGE = "headersExchange";

    @Bean
    public Queue miaoshaQueue() {
        return new Queue(MIAOSHA_QUEUE, true);
    }

//    // Direct模式 交换机
//    @Bean
//    public Queue queue() {
//        return new Queue(QUEUE, true);
//    }
//
//    // Topic模式 交换机
//    @Bean
//    public Queue topicQueue1() {
//        return new Queue(TOPIC_QUEUE1, true);
//    }
//    @Bean
//    public Queue topicQueue2() {
//        return new Queue(TOPIC_QUEUE2, true);
//    }
//    @Bean
//    public TopicExchange topicExchage(){
//        return new TopicExchange(TOPIC_EXCHANGE);
//    }
//    @Bean
//    public Binding topicBinding1() {
//        return BindingBuilder.bind(topicQueue1()).to(topicExchage()).with("topic.key1");
//    }
//    @Bean // # 代表0个或多个 *代表一个
//    public Binding topicBinding2() {
//        return BindingBuilder.bind(topicQueue2()).to(topicExchage()).with("topic.#");
//    }
//
//    // Fanout模式 交换机Exchange
//    @Bean
//    public FanoutExchange fanoutExchage(){
//        return new FanoutExchange(FANOUT_EXCHANGE);
//    }
//    @Bean
//    public Binding FanoutBinding1() {
//        return BindingBuilder.bind(topicQueue1()).to(fanoutExchage());
//    }
//    @Bean
//    public Binding FanoutBinding2() {
//        return BindingBuilder.bind(topicQueue2()).to(fanoutExchage());
//    }
//
//    // Header模式 交换机Exchange
//    @Bean
//    public HeadersExchange headersExchage(){
//        return new HeadersExchange(HEADERS_EXCHANGE);
//    }
//    @Bean
//    public Queue headerQueue() {
//        return new Queue(HEADER_QUEUE, true);
//    }
//    @Bean
//    public Binding headerBinding() {
//        Map<String, Object> map = new HashMap<>();
//        map.put("header1", "value1");
//        map.put("header2", "value2");
//        return BindingBuilder.bind(headerQueue()).to(headersExchage()).whereAll(map).match();
//    }
}
