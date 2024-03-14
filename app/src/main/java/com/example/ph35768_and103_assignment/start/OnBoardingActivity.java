package com.example.ph35768_and103_assignment.start;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.ph35768_and103_assignment.R;
import com.example.ph35768_and103_assignment.adapter.ViewPagerAdapter;
import com.example.ph35768_and103_assignment.auth.LoginActivity;
import com.example.ph35768_and103_assignment.auth.SignUpActivity;
import com.example.ph35768_and103_assignment.databinding.ActivityOnBoardingBinding;

public class OnBoardingActivity extends AppCompatActivity {
    private ActivityOnBoardingBinding binding;

    private ViewPagerAdapter viewPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOnBoardingBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        binding.viewPager.setAdapter(viewPagerAdapter);
        binding.ciCircleIndicator.setViewPager(binding.viewPager);

//        binding.tvSkip.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                binding.viewPager.setCurrentItem(2);
//            }
//        });

//        binding.layoutNext.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (binding.viewPager.getCurrentItem() < 2){
//                    binding.viewPager.setCurrentItem(binding.viewPager.getCurrentItem() + 1);
//                }
//            }
//        });

        binding.viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
//                if (position == 2){
//                    binding.tvSkip.setVisibility(View.GONE);
//                    binding.layoutBottom.setVisibility(View.GONE);
//                } else {
//                    binding.tvSkip.setVisibility(View.VISIBLE);
//                    binding.layoutBottom.setVisibility(View.VISIBLE);
//                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        binding.btnSignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(OnBoardingActivity.this, LoginActivity.class));
            }
        });

        binding.btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(OnBoardingActivity.this, SignUpActivity.class));
            }
        });

    }
}