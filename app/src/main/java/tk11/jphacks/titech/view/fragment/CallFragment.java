package tk11.jphacks.titech.view.fragment;

import android.app.Activity;
import android.media.AudioManager;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;
import org.json.JSONArray;

import io.skyway.Peer.Browser.MediaConstraints;
import io.skyway.Peer.Browser.MediaStream;
import io.skyway.Peer.Browser.Navigator;
import io.skyway.Peer.CallOption;
import io.skyway.Peer.MediaConnection;
import io.skyway.Peer.OnCallback;
import io.skyway.Peer.Peer;
import io.skyway.Peer.PeerError;
import io.skyway.Peer.PeerOption;
import tk11.jphacks.titech.R;
import tk11.jphacks.titech.controller.animation.RevealEffect;
import tk11.jphacks.titech.view.view.ExtensionBrowserCanvas;

@EFragment(R.layout.fragment_call)
public class CallFragment extends BaseFragment {

    private static final String TAG = CallFragment.class.getSimpleName();
    @ViewById(R.id.extension_canvas)
    ExtensionBrowserCanvas extentionBrowserCanvas;
    @ViewById(R.id.svPrimary)
    ExtensionBrowserCanvas primaryCanvas;
    @ViewById(R.id.btnAction)
    Button btnAction;
    @ViewById(R.id.tvOwnId)
    TextView tvOwnId;
    private Peer _peer;
    private MediaConnection _media;
    private MediaStream _msLocal;
    private MediaStream _msRemote;
    private Handler _handler;
    private String _id;
    private String[] _listPeerIds;
    private boolean _bCalling;

    @Click(R.id.red_filter_button)
    void clickRedFilter() {
        extentionBrowserCanvas.setStandardFilter(ExtensionBrowserCanvas.RED_FILTER);
    }

    @Click(R.id.white_filter_button)
    void clickWhiteFilter() {
        extentionBrowserCanvas.setStandardFilter(ExtensionBrowserCanvas.WHITE_FILTER);
    }

    @Click(R.id.frame_filter_button)
    void clickFrameFilter() {
        extentionBrowserCanvas.setFrameFilter();
    }

    @Click(R.id.star_anim_filter_button)
    void clickStarAnimFilter() {
        extentionBrowserCanvas.setAnimationFilter(getActivity());
    }

    @Click(R.id.reset_filter_button)
    void clickResetButton() {
        extentionBrowserCanvas.clearFilter();
    }

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

        Window wnd = getActivity().getWindow();
        wnd.addFlags(Window.FEATURE_NO_TITLE);

        _handler = new Handler(Looper.getMainLooper());

        PeerOption options = new PeerOption();

        options.key = "20acaf0d-4c8f-4d3b-bfa0-d320db8f283c";
        options.domain = "tk11.titech.jphacks";


        _peer = new Peer(getActivity(), options);
        setPeerCallback(_peer);

        Navigator.initialize(_peer);
        MediaConstraints constraints = new MediaConstraints();
        _msLocal = Navigator.getUserMedia(constraints);

        extentionBrowserCanvas.addSrc(_msLocal, 0);
        _bCalling = false;
        btnAction.setEnabled(true);
        btnAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.setEnabled(false);

