package com.apps.gragas.storydirector.projectTabs;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import io.realm.Realm;
import io.realm.RealmList;

import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.apps.gragas.storydirector.CharActivity;
import com.apps.gragas.storydirector.Implements.Characters;
import com.apps.gragas.storydirector.Implements.Projects;
import com.apps.gragas.storydirector.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class charactersFragment extends Fragment {

    private static final int MENU_CONTEXT_DELETE_ID = 1;
    private static final int MENU_CONTEXT_OPEN_ID = 0;
    private FloatingActionButton fab; //плавающая кнопка

    private View parent_view;
    private ListView lvChars;
    private String DELETE_TEXT = "Удалить";
    private String OPEN_TEXT = "Открыть";

    private Intent intent;



    private ArrayAdapter <String> adapter_vlChars;

    private RecyclerView mRecyclerView; //объект типа RecyclerView
    private RecyclerView.Adapter mAdapter; //адаптер преобразования из внутренней формы, в компоненты View
    private RecyclerView.LayoutManager mLayoutManager; //менеджер управления

    private Realm mRealmProjects; //база данных

    private String t_prName;

    private String nameItemClicked;

    private ArrayList <String> temp_projects; //переменная списка для получения пакета из активности проектов

    private ArrayList <String> temp_chars; //список персонажей для передачи в адаптер listview

    private CallbackListItemAdded callbackListItemAdded; //интерфейс, с помощью которого будет обновляьтся список персов в первом фрагменте через активность


    public interface CallbackListItemAdded {
        void onCallBackRefresh (String charNew, String action);
    }

    public charactersFragment () {
    }

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true); //включаем поддержку меню для тулбара.

        mRealmProjects.init(getContext());
        mRealmProjects = Realm.getDefaultInstance();

        intent = new Intent(getActivity(), CharActivity.class);



    }

    //реализация передачи данных в активность из фрагмента
    @Override
    public void onAttach(@NonNull Context context) {
        //ищем активность, к которой подключен данный фрагмент. Пробуем привести эту активность к данному типу интерфейса
        super.onAttach(context);
        try{
            callbackListItemAdded = (charactersFragment.CallbackListItemAdded) context;

        } catch (ClassCastException e){
            throw new ClassCastException(context.toString() + " must implement CallbackListItemAdded");
        }

    }


    @Override public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                                       Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.characters_fragment, container, false);
        View parent_view = view.findViewById(R.id.characters_fragment); //Это идентификатор, который вручную прописал в xml у лэйера

        //находим listView
        lvChars = view.findViewById(R.id.lv_char_frag);

        temp_chars = new ArrayList<>();


        //определяем стандартный адаптер
        adapter_vlChars = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, temp_chars);

        //посылаем данные в ListView
        lvChars.setAdapter(adapter_vlChars);
        registerForContextMenu(lvChars);


        //определение плавающей кнопки
        fab = view.findViewById(R.id.fab_characters_add);




        //FAB - обработка нажатий кнопки добавления
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //подключаемся к проекту
                Projects results = mRealmProjects.where(Projects.class).equalTo("projectName", t_prName).findFirst();

               Characters newChar = new Characters();
               /*
               тестовый персонаж
                */
                //mRealmProjects.beginTransaction();

                /************/
                /************/

               /* newChar.setId(results.characters.size()+1);
                results.characters.add(newChar);
                temp_chars.add(newChar.getName());
                adapter_vlChars.notifyDataSetChanged();
                lvChars.setAdapter(adapter_vlChars);

                mRealmProjects.insertOrUpdate(newChar);
                mRealmProjects.commitTransaction();
                */

                openChar(t_prName, "", true);

                /************/
                callbackListItemAdded.onCallBackRefresh("111","added");
            }
        });

        return view;
    }

@Override
    public void onStart(){
        super.onStart();

    final View view = getView(); //Метод getView() получает корневой объект View фрагмента.
    // Далее полученный объект используется для получения ссылок на надписи

    if(view!=null){

        //получаем данные из пакета от активности проектов
        temp_projects = getArguments().getStringArrayList("bundle");
        t_prName = temp_projects.get(0); //вытаскиваем имя



        //РАБОТА С БАЗОЙ

        Projects resultsFindProject = mRealmProjects.where(Projects.class).equalTo("projectName", t_prName).findFirst();
        RealmList<Characters> tmp1 = resultsFindProject.getCharacters();

        if (tmp1 != null) {
            temp_chars.clear();
            for (int i = 0; i < tmp1.size(); i++) {
                temp_chars.add(tmp1.get(i).getName());

            }
            adapter_vlChars.notifyDataSetChanged();
            lvChars.setAdapter(adapter_vlChars);
        }

        // adapter_vlChars.notifyDataSetChanged();
           // lvChars.setAdapter(adapter_vlChars);
      // mRealmProjects.close();


    }

    lvChars.setOnItemClickListener(new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            openChar(t_prName, temp_chars.get(position), false);
        }
    });

    lvChars.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                nameItemClicked = lvChars.getItemAtPosition(position).toString();
            return false;
        }
    });
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {

        super.onCreateContextMenu(menu, v, menuInfo);
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;

        menu.add(Menu.NONE, MENU_CONTEXT_OPEN_ID, Menu.NONE, OPEN_TEXT);
        menu.add(Menu.NONE, MENU_CONTEXT_DELETE_ID, Menu.NONE, DELETE_TEXT);

    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if (getUserVisibleHint()){
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
            switch (item.getItemId()) {
                case MENU_CONTEXT_OPEN_ID:
                    openChar(t_prName, temp_chars.get(info.position), false);
                    return true;
                case MENU_CONTEXT_DELETE_ID:
                       showAlertOfDeleteDialog(info.position);
                    return true;
            }

        }
        return false;
        }



    @Override
    public void onResume (){
        super.onResume();

    }


    public void refreshList(){


        Projects resultsFindProject = mRealmProjects.where(Projects.class).equalTo("projectName", t_prName).findFirst();
        RealmList<Characters> tmp1 = resultsFindProject.getCharacters();

        if (tmp1 != null) {
            Log.i("LOG", "==> mainFragment. From base to list. Refresh");
            for (int i = 0; i < tmp1.size(); i++) {
                Log.i("LOG", i+". " + tmp1.get(i).getName());
                temp_chars.add(tmp1.get(i).getName());

            }
            adapter_vlChars.notifyDataSetChanged();
            lvChars.setAdapter(adapter_vlChars);
        }

       // mRealmProjects.close();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mRealmProjects.close();

    }

    private void showAlertOfDeleteDialog(final int position) {

        final Dialog dialog = new Dialog(getActivity());
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

                //подключаемся к проекту

                mRealmProjects.beginTransaction();
                Projects results = mRealmProjects.where(Projects.class).equalTo("projectName", t_prName).findFirst();
                results.characters.remove(position);
                mRealmProjects.commitTransaction();
                temp_chars.remove(position);
                adapter_vlChars.notifyDataSetChanged();
                lvChars.setAdapter(adapter_vlChars);
                callbackListItemAdded.onCallBackRefresh(nameItemClicked,"deleted");

               // mRealmProjects.close();
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void openChar(String t_prName, String nameItemClicked, boolean add_not_change){
           intent.putExtra("PROJECT_NAME", t_prName);
           String name = nameItemClicked;
           intent.putExtra("PERS_NAME", name);
           intent.putExtra("ADD_NOTCHANGE", add_not_change);
           intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
           Log.i("LOG", "=> Try to start CharActivity from charFragment");
           startActivity(intent);
       }

}

