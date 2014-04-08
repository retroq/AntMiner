package antminer.rulediscover.graph;

import antminer.rulediscover.*;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.*;
/**
 * Date: 08.04.14
 *
 * @author sipachev_ai
 */
public class ConstructionGraphImplTest {
    @Test
    public void testInit() throws Exception {
        ConstructionGraph graph = new ConstructionGraphImpl();
        List<Domain> fixture = new LinkedList<Domain>();
        fixture.add(getDomain(new String[][]{{"x", "1"}, {"y", "4"}}, "c1"));
        fixture.add(getDomain(new String[][]{{"x", "1"}, {"y", "4"}}, "c1"));
        fixture.add(getDomain(new String[][]{{"x", "2"}, {"y", "5"}}, "c1"));
        fixture.add(getDomain(new String[][]{{"x", "3"}, {"y", "6"}}, "c2"));
        fixture.add(getDomain(new String[][]{{"x", "4"}, {"y", "7"}}, "c2"));
        List<DomainAttribute> attributes = Arrays.<DomainAttribute>asList(new SimpleDomainAttribute("x"), new SimpleDomainAttribute("y"));
        graph.init(attributes, fixture);
        graph.generateRule();
    }

    private Domain getDomain(String [][]attributes, String domainClass){
        HashMap<DomainAttribute, DomainValue> map = new HashMap<DomainAttribute, DomainValue>();

        for (int i = 0; i < attributes.length; i++) {
            map.put(new SimpleDomainAttribute(attributes[i][0]), new SimpleStringDomainValue(attributes[i][1]));
        }
         Domain domain = new SimpleDomain(new SimpleDomainClass(domainClass), map);
        return domain;
    }
}
