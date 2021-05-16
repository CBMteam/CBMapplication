package com.cbm.cbmapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class ListViewAdpater extends BaseAdapter {
    private TextView titleTextView;
    private TextView contentTextView;

    //Adapter에 추가된 데이터를 저장하기 위한 ArrayList
    private ArrayList<ListVIewItem> listViewItemList = new ArrayList<ListVIewItem>();

    //ListViewAdapter의 생성자
    public ListViewAdpater(){

    }

    public void clear() {
        listViewItemList.clear();
    }

    //Adapter에 사용되는 데이터의 개수를 리턴
    @Override
    public int getCount() {
        return listViewItemList.size();
    }

    //position에 위치한 데이터를 화면에 출력하는데 사용될 VIew를 리턴
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int pos = position;
        final Context context=parent.getContext();

        //"listview_item" Layout 을 inflate하여 convertView 참조 획득
        if(convertView == null){
            LayoutInflater inflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView=inflater.inflate(R.layout.cal_list_item,parent,false);
        }

        //화면에 표시될 View(Layout이 inflate된 )으로부터 위젯에 대한 참조 획득
       titleTextView=(TextView) convertView.findViewById(R.id.listtitle);
        contentTextView=(TextView) convertView.findViewById(R.id.listcontent);

        ListVIewItem listVIewItem = listViewItemList.get(position);

        //아이템 내 각 위젯에 데이터 반영
        titleTextView.setText(listVIewItem.getTitle());
        contentTextView.setText(listVIewItem.getContent());

        return convertView;
    }

    //지정한 위치(position)에 있는 데이터와 관꼐된 아이템(row)의 ID를 리턴
    @Override
    public long getItemId(int position) {
        return position;
    }

    //지정한 위치(position)에 있는 데이터 리턴
    @Override
    public Object getItem(int position) {
        return listViewItemList.get(position);
    }

    //아이템 추가를 위한 함수
    public void addItem(String title,String content){
        ListVIewItem item=new ListVIewItem();

        item.setTitle(title);
        item.setContent(content);

        listViewItemList.add(item);
    }
}
