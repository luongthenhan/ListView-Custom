package com.example.thenhan.listview_custom;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import vn.khmt.entity.Book;

/**
 * Created by TheNhan on 4/13/2016.
 */
public class BookAdapter extends BaseAdapter {

    private ArrayList<Book> bookList;
    private LayoutInflater inflater;

    public BookAdapter(Context context, ArrayList<Book> bookList){
        this.bookList = bookList;
        Log.e("BOOK", String.valueOf(this.bookList.size()));
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return bookList.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private class Holder{
        private ImageView img;
        private TextView title;
        private TextView authorYear;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder = new Holder();

        // Get view of item
        View rowView = this.inflater.inflate(R.layout.item_view, null);
        holder.img = (ImageView) rowView.findViewById(R.id.imageView);
        holder.title = (TextView) rowView.findViewById(R.id.titleView);
        holder.authorYear = (TextView) rowView.findViewById(R.id.authorView);

        // Fill contents
        holder.img.setImageResource(R.drawable.ic_launcher);
        holder.title.setText(this.bookList.get(position).getTitle());
        holder.authorYear.setText(this.bookList.get(position).getAuthor()
            + ", " + this.bookList.get(position).getYear());

        return rowView;
    }
}