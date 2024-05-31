package vn.conyeu.ts.odcore.domain;

import vn.conyeu.commons.beans.ObjectMap;

public interface IOdUserLogin {
    ClsUser login(ObjectMap info);
}