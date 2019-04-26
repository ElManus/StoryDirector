package com.apps.gragas.storydirector;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import es.dmoral.toasty.Toasty;
import io.realm.Realm;
import io.realm.RealmList;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.KeyListener;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.apps.gragas.storydirector.Implements.Characters;
import com.apps.gragas.storydirector.Implements.Projects;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class CharActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private Intent intent;
    private Realm mRealmProjects; //база данных
    private String name_project;
    private String name_char;

    private int position; //позиция для изменяемого персонажа в realmList

    private Spinner sexSpinner;
    private Spinner roleSpinner;
    private String roleCharacter;
    private String  sideCharacter;
    private Spinner sideSpinner;

    private boolean add_notchange;
    private String sexHero;

    private FloatingActionButton fab; //плавающая кнопка

    private Projects resultsForRead;
    private RealmList<Characters> tmp1;

    private boolean flag_edit;

    private EditText et_heroNameCharAct, et_heroOld, et_heroPhysics, et_heroLook, et_heroCreed,
            et_heroLookHimself, et_leisureSelf, et_hobbie, et_sports, et_parents_0_10,
            et_persona_0_10, et_school_friends_0_10, et_interests_10_18, et_school_10_18,
            et_relationship_10_18, et_parents_elders_10_18, et_job_10_18, et_events_10_18,
            et_interests_18_25, et_relationship_18_25, et_studing_18_25, et_parents_18_25,
            et_events_18_25, et_job_colleges, et_job_boss, et_job_what, et_job_pleasure,
    et_family, et_startRelationship, et_nowRelationship, et_leisure, et_adulter, et_conflicts, et_role;

    private KeyListener listener_heroNameCharAct, listener_heroOld, listener_HeroPhysics, listener_heroLook, listener_heroCreed,
            listener_heroLookHimself, listener_leisureSelf, listener_hobbie, listener_sports, listener_parents_0_10,
            listener_persona_0_10, listener_school_friends_0_10, listener_interests_10_18, listener_school_10_18,
            listener_relationship_10_18, listener_parents_elders_10_18, listener_job_10_18, listener_events_10_18,
            listener_interests_18_25, listener_relationship_18_25, listener_studing_18_25, listener_parents_18_25,
            listener_events_18_25, listener_job_colleges, listener_job_boss, listener_job_what, listener_job_pleasure,
            listener_family, listener_startRelationship, listener_nowRelationship, listener_leisure, listener_adulter, listener_conflicts, listener_role;

    private int heroOld;
    private String heroPhysics, heroLook, heroCreed, heroLookHimself, leisureSelf, hobbie, sports,
    parents_0_10, persona_0_10, school_friends_0_10, interests_10_18, school_10_18,
    relationship_10_18, parents_elders_10_18, job_10_18, events_10_18,
    interests_18_25, relationship_18_25, studing_18_25, parents_18_25,
    events_18_25, job_colleges, job_boss, job_what, job_pleasure,
    family, startRelationship, nowRelationship, leisure, adulter, conflicts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_char);


        flag_edit = false; // флаг нажатия кнопки редактирования

        //определение плавающей кнопки
        fab = findViewById(R.id.fab_characters_addCharAct1);


        /*
        подключение полей
         */
        et_heroNameCharAct = findViewById(R.id.et_heroNameCharAct);
        et_heroOld = findViewById(R.id.et_heroOld);
        et_heroPhysics = findViewById(R.id.et_heroPhysics);
        et_heroLook = findViewById(R.id.et_heroLook);
        et_heroCreed = findViewById(R.id.et_heroCreed);
        et_heroLookHimself = findViewById(R.id.et_heroLookHimself);
        et_leisureSelf = findViewById(R.id.et_leisureSelf);
        et_hobbie = findViewById(R.id.et_hobbie);
        et_sports = findViewById(R.id.et_sports);
        et_parents_0_10 = findViewById(R.id.et_parents_0_10);
        et_persona_0_10 = findViewById(R.id.et_persona_0_10);
        et_school_friends_0_10 = findViewById(R.id.et_school_friends_0_10);
        et_interests_10_18 = findViewById(R.id.et_interests_10_18);
        et_school_10_18 = findViewById(R.id.et_school_10_18);
        et_relationship_10_18 = findViewById(R.id.et_relationship_10_18);
        et_parents_elders_10_18 = findViewById(R.id.et_parents_elders_10_18);
        et_job_10_18 = findViewById(R.id.et_job_10_18);
        et_events_10_18 = findViewById(R.id.et_events_10_18);
        et_interests_18_25 = findViewById(R.id.et_interests_18_25);
        et_relationship_18_25 = findViewById(R.id.et_relationship_18_25);
        et_studing_18_25 = findViewById(R.id.et_studing_18_25);
        et_events_18_25 = findViewById(R.id.et_events_18_25);
        et_job_colleges = findViewById(R.id.et_job_colleges);
        et_job_boss = findViewById(R.id.et_job_boss);
        et_job_what = findViewById(R.id.et_job_what);
        et_job_pleasure = findViewById(R.id.et_job_pleasure);
        et_family = findViewById(R.id.et_family);
        et_parents_18_25 = findViewById(R.id.et_parents_18_25);
        et_startRelationship = findViewById(R.id.et_startRelationship);
        et_nowRelationship = findViewById(R.id.et_nowRelationship);
        et_leisure = findViewById(R.id.et_leisure);
        et_adulter = findViewById(R.id.et_adulter);
        et_conflicts = findViewById(R.id.et_conflicts);

        /*
        Получаем слушатели полей
         */
        listener_heroNameCharAct = et_heroNameCharAct.getKeyListener();
        listener_heroOld = et_heroOld.getKeyListener();
        listener_HeroPhysics = et_heroPhysics.getKeyListener();
        listener_heroLook = et_heroLook.getKeyListener();
        listener_heroCreed = et_heroCreed.getKeyListener();
        listener_heroLookHimself = et_heroLookHimself.getKeyListener();
        listener_leisureSelf = et_leisureSelf.getKeyListener();
        listener_hobbie = et_hobbie.getKeyListener();
        listener_sports = et_sports.getKeyListener();
        listener_parents_0_10 = et_parents_0_10.getKeyListener();
        listener_persona_0_10 = et_persona_0_10.getKeyListener();
        listener_school_friends_0_10 = et_school_friends_0_10.getKeyListener();
        listener_interests_10_18 = et_interests_10_18.getKeyListener();
        listener_school_10_18 = et_school_10_18.getKeyListener();
        listener_relationship_10_18 = et_relationship_10_18.getKeyListener();
        listener_parents_elders_10_18 = et_parents_elders_10_18.getKeyListener();
        listener_job_10_18 = et_job_10_18.getKeyListener();
        listener_events_10_18 = et_events_10_18.getKeyListener();
        listener_interests_18_25 = et_interests_18_25.getKeyListener();
        listener_relationship_18_25 = et_relationship_18_25.getKeyListener();
        listener_studing_18_25 = et_studing_18_25.getKeyListener();
        listener_parents_18_25 = et_parents_18_25.getKeyListener();
        listener_events_18_25 = et_events_18_25.getKeyListener();
        listener_job_colleges = et_job_colleges.getKeyListener();
        listener_job_boss = et_job_boss.getKeyListener();
        listener_job_what = et_job_what.getKeyListener();
        listener_job_pleasure = et_job_pleasure.getKeyListener();
        listener_family = et_family.getKeyListener();
        listener_startRelationship = et_startRelationship.getKeyListener();
        listener_nowRelationship = et_nowRelationship.getKeyListener();
        listener_leisure = et_leisure.getKeyListener();
        listener_adulter = et_adulter.getKeyListener();
        listener_conflicts = et_conflicts.getKeyListener();

        /*
        Спиннер выбора пола
         */
        sexSpinner = findViewById(R.id.sex_spinner);
        ArrayAdapter<CharSequence> sexSpinnerAdapter = ArrayAdapter.createFromResource(this,
                R.array.sex_array, android.R.layout.simple_spinner_item);
        sexSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sexSpinner.setAdapter(sexSpinnerAdapter);
        sexSpinner.setSelection(2);
        sexSpinner.setOnItemSelectedListener(this);

        /*
        Спиннер выбора роли
         */
        roleSpinner = findViewById(R.id.role_spinner);
        ArrayAdapter <CharSequence> roleAdapter = ArrayAdapter.createFromResource(this,
                R.array.role_array, android.R.layout.simple_spinner_item);
        roleAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        roleSpinner.setAdapter(roleAdapter);
        roleSpinner.setSelection(0);
        roleSpinner.setOnItemSelectedListener(this);

         /*
        Спиннер выбора стороны
         */
        sideSpinner = findViewById(R.id.side_spinner);
        ArrayAdapter <CharSequence> sideAdapter = ArrayAdapter.createFromResource(this,
                R.array.side_array, android.R.layout.simple_spinner_item);
        sideAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sideSpinner.setAdapter(sideAdapter);
        sideSpinner.setSelection(2);
        sideSpinner.setOnItemSelectedListener(this);

        sexHero = "Не указано";


        mRealmProjects.init(this);
        mRealmProjects = Realm.getDefaultInstance();

        Intent intent = getIntent();

        /*
        получение информации от активности проектов (фрагмент персонажей)
         */
        name_project = intent.getStringExtra("PROJECT_NAME");
        name_char = intent.getStringExtra("PERS_NAME");
       // Toast.makeText(this,name_char,Toast.LENGTH_SHORT).show();
        add_notchange = intent.getBooleanExtra("ADD_NOTCHANGE", false);


            /*
        Toolbar
         */
        Toolbar toolbar = findViewById(R.id.toolbarCharActivity);
        //toolbar.setNavigationIcon(R.drawable.ic_save_icon);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Story Teller");
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        /*
        подключение к базе данных
         */




        //работа с базой
        resultsForRead = mRealmProjects.where(Projects.class).equalTo("projectName", name_project).findFirst();
        //получаем список персонажей текущего проекта
        tmp1 = resultsForRead.getCharacters();



        /*
        Новый персонаж?
        Нет
         */
        if(add_notchange == false){

            fab.show();

            closeFields();


            //ищем нужного персонажа по имени, получаемого из прошлой активности (фрагмента персонажа)
            Characters temp_charObject = null;

            for (int i=0; i< tmp1.size(); i++)
            {
                String tmp_name = tmp1.get(i).getName();
                if (tmp_name.equals(name_char))
                {
                    //получаем объект персонажа
                    position = i;
                    temp_charObject = tmp1.get(i);
                }
            }



            //Заполнение поля с полом персонажа
            String tmp_string = temp_charObject.getCharacterSex();
            switch (tmp_string) {
                case "Мужской":
                    sexSpinner.setSelection(0);
                    break;
                case "Женский":
                    sexSpinner.setSelection(1);
                    break;
                default:
                    sexSpinner.setSelection(2);
                    break;
            }
                //Заполнение поля с ролью персонажа
                String tmp_string2 = temp_charObject.getRole();
            if (tmp_string2 != null && !tmp_string2.equals("")) {
                switch (tmp_string2) {
                    case "Главный герой":
                        roleSpinner.setSelection(0);
                        break;
                    case "Злодей":
                        roleSpinner.setSelection(1);
                        break;
                    case "Защитник":
                        roleSpinner.setSelection(2);
                        break;
                    case "Отражатель":
                        roleSpinner.setSelection(3);
                        break;
                    case "Верующий":
                        roleSpinner.setSelection(4);
                        break;
                    case "Сомневающийся":
                        roleSpinner.setSelection(5);
                        break;
                    case "Чувствующий":
                        roleSpinner.setSelection(6);
                    default:
                        roleSpinner.setSelection(0);
                        break;
                }
            }
            else {
                roleSpinner.setSelection(0);
            }

            //Заполнение поля со стороной персонажа
            String themeOrNot = temp_charObject.getThemeOrNot();
            //if (themeOrNot != null && !themeOrNot.equals(""))
            switch (themeOrNot) {
                case "Тема":
                    sideSpinner.setSelection(0);
                    break;
                case "Контртема":
                    sideSpinner.setSelection(1);
                    break;
                default:
                    sideSpinner.setSelection(2);
                    break;
            }
          //  else sideSpinner.setSelection(2);

            /*
            ЗАПОЛНЕНИЕ ПОЛЕЙ ЗАГРУЖЕННЫМИ ДАННЫМИ
             */
            et_heroNameCharAct.setText(name_char); //брали из интента
            et_heroOld.setText(Integer.toString(temp_charObject.getHeroOld()));
            et_heroPhysics.setText(temp_charObject.getHeroPhysics());
            et_heroLook.setText(temp_charObject.getHeroLook());
            et_heroCreed.setText(temp_charObject.getHeroCreed());
            et_heroLookHimself.setText(temp_charObject.getHeroLookHimself());
            et_leisureSelf.setText(temp_charObject.getLeisureSelf());
            et_hobbie.setText(temp_charObject.getHobbie());
            et_sports.setText(temp_charObject.getSports());
            et_parents_0_10.setText(temp_charObject.getParents_0_10());
            et_persona_0_10.setText(temp_charObject.getPersona_0_10());
            et_school_friends_0_10.setText(temp_charObject.getSchool_friends_0_10());
            et_interests_10_18.setText(temp_charObject.getInterests_10_18());
            et_school_10_18.setText(temp_charObject.getSchool_10_18());
            et_relationship_10_18.setText(temp_charObject.getRelationship_10_18());
            et_parents_elders_10_18.setText(temp_charObject.getParents_elders_10_18());
            et_job_10_18.setText(temp_charObject.getJob_10_18());
            et_events_10_18.setText(temp_charObject.getEvents_10_18());
            et_interests_18_25.setText(temp_charObject.getInterests_18_25());
            et_relationship_18_25.setText(temp_charObject.getRelationship_18_25());
            et_studing_18_25.setText(temp_charObject.getStuding_18_25());
            et_events_18_25.setText(temp_charObject.getEvents_18_25());
            et_job_colleges.setText(temp_charObject.getJob_colleges());
            et_job_boss.setText(temp_charObject.getJob_boss());
            et_job_what.setText(temp_charObject.getJob_what());
            et_job_pleasure.setText(temp_charObject.getJob_pleasure());
            et_family.setText(temp_charObject.getFamily());
            et_parents_18_25.setText(temp_charObject.getParents_18_25());
            et_startRelationship.setText(temp_charObject.getStartRelationship());
            et_nowRelationship.setText(temp_charObject.getNowRelationship());
            et_leisure.setText(temp_charObject.getLeisure());
            et_adulter.setText(temp_charObject.getAdulter());
            et_conflicts.setText(temp_charObject.getConflicts());




        }
        /*
        Да, новый
         */
        else {
            fab.hide();
            flag_edit = true;

            /*
            ОТКРЫВАЕМ ПОЛЯ, МЕНЯЕМ ЦВЕТ
             */

            openFields();
        }



    //mRealmProjects.close();

        /*
        FAB ADD CHARACTER
         */
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(flag_edit == false){
                    flag_edit = true;

                }

                fab.hide();

                openFields();

            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu (Menu menu){
        getMenuInflater().inflate(R.menu.toolbar_menu_projects, menu);
        return true;
    }

    /*
    spinner adapter
     */
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Spinner sexSpinner = (Spinner) parent;
        Spinner roleSpinner = (Spinner) parent;
        Spinner sideSpinner = (Spinner) parent;

        if(sexSpinner.getId() == R.id.sex_spinner){
            String item = parent.getItemAtPosition(position).toString();
            sexHero = item;
        }

        if (roleSpinner.getId() == R.id.role_spinner) {
            String item = parent.getItemAtPosition(position).toString();
            roleCharacter=item;
        }

        if (sideSpinner.getId() == R.id.side_spinner){
            String item = parent.getItemAtPosition(position).toString();
            sideCharacter = item;
        }
    }

    /*
       spinner adapter
        */
    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    //обработка нажатий на иконке информации в тулбаре
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.icon_share_fragment_toolbar:
            {
                //Toast.makeText(this,"Share!", Toast.LENGTH_SHORT).show();
                Toasty.info(getApplicationContext(),"Share!",Toast.LENGTH_SHORT,true).show();
                return true;
            }
            case R.id.icon_toolbarSaveProject : {

                if (flag_edit == true) {
                    saveStatement();
                  //  Toast.makeText(this,"Saving...", Toast.LENGTH_SHORT).show();
                }
                return true;
            }

        }
        return super.onOptionsItemSelected(item);
    }

    private void saveStatement(){
         /*
        если получили значение флага add_notchange истинно, тогда все поля должны быть открыты, иначе - закрыты для изменений
        */

         /*
         это новый персонаж?
         Да
          */
        if (add_notchange == true){

            /*
        подключение к базе данных
         */
            //нужно проверить, есть ли уже персонаж с таким именем?

            final Characters newCharacter = new Characters();

            /*
                  Ищем персонажа с введенным именем в базе
             */
            Characters temp_charObject = null; //ищем в базе персонажа с таким же именем.
            for (int i=0; i< tmp1.size(); i++)
            {
                String tmp_name = tmp1.get(i).getName();
                if (tmp_name.equals(et_heroNameCharAct.getText().toString().trim()))
                {
                    //получаем объект персонажа
                    temp_charObject = tmp1.get(i);
                }
            }

            /*
            если с таким именем уже есть, тогда выдаем сообщение и переходим к концу функции
            Если нет - сохраняем.
             */
            if (temp_charObject != null)
            {
                Toasty.error(getApplicationContext(),"Персонаж с таким именем уже есть! Данные не сохранены",Toast.LENGTH_SHORT,true).show();
            et_heroNameCharAct.setText("");
            }

            else
                //имя не должно быть пустое
                if (!et_heroNameCharAct.getText().toString().isEmpty())
                {
                newCharacter.setName(et_heroNameCharAct.getText().toString().trim());
                newCharacter.setCharacterSex(sexHero);
                newCharacter.setRole(roleCharacter);
                newCharacter.setThemeOrNot(sideCharacter);
                newCharacter.setHeroPhysics(et_heroPhysics.getText().toString().trim());

                try {
                    newCharacter.setHeroOld(Integer.parseInt(et_heroOld.getText().toString().trim()));
                }
                catch (Exception e){
                    newCharacter.setHeroOld(0);
                }

                setDataToObject(newCharacter);
                mRealmProjects.beginTransaction();
                tmp1.add(newCharacter);
                resultsForRead.setCharacters(tmp1);
                mRealmProjects.commitTransaction();

                Toasty.info(getApplicationContext(),"Сохранено",Toast.LENGTH_SHORT,true).show();
            }
            else {
                    Toasty.error(getApplicationContext(),"Пустое имя не допустимо!",Toast.LENGTH_SHORT,true).show();
                }
        }
        //это новый персонаж?
        //Нет
        else {
            fab.show();
            /*
            поиск по имени
             */
            //ищем нужного персонажа по имени, получаемого из прошлой активности (фрагмента персонажа)
            Characters temp_charObject = null;
            for (int i=0; i< tmp1.size(); i++)
            {
                String tmp_name = tmp1.get(i).getName();
                if (tmp_name.equals(name_char))
                {
                    //получаем объект персонажа
                    temp_charObject = tmp1.get(i);
                }
            }
            /*
            заполнение данных
             */
            /*
            Имя не меняется?
            Не меняется.
             */
            if (temp_charObject.getName().equals(et_heroNameCharAct.getText().toString().trim())) {

                mRealmProjects.beginTransaction();
                setDataToObject(temp_charObject);
                Toasty.info(getApplicationContext(),"Сохранено",Toast.LENGTH_SHORT,true).show();
                tmp1.set(position, temp_charObject);
                mRealmProjects.commitTransaction();
                // mRealmProjects.close();
            }
            /*
            Имя сменилось.
             */
            else {
                //Toast.makeText(this, "New Name!!!", Toast.LENGTH_SHORT).show();
                Characters temp1_charObject = null;
                for (int i=0; i< tmp1.size(); i++)
                {
                    String tmp_name = tmp1.get(i).getName();
                    if (tmp_name.equals(et_heroNameCharAct.getText().toString().trim()))
                    {
                        //получаем объект персонажа
                        temp1_charObject = tmp1.get(i);
                    }
                }
                /*
                пользователь с таким именем уже есть? тогда - выход из функции
                нет? - сохраняем
                 */
                if (temp1_charObject != null)
                {
                    Toasty.error(getApplicationContext(),"Персонаж с таким именем уже есть! Данные не сохранены",Toast.LENGTH_SHORT,true).show();
                    et_heroNameCharAct.setText(name_char);
                }
                else
                    if (! et_heroNameCharAct.getText().toString().isEmpty())
                    {
                    //Создаем нового персонажа, "перекачиваем" данные из старого объекта в новый, старый удаляем.
                    final Characters newCharacterNewOld = new Characters();
                    newCharacterNewOld.setName(et_heroNameCharAct.getText().toString().trim());
                    newCharacterNewOld.setCharacterSex(sexHero);
                    newCharacterNewOld.setRole(roleCharacter);
                    newCharacterNewOld.setThemeOrNot(sideCharacter);

                    try {
                        newCharacterNewOld.setHeroOld(Integer.parseInt(et_heroOld.getText().toString().trim()));
                    }
                    catch (Exception e){
                        newCharacterNewOld.setHeroOld(0);
                    }
                /*
                Перенос информации из старого персонажа в новый
                 */
                setDataToObject(newCharacterNewOld);
                /*
                Сохранение нового "старого" персонажа в базу + удаление "старого"
                 */
                    mRealmProjects.beginTransaction();
                             /*
                            Меняем имя в проекте в поле mainHero
                             */
                    resultsForRead.setMainHero(newCharacterNewOld.getName());
                    tmp1.add(newCharacterNewOld);
                    tmp1.deleteFromRealm(position);
                    resultsForRead.setCharacters(tmp1);
                    mRealmProjects.commitTransaction();

                    Toasty.info(getApplicationContext(),"Сохранено",Toast.LENGTH_SHORT,true).show();
                }
                else
                        Toasty.error(getApplicationContext(),"Пустое имя не допустимо!",Toast.LENGTH_SHORT,true).show();
            }
        }

        flag_edit =false;
        fab.show();
        closeFields();
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        mRealmProjects.close();
    }

    private void openFields(){
        //установка слушателя = "открытие" поля для записи
        et_heroNameCharAct.setKeyListener(listener_heroNameCharAct);
        et_heroOld.setKeyListener(listener_heroOld);
        et_heroPhysics.setKeyListener(listener_HeroPhysics);
        et_heroLook.setKeyListener(listener_heroLook);
        et_heroCreed.setKeyListener(listener_heroCreed);
        et_heroLookHimself.setKeyListener(listener_heroLookHimself);
        et_leisureSelf.setKeyListener(listener_leisureSelf);
        et_hobbie.setKeyListener(listener_hobbie);
        et_sports.setKeyListener(listener_sports);
        et_parents_0_10.setKeyListener(listener_parents_0_10);
        et_persona_0_10.setKeyListener(listener_persona_0_10);
        et_school_friends_0_10.setKeyListener(listener_school_friends_0_10);
        et_interests_10_18.setKeyListener(listener_interests_10_18);
        et_school_10_18.setKeyListener(listener_school_10_18);
        et_relationship_10_18.setKeyListener(listener_relationship_10_18);
        et_parents_elders_10_18.setKeyListener(listener_parents_elders_10_18);
        et_job_10_18.setKeyListener(listener_job_10_18);
        et_events_10_18.setKeyListener(listener_events_10_18);
        et_interests_18_25.setKeyListener(listener_interests_18_25);
        et_relationship_18_25.setKeyListener(listener_relationship_18_25);
        et_studing_18_25.setKeyListener(listener_studing_18_25);
        et_events_18_25.setKeyListener(listener_events_18_25);
        et_job_colleges.setKeyListener(listener_job_colleges);
        et_parents_18_25.setKeyListener(listener_parents_18_25);
        et_job_boss.setKeyListener(listener_job_boss);
        et_job_what.setKeyListener(listener_job_what);
        et_job_pleasure.setKeyListener(listener_job_pleasure);
        et_family.setKeyListener(listener_family);
        et_startRelationship.setKeyListener(listener_startRelationship);
        et_nowRelationship.setKeyListener(listener_nowRelationship);
        et_leisure.setKeyListener(listener_leisure);
        et_adulter.setKeyListener(listener_adulter);
        et_conflicts.setKeyListener(listener_conflicts);
        sexSpinner.setEnabled(true);
        roleSpinner.setEnabled(true);
        sideSpinner.setEnabled(true);

        //меняем цвет поля
        et_heroNameCharAct.setBackgroundResource(R.color.editable_fields);
        et_heroOld.setBackgroundResource(R.color.editable_fields);
        et_heroPhysics.setBackgroundResource(R.color.editable_fields);
        et_heroLook.setBackgroundResource(R.color.editable_fields);
        et_heroCreed.setBackgroundResource(R.color.editable_fields);
        et_heroLookHimself.setBackgroundResource(R.color.editable_fields);
        et_parents_18_25.setBackgroundResource(R.color.editable_fields);
        et_leisureSelf.setBackgroundResource(R.color.editable_fields);
        et_hobbie.setBackgroundResource(R.color.editable_fields);
        et_sports.setBackgroundResource(R.color.editable_fields);
        et_parents_0_10.setBackgroundResource(R.color.editable_fields);
        et_persona_0_10.setBackgroundResource(R.color.editable_fields);
        et_school_friends_0_10.setBackgroundResource(R.color.editable_fields);
        et_interests_10_18.setBackgroundResource(R.color.editable_fields);
        et_school_10_18.setBackgroundResource(R.color.editable_fields);
        et_relationship_10_18.setBackgroundResource(R.color.editable_fields);
        et_parents_elders_10_18.setBackgroundResource(R.color.editable_fields);
        et_job_10_18.setBackgroundResource(R.color.editable_fields);
        et_events_10_18.setBackgroundResource(R.color.editable_fields);
        et_interests_18_25.setBackgroundResource(R.color.editable_fields);
        et_relationship_18_25.setBackgroundResource(R.color.editable_fields);
        et_studing_18_25.setBackgroundResource(R.color.editable_fields);
        et_events_18_25.setBackgroundResource(R.color.editable_fields);
        et_job_colleges.setBackgroundResource(R.color.editable_fields);
        et_job_boss.setBackgroundResource(R.color.editable_fields);
        et_job_what.setBackgroundResource(R.color.editable_fields);
        et_job_pleasure.setBackgroundResource(R.color.editable_fields);
        et_family.setBackgroundResource(R.color.editable_fields);
        et_startRelationship.setBackgroundResource(R.color.editable_fields);
        et_nowRelationship.setBackgroundResource(R.color.editable_fields);
        et_leisure.setBackgroundResource(R.color.editable_fields);
        et_adulter.setBackgroundResource(R.color.editable_fields);
        et_conflicts.setBackgroundResource(R.color.editable_fields);
    }

    private void closeFields(){
        /*
            блокируем поля
             */
        et_heroNameCharAct.setKeyListener(null);
        et_heroOld.setKeyListener(null);
        et_heroPhysics.setKeyListener(null);
        et_heroLook.setKeyListener(null);
        et_heroCreed.setKeyListener(null);
        et_heroLookHimself.setKeyListener(null);
        et_leisureSelf.setKeyListener(null);
        et_hobbie.setKeyListener(null);
        et_sports.setKeyListener(null);
        et_parents_0_10.setKeyListener(null);
        et_persona_0_10.setKeyListener(null);
        et_school_friends_0_10.setKeyListener(null);
        et_interests_10_18.setKeyListener(null);
        et_school_10_18.setKeyListener(null);
        et_relationship_10_18.setKeyListener(null);
        et_parents_elders_10_18.setKeyListener(null);
        et_job_10_18.setKeyListener(null);
        et_events_10_18.setKeyListener(null);
        et_interests_18_25.setKeyListener(null);
        et_relationship_18_25.setKeyListener(null);
        et_studing_18_25.setKeyListener(null);
        et_events_18_25.setKeyListener(null);
        et_job_colleges.setKeyListener(null);
        et_job_boss.setKeyListener(null);
        et_job_what.setKeyListener(null);
        et_job_pleasure.setKeyListener(null);
        et_family.setKeyListener(null);
        et_startRelationship.setKeyListener(null);
        et_nowRelationship.setKeyListener(null);
        et_leisure.setKeyListener(null);
        et_adulter.setKeyListener(null);
        et_conflicts.setKeyListener(null);
        sexSpinner.setEnabled(false);
        roleSpinner.setEnabled(false);
        sideSpinner.setEnabled(false);

        //меняем цвет поля
        et_heroNameCharAct.setBackgroundColor(getResources().getColor(R.color.mainColorField));
        et_heroOld.setBackgroundColor(getResources().getColor(R.color.mainColorField));
        et_heroPhysics.setBackgroundColor(getResources().getColor(R.color.mainColorField));
        et_heroLook.setBackgroundColor(getResources().getColor(R.color.mainColorField));
        et_heroCreed.setBackgroundColor(getResources().getColor(R.color.mainColorField));
        et_heroLookHimself.setBackgroundColor(getResources().getColor(R.color.mainColorField));
        et_leisureSelf.setBackgroundColor(getResources().getColor(R.color.mainColorField));
        et_hobbie.setBackgroundColor(getResources().getColor(R.color.mainColorField));
        et_sports.setBackgroundColor(getResources().getColor(R.color.mainColorField));
        et_parents_0_10.setBackgroundColor(getResources().getColor(R.color.mainColorField));
        et_persona_0_10.setBackgroundColor(getResources().getColor(R.color.mainColorField));
        et_parents_18_25.setBackgroundColor(getResources().getColor(R.color.mainColorField));
        et_school_friends_0_10.setBackgroundColor(getResources().getColor(R.color.mainColorField));
        et_interests_10_18.setBackgroundColor(getResources().getColor(R.color.mainColorField));
        et_school_10_18.setBackgroundColor(getResources().getColor(R.color.mainColorField));
        et_relationship_10_18.setBackgroundColor(getResources().getColor(R.color.mainColorField));
        et_parents_elders_10_18.setBackgroundColor(getResources().getColor(R.color.mainColorField));
        et_job_10_18.setBackgroundColor(getResources().getColor(R.color.mainColorField));
        et_events_10_18.setBackgroundColor(getResources().getColor(R.color.mainColorField));
        et_interests_18_25.setBackgroundColor(getResources().getColor(R.color.mainColorField));
        et_relationship_18_25.setBackgroundColor(getResources().getColor(R.color.mainColorField));
        et_studing_18_25.setBackgroundColor(getResources().getColor(R.color.mainColorField));
        et_events_18_25.setBackgroundColor(getResources().getColor(R.color.mainColorField));
        et_job_colleges.setBackgroundColor(getResources().getColor(R.color.mainColorField));
        et_job_boss.setBackgroundColor(getResources().getColor(R.color.mainColorField));
        et_job_what.setBackgroundColor(getResources().getColor(R.color.mainColorField));
        et_job_pleasure.setBackgroundColor(getResources().getColor(R.color.mainColorField));
        et_family.setBackgroundColor(getResources().getColor(R.color.mainColorField));
        et_startRelationship.setBackgroundColor(getResources().getColor(R.color.mainColorField));
        et_nowRelationship.setBackgroundColor(getResources().getColor(R.color.mainColorField));
        et_leisure.setBackgroundColor(getResources().getColor(R.color.mainColorField));
        et_adulter.setBackgroundColor(getResources().getColor(R.color.mainColorField));
        et_conflicts.setBackgroundColor(getResources().getColor(R.color.mainColorField));
    }


    private void setDataToObject (Characters characters){
        characters.setHeroPhysics(et_heroPhysics.getText().toString().trim());
        characters.setHeroLook(et_heroLook.getText().toString().trim());
        characters.setHeroCreed(et_heroCreed.getText().toString().trim());
        characters.setHeroLookHimself(et_heroLookHimself.getText().toString().trim());
        characters.setLeisureSelf(et_leisureSelf.getText().toString().trim());
        characters.setHobbie(et_hobbie.getText().toString().trim());
        characters.setSports(et_sports.getText().toString().trim());
        characters.setParents_0_10(et_parents_0_10.getText().toString().trim());
        characters.setPersona_0_10(et_persona_0_10.getText().toString().trim());
        characters.setSchool_friends_0_10(et_school_friends_0_10.getText().toString().trim());
        characters.setInterests_10_18(et_interests_10_18.getText().toString().trim());
        characters.setSchool_10_18(et_school_10_18.getText().toString().trim());
        characters.setRelationship_10_18(et_relationship_10_18.getText().toString().trim());
        characters.setParents_elders_10_18(et_parents_elders_10_18.getText().toString().trim());
        characters.setJob_10_18(et_job_10_18.getText().toString().trim());
        characters.setEvents_10_18(et_events_10_18.getText().toString().trim());
        characters.setInterests_18_25(et_interests_18_25.getText().toString().trim());
        characters.setRelationship_18_25(et_relationship_18_25.getText().toString().trim());
        characters.setStuding_18_25(et_studing_18_25.getText().toString().trim());
        characters.setEvents_18_25(et_events_18_25.getText().toString().trim());
        characters.setJob_colleges(et_job_colleges.getText().toString().trim());
        characters.setJob_boss(et_job_boss.getText().toString().trim());
        characters.setJob_what(et_job_what.getText().toString().trim());
        characters.setJob_pleasure(et_job_pleasure.getText().toString().trim());
        characters.setFamily(et_family.getText().toString().trim());
        characters.setParents_18_25(et_parents_18_25.getText().toString().trim());
        characters.setStartRelationship(et_startRelationship.getText().toString().trim());
        characters.setNowRelationship(et_nowRelationship.getText().toString().trim());
        characters.setLeisure(et_leisure.getText().toString().trim());
        characters.setAdulter(et_adulter.getText().toString().trim());
        characters.setConflicts(et_conflicts.getText().toString().trim());
    }
}
