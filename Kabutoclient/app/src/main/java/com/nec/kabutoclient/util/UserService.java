package com.nec.kabutoclient.util;

import android.support.annotation.NonNull;

import com.annimon.stream.Stream;
import com.google.gson.Gson;
import com.nec.kabutoclient.data.response.User;
import com.nec.kabutoclient.data.response.UserAccount;
import com.nec.kabutoclient.data.response.UserDetailInfo;

import java.lang.ref.WeakReference;
import java.util.List;

/**
 * 保存用户基础信息
 * Date: 2016-05-24
 * Time: 13:46
 */
public class UserService {
  private static final String USER_DETAIL = "user_detail";
  private static final String USER = "user";
  private int grade;
  private UserDetailInfo detailInfo;
  private User user;
  private UserAccount userAccount;
  private static Gson gson;
  private static UserService userInfoService;
  private List<WeakReference<Observer<User>>> userObserversReference;
  private List<WeakReference<Observer<UserAccount>>> userAccountObserversReference;
  private List<WeakReference<Observer<UserDetailInfo>>> userDetailObserversReference;

  public synchronized static UserService getInstance() {
    if (userInfoService == null) {
      userInfoService = new UserService();
    }
    getGson();
    return userInfoService;
  }

  private static Gson getGson() {
    if (gson == null) {
      gson = new Gson();
    }
    return gson;
  }

  public interface Observer<T> {
    void update(T t);
  }

  public void saveUserAccount(@NonNull UserAccount userAccount) {
    if (userAccount == null) {
      throw new NullPointerException(
        "userAccount is not null,if you clear this,please call clearUserAccount()");
    } else {
//      this.userAccount = userAccount;
//      detailInfo = getUserDetailInfo();
//      if (detailInfo != null) {
//        detailInfo.setUserAccount(userAccount);
//        saveUserDetail(detailInfo);
//      }
//      if (userAccountObserversReference != null && !userAccountObserversReference.isEmpty()) {
//        Stream.of(userAccountObserversReference).forEach(value -> {
//          Observer<UserAccount> userAccountObserver = value.get();
//          if (userAccountObserver != null) {
//            userAccountObserver.update(userAccount);
//          }
//        });
//      }
    }
  }

//  @Nullable
//  public UserAccount getUserAccount() {
//    if (userAccount != null) {
//      return userAccount;
//    } else {
//      UserDetailInfo userDetailInfo = getUserDetailInfo();
//      if (userDetailInfo != null) {
//        return userDetailInfo.getUserAccount();
//      }
//    }
//    return null;
//  }

  public void clearUserAccount() {
    if (userAccountObserversReference != null && !userAccountObserversReference.isEmpty()) {
//      Stream.of(userAccountObserversReference).forEach(value -> {
//        Observer<UserAccount> userAccountObserver = value.get();
//        if (userAccountObserver != null) {
//          userAccountObserver.update(null);
//        }
//      });
    }
//    this.userAccount = null;
//    if (this.detailInfo != null) {
//      detailInfo.setUserAccount(null);
//      saveUserDetail(detailInfo);
//    }
  }

