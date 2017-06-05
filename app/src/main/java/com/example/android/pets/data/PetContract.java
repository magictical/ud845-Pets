package com.example.android.pets.data;

import android.provider.BaseColumns;

/**
 * Created by MD on 2017-06-05.
 */

//상속은 BaseColumns으로 받음
public class PetContract {
    //Table을 생성할때 내부클래스로 사용
    public static final class PetEntry implements BaseColumns {
        //SQL문을 호출할때 사용할 각 Column의 이름을 상수로 사용
        public static final String TABLENAME = "pets";
        //_id 컬럼
        public static final String _ID = "_id";
        //name column
        public static final String COLUMN_PET_NAME = "name";
        //breed column
        public static final String COLUMN_PET_BREED = "breed";
        //gender column
        public static final String COLUMN_PET_GENDER = "gender";
        //weight column
        public static final String COLUMN_PET_WEIGHT = "weight";

        public static final int GENDER_UNKNOWN = 0;
        public static final int GENDER_MAIL = 1;
        public static final int GENDER_FEMAIL = 2;

    }



}
