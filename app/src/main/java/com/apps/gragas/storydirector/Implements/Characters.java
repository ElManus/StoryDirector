package com.apps.gragas.storydirector.Implements;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

public class Characters extends RealmObject {

    @PrimaryKey
    private String characterName;
    private int id;
    @Required
    private String characterSex;
    private int heroOld;
    private String heroPhysics;
    private String heroLook;
    private String heroCreed;
    private String heroLookHimself;
    private String leisureSelf;
    private String hobbie;
    private String sports;
    private String parents_0_10;
    private String persona_0_10;
    private String school_friends_0_10;
    private String interests_10_18;
    private String school_10_18;
    private String relationship_10_18;
    private String parents_elders_10_18;
    private String job_10_18;
    private String events_10_18;
    private String interests_18_25;
    private String relationship_18_25;
    private String studing_18_25;
    private String parents_18_25;
    private String events_18_25;
    private String job_colleges;
    private String job_boss;
    private String job_what;
    private String job_pleasure;
    private String family;
    private String startRelationship;
    private String nowRelationship;
    private String leisure;
    private String adulter;
    private String conflicts;
    private String role;
    private String themeOrNot;




    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return characterName;
    }

    public void setName(String name) {
        this.characterName = name;
    }


    public String getCharacterSex() {
        return characterSex;
    }

    public void setCharacterSex(String characterSex) {
        this.characterSex = characterSex;
    }

    public int getHeroOld() {
        return heroOld;
    }

    public void setHeroOld(int heroOld) {
        this.heroOld = heroOld;
    }

    public String getHeroPhysics() {
        return heroPhysics;
    }

    public void setHeroPhysics(String heroPhysics) {
        this.heroPhysics = heroPhysics;
    }

    public String getHeroLook() {
        return heroLook;
    }

    public void setHeroLook(String heroLook) {
        this.heroLook = heroLook;
    }

    public String getHeroCreed() {
        return heroCreed;
    }

    public void setHeroCreed(String heroCreed) {
        this.heroCreed = heroCreed;
    }

    public String getHeroLookHimself() {
        return heroLookHimself;
    }

    public void setHeroLookHimself(String heroLookHimself) {
        this.heroLookHimself = heroLookHimself;
    }

    public String getLeisureSelf() {
        return leisureSelf;
    }

    public void setLeisureSelf(String leisureSelf) {
        this.leisureSelf = leisureSelf;
    }

    public String getHobbie() {
        return hobbie;
    }

    public void setHobbie(String hobbie) {
        this.hobbie = hobbie;
    }

    public String getSports() {
        return sports;
    }

    public void setSports(String sports) {
        this.sports = sports;
    }

    public String getParents_0_10() {
        return parents_0_10;
    }

    public void setParents_0_10(String parents_0_10) {
        this.parents_0_10 = parents_0_10;
    }

    public String getPersona_0_10() {
        return persona_0_10;
    }

    public void setPersona_0_10(String persona_0_10) {
        this.persona_0_10 = persona_0_10;
    }

    public String getSchool_friends_0_10() {
        return school_friends_0_10;
    }

    public void setSchool_friends_0_10(String school_friends_0_10) {
        this.school_friends_0_10 = school_friends_0_10;
    }

    public String getInterests_10_18() {
        return interests_10_18;
    }

    public void setInterests_10_18(String interests_10_18) {
        this.interests_10_18 = interests_10_18;
    }

    public String getSchool_10_18() {
        return school_10_18;
    }

    public void setSchool_10_18(String school_10_18) {
        this.school_10_18 = school_10_18;
    }

    public String getRelationship_10_18() {
        return relationship_10_18;
    }

    public void setRelationship_10_18(String relationship_10_18) {
        this.relationship_10_18 = relationship_10_18;
    }

    public String getParents_elders_10_18() {
        return parents_elders_10_18;
    }

    public void setParents_elders_10_18(String parents_elders_10_18) {
        this.parents_elders_10_18 = parents_elders_10_18;
    }

    public String getJob_10_18() {
        return job_10_18;
    }

    public void setJob_10_18(String job_10_18) {
        this.job_10_18 = job_10_18;
    }

    public String getEvents_10_18() {
        return events_10_18;
    }

    public void setEvents_10_18(String events_10_18) {
        this.events_10_18 = events_10_18;
    }

    public String getInterests_18_25() {
        return interests_18_25;
    }

    public void setInterests_18_25(String interests_18_25) {
        this.interests_18_25 = interests_18_25;
    }

    public String getRelationship_18_25() {
        return relationship_18_25;
    }

    public void setRelationship_18_25(String relationship_18_25) {
        this.relationship_18_25 = relationship_18_25;
    }

    public String getStuding_18_25() {
        return studing_18_25;
    }

    public void setStuding_18_25(String studing_18_25) {
        this.studing_18_25 = studing_18_25;
    }

    public String getParents_18_25() {
        return parents_18_25;
    }

    public void setParents_18_25(String parents_18_25) {
        this.parents_18_25 = parents_18_25;
    }

    public String getEvents_18_25() {
        return events_18_25;
    }

    public void setEvents_18_25(String events_18_25) {
        this.events_18_25 = events_18_25;
    }

    public String getJob_colleges() {
        return job_colleges;
    }

    public void setJob_colleges(String job_colleges) {
        this.job_colleges = job_colleges;
    }

    public String getJob_boss() {
        return job_boss;
    }

    public void setJob_boss(String job_boss) {
        this.job_boss = job_boss;
    }

    public String getJob_what() {
        return job_what;
    }

    public void setJob_what(String job_what) {
        this.job_what = job_what;
    }

    public String getJob_pleasure() {
        return job_pleasure;
    }

    public void setJob_pleasure(String job_pleasure) {
        this.job_pleasure = job_pleasure;
    }

    public String getFamily() {
        return family;
    }

    public void setFamily(String family) {
        this.family = family;
    }

    public String getStartRelationship() {
        return startRelationship;
    }

    public void setStartRelationship(String startRelationship) {
        this.startRelationship = startRelationship;
    }

    public String getNowRelationship() {
        return nowRelationship;
    }

    public void setNowRelationship(String nowRelationship) {
        this.nowRelationship = nowRelationship;
    }

    public String getLeisure() {
        return leisure;
    }

    public void setLeisure(String leisure) {
        this.leisure = leisure;
    }

    public String getAdulter() {
        return adulter;
    }

    public void setAdulter(String adulter) {
        this.adulter = adulter;
    }

    public String getConflicts() {
        return conflicts;
    }

    public void setConflicts(String conflicts) {
        this.conflicts = conflicts;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }


    public String getThemeOrNot() {
        return themeOrNot;
    }

    public void setThemeOrNot(String themeOrNot) {
        this.themeOrNot = themeOrNot;
    }
}


