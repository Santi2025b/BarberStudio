package com.proyect.barberstudio.ui.screen.profile

sealed class ProfileUiEvent {
    object OnEditProfileClick : ProfileUiEvent();
    object OnPersonalDataClick : ProfileUiEvent();
    object OnNotificationsClick : ProfileUiEvent();
    object OnHistoryClick : ProfileUiEvent();
    object OnLogoutClick : ProfileUiEvent();
}