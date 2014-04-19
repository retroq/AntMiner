package antminer.rulediscover.graph;

import antminer.rulediscover.*;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * Date: 08.04.14
 *
 * @author sipachev_ai
 */
public class ConstructionGraphImplTest {
    List<Domain> fixture = new LinkedList<Domain>();
    List<DomainAttribute> attributes;
    ConstructionGraph graph;


    @Before
    public void setUp() {
        fixture.add(getDomain(new String[][]{{"Condition", "Excellent"}, {"Safety", "Bad"}}, "Buy"));
        fixture.add(getDomain(new String[][]{{"Condition", "Very Good"}, {"Safety", "Very Good"}}, "Buy"));
        fixture.add(getDomain(new String[][]{{"Condition", "Good"}, {"Safety", "Good"}}, "Buy"));
        fixture.add(getDomain(new String[][]{{"Condition", "Good"}, {"Safety", "Very Good"}}, "Buy"));
        fixture.add(getDomain(new String[][]{{"Condition", "Bad"}, {"Safety", "Very Good"}}, "Wait"));
        fixture.add(getDomain(new String[][]{{"Condition", "Bad"}, {"Safety", "Very Good"}}, "Wait"));
        fixture.add(getDomain(new String[][]{{"Condition", "Bad"}, {"Safety", "Good"}}, "Not Buy"));
        fixture.add(getDomain(new String[][]{{"Condition", "Bad"}, {"Safety", "Bad"}}, "Not Buy"));
        attributes = Arrays.<DomainAttribute>asList(
                new SimpleDomainAttribute("Condition"),
                new SimpleDomainAttribute("Safety")
        );
        graph = new ConstructionGraphImpl();
        graph.init(fixture);
    }

    @Test
    public void testInit() throws Exception {
        //todo implement
    }

    @Test
    public void testGenerateRule() {
        ClassificationRule rule = graph.generateRule();
        for (int i = 0; i < 100; i++) {
            rule = graph.generateRule();
            rule.setMostFrequentClass(fixture);
            System.out.println(rule);
            System.out.println(rule.getQuality(fixture, fixture.size()));
        }


    }

    private Domain getDomain(String[][] attributes, String domainClass) {
        HashMap<DomainAttribute, DomainValue> map = new HashMap<DomainAttribute, DomainValue>();

        for (int i = 0; i < attributes.length; i++) {
            map.put(new SimpleDomainAttribute(attributes[i][0]), new SimpleStringDomainValue(attributes[i][1]));
        }
        Domain domain = new SimpleDomain(new SimpleDomainClass(domainClass), map);
        return domain;
    }
}
