package com.mobile.pid.pid.home.adapters;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.mobile.pid.pid.R;
import com.mobile.pid.pid.home.perfil.FollowItem;


import java.util.List;

/**
 * Created by jonasramos on 19/03/18.
 */

public class FollowAdapter extends RecyclerView.Adapter<FollowAdapter.RecyclerViewHolder> {

    public static final int SEGUINDO_CONTEXT = 1;
    public static final int SEGUIDORES_CONTEXT = 0;

    private Context context;
    private List<FollowItem> follow;
    private int context_cod;

    private final String FOLLOW_STRING;
    private final String FOLLOWING_STRING;

    public FollowAdapter(Context context, List<FollowItem> follow, int context_cod) {
        this.context = context;
        this.follow = follow;
        this.context_cod = context_cod;
        FOLLOW_STRING = context.getResources().getString(R.string.follow);
        FOLLOWING_STRING = context.getResources().getString(R.string.following);
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.follow_layout, parent, false);
        return new RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return follow.size() + 10;
    }


    public class RecyclerViewHolder extends RecyclerView.ViewHolder {

        private ImageView foto;
        private TextView usuario;
        private Button botaoSeguir;

        public RecyclerViewHolder(View itemView) {
            super(itemView);

            foto        = itemView.findViewById(R.id.icon_user_follow);
            usuario     = itemView.findViewById(R.id.textView_user_name);
            botaoSeguir = itemView.findViewById(R.id.btn_follow);

            if(context_cod == SEGUINDO_CONTEXT) { // SE TIVER NO FRAGMENT DE SEGUINDO
                setFollowStateButton(botaoSeguir);
            } else { // SE TIVER NO FRAGMENT DE SEGUIDORES

            }

            botaoSeguir.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    changeStateButton(botaoSeguir);
                }
            });

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(view.getContext(), "ALO", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void changeStateButton(Button btn_follow) {

        if(btn_follow.getText().toString().equals(FOLLOW_STRING)) {
            setFollowStateButton(btn_follow);
        } else {
            setUnfollowStateButton(btn_follow);
        }
    }

    private void setFollowStateButton(Button btn_follow) {

        btn_follow.setText(FOLLOWING_STRING);
        btn_follow.setBackgroundResource(R.color.white);
        btn_follow.setTextColor(ContextCompat.getColor(context, R.color.colorPrimaryDark));
    }

    private void setUnfollowStateButton(Button btn_follow) {

        btn_follow.setText(FOLLOW_STRING);
        btn_follow.setBackgroundResource(R.color.colorPrimaryDark);
        btn_follow.setTextColor(ContextCompat.getColor(context, R.color.white));
    }
}

