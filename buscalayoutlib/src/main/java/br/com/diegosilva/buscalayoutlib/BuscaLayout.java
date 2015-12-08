package br.com.diegosilva.buscalayoutlib;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.content.Context;
import android.content.res.TypedArray;
import android.support.v4.view.MotionEventCompat;
import android.util.AttributeSet;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Diego on 28/11/2015.
 */
public class BuscaLayout extends ViewGroup {

    private int pnlTituloResultadoId = -1;
    private View pnlTituloResultado;

    private int pnlResultadoId = -1;
    private View pnlResultado;

    private int pnlCampoBuscaId = -1;
    private View pnlCampoBusca;

    private int pnlContentResultadoId = -1;
    private View pnlContentResultado;

    private int pnlSearchBotton;

    private int inversePanelState = PanelState.EXPANDED;
    private int panelState = PanelState.COLLAPSED;

    private int actionBarSize;

    private int expandedSize;
    private int anchoredSize;

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

        if(attrs != null) {
            TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.BuscaLayout);
            if (ta != null) {
                pnlTituloResultadoId = ta.getResourceId(R.styleable.BuscaLayout_pnlTxResultadoId, -1);
                pnlResultadoId = ta.getResourceId(R.styleable.BuscaLayout_pnlResultadoId, -1);
                pnlCampoBuscaId = ta.getResourceId(R.styleable.BuscaLayout_pnlCampoBuscaId, -1);
                pnlContentResultadoId = ta.getResourceId(R.styleable.BuscaLayout_pnlContentResultadoId, -1);
            }
            ta.recycle();
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
        v3.layout(l, pnlSearchBotton, r, b);

        if (primeiroLayout) {
            v3.setY((height - pnlTituloResultado.getHeight()) - actionBarSize);
        }
        primeiroLayout = false;

        // pnlContentResultado.getLayoutParams().height = (b- pnlSearchBotton - txTituloResultado.getHeight());
        expandedSize = (b - pnlSearchBotton - pnlTituloResultado.getHeight());
        anchoredSize = (height / 2) - pnlTituloResultado.getHeight();
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


    private float mInitialMotionX;
    private float mInitialMotionY;

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        if (pnlTituloResultadoId != -1) {
            pnlTituloResultado = findViewById(pnlTituloResultadoId);
            pnlTituloResultado.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    changePanelState();
                }
            });

            pnlTituloResultado.setOnTouchListener(new OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {

                    final int action = MotionEventCompat.getActionMasked(event);
                    final float x = event.getX();
                    final float y = event.getY();

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

                    return true;
                }
            });

        }

        if (pnlResultadoId != -1) {
            pnlResultado = findViewById(pnlResultadoId);
        }

        if (pnlCampoBuscaId != -1) {
            pnlCampoBusca = findViewById(pnlCampoBuscaId);
        }

        if (pnlContentResultadoId != -1) {
            pnlContentResultado = findViewById(pnlContentResultadoId);
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
                pnlContentResultado.getLayoutParams().height = anchoredSize;
                pnlResultado.setVisibility(View.VISIBLE);
                PropertyValuesHolder pvhY = PropertyValuesHolder.ofFloat("y", ((height - actionBarSize) / 2) + pnlTituloResultado.getHeight());
                ObjectAnimator.ofPropertyValuesHolder(pnlResultado, pvhY).setDuration(200).start();
                if (panelStateListener != null) {
                    panelStateListener.onPanelAnchored(pnlResultado);
                }
                break;
            }
            case PanelState.COLLAPSED: {
                pnlContentResultado.getLayoutParams().height = expandedSize;
                pnlResultado.setVisibility(View.VISIBLE);
                PropertyValuesHolder pvhY = PropertyValuesHolder.ofFloat("y", (height - pnlTituloResultado.getHeight() - actionBarSize));
                ObjectAnimator.ofPropertyValuesHolder(pnlResultado, pvhY).setDuration(200).start();

                if (panelStateListener != null) {
                    panelStateListener.onPanelCollapsed(pnlResultado);
                }
                break;
            }
            case PanelState.EXPANDED: {
                pnlContentResultado.getLayoutParams().height = expandedSize;
                pnlResultado.setVisibility(View.VISIBLE);
                PropertyValuesHolder pvhY = PropertyValuesHolder.ofFloat("y", pnlSearchBotton);
                ObjectAnimator.ofPropertyValuesHolder(pnlResultado, pvhY).setDuration(200).start();

                if (panelStateListener != null) {
                    panelStateListener.onPanelExpanded(pnlResultado);
                }
                break;
            }
            case PanelState.HIDDEN: {
                pnlContentResultado.getLayoutParams().height = expandedSize;
                pnlResultado.setVisibility(View.GONE);
                if (panelStateListener != null) {
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


    public void setInversePanelState(int inversePanelState) {
        this.inversePanelState = inversePanelState;
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
