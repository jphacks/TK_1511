package tk11.jphacks.titech.view.fragment;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;

import tk11.jphacks.titech.R;
import tk11.jphacks.titech.controller.animation.RevealTriggerIntent;
import tk11.jphacks.titech.view.activity.BindingActivity_;
import tk11.jphacks.titech.view.activity.CallActivity_;
import tk11.jphacks.titech.view.activity.SettingActivity_;

@EFragment(R.layout.fragment_home)
public class HomeFragment extends BaseFragment {

    @AfterViews
    void onAfterViews() {
    }

    @Click(R.id.main_button_binding)
    void bindingButtonClicked() {
        RevealTriggerIntent intent = new RevealTriggerIntent(getActivity().getApplicationContext(), BindingActivity_
                .class);
        intent.setRevealIntentPivot(getView().findViewById(R.id.main_button_binding));
        startActivity(intent);
    }

    @Click(R.id.main_button_setting)
    void settingButtonClicked() {
        RevealTriggerIntent intent = new RevealTriggerIntent(getActivity().getApplicationContext(), SettingActivity_.class);
        intent.setRevealIntentPivot(getView().findViewById(R.id.main_button_setting));
        startActivity(intent);
    }

    @Click(R.id.main_button_calling)
    void callingButtonClicked() {
        RevealTriggerIntent intent = new RevealTriggerIntent(getActivity().getApplicationContext(), CallActivity_.class);
        intent.setRevealIntentPivot(getView().findViewById(R.id.main_button_calling));
        startActivity(intent);
    }
}