package com.zykj.landous.classify;

import java.util.Dictionary;
import java.util.Hashtable;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Scroller;

import com.zykj.landous.R;

/**
 * �Զ�����Ի�����RelativeLayout,����������Ļ���Ч��
 * �ö���ʵ�ֹ���Ч��
 *
 * @author linyanjun
 * @blog http://blog.csdn.net/xiaolinxx
 */
public class AnimationSildingLayout extends RelativeLayout {
    /**
     * SildingLayout ���ֵĸ�����
     */
    private ViewGroup parentView;
    private ListView leftView;
    private ListView rightView;
    /**
     * ��������С����
     */
    private int mTouchSlop;
    /**
     * ���µ��X����
     */
    private int downX;
    /**
     * ���µ��Y����
     */
    private int downY;
    /**
     * ��ʱ�洢X����
     */
    private int tempX;
    /**
     * ��ʱ�洢ʱ��
     */
    private long tempTime;
    /**
     * ������
     */
    private Scroller mScroller;
    /**
     * SildingLayout�Ŀ��
     */
    private int viewWidth;

    private int parentViewScrollX;

    private boolean isSilding;

    /**
     * �ٶ�׷�ٶ���
     */
    private VelocityTracker velocityTracker;
    private static final int SNAP_VELOCITY = 300;

    private float leftlist_move_rate;
    private float leftlist_img_width;
    private OnSildingFinishListener onSildingFinishListener;


    private int leftViewTotalMove;
    /**
     * ��¼ѡ�е�itemλ��
     */
    private int selectItemPosition;
    //��ͷͼ��
    private ImageView arrowView;

    public AnimationSildingLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AnimationSildingLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        mScroller = new Scroller(context);
        leftlist_img_width = context.getResources().getDimension(R.dimen.leftlist_img_width);

