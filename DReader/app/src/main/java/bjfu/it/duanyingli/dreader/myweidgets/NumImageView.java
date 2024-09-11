package bjfu.it.duanyingli.dreader.myweidgets;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.ActionProvider;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import bjfu.it.duanyingli.dreader.R;

import static java.security.AccessController.getContext;


public class NumImageView extends androidx.appcompat.widget.AppCompatImageView {

    //要显示的数量数量
    private int num = 0;
    //红色圆圈的半径
    private float radius;
    //圆圈内数字的半径
    private float textSize;
    //右边和上边内边距
    private int paddingRight;
    private int paddingTop;

    public NumImageView(Context context) {
        super(context);
    }

    public NumImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public NumImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public int getNum(){
        return this.num;
    }

    //设置显示的数量
    public void setNum(int num) {
        this.num = num;
        //重新绘制画布
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (num > 0) {
            //初始化半径
            radius = getWidth() / 4;
            //初始化字体大小
            textSize = num < 10 ? radius + 5 : radius;
            //初始化边距
            paddingRight = getPaddingRight();
            paddingTop = getPaddingTop();
            //初始化画笔
            Paint paint = new Paint();
            //设置抗锯齿
            paint.setAntiAlias(true);
            //设置颜色为红色
            paint.setColor(getResources().getColor(R.color.carNum));
            //设置填充样式为充满
            paint.setStyle(Paint.Style.FILL);
            //画圆
            canvas.drawCircle(getWidth() - radius - paddingRight / 2, radius + paddingTop / 2, radius, paint);
            //设置颜色为白色
            paint.setColor(0xffffffff);
            //设置字体大小
            paint.setTextSize(textSize);
            //画数字
            canvas.drawText("" + (num < 99 ? num : 99),
                    num < 10 ? getWidth() - radius - textSize / 4 - paddingRight / 2
                            : getWidth() - radius - textSize / 2 - paddingRight / 2,
                    radius + textSize / 3 + paddingTop / 2, paint);
        }
    }

}

//public class NumImageView extends ActionProvider {
//
//    private TextView tagTextView;
//
//    //点击监听的接口，用于调用者的回调
//    private OnMenuClickListener onMenuClickListener;
//    public interface OnMenuClickListener{
//        void onClick();
//    }
//
//    public NumImageView(Context context) {
//        super(context);
//    }
//
//
//
//    //初始化布局
//    @Override
//    public View onCreateActionView() {
//        //读取support下Toolbar/ActionBar的高度
//        int size = getContext().getResources().getDimensionPixelSize(android.support.design.R.dimen.abc_action_bar_default_height_material);
//
//        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(size, size);
//
//        View view = LayoutInflater.from(getContext()).inflate(R.layout.cell_toolbar_download, null);
//        view.setLayoutParams(layoutParams);
//        tagTextView = (TextView) view.findViewById(R.id.tv_menu_download_count);
//        view.setOnClickListener(onViewClickListener);
//
//        return view;
//    }
//
//    private View.OnClickListener onViewClickListener = new View.OnClickListener() {
//        @Override
//        public void onClick(View v) {
//            if (onMenuClickListener != null)
//                onMenuClickListener.onClick();
//        }
//    };
//
//    public void setOnMenuClickListener(OnMenuClickListener onMenuClickListener) {
//        this.onMenuClickListener = onMenuClickListener;
//    }
//
//    //对外提供一些需要的接口
//    public void showTagTextView(String tag) {
//        tagTextView.setText(tag);
//        tagTextView.setVisibility(View.VISIBLE);
//    }
//
//    public void hideTagTextView() {
//        tagTextView.setVisibility(View.GONE);
//    }
//}