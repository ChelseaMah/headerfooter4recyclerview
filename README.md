# HeaderView & FooterView For RecyclerView

为RecyclerView提供HeaderView&FooterView，使用简单，不利用Adapter的getItemViewType()方法，正产使用Adapter，**对RecyclerView的item数量没有影响**， 横向、纵向都可以使用，可快速集成。



## HeaderView[^footnote]
 [^footnote]:https://github.com/blipinsk/RecyclerViewHeader.
 
HeaderView 的原作者：Bartek Lipinski（https://github.com/blipinsk）
我只对HeaderView进行了微小的改动。
## FooterVew
顺着HeaderView的思路，写出了FooterView。
与HeaderView不同的是，HeaderView主要在布局中摆好HeaderView的初始位置，FooterView则不需要。
##局限性
目前仅支持LinearLayoutManeger和其子类GridLayoutManager的RecyclerView，其余需要读按需要自行开发。

##使用方法：
```Java
    RecyclerViewHeader recyclerHeader = (RecyclerViewHeader) view.findViewById(R.id.header);
recyclerHeader.attachTo(recycler);

RecyclerViewFooter recyclerFooter = (RecyclerViewFooter) view.findViewById(R.id.footer);
recyclerFooter.attachTo(recycler);
```
```xml
<?xml version="1.0" encoding="utf-8"?>
<FrameLayout xmlns:android="http://schemas.android.com/apk/res/android"
             android:layout_width="match_parent"
             android:layout_height="match_parent">

    <android.support.v7.widget.RecyclerView
        android:id="@+id/recycler"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:layout_gravity="center_horizontal|top"/>

    <com.mcx.footerheaderforrecyclerview.RecyclerViewHeader
        android:id="@+id/header"
        android:layout_width="match_parent"
        android:layout_height="wrap_content">
        <ImageView
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:src="@mipmap/ic_launcher"
            android:layout_margin="20dp"/>

        <TextView
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:padding="20dp"
            android:text="@string/header"
            android:textSize="18sp"
            android:textColor="@color/clouds"
            android:background="@color/colorAccent"/>



    </com.mcx.footerheaderforrecyclerview.RecyclerViewHeader>

    <com.mcx.footerheaderforrecyclerview.RecyclerViewFooter
        android:id="@+id/footer"
        android:layout_width="match_parent"
        android:layout_height="wrap_content">

        <TextView
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:padding="20dp"
            android:text="@string/footer"
            android:textSize="18sp"
            android:textColor="@color/clouds"
            android:background="@color/colorPrimary"/>

    </com.mcx.footerheaderforrecyclerview.RecyclerViewFooter>
</FrameLayout>
```
特别感谢Bartek Lipinski 提供思路
