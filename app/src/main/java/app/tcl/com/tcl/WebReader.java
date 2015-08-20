package app.tcl.com.tcl;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Created by Diego on 8/19/2015.
 */
public class WebReader {
    public static List<String> GetTitles(String MainHTML)
    {
        List<String> titlesList = new ArrayList<>();
        //String[] titlesElements = Regex.Split (MainHTML, "<h3><a href=");
        String[] titlesElements = MainHTML.split(Pattern.quote("<h3><a href="));
        String tempString;

        for (int titlesIndex = 1; titlesIndex < titlesElements.length; titlesIndex++)
        {
            //tempString = Regex.Split(titlesElements[titlesIndex],"</a></h3> ")[0];
            tempString = titlesElements[titlesIndex].split(Pattern.quote("</a></h3>"))[0];
            tempString = tempString.split(">")[1];
            //tempString = tempString.Remove(tempString.Length - 1, 1);
            titlesList.add (tempString);
        }
        return titlesList;
    }

    public static List<String> GetImages(String MainHTML)
    {
        List<String> imgList = new ArrayList<>();
        String[] imageElements = MainHTML.split(Pattern.quote("class=\"e\"><img src=\""));
        String tempString;

        for (int imagesIndex = 1; imagesIndex < imageElements.length; imagesIndex++)
        {
            tempString = imageElements[imagesIndex].split(Pattern.quote("><br><i>"))[0];
            tempString = tempString.substring(0,tempString.length() - 1);
            imgList.add(tempString);
        }
        return imgList;
    }

}

