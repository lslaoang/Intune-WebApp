package com.testco.intunewebapp.service.feedback;

import com.azure.spring.aad.AADOAuth2AuthenticatedPrincipal;
import com.testco.intunewebapp.model.feedback.BodyMessage;
import com.testco.intunewebapp.model.feedback.EmailRecipient;
import com.testco.intunewebapp.model.feedback.FeedBackModel;
import com.testco.intunewebapp.service.verify.VerifyGroupException;
import com.testco.iw.models.FeedBack;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.reactive.function.client.WebClient;

import java.net.URI;
import java.util.Collections;

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

        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        AADOAuth2AuthenticatedPrincipal user = (AADOAuth2AuthenticatedPrincipal) principal;
        String userEmail = user.getAttribute("preferred_username");

        final String SENDMAIL_ENDPOINT = GRAPH_ENDPOINT +"users/"+ userEmail + "/sendMail";

        if (userEmail == null) {
            throw new VerifyGroupException("Claim \"preferred_username\" with user email is not present in the token. ");
        }

        try {
            webClient
                    .post()
                    .uri(new URI(SENDMAIL_ENDPOINT))
                    .attributes(clientRegistrationId("graph-send"))
                    .headers(h -> h.setBearerAuth(user.getTokenValue()))
//                    .attributes(clientRegistrationId("testco-webapp"))
//                    .header("Authorization","Bearer EwBoA8l6BAAUkj1NuJYtTVha+Mogk+HEiPbQo04AAfEeLM7/a7HwENFmv0yjz8aGBQRQqzjEVuFltIX0HnFpwHpau2z3N8nghLUXwnTDeoBbzzLevLvD2zc3hmMC3M0ItNFivqLfzYPPYmAjW/QcFy7yyvBxNrvVifeFjKCR/p2FtSLMC/Iwws1Sv7KRGWT0OfU4U8igmYq8fa6D/Z7MopId7n5MMfI/hAmLTvtXTT1qlQVTp2LdF0RGwLaYt1X08kA8UOryPFgMwPqUaM85t/TKyh8KhfyR/RjruZ1qL1tIC3hLIFJAotslKEWWUteZYu6ETtPG+nJ9Q44STugjowI3OTiZmAiR0vZpEdQr7hbiAdsZWj+eWgnJLTq5Rm8DZgAACKnIixEj7xC7OAKPoT55ULNBJlUYI4kbL/LCyjYPZWOPNn5iv6+a1/ZKilP4i/5AIRYOOk7NpW3MWM/cWOrgMroiPD1nbCwkiIgYkoClztrfg5VL5knP/Uhg8M5JTYPEGrI+PG1SrQ3EEjYS1blxye+Sq/eptaEtKZ/4Iz9wgEfteMDtrPqanYpxi+7uJ6JYPoKlG7D/5aXB2gZQr+YhX0qkMVPog5QdvAvIkQbpaXsNkCFQ9qdhr3lJJMLGv1RaU7GGLMlBptAtQSEoTp5Ew05qJCkJ77/xU4isckE4yuRbAllJOUsdaB+Spvg7JfQlExhHULizeqCYX2PowUh5phIxFU5NrAA5X2Y7eBad0iDNhV3Uh57PuPKl6CUrOucouV/vc/m8AtSt/IgLWFPfv77Kis1Mf9cXGCu6t9wKGSrVy3hLusr8zDHr4Zp9/EIRmSzKDGPWuT6QC1rJoYi4GAUUltoAda2hr2C+izHz11JuUldH4iSPGkN7VfUb/69Kz0BRpJyuN3CViN2gwzDzt/OQ3B+OwEVlpV0Mx6uI6O7Ck6x/Ue1Wpe7G3oXyp+ix42tZrr/rv/F+oBVaJYE7/aAVUBw2BlOk24BcAM8Y6KI7yIWq4paXBf1f4gs8NGB6TfwC16WKISJc+EXwYSed4InpH5Yl+2qbKuIR93RY8GMtWDTpItVDgpLDvEjlg91tMvjUPy1VnCAIkff9XsL9+XRc/2Nn3kNtIahFexvZBE32sWkguFpb+d0bsaQsULsapXJKigI=")
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

    private Object prepareMessageBody(FeedBack feedBack){
       FeedBackModel messageBody =  FeedBackModel.builder()
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
       LOGGER.info("Message body below \n" +  printObjectAsString(messageBody));
       return messageBody;
    }
}
