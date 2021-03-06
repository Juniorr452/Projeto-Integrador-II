package com.mobile.pid.pid.home.turmas;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;


import com.mobile.pid.pid.R;
import com.mobile.pid.pid.home.turmas.fragments.TurmasCriadasFragment;
import com.mobile.pid.pid.home.turmas.fragments.TurmasMatriculadasFragment;

/**
 * A simple {@link Fragment} subclass.
 */
public class TurmasFragment extends Fragment
{
    Toolbar toolbarTurmas;

    public TurmasFragment(){
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_turmas, container, false);

        PagerAdapter turmasPageAdapter    = new TurmasPageAdapter(getChildFragmentManager());
        ViewPager    turmasViewPager      = v.findViewById(R.id.turmas_view_pager);
        TabLayout    turmasTabLayout      = v.findViewById(R.id.tab_turmas);
        toolbarTurmas        = v.findViewById(R.id.toolbar_turmas);

        turmasViewPager.setAdapter(turmasPageAdapter);
        turmasTabLayout.setupWithViewPager(turmasViewPager);

        if(getActivity() instanceof AppCompatActivity) {
            ((AppCompatActivity) getActivity()).setSupportActionBar(toolbarTurmas);
            ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(false);
            ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        }

        return v;
    }


    // Tabs and ViewPager - https://www.youtube.com/watch?v=zQekzaAgIlQ
    private class TurmasPageAdapter extends FragmentPagerAdapter
    {
        public TurmasPageAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public Fragment getItem(int position)
        {
            switch(position)
            {
                case 0:
                    return new TurmasMatriculadasFragment();
                case 1:
                    return new TurmasCriadasFragment();
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
                    return getString(R.string.tab_turmas_matriculadas);
                case 1:
                    return getString(R.string.tab_turmas_criadas);
                default:
                    return null;
            }
        }
    }
}
