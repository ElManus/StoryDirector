package com.apps.gragas.storydirector.Adapters;

import com.apps.gragas.storydirector.Animation.ItemAnimation;

import android.content.Context;

import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.apps.gragas.storydirector.Implements.Projects;
import com.apps.gragas.storydirector.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class projectsAdapter extends RecyclerView.Adapter<projectsAdapter.MyViewHolder>  {

    private  ArrayList<Projects> items = new ArrayList<>(); //список проектов-объектов для передачи на адаптер
    private static final String TAG = "InfoMessage"; //Tag для Logcat
    static Context context;
    static boolean flag_refresh ;

    private final String LOG = "###MY_DB_LOG###";








    private OnClickListener mOnClickListener; //переменная слушателя
    private OnPopupListener mOnPopupListener; //переменная слушателя


    public interface OnClickListener { //добавляем новый интерфейс, отвечающий за нажатие по элементу списка
        void onItemClick (MyViewHolder view, int position);
    }

    public interface OnPopupListener { //интерфейс для вызова коллбэка, по лонг-клику в главной активити будет сделан вызов
        void onPopupClick (int position, View view); // в mainactivity будет вызываться этот метод
        //position - номер позиции (минус один) пункта меню, будет исп-ся для удаления или правки
        //View view - сюда мы должны засунуть ту вьюшку, которая является текущей нажатой - элемент списка recyclerView.
        //Она нужна в главной активности, к ней будет привязываться попапМеню (чтобы меню стартовало там, где нажал).
    }

    public void setOnClickListener (final OnClickListener mItemClickListener){ //этот метод будем использовать в mainActivity
        this.mOnClickListener = mItemClickListener;
    }

    public void setOnPopupListener (final OnPopupListener mPopupClickListener){
        this.mOnPopupListener = mPopupClickListener; //при долгом нажатии будет вызываться эта переменная

    }


    public projectsAdapter(ArrayList<Projects> mDataset, Context context){ //конструктор, адаптер при создании получает данные в виде списка
        items = mDataset;
        this.context = context;

    }


    public static class MyViewHolder extends RecyclerView.ViewHolder { //implements для вызова контекстного меню. Смотрим контруктор холдера.


        public TextView projectName; //текстовое поле для отображения того, что есть
        //// в списке (в главном классе активности)
        public View listParentLayout; //это тип уже выше в иерархии, будем ссылаться
        //на LinearLayout
        public TextView m_MainInfo; //текстовое поле вывода главной информации о проекте

        public TextView moreMenu;//контекстное меню из трех точек

        public TextView dateTime;


        public MyViewHolder(View v){ //конструктор холдера
            super(v);

            //конвертация в java-объекты
            projectName = (TextView) v.findViewById(R.id.textview2); //TextView
            listParentLayout = (View) v.findViewById(R.id.lin_layout); //LinearLayout
            m_MainInfo = (TextView) v.findViewById(R.id.t_mainInfo); //TextView
            moreMenu = (TextView) v.findViewById(R.id.threeDotsMainList);
            dateTime = (TextView) v.findViewById(R.id.dateTime);

        }

    }

    @Override
    public projectsAdapter.MyViewHolder onCreateViewHolder (ViewGroup parent, int viewType){
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list,parent,false);//ОБЯЗАТЕЛЬНО НУЖЕН НОВЫЙ ЛЭЙАУТ!!!! My_view – название второго xml, //который определяет форму вывода списка.
        return new MyViewHolder(v);

    }

    @Override
    public void onBindViewHolder (MyViewHolder holder, final int position){
        //holder.mTextView.setText(items.get(position));


        if (holder instanceof MyViewHolder) {

            final MyViewHolder view = (MyViewHolder) holder;

            datesChange(view, position);

            /*
            Установка слушателя обработки нажатий на элемент списка.
             */
            view.listParentLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnClickListener != null) {
                        mOnClickListener.onItemClick(view, position);

                    }
                }
            });

            /*
            установка слушателя на сабменю.
             */
            view.moreMenu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnPopupListener != null){
                        mOnPopupListener.onPopupClick(position,v);
                    }
                }
            });

            /*
            end
             */
            setAnimation(view.itemView, position);

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

    public void mnotifyData(ArrayList <Projects> mList){

        this.items = mList;
        notifyDataSetChanged();
    }

    /*
    Блок анимации
     */
    private int lastPosition = -1;
    private boolean on_attach = true;

    private void setAnimation (View view, int position){
        if(position > lastPosition){
            //2 - выбранный тип анимации
            // Log.i(TAG, "DDD");
            ItemAnimation.animate(view, on_attach ? position : -1, 1);
            lastPosition = position;
        }
    }

    private void datesChange (MyViewHolder view, int position){
        view.projectName.setText(items.get(position).getProjectName());
        view.m_MainInfo.setText(items.get(position).getMainInfo());

        String tmp_day_changed = items.get(position).getDataChanging().substring(0,2);
        String tmp_month_changed = items.get(position).getDataChanging().substring(3,5);
        String tmp_year_changed = items.get(position).getDataChanging().substring(6,10);

        SimpleDateFormat simpleDateFormatMain = new SimpleDateFormat("dd.MM.yyyy");
        String tmp_currentDate = simpleDateFormatMain.format(new Date());

        String tmp_day_current = tmp_currentDate.substring(0, 2);
        String tmp_month_current = tmp_currentDate.substring(3, 5);
        String tmp_year_current = tmp_currentDate.substring(6,10);


        boolean bDay, bMonth, bYear;
        bDay = false;
        bMonth = false;
        bYear = false;

        bDay = tmp_day_changed.equals(tmp_day_current);
        bMonth = tmp_month_changed.equals(tmp_month_current);
        bYear = tmp_year_changed.equals(tmp_year_current);

        if (bDay == true && bMonth == true && bYear == true)
        {
            // Log.i(LOG, "---------------" );
            //Log.i(LOG, "time  = " + items.get(position).getTimeChanging());
            view.dateTime.setText(items.get(position).getTimeChanging());
        }
        else
        {
            // Log.i(LOG, "---------------" );
            //Log.i(LOG, "Изменения прошли не сегодня.");
            view.dateTime.setText(items.get(position).getDataChanging());
        }

    }






}
