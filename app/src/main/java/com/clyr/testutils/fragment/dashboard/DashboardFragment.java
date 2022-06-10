package com.clyr.testutils.fragment.dashboard;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.clyr.testutils.databinding.FragmentDashboardBinding;

public class DashboardFragment extends Fragment {

    private DashboardViewModel dashboardViewModel;
    private FragmentDashboardBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        dashboardViewModel =
                new ViewModelProvider(this).get(DashboardViewModel.class);

        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        View root = binding.getRoot();


        dashboardViewModel.getText().observe(getViewLifecycleOwner(), s -> {

        });
        initView();
        return root;
    }

    private void initView() {
        String imgUrl = "https://imgtuku.heiguang.net/photogallery/2022/06/07/u_1654574139_s7IXEoxg.jpg!n";
        Glide.with(getActivity())
                .load(imgUrl)
                .into(binding.imageView);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}