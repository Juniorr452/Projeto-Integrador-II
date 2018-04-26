package com.mobile.pid.pid.home.perfil;


import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
//import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.github.clans.fab.FloatingActionMenu;
import com.github.clans.fab.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mobile.pid.pid.R;
import com.mobile.pid.pid.home.feed.Post;
import com.mobile.pid.pid.home.perfil.fragments.CurtidasPerfilFragment;
import com.mobile.pid.pid.home.perfil.fragments.PostsFragment;
import com.mobile.pid.pid.home.perfil.fragments.SeguidoresFragment;
import com.mobile.pid.pid.home.perfil.fragments.SeguindoFragment;
import com.mobile.pid.pid.login.RedesSociaisActivity;
import com.mobile.pid.pid.login.Usuario;

/**
 * A simple {@link Fragment} subclass.
 */

// https://www.youtube.com/watch?v=BTYuLho5_rE COLLAPSING TOOLBAR
public class PerfilFragment extends Fragment
{
    private static final String TAG = "PerfilFragment";
    private View view;


    // componentes
    private CollapsingToolbarLayout collapsing_perfil;
    private TabLayout tabLayout_perfil;
    private ViewPager viewPager_perfil;
    private PagerAdapter pageAdapter_perfil;
    private ImageView imageView_user;
    private FloatingActionMenu fab_menu;
    private FloatingActionButton fab_menu_edit;
    private FloatingActionButton fab_menu_signout;

    private Usuario user_logado;

    //firebase

    DatabaseReference db;
    String user_id;
    FirebaseUser user;
    FirebaseAuth auth;

    private TextView count_followers;
    private TextView count_following;
    private TextView count_posts;


    public PerfilFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        user_id = user.getUid();
    }

    @Override
    public void onStart() {
        super.onStart();

        collapsing_perfil.setTitle(user.getDisplayName());
        Glide.with(this).load(user.getPhotoUrl()).into(imageView_user);
        //Glide.with(this).load(user.getPhotoUrl()).override(10,10).error(R.drawable.logo).into(imageView_user);
        // TODO COLOCAR GLIDE PARA SETAR IMAGEM DO USUARIO

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_perfil, container ,false);

        // FIREBASEEEE

        pageAdapter_perfil   = new PerfilPageAdapter(getChildFragmentManager());
        viewPager_perfil     = (ViewPager) view.findViewById(R.id.viewpager_perfil);
        tabLayout_perfil     = (TabLayout) view.findViewById(R.id.tab_perfil);
        imageView_user       = (ImageView) view.findViewById(R.id.image_user);
        collapsing_perfil    = (CollapsingToolbarLayout) view.findViewById(R.id.collapsing_tb);
        fab_menu             = (FloatingActionMenu) view.findViewById(R.id.fab_menu);
        fab_menu_edit        = (FloatingActionButton) view.findViewById(R.id.fab_menu_edit);
        fab_menu_signout     = (FloatingActionButton) view.findViewById(R.id.fab_menu_signout);
        count_followers      = view.findViewById(R.id.count_followers);
        count_following      = view.findViewById(R.id.count_following);
        count_posts          = view.findViewById(R.id.count_posts);

        FirebaseDatabase.getInstance().getReference("usuarios").child(user_id).child("posts")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists())
                            count_posts.setText(formatNumber(dataSnapshot.getChildrenCount()));
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

        viewPager_perfil.setAdapter(pageAdapter_perfil);
        tabLayout_perfil.setupWithViewPager(viewPager_perfil);

        // AO CLICAR NA FOTO DO USUARIO, CRIA UM DIALOG MOSTRANDO ELA EM TAMANHO REAL.
        imageView_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final Dialog dialog = new Dialog(getActivity());
                dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(getLayoutInflater().inflate(R.layout.image_fullsize, null));

                ImageView image_user_fullsize = (ImageView) dialog.findViewById(R.id.image_user_fullsize);

                Glide.with(getActivity()).load(user.getPhotoUrl()).into(image_user_fullsize);
                dialog.show();

                image_user_fullsize.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) { dialog.dismiss(); }
                });
            }
        });

        fab_menu_edit.setColorNormalResId(R.color.colorAccent);
        fab_menu_signout.setColorNormalResId(R.color.colorAccent);

        fab_menu_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fab_menu.close(true);
                startActivity(new Intent(getActivity(), AtualizarPerfilActivity.class));
            }
        });

        fab_menu_signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Context c = getActivity();

                fab_menu.close(true);
                FirebaseAuth.getInstance().signOut();

                startActivity(new Intent(c, RedesSociaisActivity.class));
                getActivity().finish();
            }
        });

        viewPager_perfil.setOffscreenPageLimit(1);

        return view;
    }

    private String formatNumber(long number) {

        String numberString = "";
        if (Math.abs(number / 1000000) > 1)
            numberString = String.valueOf(number / 1000000).toString() + "M";
        else if (Math.abs(number / 1000) > 1)
            numberString = String.valueOf(number / 1000).toString() + "K";
        else
            numberString = String.valueOf(number);

        return numberString;
    }

    private class PerfilPageAdapter extends FragmentPagerAdapter
    {
        public PerfilPageAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return 4;
        }

        @Override
        public Fragment getItem(int position)
        {
            switch(position)
            {
                case 0:
                    return new PostsFragment();
                case 1:
                    return new CurtidasPerfilFragment();
                case 2:
                    return new SeguindoFragment();
                case 3:
                    return new SeguidoresFragment();
                default:
                    return null;
            }
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position)
        {
            switch(position)
            {
                case 0:
                    return getString(R.string.tab_perfil_posts);
                case 1:
                    return getString(R.string.tab_perfil_curtidos);
                case 2:
                    return getString(R.string.following);
                case 3:
                    return getString(R.string.followers);
                default:
                    return null;
            }
        }
    }
}
