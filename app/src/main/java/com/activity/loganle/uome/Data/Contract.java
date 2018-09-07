package com.activity.loganle.uome.Data;

import android.provider.BaseColumns;

/**
 * Created by LoganLe on 11/5/16.
 */

public class Contract {
    // To prevent someone from accidentally instantiating the contract class,
    // make the constructor private.
    private Contract(){

    }
    // Contact table
    public static class Contact implements BaseColumns{
        public static final String Contact_table_name = "contact";
        public static final String Contact_name = "name";
        public static final String Contact_address = "address";
        public static final String Contact_phone = "phone";
        public static final String Contact_email = "email";
        public static final String Contact_owed_money = "owedmoney";
        public static final String Contact_Profile_image = "imageProfile";
        public static final String Contact_deadline_date = "contactDeadlineDate";
    }
}
