package com.apps.gragas.storydirector;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;

import com.apps.gragas.storydirector.Adapters.projectsAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.Toast;


import com.apps.gragas.storydirector.Parcels.parcel_project;
import com.apps.gragas.storydirector.Implements.Projects;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import es.dmoral.toasty.Toasty;
import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;

public class MainActivity extends AppCompatActivity  {

    private RealmResults <Projects> projectsR;

    private final String LOG = "###MY_DB_LOG###";

    private View parent_view; //ссылка на главный xml
    private RecyclerView mRecyclerView; //объект типа RecyclerView
    private RecyclerView.Adapter mAdapter; //адаптер преобразования из внутренней формы, в компоненты View
    private RecyclerView.LayoutManager mLayoutManager; //менеджер управления
    public Context ctx;

    private String newnameP;
    private String additionalInfo;


    public Intent intent;
    private int deletePosition;



    private ArrayList<Projects> projectList; //список проектов
    private Realm mRealmProjects; //база данных

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        parent_view = findViewById(android.R.id.content); //Это идентификатор, который вручную прописал в xml у лэйера

        projectList = new ArrayList<>();
        intent = new Intent(this, projectsActivity.class);

        mRealmProjects.init(getApplicationContext());
        mRealmProjects = Realm.getDefaultInstance();




