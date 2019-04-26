package com.apps.gragas.storydirector.Implements;

import java.util.ArrayList;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

public class Projects extends RealmObject {
    @PrimaryKey
    private int id;
    @Required
    private String projectName;
    private String mainInfo;
    private String mainHero;
    private String mainGoal;
    private String mainAntag;
    private String whatHappensIf;
    private String mainQuestionGlobal; //глобальная цель
    private String mainQuestionPersonal; //персональная цель
    private String mainQuestionPrivate; //личная цель
    private String mainTheme;
    private String mainContreTheme;
    private String mainSyntez;
    private String dataCreating;
    private String timeCreating;
    private String dataChanging;
    private String timeChanging;

    public RealmList <Characters> characters;
    public RealmList <Scenes> scenes;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getProjectName() {
        return projectName;
    }

    public String getMainInfo() {
        return mainInfo;
    }

    public void setMainInfo(String mainInfo) {
        this.mainInfo = mainInfo;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }


    public String getDataChanging() {
        return dataChanging;
    }

    public void setDataChanging(String dataChanging) {
        this.dataChanging = dataChanging;
    }

    public String getTimeChanging() {
        return timeChanging;
    }

    public void setTimeChanging(String timeChanging) {
        this.timeChanging = timeChanging;
    }

    public String getDataCreating() {
        return dataCreating;
    }

    public void setDataCreating(String dataCreating) {
        this.dataCreating = dataCreating;
    }

    public String getTimeCreating() {
        return timeCreating;
    }

    public void setTimeCreating(String timeCreating) {
        this.timeCreating = timeCreating;
    }

    public String getMainHero() {
        return mainHero;
    }

    public String getMainGoal() {
        return mainGoal;
    }

    public void setMainGoal(String mainGoal) {
        this.mainGoal = mainGoal;
    }

    public String getMainAntag() {
        return mainAntag;
    }

    public void setMainAntag(String mainAntag) {
        this.mainAntag = mainAntag;
    }

    public String getWhatHappensIf() {
        return whatHappensIf;
    }

    public void setWhatHappensIf(String whatHappensIf) {
        this.whatHappensIf = whatHappensIf;
    }

    public String getMainQuestionGlobal() {
        return mainQuestionGlobal;
    }

    public void setMainQuestionGlobal(String mainQuestionGlobal) {
        this.mainQuestionGlobal = mainQuestionGlobal;
    }

    public String getMainQuestionPersonal() {
        return mainQuestionPersonal;
    }

    public void setMainQuestionPersonal(String mainQuestionPersonal) {
        this.mainQuestionPersonal = mainQuestionPersonal;
    }

    public String getMainQuestionPrivate() {
        return mainQuestionPrivate;
    }

    public void setMainQuestionPrivate(String mainQuestionPrivate) {
        this.mainQuestionPrivate = mainQuestionPrivate;
    }

    public String getMainTheme() {
        return mainTheme;
    }

    public void setMainTheme(String mainTheme) {
        this.mainTheme = mainTheme;
    }

    public String getMainContreTheme() {
        return mainContreTheme;
    }

    public void setMainContreTheme(String mainContreTheme) {
        this.mainContreTheme = mainContreTheme;
    }

    public String getMainSyntez() {
        return mainSyntez;
    }

    public void setMainSyntez(String mainSyntez) {
        this.mainSyntez = mainSyntez;
    }

    public void setMainHero(String mainHero) {
        this.mainHero = mainHero;
    }

    public RealmList <Characters> getCharacters (){
            return characters;
    }


    public void setCharacters(io.realm.RealmList<Characters> characters) {
        this.characters = characters;
    }

    public RealmList <Scenes> getScenes(){
        return scenes;
    }


    public void setScenes(io.realm.RealmList<Scenes> scenes) {
        this.scenes = scenes;
    }
}
