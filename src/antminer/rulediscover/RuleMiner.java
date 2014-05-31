package antminer.rulediscover;

import java.util.Collection;
import java.util.List;

/**
 * Date: 08.04.14
 *
 * @author sipachev_ai
 */
public interface RuleMiner {
    public ClassificationRule extractRules(Collection<Domain> domains);

}
