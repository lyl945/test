package com.surong.leadloan.utils;

import java.lang.reflect.Type;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.surong.leadload.api.data.Friend;
import com.surong.leadload.api.data.TimeTable;
import com.surong.leadload.api.data.VersionNews;
import com.surong.leadloan.entity.CommissionDetail;
import com.surong.leadloan.entity.Education;
import com.surong.leadloan.entity.Order;
import com.surong.leadloan.entity.OrderDetail;
import com.surong.leadloan.entity.OrderSituation;
import com.surong.leadloan.entity.Product;
import com.surong.leadloan.entity.Promotion;
import com.surong.leadloan.entity.WorkExperience;

public class Analyze {
	// 解析客户管理订单
	public static List<Order> analyzeCRMOrders(String json) {
		Type type = new TypeToken<List<Order>>() {
		}.getType();
		Gson gson = new Gson();
		List<Order> orderList = gson.fromJson(json, type);
		return orderList;
	}

	// 解析好友
	public static List<Friend> analyzeFriends(String json) {
		Type type = new TypeToken<List<Friend>>() {
		}.getType();
		Gson gson = new Gson();
		List<Friend> friendList = gson.fromJson(json, type);
		return friendList;
	}

	// 解析佣金订单详情
	public static List<CommissionDetail> analyzeCommissionDetail(String json) {
		Type type = new TypeToken<List<CommissionDetail>>() {
		}.getType();
		Gson gson = new Gson();
		List<CommissionDetail> commissionDetailList = gson.fromJson(json, type);
		return commissionDetailList;
	}

	// 解析佣金订单跟单详情
	public static List<OrderSituation> analyzeOrderSituation(String json) {
		Type type = new TypeToken<List<OrderSituation>>() {
		}.getType();
		Gson gson = new Gson();
		List<OrderSituation> orderSituationList = gson.fromJson(json, type);
		return orderSituationList;
	}

	// 解析推广列表
	public static List<Promotion> analyzePromotion(String json) {
		Type type = new TypeToken<List<Promotion>>() {
		}.getType();
		Gson gson = new Gson();
		List<Promotion> promotionList = gson.fromJson(json, type);
		return promotionList;
	}

	// 解析工作经历list
	public static List<WorkExperience> analyzeWorkExperience(String json) {
		Type type = new TypeToken<List<WorkExperience>>() {
		}.getType();
		Gson gson = new Gson();
		List<WorkExperience> workList = gson.fromJson(json, type);
		return workList;
	}

	// 解析教育经历list
	public static List<Education> analyzeEducationExperience(String json) {
		Type type = new TypeToken<List<Education>>() {
		}.getType();
		Gson gson = new Gson();
		List<Education> educationList = gson.fromJson(json, type);
		return educationList;
	}

	public static List<TimeTable> analyzeCRMO(String json) {
		Type type = new TypeToken<List<TimeTable>>() {
		}.getType();
		Gson gson = new Gson();
		List<TimeTable> timeTable = gson.fromJson(json, type);
		return timeTable;
	}

	public static List<VersionNews> analyzeVersion(String json) {
		Type type = new TypeToken<List<VersionNews>>() {
		}.getType();
		Gson gson = new Gson();
		List<VersionNews> versionNews = gson.fromJson(json, type);
		return versionNews;
	}

	// 解析客户管理订单详情
	public static OrderDetail analyzeCRMOrdersDetail(String json) {
		Type type = new TypeToken<OrderDetail>() {
		}.getType();
		Gson gson = new Gson();
		OrderDetail order = gson.fromJson(json, type);
		return order;
	}

	// 解析信贷店 产品
	public static List<Product> analyzePersonalProduct(String json) {
		Type type = new TypeToken<List<Product>>() {
		}.getType();
		Gson gson = new Gson();
		List<Product> productList = gson.fromJson(json, type);
		return productList;
	}

	// // 返回个人信息
	// public static void analyzePersonal(final JSONObject object, final DbUtils
	// db) {
	//
	// // new Thread(new Runnable() {
	// //
	// // @Override
	// // public void run() {
	// try {
	//
	// try {
	// db.dropTable(Personal.class);
	// // db.createTableIfNotExist(Personal.class);
	// db.save(per);
	// } catch (DbException e) {
	// e.printStackTrace();
	// }
	// // System.out.println("ddddddddddddd");
	//
	// } catch (JSONException e) {
	// e.printStackTrace();
	// }
	//
	// // }
	// // }).start();
	//
	// // return per;
	// }

}
