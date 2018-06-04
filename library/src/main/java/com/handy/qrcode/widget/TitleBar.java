package com.handy.qrcode.widget;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Build;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.handy.qrcode.module.single.ScanSingleConfig;
import com.handy.qrcode.utils.LogUtils;

import java.lang.reflect.Field;
import java.util.LinkedList;

/**
 * 类名
 *
 * @author LiuJie https://www.Handy045.com
 * @description 类功能内容
 * @date Created in 2018/5/28 下午7:13
 * @modified By LiuJie
 */
public class TitleBar extends ViewGroup implements View.OnClickListener {
    /**
     * 主标题字体大小
     */
    public static final int DEFAULT_MAIN_TEXT_SIZE = 17;
    /**
     * 副标题字体大小
     */
    public static final int DEFAULT_SUB_TEXT_SIZE = 11;
    /**
     * 动作按钮字体大小
     */
    public static final int DEFAULT_ACTION_TEXT_SIZE = 13;
    /**
     * 动作按钮图片大小
     */
    public static final int DEFAULT_ACTION_IMG_SIZE = 18;
    /**
     * 标题栏高度
     */
    public static final int DEFAULT_TITLEBAR_HEIGHT = 48;
    /**
     * 分割线高度
     */
    public static final int DEFAULT_TOPLINE_HEIGHT = 0;
    /**
     * 分割线高度
     */
    public static final int DEFAULT_BOTTOMLINE_HEIGHT = 0;
    public View StatusBar;
    public View TopLine;
    public LinearLayout LeftLayout;
    public LinearLayout CenterLayout;
    public MarqueeTextView CenterText;
    public MarqueeTextView SubTitleText;
    public View CustomCenterView;
    public LinearLayout RightLayout;
    public View BottomLine;
    /**
     * 屏幕宽度
     */
    private int ScreenWidth;
    /**
     * 屏幕高度
     */
    private int ScreenHeight;
    /**
     * 状态栏背景
     */
    private int StatusBarColor = 0;
    /**
     * 状态栏高度
     */
    private int StatusBarHeight = 0;
    /**
     * 动作按钮内边距
     */
    private int ActionPadding = dpTopx(4);
    /**
     * 动作按钮外边距
     */
    private int OutPadding = dpTopx(8);
    /**
     * 标题文本字体颜色
     */
    private int TitleTextColor = Color.WHITE;
    /**
     * 动作按钮字体颜色
     */
    private int ActionTextColor = Color.WHITE;
    /**
     * 标题栏高度
     */
    private int TitleBarHeight = dpTopx(DEFAULT_TITLEBAR_HEIGHT);
    /**
     * 顶部分割线高度
     */
    private int TopLineHeight = dpTopx(DEFAULT_TOPLINE_HEIGHT);
    /**
     * 底部分割线高度
     */
    private int BottomLineHeight = dpTopx(DEFAULT_BOTTOMLINE_HEIGHT);
    /**
     * 全局高度
     */
    private int ParentHeight;
    private Context context;

    public TitleBar(Context context) {
        super(context);
        init(context);
    }

