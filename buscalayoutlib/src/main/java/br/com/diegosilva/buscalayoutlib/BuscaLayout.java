package br.com.diegosilva.buscalayoutlib;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.content.Context;
import android.content.res.TypedArray;
import android.support.v4.view.MotionEventCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Diego on 28/11/2015.
 */
public class BuscaLayout extends ViewGroup {

    private int txTituloResultadoId = -1;
    private View txTituloResultado;

    private int pnlResultadoId = -1;
    private View pnlResultado;


    private float mPrevMotionY;
    private float mInitialMotionX;
    private float mInitialMotionY;

    private static int panelState = PanelState.COLLAPSED;
    public static class PanelState {
        public static final int HIDDEN = 0;
        public static final int COLLAPSED = 1;
        public static final int ANCHORED = 2;
        public static final int EXPANDED = 3;
    }

    public BuscaLayout(Context context) {
        super(context);
    }

    public BuscaLayout(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.BuscaLayout);
        if(attrs != null){
            txTituloResultadoId = ta.getResourceId(R.styleable.BuscaLayout_blTxDetalheId, -1);
            pnlResultadoId = ta.getResourceId(R.styleable.BuscaLayout_pnlResultadoId, -1);
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int actionBarSize = UIUtils.getThemeAttributeDimensionSize(getContext(), android.R.attr.actionBarSize);
        int itemHeight = (b - actionBarSize) / getChildCount();

        int height = getMeasuredHeight();
        int width = getMeasuredWidth();


        View v1 = getChildAt(0);
        v1.layout(l, actionBarSize, r, b);

        View v2 = getChildAt(1);
        v2.layout(l + 20, actionBarSize + 100, r - 20, actionBarSize + 200);

        View v3 = getChildAt(2);
        v3.layout(l, (itemHeight * 2) + actionBarSize, r, (3 * itemHeight)+actionBarSize);
        v3.setY((height - txTituloResultado.getHeight()));
    }


    public void setPanelState(int panelState){

        int height = getMeasuredHeight();
        int width = getMeasuredWidth();

        switch (panelState){
            case PanelState.ANCHORED:{
                this.setVisibility(View.VISIBLE);
                PropertyValuesHolder pvhY = PropertyValuesHolder.ofFloat("y", height - pnlResultado.getHeight());
                ObjectAnimator.ofPropertyValuesHolder(pnlResultado, pvhY).setDuration(200).start();
                break;
            }
            case PanelState.COLLAPSED:{
                this.setVisibility(View.VISIBLE);
                PropertyValuesHolder pvhY = PropertyValuesHolder.ofFloat("y", (height - txTituloResultado.getHeight()));
                ObjectAnimator.ofPropertyValuesHolder(pnlResultado, pvhY).setDuration(200).start();
                break;
            }
            case PanelState.HIDDEN: {
                this.setVisibility(View.GONE);
            }
        }

        this.panelState = panelState;
    }

    public int getPanelState(){
        return this.panelState;
    }

    private void alternatePanelState(){
        switch(panelState){
            case PanelState.COLLAPSED:
                setPanelState(PanelState.ANCHORED);
                break;
            case PanelState.ANCHORED:
                setPanelState(PanelState.COLLAPSED);
                break;
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int wspec = MeasureSpec.makeMeasureSpec(getMeasuredWidth(), MeasureSpec.EXACTLY);
        int hspec = MeasureSpec.makeMeasureSpec(getMeasuredHeight(), MeasureSpec.EXACTLY);

        final int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        final int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        final int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        final int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        for (int i = 0; i < getChildCount(); i++) {
            View v = getChildAt(i);
            v.measure(wspec, hspec);
        }
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        if(txTituloResultadoId != -1){
            txTituloResultado = findViewById(txTituloResultadoId);
            txTituloResultado.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                   alternatePanelState();
                }
            });
        }

        if(pnlResultadoId != -1){
            pnlResultado = findViewById(pnlResultadoId);
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {

        final int action = MotionEventCompat.getActionMasked(ev);

        if(action == MotionEvent.ACTION_DOWN){

        }else if(action == MotionEvent.ACTION_MOVE){

        }

        return super.dispatchTouchEvent(ev);
    }


    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {

        final int action = MotionEventCompat.getActionMasked(ev);
        final float x = ev.getX();
        final float y = ev.getY();

        switch (action) {
            case MotionEvent.ACTION_DOWN: {
                mInitialMotionX = x;
                mInitialMotionY = y;
                break;
            }

            case MotionEvent.ACTION_MOVE: {
                final float adx = Math.abs(x - mInitialMotionX);
                final float ady = Math.abs(y - mInitialMotionY);

                break;
            }

            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                break;
        }
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }
}