                if (!_bCalling) {
                    listingPeers();
                } else {
                    closing();
                }
                v.setEnabled(true);
            }
        });

    }

    /**
     * Media connecting to remote peer.
     *
     * @param strPeerId Remote peer.
     */
    void calling(String strPeerId) {

        if (null == _peer) {
            return;
        }

        if (null != _media) {
            _media.close();
            _media = null;
        }

        CallOption option = new CallOption();

        _media = _peer.call(strPeerId, _msLocal, option);

        if (null != _media) {
            setMediaCallback(_media);

            _bCalling = true;
        }

        updateUI();
    }

    private void setPeerCallback(Peer peer) {
        peer.on(Peer.PeerEventEnum.OPEN, new OnCallback() {
            @Override
            public void onCallback(Object object) {
                Log.d(TAG, "[On/Open]");
                if (object instanceof String) {
                    _id = (String) object;
                    Log.d(TAG, "ID:" + _id);
                    updateUI();
                }
            }
        });

        peer.on(Peer.PeerEventEnum.CALL, new OnCallback() {
            @Override
            public void onCallback(Object object) {
                Log.d(TAG, "[On/Call]");
                if (!(object instanceof MediaConnection)) {
                    return;
                }

                _media = (MediaConnection) object;

                _media.answer(_msLocal);

                setMediaCallback(_media);

                _bCalling = true;

                updateUI();
            }
        });

        // !!!: Event/Close
        peer.on(Peer.PeerEventEnum.CLOSE, new OnCallback() {
            @Override
            public void onCallback(Object object) {
                Log.d(TAG, "[On/Close]");
            }
        });

        // !!!: Event/Disconnected
        peer.on(Peer.PeerEventEnum.DISCONNECTED, new OnCallback() {
            @Override
            public void onCallback(Object object) {
                Log.d(TAG, "[On/Disconnected]");
            }
        });

        // !!!: Event/Error
        peer.on(Peer.PeerEventEnum.ERROR, new OnCallback() {
            @Override
            public void onCallback(Object object) {
                PeerError error = (PeerError) object;

                Log.d(TAG, "[On/Error]" + error);

                String strMessage = "" + error;
                String strLabel = getString(android.R.string.ok);

            }
        });
    }


    //Unset peer callback
    void unsetPeerCallback(Peer peer) {
        peer.on(Peer.PeerEventEnum.OPEN, null);
        peer.on(Peer.PeerEventEnum.CONNECTION, null);
        peer.on(Peer.PeerEventEnum.CALL, null);
        peer.on(Peer.PeerEventEnum.CLOSE, null);
        peer.on(Peer.PeerEventEnum.DISCONNECTED, null);
        peer.on(Peer.PeerEventEnum.ERROR, null);
    }


    void setMediaCallback(MediaConnection media) {
        media.on(MediaConnection.MediaEventEnum.STREAM, new OnCallback() {
            @Override
            public void onCallback(Object object) {
                _msRemote = (MediaStream) object;

                primaryCanvas.addSrc(_msRemote, 0);
            }
        });

        // !!!: MediaEvent/Close
        media.on(MediaConnection.MediaEventEnum.CLOSE, new OnCallback() {
            @Override
            public void onCallback(Object object) {
                if (null == _msRemote) {
                    return;
                }
                primaryCanvas.removeSrc(_msRemote, 0);

                _msRemote = null;

                _media = null;
                _bCalling = false;

                updateUI();
            }
        });

        // !!!: MediaEvent/Error
        media.on(MediaConnection.MediaEventEnum.ERROR, new OnCallback() {
            @Override
            public void onCallback(Object object) {
                PeerError error = (PeerError) object;

                Log.d(TAG, "[On/MediaError]" + error);

                String strMessage = "" + error;
                String strLabel = getString(android.R.string.ok);
            }
        });

        //////////////////////////////////////////////////////////////////////////////////
        ///////////////  END: Set SkyWay peer Media connection callback   ////////////////
        //////////////////////////////////////////////////////////////////////////////////
    }


    //Unset media connection event callback.
    void unsetMediaCallback(MediaConnection media) {
        media.on(MediaConnection.MediaEventEnum.STREAM, null);
        media.on(MediaConnection.MediaEventEnum.CLOSE, null);
        media.on(MediaConnection.MediaEventEnum.ERROR, null);
    }

    // Listing all peers
    void listingPeers() {
        if ((null == _peer) || (null == _id) || (0 == _id.length())) {
            return;
        }

        _peer.listAllPeers(new OnCallback() {
            @Override
            public void onCallback(Object object) {
                if (!(object instanceof JSONArray)) {
                    return;
                }

                JSONArray peers = (JSONArray) object;

                StringBuilder sbItems = new StringBuilder();
                for (int i = 0; peers.length() > i; i++) {
                    String strValue = "";
                    try {
                        strValue = peers.getString(i);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    if (0 == _id.compareToIgnoreCase(strValue)) {
                        continue;
                    }

                    if (0 < sbItems.length()) {
                        sbItems.append(",");
                    }

                    sbItems.append(strValue);
                }

                String strItems = sbItems.toString();
                _listPeerIds = strItems.split(",");

                if ((null != _listPeerIds) && (0 < _listPeerIds.length)) {
                    selectingPeer();
                }
            }
        });

    }

    /**
     * Selecting peer
     */
    void selectingPeer() {
        if (null == _handler) {
            return;
        }

        _handler.post(new Runnable() {
            @Override
            public void run() {
                _handler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (mSharedPrefsHelper.loadName().equals("id1")) {
                            calling("id2");
                        } else {
                            calling("id1");
                        }
                    }
                });
            }
        });
    }

    /**
     * Closing connection.
     */
    void closing() {
        if (false == _bCalling) {
            return;
        }

        _bCalling = false;

        if (null != _media) {
            _media.close();
        }
    }

    void updateUI() {
        _handler.post(new Runnable() {
            @Override
            public void run() {
                if (null != btnAction) {
                    if (false == _bCalling) {
                        btnAction.setText("Calling");
                    } else {
                        btnAction.setText("Hang up");
                    }
                }

                if (null != tvOwnId) {
                    if (null == _id) {
                        tvOwnId.setText("");
                    } else {
                        tvOwnId.setText(_id);
                    }
                }
            }
        });
    }


    /**
     * Destroy Peer object.
     */
    private void destroyPeer() {
        closing();

        if (null != _msRemote) {
            primaryCanvas.removeSrc(_msRemote, 0);

            _msRemote.close();

            _msRemote = null;
        }

        if (null != _msLocal) {
            extentionBrowserCanvas.removeSrc(_msLocal, 0);

            _msLocal.close();

            _msLocal = null;
        }

        if (null != _media) {
            if (_media.isOpen) {
                _media.close();
            }

            unsetMediaCallback(_media);

            _media = null;
        }

        Navigator.terminate();

        if (null != _peer) {
            unsetPeerCallback(_peer);

            if (false == _peer.isDisconnected) {
                _peer.disconnect();
            }

            if (false == _peer.isDestroyed) {
                _peer.destroy();
            }
            _peer = null;
        }
    }


    @Override
    public void onStart() {
        super.onStart();
        // Disable Sleep and Screen Lock
        Window wnd = getActivity().getWindow();
        wnd.addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
        wnd.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    @Override
    public void onResume() {
        super.onResume();
        // Set volume control stream type to WebRTC audio.
        getActivity().setVolumeControlStream(AudioManager.STREAM_VOICE_CALL);
    }

    @Override
    public void onPause() {
        // Set default volume control stream type.
        getActivity().setVolumeControlStream(AudioManager.USE_DEFAULT_STREAM_TYPE);
        super.onPause();
    }

    @Override
    public void onStop() {
        // Enable Sleep and Screen Lock
        Window wnd = getActivity().getWindow();
        wnd.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        wnd.clearFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
        super.onStop();
    }

    @Override
    public void onDestroy() {
        destroyPeer();
        _listPeerIds = null;
        _handler = null;
        super.onDestroy();
    }
}
