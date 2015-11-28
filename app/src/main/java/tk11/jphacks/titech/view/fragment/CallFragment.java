package tk11.jphacks.titech.view.fragment;

import android.app.Activity;
import android.view.ViewGroup;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;

import tk11.jphacks.titech.R;
import tk11.jphacks.titech.controller.animation.RevealEffect;

@EFragment(R.layout.fragment_call)
public class CallFragment extends BaseFragment {

    @AfterViews
    void onAfterViews() {
        Activity activity = getActivity();
        RevealEffect.bindAnimation(
                (ViewGroup) activity.getWindow().getDecorView().findViewById(android.R.id.content),
                activity.getIntent(),
                activity.getApplicationContext(),
                activity.getWindow(),
                getResources()
        );
    }
}