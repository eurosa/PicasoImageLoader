package com.example.w3e_52.picasoimageloader;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by w3e-52 on 3/15/16.
 */
public class CardViewAdapter extends RecyclerView.Adapter<ViewHolder> {
    Context context;

    private static ArrayList<Persons> dataSet;


    public CardViewAdapter(ArrayList<Persons> dataSet, Context context) {

        this.dataSet = dataSet;
        this.context=context;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
// create a new view
        View itemLayoutView = LayoutInflater.from(viewGroup.getContext()).inflate(
                R.layout.recylce_item, null);

        // create ViewHolder

        ViewHolder viewHolder = new ViewHolder(itemLayoutView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        final int pos = i;
        Persons fp = dataSet.get(i);
        TextView textName=viewHolder.txtName;
        TextView imageList=viewHolder.imageList;
        textName.setText(fp.getId());
        imageList.setText(fp.getName());
//        ImageView imageShow=viewHolder.imageShow;


        System.out.println("SSSS"+dataSet.get(i).getName());

        Picasso.with(context)
                .load(dataSet.get(i).getName())
                .placeholder(R.drawable.icon).fit().centerCrop()
                .into(viewHolder.imageShow);
//        Picasso.with(context).load(dataSet.get(i).getName()).into(viewHolder.imageShow);
//        ImageLoader imageLoader=new ImageLoader(this.context);
//        imageLoader.DisplayImage(fp.getName(),imageShow);
//        viewHolder.feed = fp;
//       chkSelected = (CheckBox) chkSelected.findViewById(R.id.chkSelected);
//        viewHolder.chkSelected.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//                CheckBox cb = (CheckBox) v;
//                Persons contact = (Persons) cb.getTag();
//
//                contact.setSelected(cb.isChecked());
//                dataSet.get(pos).setSelected(cb.isChecked());
//
//                Toast.makeText(
//                        v.getContext(),
//                        "Clicked on Checkbox: " + cb.getText() + " is "
//                                + cb.isChecked(), Toast.LENGTH_LONG).show();
//            }
//        });

    }


    @Override
    public int getItemCount() {
        return dataSet.size();
    }
}

// inner class to hold a reference to each item of RecyclerView
class ViewHolder extends RecyclerView.ViewHolder {

    public TextView imageList;
    public TextView txtName;

    public ImageView imageShow;
    public Persons feed;
    CheckBox chkSelected;
    public ViewHolder(View itemLayoutView) {
        super(itemLayoutView);

        this.imageList = (TextView) itemLayoutView.findViewById(R.id.image_list);
        this.txtName=(TextView)itemLayoutView.findViewById(R.id.textName);
        this.imageShow=(ImageView)itemLayoutView.findViewById(R.id.imageMine);

//
//         chkSelected = (CheckBox) itemLayoutView
//                .findViewById(R.id.chkSelected);

//
//        chkSelected.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                System.out.println("Sabuj");
////if (feed.isSelected()==true) {
//
//
////                    Intent intent = new Intent(v.getContext(), SecondPage.class);
////                    v.getContext().startActivity(intent);
//Toast.makeText(v.getContext(), "Image is: " + feed.getId(), Toast.LENGTH_SHORT).show();
////}
//            }
//        });


    }

}


