package com.fyjr.baselibrary.views;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.AbsListView;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.fyjr.baselibrary.R;
import com.fyjr.baselibrary.utils.Log;

/**
 * Created by QNapex on 2016/11/11.
 * 下拉刷新，上拉加载控件
 */

public class RefreshLayout extends SwipeRefreshLayout implements AbsListView.OnScrollListener, SwipeRefreshLayout.OnRefreshListener {

    private Context context;
    private AbsListView mListView;
    private OnLoadListener mOnLoadListener;
    private View mFooter;
    private TextView tvLoad;
    private ProgressBar bar;
    private boolean isLoading = false;
    private boolean hasFooter = false;
    private boolean refreshEnable = true;
    private AbsListView.OnScrollListener scrollListener;

    private float startY;
    private float startX;
    // 记录viewPager是否拖拽的标记
    private boolean mIsVpDragger;
    private int mTouchSlop;

    public RefreshLayout(Context context) {
        this(context, null);
    }

    public RefreshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        this.context = context;
        mFooter = LayoutInflater.from(context).inflate(R.layout.listview_footer, null, false);
        tvLoad = (TextView) mFooter.findViewById(R.id.textView);
        bar = (ProgressBar) mFooter.findViewById(R.id.progress);
        setProgressViewOffset(true, -70, 200);
        setSize(SwipeRefreshLayout.LARGE);
        setDistanceToTriggerSync(300);
        setColorSchemeResources(
                android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        try {
            // 初始化ListView对象
            if (mListView == null) {
                getListView();
            }
            setOnRefreshListener(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取ListView对象
     */
    private void getListView() {
        int childs = getChildCount();
        for (int i = 0; i < childs; i++) {
            View childView = getChildAt(i);
            if (childView instanceof ListView) {
                mListView = (ListView) childView;
            } else if (childView instanceof GridViewWithHeaderAndFooter) {
                mListView = (GridViewWithHeaderAndFooter) childView;
            } else if (childView instanceof GridView) {
                mListView = (GridView) childView;
            }
        }
        // 设置滚动监听器给ListView
        mListView.setOnScrollListener(this);
    }

    /**
     * 设置ListView
     *
     * @param listView
     */
    public void setListView(ListView listView) {
        mListView = listView;
        mListView.setOnScrollListener(this);
    }

    /**
     * 设置滚动监听对象
     *
     * @param scrollListener
     */
    public void setOnScrollListener(AbsListView.OnScrollListener scrollListener) {
        this.scrollListener = scrollListener;
    }

    /**
     * 设置加载状态,添加或者移除加载更多圆形进度条
     *
     * @param loading
     */
    public void setLoading(boolean loading) {
        isLoading = loading;
//        if (isLoading) {
//            addFooterView();
//        } else {
//            removeFooterView();
//        }
    }

    /**
     * 设置监听器
     *
     * @param loadListener
     */
    public void setOnLoadListener(OnLoadListener loadListener) {
        mOnLoadListener = loadListener;
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if (scrollListener != null)
            scrollListener.onScrollStateChanged(view, scrollState);
        if (scrollState == SCROLL_STATE_IDLE) {
            if (view.getLastVisiblePosition() == view.getCount() - 1 && !isLoading) {
                setLoading(true);
                mOnLoadListener.onLoad();
            }
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if (scrollListener != null)
            scrollListener.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);
    }

    /**
     * 添加加载状态显示
     */
    public void addFooterView() {
        try {
            if (!hasFooter) {
                Log.e("addFooterView", ": " + mListView);
                if (mListView instanceof ListView) {
                    ((ListView) mListView).addFooterView(mFooter);
                    hasFooter = true;
                } else if (mListView instanceof GridViewWithHeaderAndFooter) {
                    Log.e("addFooterView", "addFooterView");
                    ((GridViewWithHeaderAndFooter) mListView).addFooterView(mFooter);
                    hasFooter = true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 移除加载状态显示
     */
    public void removeFooterView() {
        try {
            if (hasFooter) {
                if (mListView instanceof ListView) {
                    ((ListView) mListView).removeFooterView(mFooter);
                    hasFooter = false;
                } else if (mListView instanceof GridViewWithHeaderAndFooter) {
                    ((GridViewWithHeaderAndFooter) mListView).removeFooterView(mFooter);
                    hasFooter = false;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 设置是否可以下拉刷新
     *
     * @param enable
     */
    public void setRefreshEnable(boolean enable) {
        refreshEnable = enable;
    }

    /**
     * 显示加载中状态
     */
    public void showLoading() {
        isLoading = false;
        bar.setVisibility(VISIBLE);
        tvLoad.setText("加载中");
    }

    /**
     * 显示没有更多数据
     */
    public void setNoMoreData() {
        isLoading = true;
        bar.setVisibility(GONE);
        tvLoad.setText("没有更多了");
    }

    @Override
    public void onRefresh() {
        if (refreshEnable) {
            if (mOnLoadListener != null)
                mOnLoadListener.onRefresh();
        } else {
            setRefreshing(false);
        }
    }

    public interface OnLoadListener {
        void onRefresh();

        void onLoad();
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        int action = ev.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                // 记录手指按下的位置
                startY = ev.getY();
                startX = ev.getX();
                // 初始化标记
                mIsVpDragger = false;
                break;
            case MotionEvent.ACTION_MOVE:
                // 如果viewpager正在拖拽中，那么不拦截它的事件，直接return false；
                if (mIsVpDragger) {
                    return false;
                }

                // 获取当前手指位置
                float endY = ev.getY();
                float endX = ev.getX();
                float distanceX = Math.abs(endX - startX);
                float distanceY = Math.abs(endY - startY);
                // 如果X轴位移大于Y轴位移，那么将事件交给viewPager处理。
                if (distanceX > mTouchSlop && distanceX > distanceY) {
                    mIsVpDragger = true;
                    return false;
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                // 初始化标记
                mIsVpDragger = false;
                break;
        }
        // 如果是Y轴位移大于X轴，事件交给swipeRefreshLayout处理。
        return super.onInterceptTouchEvent(ev);
    }
}
