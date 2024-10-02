package com.pwmcintyre;

import java.io.IOException;
import java.util.Properties;

import com.pwmcintyre.kafka.Consumer;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Application {

	public static void main(String[] args) {

		try {

			// Load properties
			Properties configs = PropertiesLoader.loadProperties();
			log.debug(configs.toString());
			
			// Start producer
			new Thread(() -> com.pwmcintyre.kafka.Producer.start(configs)).start();

			// Start consumer
			new Thread(() -> Consumer.listen(configs)).start();

		} catch (IOException ex) {
			log.error("failed to start", ex);
		}


	}

}
