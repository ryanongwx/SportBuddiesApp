package com.example.sportbuddiesapp.Fragments;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.sportbuddiesapp.Buddies_Network_Activity;
import com.example.sportbuddiesapp.R;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;


public class InvitationFragment extends Fragment {

    // We can create fragments within fragments
    // In the invitationFragment, there are 2 child fragments which is sent adn received fragments.

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        // Inflate the layout for this fragment

        View view =  inflater.inflate(R.layout.fragment_invitation, container, false);


        TabLayout tabLayout = view.findViewById(R.id.invitationTablayout);
        ViewPager viewPager = view.findViewById(R.id.invitationViewpager);

        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getParentFragmentManager());

        viewPagerAdapter.addFragment(new SentFragment(), "Sent");
        viewPagerAdapter.addFragment(new ReceivedFragment(), "Received");



        viewPager.setAdapter(viewPagerAdapter);

        tabLayout.setupWithViewPager(viewPager);

        return view;
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {

        private ArrayList<Fragment> fragments;
        private ArrayList<String> titles;

        ViewPagerAdapter(FragmentManager fm) {
            super(fm);
            this.fragments = new ArrayList<>();
            this.titles = new ArrayList<>();
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        public void addFragment(Fragment fragment, String title){
            fragments.add(fragment);
            titles.add(title);
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return titles.get(position);
        }
    }

}