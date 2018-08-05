package com.can.canutils.ui;

import android.animation.ValueAnimator;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.can.canutils.R;
import com.can.mvp.base.BaseActivity;
import com.can.mvp.mvps.models.BaseModel;
import com.can.mvp.mvps.presenters.QRCodePresenter;
import com.can.mvp.mvps.views.QRCodeView;
import com.can.mvp.utils.ToastUtils;
import com.can.mvp.views.BindView;

/**
 * Created by can on 2018/4/9.
 * 生成二维码
 */

public class QRCodeActivity extends BaseActivity implements QRCodeView {

    @BindView(id = com.can.mvp.R.id.et_content)
    private EditText et_content;
    @BindView(id = R.id.ll_container)
    private LinearLayout ll_container;
    @BindView(id = R.id.sv)
    private ScrollView sv;


    private QRCodePresenter presenter;

    @Override
    public int getLayoutId() {
        return com.can.mvp.R.layout.activity_qrcode;
    }

    @Override
    public void initData(Bundle bundle) {
        super.initData(bundle);
        presenter = new QRCodePresenter(this,new BaseModel(mCompositeSubscription));
    }

    @Override
    public void initView(View view) {
        super.initView(view);

        ll_container.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            private int [] sc;
            private int scrollHeight;

            @Override
            public void onGlobalLayout() {
                Rect rect = new Rect();
                ll_container.getWindowVisibleDisplayFrame(rect);
                sc = new int[2];
                et_content.getLocationOnScreen(sc);
                int screenHeight = ll_container.getRootView().getHeight();
                int softHeight = screenHeight - rect.bottom;
                if(softHeight>140){
                    scrollHeight = sc[1]-softHeight-et_content.getHeight();
                    if(ll_container.getScrollY()!=scrollHeight&&scrollHeight>0){
//                        ll_container.scrollTo(0,scrollHeight);
                        scrollToPos(0,scrollHeight);
                    }
                }else{
                    if(ll_container.getScrollY()!=0){
//                        ll_container.scrollTo(scrollHeight,0);
                        ll_container.scrollTo(0,0);
                    }
                }
            }
        });

        et_content.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                //重写onTouch()事件,在事件里通过requestDisallowInterceptTouchEvent(boolean)
                //方法来设置父类的不可用,true表示父类的不可用
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    sv.requestDisallowInterceptTouchEvent(false);
                } else {
                    sv.requestDisallowInterceptTouchEvent(true);
                }
                return false;
            }
        });

    }

    private void scrollToPos(int start, int end) {
        ValueAnimator animator = ValueAnimator.ofInt(start, end);
        animator.setDuration(250);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                ll_container.scrollTo(0, (Integer) valueAnimator.getAnimatedValue());
            }
        });
        animator.start();
    }

    @Override
    public void onError(String error) {
        ToastUtils.getInstance(this).showText(error);
    }

    @Override
    public void onSuccess(Bitmap bitmap) {

    }


}
