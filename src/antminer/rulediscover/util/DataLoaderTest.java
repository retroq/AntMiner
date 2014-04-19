package antminer.rulediscover.util;

import antminer.rulediscover.Domain;
import org.junit.Test;

import java.util.List;

/**
 * Date: 18.04.14
 * Time: 23:38
 *
 * @author anton
 */
public class DataLoaderTest {
    @Test
    public void testLoadDataFromArff() throws Exception {
        List<Domain> domains = DataLoader.getInstance().loadDataFromArff("/home/anton/IdeaProjects/AntMiner/AntMiner/src/antminer/rulediscover/util/testData.arff");
        System.out.println(domains.size());
        for (Domain o : domains) {
            System.out.println(o);
        }
    }
}
