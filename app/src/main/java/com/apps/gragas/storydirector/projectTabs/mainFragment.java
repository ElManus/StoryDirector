package com.apps.gragas.storydirector.projectTabs;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import io.realm.Realm;
import io.realm.RealmList;

import android.text.method.KeyListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.apps.gragas.storydirector.Implements.Characters;
import com.apps.gragas.storydirector.Implements.Projects;
import com.apps.gragas.storydirector.MainActivity;
import com.apps.gragas.storydirector.R;
import com.apps.gragas.storydirector.Tools.CharsMainFragAdapter;
import com.apps.gragas.storydirector.projectsActivity;
import com.google.android.material.chip.Chip;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class mainFragment extends Fragment {

    private Drawable iconToolbarSave;
    private Drawable iconToolbarBack;
    private boolean flag_edit;

    private TextView podrobnosi;
    private TextView heroPodrobnosti;
    private TextView villPodrobnosti;
    private Chip chip_hero;
    private Chip chip_villian;

    private CharsMainFragAdapter adapter_hero;
    private CharsMainFragAdapter adapter_villians;


    private FloatingActionButton fab; //плавающая кнопка

    private Realm mRealmProjects; //база данных

    private String nameFromHeroNameList; //имя из списка
    private String nameFromVillaianNameList;

    private String nameHeroFromChip;
    private String nameVillianFromChip;

    private View view;

    private Projects resultFindProject;


    static String ADDITION_INFO = "Ещё";
    static String MINIMIZE = "Свернуть";

    public KeyListener listener_mainInfo;
    public KeyListener listener_mainGoal;
    public KeyListener listener_mainHero;
    public KeyListener listener_mainAntag, listener_whatHappensIf, listener_mainQuestionGlobal, listener_mainQuestionPersonal,listener_mainQuestionPrivate;
    public KeyListener listener_mainTheme, listener_mainContreTheme, listener_mainSyntez;

    private ListView charsList;
    private ListView vilList;

    //ярлыки, сейчас исп-ся только второй
    private Integer [] charIcons = {R.drawable.ic_person_add_black_24dp, R.drawable.ic_person_black_24dp};

    private EditText putNewPersName;
    private AppCompatActivity activity;
    public Intent intent;
    private Toolbar toolbar;
    private  TextView projectName; //во фрагменте
    private EditText mainInfo; //во фрагменте
    private EditText mainHero;
    private EditText mainGoal;
    private TextView dateChanging;
    private TextView dateCreating;
    private EditText mainAntag, whatHappensIf, mainQuestionGlobal, mainQuestionPrivate, mainQuestionPersonal, mainTheme, mainContreTheme, mainSyntez;


    private View expandable_podrobnosti;
    private View expandable_layout_chars;
    private ViewGroup expandable_layout_vills;
    private TextView addNewHero;
    private TextView addNewVillian;
    private TextView numberCharacters;
    private TextView numberWords;

    private boolean flag_existingChar;



    private View parent_view; //ссылка на главный xml

    private String t_prName, t_maininfo, t_mainHero, t_mainGoal, t_dateChanging, t_timeChanging, t_dateCreating, t_timeCreating, t_mainAntag, t_whatHappensIf, t_mainQuestionGlobal, t_mainQuestionPersonal, t_mainQuestionPrivate, t_mainTheme, t_mainContreTheme, t_mainSyntez; //переменные для ввода имени и информации
    private int position;

    private ArrayList <String> temp_projects; //переменная списка для получения пакета из активности проектов
    private ArrayList <String> temp_chars; //переменная списка для персонажей


    private CallBackGetFlagEdit callBackGetFlagEdit; //объявление имплементации интерфейса данного коллбэка. Передает в projectActivity состояние флага изменения полей




    //коллбэк для передачи в projectActivity флага редактирования
    public interface CallBackGetFlagEdit {
        void onCallBack(boolean flag_edit); //что передаем - пишем внутри коллбэка
    }




    public mainFragment () {

    }

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setFlag_edit(); //flag is false

        flag_existingChar = false;

        setHasOptionsMenu(true); //включаем поддержку меню для тулбара.

        mRealmProjects.init(getContext());
        mRealmProjects = Realm.getDefaultInstance();



      }




    //реализация передачи данных в активность из фрагмента
    @Override
    public void onAttach(@NonNull Context context) {
        //ищем активность, к которой подключен данный фрагмент. Пробуем привести эту активность к данному типу интерфейса
        super.onAttach(context);
        try{
            callBackGetFlagEdit = (CallBackGetFlagEdit) context;

        } catch (ClassCastException e){
            throw new ClassCastException(context.toString() + " must implement CallBackGetFlagEdit");
        }

    }



    @SuppressLint("ResourceAsColor")
    @Override public View onCreateView(@NonNull final LayoutInflater inflater, ViewGroup container,
                                       Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.main_fragment, container, false);
        parent_view = view.findViewById(android.R.id.content); //Это идентификатор, который вручную прописал в xml у layout

        heroPodrobnosti = view.findViewById(R.id.tv_heroPodrobnosti);
        villPodrobnosti = view.findViewById(R.id.tv_villiansPodrobnosti);

       // addNewHero = view.findViewById(R.id.tv_addNewHero_MainFragment);
       // addNewVillian = view.findViewById(R.id.tv_addNewVillian_MainFragment);


        chip_hero = (Chip) view.findViewById(R.id.chip_hero);
        chip_hero.setClickable(true);
        chip_hero.setVisibility(View.GONE);

        chip_villian = view.findViewById(R.id.chip_villian);
        chip_villian.setClickable(true);
        chip_villian.setVisibility(View.GONE);
        chip_villian.setText("");
        chip_hero.setText("");

        //СПИСОК ПЕРСОНАЖЕЙ
        //лист для передачи в адаптер списка
        charsList = (ListView) view.findViewById(R.id.lvCharacters);
        vilList = view.findViewById(R.id.lvVillians);

        //лэйаут с персонажами
        expandable_layout_chars = (View) view.findViewById(R.id.expandable_layout_chars);

        //лэйаут с злодеями
        expandable_layout_vills = view.findViewById(R.id.expandable_layout_villians);

        //Поля для ввода кол-ва символов и слов
        numberCharacters = view.findViewById(R.id.tv_numberCharacters);
        numberWords = view.findViewById(R.id.tv_numberWords);

        //Поля для ввода дат
        dateCreating = view.findViewById(R.id.tv_create_date);
        dateChanging = view.findViewById(R.id.tv_change_date);

        //синяя текстовая кнопка "Подробности"
        podrobnosi = (TextView) view.findViewById(R.id.tv_pordobnosti);

        //Лэйаут с подробностями
        expandable_podrobnosti = (View) view.findViewById(R.id.lyt_expandable_podrobnosti);

        //сворачиваем layout с дополнительной информацией
        expandable_podrobnosti.setVisibility(View.GONE);

        //сворачиваем layout с персонажами и злодеями
        expandable_layout_chars.setVisibility(View.GONE);
        expandable_layout_vills.setVisibility(View.GONE);

        mainInfo =  view.findViewById(R.id.et_mainInfo); //info in fragment
        mainHero = view.findViewById(R.id.et_mainHero);

        mainHero.setVisibility(View.GONE);


        mainGoal = view.findViewById(R.id.et_mainGoal);
        mainAntag = view.findViewById(R.id.et_mainAntag);

        mainAntag.setVisibility(View.GONE);

        whatHappensIf  = view.findViewById(R.id.et_whatHappensIf);
        mainQuestionGlobal = view.findViewById(R.id.et_mainQuestionGlobal);
        mainQuestionPersonal = view.findViewById(R.id.et_mainQuestionPersonal);
        mainQuestionPrivate = view.findViewById(R.id.et_mainQuestionPrivate);
        mainTheme = view.findViewById(R.id.et_mainTheme);
        mainContreTheme = view.findViewById(R.id.et_mainContreTheme);
        mainSyntez = view.findViewById(R.id.et_mainSyntez);


        temp_projects = new ArrayList<>(); //список для получения информации из посылки
        temp_chars = new ArrayList<>(); //список имён персонажей, будет браться из базы


        //intent = new Intent(getActivity(), MainActivity.class);

        //подключение к самодельному адаптеру
        adapter_hero = new CharsMainFragAdapter(getActivity(), temp_chars, charIcons);
        charsList.setAdapter(adapter_hero);
        adapter_villians  = new CharsMainFragAdapter(getActivity(),temp_chars, charIcons);
        vilList.setAdapter(adapter_villians);

        //определение плавающей кнопки
        fab = view.findViewById(R.id.fab_edit);




        return view;
    }


    @Override
    public void onStart (){
        super.onStart();



        final View view = getView(); //Метод getView() получает корневой объект View фрагмента.
        // Далее полученный объект используется для получения ссылок на надписи

        if(view!=null){



            //скрываем кнопку подробностей у персонажей
            //будут открываться только при редактировании
            //обнуляем строки в чипах
            chip_hero.setText("");
            chip_villian.setText("");
            chip_hero.setVisibility(View.GONE);
            chip_villian.setVisibility(View.GONE);
            heroPodrobnosti.setVisibility(View.GONE);
            chip_hero.setVisibility(View.GONE);
            villPodrobnosti.setVisibility(View.GONE);

            //получаем данные из пакета от активности проектов
            temp_projects = getArguments().getStringArrayList("bundle");
            t_prName = temp_projects.get(0); //вытаскиваем имя

            //ОТКРЫТИЕ БАЗЫ
            //НАЧАЛО ТРАНЗАКЦИИ



            //ищем проект по имени
            //TODO если ходить по фрагментам, то выскакивает ошибка с доступом к закрытой базе с ссылкой на строку ниже
            final Projects resultsForRead = mRealmProjects.where(Projects.class).equalTo("projectName", t_prName).findFirst();
            resultFindProject = resultsForRead;

            //получаем список персонажей текущего проекта
            RealmList<Characters> tmp1 = resultFindProject.getCharacters();

            //переносим персонажей в список для адаптера листа
            Log.i("LOG", "==> mainFragment. From base to list");
            if (tmp1 != null) {
                temp_chars.clear();
                for (int i = 0; i < tmp1.size(); i++) {
                    Log.i("LOG", i + ". " +tmp1.get(i).getName());
                    temp_chars.add(tmp1.get(i).getName());
                }
            }

            t_maininfo = resultsForRead.getMainInfo();

            /*
            получение имени героя.
             */
            t_mainHero = resultsForRead.getMainHero();

            /*
            ищем, не удален ли этот персонаж из базы
             */
            for (int i=0; i<tmp1.size();i++){
                if (tmp1.get(i).getName().equals(t_mainHero)){
                        flag_existingChar = true;
                }
            }


            if (t_mainHero != null && !t_mainHero.isEmpty() && flag_existingChar == true)
            {
                //если с базы имя героя считалось, тогда
                //в чип ставим имя
                chip_hero.setText(t_mainHero);
                //открываем для видимости чип
                chip_hero.setVisibility(View.VISIBLE);
                //прячем кнопку подробностей о героях
                heroPodrobnosti.setVisibility(View.GONE);
                chip_hero.setCloseIconVisible(false);
                chip_hero.setClickable(false);

                flag_existingChar = false;
            }
            else
                chip_hero.setVisibility(View.GONE);



           /* adapter_hero.notifyDataSetChanged();
            adapter_villians.notifyDataSetChanged();
            charsList.setAdapter(adapter_hero);
            vilList.setAdapter(adapter_villians);
            */

            t_mainGoal = resultsForRead.getMainGoal();
            t_dateChanging = resultsForRead.getDataChanging();
            t_timeChanging = resultsForRead.getTimeChanging();
            t_dateCreating = resultsForRead.getDataCreating();
            t_timeCreating = resultsForRead.getTimeCreating();


            //Антагонист, злодей
            t_mainAntag = resultsForRead.getMainAntag();
              /*
            ищем, не удален ли этот персонаж из базы
             */
            for (int i=0; i<tmp1.size();i++){
                if (tmp1.get(i).getName().equals(t_mainAntag)){
                    flag_existingChar = true;
                }
            }

            if (t_mainAntag != null && ! t_mainAntag.isEmpty() && flag_existingChar == true)
            {
                //если с базы имя злодея считалось, тогда
                //в чип ставим имя
                chip_villian.setText(t_mainAntag);
                //открываем для видимости чип
                chip_villian.setVisibility(View.VISIBLE);
                //прячем кнопку подробностей о героях
               villPodrobnosti.setVisibility(View.GONE);
                chip_villian.setCloseIconVisible(false);
                chip_villian.setClickable(false);

                flag_existingChar = false;
            }
            else
                chip_villian.setVisibility(View.GONE);


            t_whatHappensIf = resultsForRead.getWhatHappensIf();
            t_mainQuestionGlobal = resultsForRead.getMainQuestionGlobal();
            t_mainQuestionPersonal = resultsForRead.getMainQuestionPersonal();
            t_mainQuestionPrivate = resultsForRead.getMainQuestionPrivate();
            t_mainTheme = resultsForRead.getMainTheme();
            t_mainContreTheme = resultsForRead.getMainContreTheme();
            t_mainSyntez = resultsForRead.getMainSyntez();

            //заполняем текстовые поля фрагмента
            mainInfo.setText(t_maininfo);
           // mainHero.setText(t_mainHero);
            mainGoal.setText(t_mainGoal);
            //mainAntag.setText(t_mainAntag);
            whatHappensIf.setText(t_whatHappensIf);
            mainQuestionGlobal.setText(t_mainQuestionGlobal);
            mainQuestionPersonal.setText(t_mainQuestionPersonal);
            mainQuestionPrivate.setText(t_mainQuestionPrivate);
            mainTheme.setText(t_mainTheme);
            mainContreTheme.setText(t_mainContreTheme);
            mainSyntez.setText(t_mainSyntez);

            //перед отключением edittext от изменений, получаем их слушатели
            listener_mainInfo = mainInfo.getKeyListener();
            listener_mainHero = mainHero.getKeyListener();
            listener_mainGoal = mainGoal.getKeyListener();
            listener_mainAntag = mainAntag.getKeyListener();
            listener_whatHappensIf = whatHappensIf.getKeyListener();
            listener_mainQuestionGlobal = mainQuestionGlobal.getKeyListener();
            listener_mainQuestionPersonal = mainQuestionPersonal.getKeyListener();
            listener_mainQuestionPrivate = mainQuestionPrivate.getKeyListener();
            listener_mainTheme = mainTheme.getKeyListener();
            listener_mainContreTheme = mainContreTheme.getKeyListener();
            listener_mainSyntez = mainSyntez.getKeyListener();

            //блокируем edittext от изменений
            mainInfo.setKeyListener(null);
            mainHero.setKeyListener(null);
            mainGoal.setKeyListener(null);
            mainAntag.setKeyListener(null);
            whatHappensIf.setKeyListener(null);
            mainQuestionGlobal.setKeyListener(null);
            mainQuestionPersonal.setKeyListener(null);
            mainQuestionPrivate.setKeyListener(null);
            mainTheme.setKeyListener(null);
            mainContreTheme.setKeyListener(null);
            mainSyntez.setKeyListener(null);

            //заполнение полей в "Подробностях"
            dateChanging.setText(t_dateChanging + " " + t_timeChanging);
            dateCreating.setText(t_dateCreating + " " + t_timeCreating);
            numberWords.setText("0");
            numberCharacters.setText("0");


            //Обработка нажатий на "Еще" о проекте
            podrobnosi.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(podrobnosi.getText().toString().equals(ADDITION_INFO)) {
                        podrobnosi.setText(MINIMIZE);
                    }
                    else{
                        podrobnosi.setText(ADDITION_INFO);
                    }

                    expandable_podrobnosti.setVisibility(expandable_podrobnosti.isShown()
                    ? View.GONE
                            : View.VISIBLE);
                }
            });

            // Обработка нажатий на чипе героя
            chip_hero.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Toast.makeText(getContext(),"CHIP", Toast.LENGTH_LONG).show();
                }
            });

            // Обработка нажатий на чипе злодея
            chip_villian.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Toast.makeText(getContext(),"CHIP", Toast.LENGTH_LONG).show();
                }
            });

            //Нажатие на "ЕЩЕ", героя
            heroPodrobnosti.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(heroPodrobnosti.getText().toString().equals(ADDITION_INFO)){
                        heroPodrobnosti.setText(MINIMIZE);
                    }
                    else{
                        //установить текст как "Tot"
                        heroPodrobnosti.setText(ADDITION_INFO);
                    }

                    expandable_layout_chars.setVisibility(expandable_layout_chars.isShown()
                            ? View.GONE
                            : View.VISIBLE);


                }
            });

            //Нажатие на "ЕЩЕ", злоедя
            villPodrobnosti.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(villPodrobnosti.getText().toString().equals(ADDITION_INFO)){
                        villPodrobnosti.setText(MINIMIZE);
                    }
                    else{
                        //установить текст как "Tot"
                        villPodrobnosti.setText(ADDITION_INFO);
                    }

                    expandable_layout_vills.setVisibility(expandable_layout_vills.isShown()
                            ? View.GONE
                            : View.VISIBLE);


                }
            });





            // Обработка закрытия CHIP героя
            chip_hero.setOnCloseIconClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    chip_hero.setVisibility(View.GONE);

                    mRealmProjects.beginTransaction();
                    chip_hero.setText("");
                    mRealmProjects.copyToRealmOrUpdate(resultsForRead);
                    mRealmProjects.commitTransaction();

                    heroPodrobnosti.setVisibility(View.VISIBLE);
                }
            });

            // Обработка закрытия CHIP злодея
            chip_villian.setOnCloseIconClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    chip_villian.setVisibility(View.GONE);
                    chip_villian.setText("");
                    mRealmProjects.beginTransaction();
                    resultsForRead.setMainAntag("");
                    mRealmProjects.copyToRealmOrUpdate(resultsForRead);
                    mRealmProjects.commitTransaction();

                    villPodrobnosti.setVisibility(View.VISIBLE);
                }
            });



            // Обработка нажатий на списке имён героев
            charsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    //вытаскиваем имя
                    nameFromHeroNameList = (String) charsList.getItemAtPosition(position);

                    chip_hero.setVisibility(View.VISIBLE);
                    chip_hero.setText(nameFromHeroNameList);

                    //"Свернуть" у персонажей меняем на "Ещё", сворачиваем. Так же сворачиваем весь лэйаут с персонажами
                    heroPodrobnosti.setVisibility(View.GONE);
                    expandable_layout_chars.setVisibility(expandable_layout_chars.isShown()
                            ? View.GONE
                            : View.VISIBLE);
                    heroPodrobnosti.setText(ADDITION_INFO);
                }
            });

            // Обработка нажатий на списке имён злодеев
            vilList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    //вытаскиваем имя
                    nameFromVillaianNameList = (String) vilList.getItemAtPosition(position);

                    chip_villian.setVisibility(View.VISIBLE);
                    chip_villian.setText(nameFromVillaianNameList);

                    //"Свернуть" у злодеев меняем на "Ещё", сворачиваем. Так же сворачиваем весь лэйаут со злодеями
                    villPodrobnosti.setVisibility(View.GONE);
                    expandable_layout_vills.setVisibility(expandable_layout_vills.isShown()
                            ? View.GONE
                            : View.VISIBLE);
                    villPodrobnosti.setText(ADDITION_INFO);
                }
            });


            //FAB - обработка нажатий кнопки редактирования
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                   /*
                    //ищем проект по имени
                    Projects resultsForRead = mRealmProjects.where(Projects.class).equalTo("projectName", t_prName).findFirst();
                    resultFindProject = resultsForRead;

                    //получаем список персонажей текущего проекта
                    RealmList<Characters> tmp1 = resultFindProject.getCharacters();
                    */
                    /*
                    //переносим персонажей в список для адаптера листа
                    if (tmp1 != null) {
                        for (int i = 0; i < tmp1.size(); i++) {
                            temp_chars.add(tmp1.get(i).getName());

                        }
                    }*/

                    /***adapter_hero.notifyDataSetChanged();***/

                    //прячем плавающую кнопку
                    fab.hide();

                    //ставим "крестик" в чип
                    chip_hero.setCloseIconVisible(true);
                    chip_villian.setCloseIconVisible(true);

                    //делаем видимым кнопку подробностей у персонажа, если чип не виден
                    if (chip_hero.getVisibility() == View.GONE)
                        heroPodrobnosti.setVisibility(View.VISIBLE);

                    //делаем видимым кнопку подробностей у злодея, если чип не виден
                    if (chip_villian.getVisibility() == View.GONE)
                        villPodrobnosti.setVisibility(View.VISIBLE);

                    //вызываем метод коллбэка, туда передаем флаг (=true)
                    callBackGetFlagEdit.onCallBack(true);

                    //установка слушателя = "открытие" поля для записи
                    mainInfo.setKeyListener(listener_mainInfo);
                    mainHero.setKeyListener(listener_mainHero);
                    mainGoal.setKeyListener(listener_mainGoal);
                    mainAntag.setKeyListener(listener_mainAntag);
                    whatHappensIf.setKeyListener(listener_whatHappensIf);
                    mainQuestionGlobal.setKeyListener(listener_mainQuestionGlobal);
                    mainQuestionPersonal.setKeyListener(listener_mainQuestionPersonal);
                    mainQuestionPrivate.setKeyListener(listener_mainQuestionPrivate);
                    mainTheme.setKeyListener(listener_mainTheme);
                    mainContreTheme.setKeyListener(listener_mainContreTheme);
                    mainSyntez.setKeyListener(listener_mainSyntez);

                    //ставим курсор на последнюю букву
                    mainInfo.setSelection(mainInfo.getText().length());

                    //меняем флаг = кнопка редактирования была нажата
                    flag_edit = true;

                    //меняем цвет поля
                    mainInfo.setBackgroundResource(R.color.editable_fields);
                    mainHero.setBackgroundResource(R.color.editable_fields);
                    mainGoal.setBackgroundResource(R.color.editable_fields);
                    mainAntag.setBackgroundResource(R.color.editable_fields);
                    whatHappensIf.setBackgroundResource(R.color.editable_fields);
                    mainQuestionGlobal.setBackgroundResource(R.color.editable_fields);
                    mainQuestionPersonal.setBackgroundResource(R.color.editable_fields);
                    mainQuestionPrivate.setBackgroundResource(R.color.editable_fields);
                    mainTheme.setBackgroundResource(R.color.editable_fields);
                    mainContreTheme.setBackgroundResource(R.color.editable_fields);
                    mainSyntez.setBackgroundResource(R.color.editable_fields);

                    //Блокирование вкладок
                    ((projectsActivity) getActivity()).controlTabs(false);

                }
            });
        }
    }


    public  void setFlag_edit() {
        flag_edit = false;
    }


  //метод, вызываемый при нажатии на "дискету"
    public void saveStatement (){
        //нажали на "дискету"
        //отображаем плавающую кнопку
        fab.show();

        //скидываем флаг
        flag_edit = false;

        //вызываем метод коллбэка, туда передаем флаг (=false)
        callBackGetFlagEdit.onCallBack(false);

        //у чипа убираем "крестик"
        chip_hero.setCloseIconVisible(false);
        chip_villian.setCloseIconVisible(false);
        expandable_layout_vills.setVisibility(View.GONE);
        expandable_layout_chars.setVisibility(View.GONE);

        //меняем цвет поля
        mainInfo.setBackgroundColor(getResources().getColor(R.color.mainColorField));
        mainHero.setBackgroundColor(getResources().getColor(R.color.mainColorField));
        mainGoal.setBackgroundColor(getResources().getColor(R.color.mainColorField));
        mainAntag.setBackgroundColor(getResources().getColor(R.color.mainColorField));
        whatHappensIf.setBackgroundColor(getResources().getColor(R.color.mainColorField));
        mainQuestionGlobal.setBackgroundColor(getResources().getColor(R.color.mainColorField));
        mainQuestionPersonal.setBackgroundColor(getResources().getColor(R.color.mainColorField));
        mainQuestionPrivate.setBackgroundColor(getResources().getColor(R.color.mainColorField));
        mainTheme.setBackgroundColor(getResources().getColor(R.color.mainColorField));
        mainContreTheme.setBackgroundColor(getResources().getColor(R.color.mainColorField));
        mainSyntez.setBackgroundColor(getResources().getColor(R.color.mainColorField));

        //блокируем доступность поля
        mainHero.setKeyListener(null);
        mainInfo.setKeyListener(null);
        mainGoal.setKeyListener(null);
        mainAntag.setKeyListener(null);
        whatHappensIf.setKeyListener(null);
        mainQuestionPrivate.setKeyListener(null);
        mainQuestionPersonal.setKeyListener(null);
        mainQuestionGlobal.setKeyListener(null);
        mainTheme.setKeyListener(null);
        mainContreTheme.setKeyListener(null);
        mainSyntez.setKeyListener(null);

        //разблокируем свайп и нажатие на табы с вкладками
        ((projectsActivity) getActivity()).controlTabs(true);

        //получаем имя героя и злодея из чипа
        nameHeroFromChip = chip_hero.getText().toString().trim();
        nameVillianFromChip = chip_villian.getText().toString().trim();

        //НИЖЕ СОХРАНЕНИЕ В БАЗУ
        Projects resultsForSaveProjects = mRealmProjects.where(Projects.class).equalTo("projectName", t_prName).findFirst(); //ищем первоначальное имя в базе

        SimpleDateFormat simpleDateFormatMain = new SimpleDateFormat("dd.MM.yyyy");
        SimpleDateFormat simpleDateFormatTime = new SimpleDateFormat("kk:mm", Locale.UK);

        //НАЧАЛО ТРАНЗАКЦИИ
        mRealmProjects.beginTransaction();

        //Общая информация
        resultsForSaveProjects.setMainInfo(mainInfo.getText().toString());

        //Даты изменения
        resultsForSaveProjects.setDataChanging(simpleDateFormatMain.format(new Date()));
        resultsForSaveProjects.setTimeChanging(simpleDateFormatTime.format(new Date()));

        //Главный герой
        //прячем кнопку подробностей в персонажах
        heroPodrobnosti.setVisibility(View.GONE);

        //сохраняем имя в проекте в объекте класса Project
        resultsForSaveProjects.setMainHero(nameHeroFromChip);

        //Главная цель
        resultsForSaveProjects.setMainGoal(mainGoal.getText().toString());

        //злодей
        resultsForSaveProjects.setMainAntag(nameVillianFromChip);

        //прячем кнопку подробностей в злодеях
        villPodrobnosti.setVisibility(View.GONE);
        villPodrobnosti.setText(ADDITION_INFO);
        heroPodrobnosti.setText(ADDITION_INFO);

        resultsForSaveProjects.setWhatHappensIf(whatHappensIf.getText().toString());
        resultsForSaveProjects.setMainQuestionGlobal(mainQuestionGlobal.getText().toString());
        resultsForSaveProjects.setMainQuestionPersonal(mainQuestionPersonal.getText().toString());
        resultsForSaveProjects.setMainQuestionPrivate(mainQuestionPrivate.getText().toString());
        resultsForSaveProjects.setMainTheme(mainTheme.getText().toString());
        resultsForSaveProjects.setMainContreTheme(mainContreTheme.getText().toString());
        resultsForSaveProjects.setMainSyntez(mainSyntez.getText().toString());

        //обновляем дату изменения в "подробностях"
        dateChanging.setText(resultsForSaveProjects.getDataChanging() + " " +resultsForSaveProjects.getTimeChanging());

        mRealmProjects.copyToRealmOrUpdate(resultsForSaveProjects);

        mRealmProjects.commitTransaction();

    }


    private boolean dialogAddNewPers(final int flag_vill_hero){
       /* LayoutInflater li = LayoutInflater.from(getActivity());
        View layout = li.inflate(R.layout.add_pers_mfrag,null);
        AlertDialog.Builder mDialogBuilder = new AlertDialog.Builder(getActivity());
        mDialogBuilder.setView(layout);

        final EditText putNewName = layout.findViewById(R.id.et_putNewPersName);

        mDialogBuilder
                .setCancelable(false)
                .setPositiveButton("Save",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                switch (flag_vill_hero){
                                    case 1:
                                        chip_hero.setVisibility(View.VISIBLE);
                                        chip_hero.setText(putNewName.getText().toString());
                                        heroPodrobnosti.setVisibility(View.GONE);
                                        break;
                                    case 2:
                                        chip_villian.setVisibility(View.VISIBLE);
                                        chip_villian.setText(putNewName.getText().toString());
                                        villPodrobnosti.setVisibility(View.GONE);
                                        break;
                                    default: break;

                                }



                            }
                        })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {


                                //Toast.makeText(getContext(),"cancel",Toast.LENGTH_LONG).show();
                            }
                        });

        AlertDialog alertDialog = mDialogBuilder.create();
        alertDialog.show();
*/
        return true;

    }

    public void refreshList(String action, String newChar) {



        Projects resultsFindProject = mRealmProjects.where(Projects.class).equalTo("projectName", t_prName).findFirst();
        RealmList<Characters> tmp1 = resultsFindProject.getCharacters();

        adapter_hero.clear();


        if (action.equals("add")) {

            Log.i("LOG", "==> mainFragment. From base to list Refresh");
            if (tmp1 != null) {
                for (int i = 0; i < tmp1.size(); i++) {
                    Log.i("LOG", i + ", "+ tmp1.get(i).getName());
                    temp_chars.add(tmp1.get(i).getName());

                }

            }
        }
        else if(action.equals("delete"))
        {
            /*
            если имя удаленного персонажа совпадает с тем, что записан в чипах - удаляем из чипа
             */
            if (chip_hero.getText().toString().equals(newChar)){
                chip_hero.setText("");
                chip_hero.setVisibility(View.GONE);
            }
            if(chip_villian.getText().toString().equals(newChar)){
                chip_villian.setText("");
                chip_villian.setVisibility(View.GONE);
            }
        }

        adapter_hero.notifyDataSetChanged();
        adapter_villians.notifyDataSetChanged();




    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
       // mRealmProjects.close();

    }


    }



