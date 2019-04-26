package com.apps.gragas.storydirector.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.apps.gragas.storydirector.Implements.Characters;
import com.apps.gragas.storydirector.R;

import java.util.ArrayList;

import androidx.recyclerview.widget.RecyclerView;

public class charactersAdapter extends RecyclerView.Adapter<charactersAdapter.MyViewHolder>{

    private  ArrayList<Characters> items = new ArrayList<>(); //список персонажей для передачи на адаптер
    static Context context;

    public charactersAdapter(ArrayList<Characters> mDataset, Context context){ //конструктор, адаптер при создании получает данные в виде списка
        items = mDataset;
        this.context = context;

    }



    public static class MyViewHolder extends RecyclerView.ViewHolder { //implements для вызова контекстного меню. Смотрим контруктор холдера.

        public TextView characterName; //текстовое поле для отображения того, что есть
        //// в списке (в главном классе активности)
        public View listParentLayout; //это тип уже выше в иерархии, будем ссылаться
        //на LinearLayout


        public MyViewHolder(View v){ //конструктор холдера
            super(v);
            //конвертация в java-объекты
            characterName = (TextView) v.findViewById(R.id.tv_nameHero); //TextView
            listParentLayout = (View) v.findViewById(R.id.lin_layout_characters); //LinearLayout

        }
    }



    @Override
    public charactersAdapter.MyViewHolder onCreateViewHolder (ViewGroup parent, int viewType){
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_characters_char_fragment,parent,false);//ОБЯЗАТЕЛЬНО НУЖЕН НОВЫЙ ЛЭЙАУТ!!!! My_view – название второго xml, //который определяет форму вывода списка.
        return new MyViewHolder(v);

    }



    @Override
    public void onBindViewHolder (MyViewHolder holder, final int position){
        //holder.mTextView.setText(items.get(position));


        if (holder instanceof MyViewHolder) {

            final MyViewHolder view = (MyViewHolder) holder;

            //Здесь идет заполнение элементов списка
            view.characterName.setText(items.get(position).getName());



        }

    }


    @Override //метод, возвращающий позицию списка, которую выбрали для вызова контекстного меню
    public long getItemId(int position) {

        return super.getItemId(position);
    }

    @Override
    public int getItemCount(){ //размер нашего списка
        return items.size();

    }


}
