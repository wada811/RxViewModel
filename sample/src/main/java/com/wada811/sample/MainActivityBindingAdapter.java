package com.wada811.sample;

import android.app.Activity;
import com.wada811.sample.databinding.ActivityMainBinding;
import org.jetbrains.annotations.NotNull;

public class MainActivityBindingAdapter extends ActivityBindingAdapter<ActivityMainBinding, MainViewModel> {

    public MainActivityBindingAdapter(@NotNull Activity activity, int layoutId, @NotNull MainViewModel viewModel){
        super(activity, layoutId, viewModel);
    }
}
