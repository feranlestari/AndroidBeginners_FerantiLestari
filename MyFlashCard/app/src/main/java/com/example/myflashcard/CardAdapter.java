package com.example.myflashcard;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by SRIN on 6/7/2017.
 */
public class CardAdapter extends RecyclerView.Adapter<CardAdapter.Card> {
    private Context mActivity;
    private List<Data> mList;

    public CardAdapter(Context contexts, List<Data> sdata) {
        this.mActivity = contexts;
        this.mList = sdata;
    }

    @Override
    public CardAdapter.Card onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_card, parent, false);
        return new Card(view);
    }

    @Override
    public void onBindViewHolder(CardAdapter.Card holder, int position) {
        final String question = mList.get(position).question;
        final String answer = mList.get(position).answer;
        holder.mText.setText(question);
        holder.mText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mActivity, AnswerActivity.class);
                intent.putExtra(AnswerActivity.KEY_QUESTION, question);
                intent.putExtra(AnswerActivity.KEY_ANSWER, answer);
                mActivity.startActivity(intent);
            }
        });
    }

    public void addItem(Data data) {
        mList.add(data);
        notifyDataSetChanged();
    }

    public List<Data> getList() {
        return mList;
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class Card extends RecyclerView.ViewHolder {
        private TextView mText;

        public Card(View itemView) {
            super(itemView);
            mText = (TextView) itemView.findViewById(R.id.cardTitle);
        }
    }
}