    public TitleBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public TitleBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    /**
     * dp转px
     *
     * @param dpValue dp值
     */
    public int dpTopx(int dpValue) {
        final float scale = Resources.getSystem().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * px转sp
     *
     * @param pxValue px值
     */
    public int px2sp(float pxValue) {
        float fontScale = Resources.getSystem().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }

    /**
     * 获取状态栏高度高度
     */
    private int getStatusBarHeight(Context context) {
        try {
            Object obj = Class.forName("com.android.internal.R$dimen").newInstance();
            Field field = Class.forName("com.android.internal.R$dimen").getField("status_bar_height");
            return context.getResources().getDimensionPixelSize(Integer.parseInt(field.get(obj).toString()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    private void init(Context context) {
        this.context = context;
        if (ScanSingleConfig.KEY_SCREEN_ORIENTATION == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
            ScreenWidth = getResources().getDisplayMetrics().heightPixels;
            ScreenHeight = getResources().getDisplayMetrics().widthPixels;
        } else if (ScanSingleConfig.KEY_SCREEN_ORIENTATION == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
            ScreenWidth = getResources().getDisplayMetrics().widthPixels;
            ScreenHeight = getResources().getDisplayMetrics().heightPixels;
        }

        ParentHeight = TitleBarHeight + StatusBarHeight + TopLineHeight + BottomLineHeight;
        initView(context);
    }

    private void initView(Context context) {
        StatusBar = new View(context);
        StatusBarHeight = getStatusBarHeight(context);
        StatusBar.setBackgroundColor(Color.TRANSPARENT);

        TopLine = new View(context);
        TopLine.setBackgroundColor(Color.TRANSPARENT);

        LeftLayout = new LinearLayout(context);
        LeftLayout.setGravity(Gravity.CENTER_VERTICAL);
        LeftLayout.setPadding(OutPadding, 0, OutPadding, 0);
        LeftLayout.setBackgroundColor(Color.TRANSPARENT);

        CenterText = new MarqueeTextView(context);
        CenterText.setTextSize(DEFAULT_MAIN_TEXT_SIZE);
        CenterText.setSingleLine();
        CenterText.setGravity(Gravity.CENTER);
        CenterText.setTextColor(TitleTextColor);
        CenterText.setBackgroundColor(Color.TRANSPARENT);
        CenterText.setEllipsize(TextUtils.TruncateAt.MARQUEE);
        SubTitleText = new MarqueeTextView(context);
        SubTitleText.setTextSize(DEFAULT_SUB_TEXT_SIZE);
        SubTitleText.setSingleLine();
        SubTitleText.setGravity(Gravity.CENTER);
        SubTitleText.setTextColor(TitleTextColor);
        SubTitleText.setBackgroundColor(Color.TRANSPARENT);
        SubTitleText.setEllipsize(TextUtils.TruncateAt.MARQUEE);
        CenterLayout = new LinearLayout(context);
        CenterLayout.addView(CenterText);
        CenterLayout.addView(SubTitleText);
        CenterLayout.setGravity(Gravity.CENTER);
        CenterLayout.setBackgroundColor(Color.TRANSPARENT);

        RightLayout = new LinearLayout(context);
        RightLayout.setGravity(Gravity.CENTER_VERTICAL);
        RightLayout.setPadding(OutPadding, 0, OutPadding, 0);
        RightLayout.setBackgroundColor(Color.TRANSPARENT);

        BottomLine = new View(context);
        BottomLine.setBackgroundColor(Color.TRANSPARENT);

        if (context instanceof Activity) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                StatusBarColor = ((Activity) context).getWindow().getStatusBarColor();
            }
            setImmersive((Activity) context, false);
        }

        addView(StatusBar, new ViewGroup.LayoutParams(LayoutParams.MATCH_PARENT, StatusBarHeight));
        addView(TopLine, new LayoutParams(LayoutParams.MATCH_PARENT, TopLineHeight));
        addView(LeftLayout, new LayoutParams(LayoutParams.WRAP_CONTENT, TitleBarHeight));
        addView(CenterLayout, new LayoutParams(LayoutParams.WRAP_CONTENT, TitleBarHeight));
        addView(RightLayout, new LayoutParams(LayoutParams.WRAP_CONTENT, TitleBarHeight));
        addView(BottomLine, new LayoutParams(LayoutParams.MATCH_PARENT, BottomLineHeight));
    }

    /**
     * 设置系统状态栏是否可见，安卓系统版本大于等于19
     */
    public TitleBar setImmersive(Activity activity, boolean immersive) {
        if (immersive) {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
                StatusBarHeight = 0;
                activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
            } else {
                StatusBarHeight = getStatusBarHeight(context);
                activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                    activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
                    activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                    activity.getWindow().setStatusBarColor(Color.TRANSPARENT);
                }
            }
        } else {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
                StatusBarHeight = 0;
                activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
            } else {
                StatusBarHeight = getStatusBarHeight(context);
                activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                    activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
                    activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                    activity.getWindow().setStatusBarColor(StatusBarColor);
                }
            }
        }
        onRefresh();
        return this;
    }

    public TitleBar setStatusBarBackground(int statusBarBackground) {
        try {
            StatusBar.setBackgroundResource(statusBarBackground);
        } catch (Exception e) {
            LogUtils.d("TitleBar", "资源ID设置有误");
        }
        onRefresh();
        return this;
    }

    /**
     * 顶部分割线高度
     */
    public TitleBar setTopLineHeight(int dividerHeight) {
        if (dividerHeight >= 0) {
            TopLineHeight = dpTopx(dividerHeight);
            TopLine.getLayoutParams().height = TopLineHeight;
        } else {
            LogUtils.e("TitleBar", " TopLineHeight 设置无效");
        }
        onRefresh();
        return this;
    }

    public TitleBar setTopLineBackground(int backgroundresId) {// 顶部分割线背景(backgroundresId = R.color.black)
        try {
            TopLine.setBackgroundResource(backgroundresId);
        } catch (Exception e) {
            LogUtils.d("TitleBar", "资源ID设置有误");
        }
        onRefresh();
        return this;
    }

    /**
     * 标题栏高度
     */
    public TitleBar setTitleBarHeight(int height) {
        if (height >= 0) {
            TitleBarHeight = dpTopx(height);
        } else {
            TitleBarHeight = dpTopx(DEFAULT_TITLEBAR_HEIGHT);
            LogUtils.e("TitleBar", " TitleBarHeight 设置无效，恢复为默认高度");
        }
        onRefresh();
        return this;
    }

    /**
     * 标题栏背景
     */
    public TitleBar setTitleBarBackground(int resID) {
        try {
            setBackgroundResource(resID);
        } catch (Exception e) {
            LogUtils.d("TitleBar", "资源ID设置有误");
        }
        onRefresh();
        return this;
    }

    /**
     * 新增多个动作按钮
     */
    public void addLeftActions(ActionList actionList) {
        int actions = actionList.size();
        for (int i = 0; i < actions; i++) {
            addLeftAction(actionList.get(i));
        }
    }

    /**
     * 新增单个动作按钮
     */
    public View addLeftAction(Action action) {
        final int index = LeftLayout.getChildCount();
        return addLeftAction(action, index);
    }

    /**
     * 指定位置新增动作按钮
     */
    public View addLeftAction(Action action, int index) {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
        View view = inflateAction(action);
        action.setActionView(view);
        LeftLayout.addView(view, index, params);
        return view;
    }

    public void removeAllLeftActions() {// 移除全部动作按钮
        LeftLayout.removeAllViews();
    }

    public void removeLeftActionAt(int index) {// 移除指定位置动作按钮
        LeftLayout.removeViewAt(index);
    }

    /**
     * 移除单个动作按钮
     */
    public void removeLeftAction(Action action) {
        int childCount = LeftLayout.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View view = LeftLayout.getChildAt(i);
            if (view != null) {
                final Object tag = view.getTag();
                if (tag instanceof Action && tag.equals(action)) {
                    LeftLayout.removeView(view);
                }
            }
        }
    }

    public int getLeftActionCount() {// 获取动作按钮数量
        return LeftLayout.getChildCount();
    }

    /**
     * 标题内容设置（主标题与副标题用"\n"或"\t"分割）
     */
    @SuppressLint("SetTextI18n")
    public TitleBar setTitle(CharSequence title) {
        CenterText.setVisibility(View.VISIBLE);
        SubTitleText.setVisibility(View.VISIBLE);
        int index = title.toString().indexOf("\n");
        if (index > 0) {
            CenterLayout.setOrientation(LinearLayout.VERTICAL);
            CenterText.setText(title.subSequence(0, index));
            SubTitleText.setText(title.subSequence(index + 1, title.length()));
        } else {
            index = title.toString().indexOf("\t");
            if (index > 0) {
                CenterLayout.setOrientation(LinearLayout.HORIZONTAL);
                CenterText.setText(title.subSequence(0, index));
                SubTitleText.setText("  " + title.subSequence(index + 1, title.length()));
            } else {
                CenterText.setText(title);
                SubTitleText.setVisibility(View.GONE);
            }
        }
        return this;
    }

    public MarqueeTextView getCenterTextView() {
        return CenterText;
    }

    public MarqueeTextView getSubTitleTextView() {
        return SubTitleText;
    }

    public String getCenterText() {
        return CenterText.getText().toString();
    }

    public String getSubTitleText() {
        return SubTitleText.getText().toString();
    }

    /**
     * 标题内容设置（主标题与副标题用"\n"或"\t"分割）
     */
    public TitleBar setTitle(int resid) {
        setTitle(getResources().getString(resid));
        return this;
    }

    /**
     * 标题内容点击事件
     */
    public TitleBar setCenterClickListener(OnClickListener l) {
        CenterLayout.setOnClickListener(l);
        return this;
    }

    /**
     * 主标题字体颜色
     */
    public TitleBar setTitleColor(int resid) {
        CenterText.setTextColor(resid);
        return this;
    }

    /**
     * 主标题字体大小
     */
    public TitleBar setTitleSize(float size) {
        CenterText.setTextSize(size);
        return this;
    }

    /**
     * 主标题字体大小
     */
    public TitleBar setTitleSizeDimens(int resdimensid) {
        CenterText.setTextSize(px2sp(context.getResources().getDimension(resdimensid)));
        return this;
    }

    /**
     * 主标题背景
     */
    public TitleBar setTitleBackground(int resid) {
        try {
            CenterText.setBackgroundResource(resid);
        } catch (Exception e) {
            LogUtils.d("TitleBar", "资源ID设置有误");
        }
        return this;
    }

    /**
     * 主标题点击事件
     */
    public TitleBar setTitleOnClickListener(OnClickListener listener) {
        CenterText.setOnClickListener(listener);
        return this;
    }

    /**
     * 副标题字体颜色
     */
    public TitleBar setSubTitleColor(int resid) {
        SubTitleText.setTextColor(resid);
        return this;
    }

    /**
     * 副标题字体大小
     */
    public TitleBar setSubTitleSize(float size) {
        SubTitleText.setTextSize(size);
        return this;
    }

    /**
     * 副标题字体大小
     */
    public TitleBar setSubTitleSizeDimens(int resdimensid) {
        SubTitleText.setTextSize(px2sp(context.getResources().getDimension(resdimensid)));
        return this;
    }

    /**
     * 副标题背景
     */
    public TitleBar setSubTitleBackground(int resid) {
        try {
            SubTitleText.setBackgroundResource(resid);
        } catch (Exception e) {
            LogUtils.d("TitleBar", "资源ID设置有误");
        }
        return this;
    }

    /**
     * 副标题点击事件
     */
    public TitleBar setSubTitleOnClickListener(OnClickListener listener) {
        SubTitleText.setOnClickListener(listener);
        return this;
    }

    /**
     * 自定义标题内容样式
     */
    public TitleBar setCustomTitle(View titleView) {
        if (titleView == null) {
            CenterText.setVisibility(View.VISIBLE);
            if (CustomCenterView != null) {
                CenterLayout.removeView(CustomCenterView);
            }
        } else {
            if (CustomCenterView != null) {
                CenterLayout.removeView(CustomCenterView);
            }
            LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
            CustomCenterView = titleView;
            CenterLayout.addView(titleView, layoutParams);
            CenterText.setVisibility(View.GONE);
        }
        return this;
    }

    /**
     * 动作按钮字体颜色
     */
    public TitleBar setActionTextColor(int colorResId) {
        ActionTextColor = colorResId;
        return this;
    }

    /**
     * 新增多个动作按钮
     */
    public void addRightActions(ActionList actionList) {
        int actions = actionList.size();
        for (int i = 0; i < actions; i++) {
            addRightAction(actionList.get(i));
        }
    }

    /**
     * 新增单个动作按钮
     */
    public View addRightAction(Action action) {
        final int index = RightLayout.getChildCount();
        return addRightAction(action, index);
    }

    /**
     * 指定位置新增动作按钮
     */
    public View addRightAction(Action action, int index) {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
        View view = inflateAction(action);
        action.setActionView(view);
        RightLayout.addView(view, index, params);
        return view;
    }

    public void removeRightActionAt(int index) {// 移除指定位置动作按钮
        RightLayout.removeViewAt(index);
    }

    /**
     * 移除单个动作按钮
     */
    public void removerightAction(Action action) {
        int childCount = RightLayout.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View view = RightLayout.getChildAt(i);
            if (view != null) {
                final Object tag = view.getTag();
                if (tag instanceof Action && tag.equals(action)) {
                    RightLayout.removeView(view);
                }
            }
        }
    }

    public void removeAllRightActions() {// 移除全部动作按钮
        RightLayout.removeAllViews();
    }

    public int getRightActionCount() {// 获取动作按钮数量
        return RightLayout.getChildCount();
    }

    /**
     * 底部分割线高度
     */
    public TitleBar setBottomLineHeight(int dividerHeight) {
        if (dividerHeight >= 0) {
            BottomLineHeight = dpTopx(dividerHeight);
            BottomLine.getLayoutParams().height = BottomLineHeight;
        } else {
            LogUtils.e("TitleBar", " BottomLineHeight 设置无效");
        }
        onRefresh();
        return this;
    }

    /**
     * 底部分割线背景
     */
    public TitleBar setBottomLineBackground(int backgroundresId) {
        try {
            BottomLine.setBackgroundResource(backgroundresId);
        } catch (Exception e) {
            LogUtils.d("TitleBar", "资源ID设置有误");
        }
        onRefresh();
        return this;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        measureChild(StatusBar, widthMeasureSpec, heightMeasureSpec);
        measureChild(TopLine, widthMeasureSpec, heightMeasureSpec);
        measureChild(LeftLayout, widthMeasureSpec, heightMeasureSpec);
        measureChild(RightLayout, widthMeasureSpec, heightMeasureSpec);
        if (LeftLayout.getMeasuredWidth() > RightLayout.getMeasuredWidth()) {
            CenterLayout.measure(MeasureSpec.makeMeasureSpec(ScreenWidth - 2 * LeftLayout.getMeasuredWidth(), MeasureSpec.EXACTLY), heightMeasureSpec);
        } else {
            CenterLayout.measure(MeasureSpec.makeMeasureSpec(ScreenWidth - 2 * RightLayout.getMeasuredWidth(), MeasureSpec.EXACTLY), heightMeasureSpec);
        }
        measureChild(BottomLine, widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(MeasureSpec.getSize(widthMeasureSpec), ParentHeight);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        StatusBar.layout(0, 0, getMeasuredWidth(), StatusBar.getMeasuredHeight());
        TopLine.layout(0, StatusBarHeight, getMeasuredWidth(), StatusBar.getMeasuredHeight() + TopLine.getMeasuredHeight());
        LeftLayout.layout(0, StatusBarHeight + TopLineHeight, LeftLayout.getMeasuredWidth(), getMeasuredHeight() - BottomLine.getMeasuredHeight());
        RightLayout.layout(ScreenWidth - RightLayout.getMeasuredWidth(), StatusBarHeight + TopLineHeight, ScreenWidth, getMeasuredHeight() - BottomLine.getMeasuredHeight());
        if (LeftLayout.getMeasuredWidth() > RightLayout.getMeasuredWidth()) {
            CenterLayout.layout(LeftLayout.getMeasuredWidth(), StatusBarHeight + TopLineHeight, ScreenWidth - LeftLayout.getMeasuredWidth(), getMeasuredHeight() - BottomLine.getMeasuredHeight());
        } else {
            CenterLayout.layout(RightLayout.getMeasuredWidth(), StatusBarHeight + TopLineHeight, ScreenWidth - RightLayout.getMeasuredWidth(), getMeasuredHeight() - BottomLine.getMeasuredHeight());
        }
        BottomLine.layout(0, getMeasuredHeight() - BottomLine.getMeasuredHeight(), getMeasuredWidth(), getMeasuredHeight());
    }

    /**
     * 初始化动作按钮布局控件
     */
    private View inflateAction(Action action) {
        LinearLayout view = new LinearLayout(getContext());
        view.setGravity(Gravity.CENTER_VERTICAL);
        view.setPadding(ActionPadding, 0, ActionPadding, 0);
        view.setTag(action);
        view.setOnClickListener(this);

        if (action.setDrawable() != 0) {
            //添加动作按钮的图片
            ImageView img = new ImageView(getContext());
            LayoutParams imglp = action.setDrawableSize() == 0 ? new LayoutParams(dpTopx(DEFAULT_ACTION_IMG_SIZE), dpTopx(DEFAULT_ACTION_IMG_SIZE)) : new LayoutParams(dpTopx(action.setDrawableSize()), dpTopx(action.setDrawableSize()));
            img.setLayoutParams(imglp);
            img.setImageResource(action.setDrawable());
            img.setClickable(false);
            view.addView(img);
        }

        //若文字内容不为空，添加动作按钮的文字
        if (!TextUtils.isEmpty(action.setText())) {
            TextView text = new TextView(getContext());
            text.setGravity(Gravity.CENTER);
            text.setText(action.setText());
            //动作按钮中文字举例图片4dp
            text.setPadding(dpTopx(4), 0, 0, 0);
            text.setTextSize(action.setTextSize() == 0 ? DEFAULT_ACTION_TEXT_SIZE : action.setTextSize());
            if (action.setTextColor() == 0) {
                text.setTextColor(ActionTextColor);
            } else {
                text.setTextColor(action.setTextColor());
            }
            view.addView(text);
        }
        return view;
    }

    @Override
    public void onClick(View view) {
        final Object tag = view.getTag();
        if (tag instanceof Action) {
            final Action action = (Action) tag;
            action.onClick();
        }
    }

    /**
     * 获取单个动作按钮布局容器
     */
    public View getViewByAction(Action action) {
        View view = findViewWithTag(action);
        return view;
    }

    /**
     * 刷新控件
     */
    public void onRefresh() {
        ParentHeight = TitleBarHeight + StatusBarHeight + TopLineHeight + BottomLineHeight;
        setMeasuredDimension(getMeasuredWidth(), ParentHeight);
        requestLayout();
        invalidate();
    }

    /**
     * 获取标题栏全局高度
     */
    public int getParentHeight() {
        return ParentHeight;
    }

    /**
     * 自定义对象类
     */
    public static abstract class Action {
        private View actionView = null;

        public abstract void onClick();

        public String setText() {
            return null;
        }

        public int setTextColor() {
            return Color.WHITE;
        }

        public int setTextSize() {
            return DEFAULT_ACTION_TEXT_SIZE;
        }

        public int setDrawable() {
            return 0;
        }

        public int setDrawableSize() {
            return DEFAULT_ACTION_IMG_SIZE;
        }

        public View getActionView() {
            return actionView;
        }

        public void setActionView(View actionView) {
            this.actionView = actionView;
        }
    }

    @SuppressWarnings("serial")
    public static class ActionList extends LinkedList<Action> {
    }

    public class MarqueeTextView extends android.support.v7.widget.AppCompatTextView {
        public MarqueeTextView(Context context) {
            super(context);
        }

        public MarqueeTextView(Context context, AttributeSet attrs) {
            super(context, attrs);
        }

        public MarqueeTextView(Context context, AttributeSet attrs, int defStyle) {
            super(context, attrs, defStyle);
        }

        @Override
        public boolean isFocused() {
            return true;
        }
    }
}
