package mobileos.usna.edu;

import android.widget.ImageView;

import java.io.Serializable;

/**
 * Author: MIDN Hitoshi Oue
 * Date: April 24, 2019
 * Description: this is the class responsible for storing island information from the
 *              database. this class is designed to take in information such as
 *              island name, population, history, languages, images, etc. a custom object
 */
public class IslandInfo implements Serializable {
    //below are all the needed UIs and global variables
    private String country;
    private String greeting;
    private String history;
    private String flag;
    private String map;
    private int population;
    private String languages;
    private String facts;
    private String culture;
    private String pic1;
    private String pic2;
    private String pic3;

    /**
     * a constructor to easily inialize an IslandInfo object
     * @param country
     * @param greeting
     * @param history
     * @param flag
     * @param map
     * @param population
     * @param languages
     * @param facts
     * @param culture
     * @param pic1
     * @param pic2
     * @param pic3
     */
    public IslandInfo(String country, String greeting, String history, String flag, String map, int population, String languages, String facts, String culture, String pic1, String pic2, String pic3) {
        this.country = country;
        this.greeting = greeting;
        this.history = history;
        this.flag = flag;
        this.map = map;
        this.population = population;
        this.languages = languages;
        this.facts = facts;
        this.culture = culture;
        this.pic1 = pic1;
        this.pic2 = pic2;
        this.pic3 = pic3;
    }

    /**
     * BELOW ARE ALL GETTERS AND SETTERS FOR THE CLASS
     * @return
     */
    public String getCountry() {
        return country;
    }

    public String getGreeting() {
        return greeting;
    }

    public String getHistory() {
        return history;
    }

    public String getFlag() {
        return flag;
    }

    public String getMap() {
        return map;
    }

    public int getPopulation() {
        return population;
    }

    public String getLanguages() {
        return languages;
    }

    public String getFacts() {
        return facts;
    }

    public String getCulture() {
        return culture;
    }

    public String getPic1() {
        return pic1;
    }

    public String getPic2() {
        return pic2;
    }

    public String getPic3() {
        return pic3;
    }
}
