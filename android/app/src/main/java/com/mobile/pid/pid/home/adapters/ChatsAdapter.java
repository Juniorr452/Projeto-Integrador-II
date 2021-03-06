package com.mobile.pid.pid.home.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mobile.pid.pid.R;
import com.mobile.pid.pid.classes_e_interfaces.Dialogs;
import com.mobile.pid.pid.classes_e_interfaces.Turma;
import com.mobile.pid.pid.classes_e_interfaces.Chat;
import com.mobile.pid.pid.home.turmas.detalhes_turma.DetalhesTurma;
import com.mobile.pid.pid.home.turmas.detalhes_turma.chat.ChatActivity;

import java.util.List;

public class ChatsAdapter extends RecyclerView.Adapter<ChatsAdapter.ChatsViewHolder>
{
    private Context ctx;
    private LayoutInflater layoutInflater;
    private List<Chat> chats;
    private Turma turma;
    private int usuario;

    public ChatsAdapter(Context c, List<Chat> chats, Turma t, int usuario)
    {
        this.ctx       = c;
        this.turma     = t;
        this.chats     = chats;
        this.usuario   = usuario;
        layoutInflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public ChatsAdapter.ChatsViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View v = layoutInflater.inflate(R.layout.item_chat, parent, false);

        ChatsViewHolder vh = new ChatsViewHolder(v);

        return vh;
    }

    @Override
    public void onBindViewHolder(ChatsViewHolder holder, int position)
    {
        final Chat chat = chats.get(position);

        holder.chatNome.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent i = new Intent(ctx, ChatActivity.class);
                i.putExtra("turma", turma);
                i.putExtra("chat",    chat);
                i.putExtra("usuario", usuario);

                ctx.startActivity(i);
            }
        });

        if(usuario == DetalhesTurma.PROFESSOR)
            holder.chatNome.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    Dialogs.deletarChat(ctx, turma, chat);
                    return true;
                }
            });

        holder.chatNome.setText("#" + chat.getNome());
    }

    @Override
    public int getItemCount() {
        return chats.size();
    }

    public class ChatsViewHolder extends RecyclerView.ViewHolder
    {
        private TextView chatNome;

        public ChatsViewHolder(View itemView)
        {
            super(itemView);

            chatNome = itemView.findViewById(R.id.chat_nome);
        }
    }

    public void add(Chat c)
    {
        chats.add(c);
    }

    public void clear()
    {
        chats.clear();
        notifyDataSetChanged();
    }
}
