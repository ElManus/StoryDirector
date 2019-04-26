package com.apps.gragas.storydirector;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.apps.gragas.storydirector.projectTabs.scenesFragment;
import com.google.android.material.tabs.TabLayout;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.appcompat.app.AppCompatActivity;
import es.dmoral.toasty.Toasty;

import com.apps.gragas.storydirector.Tools.CustomViewPager;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.Toast;

import com.apps.gragas.storydirector.Parcels.parcel_project;

import com.apps.gragas.storydirector.projectTabs.FragmentPageAdapter;
import com.apps.gragas.storydirector.projectTabs.charactersFragment;
import com.apps.gragas.storydirector.projectTabs.mainFragment;
import com.apps.gragas.storydirector.Tools.TabLayoutUtils;

import java.util.ArrayList;


public class projectsActivity extends AppCompatActivity implements mainFragment.CallBackGetFlagEdit, charactersFragment.CallbackListItemAdded { //дописываем интерфейс коллбэка и внизу переопределяем метод
    private final String LOG = "###MY_DB_LOG###";

    private CustomViewPager viewPager;
    private TabLayout tabLayout;

    private String name;
    private String info;
    private String hero;
    private String goal;
    private String dateChange;
    private String timeChange;


    private FragmentManager manager;


    private ArrayList <String> bundleToFragment;
    private Bundle bundle;
    public Intent intent;

    private String firstName, firstInfo;

    private String fragmentTagMainFrag;
    private String fragmentTagCharFrag;
    private String fragmentTagScenesFrag;

    static boolean flagEdit; //флаг изменения во фрагменте


