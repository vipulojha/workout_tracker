package com.example.workouttrackerr;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

public class FitnessRingsView extends View {
    private final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final RectF arcBounds = new RectF();
    private float animationProgress = 0f;

    private final float[] targets = {0.86f, 0.72f, 0.58f};
    private final int[] colors = {
            Color.rgb(24, 194, 156),
            Color.rgb(91, 66, 243),
            Color.rgb(255, 184, 77)
    };

    public FitnessRingsView(Context context) {
        super(context);
        init();
    }

    public FitnessRingsView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeCap(Paint.Cap.ROUND);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        ValueAnimator animator = ValueAnimator.ofFloat(0f, 1f);
        animator.setDuration(1200);
        animator.setInterpolator(new DecelerateInterpolator());
        animator.addUpdateListener(valueAnimator -> {
            animationProgress = (float) valueAnimator.getAnimatedValue();
            invalidate();
        });
        animator.start();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        float size = Math.min(getWidth(), getHeight());
        float centerX = getWidth() / 2f;
        float centerY = getHeight() / 2f;
        float stroke = size * 0.065f;
        float radius = size * 0.42f;

        paint.setStrokeWidth(stroke);
        for (int i = 0; i < targets.length; i++) {
            float ringRadius = radius - (i * stroke * 1.7f);
            arcBounds.set(centerX - ringRadius, centerY - ringRadius, centerX + ringRadius, centerY + ringRadius);

            paint.setColor(Color.rgb(35, 41, 54));
            canvas.drawArc(arcBounds, -90f, 360f, false, paint);

            paint.setColor(colors[i]);
            canvas.drawArc(arcBounds, -90f, 360f * targets[i] * animationProgress, false, paint);
        }
    }
}
