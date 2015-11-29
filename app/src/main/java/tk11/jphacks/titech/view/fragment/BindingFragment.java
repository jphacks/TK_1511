package tk11.jphacks.titech.view.fragment;

import android.app.Activity;
import android.view.ViewGroup;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;

import tk11.jphacks.titech.R;
import tk11.jphacks.titech.controller.animation.RevealEffect;
import tk11.jphacks.titech.controller.animation.RevealTriggerIntent;
import tk11.jphacks.titech.view.activity.BindingActivity_;

@EFragment(R.layout.fragment_binding)
public class BindingFragment extends BaseFragment {

    private static Activity activity;

    @AfterViews
    void onAfterViews() {
        activity = getActivity();
        RevealEffect.bindAnimation(
                (ViewGroup) activity.getWindow().getDecorView().findViewById(android.R.id.content),
                activity.getIntent(),
                activity.getApplicationContext(),
                activity.getWindow(),
                getResources()
        );
    }

    @Click(R.id.main_button_binding)
    void bindingButtonClicked() {
        RevealTriggerIntent intent = new RevealTriggerIntent(getActivity().getApplicationContext(), BindingActivity_
                .class);
        intent.setRevealIntentPivot(getView().findViewById(R.id.main_button_binding));
        startActivity(intent);
    }
}