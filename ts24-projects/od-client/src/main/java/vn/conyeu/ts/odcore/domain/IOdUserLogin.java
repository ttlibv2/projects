package vn.conyeu.ts.odcore.domain;

import vn.conyeu.commons.beans.ObjectMap;

public interface IOdUserLogin {
    ClsUser login();
    ClsUser login(ObjectMap info);
    ClsUser login(String username, String password);
}