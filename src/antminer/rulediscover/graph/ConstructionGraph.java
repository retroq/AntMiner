package antminer.rulediscover.graph;

import antminer.rulediscover.ClassificationRule;import antminer.rulediscover.Domain;
import antminer.rulediscover.DomainAttribute;

import java.util.Collection;
import java.util.List;

/**
 * Date: 08.04.14
 *
 * @author sipachev_ai
 */
public interface ConstructionGraph {
    public void init(List<DomainAttribute> attributes, Collection<Domain> domains);
    public ClassificationRule generateRule();

    public void updateProbabilities(ClassificationRule rule);
}
