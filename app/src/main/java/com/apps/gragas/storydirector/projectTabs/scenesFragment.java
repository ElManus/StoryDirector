package com.apps.gragas.storydirector.projectTabs;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
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
import android.widget.Toast;

import com.apps.gragas.storydirector.Implements.Projects;
import com.apps.gragas.storydirector.Implements.Scenes;
import com.apps.gragas.storydirector.R;
import com.apps.gragas.storydirector.SceneActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import io.realm.Realm;
import io.realm.RealmList;

public class scenesFragment extends Fragment {


    private static final int MENU_CONTEXT_DELETE_ID = 1;
    private static final int MENU_CONTEXT_OPEN_ID = 0;

    private String DELETE_TEXT = "Удалить";
    private String OPEN_TEXT = "Открыть";

    private Intent intent;

    private ArrayAdapter <String> adapter_scenesList;
    private ArrayList <String> sceneList;

    private String t_prName;
    private ArrayList <String> temp_projects; //переменная списка для получения пакета из активности проектов

    private Realm mRealmProjects; //база данных

    private FloatingActionButton fab; //плавающая кнопка

    private ListView sceneListView;

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mRealmProjects.init(getContext());
        mRealmProjects = Realm.getDefaultInstance();

        setHasOptionsMenu(true); //включаем поддержку меню для тулбара.

        intent = new Intent(getActivity(), SceneActivity.class);
    }

    @Override public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                                       Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.scenes_fragment, container, false);
        sceneListView = view.findViewById(R.id.lv_scenes_frag);
        sceneList = new ArrayList<>();

        //определяем стандартный адаптер
        adapter_scenesList = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, sceneList);

        //посылаем данные в адаптер
        sceneListView.setAdapter(adapter_scenesList);

        //регистрируем меню для списка
        registerForContextMenu(sceneListView);


        //определение плавающей кнопки
        fab = view.findViewById(R.id.fab_scenes_add);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openScene(t_prName, "", true);
            }
        });

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        final View view = getView(); //Метод getView() получает корневой объект View фрагмента.
        // Далее полученный объект используется для получения ссылок на надписи

        if (view != null) {



            //получаем данные из пакета от активности проектов
            temp_projects = getArguments().getStringArrayList("bundle");
            t_prName = temp_projects.get(0); //вытаскиваем имя

            //РАБОТА С БАЗОЙ
            Projects resultsFindProject = mRealmProjects.where(Projects.class).equalTo("projectName", t_prName).findFirst();
            RealmList<Scenes> tmp1 = resultsFindProject.getScenes();

            if(tmp1!=null){
                sceneList.clear();
                for (int i = 0; i<tmp1.size(); i ++){
                    sceneList.add(tmp1.get(i).getSceneName());
                }
                adapter_scenesList.notifyDataSetChanged();
                sceneListView.setAdapter(adapter_scenesList);
            }


            sceneListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    openScene(t_prName, sceneList.get(position), false);
                }
            });
        }
    }


    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {

        super.onCreateContextMenu(menu, v, menuInfo);

        menu.add(Menu.NONE, MENU_CONTEXT_OPEN_ID, Menu.NONE, OPEN_TEXT);
        menu.add(Menu.NONE, MENU_CONTEXT_DELETE_ID, Menu.NONE, DELETE_TEXT);

    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if (getUserVisibleHint()) {
            AdapterView.AdapterContextMenuInfo info2 = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
            switch (item.getItemId()) {
                case MENU_CONTEXT_OPEN_ID:

                     Toast.makeText(getActivity(), sceneList.get(info2.position),Toast.LENGTH_SHORT).show();
                    return true;
                case MENU_CONTEXT_DELETE_ID:
                      showAlertOfDeleteDialog(info2.position);
            }

            return true;
        }
        return false;
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

                results.scenes.remove(position);
                mRealmProjects.commitTransaction();
                sceneList.remove(position);
                adapter_scenesList.notifyDataSetChanged();
                sceneListView.setAdapter(adapter_scenesList);

                Toast.makeText(getActivity(), Integer.toString(position),Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void openScene(String t_prName, String nameItemClicked, boolean add_not_change){
        intent.putExtra("PROJECT_NAME", t_prName);
        intent.putExtra("SCENE_NAME", nameItemClicked);
        intent.putExtra("ADD_NOTCHANGE", add_not_change);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
    }
}
