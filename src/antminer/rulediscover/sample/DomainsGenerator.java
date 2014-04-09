package antminer.rulediscover.sample;

import antminer.rulediscover.*;

import java.util.*;

/**
 * Date: 09.04.14
 * Time: 23:24
 *
 * @author anton
 */
public class DomainsGenerator {
    public static Collection<Domain> generate(String [] attributes, String[][] possibleValues, String[] classes, int size){
        List<DomainAttribute> domainAttributes = new LinkedList<DomainAttribute>();
        HashMap<DomainAttribute, Integer> attributeToIndex = new HashMap<DomainAttribute, Integer>();
        for (int i = 0; i < attributes.length; i++) {
            DomainAttribute attribute = new SimpleDomainAttribute(attributes[i]);
            domainAttributes.add(attribute);
            attributeToIndex.put(attribute, i);
        }
        HashMap<DomainAttribute, Set<DomainValue>> attributeSetHashMap = new HashMap<DomainAttribute, Set<DomainValue>>();
        for (DomainAttribute attribute : domainAttributes){
            attributeSetHashMap.put(attribute, new HashSet<DomainValue>());
        }

        for (DomainAttribute attribute : domainAttributes){
            int index = attributeToIndex.get(attribute);
            for (int i = 0; i < possibleValues[index].length; i++) {
                attributeSetHashMap.get(attribute).add(new SimpleStringDomainValue(possibleValues[index][i]));
            }
        }

        List<DomainClass> domainClasses = new ArrayList<DomainClass>(classes.length);
        for (int i = 0; i < classes.length; i++) {
            domainClasses.add(new SimpleDomainClass(classes[i]));
        }

        List<Domain> domains = new ArrayList<Domain>(size);

        for (int i = 0; i < size; i++) {
            HashMap<DomainAttribute, DomainValue> attributeToValue = new HashMap<DomainAttribute, DomainValue>();
            for (DomainAttribute attribute : domainAttributes){
                ArrayList<DomainValue> values = new ArrayList<DomainValue>(attributeSetHashMap.get(attribute));
                attributeToValue.put(attribute, values.get((int)Math.round(Math.random()*(values.size() - 1))));
            }

            Domain domain = new SimpleDomain(
                    domainClasses.get(
                            (int)Math.round(Math.random()*(domainClasses.size() - 1))
                    ),
                    attributeToValue
            );
            domains.add(domain);
        }
        return domains;
    }
}