    public String prName; //новое имя проекта
    public String prInfo; //новая информация
    private int position; //позиция в списке выбранного проекта


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_projects);





        flagEdit = false;
       // Toast.makeText(getApplicationContext()," 0. flag = " + flagEdit, Toast.LENGTH_SHORT).show();
        intent = new Intent(this, MainActivity.class);
        bundleToFragment = new ArrayList<>();


        viewPager = (CustomViewPager) findViewById(R.id.viewpager);

        FragmentPageAdapter adapter = new FragmentPageAdapter(getSupportFragmentManager());

        /*
        получение "посылки" от MainActivity
         */
        Bundle args = getIntent().getExtras();

        final parcel_project parcelProjectIn;

        if (args != null)
        {
            parcelProjectIn=args.getParcelable(parcel_project.class.getSimpleName());
            name = parcelProjectIn.getProjectName();
            position = parcelProjectIn.getPosition();

            firstInfo = info;
            firstName = name;


          /*
          отправляем пакет во фрагмент
           */
            bundle = new Bundle();
            bundleToFragment.add(name);
            bundle.putStringArrayList("bundle", bundleToFragment);
        }


         /*
        создаем фрагменты - вкладки
         */
        adapter.addFragment(new mainFragment(),"Общее", bundle);
        adapter.addFragment(new charactersFragment(), "Персонажи", bundle);
        adapter.addFragment(new scenesFragment(), "Сцены", bundle);

        //для первого фрагмента mainFragment получаем имя фрагмента, которое потом будет использоваться для вызова функции фрагмента из данной активности. 0 - номер фрагмента в списке
        fragmentTagMainFrag = makeFragmentName(viewPager.getId(),0);

        //для второго фрагмента charactersFragment получаем имя фрагмента как тэг
        fragmentTagCharFrag = makeFragmentName(viewPager.getId(), 1);

        //для третьего фрагмента scenesFragment получаем имя фрагмента как тэг
        fragmentTagScenesFrag = makeFragmentName(viewPager.getId(), 2);

        /*

         */

        viewPager.setAdapter(adapter);

        /*
        Создаем "листатель"
         */
        tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setTabMode(TabLayout.MODE_FIXED);


           /*
        Toolbar
         */
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarProjects);
        //toolbar.setNavigationIcon(R.drawable.ic_save_icon);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(name);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);



    }


    @Override
    public void onBackPressed(){



        //ранее через коллбэк получили данные из фрагмента - флаг. теперь проверяем на истину и ложь.
        if (flagEdit == true) {
            //редактирование было нажато? тогда спрашиваем, закрываем без сохранения?
            showAlertDialogWithoutChanges();
        }
            else {
            //если кнопка редактирования не была нажата, тогда убивается текущая активность и запускается главная активность.
            //выставляем флаги: теперь в стэке активностей останется только главная активность, которую ниже мы и вызываем.
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            projectsActivity.super.finish();
        }

        }





   // }

    public void setNewData (String name, String info)
    {
        this.prName = name;
        this.prInfo = info;

    }

    public int getPosition (){
        return position;
    }

    @Override
    public void onDestroy(){
        ((mainFragment) getSupportFragmentManager().findFragmentByTag(fragmentTagMainFrag)).onDestroy();
        super.onDestroy();

    }


    //управление табами (отключение - включение)
    public void controlTabs(boolean enabled){
        viewPager.setPagingEnabled(enabled);
        TabLayoutUtils.enableTabs(tabLayout, enabled);
    }

    //CALLBACK
    @Override
    public void onCallBack(boolean flag_edit) {
        flagEdit = flag_edit;
    }


    private void showAlertDialogWithoutChanges() {

        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setContentView(R.layout.exit_without_changes);


        Button btnNo = (Button) dialog.findViewById(R.id.noButtonWithoutChanges);
        Button btnExit = (Button) dialog.findViewById(R.id.exitButtonWithoutChanges);

        btnNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               // Toast.makeText(getApplicationContext()," 4. flag = " + flagEdit, Toast.LENGTH_SHORT).show();
                startActivity(intent);
                finish();
                dialog.dismiss();
            }
        });

        dialog.show();
    }


    @Override
    public boolean onCreateOptionsMenu (Menu menu){
        getMenuInflater().inflate(R.menu.toolbar_menu_projects, menu);
        return true;
    }

    //обработка нажатий на иконке информации в тулбаре
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.icon_share_fragment_toolbar:
            {
                Toast.makeText(this,"Share!", Toast.LENGTH_SHORT).show();
                return true;
            }
            case R.id.icon_toolbarSaveProject :
            {
                if (flagEdit == true) {
                    //вызов метода фрагмента по тэгу, который устанавливаем в начале файла при создании фрагментов
                    ((mainFragment) getSupportFragmentManager().findFragmentByTag(fragmentTagMainFrag)).saveStatement();
                    Toasty.info(getApplicationContext(),"Сохранено",Toast.LENGTH_SHORT,true).show();
                    //Toast.makeText(this, "Сохранено", Toast.LENGTH_SHORT).show();


                    //УБИРАЕМ КЛАВИАТУРУ
                    // Есть ли фокус?
                    View view = this.getCurrentFocus();
                    if (view != null) {
                        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    }
                }
               // else
                //    Toast.makeText(this,"Error with saving!", Toast.LENGTH_SHORT).show();


                return true;
            }

        }
        return super.onOptionsItemSelected(item);
    }

    //метод присваивания определенного тега определенному фрагменту в viewpager
    private static String makeFragmentName(int viewId, long id) {
        return "android:switcher:" + viewId + ":" + id;
    }

    @Override
    public void onCallBackRefresh(String newChar, String action) {
            if (action.equals("added"))
         {
            //Toast.makeText(this, "Added", Toast.LENGTH_LONG).show();
            ((mainFragment) getSupportFragmentManager().findFragmentByTag(fragmentTagMainFrag)).refreshList("add", newChar);
        }
        else
            if (action.equals("deleted")){
                Toasty.info(getApplicationContext(),"Удалено",Toast.LENGTH_SHORT,true).show();
               // Toast.makeText(this, "Deleted", Toast.LENGTH_LONG).show();
                ((mainFragment) getSupportFragmentManager().findFragmentByTag(fragmentTagMainFrag)).refreshList("delete", newChar);
            }
    }
}
