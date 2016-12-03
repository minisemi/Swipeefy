package linkopinghackers.swipeefy.TabLayoutActivity;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by Alexander on 2016-08-10.
 */
public class ScrollableViewPager extends ViewPager{
    private Boolean scrollable = true;

    public ScrollableViewPager(Context context) {
        super(context);
    }

    public ScrollableViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        if (scrollable){
            return super.onInterceptTouchEvent(event);
        }
        // Never allow swiping to switch between pages
        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (scrollable){
            return super.onTouchEvent(event);
        }
        // Never allow swiping to switch between pages
        return false;
    }

    public void setScrollable (Boolean scrollable){
        this.scrollable = scrollable;

    }
}
