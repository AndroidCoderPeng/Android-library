package com.pengxh.app.multilib.widget.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.pengxh.app.multilib.R;
import com.pengxh.app.multilib.utils.SizeUtil;

/**
 * @author: Pengxh
 * @email: 290677893@qq.com
 * @date: 2021-09-05 17:15:11
 * @description: 底部列表Sheet
 */
public class BottomActionSheet extends Dialog {


    private Context ctx;
    private String[] sheetItems;
    private OnActionSheetListener sheetListener;

    public static class Builder {
        private Context context;
        private String[] actionItems;
        private OnActionSheetListener listener;

        public Builder setContext(Context context) {
            this.context = context;
            return this;
        }

        public Builder setActionItemTitles(String[] items) {
            this.actionItems = items;
            return this;
        }

        public Builder setOnActionSheetListener(OnActionSheetListener listener) {
            this.listener = listener;
            return this;
        }

        public BottomActionSheet build() {
            return new BottomActionSheet(this);
        }
    }

    private BottomActionSheet(Builder builder) {
        super(builder.context, R.style.DialogStyle);
        this.ctx = builder.context;
        this.sheetItems = builder.actionItems;
        this.sheetListener = builder.listener;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        configDialogLayout();
        setContentView(R.layout.bottom_action_sheet);
        setCancelable(true);
        setCanceledOnTouchOutside(true);

        ListView itemListView = findViewById(R.id.itemListView);
        itemListView.setAdapter(new ItemListAdapter(ctx));
        itemListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                dismiss();
                sheetListener.onActionItemClick(position);
            }
        });

        TextView sheetCancelView = findViewById(R.id.sheetCancelView);
        sheetCancelView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
    }

    private void configDialogLayout() {
        Window window = getWindow();
        if (window == null) {
            return;
        }
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        window.getDecorView().setBackgroundColor(Color.TRANSPARENT);
        window.setGravity(Gravity.BOTTOM);
        //设置Dialog出现的动画
        window.setWindowAnimations(R.style.ActionSheetDialogAnimation);
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = SizeUtil.getScreenWidth(ctx);
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(params);
    }

    public interface OnActionSheetListener {
        void onActionItemClick(int position);
    }

    class ItemListAdapter extends BaseAdapter {

        private LayoutInflater inflater;

        ItemListAdapter(Context context) {
            this.inflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return sheetItems.length;
        }

        @Override
        public Object getItem(int position) {
            return sheetItems[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup viewGroup) {
            ItemViewHolder holder;
            if (convertView == null) {
                holder = new ItemViewHolder();
                convertView = inflater.inflate(R.layout.item_action_sheet, null);
                holder.sheetItemView = convertView.findViewById(R.id.sheetItemView);
                convertView.setTag(holder);
            } else {
                holder = (ItemViewHolder) convertView.getTag();
            }
            if (position == 0) {
                holder.sheetItemView.setBackgroundResource(R.drawable.sheet_item_top_selector);
            } else if (position == sheetItems.length - 1) {
                holder.sheetItemView.setBackgroundResource(R.drawable.sheet_item_bottom_selector);
            } else {
                holder.sheetItemView.setBackgroundResource(R.drawable.sheet_item_middle_selector);
            }
            holder.sheetItemView.setText(sheetItems[position]);
            holder.sheetItemView.setTextSize(18);
            //需要动态设置item的高度
            AbsListView.LayoutParams param = new AbsListView.LayoutParams(
                    WindowManager.LayoutParams.MATCH_PARENT,
                    SizeUtil.dp2px(ctx, 46)
            );
            convertView.setLayoutParams(param);
            return convertView;
        }
    }

    static class ItemViewHolder {
        TextView sheetItemView;
    }
}
