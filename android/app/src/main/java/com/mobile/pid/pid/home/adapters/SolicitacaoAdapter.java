package com.mobile.pid.pid.home.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mobile.pid.pid.R;
import com.mobile.pid.pid.classes_e_interfaces.Turma;
import com.mobile.pid.pid.classes_e_interfaces.Usuario;

import java.util.List;

public class SolicitacaoAdapter extends RecyclerView.Adapter<SolicitacaoAdapter.SolicitacaoViewHolder>
{
    private Turma   t;
    private List<Usuario> solicitacoes;
    private LayoutInflater layoutInflater;

    private int qtdSolicitacoes;

    public SolicitacaoAdapter(Context c, Turma t, List<Usuario> solicitacoes)
    {
        this.t = t;
        this.solicitacoes = solicitacoes;
        this.qtdSolicitacoes = 0;

        layoutInflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @NonNull
    @Override
    public SolicitacaoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View v = layoutInflater.inflate(R.layout.item_solicitacao, parent, false);

        SolicitacaoViewHolder vh = new SolicitacaoViewHolder(v);

        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull SolicitacaoViewHolder holder, int position)
    {
        final Usuario u = solicitacoes.get(position);

        Glide.with(holder.foto).load(u.getFotoUrl()).into(holder.foto);
        holder.nome.setText(u.getNome());

        holder.aceitar.setChecked(true);
        holder.aceitar.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                String uid = u.getUid();
                adicionarUsuarioNaTurma(uid);
                excluirSolicitacao(u);
            }
        });

        holder.recusar.setChecked(false);
        holder.recusar.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                excluirSolicitacao(u);
            }
        });
    }

    public void add(Usuario u)
    {
        solicitacoes.add(u);
        notifyItemInserted(qtdSolicitacoes++);
    }

    public void remove(String uid)
    {
        qtdSolicitacoes--;

        int i;

        for(i = 0; i < solicitacoes.size(); i++)
            if(uid.equals(solicitacoes.get(i).getUid()))
                break;

        solicitacoes.remove(i);
        notifyItemRemoved(i);
    }

    public void clear()
    {
        qtdSolicitacoes = 0;
        solicitacoes.clear();
        notifyDataSetChanged();
    }

    private void adicionarUsuarioNaTurma(String uid)
    {
        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference turmaMatriculadaRef = dbRef.child("userTurmasMatriculadas")
                .child(uid)
                .child(t.getId());

        // Adicionar como membro na turma
        dbRef.child("turmas")
                .child(t.getId())
                .child("membros")
                .child(uid).setValue(true);

        // Adicionar turma no turmas_matriculadas do aluno novo
        turmaMatriculadaRef.child("capaUrl").setValue(t.getCapaUrl());
        turmaMatriculadaRef.child("diasDaSemana").setValue(t.getDiasDaSemana());
        turmaMatriculadaRef.child("nome").setValue(t.getNome());
    }

    private void excluirSolicitacao(Usuario u)
    {
        DatabaseReference solicitacaoRef = FirebaseDatabase.getInstance().getReference()
                .child("turmas")
                .child(t.getId())
                .child("solicitacoes")
                .child(u.getUid());

        solicitacaoRef.removeValue();

        solicitacoes.remove(u);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return solicitacoes.size();
    }

    public class SolicitacaoViewHolder extends RecyclerView.ViewHolder
    {
        ImageView foto;
        TextView  nome;
        CheckBox  aceitar;
        CheckBox  recusar;

        public SolicitacaoViewHolder(View itemView)
        {
            super(itemView);

            foto    = itemView.findViewById(R.id.solicitacao_foto);
            nome    = itemView.findViewById(R.id.solicitacao_nome);
            aceitar = itemView.findViewById(R.id.solicitacao_btn_aceitar);
            recusar = itemView.findViewById(R.id.solicitacao_btn_recusar);
        }
    }
}