  public void saveUser(@NonNull User user) {
//    if (user == null) {
//      throw new NullPointerException("user is not null,if you reset this,please call clearUser()");
//    } else {
//      this.user = user;
//      detailInfo = getUserDetailInfo();
//      if (detailInfo != null) {
//        detailInfo.setUserBasicInfo(user);
//      }
//      if (userObserversReference != null && !userObserversReference.isEmpty()) {
        Stream.of(userObserversReference).forEach(value -> {
          Observer<User> userAccountObserver = value.get();
          if (userAccountObserver != null) {
            userAccountObserver.update(user);
          }
        });
//      }
//      ThreadPool.execute(() -> {
//        Config.saveString(USER, getGson().toJson(user));
//        Config.saveString(USER_DETAIL, getGson().toJson(detailInfo));
//      });
//    }
  }

//  @Nullable
//  public User getUser() {
//    if (user != null) {
//      return user.clone();
//    } else {
//      user = getLocalUser();
//      if (user != null) {
//        return user.clone();
//      }
//      return null;
//    }
//  }

//  @Nullable
//  public User getLocalUser() {
//    String userJson = Config.getString(USER);
//    if (TextUtils.isEmpty(userJson)) {
//      return null;
//    } else {
//      return getGson().fromJson(userJson, User.class);
//    }
//  }
//
//  public void clearUser() {
//    if (userObserversReference != null && !userObserversReference.isEmpty()) {
//      Stream.of(userObserversReference).forEach(value -> {
//        Observer<User> userAccountObserver = value.get();
//        if (userAccountObserver != null) {
//          userAccountObserver.update(null);
//        }
//      });
//    }
//    this.user = null;
//    ThreadPool.execute(() -> Config.saveString(USER, ""));
//    if (this.detailInfo != null) {
//      detailInfo.setUserBasicInfo(null);
//      saveUserDetail(detailInfo);
//    }
//  }


//  public Future<User> getUserOnIO() {
//    return ThreadPool.submit(this::getUser);
//  }
//
//
//  public void saveUserDetail(@NonNull UserDetailInfo detailInfo) {
//    if (detailInfo == null) {
//      throw new NullPointerException(
//        "userDetailInfo is not null,if you reset this,please call clearUserDetailInfo()");
//    } else {
//      UserAccount userAccount = detailInfo.getUserAccount();
//      if (userAccount == null && this.detailInfo != null
//        && this.detailInfo.getUserAccount() != null) {
//        detailInfo.setUserAccount(this.detailInfo.getUserAccount());
//      }
//
//      User user = detailInfo.getUserBasicInfo();
//      if (user == null && this.detailInfo != null && this.detailInfo.getUserBasicInfo() != null) {
//        detailInfo.setUserBasicInfo(this.detailInfo.getUserBasicInfo());
//      }
//
//      List<Album> userAlbum = detailInfo.getUserAlbum();
//      if (userAlbum == null && this.detailInfo != null && this.detailInfo.getUserAlbum() != null) {
//        detailInfo.setUserAlbum(this.detailInfo.getUserAlbum());
//      }
//
//      if (user != null) {
//        int grade1 = user.getGrade();
//        if (grade1 < grade) {
//          detailInfo.getUserBasicInfo().setGrade(grade);
//        } else {
//          grade = grade1;
//        }
//      } else {
//        this.grade = 0;
//      }
//      this.detailInfo = detailInfo;
//      this.user = this.detailInfo.getUserBasicInfo();
//      this.userAccount = this.detailInfo.getUserAccount();
//      if (userDetailObserversReference != null && !userDetailObserversReference.isEmpty()) {
//        Stream.of(userDetailObserversReference).forEach(value -> {
//          Observer<UserDetailInfo> userAccountObserver = value.get();
//          if (userAccountObserver != null) {
//            userAccountObserver.update(detailInfo);
//          }
//        });
//      }
//      ThreadPool.execute(() -> {
//        Config.saveString(USER, getGson().toJson(user));
//        Config.saveString(USER_DETAIL, getGson().toJson(detailInfo));
//      });
//    }
//
//  }
//
//  public Future<UserDetailInfo> getUserDetailOnIO() {
//    return ThreadPool.submit(this::getUserDetailInfo);
//  }
//
//  @Nullable
//  public UserDetailInfo getUserDetailInfo() {
//    if (this.detailInfo != null) {
//      return this.detailInfo.clone();
//    } else {
//      String userDetailJson = Config.getString(USER_DETAIL);
//      if (TextUtils.isEmpty(userDetailJson)) {
//        return null;
//      } else {
//        this.detailInfo = getGson().fromJson(userDetailJson, UserDetailInfo.class);
//        if (this.detailInfo != null) {
//          return this.detailInfo.clone();
//        } else {
//          return null;
//        }
//      }
//    }
//  }
//
//  public void clearUserDetail() {
//    if (userDetailObserversReference != null && !userDetailObserversReference.isEmpty()) {
//      Stream.of(userDetailObserversReference).forEach(value -> {
//        Observer<UserDetailInfo> userAccountObserver = value.get();
//        if (userAccountObserver != null) {
//          userAccountObserver.update(null);
//        }
//      });
//    }
//    this.detailInfo = null;
//    ThreadPool.execute(() -> Config.saveString(USER_DETAIL, ""));
//  }
//
//  public void reset() {
//    grade = 0;
//    detailInfo = null;
//    user = null;
//    userAccount = null;
//    gson = null;
//    userObserversReference = null;
//    userAccountObserversReference = null;
//    userDetailObserversReference = null;
//    userInfoService = null;
//  }


}