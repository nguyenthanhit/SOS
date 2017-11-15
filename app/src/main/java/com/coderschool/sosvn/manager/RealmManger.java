package com.coderschool.sosvn.manager;

import io.realm.Realm;

/**
 * Created by Admin on 7/31/2017.
 */

public class RealmManger {

    private static Realm realm;
    private static RealmManger mRealmManager;

    public static RealmManger getInstance() {
        if (mRealmManager == null)
            mRealmManager = new RealmManger();
        return mRealmManager;
    }



//    public void writeUser(final User user) {
//        realm = Realm.getDefaultInstance();
//        try {
//
//            realm.executeTransaction(new Realm.Transaction() {
//                @Override
//                public void execute(Realm realm) {
//                    realm.delete(User.class);
//                    realm.copyToRealmOrUpdate(user);
//                }
//            });
//        } finally {
//            if (realm !=null) {
//                realm.close();
//            }
//        }
//    }

//    public User getUser(String phoneNumber) {
//        realm = Realm.getDefaultInstance();
//        User user = null;
//        try {
//            user = realm.where(User.class).equalTo("phoneNumber",phoneNumber).findFirst();
//       } finally {
//       }
//        return user;
//    }
}
