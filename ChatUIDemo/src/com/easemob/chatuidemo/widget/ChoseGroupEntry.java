package com.easemob.chatuidemo.widget;

import java.io.Serializable;
import java.lang.reflect.Member;

public class ChoseGroupEntry implements Serializable {
    /**
		 * 
		 */
    private static final long serialVersionUID = 421245456309590620L;

    public static final int CATEGORY_FRIEND = 0;
    public static final int CATEGORY_WORKMATE = 1;

    public static final int TYPE_ID = 0;
    public static final int TYPE_PHONE = 1;

    public Member member;
    public int type;
    public int category;
    public Serializable extraObject;
    public String message;

    public boolean success = false;
}