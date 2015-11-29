package tk11.jphacks.titech.view.fragment;

import android.app.Activity;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;

import tk11.jphacks.titech.R;
import tk11.jphacks.titech.controller.animation.RevealEffect;

@EFragment(R.layout.fragment_binding)
public class BindingFragment extends BaseFragment {

    private static Activity activity;
    private EditText partnerIdText;
    private TextView partnerNameText;
    private ImageView iconImageView;

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
        bindComponent();
    }

    @Click(R.id.button_binding)
    void bindingButtonClicked() {
        startBinding();
    }

    public void bindComponent() {
        partnerIdText = (EditText) activity.findViewById(R.id.partner_id_text);
        partnerNameText = (TextView) activity.findViewById(R.id.partner_name_text);
    }

    public void setRegisteredInfo() {
        partnerNameText.setText(mSharedPrefsHelper.loadPartnerName());
        partnerIdText.setText(mSharedPrefsHelper.loadPartnerKeyStorePid());

        //TODO アイコンの取得
        /**
        if(mSharedPrefsHelper.loadImageUri()==""){
            iconImageView.setImageResource(R.drawable.no_image_icon);
        }else{
            iconImageView.setImageURI(Uri.parse(mSharedPrefsHelper.loadImageUri()));
        }
         **/
    }

    public void startBinding(){

    }

}