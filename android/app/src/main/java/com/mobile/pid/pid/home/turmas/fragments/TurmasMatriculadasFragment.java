package com.mobile.pid.pid.home.turmas.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mobile.pid.pid.R;
import com.mobile.pid.pid.home.adapters.TurmaAdapter;
import com.mobile.pid.pid.classes_e_interfaces.Turma;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class TurmasMatriculadasFragment extends Fragment
{
    private ProgressBar progressBar;
    private FrameLayout conteudo;
    private LinearLayout mensagemSemTurma;

    private DatabaseReference turmasMatriculadasRef;

    private TurmaAdapter turmaAdapter;
    private RecyclerView recyclerView;
    private List<Turma> turmasMatriculadas;

    private ValueEventListener turmasListener;

    public TurmasMatriculadasFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        turmasMatriculadas = new ArrayList<>();
        turmaAdapter = new TurmaAdapter(getActivity(), turmasMatriculadas);

        final String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        turmasMatriculadasRef = FirebaseDatabase.getInstance().getReference().child("userTurmasMatriculadas").child(uid);

        turmasListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists())
                {
                    recyclerView.getRecycledViewPool().clear();
                    turmaAdapter.clear();

                    for(DataSnapshot dataTurma : dataSnapshot.getChildren())
                    {
                        String tuid = dataTurma.getKey();
                        FirebaseDatabase.getInstance().getReference()
                                .child("turmas")
                                .child(tuid)
                                .addListenerForSingleValueEvent(new ValueEventListener()
                                {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshotTurma)
                                    {
                                        Turma t = dataSnapshotTurma.getValue(Turma.class);
                                        t.setId(dataSnapshotTurma.getKey());

                                        turmaAdapter.add(t);
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });
                    }

                    mensagemSemTurma.setVisibility(View.GONE);
                }
                else
                    mensagemSemTurma.setVisibility(View.VISIBLE);

                progressBar.setVisibility(View.GONE);
                conteudo.setVisibility(View.VISIBLE);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_ordenar_turmas, menu);

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.ordenar_alfabetica:
                progressBar.setVisibility(View.VISIBLE);
                conteudo.setVisibility(View.GONE);

                turmaAdapter.ordenar(Turma.compararPorNome);
                break;

            case R.id.ordenar_data:
                progressBar.setVisibility(View.VISIBLE);
                conteudo.setVisibility(View.GONE);

                turmaAdapter.ordenar(Turma.compararPorDia);
                break;
            default:
                return false;
        }

        progressBar.setVisibility(View.GONE);
        conteudo.setVisibility(View.VISIBLE);

        return true;

    }

    @Override
    public void onStart() {
        super.onStart();
        turmasMatriculadasRef.addValueEventListener(turmasListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        turmasMatriculadasRef.removeEventListener(turmasListener);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.fragment_turmas_matriculadas, container, false);

        mensagemSemTurma = v.findViewById(R.id.ll_mensagem_sem_turmas_matriculadas);
        recyclerView     = v.findViewById(R.id.rv_turmas_matriculadas);
        progressBar      = v.findViewById(R.id.pb_turmas_matriculadas);
        conteudo         = v.findViewById(R.id.fl_turmas_matriculadas);
        conteudo.setVisibility(View.GONE);

        // Recycler View
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);

        recyclerView.setAdapter(turmaAdapter);

        return v;
    }
}
