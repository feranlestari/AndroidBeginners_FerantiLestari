package com.example.myflashcard;

import android.provider.BaseColumns;

/**
 * Created by SRIN on 6/9/2017.
 */
public final class CardContract {
    private CardContract() {
    }

    public static class CardData implements BaseColumns {
        public static final String TABLE_NAME = "card";
        public static final String QUESTION = "question";
        public static final String ANSWER = "answer";
    }
}
