package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.aws.autoconfigure.context.ContextRegionProviderAutoConfiguration;
import org.springframework.cloud.aws.autoconfigure.context.ContextStackAutoConfiguration;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.amazonaws.services.sns.AmazonSNSClient;
import com.amazonaws.services.sns.model.PublishRequest;
import com.amazonaws.services.sns.model.SubscribeRequest;

@RestController
@SpringBootApplication(exclude = {ContextStackAutoConfiguration.class,ContextRegionProviderAutoConfiguration.class})
public class AwsSnsApp1Application {

	@Autowired
	private AmazonSNSClient snsClient;
	private String TOPIC_ARN = "arn:aws:sns:ap-south-1:501903367099:Lock-Down";

	/*
	 * http://localhost:8080/addSubscription/raj.rajeev2244@gmail.com
	 */
	@GetMapping(value = "/addSubscription/{email}")
	public String addSubscription(@PathVariable String email) {
		SubscribeRequest request = new SubscribeRequest(TOPIC_ARN, "email", email);
		snsClient.subscribe(request);
		return "<h3 style='color:blue;text-align:center'>Subscription request is pending,To confirm subscription,check your mail "
				+ email + "</h3>";
	}

	/*
	 * http://localhost:8080/sendNotification
	 */
	@GetMapping(value = "/sendNotification")
	public String publicshMessageToTopic() {
		String subject = "Covid-19 Lock Down";
		PublishRequest publishRequest = new PublishRequest(TOPIC_ARN, buildEmailBody(), subject);
		snsClient.publish(publishRequest);
		return "<h3 style='text-align:center'>Notification sent successfully</h3>";
	}

	private String buildEmailBody() {

		return "All India is under lockdown,Stay Safe";
	}

	public static void main(String[] args) {
		SpringApplication.run(AwsSnsApp1Application.class, args);
	}

}
