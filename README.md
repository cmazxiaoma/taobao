## 前言
### juejin blog url: https://www.jianshu.com/u/eff099687e03
### jianshu blog url: https://juejin.im/user/592fa64d2f301e00581f7858/posts
其实做一个电商购物车，还真不是一个轻松的活。但是只要掌握思路，一步一步来做，就会发现也就这样。废物不多说，直接上效果图
## 效果图

![GIF1.gif](http://upload-images.jianshu.io/upload_images/4636177-6e6a9f14093038f9.gif?imageMogr2/auto-orient/strip)

![GIF6666.gif](http://upload-images.jianshu.io/upload_images/4636177-0c2050403ce954ab.gif?imageMogr2/auto-orient/strip)
***
## 主要思路
整一个布局就是**ExpandableListView**，然后自定义一个**ActionBar**，**ActionBar**上面显示购物车数量，通过**ActionBar**上面的编辑状态，店铺布局，所有商品布局，底部布局要进行相应的变化，编辑状态下需要改变商品的数量，删除商品，全选商品，隐藏店铺的编辑。非编辑状态可以显示店铺的编辑，显示结算，商品的信息。通过每一个店铺上面的编辑状态，该店铺旗下的所有商品布局都要进行相应的变化。编辑状态下，需要改变商品的数量和删除商品。非编辑状态下只需要显示商品的信息。当该店铺下所有商品都被勾选，对应的店铺也要被勾选。反之，该店铺下只要有一个商品没有被勾选，那么店铺就不用勾选。**其实逻辑挺简单的，复杂的逻辑其实就是很多简单逻辑组成的，我们只需要把复杂的逻辑简单化成很多简单的逻辑，我们就能完成一个大概的思路**
****

## 代码教学
****
我们第一步要做就是自定义一个ActionBar，几行代码就能解决。
<pre>
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
        </pre>
完成之后效果图:

![Paste_Image.png](http://upload-images.jianshu.io/upload_images/4636177-502c4341eebd3432.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
***
第二步就是写整个布局xml文件了，基础知识也没有什么好说的。

    <?xml version="1.0" encoding="utf-8"?>
    <LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:gravity="center_vertical"
    android:orientation="vertical"
    android:layout_width="match_parent"
    android:layout_height="match_parent">
    <in.srain.cube.views.ptr.PtrFrameLayout
        android:background="#f1f1f1"
        xmlns:android="http://schemas.android.com/apk/res/android"
        android:id="@+id/mPtrframe"
        xmlns:cube_ptr="http://schemas.android.com/apk/res-auto"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        cube_ptr:ptr_resistance="1.7"
        cube_ptr:ptr_ratio_of_header_height_to_refresh="1.2"
        cube_ptr:ptr_duration_to_close="300"
        cube_ptr:ptr_duration_to_close_header="2000"
        cube_ptr:ptr_keep_header_when_refresh="true"
        cube_ptr:ptr_pull_to_fresh="false" >
    <FrameLayout
        android:background="@color/white"
        android:layout_width="match_parent"
        android:layout_height="match_parent">
    <LinearLayout
        android:id="@+id/ll_cart"
        android:orientation="vertical"
        android:layout_width="match_parent"
        android:layout_height="match_parent">
    <ExpandableListView
        android:id="@+id/listView"
        android:layout_width="match_parent"
        android:layout_weight="1"
        android:layout_height="0dp"/>
    <!--分割线-->
    <View
        android:layout_width="match_parent"
        android:layout_height="0.5dp"
        android:background="@color/gray" />
    <LinearLayout
        android:gravity="center"
        android:orientation="horizontal"
        android:layout_width="match_parent"
        android:layout_height="50dp">
        <CheckBox
            android:textSize="16sp"
            android:background="@null"
            android:clickable="true"
            android:drawablePadding="10dp"
            android:drawableLeft="@drawable/checkbox_bg"
            android:text="@string/all"
            android:id="@+id/all_checkBox"
            android:button="@null"
            android:minHeight="64dp"
            android:layout_marginStart="10dp"
            android:layout_gravity="center_vertical"
            android:layout_weight="0.3"
            android:layout_width="0dp"
            android:layout_height="wrap_content" />
        <LinearLayout
            android:gravity="center"
            android:orientation="horizontal"
            android:id="@+id/order_info"
            android:layout_weight="0.7"
            android:layout_width="0dp"
            android:layout_height="wrap_content">
            <LinearLayout
                android:layout_marginEnd="20dp"
                android:orientation="vertical"
                android:layout_weight="0.5"
                android:layout_width="0dp"
                android:layout_height="wrap_content">
                <LinearLayout
                    android:gravity="end"
                    android:orientation="horizontal"
                    android:layout_width="match_parent"
                    android:layout_height="wrap_content">
                    <TextView
                        android:textColor="@android:color/black"
                        android:textSize="18sp"
                        android:text="@string/order_total"
                        android:layout_marginStart="5dp"
                        android:layout_width="wrap_content"
                        android:layout_height="wrap_content" />
                    <TextView
                        android:id="@+id/total_price"
                        android:text="￥0.00"
                        android:textSize="18sp"
                        android:textColor="@color/ic_taobao"
                        android:layout_width="wrap_content"
                        android:layout_height="wrap_content" />
                </LinearLayout>
                <TextView
                    android:gravity="right"
                    android:text="不含运费"
                    android:layout_width="match_parent"
                    android:layout_height="wrap_content" />
            </LinearLayout>
            <TextView
                android:id="@+id/go_pay"
                android:textSize="16sp"
                android:textColor="@android:color/white"
                android:background="@color/ic_taobao"
                android:gravity="center"
                android:text="结算(0)"
                android:layout_weight="0.5"
                android:layout_width="0dp"
                android:layout_height="60dp" />
            </LinearLayout>
        <LinearLayout
            android:visibility="gone"
            android:gravity="center"
            android:id="@+id/share_info"
            android:layout_weight="0.7"
            android:orientation="horizontal"
            android:layout_width="0dp"
            android:layout_height="match_parent">
            <TextView
                android:id="@+id/share_goods"
                android:textSize="16sp"
                android:textColor="@android:color/white"
                android:background="@color/ic_yellow"
                android:gravity="center"
                android:text="分享宝贝"
                android:layout_weight="0.3"
                android:layout_width="0dp"
                android:layout_height="match_parent" />
            <TextView
                android:layout_marginStart="1dp"
                android:id="@+id/collect_goods"
                android:textSize="16sp"
                android:textColor="@android:color/white"
                android:background="@color/ic_taobao"
                android:gravity="center"
                android:text="移到收藏夹"
                android:layout_weight="0.3"
                android:layout_width="0dp"
                android:layout_height="match_parent" />
            <TextView
                android:layout_marginStart="1dp"
                android:id="@+id/del_goods"
                android:textSize="16sp"
                android:textColor="@android:color/white"
                android:background="@color/ic_red"
                android:gravity="center"
                android:text="删除"
                android:layout_weight="0.3"
                android:layout_width="0dp"
                android:layout_height="match_parent" />
        </LinearLayout>
        </LinearLayout>
    </LinearLayout>
        <include
            android:id="@+id/layout_empty_shopcart"
            android:visibility="gone"
            android:layout_width="match_parent"
            android:layout_height="match_parent"
            layout="@layout/empty_shopcart" />
    </FrameLayout>
    </in.srain.cube.views.ptr.PtrFrameLayout>
    </LinearLayout>
完成之后效果图：
![Paste_Image.png](http://upload-images.jianshu.io/upload_images/4636177-33fde46e946b6d22.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

***

第三步 就是写店铺的xml，商品的xml,商品为空xml，此处略。

***
第四步 重点讲代码

 * ActionBar的编辑按钮处理
<pre>
 case R.id.actionBar_edit:
                flag = !flag;
                setActionBarEditor();
                setVisiable();
                break;
</pre>
<pre>
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
    }</pre>

* 接口回调
***
 checkBox的多选，勾选店铺，勾选商品的回调
<pre>
    public interface CheckInterface {
       
        void checkGroup(int groupPosition, boolean isChecked);
        void checkChild(int groupPosition, int childPosition, boolean isChecked);
    }
</pre>

 通过监听checkBox的勾选状态，便于计算商品金额和删除商品，计算购物车数量等操作。
***
店铺对应的商品的数量增加，减少，删除，更新的回调
<pre>
public interface ModifyCountInterface {
        void doIncrease(int groupPosition, int childPosition, View showCountView, boolean isChecked);
        void doDecrease(int groupPosition, int childPosition, View showCountView, boolean isChecked);
        void doUpdate(int groupPosition,int childPosition,View showCountView, boolean isChecked);
        void childDelete(int groupPosition, int childPosition);
    }
</pre>

通过该店铺下的商品数量变化，来计算计算金额和购物车数量，当该店铺的商品删除完时，便把该店铺从购物车中删除掉。


* 相关购物车的操作

***
   增加商品数量
   <pre>
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
</pre>

 减少商品数量
<pre>
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
</pre>

删除商品
<pre>@Override
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
</pre>

更新商品数量
<pre>
 public void doUpdate(int groupPosition, int childPosition, View showCountView, boolean isChecked) {
        GoodsInfo good = (GoodsInfo) adapter.getChild(groupPosition, childPosition);
        int count = good.getCount();
        UtilsLog.i("进行更新数据，数量" + count + "");
        ((TextView) showCountView).setText(String.valueOf(count));
        adapter.notifyDataSetChanged();
        calulate();
    }
</pre>

勾选店铺，商品
<pre>
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
</pre>

底部的遍历删除商品

<pre>
     删除操作
     1.不要边遍历边删除,容易出现数组越界的情况
     2.把将要删除的对象放进相应的容器中，待遍历完，用removeAll的方式进行删除
    private void doDelete() {
        List<StoreInfo> toBeDeleteGroups = new ArrayList<StoreInfo>(); 
        //待删除的组元素
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
</pre>

底部的全选商品和反选商品
<pre>
private boolean isCheckAll() {
        for (StoreInfo group : groups) {
            if (!group.isChoosed()) {
                return false;
            }
        }
        return true;
    }

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
</pre>
计算商品价格
<pre> private void calulate() {
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
</pre>

设置购物车数量和清空购物车
<pre>
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
</pre>

初始化店铺和商品信息
<pre>
     *模拟数据<br>
      遵循适配器的数据列表填充原则，组元素被放在一个list中，对应着组元素的下辖子元素被放在Map中
      其Key是组元素的Id
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
</pre>
***

* 购物车适配器
不多说，直接贴代码吧。代码还是有点多的啊
<pre>
public class ShopcatAdapter extends BaseExpandableListAdapter {
    private List<StoreInfo> groups;
    //这个String对应着StoreInfo的Id，也就是店铺的Id
    private Map<String, List<GoodsInfo>> childrens;
    private Context mcontext;
    private CheckInterface checkInterface;
    private ModifyCountInterface modifyCountInterface;
    private GroupEditorListener groupEditorListener;
    private int count = 0;
    private boolean flag=true; //组的编辑按钮是否可见，true可见，false不可见


    public ShopcatAdapter(List<StoreInfo> groups, Map<String, List<GoodsInfo>> childrens, Context mcontext) {
        this.groups = groups;
        this.childrens = childrens;
        this.mcontext = mcontext;
    }

    @Override
    public int getGroupCount() {
        return groups.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        String groupId = groups.get(groupPosition).getId();
        return childrens.get(groupId).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return groups.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        List<GoodsInfo> childs = childrens.get(groups.get(groupPosition).getId());
        return childs.get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(final int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        final GroupViewHolder groupViewHolder;
        if (convertView == null) {
            convertView = View.inflate(mcontext, R.layout.item_shopcat_group, null);
            groupViewHolder = new GroupViewHolder(convertView);
            convertView.setTag(groupViewHolder);
        } else {
            groupViewHolder = (GroupViewHolder) convertView.getTag();
        }
        final StoreInfo group = (StoreInfo) getGroup(groupPosition);
        groupViewHolder.storeName.setText(group.getName());
        groupViewHolder.storeCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                group.setChoosed(((CheckBox) v).isChecked());
                checkInterface.checkGroup(groupPosition, ((CheckBox) v).isChecked());
            }
        });
        groupViewHolder.storeCheckBox.setChecked(group.isChoosed());

        /**【文字指的是组的按钮的文字】
         * 当我们按下ActionBar的 "编辑"按钮， 应该把所有组的文字显示"编辑",并且设置按钮为不可见
         * 当我们完成编辑后，再把组的编辑按钮设置为可见
         * 不懂，请自己操作淘宝，观察一遍
         */
        if(group.isActionBarEditor()){
            group.setEditor(false);
            groupViewHolder.storeEdit.setVisibility(View.GONE);
            flag=false;
        }else{
            flag=true;
            groupViewHolder.storeEdit.setVisibility(View.VISIBLE);
        }

        /**
         * 思路:当我们按下组的"编辑"按钮后，组处于编辑状态，文字显示"完成"
         * 当我们点击“完成”按钮后，文字显示"编辑“,组处于未编辑状态
         */
        if (group.isEditor()) {
            groupViewHolder.storeEdit.setText("完成");
        } else {
            groupViewHolder.storeEdit.setText("编辑");
        }

        groupViewHolder.storeEdit.setOnClickListener(new GroupViewClick(group, groupPosition, groupViewHolder.storeEdit));
        return convertView;
    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        final ChildViewHolder childViewHolder;
        if (convertView == null) {
            convertView = View.inflate(mcontext, R.layout.item_shopcat_product, null);
            childViewHolder = new ChildViewHolder(convertView);
            convertView.setTag(childViewHolder);
        } else {
            childViewHolder = (ChildViewHolder) convertView.getTag();
        }

        /**
         * 根据组的编辑按钮的可见与不可见，去判断是组对下辖的子元素编辑  还是ActionBar对组的下瞎元素的编辑
         * 如果组的编辑按钮可见，那么肯定是组对自己下辖元素的编辑
         * 如果组的编辑按钮不可见，那么肯定是ActionBar对组下辖元素的编辑
         */
        if(flag){
            if (groups.get(groupPosition).isEditor()) {
                childViewHolder.editGoodsData.setVisibility(View.VISIBLE);
                childViewHolder.delGoods.setVisibility(View.VISIBLE);
                childViewHolder.goodsData.setVisibility(View.GONE);
            } else {
                childViewHolder.delGoods.setVisibility(View.VISIBLE);
                childViewHolder.goodsData.setVisibility(View.VISIBLE);
                childViewHolder.editGoodsData.setVisibility(View.GONE);
            }
        }else{

            if(groups.get(groupPosition).isActionBarEditor()){
                childViewHolder.delGoods.setVisibility(View.GONE);
                childViewHolder.editGoodsData.setVisibility(View.VISIBLE);
                childViewHolder.goodsData.setVisibility(View.GONE);
            }else{
                childViewHolder.delGoods.setVisibility(View.VISIBLE);
                childViewHolder.goodsData.setVisibility(View.VISIBLE);
                childViewHolder.editGoodsData.setVisibility(View.GONE);
            }
        }

        final GoodsInfo child = (GoodsInfo) getChild(groupPosition, childPosition);
        if (child != null) {
            childViewHolder.goodsName.setText(child.getDesc());
            childViewHolder.goodsPrice.setText("￥" + child.getPrice() + "");
            childViewHolder.goodsNum.setText(String.valueOf(child.getCount()));
            childViewHolder.goodsImage.setImageResource(R.drawable.cmaz);
            childViewHolder.goods_size.setText("门票:" + child.getColor() + ",类型:" + child.getSize());
            //设置打折前的原价
            SpannableString spannableString = new SpannableString("￥" + child.getPrime_price() + "");
            StrikethroughSpan span = new StrikethroughSpan();
            spannableString.setSpan(span,0,spannableString.length()-1+1, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
            //避免无限次的append
            if (childViewHolder.goodsPrimePrice.length() > 0) {
                childViewHolder.goodsPrimePrice.setText("");
            }
            childViewHolder.goodsPrimePrice.setText(spannableString);
            childViewHolder.goodsBuyNum.setText("x" + child.getCount() + "");
            childViewHolder.singleCheckBox.setChecked(child.isChoosed());
            childViewHolder.singleCheckBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    child.setChoosed(((CheckBox) v).isChecked());
                    childViewHolder.singleCheckBox.setChecked(((CheckBox) v).isChecked());
                    checkInterface.checkChild(groupPosition, childPosition, ((CheckBox) v).isChecked());
                }
            });
            childViewHolder.increaseGoodsNum.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    modifyCountInterface.doIncrease(groupPosition, childPosition, childViewHolder.goodsNum, childViewHolder.singleCheckBox.isChecked());
                }
            });
            childViewHolder.reduceGoodsNum.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    modifyCountInterface.doDecrease(groupPosition, childPosition, childViewHolder.goodsNum, childViewHolder.singleCheckBox.isChecked());
                }
            });
            childViewHolder.goodsNum.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showDialog(groupPosition,childPosition,childViewHolder.goodsNum,childViewHolder.singleCheckBox.isChecked(),child);
                }
            });
            childViewHolder.delGoods.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new AlertDialog.Builder(mcontext)
                            .setMessage("确定要删除该商品吗")
                            .setNegativeButton("取消",null)
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    modifyCountInterface.childDelete(groupPosition,childPosition);
                                }
                            })
                            .create()
                            .show();;
                }
            });
        }
        return convertView;
    }
    private void showDialog(final int groupPosition, final int childPosition, final View showCountView,final  boolean isChecked, final  GoodsInfo child) {
        final AlertDialog.Builder alertDialog_Builder=new AlertDialog.Builder(mcontext);
        View view= LayoutInflater.from(mcontext).inflate(R.layout.dialog_change_num,null);
        final AlertDialog  dialog=alertDialog_Builder.create();
        dialog.setView(view);
        count=child.getCount();
        final EditText num= (EditText) view.findViewById(R.id.dialog_num);
        num.setText(count+"");
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                UtilTool.showKeyboard(mcontext,showCountView);
            }
        });
        final TextView increase= (TextView) view.findViewById(R.id.dialog_increaseNum);
        final TextView DeIncrease=(TextView)view.findViewById(R.id.dialog_reduceNum);
        final TextView pButton= (TextView) view.findViewById(R.id.dialog_Pbutton);
        final TextView nButton= (TextView) view.findViewById(R.id.dialog_Nbutton);
        nButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        pButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int number=Integer.parseInt(num.getText().toString().trim());
                if(number==0){
                    dialog.dismiss();
                }else{
                    UtilsLog.i("数量="+number+"");
                    num.setText(String.valueOf(number));
                    child.setCount(number);
               modifyCountInterface.doUpdate(groupPosition,childPosition,showCountView,isChecked);
                 dialog.dismiss();
                }
            }
        });
        increase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                count++;
                num.setText(String.valueOf(count));
            }
        });
        DeIncrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(count>1){
                    count--;
                    num.setText(String.valueOf(count));
                }
            }
        });
        dialog.show();
    }
    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }
    public GroupEditorListener getGroupEditorListener() {
        return groupEditorListener;
    }
    public void setGroupEditorListener(GroupEditorListener groupEditorListener) {
        this.groupEditorListener = groupEditorListener;
    }
    public CheckInterface getCheckInterface() {
        return checkInterface;
    }
    public void setCheckInterface(CheckInterface checkInterface) {
        this.checkInterface = checkInterface;
    }
    public ModifyCountInterface getModifyCountInterface() {
        return modifyCountInterface;
    }
    public void setModifyCountInterface(ModifyCountInterface modifyCountInterface) {
        this.modifyCountInterface = modifyCountInterface;
    }
    static class GroupViewHolder {
        @BindView(R.id.store_checkBox)
        CheckBox storeCheckBox;
        @BindView(R.id.store_name)
        TextView storeName;
        @BindView(R.id.store_edit)
        TextView storeEdit;

        public GroupViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
    public interface CheckInterface {
        void checkGroup(int groupPosition, boolean isChecked);
        void checkChild(int groupPosition, int childPosition, boolean isChecked);
    }
        void doIncrease(int groupPosition, int childPosition, View showCountView, boolean isChecked);

        void doDecrease(int groupPosition, int childPosition, View showCountView, boolean isChecked);

        void doUpdate(int groupPosition,int childPosition,View showCountView, boolean isChecked);
        void childDelete(int groupPosition, int childPosition);
    }
    public interface GroupEditorListener {
        void groupEditor(int groupPosition);
    }
    private class GroupViewClick implements View.OnClickListener {
        private StoreInfo group;
        private int groupPosition;
        private TextView editor;

        public GroupViewClick(StoreInfo group, int groupPosition, TextView editor) {
            this.group = group;
            this.groupPosition = groupPosition;
            this.editor = editor;
        }
        @Override
        public void onClick(View v) {
            if (editor.getId() == v.getId()) {
                groupEditorListener.groupEditor(groupPosition);
                if (group.isEditor()) {
                    group.setEditor(false);
                } else {
                    group.setEditor(true);
                }
                notifyDataSetChanged();
            }
        }
    }


    static class ChildViewHolder {
        @BindView(R.id.single_checkBox)
        CheckBox singleCheckBox;
        @BindView(R.id.goods_image)
        ImageView goodsImage;
        @BindView(R.id.goods_name)
        TextView goodsName;
        @BindView(R.id.goods_size)
        TextView goods_size;
        @BindView(R.id.goods_price)
        TextView goodsPrice;
        @BindView(R.id.goods_prime_price)
        TextView goodsPrimePrice;
        @BindView(R.id.goods_buyNum)
        TextView goodsBuyNum;
        @BindView(R.id.goods_data)
        RelativeLayout goodsData;
        @BindView(R.id.reduce_goodsNum)
        TextView reduceGoodsNum;
        @BindView(R.id.goods_Num)
        TextView goodsNum;
        @BindView(R.id.increase_goods_Num)
        TextView increaseGoodsNum;
        @BindView(R.id.goodsSize)
        TextView goodsSize;
        @BindView(R.id.del_goods)
        TextView delGoods;
        @BindView(R.id.edit_goods_data)
        LinearLayout editGoodsData;

        public ChildViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
</pre>

***
## 总结
其实代码量还是挺多的，主要的还是思路。思路理明白了，自然水到渠成。代码照着打一遍，思路自然明了。难点在于店铺与商品，单选框，编辑按钮之间的关系处理和布局的改变。
