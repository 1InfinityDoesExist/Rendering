package com.template.render.config;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.elasticsearch.client.Client;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;

@Configuration
public class ElasticsearchConfig {

	@Value("${elasticsearch.host}")
	private String hostName;
	@Value("${elasticsearch.port}")
	private int port;
	@Value("${elasticsearch.cluster.name}")
	private String clusterName;
	@Value("${elasticsearch.node.name}")
	private String nodeName;

	@Bean
	public Client client() throws UnknownHostException {
		Settings settings = Settings.builder().put("client.transport.sniff", true).put("cluster.name", clusterName)
				.put("node.name", nodeName).build();
		return new PreBuiltTransportClient(settings)
				.addTransportAddress(new TransportAddress(InetAddress.getByName(hostName), port));
	}

	@Bean
	public ElasticsearchTemplate elasticsearchTemplate(@Autowired Client client) throws UnknownHostException {
		return new ElasticsearchTemplate(client());
	}
}
