package com.nec.kabutoclient.data.db;

import android.util.Log;

import com.nec.kabutoclient.data.ChatType;
import com.nec.kabutoclient.data.SendState;
import com.nec.kabutoclient.data.realm.RealmString;

import io.realm.DynamicRealm;
import io.realm.DynamicRealmObject;
import io.realm.FieldAttribute;
import io.realm.RealmMigration;
import io.realm.RealmObjectSchema;
import io.realm.RealmSchema;


public class Migration implements RealmMigration {
  @Override
  public void migrate(DynamicRealm realm, long oldVersion, long newVersion) {
    Log.e("migrate", "oldVersion:" + oldVersion + "  newVersion:" + newVersion);
    RealmSchema schema = realm.getSchema();
    if (oldVersion == 0 && oldVersion < newVersion) {
      oldVersion++;
    }
    if (oldVersion == 1 && oldVersion < newVersion) {
      RealmObjectSchema giftSchema = schema.get("RealmGift");
      giftSchema
          .addField("staySecond", int.class)
          .addField("specialPicture", String.class)
          .addField("continuous", int.class)
          .addField("bigPictureOnlyName", String.class);
      RealmObjectSchema feedSchema = schema.get("RealmFeed");
      feedSchema.addRealmListField("roomCoverList", schema.get(RealmString.class.getSimpleName()));
      oldVersion++;
    }
    if (oldVersion == 2 && oldVersion < newVersion) {
      schema
          .create("RealmLocalPhotoData")
          .addField("localFileKey", String.class, FieldAttribute.PRIMARY_KEY,
              FieldAttribute.REQUIRED)
          .addField("localFilePath", String.class)
          .addField("thumbFilePath", String.class)
          .addField("netFileKey", String.class)
          .addField("width", int.class)
          .addField("height", int.class);
      schema
          .create("RealmHisDefaultEmoji")
          .addField("defaultEmojiString", String.class, FieldAttribute.PRIMARY_KEY,
              FieldAttribute.REQUIRED)
          .addField("lastUpdateTime", long.class);
      RealmObjectSchema giftSchema = schema.get("RealmGift");
      giftSchema
          .addField("factor", float.class)
          .addField("propsText", String.class)
          .addField("specialType", int.class)
          .addField("useGrade", int.class)
          .addField("video", String.class);
      schema.create("RealmProp")
          .addField("type", int.class, FieldAttribute.PRIMARY_KEY)
          .addField("name", String.class)
          .addRealmListField("props", giftSchema);
      RealmObjectSchema messageSchema =
          schema.get("RealmMessage")
              .addField("sendState", int.class)
              .addField("imageWidth", int.class)
              .addField("imageHeight", int.class)
              .addField("audioSecond", int.class)
              .addField("localImagePath", String.class)
              .addField("localResizeImagePath", String.class)
              .addField("localThumbImagePath", String.class)
              .addField("localAudioPath", String.class)
              .addField("localFileKey", String.class)
              .addField("giftBigPhotoName", String.class)
              .addField("stay", int.class)
              .addField("userDiamondNum", int.class)
              .addField("propScore", int.class)
              .addField("propBankNote", int.class)
              .transform(obj -> {
                obj.setInt("type", ChatType.TEXT.getCode());
                obj.setInt("sendState", SendState.SUCCEED.getCode());
              });
      schema
          .get("RealmSessionDetail")
          .addRealmObjectField("realmMessage", messageSchema)
          .transform(
              obj -> {
                DynamicRealmObject message = realm.where("RealmMessage")
                    .equalTo("userFromId", obj.getInt("userFromId"))
                    .equalTo("userToId", obj.getInt("userToId")).findFirst();
                if (message == null) {
                  message = realm.createObject("RealmMessage", obj.getString("id"));
                  message.setString("localId", obj.getString("localId"));
                  message.setString("id", obj.getString("id"));
                  message.setInt("userFromId", obj.getInt("userFromId"));
                  message.setInt("userToId", obj.getInt("userToId"));
                  message.setInt("type", obj.getInt("type"));
                  message.setLong("createTime", obj.getLong("createTime"));
                  message.setInt("sendState", SendState.SUCCEED.getCode());
                  message.setString("content", obj.getString("content"));
                  message.setInt("friendState", obj.getInt("friendState"));
                }
                obj.setObject("realmMessage", message);
              });
      schema.create("RealmTopUser")
          .addField("localId", String.class, FieldAttribute.PRIMARY_KEY, FieldAttribute.REQUIRED)
          .addField("id", int.class)
          .addField("userId", int.class)
          .addField("type", int.class)
          .addField("createTime", long.class)
          .addField("count", int.class)
          .addField("sortNum", int.class)
          .addField("allCount", int.class)
          .addField("nickName", String.class)
          .addField("avatar", String.class)
          .addField("address", String.class)
          .addField("description", String.class)
          .addField("lastSortNum", int.class)
          .addField("topUserType", int.class);
      oldVersion++;
    }
    if (oldVersion == 3 && oldVersion < newVersion) {
      RealmObjectSchema giftSchema = schema.get("RealmGame");
      giftSchema
          .addField("backgroundColor", String.class)
          .addField("logoUrl", String.class);
      schema
          .create("RealmLiveInGame")
          .addField("id", int.class, FieldAttribute.PRIMARY_KEY,
              FieldAttribute.REQUIRED)
          .addField("name", String.class)
          .addField("summary", String.class)
          .addField("gameCover", String.class)
          .addField("description", String.class)
          .addField("descriptionUrl", String.class)
          .addField("createTime", long.class)
          .addField("updateTime", long.class)
          .addField("status", int.class)
          .addField("backgroundColor", String.class)
          .addField("logoUrl", String.class);
      oldVersion++;
    }
    if (oldVersion == 4 && oldVersion < newVersion) {
      RealmObjectSchema realmSongHistorySchema = schema.create("RealmSongHistory");
      realmSongHistorySchema
          .addField("id", int.class, FieldAttribute.PRIMARY_KEY, FieldAttribute.REQUIRED)
          .addField("name", String.class)
          .addField("singer", String.class)
          .addField("like", int.class)
          .addField("count", int.class)
          .addField("timestamp", long.class)
          .addField("lrcPath", String.class)
          .addField("songPath", String.class);
      oldVersion++;
    }
    if (oldVersion == 5 && oldVersion < newVersion) {
      RealmObjectSchema messageSchema = schema.get("RealmMessage");
      messageSchema.addField("props", int.class)
          .addField("vip", int.class)
          .addField("grade", int.class)
          .addField("text", String.class)
          .addField("title", String.class)
          .addField("hour", int.class)
          .addField("propsName", String.class)
          .addField("propsDiamondNum", int.class);

      RealmObjectSchema kickMessageSchema = schema.create("RealmKickMessage");
      kickMessageSchema
          .addField("id", String.class, FieldAttribute.PRIMARY_KEY, FieldAttribute.REQUIRED)
          .addField("userFromId", int.class)
          .addField("userToId", int.class)
          .addField("createTime", long.class)
          .addField("sendState", int.class)
          .addField("giftBigPhotoName", String.class)
          .addField("props", int.class)
          .addField("vip", int.class)
          .addField("grade", int.class)
          .addField("text", String.class)
          .addField("title", String.class)
          .addField("hour", int.class)
          .addField("code", int.class)
          .addField("propsDiamondNum", int.class)
          .addField("localId", String.class)
          .addField("userDiamondNum", int.class)
          .addField("propsName", String.class);

      schema
          .get("RealmSessionDetail")
          .removeField("realmMessage");
      oldVersion++;
    }
    if (oldVersion == 6 && oldVersion < newVersion) {
      schema.get("RealmUser").addField("relationId", int.class).addField("followId", int.class);
      schema.get("RealmLocalPhotoData").setNullable("localFileKey", true);
      schema.get("RealmHisDefaultEmoji").setNullable("defaultEmojiString", true);
      schema.get("RealmTopUser").setNullable("localId", true);
      schema.get("RealmKickMessage").setNullable("id", true);
      schema.get("RealmMessage").setNullable("id", true);
      oldVersion++;
    }
    if (oldVersion == 7 && oldVersion < newVersion) {
      schema.get("RealmUser").addField("specialFocus", int.class);

      schema.get("RealmSessionDetail")
          .addField("fromSystem", int.class);

      schema.get("RealmMessage")
          .addField("fromSystem", int.class);

      RealmObjectSchema followSchema =
          schema.create("RealmFollowList");
      followSchema
          .addField("key", String.class, FieldAttribute.PRIMARY_KEY, FieldAttribute.REQUIRED)
          .setNullable("key", true)
          .addField("followId", int.class)
          .addRealmListField("dataList", schema.get("RealmUser"));
      RealmObjectSchema giftSchema = schema.get("RealmGift");
      giftSchema.addField("score", int.class);
      oldVersion++;
    }

    if (oldVersion == 8 && oldVersion < newVersion) {// app versionName = 3.0.0&3.0.1
      RealmObjectSchema gameSchema = schema.get("RealmGame");
      gameSchema
          .addField("detailCover", String.class);
      RealmObjectSchema liveInGameSchema = schema.get("RealmLiveInGame");
      liveInGameSchema
          .addField("detailCover", String.class);
      RealmObjectSchema gameBannerSchema = schema.create("RealmGameBanner");
      gameBannerSchema
          .addField("showGameType", int.class, FieldAttribute.PRIMARY_KEY, FieldAttribute.REQUIRED)
          .setNullable("showGameType", true)
          .addField("bannerUrl", String.class)
          .addField("bannerGameId", int.class);
      RealmObjectSchema gameDataSchema = schema.create("RealmGameData");
      gameDataSchema
          .addField("showGameType", int.class, FieldAttribute.PRIMARY_KEY, FieldAttribute.REQUIRED)
          .setNullable("showGameType", true)
          .addRealmObjectField("gameBanner", schema.get("RealmGameBanner"))
          .addRealmListField("games", schema.get("RealmGame"));

      schema.get("RealmGift")
          .renameField("video", "audio")
          .addField("animateUrl", String.class)
          .addField("displayPriority", int.class)
          .addField("num", int.class)
          .addField("subAnimateUrl", String.class)
          .addField("plusPicture", String.class);
      schema.get("RealmProp")
          .addField("plusPicture", String.class);
      schema.get("RealmUser").addField("starSign", String.class);
      RealmObjectSchema messageSchema = schema.get("RealmMessage");
      messageSchema.addField("liveAvatarUrl", String.class)
          .addField("nick", String.class)
          .addField("linkUrl", String.class);
      oldVersion++;
    }

    if (oldVersion == 9 && oldVersion < newVersion) {// 新增数据库
      schema.create("RealmEmojiPacket")
          .addField("id", String.class, FieldAttribute.PRIMARY_KEY, FieldAttribute.REQUIRED)
          .setNullable("id", true)
          .addField("packetId", int.class)
          .addField("name", String.class)
          .addField("packageFaceUrl", String.class)
          .addField("price", int.class)
          .addField("diamond", int.class)
          .addField("gameCoin", int.class)
          .addField("packageKeyboardUrl", String.class)
          .addField("facePackageType", int.class)
          .addField("facePackageInfo", String.class)
          .addField("packageUrl", String.class)
          .addField("updateTime", long.class)
          .addField("payStatus", int.class)
          .addField("userId", String.class);
      schema.get("RealmHisDefaultEmoji").addField("type", int.class);

      schema.get("RealmMessage")
          .addField("facePackageId", int.class)
          .addField("image", String.class);

      RealmObjectSchema feedSchema =
          schema.create("RealmExploreFeedList");
      feedSchema
          .addField("discoverId", int.class, FieldAttribute.PRIMARY_KEY, FieldAttribute.REQUIRED)
          .addField("isHot", boolean.class)
          .addRealmListField("dataList", schema.get("RealmFeed"));

      RealmObjectSchema hotSchema =
          schema.create("RealmExploreHotList");
      hotSchema
          .addField("discoverId", int.class, FieldAttribute.PRIMARY_KEY, FieldAttribute.REQUIRED)
          .addRealmListField("dataList", schema.get("RealmFeed"));

      RealmObjectSchema userSchema =
          schema.create("RealmExploreUserList");
      userSchema
          .addField("discoverId", int.class, FieldAttribute.PRIMARY_KEY, FieldAttribute.REQUIRED)
          .addRealmListField("dataList", schema.get("RealmUserDetailInfo"));

      schema.get("RealmFeed").addField("distance", int.class).addField("location", String.class);
      schema.get("RealmUserDetailInfo").addRealmListField("userCoverList",
          schema.get("RealmString"));

      // schema.get("RealmGift")
      // .addField("circularCount",String.class)
      // .addField("prompt",String.class)
      // .addField("delay",int.class)
      // .addRealmListField("propsList", schema.get("RealmGift"));

      schema.create("RealmFollowHistory")
          .addField("userId", int.class, FieldAttribute.PRIMARY_KEY, FieldAttribute.REQUIRED)
          .addField("tn", int.class)
          .addField("desc", String.class)
          .addField("nickName", String.class)
          .addField("nn", int.class)
          .addField("avatar", String.class);
      oldVersion++;
    }

    if (oldVersion == 10 && oldVersion < newVersion) {
      RealmObjectSchema exploreHistorySchema =
          schema.create("RealmExploreHistoryList");
      exploreHistorySchema
          .addField("discoverId", int.class, FieldAttribute.PRIMARY_KEY, FieldAttribute.REQUIRED)
          .addRealmListField("dataList", schema.get("RealmFollowHistory"));
      schema.create("RealmHandselUser")
          .addField("id", String.class, FieldAttribute.PRIMARY_KEY, FieldAttribute.REQUIRED)
          .setNullable("id", true).addField("handselUserId", int.class)
          .addField("userId", int.class).addField("blueDiamond", int.class)
          .addRealmObjectField("userBasicInfo", schema.get("RealmUser"));
      schema.create("RealmHandselCount")
          .addField("userId", int.class, FieldAttribute.PRIMARY_KEY, FieldAttribute.REQUIRED)
          .addField("userCount", int.class).addField("roomCount", int.class)
          .addField("blueDiamond", int.class);
      schema.get("RealmSessionDetail").addField("reply", boolean.class);
      oldVersion++;
    }

    if (oldVersion == 11 && oldVersion < newVersion) {
      schema.create("RealmGroup")
          .addField("id", int.class, FieldAttribute.PRIMARY_KEY, FieldAttribute.REQUIRED)
          .addField("name", String.class)
          .addField("avatar", String.class)
          .addField("count", int.class)
          .addField("unreadCount", int.class)
          .addField("createTime", long.class)
          .addField("remind", int.class)
          .addField("nickname", String.class)
          .addField("useAvatar", int.class)
          .addField("level", int.class)
          .addField("invalid", boolean.class)
          .addField("useName", int.class);

      schema.create("RealmGroupFollowList")
          .addField("key", String.class, FieldAttribute.PRIMARY_KEY, FieldAttribute.REQUIRED)
          .setNullable("key", true)
          .addRealmListField("dataList", schema.get("RealmUser"));

      schema.create("RealmGroupMember")
          .addField("primaryId", String.class, FieldAttribute.PRIMARY_KEY, FieldAttribute.REQUIRED)
          .setNullable("primaryId", true)
          .addField("id", int.class)
          .addField("groupId", int.class)
          .addRealmObjectField("userInfo", schema.get("RealmUser"))
          .addField("nickname", String.class)
          .addField("level", int.class);

      schema.get("RealmUser").addField("invited", int.class);

      schema.get("RealmMessage")
          .addField("gid", int.class)
          .addField("groupAvatar", String.class)
          .addField("chatId", int.class)
          .addField("useAvatar", int.class)
          .addField("groupName", String.class)
          .addField("groupMemberCount", int.class)
          .addField("avatar", String.class)
          .addField("fromId", int.class)
          .addField("toId", int.class)
          .addField("fromNick", String.class)
          .addField("level", int.class);

      schema.get("RealmSessionDetail")
          .addField("gid", int.class)
          .addField("groupAvatar", String.class)
          .addField("useAvatar", int.class)
          .addField("groupMemberCount", int.class)
          .addField("groupFromNick", String.class)
          .addField("groupName", String.class);
      oldVersion++;
    }

    if (oldVersion == 12 && oldVersion < newVersion) {
      schema.get("RealmMessage")
          .addField("privateLiveKey", String.class);
      schema.get("RealmFeed").addField("roomType", int.class);
      schema.get("RealmLiveDetail").addField("roomType", int.class);
      oldVersion++;
    }

    if (oldVersion == 13 && oldVersion < newVersion) {
      schema.get("RealmMessage")
          .addField("audiounRead", int.class);

      schema.get("RealmSessionDetail")
          .addField("remind", int.class);
      oldVersion++;
    }

    if (oldVersion == 14 && oldVersion < newVersion) {
      schema.create("RealmLiveTheme")
          .addField("id", int.class, FieldAttribute.PRIMARY_KEY, FieldAttribute.REQUIRED)
          .addField("userStatus", int.class)
          .addField("type", int.class)
          .addField("userPurchaseStatus", int.class)
          .addField("weight", int.class)
          .addField("name", String.class)
          .addField("previewImgSmallUrl", String.class)
          .addField("previewBottomImgUrl", String.class)
          .addField("previewGameImgUrl", String.class)
          .addField("previewBottomIconUrl", String.class)
          .addField("previewImgBigUrl", String.class)
          .addField("msgImgCellUrl", String.class)
          .addField("resourceZipPathUrl", String.class)
          .addField("animationZipUrl", String.class)
          .addField("previewImgBottomUrl", String.class)
          .addField("purchaseType", int.class)
          .addField("price", int.class)
          .addField("animationEffect", int.class)
          .addField("unlockGrade", int.class)
          .addField("status", int.class)
          .addField("createTime", long.class);
      schema.get("RealmEmojiPacket").addField("grade", int.class);
      schema.get("RealmUserDetailInfo").addField("kickOut", boolean.class);
      oldVersion++;
    }
    if (oldVersion == 15 && oldVersion < newVersion) {
      schema.get("RealmFeed").addField("betType", int.class);
      schema.get("RealmGame").addField("roundOff", int.class);
      schema.get("RealmLiveInGame").addField("roundOff", int.class);
      schema.create("RealmThemeBanner")
          .addField("id", int.class, FieldAttribute.PRIMARY_KEY, FieldAttribute.REQUIRED)
          .addField("bannerImageUrl", String.class);
      schema.get("RealmLiveTheme").addField("previewTopIconUrl", String.class);
      schema.get("RealmMessage")
          .addField("liveThemeId", int.class)
          .addField("liveThemeName", String.class)
          .addField("liveThemeImageUrl", String.class)
          .addField("desc", String.class)
          .addField("keyDesc", String.class);
      schema.create("RealmGameWinner")
          .addField("id", String.class, FieldAttribute.PRIMARY_KEY, FieldAttribute.REQUIRED)
          .setNullable("id", true)
          .addField("type", int.class)
          .addField("userId", int.class)
          .addField("avatar", String.class)
          .addField("joinGames", int.class)
          .addField("winGames", int.class)
          .addField("gameCoins", long.class)
          .addField("winRate", double.class)
          .addField("nickName", String.class);
      oldVersion++;
    }

    if (oldVersion == 16 && oldVersion < newVersion) {
      schema.get("RealmSessionDetail")
          .addField("readed", boolean.class)
          .addField("groupChatId", int.class);
      schema.create("RealmShortCutWord")
          .addField("id", int.class, FieldAttribute.PRIMARY_KEY, FieldAttribute.REQUIRED)
          .addField("content", String.class)
          .addField("weight", int.class)
          .addField("status", int.class)
          .addField("createTime", long.class)
          .addField("updateTime", long.class);
      schema.create("RealmHisShortCutWord")
          .addField("id", int.class, FieldAttribute.PRIMARY_KEY, FieldAttribute.REQUIRED)
          .addField("content", String.class)
          .addField("weight", int.class)
          .addField("status", int.class)
          .addField("createTime", long.class)
          .addField("updateTime", long.class);
      schema.get("RealmUserAccount").addField("gameCoinCurrNum_tmp", long.class)
          .addField("gameCoinHisAllNum_tmp", long.class)
          .transform(obj -> {
            obj.setLong("gameCoinCurrNum_tmp", obj.getInt("gameCoinCurrNum"));
            obj.setLong("gameCoinHisAllNum_tmp", obj.getInt("gameCoinHisAllNum"));
          }).removeField("gameCoinCurrNum").renameField("gameCoinCurrNum_tmp", "gameCoinCurrNum")
          .removeField("gameCoinHisAllNum")
          .renameField("gameCoinHisAllNum_tmp", "gameCoinHisAllNum");
      schema.create("RealmClientMenu")
          .addField("androidControllerName", String.class, FieldAttribute.PRIMARY_KEY,
              FieldAttribute.REQUIRED)
          .setNullable("androidControllerName", true)
          .addField("linkContent", String.class)
          .addField("type", int.class)
          .addField("photoUrl", String.class)
          .addField("label", String.class);
      schema.get("RealmUserDetailInfo").addField("privilegeRed", boolean.class);

      schema.create("RealmNearbyData")
          .addField("id", String.class, FieldAttribute.PRIMARY_KEY, FieldAttribute.REQUIRED)
          .setNullable("id", true)
          .addField("mainId", int.class)
          .addField("otherIds", String.class)
          .addRealmObjectField("userDetailInfo", schema.get("RealmUserDetailInfo"))
          .addRealmObjectField("realmFeed", schema.get("RealmFeed"));

      schema.create("RealmAttitudeIcon")
          .addField("iconId", String.class, FieldAttribute.PRIMARY_KEY,
              FieldAttribute.REQUIRED)
          .addField("localPath", String.class)
          .addField("imageType", int.class)
          .addField("url", String.class);

      schema.get("RealmMessage")
        .addField("attitude", int.class)
        .addField("author", String.class)
        .addField("storyId", String.class)
        .addField("pic", String.class)
        .addField("contentMessage", String.class);

      oldVersion++;
    }
  }
}
