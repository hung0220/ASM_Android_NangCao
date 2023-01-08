package com.example.asm_android_nang_cao;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class SanPhamAdapter extends BaseAdapter {

    Context myContext;
    int myLayout;
    List<SanPham> arrSanPham;

    public SanPhamAdapter(Context myContext, int myLayout, List<SanPham> arrSanPham) {
        this.myContext = myContext;
        this.myLayout = myLayout;
        this.arrSanPham = arrSanPham;
    }

    @Override
    public int getCount() {
        return arrSanPham.size();
    }

    @Override
    public Object getItem(int i) {
        return arrSanPham.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }
    private class ViewHolder {
        ImageView imgHinh;
        TextView txtTenSp, txtGiaSp, txtGhiChu;
    }
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater inflater = (LayoutInflater) myContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = view;
        ViewHolder holder = new ViewHolder();

        if (rowView == null) {
            rowView = inflater.inflate(myLayout, null);
            holder.txtTenSp = (TextView) rowView.findViewById(R.id.txtTenMotDong);
            holder.txtGiaSp = (TextView) rowView.findViewById(R.id.txtGiaMotDong);
            holder.txtGhiChu = (TextView) rowView.findViewById(R.id.txtGhiChuMotDong);
            holder.imgHinh = (ImageView) rowView.findViewById(R.id.imgMotDong);
            rowView.setTag(holder);

        }else {
            holder = (ViewHolder) rowView.getTag();
        }


        // gan gia tri
        holder.txtTenSp.setText(arrSanPham.get(i).getTenSp());
        holder.txtGiaSp.setText("Giá: "+arrSanPham.get(i).getGiaSp()+"K");
        holder.txtGhiChu.setText("Ghi Chú: "+arrSanPham.get(i).getGhiChu());


        Picasso.get().load(arrSanPham.get(i).getLinkHinhSp()).into(holder.imgHinh);

        Animation animation = AnimationUtils.loadAnimation(myContext, R.anim.list_item_anim);
        rowView.startAnimation(animation);



        return rowView;
    }
}
