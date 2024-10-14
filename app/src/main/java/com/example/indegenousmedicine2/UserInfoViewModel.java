package com.example.indegenousmedicine2;


import androidx.lifecycle.ViewModel;

public class UserInfoViewModel extends ViewModel {
    private final UserInfo userInfo = new UserInfo();

    public UserInfo getUserInfo() {
        return userInfo;
    }
}
