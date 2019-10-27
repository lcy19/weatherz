package com.example.weatherz;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.weatherz.Bean.UserEntity;
import com.example.weatherz.Utils.QGridView;
import com.example.weatherz.Utils.ToastUtil;
import com.example.weatherz.adapter.CYBChangeCityGridViewAdapter;
import com.example.weatherz.adapter.ContactAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import me.yokeyword.indexablerv.IndexableAdapter;
import me.yokeyword.indexablerv.IndexableHeaderAdapter;
import me.yokeyword.indexablerv.IndexableLayout;

public class CityPickerActivity extends AppCompatActivity{
    private ContactAdapter mAdapter;
    private BannerHeaderAdapter mBannerHeaderAdapter;
    //热门城市的数值
    private String[] city = {"北京","上海","广州","深圳","杭州","长沙","成都","临沂","福州","济南","苏州","青岛"};
    private IndexableLayout indexableLayout;
    //热门城市的适配器
    private CYBChangeCityGridViewAdapter cybChangeCityGridViewAdapter;
    //热门城市的集合
    private ArrayList<String> list;
    private ImageView pic_contact_back;
    private EditText search_city;
    private Button btn_search_city;
    private Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick_contact);

        initview();
        initAdapter();
        onlisten();
    }

    public void initAdapter(){
        mAdapter = new ContactAdapter(this);
        indexableLayout.setAdapter(mAdapter);
//        列表查询时，中心显示首字母拼音
        indexableLayout.setOverlayStyle_Center();
//        初始化所有城市的数据
        mAdapter.setDatas(initDatas());
//        气泡显示为红色，居右
//        indexableLayout.setOverlayStyle_MaterialDesign(Color.RED);
        // 全字母排序。  排序规则设置为：每个字母都会进行比较排序；速度较慢
        indexableLayout.setCompareMode(IndexableLayout.MODE_FAST);

        // 这里BannerView只有一个Item, 添加一个长度为1的任意List作为第三个参数
        List<String> bannerList = new ArrayList<>();
        bannerList.add("");
//        indexTitle 索引栏的标题，字符串类型
        mBannerHeaderAdapter = new BannerHeaderAdapter("❤", null, bannerList);
        indexableLayout.addHeaderAdapter(mBannerHeaderAdapter);
    }

    public void initview(){
        intent = getIntent();
        pic_contact_back =  findViewById(R.id.pic_contact_back);
        search_city = (EditText)findViewById(R.id.search_city);
        btn_search_city = (Button)findViewById(R.id.btn_search_city);
        indexableLayout =  findViewById(R.id.indexableLayout);
        indexableLayout.setLayoutManager(new LinearLayoutManager(this));

    }

    public void onlisten(){
        pic_contact_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btn_search_city.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String city = search_city.getText().toString();
                intent.putExtra("city",city);
                setResult(RESULT_OK, intent);
                finish();
            }
        });

        mAdapter.setOnItemContentClickListener(new IndexableAdapter.OnItemContentClickListener<UserEntity>() {
            @Override
            public void onItemClick(View v, int originalPosition, int currentPosition, UserEntity entity) {
                if (originalPosition >= 0) {
                    intent.putExtra("city", entity.getNick());
                    setResult(RESULT_OK, intent);
                    finish();
                } else {
                    ToastUtil.showShort(CityPickerActivity.this, "选中Header/Footer:" + entity.getNick() + "  当前位置:" + currentPosition);
                }
            }
        });
    }

    /**
     * 自定义的Banner Header
     */
    class BannerHeaderAdapter extends IndexableHeaderAdapter {
        private static final int TYPE = 1;

        public BannerHeaderAdapter(String index, String indexTitle, List datas) {
            super(index, indexTitle, datas);
        }

        @Override
        public int getItemViewType() {
            return TYPE;
        }

        @Override
        public RecyclerView.ViewHolder onCreateContentViewHolder(ViewGroup parent) {
            View view = LayoutInflater.from(CityPickerActivity.this).inflate(R.layout.item_city_header, parent, false);
            VH holder = new VH(view);
            return holder;
        }

        @Override
        public void onBindContentViewHolder(RecyclerView.ViewHolder holder, Object entity) {
            // 数据源为null时, 该方法不用实现
            final VH vh = (VH) holder;
            list=new ArrayList<>();
            for(int i = 0; i<city.length; i++){
                list.add(city[i]);
            }
            System.out.println("------------city"+list);
            cybChangeCityGridViewAdapter=new CYBChangeCityGridViewAdapter(CityPickerActivity.this, list);
            vh.head_home_change_city_gridview.setAdapter(cybChangeCityGridViewAdapter);
            vh.head_home_change_city_gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    intent.putExtra("city", list.get(position));
                    System.out.println("aaaaaayyyyyyyyy"+list.get(position));
                    setResult(RESULT_OK, intent);
                    finish();
                }
            });
            vh.item_header_city_dw.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    intent.putExtra("city", vh.item_header_city_dw.getText().toString());
                    setResult(RESULT_OK, intent);
                    finish();
                }
            });

        }

        private class VH extends RecyclerView.ViewHolder {
            GridView head_home_change_city_gridview;
            TextView item_header_city_dw;
            public VH(View itemView) {
                super(itemView);
                head_home_change_city_gridview =(QGridView)itemView.findViewById(R.id.item_header_city_gridview);
                item_header_city_dw = itemView.findViewById(R.id.item_header_city_dw);
            }
        }
    }

    private List<UserEntity> initDatas() {
        List<UserEntity> list = new ArrayList<>();
        // 初始化数据
        List<String> contactStrings = Arrays.asList(getResources().getStringArray(R.array.provinces));
        List<String> mobileStrings = Arrays.asList(getResources().getStringArray(R.array.provinces));
        for (int i = 0; i < contactStrings.size(); i++) {
            UserEntity contactEntity = new UserEntity(contactStrings.get(i), mobileStrings.get(i));
            list.add(contactEntity);
        }
        return list;
    }
}
