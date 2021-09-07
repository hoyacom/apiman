package io.apiman.manager.api.notifications.impl;

import io.apiman.common.logging.ApimanLoggerFactory;
import io.apiman.common.logging.IApimanLogger;
import io.apiman.manager.api.beans.events.AccountSignupEvent;
import io.apiman.manager.api.beans.events.IVersionedApimanEvent;
import io.apiman.manager.api.beans.notifications.NotificationCategory;
import io.apiman.manager.api.beans.notifications.dto.RecipientDto;
import io.apiman.manager.api.notifications.INotificationProducer;
import io.apiman.manager.api.beans.notifications.dto.CreateNotificationDto;
import io.apiman.manager.api.beans.notifications.dto.RecipientType;
import io.apiman.manager.api.service.NotificationService;

import java.util.List;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

/**
 * @author Marc Savy {@literal <marc@blackparrotlabs.io>}
 */
@ApplicationScoped
public class NewAccountNotificationProducer implements INotificationProducer {

    private static final IApimanLogger LOGGER = ApimanLoggerFactory.getLogger(NewAccountNotificationProducer.class);
    public static final String APIMAN_ACCOUNT_APPROVAL_REQUEST = "apiman.account.approval.request";
    private final NotificationService notificationService;

    @Inject
    public NewAccountNotificationProducer(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @Override
    public void processEvent(IVersionedApimanEvent event) {
        if (!(event instanceof AccountSignupEvent)) {
            LOGGER.trace("NewAccountEventProcessor not interested in {0}", event.getClass()); // TODO deleteme
            return;
        }

        AccountSignupEvent signupEvent = (AccountSignupEvent) event;

        if (signupEvent.isApprovalRequired()) {
            CreateNotificationDto newNotification = new CreateNotificationDto();

            RecipientDto approversRole = new RecipientDto()
                 .setRecipient("approver")
                 .setRecipientType(RecipientType.ROLE);

            newNotification.setRecipient(List.of(approversRole)) // TODO(msavy): take this from preferences
                           .setReason(APIMAN_ACCOUNT_APPROVAL_REQUEST)
                           .setReasonMessage("A new account needs approval to gain access " + signupEvent.getUsername())
                           .setCategory(NotificationCategory.USER_ADMINISTRATION)
                           .setPayload(signupEvent);

            notificationService.sendNotification(newNotification);
        }
        // TODO(msavy): should we have an ABAC approach here and/or blended approach and/or individual opt-in?
    }
}