        /*
        Тулбар
         */
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarMainActivity);
        toolbar.setTitle("Story Teller");
        setSupportActionBar(toolbar);





        initComponent();
    }


    private void initComponent() {

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setHasFixedSize(true); //сообщаем RecyclerView, что мы заранее знаем размером нашего списка
        ctx = getApplicationContext(); //получение текущего контекста для передачи в адаптер


        //устанавливаем менеджер линейного лайаута
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);




        if (mRealmProjects != null) {

            //project = new Projects();
            RealmResults<Projects> results = mRealmProjects.where(Projects.class).findAll();

            if (results != null) {
                for (int i = 0; i < results.size(); i++) {
                    projectList.add(results.get(i));
                }
            }
            else
                //Toast.makeText(getApplicationContext(),"Base is empty?",Toast.LENGTH_SHORT).show();
               Toasty.error(getApplicationContext(),"Base is empty?",Toast.LENGTH_SHORT,true).show();
        }
        else
           // Toast.makeText(getApplicationContext(),"Base is empty!",Toast.LENGTH_SHORT).show();
            Toasty.error(getApplicationContext(),"Base is empty?",Toast.LENGTH_SHORT,true).show();

      // mRealmProjects.close();




        /*
        Отправка данных списка в адаптер на дальнейщую визуализацию
         */
        mAdapter = new projectsAdapter(projectList, getApplicationContext()); //передали список ПРОЕКТОВ

        /*
        Действия по клику на список
        * */
        onClickElement (mAdapter, projectList, intent);

        /*
        Действия по лонг-клику на элементе списка (коллбэком сделано)
         */
        onMoreClick (mAdapter);

        /*
        Fab - обработка добавления списка.
         */
        FloatingActionButton fab = findViewById(R.id.fab_add);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View v) {

                showAddDialog();

            }

        });


        mRecyclerView.setAdapter(mAdapter);
    }

    private void onMoreClick (RecyclerView.Adapter mAdapter){
        ((projectsAdapter) mAdapter).setOnPopupListener(new projectsAdapter.OnPopupListener() {
            @Override
            public void onPopupClick(int position, View view) {
                showPopupMenu(view, position);
                deletePosition = position;
            }
        });
    }

    private void onClickElement (RecyclerView.Adapter mAdapter, final ArrayList <Projects> projectList, final Intent intent){

        ((projectsAdapter) mAdapter).setOnClickListener(new projectsAdapter.OnClickListener() {
            @Override
            public void onItemClick(projectsAdapter.MyViewHolder view, int position) {

                /*
                 отправка посылки во вторую активность и вызов этой активности
                 */


                parcel_project parcelProject = new parcel_project(
                        projectList.get(position).getProjectName(),
                        projectList.get(position).getId());

                intent.putExtra (parcel_project.class.getSimpleName(), parcelProject );
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);

                startActivity(intent);
               // MainActivity.super.finish();

            }
        });

    }
    //диалоговое окно для создания новой записи - нового проекта
    public void showAddDialog() {

        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setContentView(R.layout.add_dialog);

        Button btnCancel = (Button) dialog.findViewById(R.id.cancelButton);
        Button btnSave = (Button) dialog.findViewById(R.id.saveButton);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EditText newProjectName = (EditText) dialog.findViewById(R.id.addPRName);
                EditText et_additionalInfo = (EditText) dialog.findViewById(R.id.addPRInfo);

                newnameP = newProjectName.getText().toString().trim();
                additionalInfo = et_additionalInfo.getText().toString().trim();

               // mRealmProjects = Realm.getDefaultInstance();

                Projects results = mRealmProjects.where(Projects.class).equalTo("projectName", newnameP).findFirst();


                if (!newProjectName.getText().toString().isEmpty())
                    if (results == null) {

                        newnameP = newProjectName.getText().toString();
                        additionalInfo = et_additionalInfo.getText().toString();


                        SimpleDateFormat simpleDateFormatMain = new SimpleDateFormat("dd.MM.yyyy");
                        SimpleDateFormat simpleDateFormatTime = new SimpleDateFormat("kk:mm", Locale.UK);

                        Projects prNew = new Projects();

                        prNew.setMainInfo(additionalInfo.trim());

                        prNew.setDataCreating(simpleDateFormatMain.format(new Date()));
                        prNew.setTimeCreating(simpleDateFormatTime.format(new Date()));
                        prNew.setDataChanging(simpleDateFormatMain.format(new Date()));
                        prNew.setTimeChanging(simpleDateFormatTime.format(new Date()));


                        prNew.setProjectName(newnameP.trim());
                        prNew.setId(projectList.size() + 1);

                        //создали список персонажей для данного проекта
                        prNew.characters = new RealmList<>();

                        projectList.add(prNew);


                        mRealmProjects.beginTransaction();
                        mRealmProjects.insertOrUpdate(prNew);
                        mRealmProjects.commitTransaction();



                        //    Log.i(LOG, "сохранение нового проекта");
                        //   Log.i(LOG, "size = " + projectList.size() + ", name = " + prNew.getProjectName());

                        ((projectsAdapter) mAdapter).mnotifyData(projectList);

                        dialog.dismiss();
                    } else
                       // Toast.makeText(getApplicationContext(), "Проект с таким названием уже существует", Toast.LENGTH_SHORT).show();
                Toasty.error(getApplicationContext(),"Проект с таким названием уже существует",Toast.LENGTH_SHORT,true).show();


                else {
                    /*Toast toastNewName = Toast.makeText(getApplicationContext(), "Проект не может быть пустым! Введите имя или нажмите кнопку 'Отмена'", Toast.LENGTH_SHORT);
                    toastNewName.setGravity(Gravity.CENTER|Gravity.CENTER_HORIZONTAL, 0, 0);
                    toastNewName.show();*/
                    Toasty.warning(getApplicationContext(),"Проект не может быть пустым! Введите имя или нажмите кнопку 'Отмена'",Toast.LENGTH_SHORT,true).show();

                }
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });



       // mRealmProjects.close();
        dialog.show();

    }

    @Override
    protected void onDestroy (){
        super.onDestroy();
        Log.i("LOG","Close DB in onDestroy in mainActivity");
        mRealmProjects.close();
    }

    private void showPopupMenu (View v, final int position){
        PopupMenu popupMenu = new PopupMenu(this, v);
        popupMenu.inflate(R.menu.popupmenu1);
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.menu_delete:
                        showAlertOfDeleteDialog();
                        return true;
                    case R.id.menu_open:

                        parcel_project parcelProject = new parcel_project(
                                projectList.get(position).getProjectName(), position);

                        intent.putExtra (parcel_project.class.getSimpleName(), parcelProject );

                        startActivity(intent);
                        MainActivity.super.finish();

                        return true;
                    case R.id.menu_rename:

                        showRenameDialog (position);
                        return true;
                    default:
                        return false;
                }
            }
        });

        popupMenu.show();
    }

    private  void showRenameDialog (final int position){
        final Dialog dialog = new Dialog(this);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setContentView(R.layout.rename_project);

        Button btnNoRename =  dialog.findViewById(R.id.cancelButtonRename);
        Button btnRename =  dialog.findViewById(R.id.saveButtonRename);

        //final TextView oldName = dialog.findViewById(R.id.tvOldName);
        final EditText newName = dialog.findViewById(R.id.addNewName);

        // oldName.setText(projectList.get(position).getProjectName());
         newName.setText(projectList.get(position).getProjectName());
         newName.setSelection(newName.getText().length());

        btnNoRename.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        btnRename.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String tmp_newname = newName.getText().toString().trim();
                String tmp_oldname = projectList.get(position).getProjectName();

                if (tmp_newname.length() == 0)
                {
                   // Toast.makeText(getApplicationContext(),"Имя не может быть пустым!", Toast.LENGTH_SHORT).show();
                    Toasty.warning(getApplicationContext(),"Имя не может быть пустым!",Toast.LENGTH_SHORT,true).show();
                }
                else {

                    Projects results = mRealmProjects.where(Projects.class).equalTo("projectName", tmp_oldname).findFirst(); //ищем первоначальное имя в базе

                    Projects result_dublicate = mRealmProjects.where(Projects.class).equalTo("projectName", tmp_newname).findFirst(); //ищем, есть ли уже в базе проект с таким же именем

                    if (result_dublicate == null) {

                        SimpleDateFormat simpleDateFormatMain = new SimpleDateFormat("dd.MM.yyyy");
                        SimpleDateFormat simpleDateFormatTime = new SimpleDateFormat("kk:mm", Locale.UK);


                        mRealmProjects.beginTransaction();


                        results.setProjectName(tmp_newname);
                        results.setDataChanging(simpleDateFormatMain.format(new Date()));
                        results.setTimeChanging(simpleDateFormatTime.format(new Date()));

                        //results.setMainInfo(prInfo);
                        mRealmProjects.copyToRealmOrUpdate(results);
                        mRealmProjects.commitTransaction();

                        projectList.clear();

                        RealmResults<Projects> results2 = mRealmProjects.where(Projects.class).findAll();

                        if (results2 != null) {

                            for (int i = 0; i < results2.size(); i++) {
                                projectList.add(results2.get(i));
                                Log.i(LOG, "/rename2/. results2.size = " + results2.size() );

                            }

                            ((projectsAdapter) mAdapter).mnotifyData(projectList);
                            dialog.dismiss();
                        }

                       // mRealmProjects.close();
                    }
                    else {
                        /*Toast toastRename = Toast.makeText(getApplicationContext(), "Проект с таким названием уже существует!", Toast.LENGTH_SHORT);
                        toastRename.setGravity(Gravity.CENTER|Gravity.CENTER_HORIZONTAL, 0, 0);
                        toastRename.show();*/
                        Toasty.error(getApplicationContext(),"Проект с таким названием уже существует!",Toast.LENGTH_SHORT,true).show();
                    }
                }
            }
        });

        //mRealmProjects.close();
        dialog.show();
    }
    private void showAlertOfDeleteDialog() {

        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setContentView(R.layout.delete_dialog);


        Button btnNo = (Button) dialog.findViewById(R.id.noButton);
        Button btnDel = (Button) dialog.findViewById(R.id.delButton);

        btnNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        btnDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                deleteRowRealm(projectList.get(deletePosition).getProjectName(), mRealmProjects);
                projectList.remove(deletePosition);

                ((projectsAdapter) mAdapter).mnotifyData(projectList);

                dialog.dismiss();
            }
        });

        dialog.show();
    }


    private void deleteRowRealm (String name, Realm mRealmProjects){

        Projects results = mRealmProjects.where(Projects.class).equalTo("projectName", name).findFirst();


        mRealmProjects.beginTransaction();
        results.deleteFromRealm();
        mRealmProjects.commitTransaction();

    }

    @Override
    public void onBackPressed(){

       // mRealmProjects.close();
        finish();

    }

    @Override
    public void onResume(){
        super.onResume();

    }


}


