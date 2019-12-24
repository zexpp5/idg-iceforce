package newProject.company.project_manager.investment_project.utils;

import android.graphics.Color;

import com.injoy.idg.R;

/**
 * Created by zsz on 2019/4/29.
 */

public class WordColorUtils {
    public static int getWordBackgroundColorId(String word){
        int colorId = R.color.top_bg;
        if (word.equals("A")){
            colorId = R.color.red;
        }else if (word.equals("G")){
            colorId = R.color.green;
        }else if (word.equals("W")){
            colorId = R.color.orange;
        }else if (word.equals("E")){
            colorId = R.color.blue;
        }
        return colorId;
    }
}