        Log.d("LogonActivity", "SildingLayout() mTouchSlop:" + mTouchSlop);

    }

    public void initLayout(final ListView leftView,ListView rightView){
        this.leftView=leftView;
        this.rightView=rightView;
        this.parentView= (ViewGroup) rightView.getParent();
        parentView.setVisibility(INVISIBLE);
        arrowView= (ImageView) findViewById(R.id.img_arrow);
        leftView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

                if (leftView.getChildAt(0)!=null) {
                    int deltaY=tempScrollY-getScroll();
                     tempScrollY=getScroll();
                    arrowView.setY(arrowView.getY()+deltaY);
                }
            }


        });

    }

    private Dictionary<Integer, Integer> listViewItemHeights = new Hashtable<Integer, Integer>();
    private int tempScrollY;
    private int getScroll() {
        View c = leftView.getChildAt(0); //this is the first visible row
        int scrollY = -c.getTop();
        listViewItemHeights.put(leftView.getFirstVisiblePosition(), c.getHeight());
        for (int i = 0; i < leftView.getFirstVisiblePosition(); ++i) {
            if (listViewItemHeights.get(i) != null) // (this is a sanity check)
                scrollY += listViewItemHeights.get(i); //add all heights of the views that are gone
        }
        return scrollY;
    }


    /**
     * �¼����ز���
     */
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {

        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                // addVelocityTracker(ev);
                downX = tempX = (int) ev.getRawX();
                downY = (int) ev.getRawY();
                tempTime = System.currentTimeMillis();
                break;
            case MotionEvent.ACTION_MOVE:

                int moveX = (int) ev.getRawX();
                //�������������SildingLayout���������touch�¼�
                if ((Math.abs(moveX - downX) > mTouchSlop
                        && Math.abs((int) ev.getRawY() - downY) < mTouchSlop)) {
                    return true;
                }
                break;
            case MotionEvent.ACTION_UP:
                //recycleVelocityTracker();
                break;
        }

        return super.onInterceptTouchEvent(ev);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:


                break;
            case MotionEvent.ACTION_MOVE:
                if(parentView.getVisibility()==View.INVISIBLE){
                    break;
                }
                //���ƶ�ʱ��leftviewʼ�տɼ�
                setLeftViewVisiable();
                int moveX = (int) event.getRawX();
                int deltaX = tempX - moveX;
                leftViewTotalMove += deltaX;
                long nowTime = System.currentTimeMillis();
                long deltaTime = nowTime - tempTime;
                tempTime = nowTime;
                tempX = moveX;
                if (Math.abs(moveX - downX) > mTouchSlop
                        && Math.abs((int) event.getRawY() - downY) < mTouchSlop) {
                    isSilding = true;
                }

                if (moveX - downX >= 0 && isSilding) {
                    parentView.setX(parentView.getX() - deltaX);

                    //����listview���ı�
                    changeListView();
                    if (Math.abs(leftViewTotalMove * leftlist_move_rate) > 1) {
                        float targetX = leftView.getX() - leftViewTotalMove * leftlist_move_rate;
                        if (targetX >= -leftlist_img_width && leftView.getX() <= 0) {
                            changeListView();
                            leftView.setX(targetX);
                        }
                        leftViewTotalMove = 0;
                    }

                }
                break;
            case MotionEvent.ACTION_UP:
                if(parentView.getVisibility()==View.INVISIBLE){
                    break;
                }
                leftViewTotalMove = 0;
                isSilding = false;
                if (parentView.getX() >= viewWidth / 3) {//�ƶ���������1/3�����һ���
                    scrollRight();
                    extendLeftView();
                } else {
                    scrollOrigin();
                    leftViewSliding((int) leftView.getX(), -(int) leftlist_img_width, leftView, 500l);
                }

                break;
            case MotionEvent.ACTION_CANCEL:

                break;
        }

        return true;
    }

    private void onLeftViewShorted() {
        LeftViewAdapter ba = (LeftViewAdapter) leftView.getAdapter();
        ba.setHideFlag(true);
        ba.notifyDataSetChanged();
    }

    private void onLeftViewExtended() {
        LeftViewAdapter ba = setLeftViewVisiable();
        ba.notifyDataSetChanged();
    }

    private LeftViewAdapter setLeftViewVisiable() {
        LeftViewAdapter ba = (LeftViewAdapter) leftView.getAdapter();
        if (ba.isHideFlag()) {
            ba.setHideFlag(false);
            ba.notifyDataSetChanged();
        }
        return ba;
    }


    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

        super.onLayout(changed, l, t, r, b);
        if (changed) {
            // ��ȡrightView���ڲ��ֵĸ�����
            parentView = (ViewGroup) rightView.getParent();
            viewWidth = this.getWidth();
            leftlist_move_rate = leftlist_img_width / (viewWidth - leftlist_img_width);
            Log.d("LogonActivity", "SildingLayout��onLayout: leftlist_move_rate" + leftlist_move_rate);


        }
    }

    /**
     * ����OnSildingFinishListener, ��onSildingFinish()������finish Activity
     *
     * @param onSildingFinishListener
     */
    public void setOnSildingFinishListener(
            OnSildingFinishListener onSildingFinishListener) {
        this.onSildingFinishListener = onSildingFinishListener;
    }


    /**
     * ����������
     */
    private void scrollRight() {
        parentViewSliding((int) parentView.getX(), viewWidth, parentView, 500l, new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                onLeftViewExtended();
                if (onSildingFinishListener != null) {
                    onSildingFinishListener.onSildingFinish();
                }
            }
        });
    }

    /**
     * ��������ʼλ��
     */
    public void scrollOrigin() {
        parentViewSliding((int) parentView.getX(), 0, parentView, 500l, new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                parentViewScrollX = 0;
            }
        });
    }


    private void changeListView() {
        int visiblePosition = leftView.getFirstVisiblePosition();
        int lastVisiblePosition = leftView.getLastVisiblePosition();
        int selectedItemNum=selectItemPosition-visiblePosition;
        // Log.d("LogonActivity", String.format("SildingLayout��visiblePosition:%d ,lastVisiblePosition %d",visiblePosition,lastVisiblePosition));
        for (int i = 0; i <= lastVisiblePosition - visiblePosition; i++)//��ȡListView������Item��Ŀ
        {
            //    LinearLayout linearlayout = (LinearLayout)mListView.getChildAt(i);
            View view = leftView.getChildAt(i);

            if (view != null && view.getTag() != null) {
                LeftViewAdapter.ViewHolder holder = (LeftViewAdapter.ViewHolder) view.getTag();
                // holder.textView.setText(String.valueOf(parentViewScrollX));
                holder.description.setAlpha(Math.abs(parentView.getX()) / viewWidth);
                if(i==selectedItemNum){
                    holder.textView.setText("�ұ�ѡ���ˣ�");
                }
            }
        }
    }

    public void startSildingInAnimation(int position) {
        this.selectItemPosition = position;
        arrowView.setY(leftView.getChildAt(position-leftView.getFirstVisiblePosition()).getTop());
        tempScrollY=(int)getScroll();
        if (Math.abs(leftView.getX()) < leftlist_img_width * 0.2) { //���leftview�󲿷ֶ���ʾ��������ִ�ж���

            parentViewSliding(viewWidth, 0, parentView, 500l, new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);

                }

                @Override
                public void onAnimationStart(Animator animation) {
                    super.onAnimationStart(animation);
                    parentView.setVisibility(VISIBLE);
                }
            });

            leftViewSliding(0, -(int) leftlist_img_width, leftView, 500l);
        }
    }

    private void parentViewSliding(int fromx, int tox, View view, long duration, AnimatorListenerAdapter listener) {
        PropertyValuesHolder pvTY = PropertyValuesHolder.ofFloat("x", fromx,
                tox);
        ObjectAnimator objectAnimator = ObjectAnimator.ofPropertyValuesHolder(view, pvTY).setDuration(duration);
        objectAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
//                parentViewScrollX = ((Float) valueAnimator.getAnimatedValue()).intValue();
                changeListView();
            }
        });
        objectAnimator.addListener(listener);
        objectAnimator.setInterpolator(new DecelerateInterpolator());
        objectAnimator.start();
    }


    private void leftViewSliding(int formx, int tox, View view, long duration) {

        PropertyValuesHolder leftXPv = PropertyValuesHolder.ofFloat("x", formx,
                tox);
        ObjectAnimator leftOA = ObjectAnimator.ofPropertyValuesHolder(view, leftXPv).setDuration(duration);
        leftOA.setInterpolator(new DecelerateInterpolator());
        leftOA.start();
        leftOA.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                onLeftViewShorted();
            }
        });

    }


    private void extendLeftView() {
        PropertyValuesHolder leftXPv;
        if (leftView.getX() < 0) {
            leftXPv = PropertyValuesHolder.ofFloat("x", leftView.getX(),
                    0);
        } else {
            return;
        }

        ObjectAnimator leftOA = ObjectAnimator.ofPropertyValuesHolder(leftView, leftXPv).setDuration(500);
        leftOA.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                onLeftViewExtended();
            }
        });
        leftOA.setInterpolator(new DecelerateInterpolator());
        leftOA.start();

    }


    public interface OnSildingFinishListener {
        public void onSildingFinish();
    }

    /**
     * ����û����ٶȸ�����
     *
     * @param event
     */
    private void addVelocityTracker(MotionEvent event) {
        if (velocityTracker == null) {
            velocityTracker = VelocityTracker.obtain();
        }

        velocityTracker.addMovement(event);
    }

    /**
     * �Ƴ��û��ٶȸ�����
     */
    private void recycleVelocityTracker() {
        if (velocityTracker != null) {
            velocityTracker.recycle();
            velocityTracker = null;
        }
    }

    /**
     * ��ȡX����Ļ����ٶ�,����0���һ�������֮����
     *
     * @return
     */
    private int getScrollVelocity() {
        velocityTracker.computeCurrentVelocity(1000);
        int velocity = (int) velocityTracker.getXVelocity();
        return velocity;
    }




}
