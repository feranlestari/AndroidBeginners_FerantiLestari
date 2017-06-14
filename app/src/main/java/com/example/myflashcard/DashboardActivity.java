package com.example.myflashcard;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Scanner;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class DashboardActivity extends AppCompatActivity {
    @InjectView(R.id.recycler_view)
    RecyclerView mRecyclerView;
    @InjectView(R.id.floating_button)
    FloatingActionButton mFloatingButton;

    private List<Data> mList;
    private AlertDialog mAddCard;
    public CardAdapter mCardAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        mList = getDataFromDB();
        if (mList == null) {
            mList = getDataFromJSON();
        }
        mCardAdapter = new CardAdapter(this, mList);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mCardAdapter);

        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.SECOND, 5);

        Intent intent = new Intent("com.example.myflashcard.notif");
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 100, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        //PendingIntent pendingIntent = PendingIntent.getService(this, 0, myIntent, 0);


        /*calendar.set(Calendar.HOUR, 5);
        calendar.set(Calendar.MINUTE, 12);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.AM_PM, Calendar.PM);*/
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 1000 * 60 * 60 * 24, pendingIntent);
    }

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        super.setContentView(layoutResID);
        ButterKnife.inject(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        initDialog();
        mFloatingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAddCard.show();
            }
        });
    }

    private void initDialog() {
        final EditText mQuestion = new EditText(this);
        LinearLayout.LayoutParams lpQ = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        mQuestion.setLayoutParams(lpQ);
        mQuestion.setHint("Question");
        final EditText mAnswer = new EditText(this);
        LinearLayout.LayoutParams lpA = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        mAnswer.setLayoutParams(lpA);
        mAnswer.setHint("Answer");

        final LinearLayout mLinear = new LinearLayout(this);
        mLinear.addView(mQuestion);
        mLinear.addView(mAnswer);
        mLinear.setOrientation(LinearLayout.VERTICAL);

        mAddCard = new AlertDialog.Builder(this)
                .setTitle("Add a New Card")
                .setView(mLinear)
                .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mCardAdapter.addItem(new Data(mQuestion.getText().toString(), mAnswer.getText().toString()));
                        saveToDB(new Data(mQuestion.getText().toString(), mAnswer.getText().toString()));
                        mQuestion.getText().clear();
                        mAnswer.getText().clear();
                    }
                })
                .setNegativeButton("Cancel", null)
                .create();
    }

    @Nullable
    private List<Data> getDataFromDB() {
        String[] columns = new String[]{CardContract.CardData.QUESTION, CardContract.CardData.ANSWER};
        Cursor cursor = getContentResolver().query(DataProvider.CARD_URI, columns, null, null, null);
        if (cursor == null) return null;
        List<Data> list = new ArrayList<>();
        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                Data data = new Data(cursor.getString(0), cursor.getString(1));
                list.add(data);
                cursor.moveToNext();
            }
        }
        cursor.close();
        return list;
    }

    private void saveToDB(Data data) {
        ContentValues values = new ContentValues();
        values.put(CardContract.CardData.QUESTION, data.question);
        values.put(CardContract.CardData.ANSWER, data.answer);
        getContentResolver().insert(DataProvider.CARD_URI, values);
    }

    private List<Data> getDataFromJSON() {
        List<Data> list = new ArrayList<>();
        try {
            JSONObject jObject = new JSONObject(readJSONFromAsset());
            JSONArray jsonArray = jObject.getJSONArray("data");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String question = jsonObject.getString("question");
                String answer = jsonObject.getString("answer");
                list.add(new Data(question, answer));
                saveToDB(new Data(question, answer));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.v("List = ", list.toString());
        return list;
    }

    public String readJSONFromAsset() {
        InputStream resourceReader = getResources().openRawResource(R.raw.initial);
        Scanner scanner = new Scanner(resourceReader);
        StringBuilder stringBuilder = new StringBuilder();
        while (scanner.hasNextLine()) {
            stringBuilder.append(scanner.nextLine());
        }
        return stringBuilder.toString();
    }
}