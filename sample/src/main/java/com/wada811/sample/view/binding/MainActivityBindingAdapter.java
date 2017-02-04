package com.wada811.sample.view.binding;

import android.app.Activity;
import com.wada811.rxviewmodel.binding.ActivityBindingAdapter;
import com.wada811.sample.databinding.ActivityMainBinding;
import com.wada811.sample.viewmodel.MainViewModel;
import org.jetbrains.annotations.NotNull;

public class MainActivityBindingAdapter extends ActivityBindingAdapter<ActivityMainBinding, MainViewModel> {

    public MainActivityBindingAdapter(@NotNull Activity activity, int layoutId, @NotNull MainViewModel viewModel){
        super(activity, layoutId, viewModel);
        getBinding().setViewModel(viewModel);
    }
}