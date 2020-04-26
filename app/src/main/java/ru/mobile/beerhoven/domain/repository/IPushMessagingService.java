package ru.mobile.beerhoven.domain.repository;

import androidx.fragment.app.FragmentActivity;

public interface IPushMessagingService {
   void onSendPushNotification(FragmentActivity activity);
}
