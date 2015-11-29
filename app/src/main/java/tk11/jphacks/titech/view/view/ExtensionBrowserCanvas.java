package tk11.jphacks.titech.view.view;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.bartoszlipinski.xmltag.annotations.XmlTag;
import com.plattysoft.leonids.ParticleSystem;

import io.skyway.Peer.Browser.Canvas;
import io.skyway.Peer.Browser.MediaStream;
import tk11.jphacks.titech.R;

/**
 * Created by atsuhirotsuruta on 11/29/2015 AD.
 */
@XmlTag("ExtensionBrowserCanvas")
public class ExtensionBrowserCanvas extends FrameLayout{
    private LinearLayout filter;
    private LinearLayout animationFilter;
    private LinearLayout frameFilter;
    private Canvas browserCanvas;
    private ParticleSystem particleSystem;

//    private

    public final static int RED_FILTER = 0;
    public final static int WHITE_FILTER = 1;

    public ExtensionBrowserCanvas(Context context) {
        super(context);
        filter = new LinearLayout(context);
        animationFilter = new LinearLayout(context);
        frameFilter = new LinearLayout(context);
        browserCanvas = new Canvas(context);
        setView();
    }

    public ExtensionBrowserCanvas(Context context, AttributeSet attrs) {
        super(context, attrs);
        filter = new LinearLayout(context, attrs);
        animationFilter = new LinearLayout(context, attrs);
        frameFilter = new LinearLayout(context, attrs);
        browserCanvas = new Canvas(context, attrs);
        setView();
    }

    public ExtensionBrowserCanvas(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        filter = new LinearLayout(context, attrs);
        animationFilter = new LinearLayout(context, attrs);
        frameFilter = new LinearLayout(context, attrs);
        browserCanvas = new Canvas(context, attrs);
        setView();
    }

    public Canvas getBrowserCanvas() {
        return browserCanvas;
    }

    public void setView() {
        this.addView(browserCanvas);
        this.addView(filter);
        this.addView(animationFilter);
        this.addView(frameFilter);
    }

    public void setStandardFilter(int filterType) {
        if (filterType == 0) {
            filter.setBackgroundColor(getResources().getColor(R.color.red_filter));
        } else {
            filter.setBackgroundColor(getResources().getColor(R.color.white_filter));
        }
    }

    public void setAnimationFilter(Activity activity) {
        if (particleSystem != null) return;
        particleSystem = new ParticleSystem(activity, 80, R.drawable.star, 10000)
                .setSpeedModuleAndAngleRange(0f, 0.3f, 0, 360)
                .setAcceleration(0.00005f, 90);
        particleSystem.emit(animationFilter, 8);
    }

    public void setFrameFilter() {
        frameFilter.setBackgroundResource(R.drawable.frame);
    }

    public void clearFilter() {
        filter.setBackgroundColor(0);
        animationFilter.setBackgroundColor(0);
        frameFilter.setBackgroundColor(0);
        if (particleSystem != null) {
            particleSystem.cancel();
            particleSystem = null;
        }
    }

    public void addSrc(MediaStream stream, int trackNo) {
        browserCanvas.addSrc(stream, trackNo);
    }

    public void removeSrc(MediaStream stream, int trackNo) {
        browserCanvas.removeSrc(stream, trackNo);
    }
}
