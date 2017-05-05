package com.xiaoma.taobao;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xiaoma.taobao.Utils.UtilTool;
import com.xiaoma.taobao.Utils.UtilsLog;
import com.xiaoma.taobao.adapter.ShopcatAdapter;
import com.xiaoma.taobao.entity.GoodsInfo;
import com.xiaoma.taobao.entity.StoreInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import in.srain.cube.views.ptr.PtrClassicDefaultHeader;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;
import in.srain.cube.views.ptr.header.StoreHouseHeader;

import static in.srain.cube.views.ptr.util.PtrLocalDisplay.dp2px;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, ShopcatAdapter.CheckInterface, ShopcatAdapter.ModifyCountInterface, ShopcatAdapter.GroupEditorListener {
    @BindView(R.id.listView)
    ExpandableListView listView;
    @BindView(R.id.all_checkBox)
    CheckBox allCheckBox;
    @BindView(R.id.total_price)
    TextView totalPrice;
    @BindView(R.id.go_pay)
    TextView goPay;
    @BindView(R.id.order_info)
    LinearLayout orderInfo;
    @BindView(R.id.share_goods)
    TextView shareGoods;
    @BindView(R.id.collect_goods)
    TextView collectGoods;
    @BindView(R.id.del_goods)
    TextView delGoods;
    @BindView(R.id.share_info)
    LinearLayout shareInfo;
    @BindView(R.id.ll_cart)
    LinearLayout llCart;
    @BindView(R.id.mPtrframe)
    PtrFrameLayout mPtrFrame;

    TextView shoppingcatNum;
    Button actionBarEdit;
    LinearLayout empty_shopcart;
    private Context mcontext;
    private double mtotalPrice = 0.00;
    private int mtotalCount = 0;
    //false就是编辑，ture就是完成
    private boolean flag = false;
    private ShopcatAdapter adapter;
    private List<StoreInfo> groups; //组元素的列表
    private Map<String, List<GoodsInfo>> childs; //子元素的列表

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        ButterKnife.bind(this);
        initPtrFrame();
        initActionBar();
        initData();
        initEvents();
    }

    private void initPtrFrame() {
//        final StoreHouseHeader header=new StoreHouseHeader(this);
//        header.setPadding(dp2px(20), dp2px(20), 0, 0);
//        header.initWithString("xiaoma is good");
        final PtrClassicDefaultHeader header=new PtrClassicDefaultHeader(this);
        header.setPadding(dp2px(20), dp2px(20), 0, 0);
        mPtrFrame.setHeaderView(header);
        mPtrFrame.addPtrUIHandler(header);
        mPtrFrame.setPtrHandler(new PtrDefaultHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                mPtrFrame.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mPtrFrame.refreshComplete();
                    }
                },2000);
            }

            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                return PtrDefaultHandler.checkContentCanBePulledDown(frame, content, header);
            }
        });
    }




    private void initEvents() {
        actionBarEdit.setOnClickListener(this);
        adapter = new ShopcatAdapter(groups, childs, mcontext);
        adapter.setCheckInterface(this);//关键步骤1：设置复选框的接口
        adapter.setModifyCountInterface(this); //关键步骤2:设置增删减的接口
        adapter.setGroupEditorListener(this);//关键步骤3:监听组列表的编辑状态
        listView.setGroupIndicator(null); //设置属性 GroupIndicator 去掉向下箭头
        listView.setAdapter(adapter);
        for (int i = 0; i < adapter.getGroupCount(); i++) {
            listView.expandGroup(i); //关键步骤4:初始化，将ExpandableListView以展开的方式显示
        }
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                int firstVisiablePostion=view.getFirstVisiblePosition();
                int top=-1;
                View firstView=view.getChildAt(firstVisibleItem);
                UtilsLog.i("childCount="+view.getChildCount());//返回的是显示层面上的所包含的子view的个数
                if(firstView!=null){
                     top=firstView.getTop();
                }
                UtilsLog.i("firstVisiableItem="+firstVisibleItem+",fistVisiablePosition="+firstVisiablePostion+",firstView="+firstView+",top="+top);
                if(firstVisibleItem==0&&top==0){
                    mPtrFrame.setEnabled(true);
                }else{
                    mPtrFrame.setEnabled(false);
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        setCartNum();
    }

    /**
     * 设置购物车的数量
     */
    private void setCartNum() {
        int count = 0;
        for (int i = 0; i < groups.size(); i++) {
            StoreInfo group = groups.get(i);
            group.setChoosed(allCheckBox.isChecked());
            List<GoodsInfo> Childs = childs.get(group.getId());
            for (GoodsInfo childs : Childs) {
                count++;
            }
        }

        //购物车已经清空
        if (count == 0) {
            clearCart();
        } else {
            shoppingcatNum.setText("购物车(" + count + ")");
        }

    }

    private void clearCart() {
        shoppingcatNum.setText("购物车(0)");
        actionBarEdit.setVisibility(View.GONE);
        llCart.setVisibility(View.GONE);
        empty_shopcart.setVisibility(View.VISIBLE);//这里发生过错误
    }

    /**
     * 模拟数据<br>
     * 遵循适配器的数据列表填充原则，组元素被放在一个list中，对应着组元素的下辖子元素被放在Map中
     * 其Key是组元素的Id
     */
    private void initData() {
        mcontext = this;
        groups = new ArrayList<StoreInfo>();
        childs = new HashMap<String, List<GoodsInfo>>();
        for (int i = 0; i < 5; i++) {
            groups.add(new StoreInfo(i + "", "小马的第" + (i + 1) + "号当铺"));
            List<GoodsInfo> goods = new ArrayList<>();
            for (int j = 0; j <= i; j++) {
                int[] img = {R.drawable.cmaz, R.drawable.cmaz, R.drawable.cmaz, R.drawable.cmaz, R.drawable.cmaz, R.drawable.cmaz};
                //i-j 就是商品的id， 对应着第几个店铺的第几个商品，1-1 就是第一个店铺的第一个商品
                goods.add(new GoodsInfo(i + "-" + j, "商品", groups.get(i).getName() + "的第" + (j + 1) + "个商品", 255.00 + new Random().nextInt(1500), 1555 + new Random().nextInt(3000), "第一排", "出头天者", img[j], new Random().nextInt(100)));
            }
            childs.put(groups.get(i).getId(), goods);
        }
    }

    /**
     * 删除操作
     * 1.不要边遍历边删除,容易出现数组越界的情况
     * 2.把将要删除的对象放进相应的容器中，待遍历完，用removeAll的方式进行删除
     */
    private void doDelete() {
        List<StoreInfo> toBeDeleteGroups = new ArrayList<StoreInfo>(); //待删除的组元素
        for (int i = 0; i < groups.size(); i++) {
            StoreInfo group = groups.get(i);
            if (group.isChoosed()) {
                toBeDeleteGroups.add(group);
            }
            List<GoodsInfo> toBeDeleteChilds = new ArrayList<GoodsInfo>();//待删除的子元素
            List<GoodsInfo> child = childs.get(group.getId());
            for (int j = 0; j < child.size(); j++) {
                if (child.get(j).isChoosed()) {
                    toBeDeleteChilds.add(child.get(j));
                }
            }
            child.removeAll(toBeDeleteChilds);
        }
        groups.removeAll(toBeDeleteGroups);
        //重新设置购物车
        setCartNum();
        adapter.notifyDataSetChanged();

    }


    private void findView(View view) {
        shoppingcatNum = (TextView) view.findViewById(R.id.shoppingcat_num);
        actionBarEdit = (Button) view.findViewById(R.id.actionBar_edit);
        //不知道为什么，ButterKnife不知道BindView
        empty_shopcart = (LinearLayout) findViewById(R.id.layout_empty_shopcart);
    }


    private void initActionBar() {
        //隐藏标题栏
        if (getSupportActionBar() != null) {
            //去掉阴影
            getSupportActionBar().setElevation(0);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getSupportActionBar().setDisplayShowCustomEnabled(true);
            View view = getLayoutInflater().inflate(R.layout.acitonbar, null);
            findView(view);
            getSupportActionBar().setCustomView(view, new ActionBar.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            ActionBar.LayoutParams lp = (ActionBar.LayoutParams) view.getLayoutParams();
            lp.gravity = Gravity.HORIZONTAL_GRAVITY_MASK | Gravity.CENTER_HORIZONTAL;
            getSupportActionBar().setCustomView(view, lp);
        }


    }


    /**
     * @param groupPosition 组元素的位置
     * @param isChecked     组元素的选中与否
     *                      思路:组元素被选中了，那么下辖全部的子元素也被选中
     */
    @Override
    public void checkGroup(int groupPosition, boolean isChecked) {
        StoreInfo group = groups.get(groupPosition);
        List<GoodsInfo> child = childs.get(group.getId());
        for (int i = 0; i < child.size(); i++) {
            child.get(i).setChoosed(isChecked);
        }
        if (isCheckAll()) {
            allCheckBox.setChecked(true);//全选
        } else {
            allCheckBox.setChecked(false);//反选
        }
        adapter.notifyDataSetChanged();
        calulate();
    }

    /**
     * @return 判断组元素是否全选
     */
    private boolean isCheckAll() {
        for (StoreInfo group : groups) {
            if (!group.isChoosed()) {
                return false;
            }
        }
        return true;
    }

    /**
     * @param groupPosition 组元素的位置
     * @param childPosition 子元素的位置
     * @param isChecked     子元素的选中与否
     */
    @Override
    public void checkChild(int groupPosition, int childPosition, boolean isChecked) {
        boolean allChildSameState = true; //判断该组下面的所有子元素是否处于同一状态
        StoreInfo group = groups.get(groupPosition);
        List<GoodsInfo> child = childs.get(group.getId());
        for (int i = 0; i < child.size(); i++) {
            //不选全中
            if (child.get(i).isChoosed() != isChecked) {
                allChildSameState = false;
                break;
            }
        }

        if (allChildSameState) {
            group.setChoosed(isChecked);//如果子元素状态相同，那么对应的组元素也设置成这一种的同一状态
        } else {
            group.setChoosed(false);//否则一律视为未选中
        }

        if (isCheckAll()) {
            allCheckBox.setChecked(true);//全选
        } else {
            allCheckBox.setChecked(false);//反选
        }

        adapter.notifyDataSetChanged();
        calulate();

    }

    @Override
    public void doIncrease(int groupPosition, int childPosition, View showCountView, boolean isChecked) {
        GoodsInfo good = (GoodsInfo) adapter.getChild(groupPosition, childPosition);
        int count = good.getCount();
        count++;
        good.setCount(count);
        ((TextView) showCountView).setText(String.valueOf(count));
        adapter.notifyDataSetChanged();
        calulate();
    }

    /**
     * @param groupPosition
     * @param childPosition
     * @param showCountView
     * @param isChecked
     */
    @Override
    public void doDecrease(int groupPosition, int childPosition, View showCountView, boolean isChecked) {
        GoodsInfo good = (GoodsInfo) adapter.getChild(groupPosition, childPosition);
        int count = good.getCount();
        if (count == 1) {
            return;
        }
        count--;
        good.setCount(count);
        ((TextView) showCountView).setText("" + count);
        adapter.notifyDataSetChanged();
        calulate();
    }

    /**
     * @param groupPosition
     * @param childPosition 思路:当子元素=0，那么组元素也要删除
     */
    @Override
    public void childDelete(int groupPosition, int childPosition) {
        StoreInfo group = groups.get(groupPosition);
        List<GoodsInfo> child = childs.get(group.getId());
        child.remove(childPosition);
        if (child.size() == 0) {
            groups.remove(groupPosition);
        }
        adapter.notifyDataSetChanged();
        calulate();


    }

    public void doUpdate(int groupPosition, int childPosition, View showCountView, boolean isChecked) {
        GoodsInfo good = (GoodsInfo) adapter.getChild(groupPosition, childPosition);
        int count = good.getCount();
        UtilsLog.i("进行更新数据，数量" + count + "");
        ((TextView) showCountView).setText(String.valueOf(count));
        adapter.notifyDataSetChanged();
        calulate();


    }

    @Override
    public void groupEditor(int groupPosition) {

    }

    @OnClick({R.id.all_checkBox, R.id.go_pay, R.id.share_goods, R.id.collect_goods, R.id.del_goods})
    public void onClick(View view) {
        AlertDialog dialog;
        switch (view.getId()) {
            case R.id.all_checkBox:
                doCheckAll();
                break;
            case R.id.go_pay:
                if (mtotalCount == 0) {
                    UtilTool.toast(mcontext, "请选择要支付的商品");
                    return;
                }
                dialog = new AlertDialog.Builder(mcontext).create();
                dialog.setMessage("总计:" + mtotalCount + "种商品，" + mtotalPrice + "元");
                dialog.setButton(DialogInterface.BUTTON_POSITIVE, "支付", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        return;
                    }
                });
                dialog.setButton(DialogInterface.BUTTON_NEGATIVE, "取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        return;
                    }
                });
                dialog.show();
                break;
            case R.id.share_goods:
                if (mtotalCount == 0) {
                    UtilTool.toast(mcontext, "请选择要分享的商品");
                    return;
                }
                UtilTool.toast(mcontext, "分享成功");
                break;
            case R.id.collect_goods:
                if (mtotalCount == 0) {
                    UtilTool.toast(mcontext, "请选择要收藏的商品");
                    return;
                }
                UtilTool.toast(mcontext, "收藏成功");
                break;
            case R.id.del_goods:
                if (mtotalCount == 0) {
                    UtilTool.toast(mcontext, "请选择要删除的商品");
                    return;
                }
                dialog = new AlertDialog.Builder(mcontext).create();
                dialog.setMessage("确认要删除该商品吗?");
                dialog.setButton(DialogInterface.BUTTON_POSITIVE, "确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        doDelete();
                    }
                });
                dialog.setButton(DialogInterface.BUTTON_NEGATIVE, "取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        return;
                    }
                });
                dialog.show();
                break;
            case R.id.actionBar_edit:
                flag = !flag;
                setActionBarEditor();
                setVisiable();
                break;
        }
    }

    /**
     * ActionBar标题上点编辑的时候，只显示每一个店铺的商品修改界面
     * ActionBar标题上点完成的时候，只显示每一个店铺的商品信息界面
     */
    private void setActionBarEditor() {
        for (int i = 0; i < groups.size(); i++) {
            StoreInfo group = groups.get(i);
            if (group.isActionBarEditor()) {
                group.setActionBarEditor(false);
            } else {
                group.setActionBarEditor(true);
            }
        }
        adapter.notifyDataSetChanged();
    }


    /**
     * 全选和反选
     * 错误标记：在这里出现过错误
     */
    private void doCheckAll() {
        for (int i = 0; i < groups.size(); i++) {
            StoreInfo group = groups.get(i);
            group.setChoosed(allCheckBox.isChecked());
            List<GoodsInfo> child = childs.get(group.getId());
            for (int j = 0; j < child.size(); j++) {
                child.get(j).setChoosed(allCheckBox.isChecked());//这里出现过错误
            }
        }
        adapter.notifyDataSetChanged();
        calulate();
    }

    /**
     * 计算商品总价格，操作步骤
     * 1.先清空全局计价,计数
     * 2.遍历所有的子元素，只要是被选中的，就进行相关的计算操作
     * 3.给textView填充数据
     */
    private void calulate() {
        mtotalPrice = 0.00;
        mtotalCount = 0;
        for (int i = 0; i < groups.size(); i++) {
            StoreInfo group = groups.get(i);
            List<GoodsInfo> child = childs.get(group.getId());
            for (int j = 0; j < child.size(); j++) {
                GoodsInfo good = child.get(j);
                if (good.isChoosed()) {
                    mtotalCount++;
                    mtotalPrice += good.getPrice() * good.getCount();
                }
            }
        }
        totalPrice.setText("￥" + mtotalPrice + "");
        goPay.setText("去支付(" + mtotalCount + ")");
        if (mtotalCount == 0) {
            setCartNum();
        } else {
            shoppingcatNum.setText("购物车(" + mtotalCount + ")");
        }


    }

    private void setVisiable() {
        if (flag) {
            orderInfo.setVisibility(View.GONE);
            shareInfo.setVisibility(View.VISIBLE);
            actionBarEdit.setText("完成");
        } else {
            orderInfo.setVisibility(View.VISIBLE);
            shareInfo.setVisibility(View.GONE);
            actionBarEdit.setText("编辑");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        adapter = null;
        childs.clear();
        groups.clear();
        mtotalPrice = 0.00;
        mtotalCount = 0;
    }

}
