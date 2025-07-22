package playerAndroid.app.display.dialogs.visual_editor.recs.utils;

import playerAndroid.app.display.dialogs.visual_editor.recs.codecompletion.domain.model.Instance;
import playerAndroid.app.display.dialogs.visual_editor.recs.interfaces.utils.BucketSortComparator;

public class MultiplicityBucketSortComparator implements BucketSortComparator<Pair<Instance,Integer>> {
    @Override
    public int getKey(Pair<Instance, Integer> p) {
        int multiplicity = p.getR().getMultiplicity();
        return multiplicity;
    }
}
