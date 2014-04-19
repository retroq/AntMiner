package antminer.rulediscover.util;

import antminer.rulediscover.*;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ArffLoader;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Date: 18.04.14
 * Time: 23:21
 *
 * @author anton
 */
public class DataLoader {

    private DataLoader() {
    }

    private static class LazyHolder {
        public static final DataLoader INSTANCE = new DataLoader();
    }

    public static DataLoader getInstance() {
        return LazyHolder.INSTANCE;
    }

    public List<Domain> loadDataFromArff(String arffFileName) throws Exception {
        try {
            ArffLoader loader = new ArffLoader();
            loader.setFile(new File(arffFileName));
            Instances structure = loader.getStructure();


            Instance current;
            List<Domain> domains = new ArrayList<Domain>(structure.numAttributes());

            while ((current = loader.getNextInstance(structure)) != null) {

                HashMap<DomainAttribute, DomainValue>
                        attributeDomainValueHashMap = new HashMap<DomainAttribute, DomainValue>();

                for (int i = 0; i < current.numAttributes() - 1; i++) {
                    attributeDomainValueHashMap.put(
                            new SimpleDomainAttribute(current.attribute(i).name()),
                            new SimpleStringDomainValue(current.stringValue(i)));
                }
                DomainClass domainClass = new SimpleDomainClass(
                        current.stringValue(
                                current.attribute(
                                        structure.numAttributes() - 1
                                )
                        )
                );

                domains.add(new SimpleDomain(domainClass, attributeDomainValueHashMap));
            }
            return domains;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
