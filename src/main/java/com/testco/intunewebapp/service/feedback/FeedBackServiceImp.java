package com.testco.intunewebapp.service.feedback;

import com.azure.identity.OnBehalfOfCredential;
import com.azure.identity.OnBehalfOfCredentialBuilder;
import com.testco.iw.models.FeedBack;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

import static com.testco.intunewebapp.util.RequestUtil.getUserNameFromRequest;

@Service
public class FeedBackServiceImp implements FeedBackService{


    final OnBehalfOfCredential onBehalfOfCredential = new OnBehalfOfCredentialBuilder()
            .clientId("077eb991-1556-481b-9145-26ded6919fbf")
            .clientSecret("A2i7Q~rIzmbyuSgq-Q4iM55R.kfklTvpIDdNE")
            .tenantId("a42e43e2-c7b8-499c-89a6-4b9bac2d5a6e")
//            .clientId(clientID)
//            .pfxCertificate(pfxCertificatePath) // or .pemCertificate(certificatePath) or .clientSecret("ClientSecret")
//            .clientCertificatePassword(pfxCertificatePassword) // remove if using pemCertificate or clientSecret
//            .tokenCachePersistenceOptions(tokenCachePersistenceOptions) //Optional: enables the persistent token cache which is disabled by default
//            .userAssertion("la")
            .build();



    List scopes = List.of(".default");

//    final TokenCredentialAuthProvider tokenCredentialAuthProvider = new TokenCredentialAuthProvider(scopes, onBehalfOfCredential);


    @Override
    public void sendFeedBack(FeedBack feedBack) {

        String userEmail = getUserNameFromRequest();
//        String uid = user.getAttribute("uid");
//
//        GraphServiceClient graphClient = GraphServiceClient
//                .builder()
//                .authenticationProvider(tokenCredentialAuthProvider)
//                .buildClient();
//
//        Message message = new Message();
//        message.subject = "Meet for lunch?";
//        ItemBody body = new ItemBody();
//        body.contentType = BodyType.TEXT;
//        body.content = "The new cafeteria is open.";
//        message.body = body;
//        LinkedList<Recipient> toRecipientsList = new LinkedList<Recipient>();
//        Recipient toRecipients = new Recipient();
//        EmailAddress emailAddress = new EmailAddress();
//        emailAddress.address = "laurel.laoang@gmail.com";
//        toRecipients.emailAddress = emailAddress;
//        toRecipientsList.add(toRecipients);
//        message.toRecipients = toRecipientsList;
//        LinkedList<Recipient> ccRecipientsList = new LinkedList<Recipient>();
//        Recipient ccRecipients = new Recipient();
//        EmailAddress emailAddress1 = new EmailAddress();
//        emailAddress1.address = "laurel.laoang@gmail.com";
//        ccRecipients.emailAddress = emailAddress1;
//        ccRecipientsList.add(ccRecipients);
//        message.ccRecipients = ccRecipientsList;
//
//        boolean saveToSentItems = false;
//
//        graphClient.me()
//                .sendMail(UserSendMailParameterSet
//                        .newBuilder()
//                        .withMessage(message)
//                        .withSaveToSentItems(saveToSentItems)
//                        .build())
//                .buildRequest()
//                .post();

    }
}
