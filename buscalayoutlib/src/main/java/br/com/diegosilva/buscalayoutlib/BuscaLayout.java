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

    private int pnlCampoBuscaId = -1;
    private View pnlCampoBusca;
    private int pnlSearchBotton;

    private float mPrevMotionY;
    private float mInitialMotionX;
    private float mInitialMotionY;

    private int inversePanelState = PanelState.EXPANDED;
    private int panelState = PanelState.COLLAPSED;

    private int actionBarSize;

    public static class PanelState {
        public static final int HIDDEN = 0;
        public static final int COLLAPSED = 1;
        public static final int ANCHORED = 2;
        public static final int EXPANDED = 3;
    }

    private boolean primeiroLayout = false;

    private PanelStateListener panelStateListener;

    public BuscaLayout(Context context) {
        super(context);
    }

    public BuscaLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.BuscaLayout);
        if (attrs != null) {
            txTituloResultadoId = ta.getResourceId(R.styleable.BuscaLayout_pnlTxResultadoId, -1);
            pnlResultadoId = ta.getResourceId(R.styleable.BuscaLayout_pnlResultadoId, -1);
            pnlCampoBuscaId = ta.getResourceId(R.styleable.BuscaLayout_pnlCampoBuscaId, -1);
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        actionBarSize = UIUtils.getThemeAttributeDimensionSize(getContext(), android.R.attr.actionBarSize);
        //int itemHeight = (b - actionBarSize) / getChildCount();

        int height = getMeasuredHeight();
        int width = getMeasuredWidth();

        View v1 = getChildAt(0);
        v1.layout(l, t, r, b);

        View v2 = getChildAt(1);
        v2.layout(l, t + 15, r, (t + 15) + v2.getMeasuredHeight());
        pnlSearchBotton = (t + 15) + pnlCampoBusca.getMeasuredHeight();

        View v3 = getChildAt(2);
        v3.layout(l, pnlSearchBotton, r, b-actionBarSize);

        if (primeiroLayout) {
            v3.setY((height - txTituloResultado.getHeight()) - actionBarSize);
        }
        primeiroLayout = false;
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

        if (txTituloResultadoId != -1) {
            txTituloResultado = findViewById(txTituloResultadoId);
            txTituloResultado.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    changePanelState();
                }
            });
        }

        if (pnlResultadoId != -1) {
            pnlResultado = findViewById(pnlResultadoId);
        }

        if (pnlCampoBuscaId != -1) {
            pnlCampoBusca = findViewById(pnlCampoBuscaId);

        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (h != oldh) {
            primeiroLayout = true;
        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        primeiroLayout = true;
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        primeiroLayout = true;
    }

    //    @Override
//    public boolean dispatchTouchEvent(MotionEvent ev) {
//
//        final int action = MotionEventCompat.getActionMasked(ev);
//
//        if (action == MotionEvent.ACTION_DOWN) {
//
//        } else if (action == MotionEvent.ACTION_MOVE) {
//
//        }
//
//        return super.dispatchTouchEvent(ev);
//    }
//
//
//    @Override
//    public boolean onInterceptTouchEvent(MotionEvent ev) {
//
//        final int action = MotionEventCompat.getActionMasked(ev);
//        final float x = ev.getX();
//        final float y = ev.getY();
//
//        switch (action) {
//            case MotionEvent.ACTION_DOWN: {
//                mInitialMotionX = x;
//                mInitialMotionY = y;
//                break;
//            }
//
//            case MotionEvent.ACTION_MOVE: {
//                final float adx = Math.abs(x - mInitialMotionX);
//                final float ady = Math.abs(y - mInitialMotionY);
//
//                break;
//            }
//
//            case MotionEvent.ACTION_CANCEL:
//            case MotionEvent.ACTION_UP:
//                break;
//        }
//        return super.onInterceptTouchEvent(ev);
//    }
//
//    @Override
//    public boolean onTouchEvent(MotionEvent event) {
//        return super.onTouchEvent(event);
//    }


    private void changePanelState() {

        switch (panelState) {
            case PanelState.ANCHORED: {
                setPanelState(PanelState.COLLAPSED);
                break;
            }
            case PanelState.COLLAPSED: {
                setPanelState(inversePanelState);
                break;
            }
            case PanelState.EXPANDED: {
                setPanelState(PanelState.COLLAPSED);
                break;
            }
            case PanelState.HIDDEN: {
                setPanelState(inversePanelState);
                break;
            }
        }
    }

    public void setPanelState(int panelState) {

        int height = getMeasuredHeight();
        int width = getMeasuredWidth();

        switch (panelState) {
            case PanelState.ANCHORED: {
                pnlResultado.setVisibility(View.VISIBLE);
                PropertyValuesHolder pvhY = PropertyValuesHolder.ofFloat("y", (height-actionBarSize) / 2);
                ObjectAnimator.ofPropertyValuesHolder(pnlResultado, pvhY).setDuration(200).start();
                if(panelStateListener != null){
                    panelStateListener.onPanelAnchored(pnlResultado);
                }
                break;
            }
            case PanelState.COLLAPSED: {
                pnlResultado.setVisibility(View.VISIBLE);
                PropertyValuesHolder pvhY = PropertyValuesHolder.ofFloat("y", (height - txTituloResultado.getHeight() - actionBarSize));
                ObjectAnimator.ofPropertyValuesHolder(pnlResultado, pvhY).setDuration(200).start();

                if(panelStateListener != null){
                    panelStateListener.onPanelCollapsed(pnlResultado);
                }
                break;
            }
            case PanelState.EXPANDED: {
                pnlResultado.setVisibility(View.VISIBLE);
                PropertyValuesHolder pvhY = PropertyValuesHolder.ofFloat("y", pnlSearchBotton);
                ObjectAnimator.ofPropertyValuesHolder(pnlResultado, pvhY).setDuration(200).start();

                if(panelStateListener != null){
                    panelStateListener.onPanelExpanded(pnlResultado);
                }
                break;
            }
            case PanelState.HIDDEN: {
                pnlResultado.setVisibility(View.GONE);
                if(panelStateListener != null){
                    panelStateListener.onPanelHidden(pnlResultado);
                }
                break;
            }
        }
        inversePanelState = this.panelState;
        this.panelState = panelState;
    }

    public int getPanelState() {
        return this.panelState;
    }


    public void setPanelStateListener(PanelStateListener panelStateListener) {
        this.panelStateListener = panelStateListener;
    }

    public interface PanelStateListener {

        public void onPanelCollapsed(View panel);

        public void onPanelExpanded(View panel);

        public void onPanelAnchored(View panel);

        public void onPanelHidden(View panel);
    }
}
