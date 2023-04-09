package at.ac.fhcampuswien.fhmdb.models;

public enum ReleaseYear {
    YEAR_2023(2023),
    YEAR_2022(2022),
    YEAR_2021(2021),
    YEAR_2019(2019),
    YEAR_2018(2018),
    YEAR_2017(2017),
    YEAR_2016(2016),
    YEAR_2015(2015),
    YEAR_2014(2014),
    YEAR_2013(2013),
    YEAR_2012(2012),
    YEAR_2011(2011),
    YEAR_2010(2010),
    YEAR_2009(2009),
    YEAR_2008(2008),
    YEAR_2007(2007),
    YEAR_2006(2006),
    YEAR_2005(2005),
    YEAR_2004(2004),
    YEAR_2003(2003),
    YEAR_2002(2002),
    YEAR_2001(2001),
    YEAR_2000(2000),
    YEAR_1999(1999),
    YEAR_1998(1998),
    YEAR_1997(1997);
    // Add more years as needed

    private final int year;

    ReleaseYear(int year) {
        this.year = year;
    }

    public int getYear() {
        return year;


    }
    }

