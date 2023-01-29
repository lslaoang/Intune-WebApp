package com.testco.intunewebapp.service.feedback;

import com.testco.intunewebapp.model.feedback.BodyMessage;
import com.testco.intunewebapp.model.feedback.EmailRecipient;
import com.testco.intunewebapp.model.feedback.FeedBackModel;
import com.testco.intunewebapp.service.verify.VerifyGroupException;
import com.testco.iw.models.FeedBack;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

import java.net.URI;
import java.util.Collections;

import static com.testco.intunewebapp.util.RequestUtil.getUserNameFromRequest;
import static com.testco.intunewebapp.util.RequestUtil.printObjectAsString;
import static org.springframework.security.oauth2.client.web.reactive.function.client.ServletOAuth2AuthorizedClientExchangeFilterFunction.clientRegistrationId;

//@Service
public class FeedBackServiceImpl implements FeedBackService {

    private static final Logger LOGGER = LoggerFactory.getLogger(FeedBackServiceImpl.class);
    private static final String GRAPH_ENDPOINT = "https://graph.microsoft.com/v1.0/";

    private final WebClient webClient;

    @Value("${app.feedBack.mailbox}")
    String feedBackMailBox;

    public FeedBackServiceImpl(WebClient webClient) {
        this.webClient = webClient;
    }

    @Override
    public void sendFeedBack(FeedBack feedBack) {
        LOGGER.info("Preparing feedback body ...");
        String userEmail = getUserNameFromRequest();
        final String SENDMAIL_ENDPOINT = GRAPH_ENDPOINT + "users/" + userEmail + "/sendMail";

        if (userEmail == null) {
            throw new VerifyGroupException("Claim \"preferred_username\" with user email is not present in the token. ");
        }

        try {
            webClient
                    .post()
                    .uri(new URI(SENDMAIL_ENDPOINT))
                    .attributes(clientRegistrationId("graph-send"))
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(prepareMessageBody(feedBack))
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
            LOGGER.info("Ok you are here!");
        } catch (Exception e) {
            e.printStackTrace();
            throw new VerifyGroupException("Failed to call Graph API. " + e.getMessage());
        }

    }

    private Object prepareMessageBody(FeedBack feedBack) {
        FeedBackModel messageBody = FeedBackModel.builder()
                .saveToSentItems(false)
                .message(FeedBackModel.Message.builder()
                        .subject(feedBack.getSubject())
                        .body(BodyMessage.builder()
                                .contentType("Text")
                                .content(feedBack.getBody())
                                .build())
                        .toRecipients(Collections.singletonList(EmailRecipient.builder()
                                .emailAddress(EmailRecipient.MailBoxAddress.builder()
                                        .address(feedBackMailBox)
                                        .build())
                                .build()))
                        .build())
                .build();
        LOGGER.info("Message body below \n" + printObjectAsString(messageBody));
        return messageBody;
    }
}
