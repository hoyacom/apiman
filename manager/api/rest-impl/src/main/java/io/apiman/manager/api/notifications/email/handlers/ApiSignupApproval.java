package io.apiman.manager.api.notifications.email.handlers;

import io.apiman.manager.api.beans.events.ApiSignupEvent;
import io.apiman.manager.api.beans.events.IVersionedApimanEvent;
import io.apiman.manager.api.beans.notifications.dto.NotificationDto;
import io.apiman.manager.api.notifications.impl.ApiSignupNotificationProducer;

/**
 * @author Marc Savy {@literal <marc@blackparrotlabs.io>}
 */
public class ApiSignupApproval implements INotificationHandler  {

    @Override
    public void handle(NotificationDto<? extends IVersionedApimanEvent> rawNotification) {
        @SuppressWarnings("unchecked")
        NotificationDto<ApiSignupEvent> signupNotification = (NotificationDto<ApiSignupEvent>) rawNotification;


    }

    @Override
    public boolean wants(NotificationDto<? extends IVersionedApimanEvent> notification) {
        if (notification.getReason().equals(ApiSignupNotificationProducer.APIMAN_API_APPROVAL_REQUEST)) {
            return true;
        }
        return false;
    }
}