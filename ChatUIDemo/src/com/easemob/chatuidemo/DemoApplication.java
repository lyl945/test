/**
 * Copyright (C) 2013-2014 EaseMob Technologies. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *     http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.easemob.chatuidemo;

import java.util.Map;

import android.app.Application;
import android.content.Context;

import com.easemob.EMCallBack;
import com.easemob.chatuidemo.domain.User;
import com.surong.leadloan.app.LDApplication;

public class DemoApplication extends LDApplication {

	public static DemoApplication getInstance() {
		return LDApplication.getInstance();
	}
}
