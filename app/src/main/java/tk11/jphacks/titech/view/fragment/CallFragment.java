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
import android.widget.HorizontalScrollView;

import com.daimajia.swipe.SwipeLayout;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

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
    private static final int TRACK_NUMBAR = 0;

    @ViewById(R.id.extension_canvas)
    ExtensionBrowserCanvas extentionBrowserCanvas;
    @ViewById(R.id.svPrimary)
    ExtensionBrowserCanvas primaryCanvas;
    @ViewById(R.id.sliding_layout)
    SwipeLayout slidingUpPanelLayout;
    @ViewById(R.id.button_container)
    HorizontalScrollView buttonContainer;

    @ViewById(R.id.button1)
    Button button1;

    private Peer _peer;
    private MediaConnection _media;
    private MediaStream _msLocal;
    private MediaStream _msRemote;
    private Handler _handler;
    private String _id;
    private boolean _bCalling;
    // chat
//    private DataConnection _data;
//    private Boolean _bConnecting;
//    private Runnable _runAddLog;
//    private List<String> _aryLogs;
//    private Bitmap _image;

    @ViewById(R.id.red_filter_canvas)
    ExtensionBrowserCanvas redCanvas;

    @ViewById(R.id.white_filter_canvas)
    ExtensionBrowserCanvas whiteCanvas;

    @ViewById(R.id.blue_filter_canvas)
    ExtensionBrowserCanvas blueCanvas;

    @ViewById(R.id.orange_filter_canvas)
    ExtensionBrowserCanvas orangeCanvas;

    @ViewById(R.id.green_filter_canvas)
    ExtensionBrowserCanvas greenCanvas;

    @ViewById(R.id.frame_filter_canvas)
    ExtensionBrowserCanvas frameCanvas;

    @ViewById(R.id.star_anim_filter_canvas)
    ExtensionBrowserCanvas staranimCanvas;

    @ViewById(R.id.reset_filter_canvas)
    ExtensionBrowserCanvas resetmCanvas;

    @Click(R.id.red_filter_canvas)
    void clickRedFilter() {
        extentionBrowserCanvas.setStandardFilter(ExtensionBrowserCanvas.RED_FILTER);
    }

    @Click(R.id.white_filter_canvas)
    void clickWhiteFilter() {
        extentionBrowserCanvas.setStandardFilter(ExtensionBrowserCanvas.WHITE_FILTER);
    }

    @Click(R.id.blue_filter_canvas)
    void clickBlueFilter() {
        extentionBrowserCanvas.setStandardFilter(ExtensionBrowserCanvas.BLUE_FILTER);
    }

    @Click(R.id.orange_filter_canvas)
    void clickOrangeFilter() {
        extentionBrowserCanvas.setStandardFilter(ExtensionBrowserCanvas.ORANGE_FILTER);
    }

    @Click(R.id.green_filter_canvas)
    void clickGreenFilter() {
        extentionBrowserCanvas.setStandardFilter(ExtensionBrowserCanvas.GREEN_FILTER);
    }

    @Click(R.id.frame_filter_canvas)
    void clickFrameFilter() {
        extentionBrowserCanvas.setFrameFilter();
    }

    @Click(R.id.star_anim_filter_canvas)
    void clickStarAnimFilter() {
        extentionBrowserCanvas.setAnimationFilter(getActivity());
    }

    @Click(R.id.reset_filter_canvas)
    void clickResetButton() {
        extentionBrowserCanvas.clearFilter();
    }


    public void setupFooter() {
        redCanvas.addSrc(_msLocal, TRACK_NUMBAR);
        redCanvas.setStandardFilter(ExtensionBrowserCanvas.RED_FILTER);

        whiteCanvas.addSrc(_msLocal, TRACK_NUMBAR);
        whiteCanvas.setStandardFilter(ExtensionBrowserCanvas.WHITE_FILTER);

        greenCanvas.addSrc(_msLocal, TRACK_NUMBAR);
        greenCanvas.setStandardFilter(ExtensionBrowserCanvas.GREEN_FILTER);

        orangeCanvas.addSrc(_msLocal, TRACK_NUMBAR);
        orangeCanvas.setStandardFilter(ExtensionBrowserCanvas.ORANGE_FILTER);

        blueCanvas.addSrc(_msLocal, TRACK_NUMBAR);
        blueCanvas.setStandardFilter(ExtensionBrowserCanvas.BLUE_FILTER);

        frameCanvas.addSrc(_msLocal, TRACK_NUMBAR);
        frameCanvas.setFrameFilter();

        staranimCanvas.addSrc(_msLocal, TRACK_NUMBAR);

        resetmCanvas.addSrc(_msLocal, TRACK_NUMBAR);
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
        slidingUpPanelLayout.addDrag(SwipeLayout.DragEdge.Bottom, buttonContainer);
        slidingUpPanelLayout.setRightSwipeEnabled(false);
        slidingUpPanelLayout.setLeftSwipeEnabled(false);

        slidingUpPanelLayout.addSwipeListener(new SwipeLayout.SwipeListener() {
            @Override
            public void onClose(SwipeLayout layout) {
                //when the SurfaceView totally cover the BottomView.
            }

            @Override
            public void onUpdate(SwipeLayout layout, int leftOffset, int topOffset) {
                //you are swiping.
            }

            @Override
            public void onStartOpen(SwipeLayout layout) {

            }

            @Override
            public void onOpen(SwipeLayout layout) {
                //when the BottomView totally show.
            }

            @Override
            public void onStartClose(SwipeLayout layout) {

            }

            @Override
            public void onHandRelease(SwipeLayout layout, float xvel, float yvel) {
                //when user's hand released.
            }
        });

        Window wnd = getActivity().getWindow();
        wnd.addFlags(Window.FEATURE_NO_TITLE);

        _handler = new Handler(Looper.getMainLooper());

        PeerOption options = new PeerOption();

        options.key = "20acaf0d-4c8f-4d3b-bfa0-d320db8f283c";
        options.domain = "tk11.titech.jphacks";

        String receivePeerId = mSharedPrefsHelper.loadKeyStorePid();
        Log.e("receivedId", receivePeerId);
        _peer = new Peer(getActivity().getApplicationContext(), receivePeerId, options);
        setPeerCallback(_peer);
        Navigator.initialize(_peer);
        MediaConstraints constraints = new MediaConstraints();
        _msLocal = Navigator.getUserMedia(constraints);

        extentionBrowserCanvas.addSrc(_msLocal, TRACK_NUMBAR);
        _bCalling = false;
        setupFooter();
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!_bCalling) {
                    selectingPeer();
                } else {
                    closing();
                }
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

    }

    private void setPeerCallback(Peer peer) {
        peer.on(Peer.PeerEventEnum.OPEN, new OnCallback() {
            @Override
            public void onCallback(Object object) {
                Log.d(TAG, "[On/Open]");
                if (object instanceof String) {
                    _id = (String) object;
                    Log.d(TAG, "ID:" + _id);
                }
            }
        });

        // !!!: Event/Connection
//        peer.on(Peer.PeerEventEnum.CONNECTION, new OnCallback() {
//            @Override
//            public void onCallback(Object object) {
//                // TODO: PeerEvent/CONNECTION
//
//                if (!(object instanceof DataConnection)) {
//                    return;
//                }
//                _data = (DataConnection) object;
//                setDataCallback(_data);
//                _bConnecting = true;
//            }
//        });


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

                Log.e(TAG, "[On/Error]" + error.type);

                String strMessage = "" + error;
                String strLabel = getString(android.R.string.ok);

                MessageDialogFragment dialog = new MessageDialogFragment();
                dialog.setPositiveLabel(strLabel);
                dialog.setMessage(strMessage);

                dialog.show(getFragmentManager(), "error");

                Log.e("_bCalling", String.valueOf(_bCalling));
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

                String strMessage = "通信を受信しました";
                String strLabel = getString(android.R.string.ok);

                MessageDialogFragment dialog = new MessageDialogFragment();
                dialog.setPositiveLabel(strLabel);
                dialog.setMessage(strMessage);

                dialog.show(getFragmentManager(), "通信キャッチ");

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

    //////////////////////////////////////////////////////////////////////////////////
    ///////////////  START: Set SkyWay peer Data connection callback   ///////////////
    //////////////////////////////////////////////////////////////////////////////////
//    void setDataCallback(DataConnection data) {
//        // !!!: DataEvent/Open
//        data.on(DataConnection.DataEventEnum.OPEN, new OnCallback() {
//            @Override
//            public void onCallback(Object object) {
//                // TODO: DataEvent/OPEN
//                //addLog("system", "serialization:" + _data.serialization.toString());
//                //connected();
//            }
//        });
//
//        // !!!: DataEvent/Data
//        data.on(DataConnection.DataEventEnum.DATA, new OnCallback() {
//            @Override
//            public void onCallback(Object object) {
//                String strValue = null;
//
//                if (object instanceof String) {
//                    // TODO: Receive String object
//                    strValue = (String) object;
//                } else if (object instanceof Double) {
//                    Double doubleValue = (Double) object;
//
//                    strValue = doubleValue.toString();
//                } else if (object instanceof ArrayList) {
//                    // TODO: receive Array list object
//                    ArrayList arrayValue = (ArrayList) object;
//
//                    StringBuilder sbResult = new StringBuilder();
//
//                    for (Object item : arrayValue) {
//                        sbResult.append(item.toString());
//                        sbResult.append("\n");
//                    }
//
//                    strValue = sbResult.toString();
//                } else if (object instanceof Map) {
//                    // TODO: receive Map object
//                    Map mapValue = (Map) object;
//
//                    StringBuilder sbResult = new StringBuilder();
//
//                    Object[] objKeys = mapValue.keySet().toArray();
//                    for (Object objKey : objKeys) {
//                        Object objValue = mapValue.get(objKey);
//
//                        sbResult.append(objKey.toString());
//                        sbResult.append(" = ");
//                        sbResult.append(objValue.toString());
//                        sbResult.append("\n");
//                    }
//
//                    strValue = sbResult.toString();
//                } else if (object instanceof ByteBuffer) {
//                    // TODO: receive ByteBuffer object
//
//                    ByteBuffer bbValue = (ByteBuffer) object;
//                    Bitmap bmp = null;
//
//                    byte[] byteArray = new byte[bbValue.remaining()];
//                    bbValue.get(byteArray);
//                    if (byteArray != null) {
//                        bmp = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
//                    }
//                    //_image = bmp;
//
//                    strValue = "Received Image.(Type:ByteBuffer)";
//                }
//                addLog("Partner", strValue);
//            }
//        });
//
//        // !!!: DataEvent/Close
//        data.on(DataConnection.DataEventEnum.CLOSE, new OnCallback() {
//            @Override
//            public void onCallback(Object object) {
//                // TODO: DataEvent/CLOSE
//                _data = null;
//                disconnected();
//            }
//        });
//
//        // !!!: DataEvent/Error
//        data.on(DataConnection.DataEventEnum.ERROR, new OnCallback() {
//            @Override
//            public void onCallback(Object object) {
//                // TODO: DataEvent/ERROR
//                PeerError error = (PeerError) object;
//
//                Log.d(TAG, "[On/DataError]" + error);
//
//                String strMessage = error.message;
//                String strLabel = getString(android.R.string.ok);
//
//                MessageDialogFragment dialog = new MessageDialogFragment();
//                dialog.setPositiveLabel(strLabel);
//                dialog.setMessage(strMessage);
//
//                dialog.show(getFragmentManager(), "error");
//            }
//        });
//    }
    //////////////////////////////////////////////////////////////////////////////////
    /////////////////  END: Set SkyWay peer Data connection callback   ///////////////
    //////////////////////////////////////////////////////////////////////////////////

//    void addLog(String name, String strLog) {
//        StringBuilder sb = new StringBuilder();
//        sb.append("[");
//        sb.append(name);
//        sb.append("]");
//        sb.append(strLog);
//        sb.append("\r\n");
//
//        String strMessage = sb.toString();
//
//        if (null == _aryLogs) {
//            _aryLogs = Collections.synchronizedList(new ArrayList<String>());
//        }
//
//        _aryLogs.add(strMessage);
//
//        if (null == _runAddLog) {
//            _runAddLog = new Runnable() {
//                @Override
//                public void run() {
//                    if (null == _aryLogs) {
//                        return;
//                    }
//
//                    for (; ; ) {
//                        if (0 >= _aryLogs.size()) {
//                            break;
//                        }
//
//                        String strMsg = _aryLogs.get(0);
//                        Log.e("message", strMsg);
//
//                        _aryLogs.remove(0);
//                    }
//                }
//            };
//        }
//
//        _handler.post(_runAddLog);
//    }
//
//    void sendString() {
//        String strData = "Hello SkyWay.";
//
//        boolean bResult = _data.send(strData);
//        if (true == bResult) {
//            addLog("You", strData);
//        }
//    }
//
//    void connected() {
//        _bConnecting = true;
//    }
//
//    void disconnected() {
//        _bConnecting = false;
//    }


    /**
     * Selecting peer
     */
    void selectingPeer() {

        Log.e(TAG, "selecting peer");
        if (null == _handler) {
            return;
        }
        Log.e(TAG, "selecting peer2");


        _handler.post(new Runnable() {
            @Override
            public void run() {
                _handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Log.e(TAG, "selecting peer3");
                        Log.e("自分のID", mSharedPrefsHelper.loadKeyStorePid());
                        if (mSharedPrefsHelper.loadKeyStorePid().equals("id1")) {
                            calling("id3");
                            Log.e("呼び出しID", "id3");
                        } else {
                            calling("id1");
                            Log.e("呼び出しID", "id1");
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
//        Window wnd = getActivity().getWindow();
//        wnd.addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
//        wnd.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    @Override
    public void onResume() {
        super.onResume();
        // Set volume control stream type to WebRTC audio.
        //getActivity().setVolumeControlStream(AudioManager.STREAM_VOICE_CALL);
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
        _handler = null;
        super.onDestroy();
    }
}
