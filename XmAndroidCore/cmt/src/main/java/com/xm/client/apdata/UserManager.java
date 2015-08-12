package com.xm.client.apdata;

/**
 * Created by wm on 15/7/13.
 */
public class UserManager {

    public UserInfo mCurrentUser;

    private static UserManager instance;

    public static UserManager getInstance() {
        if (instance == null) {
            instance = new UserManager();
        }
        return instance;
    }

    private UserManager() {

    }

    public void setCurrentUser(UserInfo currentUser) {
        mCurrentUser = currentUser;
    }

    public String getCurrentUserId() {
        return mCurrentUser.getUserId();
    }

}
