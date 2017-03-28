/**
 * Copyright (C) 2015 Fernando Cejas Open Source Project
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package com.nec.kabutoclient.navigation;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Class used to navigate through the application.
 */
@Singleton
public class Navigator {

    @Inject
    public void Navigator() {
        // empty
    }

//    public void navigateToLogin(Context context) {
//        if (context != null) {
//            Intent intentToLaunch = MainActivity.getCallingIntent(context);
//            context.startActivity(intentToLaunch);
//        }
//    }
//
//    public void navigateToMainTabPages(Context context) {
//        if (context != null) {
//            Intent intentToLaunch = MainTabPageActivity.getCallingIntent(context);
//            context.startActivity(intentToLaunch);
//        }
//    }
//
//    public static void goToMainTabPages(Context context) {
//        if (context != null) {
//            Intent intentToLaunch = MainTabPageActivity.getCallingIntent(context);
//            context.startActivity(intentToLaunch);
//        }
//    }

    public static boolean isInteger(String value) {
        try {
            Integer.parseInt(value);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static boolean isBoolean(String value) {
        return value.equals("true") || value.equals("false") || value.equals("TRUE")
                || value.equals("FALSE");
    }

    public static boolean isLong(String value) {
        try {
            Long.parseLong(value);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

//    public void navigateToPhoneLoginActivity(Context context) {
//        if (context != null) {
//            context.startActivity(BindPhoneActivity.getCallingIntent(context, true));
//        }
//    }
//
//    public void navigateToRegisterSettingActivity(Context context) {
//        if (context != null) {
//            context.startActivity(RegisterSettingActivity.getCallingIntent(context));
//        }
//    }

}
