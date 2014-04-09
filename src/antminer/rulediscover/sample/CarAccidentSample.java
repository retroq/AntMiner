package antminer.rulediscover.sample;

import antminer.rulediscover.ClassificationRule;
import antminer.rulediscover.Domain;
import antminer.rulediscover.RuleMiner;
import antminer.rulediscover.RuleMinerImpl;

import java.util.Collection;
import java.util.List;

/**
 * Date: 09.04.14
 * Time: 23:12
 *
 * @author anton
 */
public class CarAccidentSample {
    private static String[] attributes = {"Crashtest rate", "Seatbelts", "Airbag", "Speed", "Road separator", "Passengers"};
    private static String[] classes = {"No damage", "Light damage", "High damage", "Lethal"};
    private static String [][] values = {
            {"*", "**", "***", "****", "*****"},
            {"Fastened", "Not fastened"},
            {"Presented", "Not presented"},
            {"< 40", "40-60", "60-80", "> 80"},
            {"Big road separator", "Small road separator", "No separator"},
            {"1", "2-4", "5 and more"}
    };

    public static void main(String[] args) {
        Collection<Domain> domains = DomainsGenerator.generate(attributes, values, classes, 50);
        RuleMiner ruleMiner = new RuleMinerImpl();
        List<ClassificationRule> rules = ruleMiner.extractRules(domains);
        System.out.println(rules);
    }
}
