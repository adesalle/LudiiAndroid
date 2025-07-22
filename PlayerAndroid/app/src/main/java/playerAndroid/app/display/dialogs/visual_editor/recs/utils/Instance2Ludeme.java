package playerAndroid.app.display.dialogs.visual_editor.recs.utils;


import java.util.ArrayList;
import java.util.List;

import playerAndroid.app.display.dialogs.visual_editor.recs.codecompletion.Ludeme;
import playerAndroid.app.display.dialogs.visual_editor.recs.codecompletion.domain.model.Instance;

public class Instance2Ludeme {
    /**
     * This method converts a Ngram instance into a ludeme object
     * @param instance
     */
    public static Ludeme instance2ludeme(Instance instance) {
        return new Ludeme(instance.getPrediction());
    }

    public static List<Ludeme> foreachInstance2ludeme(List<Instance> instances) {
        List<Ludeme> ludemes = new ArrayList<>();
        for(Instance instance : instances) {
            if(instance != null) {
                ludemes.add(instance2ludeme(instance));
            }
        }
        return ludemes;
    }
}
