package com.example.ph35768_and103_assignment.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.ph35768_and103_assignment.start.onboarding.OnboardingFragment1;
import com.example.ph35768_and103_assignment.start.onboarding.OnboardingFragment2;
import com.example.ph35768_and103_assignment.start.onboarding.OnboardingFragment3;

public class ViewPagerAdapter extends FragmentStatePagerAdapter {
    public ViewPagerAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new OnboardingFragment1();
            case 1:
                return new OnboardingFragment2();
            case 2:
                return new OnboardingFragment3();
            default:
                return new OnboardingFragment1();
        }

    }

    @Override
    public int getCount() {
        return 3;
    }
}
